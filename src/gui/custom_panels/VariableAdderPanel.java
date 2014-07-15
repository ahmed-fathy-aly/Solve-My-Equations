package gui.custom_panels;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import utils.MyUtils;

/**
 * Panel that adds, deletes variables
 * 
 * @author ahmed fathy aly
 * 
 * 
 */
public class VariableAdderPanel extends JPanel
{

	/* private variables */
	private JTextField textFieldVariables;
	private JButton buttonAddVaiable;
	private JList<String> listVariables;
	private HashSet<String> variableNamesSet;
	private ArrayList<String> variableNamesList;
	private DefaultListModel listModel;
	private VariableAdderListener listener;

	/**
	 * Create the panel.
	 */
	public VariableAdderPanel()
	{
		variableNamesList = new ArrayList<>();
		variableNamesSet = new HashSet<>();
		setGui();
	}

	/**
	 * initializes the gui elements
	 */
	private void setGui()
	{
		// layout
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		setMinimumSize(new Dimension(300, 500));

		// Label
		JLabel label = new JLabel("Variables");
		label.setFont(MyUtils.fontLarge);
		gbc.anchor = GridBagConstraints.CENTER;
		add(label, gbc);

		// text field, button
		JPanel varAddPanel = new JPanel();
		varAddPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		gbc.gridy++;
		add(varAddPanel, gbc);

		// button
		buttonAddVaiable = new JButton("Add");
		buttonAddVaiable.setFont(MyUtils.fontLarge);
		buttonAddVaiable.setIcon(MyUtils.loadIcon("/images/addIcon.png", getClass()));
		buttonAddVaiable.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				addVariable();
			}
		});

		// text field
		textFieldVariables = new JTextField(5);
		textFieldVariables.setFont(MyUtils.fontLarge);
		Dimension textFieldDim = textFieldVariables.getPreferredSize();
		textFieldDim.height = buttonAddVaiable.getPreferredSize().height;
		textFieldVariables.setPreferredSize(textFieldDim);

		varAddPanel.add("text field", textFieldVariables);
		varAddPanel.add("button", buttonAddVaiable);

		// variable list
		listVariables = new JList<>();
		listModel = new DefaultListModel<>();
		listVariables.setModel(listModel);
		listVariables.setFont(MyUtils.fontLarge);
		Dimension listDim = listVariables.getPreferredSize();
		listDim.width = varAddPanel.getPreferredSize().width;
		listDim.height = varAddPanel.getPreferredSize().height * 2;
		listVariables.setPreferredSize(listDim);
		listVariables.setVisibleRowCount(4);
		listVariables.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		gbc.gridy++;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 5, 0, 0);
		JScrollPane scrollPane = new JScrollPane(listVariables);
		scrollPane.setPreferredSize(listDim);
		add(scrollPane, gbc);
	}

	/**
	 * gets the variable name in the text field and add it to the list
	 */
	public void addVariable()
	{
		String newVariable = textFieldVariables.getText();
		if (!variableNamesSet.contains(newVariable))
		{
			variableNamesList.add(newVariable);
			variableNamesSet.add(newVariable);
			textFieldVariables.setText("");
			listModel.addElement(newVariable);
			textFieldVariables.requestFocus();
			
			if (this.listener != null)
				this.listener.onVariableAdded(newVariable);
		} else
		{
			JOptionPane.showMessageDialog(this, "Duplicate variable");
		}

	}

	public void addVariable(String variableName)
	{
		this.textFieldVariables.setText(variableName);
		addVariable();
	}
	
	/**
	 * @return list of variable names
	 */
	public String[] getVariables()
	{
		String[] result = new String[this.variableNamesList.size()];
		for (int i = 0; i < result.length; i++)
			result[i] = this.variableNamesList.get(i);
		return result;
	}

	public void setListener(VariableAdderListener listener)
	{
		this.listener = listener;
	}

	public interface VariableAdderListener
	{
		void onVariableAdded(String newVariable);
	}
}
