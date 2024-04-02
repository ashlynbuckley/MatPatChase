package MatPatChase;

import java.awt.*;

public class Player extends Sprites{
	
	public Player(Image i, int x, int y) {
		super(i, y, y);
		width = gameImage.getWidth(null);
		height = gameImage.getHeight(null);
	}

}
