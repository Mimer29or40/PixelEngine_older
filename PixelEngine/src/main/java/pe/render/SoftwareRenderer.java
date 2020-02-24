package pe.render;

import org.lwjgl.opengl.GL;
import pe.Profiler;
import pe.Sprite;
import pe.Window;
import pe.color.Color;
import pe.color.Colorc;
import pe.util.PairI;

import java.nio.ByteBuffer;
import java.util.HashSet;

import static org.lwjgl.opengl.GL11.*;
import static pe.PixelEngine.*;

public class SoftwareRenderer extends Renderer
{
    private final HashSet<PairI> drawCords = new HashSet<>();
    private final Color          color     = new Color();
    
    @Override
    public void init()
    {
        createFontSheet();
        
        Renderer.LOGGER.debug("Creating OpenGL Context");
        
        Window.makeCurrent();
        
        GL.createCapabilities();
        
        Renderer.LOGGER.trace("OpenGL: Shader setup");
        {
            Shader shader = new Shader();
            shader.loadVertexFile("shader/pixel.vert");
            shader.loadFragmentFile("shader/pixel.frag");
            shader.validate();
            shader.bind();
        }
        Renderer.LOGGER.trace("OpenGL: Shader Validated");
        
        glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
        
        Renderer.LOGGER.trace("OpenGL: Building Vertex Array");
        
        VertexArray vertexArray = new VertexArray();
        vertexArray.add(2, new float[] {-1.0F, 1.0F, -1.0F, -1.0F, 1.0F, -1.0F, -1.0F, 1.0F, 1.0F, -1.0F, 1.0F, 1.0F});
        vertexArray.bind();
        
        Renderer.LOGGER.trace("OpenGL: Building Texture");
        
        glEnable(GL_TEXTURE_2D);
        
        this.target = this.window = new Texture(screenWidth(), screenHeight());
        this.prev   = new Texture(screenWidth(), screenHeight());
        
        this.window.bind();
    }
    
    @Override
    public void render(boolean update, Profiler profiler)
    {
        ByteBuffer currData = this.window.getData();
        ByteBuffer prevData = this.prev.getData();
        for (int i = 0, n = screenWidth() * screenHeight(); i < n; i++)
        {
            if (update || (currData.getInt(i * this.window.getChannels()) != prevData.getInt(i * this.window.getChannels())))
            {
                this.window.upload();
                glDrawArrays(GL_TRIANGLES, 0, 6);
        
                Window.swap();
        
                this.window.copy(this.prev);
        
                break;
            }
        }
    }
    
    @Override
    protected void clearImpl()
    {
        this.target.clear(this.clear);
    }
    
    public void point(int x, int y, Colorc color)
    {
        if (this.target == null) return;
        
        switch (this.drawMode)
        {
            case NORMAL:
                this.target.setPixel(x, y, color);
                break;
            case MASK:
                if (color.a() == 255) this.target.setPixel(x, y, color);
                break;
            case BLEND:
                this.target.setPixel(x, y, this.blend.blend(color, this.target.getPixel(x, y), this.color));
                break;
            case CUSTOM:
                this.target.setPixel(x, y, this.blendFunc.blend(x, y, color, this.target.getPixel(x, y), this.color));
                break;
        }
    }
    
