package life;

import java.awt.Graphics;

public interface LifeDrawer {
	void draw(int x, int y, int w, int h, int zoom, LifeState state, Graphics g);
}
