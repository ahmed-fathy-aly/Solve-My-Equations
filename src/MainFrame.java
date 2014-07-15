import gui.main_panels.FunctionEvaluatorPanel;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import utils.MyUtils;
import de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel;

public class MainFrame extends JFrame
{

	private JPanel contentPane;
	private FunctionEvaluatorPanel functionEvaluatorPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 100 + MyUtils.width, 100 + MyUtils.height);
		setGui();
		setLookAndFeel();

	}

	/**
	 * initializes the gui elements
	 */
	private void setGui()
	{

		// The content pane
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		// function evaluator
		functionEvaluatorPanel = new FunctionEvaluatorPanel();
		contentPane.add(functionEvaluatorPanel, BorderLayout.CENTER);
		
	

	}

	/**
	 * Change the look and feel to make it look pretty
	 */
	private void setLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(new SyntheticaBlackEyeLookAndFeel());
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e)
		{
			System.out.println("failed to set look and feel");
		}
	}

}
