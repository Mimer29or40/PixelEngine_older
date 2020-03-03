package pe;

public class SpriteTransformations extends PixelEngine
{
    Sprite sprCar;
    float  rotate = 0.0f;
    
    @Override
    protected void setup()
    {
        size(256, 240, 4, 4);
    
        this.sprCar = Sprite.loadSprite("car_top1.png");
    }
    
    @Override
    protected void draw(double elapsedTime)
    {
        //if (getKey(Keyboard.Keyboard.Z).held) this.rotate -= 2.0f * elapsedTime;
        //if (getKey(Keyboard.Keyboard.X).held) this.rotate += 2.0f * elapsedTime;
        //
        //
        //clear(Color.DARK_CYAN);
        //
        //setDrawMode(DrawMode.ALPHA);
        ////DrawSprite(0, 0, sprCar, 3);
        //
        //
        //Mat2D matFinal = new Mat2D(), matA = new Mat2D(), matB = new Mat2D(), matC = new Mat2D(), matFinalInv = new Mat2D();
        //
        //matA.m20 = -100;
        //matA.m21 = -50;
        //matB.rotate(this.rotate, new Vector3f(0, 0, 1));
        //
        //matB.mul(matA, matC);
        //
        //matA.m20(getScreenWidth() / 2F).m21(getScreenHeight() / 2F);
        //matA.mul(matC, matFinal);
        //
        //matFinal.invert(matFinalInv);
        //
        //// Draws the dumb way, but leaves gaps
        ///*for (int x = 0; x < sprCar->width; x++)
        //{
        //	for (int y = 0; y < sprCar->height; y++)
        //	{
        //		olc::Color p = sprCar->GetPixel(x, y);
        //		float nx, ny;
        //		Forward(matFinal, (float)x, (float)y, nx, ny);
        //		Draw(nx, ny, p);
        //	}
        //}*/
        //
        //// Work out bounding box of sprite post-transformation
        //// by passing through sprite corner locations into
        //// transformation matrix
        //Vector3f temp = new Vector3f();
        //Vector3f p = new Vector3f();
        //float ex, ey;
        //float sx, sy;
        //float px, py;
        //
        //temp.set(0F, 0F, 1F);
        //matFinal.transform(temp, p);
        //px = p.x;
        //py = p.y;
        //sx = px;
        //sy = py;
        //ex = px;
        //ey = py;
        //
        //temp.set(sprCar.width, sprCar.height, 1F);
        //matFinal.transform(temp, p);
        //px = p.x;
        //py = p.y;
        //sx = Math.min(sx, px);
        //sy = Math.min(sy, py);
        //ex = Math.max(ex, px);
        //ey = Math.max(ey, py);
        //
        //temp.set(0.0F, sprCar.height, 1F);
        //matFinal.transform(temp, p);
        //px = p.x;
        //py = p.y;
        //sx = Math.min(sx, px);
        //sy = Math.min(sy, py);
        //ex = Math.max(ex, px);
        //ey = Math.max(ey, py);
        //
        //temp.set(sprCar.width, 0.0F, 1F);
        //matFinal.transform(temp, p);
        //px = p.x;
        //py = p.y;
        //sx = Math.min(sx, px);
        //sy = Math.min(sy, py);
        //ex = Math.max(ex, px);
        //ey = Math.max(ey, py);
        //
        //// Use transformed corner locations in screen space to establish
        //// region of pixels to fill, using inverse transform to sample
        //// sprite at suitable locations.
        //for (float x = sx; x < ex; x++)
        //{
        //    for (float y = sy; y < ey; y++)
        //    {
        //        float nx, ny;
        //        temp.set(x, y, 1F);
        //        matFinal.transform(temp, p);
        //        nx = p.x;
        //        ny = p.y;
        //        draw((int) x, (int) y, sprCar.getPixel((int) (nx + 0.5f), (int) (ny + 0.5f)));
        //    }
        //}
        //
        //setDrawMode(DrawMode.NORMAL);
    }
    
    public static void main(String[] args)
    {
        start(new SpriteTransformations());
    }
}
