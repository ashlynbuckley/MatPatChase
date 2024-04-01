package MatPatChase;

import java.awt.*;

public class MatPat extends Sprites{

	public MatPat(Image i, int x, int y) {
		super(i, y, y);
		
	}
	
	@Override
	public void move(){
		//A* pathfinding
        x += xSpeed;
        y += ySpeed;
    }
}
