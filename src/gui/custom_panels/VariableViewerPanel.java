package gui.custom_panels;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import utils.MyUtils;

public class VariableViewerPanel extends JPanel
{
	private JTable variablesTable;
	private VariablesTableModel variableTableModel;

	/**
	 * Create the panel.
	 */
	public VariableViewerPanel()
	{
		setGui();
	}

	/**
	 * sets the table
	 */
	private void setGui()
	{
		// set the layout
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);

		// label
		JLabel tabelLabel = new JLabel("Values of variables");
		tabelLabel.setFont(MyUtils.fontLarge);
		GridBagConstraints gc1 = new GridBagConstraints();
		gc1.anchor = GridBagConstraints.CENTER;
		add(tabelLabel, gc1);

		// table
		variableTableModel = new VariablesTableModel();
		variablesTable = new JTable(variableTableModel);
		variablesTable.setFont(MyUtils.fontLarge);
		variablesTable.setRowHeight(50);
		GridBagConstraints gc2 = new GridBagConstraints();
		gc2.gridy = 1;
		gc2.anchor = GridBagConstraints.CENTER;
		JScrollPane scrollPane = new JScrollPane(variablesTable);
		scrollPane.setPreferredSize(new Dimension(250,
				scrollPane.getPreferredSize().height * 9 / 10));
		add(scrollPane, gc2);

	}

	/**
	 * @param row index of the row that'll be modified
	 * @param d the new value
	 */
	public void setValue(int row, String d)
	{
		this.variableTableModel.setValueAt(d, row, 1);

	}

	/**
	 * @param values array of the new values inserted in the value field of the table
	 */
	public void setValues(double[] values)
	{
		for (int i = 0; i < values.length; i++)
			setValue(i, values[i] + "");
		this.variableTableModel.fireTableDataChanged();
		
	}
	/**
	 * adds a variable name with the value of 0
	 */
	public void addVariable(String newVariable)
	{
		this.variableTableModel.addVariable(newVariable);
	}

	class VariablesTableModel extends AbstractTableModel
	{
		private ArrayList<String> variableNames;
		private ArrayList<String> values;

		public VariablesTableModel()
		{
			this.variableNames = new ArrayList<>();

			this.values = new ArrayList<>();
		}

		public int getColumnCount()
		{
			return 2;
		}

		@Override
		public int getRowCount()
		{
			return variableNames.size();
		}

		@Override
		public Class<?> getColumnClass(int col)
		{
			switch (col)
			{
			case 0:
				return String.class;
			case 1:
				return String.class;
			default:
				return null;
			}
		}

		@Override
		public String getColumnName(int col)
		{
			switch (col)
			{
			case 0:
				return "Variable";
			case 1:
				return "Value";
			default:
				return "Why is this here ? :(";
			}
		}

		@Override
		public boolean isCellEditable(int row, int col)
		{
			return false;
		}

		@Override
		public void setValueAt(Object value, int row, int col)
		{
			switch (col)
			{

			case 1:
				this.values.set(row, (String) value);
				break;
			default:
				break;
			}
		}

		@Override
		public Object getValueAt(int row, int col)
		{
			switch (col)
			{
			case 0:
				return variableNames.get(row);
			case 1:
				return values.get(row);
			default:
				return null;
			}
		}

		public void addVariable(String newVariable)
		{
			this.variableNames.add(newVariable);
			this.values.add("-");
			fireTableDataChanged();
		}

	}

	

}
