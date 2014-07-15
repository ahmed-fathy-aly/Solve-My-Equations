package gui.main_panels;

import gui.custom_panels.EquationPanel;
import gui.custom_panels.EquationPanel.EquationPanelListener;
import gui.custom_panels.VariableAdderPanel;
import gui.custom_panels.VariableAdderPanel.VariableAdderListener;
import gui.custom_panels.VariableSetterPanel;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import utils.MyUtils;
import Core.Equation;

public class FunctionEvaluatorPanel extends JPanel implements EquationPanelListener,
		VariableAdderListener
{
	private VariableAdderPanel panelVariableAdder;
	private EquationPanel equationPanel;
	private VariableSetterPanel panelVariableSetter;
	private JButton buttonEvaluate;
	private JTextArea textFieldResult;

	/**
	 * Create the panel.
	 */
	public FunctionEvaluatorPanel()
	{
		setGui();
		addTemplateInput();
	}

	private void addTemplateInput()
	{
		this.panelVariableAdder.addVariable("x");
		this.panelVariableAdder.addVariable("y");
		this.equationPanel.setEquation("x^2 + (x*y)/2 + 2^x + 2");
		this.panelVariableSetter.setValue(0, 2.0);
		this.panelVariableSetter.setValue(1, 3.0);
		
		
		
	}

	/**
	 * initializes the gui elements
	 */
	private void setGui()
	{
		// layout
		setLayout(new BorderLayout());
		setSize(MyUtils.width, MyUtils.height);
		// variable adder
		panelVariableAdder = new VariableAdderPanel();
		panelVariableAdder.setListener(this);
		add(panelVariableAdder, BorderLayout.WEST);

		// equation field and label
		JPanel equationAndLabelPanel = new JPanel();
		GridBagLayout gridBagLayout = new GridBagLayout();
		equationAndLabelPanel.setLayout(gridBagLayout);
		add(equationAndLabelPanel, BorderLayout.CENTER);

		// label for the function name
		JLabel labelEquationName = new JLabel("f(x)");
		labelEquationName.setFont(MyUtils.fontLarge);
		GridBagConstraints gc1 = new GridBagConstraints();
		gc1.gridy = 0;
		gc1.anchor = GridBagConstraints.CENTER;
		equationAndLabelPanel.add(labelEquationName, gc1);

		// equation panel
		equationPanel = new EquationPanel("y");
		equationPanel.setListener(this);
		GridBagConstraints gc2 = new GridBagConstraints();
		gc2.anchor = GridBagConstraints.CENTER;
		gc2.gridy = 1;
		equationAndLabelPanel.add(equationPanel, gc2);

		// Variable setter panel
		panelVariableSetter = new VariableSetterPanel();
		add(panelVariableSetter, BorderLayout.EAST);

		// evaluate button and result text
		JPanel resultAndOutputPanel = new JPanel();
		resultAndOutputPanel.setLayout(new GridBagLayout());
		add(resultAndOutputPanel, BorderLayout.SOUTH);

		// button evaluate
		buttonEvaluate = new JButton("Evaluate");
		buttonEvaluate.setFont(MyUtils.fontLarge);
		buttonEvaluate.setIcon(MyUtils.loadIcon("/images/iconCalculator.png", getClass()));
		buttonEvaluate.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				onButtonEvaluateClicked();
			}
		});
		resultAndOutputPanel.add(buttonEvaluate);

		// result text
		textFieldResult = new JTextArea(2, 10);
		textFieldResult.setFont(MyUtils.fontLarge);
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridy = 1;
		resultAndOutputPanel.add(textFieldResult, gc);

	}

	public void onButtonEvaluateClicked()
	{
		// get variable names and values and try to evaluate the result
		try
		{
			// get parameters
			String[] variableNames = this.panelVariableAdder.getVariables();
			double[] values = this.panelVariableSetter.getValues();
			String equationString = this.equationPanel.getEquationString();

			// create and evaluate equation
			Equation equation = new Equation(variableNames.length, variableNames, equationString);
			String result = equation.evaluateAt(values) + "";
			this.textFieldResult.setText(result);

		} catch (Exception e)
		{
			JOptionPane.showMessageDialog(this, "Error, invalid equation");
			this.textFieldResult.setText("-");
		}
	}

	public String[] getVariables()
	{
		return this.panelVariableAdder.getVariables();
	}

	public void onVariableAdded(String newVariable)
	{
		this.equationPanel.onTextChanged();
		this.panelVariableSetter.addVariable(newVariable);
	}

}
