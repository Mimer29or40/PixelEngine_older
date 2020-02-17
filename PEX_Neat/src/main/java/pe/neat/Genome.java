package pe.neat;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import pe.Random;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static pe.PixelEngine.getPath;

public class Genome
{
    // public final Random random;
    
    public final HashMap<Integer, Node>       nodes       = new HashMap<>();
    public final HashMap<Integer, Connection> connections = new HashMap<>();
    
    public final ArrayList<Node> inputs  = new ArrayList<>();
    public final ArrayList<Node> outputs = new ArrayList<>();
    public       Node            bias    = null;
    
    public final ArrayList<Node> network = new ArrayList<>();
    
    public int    layerCount;
    public double fitness;
    
    // public Genome(Random random)
    // {
    //     this.random = random;
    // }
    
    public Genome()
    {
        // this(new Random());
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
    
        switch (node.type)
        {
            case INPUT:
                this.inputs.add(node);
                break;
            case OUTPUT:
                this.outputs.add(node);
                break;
            case BIAS:
                if (this.bias != null) throw new RuntimeException("Genome can only have ONE bias Node");
                this.bias = node;
                break;
        }
    
        boolean addToEnd = true;
        for (int i = 0, n = this.network.size(); i < n; i++)
        {
            if (this.network.get(i).layer > node.layer)
            {
                this.network.add(i, node);
                addToEnd = false;
                break;
            }
        }
        if (addToEnd) this.network.add(node);
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
        getNode(connection.in).outputConnections.add(connection);
        return connection;
    }
    
    /**
     * Calculates the genome based on the inputs
     *
     * @param inputs the inputs to the genome
     * @return the results
     */
    public double[] calculate(double[] inputs)
    {
        if (inputs.length != this.inputs.size()) throw new RuntimeException("Input length is not correct");
        
        this.network.forEach(Node::reset);
        
        for (int i = 0; i < inputs.length; i++)
        {
            this.inputs.get(i).feedInput(inputs[i]);
        }
        
        for (Node node : this.network)
        {
            node.engage(this);
        }
        
        double[] output = new double[this.outputs.size()];
        for (int i = 0; i < output.length; i++) output[i] = this.outputs.get(i).getOutputValue();
        
        return output;
    }
    
    /**
     * Mutates the weights for all connections in genome
     *
     * @param random         The random instance
     * @param perturbingRate The probability to nudge the weight by an amount
     */
    public void weightMutation(Random random, double perturbingRate)
    {
        for (Connection connection : getConnections())
        {
            if (random.nextDouble() < perturbingRate)
            {
                connection.weight += random.nextGaussian() * 0.01;
                connection.weight = Math.max(-1.0, Math.min(connection.weight, 1.0));
            }
            else
            {
                connection.weight = random.nextDouble(-1.0, 1.0);
            }
        }
    }
    
