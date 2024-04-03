package MatPatChase;

import java.awt.*;

public class MatPat extends Sprites{

	public MatPat(Image i, int x, int y) {
		super(i, y, y);
		width = gameImage.getWidth(null);
        height = gameImage.getHeight(null);
	}
	
	//pathfinding alg
	public void move(int targx, int targy) {
		int speed = 2;
		
		//use the target's x and y  to determine direction
        if (targx<x) {
            x = x - speed;
        }
        else if (targx>x) {
            x = x + speed;
        }
        if (targy<y) {
            y = y - speed;
        }
        else if (targy>y) {
            y = y + speed;
        }
    }
	
	public boolean checkCollision(Sprites other) {
		//matpat's data
		int x1 = (int)this.x;
		int y1 = (int)this.y;
		int w1 = this.width;
		int h1 = this.height;
			
		//other potential sprite
		int x2 = (int)other.x;
		int y2 = (int)other.y;
		int w2 = other.width;
		int h2 = other.height;
		
		//overlapping coordinates
	    if (
	        ( (x1<x2 && x1+w1>x2) || (x2<x1 && x2+w2>x1)
	        )
	        &&
	        ( (y1<y2 && y1+h1>y2) || (y2<y1 && y2+h2>y1) )
	        ) {
	        return true;}
        //not overlapping return false, boolean used in main
        return false;
	}
}
