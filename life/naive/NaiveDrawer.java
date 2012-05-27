package life.naive;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import util.Position2D;

import life.LifeDrawer;
import life.LifeState;

public class NaiveDrawer implements LifeDrawer {

	@Override
	public void draw(int x, int y, int zoom, LifeState state, BufferedImage b) {
		List<Position2D> alive = ((NaiveState) state).getAliveCells();
		Graphics g = b.getGraphics();
		g.setColor(Color.white);
		int w = b.getWidth(), h = b.getHeight();
		int cellSize = (zoom<0)?(1<<-zoom):(1<<zoom);
		int factor = (zoom<0)?(1<<-zoom):(1<<zoom);
		for(Position2D p : alive) {
			if(zoom < 0) {
				if(x + p.x/factor <= 0 || x + p.x/factor >= w || y + p.y/factor <= 0 || y + p.y/factor >= h)
					continue;
				int rgb = Color.white.getRGB();
				b.setRGB(x + p.x/factor, y + p.y/factor, rgb);
			} else {
				if(x + p.x*factor + cellSize <= 0 || x + p.x*factor >= w || y + p.y*factor + cellSize <= 0 || y + p.y*factor >= h)
					continue;
				g.fillRect(x + p.x*factor, y + p.y*factor, cellSize, cellSize);
			}
		}
	}
}
