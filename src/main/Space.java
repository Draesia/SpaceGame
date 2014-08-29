package main;

import java.util.ArrayList;
import java.util.Random;

import ship.Ship;

public class Space {
	public ArrayList<Entity> objects = new ArrayList<Entity>();
	public ArrayList<Graviton> gravitons = new ArrayList<Graviton>();
	public Ship myShip;

	public Space()
	{
		myShip = new Ship();
		objects.add(myShip);
//		for(int i = 0; i < 10; i++)
//			objects.add(new Ship(i*90, i*90));
		Random r = new Random();
		for(int i = 0; i < 600; i++)
		{
			int x = (int) (r.nextGaussian()*5000);
			int y = (int) (r.nextGaussian()*5000);
			Asteroid a = new Asteroid();
			a.x = x;
			a.y = y;
			a.dx = (float) r.nextGaussian();
			a.dy = (float) r.nextGaussian();
			objects.add(a);
		}
	}
	
	public void updatePosition()
	{
		//Here we do a new tick of position, relative to our velocity.
		for(Entity e : objects) {
			e.updatePosition();
			//e.updatePosition();
			//e.updatePosition();
		}
	}
}
