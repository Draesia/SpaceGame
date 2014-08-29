package ship;

import java.awt.Color;
import java.awt.Rectangle;

public class ShipPart {
	
	public Rectangle shape;
	public Color interiorColor;
	public Color exteriorColor;
	public ShipPart(Rectangle shape)
	{
		this.shape = shape;
		this.interiorColor = new Color(0x2C2C2C);
		this.exteriorColor = new Color(0x1C1C1C);
	}
}
