package pe;

import pe.color.Color;
import pe.color.Colorc;
import pe.draw.DrawPattern;

import java.util.ArrayList;

public class DrawTest extends PixelEngine
{
    public static void drawLineOld(int x1, int y1, int x2, int y2, Colorc p, DrawPattern pattern)
    {
        // Bresenham's Algorithm
        
        pattern.reset();
        
        int dx, dy, x, y, xi, yi, d, tmp;
        
        dx = x2 - x1;
        dy = y2 - y1;
        
        if (dx == 0)
        {
            if (y2 < y1)
            {
                tmp = y1;
                y1  = y2;
                y2  = tmp;
            }
            for (y = y1; y <= y2; y++)
            {
                if (pattern.shouldDraw()) draw(x1, y, p);
            }
            return;
        }
        
        if (y2 - y1 == 0)
        {
            if (x2 < x1)
            {
                tmp = x1;
                x1  = x2;
                x2  = tmp;
            }
            for (x = x1; x <= x2; x++)
            {
                if (pattern.shouldDraw()) draw(x, y1, p);
            }
            return;
        }
        
        if (Math.abs(dy) < Math.abs(dx))
        {
            if (x1 > x2)
            {
                tmp = x1;
                x1  = x2;
                x2  = tmp;
                
                tmp = y1;
                y1  = y2;
                y2  = tmp;
            }
            
            dx = x2 - x1;
            dy = y2 - y1;
            yi = 1;
            if (dy < 0)
            {
                yi = -1;
                dy = -dy;
            }
            d = 2 * dy - dx;
            y = y1;
            
            for (x = x1; x <= x2; x++)
            {
                if (pattern.shouldDraw()) draw(x, y, p);
                if (d > 0)
                {
                    y += yi;
                    d = d - 2 * dx;
                }
                d = d + 2 * dy;
            }
        }
        else
        {
            if (y1 > y2)
            {
                tmp = x1;
                x1  = x2;
                x2  = tmp;
                
                tmp = y1;
                y1  = y2;
                y2  = tmp;
            }
            
            dx = x2 - x1;
            dy = y2 - y1;
            xi = 1;
            if (dx < 0)
            {
                xi = -1;
                dx = -dx;
            }
            d = 2 * dx - dy;
            x = x1;
            
            for (y = y1; y <= y2; y++)
            {
                if (pattern.shouldDraw()) draw(x, y, p);
                if (d > 0)
                {
                    x = x + xi;
                    d = d - 2 * dy;
                }
                d = d + 2 * dx;
            }
        }
    }
    
    public static void drawLine(int x1, int y1, int x2, int y2, Colorc p, DrawPattern pattern)
    {
        pattern.reset();
        
        int dx  = Math.abs(x2 - x1), sx = x1 < x2 ? 1 : -1;
        int dy  = Math.abs(y2 - y1), sy = y1 < y2 ? 1 : -1;
        int err = dx - dy, e2; /* error value e_xy */
        
        if (dx == 0)
        {
            for (; y1 <= y2; y1 += sy)
            {
                if (pattern.shouldDraw()) draw(x1, y1, p);
            }
            return;
        }
        
        if (dy == 0)
        {
            for (; x1 <= x2; x1 += sx)
            {
                if (pattern.shouldDraw()) draw(x1, y1, p);
            }
            return;
        }
        
        for (; ; )
        {  /* loop */
            if (pattern.shouldDraw()) draw(x1, y1, p);
            if (x1 == x2 && y1 == y2) break;
            e2 = err << 1;
            if (e2 >= -dy)
            {
                err -= dy;
                x1 += sx;
            } /* e_xy+e_x > 0 */
            if (e2 <= dx)
            {
                err += dx;
                y1 += sy;
            } /* e_xy+e_y < 0 */
        }
    }
    
