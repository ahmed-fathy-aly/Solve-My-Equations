package gui.custom_panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import Core.Equation;
import utils.MyUtils;

public class EquationPanel extends JPanel
{
	/* member variables */
	private JTextField textFieldEquation;
	private ImagePanel checkImage;
	private ImagePanel backgroundImage;
	private String functionName;
	private EquationPanelListener listener;
	private boolean isCorrectEquation;

	public EquationPanel(String functionName)
	{
		this.functionName = functionName;
		this.isCorrectEquation = true;
		setGui();
	}

	/**
	 * sets gui components
	 */
	private void setGui()
	{
		// the layout
		JLayeredPane layeredPane = new JLayeredPane();

		// the background
		backgroundImage = new ImagePanel("images/greeBorder.png");
		setPreferredSize(backgroundImage.getPreferredSize());

		// the text field
		textFieldEquation = new JTextField();
		textFieldEquation.setFont(MyUtils.fontLarge);
		textFieldEquation.setBounds((int) (backgroundImage.getPreferredSize().width * 0.1),
				(int) (backgroundImage.getPreferredSize().height * 0.1), (int) (backgroundImage
						.getPreferredSize().width * 0.8),
				(int) (backgroundImage.getPreferredSize().height * 0.8));
		textFieldEquation.getDocument().addDocumentListener(new DocumentListener()
		{
			public void removeUpdate(DocumentEvent arg0)
			{
				textChanged();
			}

			public void insertUpdate(DocumentEvent arg0)
			{
				textChanged();
			}

			@Override
			public void changedUpdate(DocumentEvent arg0)
			{
				textChanged();
			}

			private void textChanged()
			{
				onTextChanged();
			}
		});

		layeredPane.add(textFieldEquation, 1);
		layeredPane.add(backgroundImage, 0);
		
		layeredPane.setPreferredSize(backgroundImage.getPreferredSize());
		add(layeredPane);
		

	}

	/**
	 * tests the equation 
	 */
	public void onTextChanged()
	{
		String newText = textFieldEquation.getText();
		
		// get the variables
		String[] variableNames;
		if (this.listener != null)
			variableNames = this.listener.getVariables();
		else
			variableNames = new String[0];
		
		// try to parse the equation
		try
		{
			Equation equation = new Equation(variableNames.length, variableNames, newText);
			double[] values = new double[variableNames.length];
			equation.evaluateAt(values);
			// change the equation to correct
			if (this.isCorrectEquation == false)
			{
				this.backgroundImage.setImage("images/greeBorder.png");
				this.repaint();
				this.isCorrectEquation = true;
				
			}
		} catch (Exception e)
		{
			// change the equation to incorrect 
			if (this.isCorrectEquation)
			{
				this.backgroundImage.setImage("images/redBorder.png");
				this.repaint();
				this.isCorrectEquation = false;
			}
				
			
		}
		
	}
	
	public void setListener(EquationPanelListener listener)
	{
		this.listener = listener;
	}
	
	/**
	 * @return the text in the text field
	 */
	public String getEquationString()
	{
		return this.textFieldEquation.getText();
	}
	
	public interface EquationPanelListener
	{
		String[] getVariables();
	}

	public void setEquation(String string)
	{
		this.textFieldEquation.setText(string);
		
	}

	
}
