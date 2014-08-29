package ship;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import main.Bullet;
import main.Entity;
import main.Panel.Engine;

public class Ship extends Entity {
	
	public ArrayList<ShipPart> shipparts = new ArrayList<ShipPart>();
	public Ship()
	{
		dx = 0;
		dy = 0;
		rotation = (int) (Math.random()*360);
		shipparts.add(new ShipPart(new Rectangle(0, 0, 20, 50)));
		shipparts.add(new ShipPart(new Rectangle(-20, 20, 20, 30)));
		shipparts.add(new ShipPart(new Rectangle(20, 20, 20, 30)));
		shipparts.add(new ShipPart(new Rectangle(-15, 50, 10, 10)));
		shipparts.add(new ShipPart(new Rectangle(25, 50, 10, 10)));
		shipparts.add(new LaserGun(new Rectangle(-12, -20, 5, 60), new Point(-10, -20), new Point(0, -10)));
		shipparts.add(new LaserGun(new Rectangle(27, -20, 5, 60), new Point(30, -20), new Point(0, -10)));
		accelerate(0.1F);
	}
	public Ship(int x, int y)
	{
		this();
		this.x = x;
		this.y = y;
	}
	public Point getVector(float force)
	{
		return new Point ((int) -(force*Math.sin(Math.toRadians(rotation-180))), (int) (force*Math.cos(Math.toRadians(rotation-180))));
	}
	public void accelerate(float force)
	{
		float nx = -(float) (force*Math.sin(Math.toRadians(rotation-180)));
		float ny = (float) (force*Math.cos(Math.toRadians(rotation-180)));
		if(dx + nx > -maxX && dx + nx < maxX) addDX(nx);
		if(dy + ny > -maxY && dy + ny < maxY) addDY(ny);
    	
    	Point p = getCenter();
    	//Engine.render.emitParticles(p.x, p.y, (int)dx, (int)dy);
	}
	public void drawShip(Graphics2D g2d)
	{
		Graphics2D g2 = (Graphics2D) g2d.create();
		Point p = getCenter();
		
		g2.rotate(Math.toRadians(rotation), p.x, p.y);
		
		for(ShipPart sp : shipparts)
		{
			g2.setColor(sp.interiorColor);
			//System.out.println("Drawing at X"+(cameraX-(x+sp.shape.getX()))+" Y:"+(int)(cameraY-(y+sp.shape.getY())));
			g2.fillRect((int)(x+sp.shape.getX()),(int)(y+sp.shape.getY()), (int)sp.shape.getWidth(), (int)sp.shape.getHeight());
			g2.setColor(sp.exteriorColor);
			g2.drawRect((int)(x+sp.shape.getX()),(int)(y+sp.shape.getY()), (int)sp.shape.getWidth(), (int)sp.shape.getHeight());
		}
		g2.drawOval(p.x-2, p.y-2, 4, 4);
		g2.dispose();
		
	}
	public Point getCenter()
	{	
		return new Point((int)(shipparts.get(0).shape.getCenterX()+x),(int)(shipparts.get(0).shape.getCenterY()+y));
	}
	public ArrayList<Bullet> fire()
	{
		ArrayList<Bullet> bullets = new ArrayList<Bullet>();
		for(ShipPart sp : shipparts)
		{
			if(sp instanceof LaserGun)
			{
				LaserGun l = (LaserGun) sp;
				if(l.canFire()) {
					l.fire();
					bullets.add(new Bullet(getX()+l.shape.x, getY()+l.shape.y,(int) (dx+(-7*Math.sin(Math.toRadians(rotation-180)))),(int) (dy+(7*Math.cos(Math.toRadians(rotation-180))))));
				}
			}
		}
		return bullets;
	}
}