    @Override
    public void line(int x1, int y1, int x2, int y2)
    {
        DrawPattern pattern = DrawPattern.SOLID;
        pattern.reset();
        
        int dx  = Math.abs(x2 - x1), sx = x1 < x2 ? 1 : -1;
        int dy  = Math.abs(y2 - y1), sy = y1 < y2 ? 1 : -1;
        int err = dx - dy, e2; /* error value e_xy */
        
        if (this.strokeWeight == 1)
        {
            if (dx == 0)
            {
                for (; y1 != y2; y1 += sy)
                {
                    if (pattern.shouldDraw()) this.drawCords.add(new PairI(x1, y1));
                }
                this.drawCords.add(new PairI(x1, y1));
            }
            else if (dy == 0)
            {
                for (; x1 != x2; x1 += sx)
                {
                    if (pattern.shouldDraw()) this.drawCords.add(new PairI(x1, y1));
                }
                this.drawCords.add(new PairI(x1, y1));
            }
            else
            {
                for (; ; )
                {
                    if (pattern.shouldDraw()) this.drawCords.add(new PairI(x1, y1));
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
        }
        else
        {
            int     x3, y3, e3;
            double  ed = dx + dy == 0 ? 1 : Math.sqrt(dx * dx + dy * dy);
            boolean shouldDraw;
            
            for (int w = (this.strokeWeight + 1) / 2; ; )
            {
                shouldDraw = pattern.shouldDraw();
                if (shouldDraw) this.drawCords.add(new PairI(x1, y1));
                e2 = err << 1;
                if (e2 >= -dx)
                {
                    for (e3 = e2 + dy, y3 = y1; e3 < ed * w && (y2 != y3 || dx > dy); e3 += dx)
                    {
                        if (shouldDraw) this.drawCords.add(new PairI(x1, y3 += sy));
                    }
                    if (x1 == x2) break;
                    err -= dy;
                    x1 += sx;
                }
                if (e2 <= dy)
                {
                    for (e3 = dx - e2, x3 = x1; e3 < ed * w && (x2 != x3 || dx < dy); e3 += dy)
                    {
                        if (shouldDraw) this.drawCords.add(new PairI(x3 += sx, y1));
                    }
                    if (y1 == y2) break;
                    err += dx;
                    y1 += sy;
                }
            }
        }
        for (PairI cord : this.drawCords) point(cord.a, cord.b, this.stroke);
        this.drawCords.clear();
    }
    
    @Override
    public void bezier(int x1, int y1, int x2, int y2, int x3, int y3)
    {
        // TODO - http://members.chello.at/~easyfilter/bresenham.html
        int  sx = x3 - x2, sy = y3 - y2;
        long xx = x1 - x2, yy = y1 - y2, xy;         /* relative values for checks */
        
        double dx, dy, err, cur = xx * sy - yy * sx;                    /* curvature */
        
        assert (xx * sx <= 0 && yy * sy <= 0);  /* sign of gradient must not change */
        
        if (sx * (long) sx + sy * (long) sy > xx * xx + yy * yy)
        { /* begin with longer part */
            x3  = x1;
            x1  = sx + x2;
            y3  = y1;
            y1  = sy + y2;
            cur = -cur;  /* swap P0 P2 */
        }
        if (cur != 0)
        {                                    /* no straight line */
            xx += sx;
            xx *= sx = x1 < x3 ? 1 : -1;           /* x step direction */
            yy += sy;
            yy *= sy = y1 < y3 ? 1 : -1;           /* y step direction */
            xy       = 2 * xx * yy;
            xx *= xx;
            yy *= yy;          /* differences 2nd degree */
            if (cur * sx * sy < 0)
            {                           /* negated curvature? */
                xx  = -xx;
                yy  = -yy;
                xy  = -xy;
                cur = -cur;
            }
            dx  = 4.0 * sy * cur * (x2 - x1) + xx - xy;             /* differences 1st degree */
            dy  = 4.0 * sx * cur * (y1 - y2) + yy - xy;
            xx += xx;
            yy += yy;
            err = dx + dy + xy;                /* error 1st step */
            do
            {
                point(x1, y1, this.stroke);                  /* plot curve */
                if (x1 == x3 && y1 == y3) return;  /* last pixel -> curve finished */
                boolean yStep = 2 * err < dx;      /* save value for test of y step */
                if (2 * err > dy)
                {
                    x1 += sx;
                    dx -= xy;
                    err += dy += yy;
                } /* x step */
                if (yStep)
                {
                    y1 += sy;
                    dy -= xy;
                    err += dx += xx;
                } /* y step */
            } while (dy < dx);           /* gradient negates -> algorithm fails */
        }
        line(x1, y1, x3, y3);
    }
    
    @Override
    public void circle(int x, int y, int radius)
    {
        if (radius < 1) return;
        if (this.fill.a() != 0)
        {
            int x0 = 0, y0 = radius, d = 3 - 2 * radius, i;
            
            while (y0 >= x0)
            {
                for (i = x - x0; i <= x + x0; i++) this.drawCords.add(new PairI(i, y - y0));
                for (i = x - y0; i <= x + y0; i++) this.drawCords.add(new PairI(i, y - x0));
                for (i = x - x0; i <= x + x0; i++) this.drawCords.add(new PairI(i, y + y0));
                for (i = x - y0; i <= x + y0; i++) this.drawCords.add(new PairI(i, y + x0));
                
                if (d < 0)
                {
                    d += 4 * x0++ + 6;
                }
                else
                {
                    d += 4 * (x0++ - y0--) + 10;
                }
            }
            for (PairI cord : this.drawCords) point(cord.a, cord.b, this.fill);
            this.drawCords.clear();
        }
        if (this.stroke.a() != 0)
        {
            int xr = -radius, yr = 0, err = 2 - 2 * radius;
            do
            {
                point(x - xr, y + yr, this.stroke); /*   I. Quadrant */
                point(x - yr, y - xr, this.stroke); /*  II. Quadrant */
                point(x + xr, y - yr, this.stroke); /* III. Quadrant */
                point(x + yr, y + xr, this.stroke); /*  IV. Quadrant */
                radius = err;
                if (radius <= yr) err += ++yr * 2 + 1;            /* e_xy+e_y < 0 */
                if (radius > xr || err > yr) err += ++xr * 2 + 1; /* e_xy+e_x > 0 or no 2nd y-step */
            } while (xr < 0);
        }
    }
    
    @Override
    public void ellipse(int x, int y, int w, int h)
    {
        // TODO - http://members.chello.at/~easyfilter/bresenham.html
        if (w < 1 || h < 1) return;
        
        if (this.fill.a() != 0)
        {
            int  x0  = x - w / 2, y0 = y - h / 2;
            int  x1  = x + w / 2, y1 = y + h / 2;
            int  b1  = h & 1; /* values of diameter */
            long dx  = 4 * (1 - w) * h * h, dy = 4 * (b1 + 1) * w * w; /* error increment */
            long err = dx + dy + b1 * w * w, e2; /* error of 1.step */
            
            if (x0 > x1)
            {
                x0 = x1;
                x1 += w;
            } /* if called with swapped points */
            if (y0 > y1) y0 = y1; /* .. exchange them */
            y0 += (h + 1) / 2;
            y1 = y0 - b1;   /* starting pixel */
            w *= 8 * w;
            b1 = 8 * h * h;
            
            do
            {
                for (int i = x0; i < x1; i++) this.drawCords.add(new PairI(i, y0));
                for (int i = x0; i < x1; i++) this.drawCords.add(new PairI(i, y1));
                e2 = 2 * err;
                if (e2 <= dy)
                {
                    y0++;
                    y1--;
                    err += dy += w;
                }  /* y step */
                if (e2 >= dx || 2 * err > dy)
                {
                    x0++;
                    x1--;
                    err += dx += b1;
                } /* x step */
            } while (x0 <= x1);
            
            while (y0 - y1 < h)
            {  /* too early stop of flat ellipses w=1 */
                this.drawCords.add(new PairI(x0 - 1, y0)); /* -> finish tip of ellipse */
                this.drawCords.add(new PairI(x1 + 1, y0++));
                this.drawCords.add(new PairI(x0 - 1, y1));
                this.drawCords.add(new PairI(x1 + 1, y1--));
            }
            for (PairI cord : this.drawCords) point(cord.a, cord.b, this.fill);
            this.drawCords.clear();
        }
        if (this.stroke.a() != 0)
        {
            int  x0  = x - w / 2, y0 = y - h / 2;
            int  x1  = x + w / 2, y1 = y + h / 2;
            int  b1  = h & 1; /* values of diameter */
            long dx  = 4 * (1 - w) * h * h, dy = 4 * (b1 + 1) * w * w; /* error increment */
            long err = dx + dy + b1 * w * w, e2; /* error of 1.step */
            
            if (x0 > x1)
            {
                x0 = x1;
                x1 += w;
            } /* if called with swapped points */
            if (y0 > y1) y0 = y1; /* .. exchange them */
            y0 += (h + 1) / 2;
            y1 = y0 - b1;   /* starting pixel */
            w *= 8 * w;
            b1 = 8 * h * h;
            
            do
            {
                point(x1, y0, this.stroke); /*   I. Quadrant */
                point(x0, y0, this.stroke); /*  II. Quadrant */
                point(x0, y1, this.stroke); /* III. Quadrant */
                point(x1, y1, this.stroke); /*  IV. Quadrant */
                e2 = 2 * err;
                if (e2 <= dy)
                {
                    y0++;
                    y1--;
                    err += dy += w;
                }  /* y step */
                if (e2 >= dx || 2 * err > dy)
                {
                    x0++;
                    x1--;
                    err += dx += b1;
                } /* x step */
            } while (x0 <= x1);
            
            while (y0 - y1 < h)
            {  /* too early stop of flat ellipses w=1 */
                point(x0 - 1, y0, this.stroke); /* -> finish tip of ellipse */
                point(x1 + 1, y0++, this.stroke);
                point(x0 - 1, y1, this.stroke);
                point(x1 + 1, y1--, this.stroke);
            }
        }
    }
    
    @Override
    public void rect(int x, int y, int w, int h)
    {
        if (w < 1 || h < 1) return;
        
        if (this.fill.a() != 0)
        {
            int x2 = x + w;
            int y2 = y + h;
            
            x  = Math.max(0, Math.min(x, screenWidth()));
            y  = Math.max(0, Math.min(y, screenHeight()));
            x2 = Math.max(0, Math.min(x2, screenWidth()));
            y2 = Math.max(0, Math.min(y2, screenHeight()));
            
            for (int i = x; i < x2; i++)
            {
                for (int j = y; j < y2; j++)
                {
                    point(i, j, this.fill);
                }
            }
        }
        if (this.stroke.a() != 0)
        {
            line(x, y, x + w - 1, y);
            line(x + w - 1, y, x + w - 1, y + h - 1);
            line(x + w - 1, y + h - 1, x, y + h - 1);
            line(x, y + h - 1, x, y);
        }
    }
    
    @Override
    public void triangle(int x1, int y1, int x2, int y2, int x3, int y3)
    {
        if (this.fill.a() != 0)
        {
            int minX, minY, maxX, maxY;
            
            minX = Math.min(x1, Math.min(x2, x3));
            minY = Math.min(y1, Math.min(y2, y3));
            maxX = Math.max(x1, Math.max(x2, x3));
            maxY = Math.max(y1, Math.max(y2, y3));
            
            minX = Math.max(0, Math.min(minX, screenWidth()));
            minY = Math.max(0, Math.min(minY, screenHeight()));
            maxX = Math.max(0, Math.min(maxX, screenWidth()));
            maxY = Math.max(0, Math.min(maxY, screenHeight()));
            
            int abc = Math.abs(x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2));
            int abp, apc, pbc;
            
            for (int i = minX; i <= maxX; i++)
            {
                for (int j = minY; j <= maxY; j++)
                {
                    pbc = Math.abs(i * (y2 - y3) + x2 * (y3 - j) + x3 * (j - y2));
                    apc = Math.abs(x1 * (j - y3) + i * (y3 - y1) + x3 * (y1 - j));
                    abp = Math.abs(x1 * (y2 - j) + x2 * (j - y1) + i * (y1 - y2));
                    if (abc == pbc + apc + abp) this.drawCords.add(new PairI(i, j));
                }
            }
            for (PairI cord : this.drawCords) point(cord.a, cord.b, this.fill);
            this.drawCords.clear();
        }
        if (this.stroke.a() != 0)
        {
            line(x1, y1, x2, y2);
            line(x2, y2, x3, y3);
            line(x3, y3, x1, y1);
        }
    }
    
    @Override
    public void partialSprite(int x, int y, Sprite sprite, int ox, int oy, int w, int h, double scale)
    {
        if (scale <= 0.0 || sprite == null) return;
        
        if (scale == (int) scale)
        {
            if (scale == 1)
            {
                for (int i = 0; i < w; i++)
                {
                    for (int j = 0; j < h; j++)
                    {
                        point(x + i, y + j, sprite.getPixel(i + ox, j + oy));
                    }
                }
            }
            else
            {
                for (int i = 0; i < w; i++)
                {
                    for (int j = 0; j < h; j++)
                    {
                        for (int is = 0; is < scale; is++)
                        {
                            for (int js = 0; js < scale; js++)
                            {
                                point(x + (i * (int) scale) + is, y + (j * (int) scale) + js, sprite.getPixel(i + ox, j + oy));
                            }
                        }
                    }
                }
            }
        }
        else
        {
            int newW = Math.max((int) Math.round(w * scale), 1);
            int newH = Math.max((int) Math.round(h * scale), 1);
            
            Color p = new Color();
            
            double r, g, b, a, total;
            double uMin, uMax, vMin, vMax;
            double xPercent, yPercent, pPercent;
            
            for (int j = 0; j < newH; j++)
            {
                for (int i = 0; i < newW; i++)
                {
                    r = g = b = a = total = 0.0;
                    
                    uMin = i / (double) newW * w;
                    uMax = (i + 1) / (double) newW * w;
                    vMin = j / (double) newH * h;
                    vMax = (j + 1) / (double) newH * h;
                    
                    for (int v = (int) vMin; v < (int) Math.ceil(vMax); v++)
                    {
                        for (int u = (int) uMin; u < (int) Math.ceil(uMax); u++)
                        {
                            xPercent = u < uMin ? 1.0 - uMin + u : u + 1 > uMax ? uMax - u : 1.0;
                            yPercent = v < vMin ? 1.0 - vMin + v : v + 1 > vMax ? vMax - v : 1.0;
                            pPercent = xPercent * yPercent;
                            
                            p.set(sprite.getPixel(ox + u, oy + v));
                            
                            r += pPercent * p.r();
                            g += pPercent * p.g();
                            b += pPercent * p.b();
                            a += pPercent * p.a();
                            
                            total += pPercent * 255;
                        }
                    }
    
                    p.set((float) (r / total), (float) (g / total), (float) (b / total), (float) (a / total));
                    point(x + i, y + j, p);
                }
            }
        }
    }
    
    @Override
    public void string(int x, int y, String text, double scale)
    {
        if (scale <= 0.0) return;
        
        int  sx = 0, sy = 0;
        char c;
        int  ox, oy;
        
        DrawMode prev = drawMode();
        if (scale == (int) scale)
        {
            drawMode(this.stroke.a() == 255 ? DrawMode.MASK : DrawMode.BLEND);
            
            for (int ci = 0; ci < text.length(); ci++)
            {
                c = text.charAt(ci);
                
                if (c == '\n')
                {
                    sx = 0;
                    sy += 8 * scale;
                }
                else
                {
                    ox = (c - 32) % 16;
                    oy = (c - 32) / 16;
                    
                    if (scale == 1)
                    {
                        for (int i = 0; i < 8; i++)
                        {
                            for (int j = 0; j < 8; j++)
                            {
                                if (this.font.getPixel(i + ox * 8, j + oy * 8).r() > 0) point(x + sx + i, y + sy + j, this.stroke);
                            }
                        }
                    }
                    else
                    {
                        for (int i = 0; i < 8; i++)
                        {
                            for (int j = 0; j < 8; j++)
                            {
                                if (this.font.getPixel(i + ox * 8, j + oy * 8).r() > 0)
                                {
                                    for (int is = 0; is < scale; is++)
                                    {
                                        for (int js = 0; js < scale; js++)
                                        {
                                            point(x + sx + (i * (int) scale) + is, y + sy + (j * (int) scale) + js, this.stroke);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    sx += 8 * scale;
                }
            }
        }
        else
        {
            drawMode(DrawMode.BLEND);
            
            int size = Math.max(scaleToPixels(scale), 1);
            
            double p, total;
            double uMin, uMax, vMin, vMax;
            double xPercent, yPercent, pPercent;
            
            for (int ci = 0; ci < text.length(); ci++)
            {
                c = text.charAt(ci);
                
                if (c == '\n')
                {
                    sx = 0;
                    sy += 8 * scale;
                }
                else
                {
                    ox = (c - 32) % 16;
                    oy = (c - 32) / 16;
                    
                    for (int j = 0; j < size; j++)
                    {
                        for (int i = 0; i < size; i++)
                        {
                            p = total = 0.0;
                            
                            uMin = i / (double) size * 8.0;
                            uMax = (i + 1) / (double) size * 8.0;
                            vMin = j / (double) size * 8.0;
                            vMax = (j + 1) / (double) size * 8.0;
                            
                            for (int v = (int) vMin; v < (int) Math.ceil(vMax); v++)
                            {
                                for (int u = (int) uMin; u < (int) Math.ceil(uMax); u++)
                                {
                                    xPercent = u < uMin ? 1.0 - uMin + u : u + 1 > uMax ? uMax - u : 1.0;
                                    yPercent = v < vMin ? 1.0 - vMin + v : v + 1 > vMax ? vMax - v : 1.0;
                                    pPercent = xPercent * yPercent;
                                    
                                    if (this.font.getPixel(ox * 8 + u, oy * 8 + v).r() > 0) p += pPercent;
                                    total += pPercent;
                                }
                            }
                            
                            if (p > 0)
                            {
                                this.color.set(this.stroke);
                                this.color.a((int) (this.stroke.a() * p / total));
                                point(x + sx + i, y + sy + j, this.color);
                            }
                        }
                    }
                    sx += 8 * scale;
                }
            }
        }
        drawMode(prev);
    }
    
    private void createFontSheet()
    {
        // PixelEngine.LOGGER.debug("Generating Font Data");
        
        String data = "";
        data += "?Q`0001oOch0o01o@F40o0<AGD4090LAGD<090@A7ch0?00O7Q`0600>00000000";
        data += "O000000nOT0063Qo4d8>?7a14Gno94AA4gno94AaOT0>o3`oO400o7QN00000400";
        data += "Of80001oOg<7O7moBGT7O7lABET024@aBEd714AiOdl717a_=TH013Q>00000000";
        data += "720D000V?V5oB3Q_HdUoE7a9@DdDE4A9@DmoE4A;Hg]oM4Aj8S4D84@`00000000";
        data += "OaPT1000Oa`^13P1@AI[?g`1@A=[OdAoHgljA4Ao?WlBA7l1710007l100000000";
        data += "ObM6000oOfMV?3QoBDD`O7a0BDDH@5A0BDD<@5A0BGeVO5ao@CQR?5Po00000000";
        data += "Oc``000?Ogij70PO2D]??0Ph2DUM@7i`2DTg@7lh2GUj?0TO0C1870T?00000000";
        data += "70<4001o?P<7?1QoHg43O;`h@GT0@:@LB@d0>:@hN@L0@?aoN@<0O7ao0000?000";
        data += "OcH0001SOglLA7mg24TnK7ln24US>0PL24U140PnOgl0>7QgOcH0K71S0000A000";
        data += "00H00000@Dm1S007@DUSg00?OdTnH7YhOfTL<7Yh@Cl0700?@Ah0300700000000";
        data += "<008001QL00ZA41a@6HnI<1i@FHLM81M@@0LG81?O`0nC?Y7?`0ZA7Y300080000";
        data += "O`082000Oh0827mo6>Hn?Wmo?6HnMb11MP08@C11H`08@FP0@@0004@000000000";
        data += "00P00001Oab00003OcKP0006@6=PMgl<@440MglH@000000`@000001P00000000";
        data += "Ob@8@@00Ob@8@Ga13R@8Mga172@8?PAo3R@827QoOb@820@0O`0007`0000007P0";
        data += "O`000P08Od400g`<3V=P0G`673IP0`@3>1`00P@6O`P00g`<O`000GP800000000";
        data += "?P9PL020O`<`N3R0@E4HC7b0@ET<ATB0@@l6C4B0O`H3N7b0?P01L3R000000020";
        
        // PixelEngine.LOGGER.trace("Font Sheet Sprite Generated");
        this.font = new Sprite(128, 48);
        Color p = new Color();
        for (int b = 0, px = 0, py = 0; b < 1024; b += 4)
        {
            int sym1 = (int) data.charAt(b) - 48;
            int sym2 = (int) data.charAt(b + 1) - 48;
            int sym3 = (int) data.charAt(b + 2) - 48;
            int sym4 = (int) data.charAt(b + 3) - 48;
            int r    = sym1 << 18 | sym2 << 12 | sym3 << 6 | sym4;
            
            for (int i = 0; i < 24; i++)
            {
                int k = (r & (1 << i)) != 0 ? 255 : 0;
                this.font.setPixel(px, py, p.set(k, k, k, k));
                if (++py == 48)
                {
                    px++;
                    py = 0;
                }
            }
        }
        // PixelEngine.LOGGER.trace("Font Sheet Generation Finished");
    }
}
