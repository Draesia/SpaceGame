package main;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

import javax.imageio.ImageIO;

import main.Panel.Engine;

public class Entity {

	private int maxHealth = 100;
	private int health = maxHealth;
	private int id;
	public int rotation;
	public BufferedImage image;
	public float x, y, dx, dy;
	public boolean massless;
	public float maxX, maxY;
	
	public void updatePosition()
	{
		x = x + dx;
		y = y + dy;
		maxX = 8;
		maxY = 8;
		if(massless) return;
//		if(Math.abs(dx) > 0.3F)
//			dx = dx * 0.99F;
//		if(Math.abs(dy) > 0.3F)
//			dy = dy * 0.99F;

		Random r = new Random();
		float rand = r.nextFloat() * .5f + .5f;

		dx = dx * (1 - rand) + dx * rand;
		dy = dy * (1 - rand) + dy * rand;

		if (Engine.space.gravitons.isEmpty() && getMagnitude() > 0.01F) { // if not pulling, slow the particle down
			dx = .97f * dx;
			dy = .97f * dy;
		} else {

			for (int gi = 0; gi < Engine.space.gravitons.size(); gi++) { // for every graviton

				Graviton v = Engine.space.gravitons.get(gi);

				if (v != null) {

					float ClickToX = v.getxPos() - x;
					float ClickToY = v.getyPos() - y;
					float xPull = v.getxPull();
					float yPull = v.getyPull();

					float InvClickToP = InvSqrt((ClickToX * ClickToX + ClickToY * ClickToY));

					dx += xPull * ClickToX * InvClickToP;
					dy += yPull * ClickToY * InvClickToP;

				}
			}
		}
	}
	private final static float InvSqrt(float x) {
		return Float.intBitsToFloat(0x5f3759d5 - (Float.floatToIntBits(x) >> 1));
	}

	public BufferedImage getImage()
	{
		if(image == null)
		{
			updateImage();
		}
		return image;
	}

	public void checkCollisions()
	{

	}

	private void updateImage()
	{
		try {
			image = ImageIO.read(getClass().getResource("images/ship/0.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Point getVector(float force)
	{
		return new Point((int) -(force*Math.sin(Math.toRadians(rotation-180))), (int) (force*Math.cos(Math.toRadians(rotation-180))));
	}
	public int getMagnitude()
	{
		return (int) Math.sqrt(dx*dx + dy*dy);
	}

	public void addDX(float dx)
	{
		//if(this.dx + dx > maxX || this.dx + dx < -maxX) return;
		this.dx = this.dx + dx;
	}
	public void addDY(float dy)
	{
		//if(this.dy + dy > maxY || this.dy + dy < -maxY) return;
		this.dy = this.dy + dy;
	}
	public int getMaxHealth() {
		return maxHealth;
	}
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
	public void addX(int amt)
	{
		this.x = this.x + amt;
	}
	public int getX() {
		return (int) x;
	}
	public int getImageX() {
		if(image != null) return (int) (x-image.getWidth()/2);
		return (int) x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void addY(int amt)
	{
		this.y = this.y + amt;
	}
	public int getY() {
		return (int) y;
	}
	public int getImageY()
	{
		if(image != null) return (int) (y-image.getHeight()/2);
		return (int) y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public boolean collidesWith(Asteroid e) {
		return false;
	}
}
