package life.naive;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import util.Position2D;

import life.LifeDrawer;
import life.LifeState;

public class NaiveDrawer implements LifeDrawer {

	@Override
	public void draw(int x, int y, int zoom, LifeState state, BufferedImage b) {
		// TODO Auto-generated method stub
		
	}

//DLM
	public void drawSmaller(int x, int y, int zoom, List<Position2D> positions, Graphics2D g, int height, int width) {
		//Zoom will not be lower than -21 anyway
		int[][] counters = new int[width][height];
		char[] pixels = new char[width*height];
		int div = 1 >> -zoom;
		
		for (Position2D pos : positions) {
			int posx = pos.x / div - x;
			int posy = pos.y / div - y;
			
			if (posx < 0 ||
				posy < 0 ||
				posx > width ||
				posy > height) {
				continue;
			}
			counters[posx][posy] ++;
		}

		for (int i=0; i<width; i++) {
			for (int j=0; j<height; j++) {
				pixels[i*width + j] /= (div/256);
			}
		}

		g.drawChars(pixels, 0, width*height, 0, 0);
	}
}
