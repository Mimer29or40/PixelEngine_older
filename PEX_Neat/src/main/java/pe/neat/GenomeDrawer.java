package pe.neat;

import pe.Color;
import pe.Sprite;
import pe.draw.DrawMode;
import pe.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

import static pe.PixelEngine.*;

@SuppressWarnings({"SuspiciousNameCombination", "unused"})
public class GenomeDrawer
{
    public int nodeRadius   = 10;
    public int nodeSpacing  = 10;
    public int layerSpacing = 10;
    
    public int imageScale = 4;
    
    public boolean drawDisabledConnections = false;
    
    public final Color textColor = Color.BLACK.copy();
    
    public final Color backgroundColor = Color.WHITE.copy();
    
    public final Color nodeBorderColor = Color.BLACK.copy();
    public final Color inputNodeColor  = new Color(50, 200, 100);
    public final Color outputNodeColor = new Color(50, 100, 200);
    public final Color hiddenNodeColor = new Color(200, 50, 100);
    public final Color biasNodeColor   = new Color(200, 100, 50);
    
    public final Color posConnColor = new Color(0, 0, 255, 100);
    public final Color negConnColor = new Color(255, 0, 0, 100);
    public final Color disConnColor = new Color(0, 0, 0, 100);
    
    public Orientation orientation = Orientation.RIGHT;
    
    public GenomeDrawer() { }
    
