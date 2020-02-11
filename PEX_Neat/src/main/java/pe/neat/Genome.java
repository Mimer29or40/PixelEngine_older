package pe.neat;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import static pe.PixelEngine.getPath;

public class Genome
{
    public final Random random;
    
    public final HashMap<Integer, Node>       nodes       = new HashMap<>();
    public final HashMap<Integer, Connection> connections = new HashMap<>();
    
    public int    layerCount;
    public double fitness;
    
    public Genome(Random random)
    {
        this.random = random;
    }
    
    public Genome()
    {
        this(new Random());
    }
    
    public Iterable<Node> getNodes()
    {
        return this.nodes.values();
    }
    
    public Iterable<Connection> getConnections()
    {
        return this.connections.values();
    }
    
    public Node getNode(int i)
    {
        return this.nodes.get(i);
    }
    
    public Connection getConnection(int i)
    {
        return this.connections.get(i);
    }
    
    /**
     * Adds a node to the node dict with the innovation number as the key
     *
     * @param node The node to add
     */
    public Node addNode(Node node)
    {
        this.layerCount = Math.max(this.layerCount, node.layer + 1);
        this.nodes.put(node.id, node);
        return node;
    }
    
    /**
     * Adds a connection to the connection dict with the innovation number as the key
     *
     * @param connection The connection to add
     */
    public Connection addConnection(Connection connection)
    {
        this.connections.put(connection.id, connection);
        return connection;
    }
    
    /**
     * Mutates the weights for all connections in genome
     *
     * @param perturbingRate The probability to nudge the weight by an amount
     */
    public void weightMutation(double perturbingRate)
    {
        for (Connection connection : getConnections())
        {
            if (this.random.nextDouble() < perturbingRate)
            {
                connection.weight += this.random.nextGaussian();
                connection.weight = Math.max(-1.0, Math.min(connection.weight, 1.0));
            }
            else
            {
                connection.weight = this.random.nextDouble() * 2.0 - 1.0;
            }
        }
    }
    
    public void nodeMutation(Counter nodeInnovation, Counter connInnovation)
    {
        ArrayList<Connection> suitable = new ArrayList<>();
        for (Connection connection : getConnections()) if (connection.enabled) suitable.add(connection);
        
        if (suitable.isEmpty()) return;
        
        Connection con = suitable.get(this.random.nextInt(suitable.size()));
        
        con.enabled = false;
        
        Node       node = new Node(nodeInnovation.inc(), Node.Type.HIDDEN, con.in.layer + 1);
        Connection con1 = new Connection(connInnovation.inc(), con.in, node, 1.0, true);
        Connection con2 = new Connection(connInnovation.inc(), node, con.out, con.weight, true);
        
        if (node.layer == con.out.layer)
        {
            for (Node n : getNodes())
            {
                if (n.layer >= node.layer)
                {
                    n.layer++;
                    this.layerCount = Math.max(this.layerCount, n.layer + 1);
                }
            }
        }
        
        addNode(node);
        addConnection(con1);
        addConnection(con2);
    }
    
    public void connectionMutation(Counter connInnovation, int attempts)
    {
        Node            temp;
        ArrayList<Node> check = new ArrayList<>();
        ArrayList<Node> nodes = new ArrayList<>();
        
        for (int attempt = 0; attempt < attempts; attempt++)
        {
            Node n1 = this.nodes.get(this.random.nextInt(this.nodes.size()));
            Node n2 = this.nodes.get(this.random.nextInt(this.nodes.size()));
            
            if (n1.equals(n2)) continue;
            if (n1.layer == n2.layer) continue;
            if (n1.type == Node.Type.INPUT && n2.type == Node.Type.INPUT) continue;
            if (n1.type == Node.Type.INPUT && n2.type == Node.Type.BIAS) continue;
            if (n1.type == Node.Type.BIAS && n2.type == Node.Type.INPUT) continue;
            if (n1.type == Node.Type.BIAS && n2.type == Node.Type.BIAS) continue;
            if (n1.type == Node.Type.OUTPUT && n2.type == Node.Type.OUTPUT) continue;
            
            if (n1.type == Node.Type.OUTPUT || n2.type == Node.Type.INPUT || n2.type == Node.Type.BIAS)
            {
                temp = n1;
                n1   = n2;
                n2   = temp;
            }
            
            check.clear();
            nodes.clear();
            for (Connection con : getConnections())
            {
                if (con.in.equals(n2))
                {
                    check.add(con.out);
                    nodes.add(con.out);
                }
            }
            
            while (!check.isEmpty())
            {
                Node node = check.remove(0);
                for (Connection con : getConnections())
                {
                    if (con.in.equals(node))
                    {
                        check.add(con.out);
                        nodes.add(con.out);
                    }
                }
            }
            
            boolean cont = false;
            for (Node node : nodes)
            {
                if (node.equals(n1))
                {
                    cont = true;
                    break;
                }
            }
            if (cont) continue;
            
            cont = false;
            for (Connection con : getConnections())
            {
                if ((con.in.equals(n1) && con.out.equals(n2)) || (con.in.equals(n2) && con.out.equals(n1)))
                {
                    cont = true;
                    break;
                }
            }
            if (cont) continue;
            
            addConnection(new Connection(connInnovation.inc(), n1, n2, this.random.nextDouble() * 2.0 - 1.0, true));
            return;
        }
    }
    
