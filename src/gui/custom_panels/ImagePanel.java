package gui.custom_panels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.omg.CORBA.RepositoryIdHelper;

import utils.MyUtils;

class ImagePanel extends JPanel
{

	private Image img;

	public ImagePanel(String path)
	{
		this.img = MyUtils.loadImage(path, getClass());
		Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		setLayout(null);
	}

	public void paintComponent(Graphics g)
	{
		g.drawImage(img, 0, 0, null);
	}

	public void setImage(String newPath)
	{
		this.img = MyUtils.loadImage(newPath, getClass());
		repaint();
	}
}