package ship;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import main.Bullet;

public class LaserGun extends ShipPart {

	Point spawningLocation;
	Point bulletVelocity;
	private long nextFire;
	public LaserGun(Rectangle shape, Point spawningLocation, Point bulletVelocity)
	{
		super(shape);
		this.interiorColor = new Color(0xDD2C2C);
		this.exteriorColor = new Color(0xDD1C1C);
		this.spawningLocation = spawningLocation;
		this.bulletVelocity = bulletVelocity;
	}
	
	public boolean canFire()
	{
		return(System.currentTimeMillis() > nextFire);
	}

	public void fire()
	{
		nextFire = System.currentTimeMillis() + 300;
	}
}