    public Genome copy()
    {
        Genome genome = new Genome();
        for (Node node : getNodes()) genome.addNode(node);
        for (Connection con : getConnections()) genome.addConnection(con);
        return genome;
    }
    
    public void save(String fileName)
    {
        if (!fileName.endsWith(".json")) fileName += ".json";
        
        try (JsonWriter writer = new JsonWriter(new FileWriter(fileName)))
        {
            writer.setIndent("  ");
            
            writer.beginObject();
            {
                writer.name("nodes").beginArray();
                {
                    for (Node node : getNodes())
                    {
                        writer.beginObject();
                        {
                            writer.name("id").value(node.id);
                            writer.name("type").value(node.type.toString());
                            writer.name("layer").value(node.layer);
                        }
                        writer.endObject();
                    }
                }
                writer.endArray();
                
                writer.name("connections").beginArray();
                {
                    for (Connection connection : getConnections())
                    {
                        writer.beginObject();
                        {
                            writer.name("id").value(connection.id);
                            writer.name("in").value(connection.in.id);
                            writer.name("out").value(connection.out.id);
                            writer.name("weight").value(connection.weight);
                            writer.name("enabled").value(connection.enabled);
                        }
                        writer.endObject();
                    }
                }
                writer.endArray();
            }
            writer.endObject();
        }
        catch (IOException ignored)
        {
        
        }
    }
    
    public static Genome load(String fileName)
    {
        if (!fileName.endsWith(".json")) fileName += ".json";
        
        try (JsonReader reader = new JsonReader(new FileReader(getPath(fileName).toString())))
        {
            Genome genome = new Genome();
            
            reader.beginObject();
            {
                String nodes = reader.nextName();
                reader.beginArray();
                {
                    while (reader.hasNext())
                    {
                        reader.beginObject();
                        {
                            int       id    = -1;
                            Node.Type type  = null;
                            int       layer = -1;
                            while (reader.hasNext())
                            {
                                String name = reader.nextName();
                                
                                switch (name)
                                {
                                    case "id":
                                        id = reader.nextInt();
                                        break;
                                    case "type":
                                        type = Node.Type.valueOf(reader.nextString());
                                        break;
                                    case "layer":
                                        layer = reader.nextInt();
                                        break;
                                }
                            }
                            genome.addNode(new Node(id, type, layer));
                        }
                        reader.endObject();
                    }
                }
                reader.endArray();
                
                String connections = reader.nextName();
                reader.beginArray();
                {
                    while (reader.hasNext())
                    {
                        reader.beginObject();
                        {
                            int     id      = -1;
                            Node    in      = null;
                            Node    out     = null;
                            double  weight  = 0;
                            boolean enabled = false;
                            while (reader.hasNext())
                            {
                                String name = reader.nextName();
                                
                                switch (name)
                                {
                                    case "id":
                                        id = reader.nextInt();
                                        break;
                                    case "in":
                                        in = genome.getNode(reader.nextInt());
                                        break;
                                    case "out":
                                        out = genome.getNode(reader.nextInt());
                                        break;
                                    case "weight":
                                        weight = reader.nextDouble();
                                        break;
                                    case "enabled":
                                        enabled = reader.nextBoolean();
                                        break;
                                }
                            }
                            genome.addConnection(new Connection(id, in, out, weight, enabled));
                        }
                        reader.endObject();
                    }
                }
                reader.endArray();
            }
            reader.endObject();
            
            return genome;
        }
        catch (IOException ignored)
        {
        
        }
        return new Genome();
    }
    
    /**
     * Created a child genome from parent genomes.
     *
     * @param genome1                Parent genome one. The more fit parent
     * @param genome2                Parent genome two. The less fit parent
     * @param disableGeneInheritance Chance for new connection to be disabled
     * @return Child genome
     */
    public static Genome crossover(Genome genome1, Genome genome2, double disableGeneInheritance)
    {
        if (genome2.fitness > genome1.fitness)
        {
            Genome temp = genome1;
            genome1 = genome2;
            genome2 = temp;
        }
        
        Genome child = new Genome();
        
        for (Connection g1Conn : genome1.getConnections())
        {
            Connection g2Conn = genome2.getConnection(g1Conn.id);
            if (g2Conn != null)
            {
                // Matching Gene
                Connection childConn = genome1.random.nextBoolean() ? g1Conn.copy() : g2Conn.copy();
                boolean    disabled  = !(g1Conn.enabled && g2Conn.enabled);
                childConn.enabled = !(disabled && genome1.random.nextDouble() < disableGeneInheritance);
                child.addConnection(childConn);
            }
            else
            {
                // Disjointed or Excess Connection
                child.addConnection(g1Conn.copy());
            }
        }
        return child;
    }
    