    public Sprite generateGraph(Genome genome)
    {
        int nodeSpacing  = (this.nodeSpacing + this.nodeRadius * 2) * this.imageScale;
        int layerSpacing = (this.layerSpacing + this.nodeRadius * 2) * this.imageScale;
        int border       = this.nodeRadius * 2 * this.imageScale;
    
        // Group all nodes into layer groups
        ArrayList<ArrayList<Node>> allNodes      = new ArrayList<>();
        int                        maxLayerCount = 0;
        for (int i = 0; i < genome.layerCount; i++)
        {
            ArrayList<Node> layer = new ArrayList<>();
            for (Node node : genome.getNodes())
            {
                if (node.layer == i) layer.add(node);
            }
            maxLayerCount = Math.max(maxLayerCount, layer.size());
            allNodes.add(layer);
        }
        
        // Generate point for each node in each layer
        HashMap<Integer, Pair<Integer, Integer>> nodes = new HashMap<>();
    
        int imageWidth  = Math.max((maxLayerCount - 1) * nodeSpacing + 2 * border, 10);
        int imageHeight = Math.max((genome.layerCount - 1) * layerSpacing + 2 * border, 10);
        if (this.orientation == Orientation.RIGHT || this.orientation == Orientation.LEFT)
        {
            int temp = imageWidth;
            imageWidth  = imageHeight;
            imageHeight = temp;
        }
        
        switch (this.orientation)
        {
            case UP:
                for (int i = 0, ni = allNodes.size(); i < ni; i++)
                {
                    ArrayList<Node> layer = allNodes.get(i);
    
                    int layerLen = layer.size();
                    int yPos     = border + (genome.layerCount - i - 1) * layerSpacing;
                    for (int j = 0, nj = layer.size(); j < nj; j++)
                    {
                        int xPos = layerLen == maxLayerCount ? border + j * nodeSpacing : (imageWidth - (layerLen - 1) * nodeSpacing) / 2 + j * nodeSpacing;
                        nodes.put(layer.get(j).id, new Pair<>(xPos, yPos));
                    }
                }
                break;
            case DOWN:
                for (int i = 0, ni = allNodes.size(); i < ni; i++)
                {
                    ArrayList<Node> layer = allNodes.get(i);
    
                    int layerLen = layer.size();
                    int yPos     = border + i * layerSpacing;
                    for (int j = 0, nj = layer.size(); j < nj; j++)
                    {
                        int xPos = layerLen == maxLayerCount ? border + j * nodeSpacing : (imageWidth - (layerLen - 1) * nodeSpacing) / 2 + j * nodeSpacing;
                        nodes.put(layer.get(j).id, new Pair<>(xPos, yPos));
                    }
                }
                break;
            case RIGHT:
                for (int i = 0, ni = allNodes.size(); i < ni; i++)
                {
                    ArrayList<Node> layer = allNodes.get(i);
    
                    int layerLen = layer.size();
                    int xPos     = border + i * layerSpacing;
                    for (int j = 0, nj = layer.size(); j < nj; j++)
                    {
                        int yPos = layerLen == maxLayerCount ? border + j * nodeSpacing : (imageHeight - (layerLen - 1) * nodeSpacing) / 2 + j * nodeSpacing;
                        nodes.put(layer.get(j).id, new Pair<>(xPos, yPos));
                    }
                }
                break;
            case LEFT:
                for (int i = 0, ni = allNodes.size(); i < ni; i++)
                {
                    ArrayList<Node> layer = allNodes.get(i);
    
                    int layerLen = layer.size();
                    int xPos     = border + (genome.layerCount - i - 1) * layerSpacing;
                    for (int j = 0, nj = layer.size(); j < nj; j++)
                    {
                        int yPos = layerLen == maxLayerCount ? border + j * nodeSpacing : (imageHeight - (layerLen - 1) * nodeSpacing) / 2 + j * nodeSpacing;
                        nodes.put(layer.get(j).id, new Pair<>(xPos, yPos));
                    }
                }
                break;
        }
    
        Sprite   sprite = new Sprite(imageWidth, imageHeight, this.backgroundColor);
        Sprite   prev   = drawTarget();
        DrawMode mode   = drawMode();
        drawTarget(sprite);
        drawMode(DrawMode.ALPHA);
    
        // Draw connections first so they are under nodes
        for (Connection con : genome.getConnections())
        {
            Pair<Integer, Integer> inNode  = nodes.get(con.in);
            Pair<Integer, Integer> outNode = nodes.get(con.out);
            if (con.enabled)
            {
                int   width = (int) Math.max(Math.abs(con.weight) * 5 * this.imageScale, 1);
                Color color = con.weight > 0 ? this.posConnColor : this.negConnColor;
                drawLine(inNode.a, inNode.b, outNode.a, outNode.b, width, color);
            }
            else if (this.drawDisabledConnections)
            {
                drawLine(inNode.a, inNode.b, outNode.a, outNode.b, this.imageScale, this.disConnColor);
            }
        }
        
        // Draw nodes
        for (Node node : genome.getNodes())
        {
            String text = "" + node.id;
    
            int w = textWidth(text, this.imageScale);
            int h = textHeight(text, this.imageScale);
    
            Pair<Integer, Integer> pos = nodes.get(node.id);
    
            Color color = Color.BLANK;
            switch (node.type)
            {
                case INPUT:
                    color = this.inputNodeColor;
                    break;
                case HIDDEN:
                    color = this.hiddenNodeColor;
                    break;
                case OUTPUT:
                    color = this.outputNodeColor;
                    break;
                case BIAS:
                    color = this.biasNodeColor;
                    break;
            }
    
            if (w + this.nodeRadius * this.imageScale > (this.nodeRadius + 1) * this.imageScale)
            {
                fillEllipse(pos.a, pos.b, w + this.nodeRadius * this.imageScale + this.imageScale, (this.nodeRadius + 1) * this.imageScale, this.nodeBorderColor);
                fillEllipse(pos.a, pos.b, w + this.nodeRadius * this.imageScale, this.nodeRadius * this.imageScale, color);
            }
            else
            {
                fillCircle(pos.a, pos.b, (this.nodeRadius + 1) * this.imageScale, this.nodeBorderColor);
                fillCircle(pos.a, pos.b, this.nodeRadius * this.imageScale, color);
            }
            drawString(pos.a - w / 2, pos.b - h / 2, text, this.textColor, this.imageScale);
        }
    
        drawTarget(prev);
        drawMode(mode);
        return sprite;
    }
    
    public enum Orientation
    {
        RIGHT, LEFT, UP, DOWN
    }
}
