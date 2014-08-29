package main;

import java.awt.Polygon;
import java.util.Random;

import main.Panel.Engine;

public class Asteroid extends Entity {
	
	private static int maxPts = 10;
	private static int minPts = 4;
	private static int maxRad = 200;
	private static int minRad = 60;
	public Polygon polygon;
	
	
	public Asteroid()
	{
		Random r = new Random();
		polygon = Generate(r.nextInt(10));
	}
	@Override
	public void updatePosition()
	{
		super.updatePosition();
		for(Entity e : Engine.space.objects)
		{
			if(e.collidesWith(this) && !e.equals(this))
			{
				Engine.space.objects.remove(this);
				return;
			}
		}
	}
	public Polygon getPolygon()
	{
		return normalise(polygon);
	}
	public Polygon normalise(Polygon p)
	{
		Polygon p2 = new Polygon();
		for(int i = 0; i < p.xpoints.length; i++)
		{
			p2.addPoint((int)(p.xpoints[i]+x), (int)(p.ypoints[i]+y));
		}
		return p2;
	}
	public static Polygon Generate(int seed)
	{
	        Random generator = new Random(seed);
	                       
	        // Set points using a min and max range
	        int numPoints = (int)Math.floor(generator.nextDouble() * (maxPts-minPts)) + minPts;
	 
	        int[] yPolyPoints = new int[numPoints];
	        int[] xPolyPoints = new int[numPoints];
	 
	        // Set the radius using min and max range as well
	        int radius = (int)Math.floor(generator.nextDouble() * (maxRad - minRad)) + minRad;
	 
	        double crAng = 0,
	       
	                        // Angle between each point
	                    angDiff = Math.toRadians(360.0 / numPoints),
	                   
	                    // Arbitrary radJitter range; notice I subtract each by the jitter / 2.0 to get a +/- jitter with this range
	                    radJitter = radius / 3.0,
	                    angJitter = angDiff * .9;
	       
	        for (int i=0; i<numPoints; i++)
	        {
	                    double tRadius = radius + (generator.nextDouble() * radJitter - radJitter / 2.0);
	                    double tAng = crAng + (generator.nextDouble() * angJitter - angJitter / 2.0);
	                    int nx = (int)(Math.sin(tAng) * tRadius),
	                            ny = (int)(Math.cos(tAng) * tRadius);
	                    yPolyPoints[i] = ny;
	                    xPolyPoints[i] = nx;
	                    crAng += angDiff;
	        }
	 
	        return new Polygon(xPolyPoints, yPolyPoints, numPoints);
	}
	
}
