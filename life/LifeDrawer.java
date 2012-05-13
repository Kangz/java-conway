package life;

import java.awt.Graphics2D;

public interface LifeDrawer {
	void draw(int x, int y, int zoom, LifeState state, Graphics2D g);
}
