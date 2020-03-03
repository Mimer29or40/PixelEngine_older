package pe;

import java.util.ArrayList;
import java.util.List;

public class PolyCollisions extends PixelEngine
{
    static class Polygon
    {
        //List<Vector2d> p       = new ArrayList<>();
        //Vector2d       pos     = new Vector2d();
        //float          angle   = 0.0F;
        //List<Vector2d> o       = new ArrayList<>();
        //boolean        overlap = false;
    }
    
    List<Polygon> shapes = new ArrayList<>();
    int           mode   = 0;
    
    @Override
    protected void setup()
    {
        size(256, 240, 4, 4);
    
        //// Create Pentagon
        //Polygon s1     = new Polygon();
        //float   fTheta = 3.14159f * 2.0f / 5.0f;
        //s1.pos = new Vector2d(100, 100);
        //s1.angle = 0.0f;
        //for (int i = 0; i < 5; i++)
        //{
        //    s1.o.add(new Vector2d(30.0f * (float) Math.cos(fTheta * i), 30.0f * (float) Math.sin(fTheta * i)));
        //    s1.p.add(new Vector2d(30.0f * (float) Math.cos(fTheta * i), 30.0f * (float) Math.sin(fTheta * i)));
        //}
        //
        //// Create Triangle
        //Polygon s2 = new Polygon();
        //fTheta = 3.14159f * 2.0f / 3.0f;
        //s2.pos = new Vector2d(200, 150);
        //s2.angle = 0.0f;
        //for (int i = 0; i < 3; i++)
        //{
        //    s2.o.add(new Vector2d(20.0f * (float) Math.cos(fTheta * i), 20.0f * (float) Math.sin(fTheta * i)));
        //    s2.p.add(new Vector2d(20.0f * (float) Math.cos(fTheta * i), 20.0f * (float) Math.sin(fTheta * i)));
        //}
        //
        //// Create Quad
        //Polygon s3 = new Polygon();
        //s1.pos = new Vector2d(50, 200);
        //s3.angle = 0.0f;
        //s3.o.add(new Vector2d(-30, -30));
        //s3.o.add(new Vector2d(-30, +30));
        //s3.o.add(new Vector2d(+30, +30));
        //s3.o.add(new Vector2d(+30, -30));
        //s3.p.add(new Vector2d());
        //s3.p.add(new Vector2d());
        //s3.p.add(new Vector2d());
        //s3.p.add(new Vector2d());
        //
        //
        //this.shapes.add(s1);
        //this.shapes.add(s2);
        //this.shapes.add(s3);
    }
    
    boolean ShapeOverlap_SAT(Polygon r1, Polygon r2)
    {
        //Polygon poly1 = r1;
        //Polygon poly2 = r2;
        //
        //for (int shape = 0; shape < 2; shape++)
        //{
        //    if (shape == 1)
        //    {
        //        poly1 = r2;
        //        poly2 = r1;
        //    }
        //
        //    for (int a = 0; a < poly1.p.size(); a++)
        //    {
        //        int      b        = (a + 1) % poly1.p.size();
        //        Vector2d axisProj = new Vector2d(-(poly1.p.get(b).y - poly1.p.get(a).y), poly1.p.get(b).x - poly1.p.get(a).x);
        //        float    d        = (float) Math.sqrt(axisProj.x * axisProj.x + axisProj.y * axisProj.y);
        //        axisProj = new Vector2d(axisProj.x / d, axisProj.y / d);
        //
        //        // Work out min and max 1D points for r1
        //        float min_r1 = Float.MAX_VALUE, max_r1 = Float.MIN_VALUE;
        //        for (int p = 0; p < poly1.p.size(); p++)
        //        {
        //            float q = (float) (poly1.p.get(p).x * axisProj.x + poly1.p.get(p).y * axisProj.y);
        //            min_r1 = Math.min(min_r1, q);
        //            max_r1 = Math.max(max_r1, q);
        //        }
        //
        //        // Work out min and max 1D points for r2
        //        float min_r2 = Float.MAX_VALUE, max_r2 = Float.MIN_VALUE;
        //        for (int p = 0; p < poly2.p.size(); p++)
        //        {
        //            float q = (float) (poly2.p.get(p).x * axisProj.x + poly2.p.get(p).y * axisProj.y);
        //            min_r2 = Math.min(min_r2, q);
        //            max_r2 = Math.max(max_r2, q);
        //        }
        //
        //        if (!(max_r2 >= min_r1 && max_r1 >= min_r2)) return false;
        //    }
        //}
        
        return true;
    }
    
