package MatPatChase;

import java.awt.*;

public class MatPat extends Sprites{

	public MatPat(Image i, int x, int y) {
		super(i, y, y);
		width = gameImage.getWidth(null);
		height = gameImage.getHeight(null);
	}
	
	@Override
	public void move(){
		//A* pathfinding
        x += xSpeed;
        y += ySpeed;
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
		
	    if (
	        ( (x1<x2 && x1+w1>x2) || (x2<x1 && x2+w2>x1)
	        )
	        &&
	        ( (y1<y2 && y1+h1>y2) || (y2<y1 && y2+h2>y1) )
	        ) {
	        return true;}
        
        return false;
	}
}
