package pl.pwr.hiervis.dimensionReduction.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.flexdock.util.SwingUtility;

import pl.pwr.hiervis.core.HVContext;

public class DimensionReductionWrapInstanceVisualizationsFrame extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private HVContext context;
	JComboBox<String> comboBox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		SwingUtility.setPlaf("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

		HVContext HVcontext = new HVContext();
		HVcontext.createGUI("HierVis");

		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					DimensionReductionWrapInstanceVisualizationsFrame frame = new DimensionReductionWrapInstanceVisualizationsFrame(
							HVcontext);
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DimensionReductionWrapInstanceVisualizationsFrame(HVContext context)
	{
		this.context = context;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		contentPane.add(this.context.getInstanceFrame().getContentPane(), BorderLayout.CENTER);

		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		flowLayout.setAlignment(FlowLayout.LEFT);

		contentPane.add(panel, BorderLayout.PAGE_START);

		comboBox = new JComboBox<String>();
		panel.add(comboBox);

		// TODO add based on dim methods and apply selection handler from context
		comboBox.addItem("test");
		comboBox.addItem("test2");
		comboBox.addItem("test22134567890");
	}

	// TODO write function and connect it to context.hierarchyChanged or something
	public void disableSelection()
	{
	}

	// TODO write function and connect it to context.hierarchyChanged or something
	public void enableSelection()
	{
	}
}