    public static void drawLine(int x1, int y1, int x2, int y2, int width, Colorc p, DrawPattern pattern)
    {
        pattern.reset();
        
        int dx  = Math.abs(x2 - x1), sx = x1 < x2 ? 1 : -1;
        int dy  = Math.abs(y2 - y1), sy = y1 < y2 ? 1 : -1;
        int err = dx - dy, e2; /* error value e_xy */
        
        if (width == 1)
        {
            if (dx == 0)
            {
                for (; y1 <= y2; y1 += sy)
                {
                    if (pattern.shouldDraw()) draw(x1, y1, p);
                }
                return;
            }
            
            if (dy == 0)
            {
                for (; x1 <= x2; x1 += sx)
                {
                    if (pattern.shouldDraw()) draw(x1, y1, p);
                }
                return;
            }
            
            for (; ; )
            {  /* loop */
                if (pattern.shouldDraw()) draw(x1, y1, p);
                if (x1 == x2 && y1 == y2) break;
                e2 = err << 1;
                if (e2 >= -dy)
                {
                    err -= dy;
                    x1 += sx;
                } /* e_xy+e_x > 0 */
                if (e2 <= dx)
                {
                    err += dx;
                    y1 += sy;
                } /* e_xy+e_y < 0 */
            }
        }
        else
        {
            int     x3, y3, e3;
            double  ed = dx + dy == 0 ? 1 : Math.sqrt(dx * dx + dy * dy);
            boolean shouldDraw;
            
            // double ex = (dy * sy) / ed;
            // double ey = (-dx * sx) / ed;
            
            // for (width = (width + 1) / 2; ; )
            // {
            //     if (pattern.shouldDraw())
            //     {
            //         for (x3 = -width; x3 < width; x3++)
            //         {
            //             draw(x1 + (int) Math.ceil(x3 * ex), y1 + (int) Math.ceil(x3 * ey), p);
            //             // draw(x1, y1, p);
            //         }
            //         // draw(x1, y1, p);
            //         // drawLine(x1 - (int) (width * ex), y1 - (int) (width * ey), x1 + (int) (width * ex), y1 + (int) (width * ey), 1, p, DrawPattern.SOLID);
            //         // for (x3 = width * -sx; x3 != width * sx; x3 += sx)
            //         // {
            //         //     for (y3 = width * -sy; y3 != width * sy; y3 += sy)
            //         //     {
            //         //         draw(x1 + x3, y1 + y3, p);
            //         //     }
            //         // }
            //     }
            //     if (x1 == x2 && y1 == y2) break;
            //     e2 = err << 1;
            //     if (e2 >= -dy)
            //     {
            //         err -= dy;
            //         x1 += sx;
            //     } /* e_xy+e_x > 0 */
            //     if (e2 <= dx)
            //     {
            //         err += dx;
            //         y1 += sy;
            //     } /* e_xy+e_y < 0 */
            // }
            
            for (width = (width + 1) / 2; ; )
            {
                shouldDraw = pattern.shouldDraw();/* pixel loop */
                if (shouldDraw) draw(x1, y1, p);
                e2 = err << 1;
                if (e2 >= -dx)
                {
                    for (e3 = e2 + dy, y3 = y1; e3 < ed * width && (y2 != y3 || dx > dy); e3 += dx)
                    {
                        if (shouldDraw) draw(x1, y3 += sy, p);
                    }
                    if (x1 == x2) break;
                    err -= dy;
                    x1 += sx;
                }
                if (e2 <= dy)
                {
                    for (e3 = dx - e2, x3 = x1; e3 < ed * width && (x2 != x3 || dx < dy); e3 += dy)
                    {
                        if (shouldDraw) draw(x3 += sx, y1, p);
                    }
                    if (y1 == y2) break;
                    err += dx;
                    y1 += sy;
                }
            }
        }
    }
    
    public static void drawCircleOld(int x, int y, int radius, Colorc p, int mask)
    {
        int x0 = 0;
        int y0 = radius;
        int d  = 3 - 2 * radius;
        if (radius < 0) return;
        
        while (y0 >= x0) // only formulate 1/8 of circle
        {
            if ((mask & 0x01) > 0) draw(x + x0, y - y0, p);
            if ((mask & 0x02) > 0) draw(x + y0, y - x0, p);
            if ((mask & 0x04) > 0) draw(x + y0, y + x0, p);
            if ((mask & 0x08) > 0) draw(x + x0, y + y0, p);
            if ((mask & 0x10) > 0) draw(x - x0, y + y0, p);
            if ((mask & 0x20) > 0) draw(x - y0, y + x0, p);
            if ((mask & 0x40) > 0) draw(x - y0, y - x0, p);
            if ((mask & 0x80) > 0) draw(x - x0, y - y0, p);
            
            if (d < 0)
            {
                d += 4 * x0++ + 6;
            }
            else
            {
                d += 4 * (x0++ - y0--) + 10;
            }
        }
    }
    
