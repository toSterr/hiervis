package pl.pwr.hiervis.dimensionReduction.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.dimensionReduction.CalculatedDimensionReduction;
import pl.pwr.hiervis.dimensionReduction.DimensionReductionRunner;
import pl.pwr.hiervis.dimensionReduction.methods.DimensionReduction;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class DimensionReductionWrapInstanceVisualizationsFrame extends JFrame
{

	private class HierarchyChangingClass implements Consumer<LoadedHierarchy>
	{
		@Override
		public void accept(LoadedHierarchy t)
		{
			disableSelection();
			disableBtn();
		}
	}

	private class HierarchyChangedClass implements Consumer<LoadedHierarchy>
	{
		@Override
		public void accept(LoadedHierarchy t)
		{
			previousSelection = -1;
			comboBox.setSelectedIndex(0);
			enableSelection();
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private HVContext context;
	JComboBox<String> comboBox;
	private DimensionReductionRunner dimensionReductionRunner;
	private HierarchyChangedClass changedClass;
	private HierarchyChangingClass changingClass;
	private int previousSelection;
	private JButton forceBtn;
	private Component horizontalStrut;
	private ConfirmationDialog confirmationDialog;
	/**
	 * Launch the application.
	 */
	/*
	 * public static void main(String[] args) {
	 * SwingUtility.setPlaf("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	 * HVContext HVcontext = new HVContext(); HVcontext.createGUI("HierVis");
	 * EventQueue.invokeLater(new Runnable() { public void run() { try {
	 * DimensionReductionWrapInstanceVisualizationsFrame frame = new
	 * DimensionReductionWrapInstanceVisualizationsFrame( HVcontext);
	 * frame.setVisible(true); } catch (Exception e) { e.printStackTrace(); } } });
	 * }
	 */

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

		confirmationDialog = new ConfirmationDialog();

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

		contentPane.add(panel, BorderLayout.PAGE_START);

		comboBox = new JComboBox<String>();
		panel.add(comboBox);

		horizontalStrut = Box.createHorizontalStrut(10);
		panel.add(horizontalStrut);

		forceBtn = new JButton("Recalculate");
		forceBtn.setFont(new Font("Tahoma", Font.PLAIN, 11));
		forceBtn.setVerticalAlignment(SwingConstants.TOP);
		panel.add(forceBtn);

		comboBox.addItem("Orginal data space");

		for (String s : context.getDimensionReductionMenager().getNames())
			comboBox.addItem(s);

		disableSelection();
		disableBtn();
		previousSelection = 0;
		changedClass = new HierarchyChangedClass();
		changingClass = new HierarchyChangingClass();

		context.hierarchyChanging.addListener(changingClass);
		context.hierarchyChanged.addListener(changedClass);

		context.dimensionReductionCalculated.addListener(this::onDimensionReductionCalculated);

		comboBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (previousSelection != comboBox.getSelectedIndex())
				{
					onDimensionReductionSelected(comboBox.getSelectedIndex());
					previousSelection = comboBox.getSelectedIndex();
				}

			}
		});
		forceBtn.setFocusable(false);
		forceBtn.addActionListener(this::forceReductionSelect);
	}

	private void forceReductionSelect(ActionEvent e)
	{
		int index = comboBox.getSelectedIndex();

		// if (!context.getDimensionReductionMenager().isInQue(context.getHierarchy(),
		// context.getDimensionReductionMenager().getDialogs()[index -
		// 1].getResultClass()))
		{
			previousSelection = -1;

			int x = (int) getContentPane().getLocationOnScreen().getX();
			int y = (int) getContentPane().getLocationOnScreen().getY();
			DimensionReduction dimensionReduction = context.getDimensionReductionMenager().showDialog(index - 1,
					context, x, y);
			if (dimensionReduction == null)
			{

			}
			else
			{
				confirmationDialog.showDialog(x, y);
				context.dimensionReductionCalculating.broadcast(dimensionReduction);
				context.getDimensionReductionMenager().addToQue(context.getHierarchy(), dimensionReduction.getClass());

				dimensionReductionRunner = new DimensionReductionRunner(context, dimensionReduction);
				dimensionReductionRunner.start();
			}
		}

	}

	private void onDimensionReductionSelected(Integer index)
	{

		if (context.getHierarchy().getHierarchyWraper().getHierarchyWithoutChange(index) == null)
		{
			// if (!context.getDimensionReductionMenager().isInQue(context.getHierarchy(),
			// context.getDimensionReductionMenager().getDialogs()[index -
			// 1].getResultClass()))
			{
				int x = (int) getContentPane().getLocationOnScreen().getX();
				int y = (int) getContentPane().getLocationOnScreen().getY();
				DimensionReduction dimensionReduction = context.getDimensionReductionMenager().showDialog(index - 1,
						context, x, y);
				if (dimensionReduction == null)
				{
					if (context.getHierarchy().getHierarchyWraper()
							.getHierarchyWithoutChange(previousSelection) != null)
					{
						comboBox.setSelectedIndex(previousSelection);
						onDimensionReductionSelected(previousSelection);
					}
					else
					{
						comboBox.setSelectedIndex(0);
						onDimensionReductionSelected(0);
						previousSelection = 0;
					}
				}
				else
				{
					confirmationDialog.showDialog(x, y);
					context.dimensionReductionCalculating.broadcast(dimensionReduction);
					context.getDimensionReductionMenager().addToQue(context.getHierarchy(),
							dimensionReduction.getClass());

					dimensionReductionRunner = new DimensionReductionRunner(context, dimensionReduction);
					dimensionReductionRunner.start();
				}
			}
			// else
			{

			}
		}
		else
		{
			afterSelection(index);
		}

	}

	private void afterSelection(int index)
	{
		if (comboBox.getSelectedIndex() > 0)
		{
			enableBtn();
		}
		else
		{
			disableBtn();
		}
		if (previousSelection != comboBox.getSelectedIndex())
		{
			context.getHierarchy().getHierarchyWraper().setHierarchy(index);
			context.getHierarchy().recalculateInstanceTable();

			context.hierarchyChanging.removeListener(changingClass);
			context.hierarchyChanged.removeListener(changedClass);

			context.hierarchyChanging.broadcast(context.getHierarchy());
			context.hierarchyChanged.broadcast(context.getHierarchy());

			context.hierarchyChanging.addListener(changingClass);
			context.hierarchyChanged.addListener(changedClass);
		}
	}

	private void onDimensionReductionCalculated(CalculatedDimensionReduction reduction)
	{
		context.getDimensionReductionMenager().removeFromQue(reduction.inputLoadedHierarchy,
				reduction.dimensionReduction.getClass());

		if (reduction.inputLoadedHierarchy == context.getHierarchy())
		{
			context.getHierarchy().getHierarchyWraper().addReducedHierarchy(reduction);
			int index = context.getDimensionReductionMenager().getIndex(reduction.dimensionReduction) + 1;
			previousSelection = -1;
			comboBox.setSelectedIndex(index);
			afterSelection(index);

		}
		else
			for (LoadedHierarchy l : context.getHierarchyList())
			{
				if (l == reduction.inputLoadedHierarchy)
				{
					l.getHierarchyWraper().addReducedHierarchy(reduction);
				}
			}
	}

	private void disableSelection()
	{
		comboBox.setEnabled(false);
	}

	private void enableSelection()
	{
		comboBox.setEnabled(true);
	}

	private void disableBtn()
	{
		forceBtn.setEnabled(false);
		forceBtn.setVisible(false);
	}

	private void enableBtn()
	{
		forceBtn.setEnabled(true);
		forceBtn.setVisible(true);
	}
}
