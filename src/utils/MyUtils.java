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
	
	public static int width = 900;
	public static int height = 500;
	
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
