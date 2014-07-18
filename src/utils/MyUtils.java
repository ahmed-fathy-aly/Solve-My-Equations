package utils;

import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class MyUtils
{
	public static Font fontSmall = new Font("Trebuchet MS", Font.PLAIN, 16);
	public static Font fontLarge = new Font("Trebuchet MS", Font.PLAIN, 22);
	
	public static int width = 1280;
	public static int height = 600;
	
	public enum Solvers 
	{
		BISECTION_METHOD("Bisection method"), 
		NEWTON_SINGLE_EQUATION("Newton single equation"), 
		NEWTON_SYSTEM_EQUATIONS("Newton system of equations");
		
		private String text;
		Solvers(String text)
		{
			this.text = text;
		}
		public String toString()
		{
			return text;
		}
	}
	public static  ImageIcon loadIcon(String path, Class c)
	{
		URL url = c.getResource(path);
		ImageIcon icon = new ImageIcon(url);
		return icon;
	}
	
	public static BufferedImage loadImage(String path, Class c)
	{
		BufferedImage img = null;
		try {
		    ClassLoader cldr = c.getClassLoader();

		    URL imageURL   = cldr.getResource(path);
		    img = ImageIO.read(imageURL);
		} catch (IOException e)
		{
			System.out.println(e);
		}
		return img;
	}


}