    boolean ShapeOverlap_SAT_STATIC(Polygon r1, Polygon r2)
    {
        //Polygon poly1 = r1;
        //Polygon poly2 = r2;
        //
        //float overlap = Float.MAX_VALUE;
        //
        //for (int shape = 0; shape < 2; shape++)
        //{
        //    if (shape == 1)
        //    {
        //        poly1 = r2;
        //        poly2 = r1;
        //    }
        //
        //    for (int a = 0; a < poly1.p.size(); a++)
        //    {
        //        int      b        = (a + 1) % poly1.p.size();
        //        Vector2d axisProj = new Vector2d(-(poly1.p.get(b).y - poly1.p.get(a).y), poly1.p.get(b).x - poly1.p.get(a).x);
        //
        //        // Optional normalisation of projection axis enhances stability slightly
        //        //float d = (float) Math.sqrt(axisProj.x * axisProj.x + axisProj.y * axisProj.y);
        //        //axisProj = { axisProj.x / d, axisProj.y / d };
        //
        //        // Work out min and max 1D points for r1
        //        float min_r1 = Float.MAX_VALUE, max_r1 = Float.MIN_VALUE;
        //        for (int p = 0; p < poly1.p.size(); p++)
        //        {
        //            float q = (float) (poly1.p.get(p).x * axisProj.x + poly1.p.get(p).y * axisProj.y);
        //            min_r1 = Math.min(min_r1, q);
        //            max_r1 = Math.max(max_r1, q);
        //        }
        //
        //        // Work out min Float.MAX_VALUE max 1D points for r2
        //        float min_r2 = Float.MAX_VALUE, max_r2 = Float.MIN_VALUE;
        //        for (int p = 0; p < poly2.p.size(); p++)
        //        {
        //            float q = (float) (poly2.p.get(p).x * axisProj.x + poly2.p.get(p).y * axisProj.y);
        //            min_r2 = Math.min(min_r2, q);
        //            max_r2 = Math.max(max_r2, q);
        //        }
        //
        //        // Calculate actual overlap along projected axis, and store the minimum
        //        overlap = Math.min(Math.min(max_r1, max_r2) - Math.max(min_r1, min_r2), overlap);
        //
        //        if (!(max_r2 >= min_r1 && max_r1 >= min_r2)) return false;
        //    }
        //}
        //
        //// If we got here, the objects have collided, we will displace r1
        //// by overlap along the vector between the two object centers
        //Vector2d d = new Vector2d(r2.pos.x - r1.pos.x, r2.pos.y - r1.pos.y);
        //float    s = (float) Math.sqrt(d.x * d.x + d.y * d.y);
        //r1.pos.x -= overlap * d.x / s;
        //r1.pos.y -= overlap * d.y / s;
        return false;
    }
    
    // Use edge/diagonal intersections.
    boolean ShapeOverlap_DIAGS(Polygon r1, Polygon r2)
    {
        //Polygon poly1 = r1;
        //Polygon poly2 = r2;
        //
        //for (int shape = 0; shape < 2; shape++)
        //{
        //    if (shape == 1)
        //    {
        //        poly1 = r2;
        //        poly2 = r1;
        //    }
        //
        //    // Check diagonals of Polygon...
        //    for (int p = 0; p < poly1.p.size(); p++)
        //    {
        //        Vector2d line_r1s = poly1.pos;
        //        Vector2d line_r1e = poly1.p.get(p);
        //
        //        // ...against edges of the other
        //        for (int q = 0; q < poly2.p.size(); q++)
        //        {
        //            Vector2d line_r2s = poly2.p.get(q);
        //            Vector2d line_r2e = poly2.p.get((q + 1) % poly2.p.size());
        //
        //            // Standard "off the shelf" line segment intersection
        //            float h  = (float) ((line_r2e.x - line_r2s.x) * (line_r1s.y - line_r1e.y) - (line_r1s.x - line_r1e.x) * (line_r2e.y - line_r2s.y));
        //            float t1 = (float) ((line_r2s.y - line_r2e.y) * (line_r1s.x - line_r2s.x) + (line_r2e.x - line_r2s.x) * (line_r1s.y - line_r2s.y)) / h;
        //            float t2 = (float) ((line_r1s.y - line_r1e.y) * (line_r1s.x - line_r2s.x) + (line_r1e.x - line_r1s.x) * (line_r1s.y - line_r2s.y)) / h;
        //
        //            if (t1 >= 0.0f && t1 < 1.0f && t2 >= 0.0f && t2 < 1.0f)
        //            {
        //                return true;
        //            }
        //        }
        //    }
        //}
        return false;
    }
    