    /**
     * Adds a random Node to the genome
     *
     * @param random         The random instance
     * @param nodeInnovation The next ids for the next node
     * @param connInnovation The next ids for the next connection
     */
    public void nodeMutation(Random random, Counter nodeInnovation, Counter connInnovation)
    {
        ArrayList<Connection> suitable = new ArrayList<>();
        for (Connection connection : getConnections()) if (connection.enabled) suitable.add(connection);
        
        if (suitable.isEmpty()) return;
        
        Connection con = random.nextIndex(suitable);
        
        con.enabled = false;
        
        Node       node = new Node(nodeInnovation.inc(), Node.Type.HIDDEN, getNode(con.in).layer + 1);
        Connection con1 = new Connection(connInnovation.inc(), con.in, node.id, 1.0, true);
        Connection con2 = new Connection(connInnovation.inc(), node.id, con.out, con.weight, true);
        
        if (node.layer == getNode(con.out).layer)
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
    
    /**
     * Adds a random Node to the genome
     *
     * @param random         The random instance
     * @param connInnovation The next ids for the next connection
     * @param attempts       The amount of times to try to add a connection before stopping
     */
    public void connectionMutation(Random random, Counter connInnovation, int attempts)
    {
        Node               temp;
        ArrayList<Integer> check = new ArrayList<>();
        ArrayList<Integer> nodes = new ArrayList<>();
        
        for (int attempt = 0; attempt < attempts; attempt++)
        {
            Node n1 = random.nextIndex(this.nodes.values());
            Node n2 = random.nextIndex(this.nodes.values());
            
            if (n1.equals(n2)) continue;
            if (n1.layer == n2.layer) continue;
            if (n1.type == Node.Type.INPUT && n2.type == Node.Type.INPUT) continue;
            if (n1.type == Node.Type.INPUT && n2.type == Node.Type.BIAS) continue;
            if (n1.type == Node.Type.BIAS && n2.type == Node.Type.INPUT) continue;
            if (n1.type == Node.Type.BIAS && n2.type == Node.Type.BIAS) continue;
            if (n1.type == Node.Type.OUTPUT && n2.type == Node.Type.OUTPUT) continue;
            
            if (n1.type == Node.Type.OUTPUT || n2.type == Node.Type.INPUT || n2.type == Node.Type.BIAS || n1.layer > n2.layer)
            {
                temp = n1;
                n1   = n2;
                n2   = temp;
            }
            
            check.clear();
            nodes.clear();
            for (Connection con : getConnections())
            {
                if (con.in == n2.id)
                {
                    check.add(con.out);
                    nodes.add(con.out);
                }
            }
            
            while (!check.isEmpty())
            {
                Node node = getNode(check.remove(0));
                for (Connection con : getConnections())
                {
                    if (con.in == node.id)
                    {
                        check.add(con.out);
                        nodes.add(con.out);
                    }
                }
            }
            
            boolean cont = false;
            for (int node : nodes)
            {
                if (node == n1.id)
                {
                    cont = true;
                    break;
                }
            }
            if (cont) continue;
            
            cont = false;
            for (Connection con : getConnections())
            {
                if ((con.in == n1.id && con.out == n2.id) || (con.in == n2.id && con.out == n1.id))
                {
                    cont = true;
                    break;
                }
            }
            if (cont) continue;
            
            addConnection(new Connection(connInnovation.inc(), n1.id, n2.id, random.nextDouble(-1.0, 1.0), true));
            return;
        }
    }
    
    /**
     * Created an exact copy of the genome.
     *
     * @return the new genome
     */
    public Genome copy()
    {
        Genome genome = new Genome();
        for (Node node : getNodes()) genome.addNode(node.copy());
        for (Connection con : getConnections()) genome.addConnection(con.copy());
        return genome;
    }
    
    /**
     * Saves the genome to a json file
     *
     * @param fileName the file to save the genome to
     */
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
                            writer.name("in").value(connection.in);
                            writer.name("out").value(connection.out);
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
    
    /**
     * Loads a genome from a json file
     *
     * @param fileName the file to load the genome from
     * @return the new genome
     */
    public static Genome load(String fileName)
    {
        if (!fileName.endsWith(".json")) fileName += ".json";
        
        try (JsonReader reader = new JsonReader(new FileReader(getPath(fileName).toString())))
        {
            Genome genome = new Genome();
            
            reader.beginObject();
            {
                reader.nextName(); // Nodes
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
                
                reader.nextName(); // Connections
                reader.beginArray();
                {
                    while (reader.hasNext())
                    {
                        reader.beginObject();
                        {
                            int     id      = -1;
                            int     in      = -1;
                            int     out     = -1;
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
                                        in = reader.nextInt();
                                        break;
                                    case "out":
                                        out = reader.nextInt();
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
     * @param random                 The random instance
     * @param genome1                Parent genome one. The more fit parent
     * @param genome2                Parent genome two. The less fit parent
     * @param disableGeneInheritance Chance for new connection to be disabled
     * @return Child genome
     */
    public static Genome crossover(Random random, Genome genome1, Genome genome2, double disableGeneInheritance)
    {
        Genome child = new Genome();
        
        for (Node node : genome1.getNodes())
        {
            child.addNode(node.copy());
        }
        
        for (Connection g1Conn : genome1.getConnections())
        {
            Connection g2Conn = genome2.getConnection(g1Conn.id);
            if (g2Conn != null)
            {
                // Matching Gene
                Connection childConn = random.nextBoolean() ? g1Conn.copy() : g2Conn.copy();
                boolean    disabled  = !(g1Conn.enabled && g2Conn.enabled);
                childConn.enabled = !(disabled && random.nextDouble() < disableGeneInheritance);
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
     * @param genome1                       Genome one
     * @param genome2                       Genome two
     * @param excessGeneWeight              Weight for excess genes
     * @param disjointGeneWeight            Weight for disjoint genes
     * @param averageWeightDifferenceWeight Weight for average weight distance
     * @return Compatibility distance
     */
    public static double compatibilityDistance(Genome genome1, Genome genome2, double excessGeneWeight, double disjointGeneWeight, double averageWeightDifferenceWeight)
    {
        int    excess     = Genome.countExcessGenes(genome1, genome2);
        int    disjoint   = Genome.countDisjointGenes(genome1, genome2);
        double weightDiff = Genome.averageWeightDiff(genome1, genome2);
        return excess * excessGeneWeight + disjoint * disjointGeneWeight + weightDiff * averageWeightDifferenceWeight;
    }
    
    /**
     * Determines the number of matching connections and nodes between two genomes.
     *
     * @param genome1 Genome one
     * @param genome2 Genome two
     * @return Number of matching genes
     */
    @SuppressWarnings("DuplicatedCode")
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
    @SuppressWarnings("DuplicatedCode")
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
    @SuppressWarnings("DuplicatedCode")
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