    /**
     * Determines how similar two genomes are
     *
     * @param genome1 Genome one
     * @param genome2 Genome two
     * @param c1      Weight for excess genes
     * @param c2      Weight for disjoint genes
     * @param c3      Weight for average weight distance
     * @return Compatibility distance
     */
    public static double compatibilityDistance(Genome genome1, Genome genome2, double c1, double c2, double c3)
    {
        int    excess     = Genome.countExcessGenes(genome1, genome2);
        int    disjoint   = Genome.countDisjointGenes(genome1, genome2);
        double weightDiff = Genome.averageWeightDiff(genome1, genome2);
        return excess * c1 + disjoint * c2 + weightDiff * c3;
    }
    
    /**
     * Determines the number of matching connections and nodes between two genomes.
     *
     * @param genome1 Genome one
     * @param genome2 Genome two
     * @return Number of matching genes
     */
    public static int countMatchingGenes(Genome genome1, Genome genome2)
    {
        int count = 0, innovation1, innovation2;
        
        innovation1 = Collections.max(genome1.nodes.keySet());
        innovation2 = Collections.max(genome2.nodes.keySet());
        for (int i = 0, n = Math.max(innovation1, innovation2); i <= n; i++)
        {
            Node n1 = genome1.getNode(i);
            Node n2 = genome2.getNode(i);
            if (n1 != null && n2 != null) count++;
        }
        
        innovation1 = Collections.max(genome1.connections.keySet());
        innovation2 = Collections.max(genome2.connections.keySet());
        for (int i = 0, n = Math.max(innovation1, innovation2); i <= n; i++)
        {
            Connection c1 = genome1.getConnection(i);
            Connection c2 = genome2.getConnection(i);
            if (c1 != null && c2 != null) count++;
        }
        
        return count;
    }
    
    /**
     * Determines the number of excess connections and nodes between two genomes
     *
     * @param genome1 Genome one
     * @param genome2 Genome two
     * @return Number of excess genes
     */
    public static int countExcessGenes(Genome genome1, Genome genome2)
    {
        int count = 0, innovation1, innovation2;
        
        innovation1 = Collections.max(genome1.nodes.keySet());
        innovation2 = Collections.max(genome2.nodes.keySet());
        for (int i = 0, n = Math.max(innovation1, innovation2); i <= n; i++)
        {
            Node n1 = genome1.getNode(i);
            Node n2 = genome2.getNode(i);
            if ((n1 == null && innovation1 < i && n2 != null) || (n2 == null && innovation2 < i && n1 != null)) count++;
        }
        
        innovation1 = Collections.max(genome1.connections.keySet());
        innovation2 = Collections.max(genome2.connections.keySet());
        for (int i = 0, n = Math.max(innovation1, innovation2); i <= n; i++)
        {
            Connection c1 = genome1.getConnection(i);
            Connection c2 = genome2.getConnection(i);
            if ((c1 == null && innovation1 < i && c2 != null) || (c2 == null && innovation2 < i && c1 != null)) count++;
        }
        
        return count;
    }
    
    /**
     * Determines the number of disjoint connections and nodes between two genomes
     *
     * @param genome1 Genome one
     * @param genome2 Genome two
     * @return Number of disjoint genes
     */
    public static int countDisjointGenes(Genome genome1, Genome genome2)
    {
        int count = 0, innovation1, innovation2;
        
        innovation1 = Collections.max(genome1.nodes.keySet());
        innovation2 = Collections.max(genome2.nodes.keySet());
        for (int i = 0, n = Math.max(innovation1, innovation2); i <= n; i++)
        {
            Node n1 = genome1.getNode(i);
            Node n2 = genome2.getNode(i);
            if ((n1 == null && innovation1 > i && n2 != null) || (n2 == null && innovation2 > i && n1 != null)) count++;
        }
        
        innovation1 = Collections.max(genome1.connections.keySet());
        innovation2 = Collections.max(genome2.connections.keySet());
        for (int i = 0, n = Math.max(innovation1, innovation2); i <= n; i++)
        {
            Connection c1 = genome1.getConnection(i);
            Connection c2 = genome2.getConnection(i);
            if ((c1 == null && innovation1 > i && c2 != null) || (c2 == null && innovation2 > i && c1 != null)) count++;
        }
        
        return count;
    }
    
    /**
     * Determines the average weight difference between two genomes
     *
     * @param genome1 Genome one
     * @param genome2 Genome two
     * @return Average weight difference
     */
    public static double averageWeightDiff(Genome genome1, Genome genome2)
    {
        int    count      = 0, innovation1, innovation2;
        double weightDiff = 0.0;
        
        innovation1 = Collections.max(genome1.connections.keySet());
        innovation2 = Collections.max(genome2.connections.keySet());
        for (int i = 0, n = Math.max(innovation1, innovation2); i <= n; i++)
        {
            Connection c1 = genome1.getConnection(i);
            Connection c2 = genome2.getConnection(i);
            if (c1 != null && c2 != null)
            {
                count++;
                weightDiff += Math.abs(c1.weight - c2.weight);
            }
        }
        
        return count > 0 ? weightDiff / (double) count : 2.0;
    }
}
