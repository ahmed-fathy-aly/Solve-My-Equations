package gui.custom_panels;

import gui.custom_panels.EquationPanel.EquationPanelListener;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import utils.MyUtils;

public class EquationsTablePanel extends JPanel implements EquationPanelListener
{

	private JButton buttonAddEqation;
	private JButton removeEquationButton;
	private JTable equationsTable;
	private EquationsTableModel equationsTableModel;
	private EquationsTableListener listener;

	/**
	 * Create the panel.
	 */
	public EquationsTablePanel()
	{
		setGui();
	}

	private void setGui()
	{
		// Layout
		setLayout(new GridBagLayout());

		// label
		JLabel label = new JLabel("Equations");
		label.setFont(MyUtils.fontLarge);
		GridBagConstraints gc1 = new GridBagConstraints();
		gc1.gridwidth = 2;
		gc1.gridx = 0;
		gc1.gridy = 0;
		gc1.anchor = GridBagConstraints.CENTER;
		add(label, gc1);

		// remove equation button
		removeEquationButton = new JButton("");
		removeEquationButton.setFont(MyUtils.fontSmall);
		removeEquationButton.setIcon(MyUtils.loadIcon("/images/minusIcon.png", getClass()));
		removeEquationButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				onEquationRemoved();
			}
		});
		GridBagConstraints gc2 = new GridBagConstraints();
		gc2.gridx = 1;
		gc2.gridy = 1;
		gc2.anchor = GridBagConstraints.WEST;
		add(removeEquationButton, gc2);

		// add equation button
		buttonAddEqation = new JButton("");
		buttonAddEqation.setFont(MyUtils.fontSmall);
		buttonAddEqation.setIcon(MyUtils.loadIcon("/images/plusIcon.png", getClass()));
		buttonAddEqation.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				onEquationAdded();
			}
		});
		GridBagConstraints gc3 = new GridBagConstraints();
		gc3.gridx = 1;
		gc3.gridy = 1;
		gc3.anchor = GridBagConstraints.EAST;
		add(buttonAddEqation, gc3);

		// equations table
		equationsTableModel = new EquationsTableModel();
		equationsTable = new JTable(equationsTableModel);
		equationsTable.setFont(MyUtils.fontLarge);
		equationsTable.setRowHeight(100);
		equationsTable.getColumnModel().getColumn(0).setMaxWidth(80);
		equationsTable.getColumnModel().getColumn(2).setMaxWidth(60);

		equationsTable.setTableHeader(null);
		equationsTable.setDefaultRenderer(EquationPanel.class, new EquationsTableRenderer());
		equationsTable.setDefaultEditor(EquationPanel.class, new EquationsTableEditor());

		// scroll pane the table is in
		JScrollPane scrollPane = new JScrollPane(equationsTable);
		scrollPane.setPreferredSize(new Dimension(600,
				scrollPane.getPreferredSize().height * 7 / 10));
		GridBagConstraints gc4 = new GridBagConstraints();
		gc4.gridx = 0;
		gc4.gridy = 2;
		gc4.gridwidth = 2;
		add(scrollPane, gc4);

	}

	public void setListener(EquationsTableListener listener)
	{
		this.listener = listener;
	}

	protected void onEquationRemoved()
	{
		this.equationsTableModel.removeEquation();

	}

	protected void onEquationAdded()
	{
		this.equationsTableModel.addEquation();

	}

	public String[] getVariables()
	{
		if (listener != null)
			return listener.getVariables();
		else
			return new String[0];
	}

	public void notifyVariablesChanged()
	{
		this.equationsTableModel.fireTableDataChanged();
	}

	class EquationsTableModel extends AbstractTableModel
	{

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex)
		{
			switch (columnIndex)
			{
			case 0:
				return false;
			case 1:
				return true;
			default:
				return false;
			}
		}


		public Class<?> getColumnClass(int col)
		{
			switch (col)
			{
			case 0:
				return String.class;
			case 1:
				return EquationPanel.class;
			case 2:
				return String.class;
			default:
				return null;
			}
		}

		private ArrayList<String> equationStrs;

		public EquationsTableModel()
		{
			this.equationStrs = new ArrayList<>();
		}

		public int getColumnCount()
		{
			return 3;
		}

		public int getRowCount()
		{
			return equationStrs.size();
		}

		@Override
		public Object getValueAt(int row, int col)
		{
			switch (col)
			{
			case 0:
				return "f" + (row + 1) + "() =";
			case 1:
				return this.equationStrs.get(row);
			case 2:
				return "= 0";
			default:
				return null;
			}
		}

		public void addEquation()
		{
			this.equationStrs.add("");
			fireTableDataChanged();
		}

		public void removeEquation()
		{
			this.equationStrs.remove(this.equationStrs.size()-1);
			fireTableDataChanged();
			
		}

		public void setValueAt(Object value, int rowIndex, int columnIndex)
		{
			switch (columnIndex)
			{
			case 1:
				this.equationStrs.set(rowIndex, (String) value);
				break;

			default:
				break;
			}
		}

	}

	class EquationsTableRenderer implements TableCellRenderer
	{

		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int col)
		{
			switch (col)
			{
			case 0:
				// make a label
				JLabel label = new JLabel((String) value);
				label.setFont(MyUtils.fontLarge);
				return label;

			case 1:
				// make an equation
				EquationPanel equationPanel = new EquationPanel("");
				equationPanel.setEquation((String) value);
				equationPanel.setListener(EquationsTablePanel.this);
				equationPanel.onTextChanged();
				return equationPanel;
			case 2:
				// make a label with "=0"
				JLabel label2 = new JLabel("=0");
				label2.setFont(MyUtils.fontLarge);
				return label2;

			default:
				return null;
			}
		}

	}

	class EquationsTableEditor extends AbstractCellEditor implements TableCellEditor
	{

		private EquationPanel equationPanel;

		public Object getCellEditorValue()
		{
			return equationPanel.getEquationString();
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value,
				boolean isSelected, int row, int column)
		{
			// make an equation
			equationPanel = new EquationPanel("");
			equationPanel.setEquation((String) value);
			equationPanel.setListener(EquationsTablePanel.this);
			equationPanel.onTextChanged();
			return equationPanel;
		}

	}

	public interface EquationsTableListener
	{
		String[] getVariables();
	}

}