    public static void drawCircle(int x, int y, int radius, Colorc p, int mask)
    {
        if (radius < 0) return;
        int xr = -radius, yr = 0, err = 2 - 2 * radius;
        do
        {
            draw(x - xr, y + yr, p); /*   I. Quadrant */
            draw(x - yr, y - xr, p); /*  II. Quadrant */
            draw(x + xr, y - yr, p); /* III. Quadrant */
            draw(x + yr, y + xr, p); /*  IV. Quadrant */
            radius = err;
            if (radius <= yr) err += ++yr * 2 + 1;            /* e_xy+e_y < 0 */
            if (radius > xr || err > yr) err += ++xr * 2 + 1; /* e_xy+e_x > 0 or no 2nd y-step */
        } while (xr < 0);
    }
    
    public static void fillCircleOld(int x, int y, int radius, Colorc p)
    {
        if (radius < 0) return;
        int x0 = 0, y0 = radius, d = 3 - 2 * radius, i;
        
        while (y0 >= x0)
        {
            for (i = x - x0; i <= x + x0; i++) draw(i, y - y0, p);
            for (i = x - y0; i <= x + y0; i++) draw(i, y - x0, p);
            for (i = x - x0; i <= x + x0; i++) draw(i, y + y0, p);
            for (i = x - y0; i <= x + y0; i++) draw(i, y + x0, p);
            
            if (d < 0)
            {
                d += 4 * x0++ + 6;
            }
            else
            {
                d += 4 * (x0++ - y0--) + 10;
            }
        }
    }
    
    public static void fillCircle(int x, int y, int radius, Colorc p)
    {
        if (radius < 0) return;
        int xr = -radius, yr = 0, err = 2 - 2 * radius, i;
        do
        {
            for (i = x - -xr; i <= x + -xr; i++) draw(i, y - yr, p); /*   I-II. Quadrant */
            for (i = x - -xr; i <= x + -xr; i++) draw(i, y + yr, p); /* III-IV. Quadrant */
            // draw(x - xr, y + yr, p); /*   I. Quadrant */
            // draw(x - yr, y - xr, p); /*  II. Quadrant */
            // draw(x + xr, y - yr, p); /* III. Quadrant */
            // draw(x + yr, y + xr, p); /*  IV. Quadrant */
            radius = err;
            if (radius <= yr) err += ++yr * 2 + 1;            /* e_xy+e_y < 0 */
            if (radius > xr || err > yr) err += ++xr * 2 + 1; /* e_xy+e_x > 0 or no 2nd y-step */
        } while (xr < 0);
    }
    
    public static void fillEllipse(int x, int y, int width, int height, Colorc p)
    {
        if (width < 1 || height < 1) return;
        int  x0  = x - width / 2, y0 = y - height / 2;
        int  x1  = x + width / 2, y1 = y + height / 2;
        int  b1  = height & 1; /* values of diameter */
        long dx  = 4 * (1 - width) * height * height, dy = 4 * (b1 + 1) * width * width; /* error increment */
        long err = dx + dy + b1 * width * width, e2; /* error of 1.step */
        
        if (x0 > x1)
        {
            x0 = x1;
            x1 += width;
        } /* if called with swapped points */
        if (y0 > y1) y0 = y1; /* .. exchange them */
        y0 += (height + 1) / 2;
        y1 = y0 - b1;   /* starting pixel */
        width *= 8 * width;
        b1 = 8 * height * height;
        
        do
        {
            for (int i = x0; i < x1; i++) draw(i, y0, p);
            for (int i = x0; i < x1; i++) draw(i, y1, p);
            e2 = 2 * err;
            if (e2 <= dy)
            {
                y0++;
                y1--;
                err += dy += width;
            }  /* y step */
            if (e2 >= dx || 2 * err > dy)
            {
                x0++;
                x1--;
                err += dx += b1;
            } /* x step */
        } while (x0 <= x1);
        
        while (y0 - y1 < height)
        {  /* too early stop of flat ellipses w=1 */
            draw(x0 - 1, y0, p); /* -> finish tip of ellipse */
            draw(x1 + 1, y0++, p);
            draw(x0 - 1, y1, p);
            draw(x1 + 1, y1--, p);
        }
    }
    
