package gui.main_panels;

import gui.custom_panels.EquationsTablePanel;
import gui.custom_panels.EquationsTablePanel.EquationsTableListener;
import gui.custom_panels.VariableAdderPanel;
import gui.custom_panels.VariableAdderPanel.VariableAdderListener;
import gui.custom_panels.VariableViewerPanel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.MyUtils;
import utils.MyUtils.Solvers;

public class FunctionSolverPanel extends JPanel implements VariableAdderListener, EquationsTableListener
{

	private VariableAdderPanel variableAdderPanel;
	private VariableViewerPanel variableViewerPanel;
	private EquationsTablePanel equationTablePanel;
	private JComboBox<Solvers> solverChooser;

	/**
	 * Create the panel.
	 */
	public FunctionSolverPanel()
	{
		setGui();
	}

	/**
	 * sets the GUI elements
	 */
	private void setGui()
	{
		// set layout
		setLayout(new BorderLayout());
		
		// variable adder
		variableAdderPanel = new VariableAdderPanel();
		variableAdderPanel.setListener(this);
		add(variableAdderPanel, BorderLayout.WEST);
		
		// variable viewer and add button
		JPanel p = new JPanel(new GridBagLayout());
		add(p, BorderLayout.EAST);
		
		// variable viewer
		variableViewerPanel = new VariableViewerPanel();
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = 0;
		gc.gridy = 0;
		p.add(variableViewerPanel, gc);
		
		// solve button
		JButton buttonSolve = new JButton("Solve");
		buttonSolve.setFont(MyUtils.fontLarge);
		buttonSolve.setIcon(MyUtils.loadIcon("/images/iconCalculator.png", getClass()));
		buttonSolve.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				onButtonSolveClicked();
			}
		});
		GridBagConstraints gc2 = new GridBagConstraints();
		gc2.gridx = 0;
		gc2.gridy = 1;
		//p.add(buttonSolve, gc2);
		add(buttonSolve, BorderLayout.SOUTH);
		
		// equations table
		equationTablePanel = new EquationsTablePanel();
		equationTablePanel.setListener(this);
		add(equationTablePanel, BorderLayout.CENTER);
		
		// solver chooser
		JPanel chooserPanel = new JPanel(new FlowLayout());
		JLabel labelChooseSolver = new JLabel("Solver : ");
		labelChooseSolver.setFont(MyUtils.fontLarge);
		chooserPanel.add(labelChooseSolver);
		solverChooser = new JComboBox<>(MyUtils.Solvers.values());
		solverChooser.setFont(MyUtils.fontLarge);
		solverChooser.setSelectedItem(MyUtils.Solvers.NEWTON_SYSTEM_EQUATIONS);
		chooserPanel.add(solverChooser);
		add(chooserPanel, BorderLayout.NORTH);
	}

	protected void onButtonSolveClicked()
	{
		// TODO Auto-generated method stub
		
	}

	public void onVariableAdded(String newVariable)
	{
		// inform other elements
		variableViewerPanel.addVariable(newVariable);
		equationTablePanel.notifyVariablesChanged();
	}

	public String[] getVariables()
	{
		return this.variableAdderPanel.getVariables();
	}
}