    // Use edge/diagonal intersections.
    boolean ShapeOverlap_DIAGS_STATIC(Polygon r1, Polygon r2)
    {
        //Polygon poly1 = r1;
        //Polygon poly2 = r2;
        //
        //for (int shape = 0; shape < 2; shape++)
        //{
        //    if (shape == 1)
        //    {
        //        poly1 = r2;
        //        poly2 = r1;
        //    }
        //
        //    // Check diagonals of this Polygon...
        //    for (int p = 0; p < poly1.p.size(); p++)
        //    {
        //        Vector2d line_r1s = poly1.pos;
        //        Vector2d line_r1e = poly1.p.get(p);
        //
        //        Vector2d displacement = new Vector2d(0, 0);
        //
        //        // ...against edges of this Polygon
        //        for (int q = 0; q < poly2.p.size(); q++)
        //        {
        //            Vector2d line_r2s = poly2.p.get(q);
        //            Vector2d line_r2e = poly2.p.get((q + 1) % poly2.p.size());
        //
        //            // Standard "off the shelf" line segment intersection
        //            float h  = (float) ((line_r2e.x - line_r2s.x) * (line_r1s.y - line_r1e.y) - (line_r1s.x - line_r1e.x) * (line_r2e.y - line_r2s.y));
        //            float t1 = (float) ((line_r2s.y - line_r2e.y) * (line_r1s.x - line_r2s.x) + (line_r2e.x - line_r2s.x) * (line_r1s.y - line_r2s.y)) / h;
        //            float t2 = (float) ((line_r1s.y - line_r1e.y) * (line_r1s.x - line_r2s.x) + (line_r1e.x - line_r1s.x) * (line_r1s.y - line_r2s.y)) / h;
        //
        //            if (t1 >= 0.0f && t1 < 1.0f && t2 >= 0.0f && t2 < 1.0f)
        //            {
        //                displacement.x += (1.0f - t1) * (line_r1e.x - line_r1s.x);
        //                displacement.y += (1.0f - t1) * (line_r1e.y - line_r1s.y);
        //            }
        //        }
        //
        //        r1.pos.x += displacement.x * (shape == 0 ? -1 : +1);
        //        r1.pos.y += displacement.y * (shape == 0 ? -1 : +1);
        //    }
        //}
        
        // Cant overlap if static collision is resolved
        return false;
    }
    
    
    @Override
    protected void draw(double elapsedTime)
    {
        //if (getKey(Keyboard.Keyboard.F1).released) this.mode = 0;
        //if (getKey(Keyboard.Keyboard.F2).released) this.mode = 1;
        //if (getKey(Keyboard.Keyboard.F3).released) this.mode = 2;
        //if (getKey(Keyboard.Keyboard.F4).released) this.mode = 3;
        //
        //// Shape 1
        //if (getKey(Keyboard.Keyboard.LEFT).held) this.shapes.get(0).angle -= 2.0f * elapsedTime;
        //if (getKey(Keyboard.Keyboard.RIGHT).held) this.shapes.get(0).angle += 2.0f * elapsedTime;
        //
        //if (getKey(Keyboard.Keyboard.UP).held)
        //{
        //    this.shapes.get(0).pos.x += (float) Math.cos(this.shapes.get(0).angle) * 60.0f * elapsedTime;
        //    this.shapes.get(0).pos.y += (float) Math.sin(this.shapes.get(0).angle) * 60.0f * elapsedTime;
        //}
        //
        //if (getKey(Keyboard.Keyboard.DOWN).held)
        //{
        //    this.shapes.get(0).pos.x -= (float) Math.cos(this.shapes.get(0).angle) * 60.0f * elapsedTime;
        //    this.shapes.get(0).pos.y -= (float) Math.sin(this.shapes.get(0).angle) * 60.0f * elapsedTime;
        //}
        //
        //// Shape 2
        //if (getKey(Keyboard.Keyboard.A).held) this.shapes.get(1).angle -= 2.0f * elapsedTime;
        //if (getKey(Keyboard.Keyboard.D).held) this.shapes.get(1).angle += 2.0f * elapsedTime;
        //
        //if (getKey(Keyboard.Keyboard.W).held)
        //{
        //    this.shapes.get(1).pos.x += (float) Math.cos(this.shapes.get(1).angle) * 60.0f * elapsedTime;
        //    this.shapes.get(1).pos.y += (float) Math.sin(this.shapes.get(1).angle) * 60.0f * elapsedTime;
        //}
        //
        //if (getKey(Keyboard.Keyboard.S).held)
        //{
        //    this.shapes.get(1).pos.x -= (float) Math.cos(this.shapes.get(1).angle) * 60.0f * elapsedTime;
        //    this.shapes.get(1).pos.y -= (float) Math.sin(this.shapes.get(1).angle) * 60.0f * elapsedTime;
        //}
        //
        //// Update Shapes and reset flags
        //for (Polygon r : this.shapes)
        //{
        //    // 2D Rotation Transform + 2D Translation
        //    for (int i = 0; i < r.o.size(); i++)
        //    {
        //        r.p.add(i,
        //                new Vector2d((r.o.get(i).x * (float) Math.cos(r.angle)) - (r.o.get(i).y * (float) Math.sin(r.angle)) + r.pos.x,
        //                             (r.o.get(i).x * (float) Math.sin(r.angle)) + (r.o.get(i).y * (float) Math.cos(r.angle)) + r.pos.y));
        //    }
        //
        //    r.overlap = false;
        //}
        //
        //// Check for overlap
        //for (int m = 0; m < this.shapes.size(); m++)
        //{
        //    for (int n = m + 1; n < this.shapes.size(); n++)
        //    {
        //        switch (this.mode)
        //        {
        //            case 0:
        //                this.shapes.get(m).overlap |= ShapeOverlap_SAT(this.shapes.get(m), this.shapes.get(n));
        //                break;
        //            case 1:
        //                this.shapes.get(m).overlap |= ShapeOverlap_SAT_STATIC(this.shapes.get(m), this.shapes.get(n));
        //                break;
        //            case 2:
        //                this.shapes.get(m).overlap |= ShapeOverlap_DIAGS(this.shapes.get(m), this.shapes.get(n));
        //                break;
        //            case 3:
        //                this.shapes.get(m).overlap |= ShapeOverlap_DIAGS_STATIC(this.shapes.get(m), this.shapes.get(n));
        //                break;
        //        }
        //    }
        //}
        //
        //// === Render Display ===
        //clear(Color.BLUE);
        //
        //// Draw Shapes
        //for (Polygon r : this.shapes)
        //{
        //    // Draw Boundary
        //    for (int i = 0; i < r.p.size(); i++)
        //    {
        //        drawLine((int) r.p.get(i).x,
        //                 (int) r.p.get(i).y,
        //                 (int) r.p.get((i + 1) % r.p.size()).x,
        //                 (int) r.p.get((i + 1) % r.p.size()).y,
        //                 (r.overlap ? Color.RED : Color.WHITE));
        //    }
        //
        //    // Draw Direction
        //    drawLine((int) r.p.get(0).x, (int) r.p.get(0).y, (int) r.pos.x, (int) r.pos.y, (r.overlap ? Color.RED : Color.WHITE));
        //}
        //
        //// Draw HUD
        //drawString(8, 10, "F1: SAT", (this.mode == 0 ? Color.RED : Color.YELLOW));
        //drawString(8, 20, "F2: SAT/STATIC", (this.mode == 1 ? Color.RED : Color.YELLOW));
        //drawString(8, 30, "F3: DIAG", (this.mode == 2 ? Color.RED : Color.YELLOW));
        //drawString(8, 40, "F4: DIAG/STATIC", (this.mode == 3 ? Color.RED : Color.YELLOW));
    }
    
    public static void main(String[] args)
    {
        start(new PolyCollisions());
    }
}