    @Override
    protected void setup()
    {
        size();
    }
    
    long tLH1, minLH1 = Long.MAX_VALUE, maxLH1 = Long.MIN_VALUE, sumLH1 = 0;
    long tLH2, minLH2 = Long.MAX_VALUE, maxLH2 = Long.MIN_VALUE, sumLH2 = 0;
    long tLV1, minLV1 = Long.MAX_VALUE, maxLV1 = Long.MIN_VALUE, sumLV1 = 0;
    long tLV2, minLV2 = Long.MAX_VALUE, maxLV2 = Long.MIN_VALUE, sumLV2 = 0;
    long tLD1, minLD1 = Long.MAX_VALUE, maxLD1 = Long.MIN_VALUE, sumLD1 = 0;
    long tLD2, minLD2 = Long.MAX_VALUE, maxLD2 = Long.MIN_VALUE, sumLD2 = 0;
    
    ArrayList<Long> LH1 = new ArrayList<>();
    ArrayList<Long> LH2 = new ArrayList<>();
    ArrayList<Long> LV1 = new ArrayList<>();
    ArrayList<Long> LV2 = new ArrayList<>();
    ArrayList<Long> LD1 = new ArrayList<>();
    ArrayList<Long> LD2 = new ArrayList<>();
    
    long tC1, minC1 = Long.MAX_VALUE, maxC1 = Long.MIN_VALUE, sumC1 = 0;
    long tC2, minC2 = Long.MAX_VALUE, maxC2 = Long.MIN_VALUE, sumC2 = 0;
    
    ArrayList<Long> C1 = new ArrayList<>();
    ArrayList<Long> C2 = new ArrayList<>();
    
    int count = 0;
    int x, y;
    
    @Override
    protected void draw(double elapsedTime)
    {
        clear();
        
        count++;
        
        // lineTest();
        // circleTest();
        
        // drawBezier(0, 0, Mouse.x(), Mouse.y(), screenWidth(), screenHeight(), Color.WHITE);
        fillEllipse(screenWidth() / 2, screenHeight() / 2, Math.abs(Mouse.x() * 2 - screenWidth()), Math.abs(Mouse.y() * 2 - screenHeight()), Color.WHITE);
    }
    
    @Override
    protected void destroy()
    {
        // println("OLD Horizontal Avg: %s Min: %s Max: %s", sumLH1 / count, minLH1, maxLH1);
        // println("NEW Horizontal Avg: %s Min: %s Max: %s", sumLH2 / count, minLH2, maxLH2);
        // println("OLD Vertical Avg: %s Min: %s Max: %s", sumLV1 / count, minLV1, maxLV1);
        // println("NEW Vertical Avg: %s Min: %s Max: %s", sumLV2 / count, minLV2, maxLV2);
        // println("OLD Diagonal Avg: %s Min: %s Max: %s", sumLD1 / count, minLD1, maxLD1);
        // println("NEW Diagonal Avg: %s Min: %s Max: %s", sumLD2 / count, minLD2, maxLD2);
        //
        // try (FileWriter writer = new FileWriter("line.csv"))
        // {
        //     writer.write("Index,H1,H2,V1,V2,D1,D2\n");
        //     for (int i = 0; i < count; i++)
        //     {
        //         writer.write(String.format("%s,%s,%s,%s,%s,%s,%s\n", i + 1, LH1.get(i), LH2.get(i), LV1.get(i), LV2.get(i), LD1.get(i), LD2.get(i)));
        //     }
        // }
        // catch (IOException e)
        // {
        //     e.printStackTrace();
        // }
        
        // println("OLD Circle Avg: %s Min: %s Max: %s", sumC1 / count, minC1, maxC1);
        // println("NEW Circle Avg: %s Min: %s Max: %s", sumC2 / count, minC2, maxC2);
        //
        // try (FileWriter writer = new FileWriter("circle.csv"))
        // {
        //     writer.write("Index,C1,C2\n");
        //     for (int i = 0; i < count; i++)
        //     {
        //         writer.write(String.format("%s,%s,%s\n", i + 1, C1.get(i), C2.get(i)));
        //     }
        // }
        // catch (IOException e)
        // {
        //     e.printStackTrace();
        // }
    }
    
