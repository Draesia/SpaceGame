package main;

import java.awt.Color;
import java.awt.Graphics2D;

public class Bullet extends Entity {

	public static final int LARGE_INT = 10000;
	public static final Color trajectoryColor = new Color(0x5D1010);
	public Bullet(int x, int y, int dx, int dy)
	{
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.massless = true;
	}
	public void drawTrajectory(Graphics2D g2d)
	{
		g2d.setColor(trajectoryColor);
		g2d.drawLine((int)x, (int)y, (int)dx*LARGE_INT, (int)dy*LARGE_INT);
	}
}
