import gui.main_panels.FunctionEvaluatorPanel;
import gui.main_panels.FunctionSolverPanel;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import utils.MyUtils;
import de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel;

public class MainFrame extends JFrame
{

	private JPanel contentPane;
	private FunctionEvaluatorPanel functionEvaluatorPanel;
	private JTabbedPane tabbedPane;
	private FunctionSolverPanel functionSolverPanel;

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
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);
		setTitle("Solve my equations");
		setIconImage(MyUtils.loadImage("images/iconCalculator.png", getClass()));
		
		// tabbed panne
		tabbedPane = new JTabbedPane();
		tabbedPane.setFont(MyUtils.fontLarge);
		tabbedPane.setTabPlacement(JTabbedPane.LEFT);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		// function solver 
		functionSolverPanel = new FunctionSolverPanel();
		tabbedPane.add(functionSolverPanel, "Solver");
		
		// function evaluator
		functionEvaluatorPanel = new FunctionEvaluatorPanel();
		tabbedPane.add(functionEvaluatorPanel, "Evaluator");
		

	

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