    private void lineTest()
    {
        x = screenWidth();
        y = 0;
        
        tLH1 = getTime();
        drawLineOld(0, 0, x, y, Color.WHITE, DrawPattern.DASHED);
        tLH1 = getTime() - tLH1;
        LH1.add(tLH1);
        minLH1 = Math.min(minLH1, tLH1);
        maxLH1 = Math.max(maxLH1, tLH1);
        sumLH1 += tLH1;
        
        tLH2 = getTime();
        drawLine(0, 0, x, y, Color.BLACK, DrawPattern.DASHED);
        tLH2 = getTime() - tLH2;
        LH2.add(tLH2);
        minLH2 = Math.min(minLH2, tLH2);
        maxLH2 = Math.max(maxLH2, tLH2);
        sumLH2 += tLH2;
        
        x = 0;
        y = screenHeight();
        
        tLV1 = getTime();
        drawLineOld(0, 0, x, y, Color.WHITE, DrawPattern.DASHED);
        tLV1 = getTime() - tLV1;
        LV1.add(tLV1);
        minLV1 = Math.min(minLV1, tLV1);
        maxLV1 = Math.max(maxLV1, tLV1);
        sumLV1 += tLV1;
        
        tLV2 = getTime();
        drawLine(0, 0, x, y, Color.BLACK, DrawPattern.DASHED);
        tLV2 = getTime() - tLV2;
        LV2.add(tLV2);
        minLV2 = Math.min(minLV2, tLV2);
        maxLV2 = Math.max(maxLV2, tLV2);
        sumLV2 += tLV2;
        
        x = screenWidth();
        y = screenHeight();
        
        tLD1 = getTime();
        drawLineOld(0, 0, x, y, Color.WHITE, DrawPattern.DASHED);
        tLD1 = getTime() - tLD1;
        LD1.add(tLD1);
        minLD1 = Math.min(minLD1, tLD1);
        maxLD1 = Math.max(maxLD1, tLD1);
        sumLD1 += tLD1;
        
        tLD2 = getTime();
        drawLine(0, 0, x, y, Color.BLACK, DrawPattern.DASHED);
        tLD2 = getTime() - tLD2;
        LD2.add(tLD2);
        minLD2 = Math.min(minLD2, tLD2);
        maxLD2 = Math.max(maxLD2, tLD2);
        sumLD2 += tLD2;
        
        drawLine(screenWidth() / 2, screenHeight() / 2, Mouse.x(), Mouse.y(), 5, Color.WHITE, DrawPattern.SOLID);
    }
    
    private void circleTest()
    {
        x = screenWidth() / 2;
        y = screenHeight() / 2;
        
        tC1 = getTime();
        fillCircleOld(x - 30, y, 20, Color.WHITE);
        tC1 = getTime() - tC1;
        C1.add(tC1);
        minC1 = Math.min(minC1, tC1);
        maxC1 = Math.max(maxC1, tC1);
        sumC1 += tC1;
        
        tC2 = getTime();
        fillCircle(x + 30, y, 20, Color.WHITE);
        tC2 = getTime() - tC2;
        C2.add(tC2);
        minC2 = Math.min(minC2, tC2);
        maxC2 = Math.max(maxC2, tC2);
        sumC2 += tC2;
    }
    
    public static void main(String[] args)
    {
        start(new DrawTest());
    }
}
