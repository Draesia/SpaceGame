package main;

import java.awt.Color;
import java.awt.Graphics2D;

import main.Panel.Engine;

public class Planet extends Entity {
	
	public Graviton graviton;
	public int radius;
	public float force;
	public Planet(int x, int y, int radius, float force, Space space)
	{
		this.x = x;
		this.y = y;
		this.force = force;
		graviton = new Graviton();
		graviton.setGraviton(x, y, force, force);
		space.gravitons.add(graviton);
		this.radius = radius;
	}
	
	@Override 
	public void updatePosition() {
		super.updatePosition();
		graviton.setGraviton(x, y, force, force);
	}

	public void drawPlanet(Graphics2D g2) {
		g2.setColor(Color.BLUE);
		g2.fillOval((int)x-radius/2,(int)y-radius/2, radius, radius);
	}

}
