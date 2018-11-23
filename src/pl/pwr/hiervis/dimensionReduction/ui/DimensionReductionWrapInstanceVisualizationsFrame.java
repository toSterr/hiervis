package pl.pwr.hiervis.dimensionReduction.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.dimensionReduction.CalculatedDimensionReduction;
import pl.pwr.hiervis.dimensionReduction.DimensionReductionRunner;
import pl.pwr.hiervis.dimensionReduction.methods.DimensionReduction;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class DimensionReductionWrapInstanceVisualizationsFrame extends JFrame {

    private class HierarchyChangingClass implements Consumer<LoadedHierarchy> {
	@Override
	public void accept(LoadedHierarchy t) {
	    disableSelection();
	}
    }

    private class HierarchyChangedClass implements Consumer<LoadedHierarchy> {
	@Override
	public void accept(LoadedHierarchy t) {
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
    public DimensionReductionWrapInstanceVisualizationsFrame(HVContext context) {
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
	comboBox.addItem("Orginal data space");

	for (String s : context.getDimensionReductionMenager().getNames())
	    comboBox.addItem(s);

	disableSelection();
	changedClass = new HierarchyChangedClass();
	changingClass = new HierarchyChangingClass();
	context.hierarchyClosed.addListener(new Consumer<LoadedHierarchy>() {
	    @Override
	    public void accept(LoadedHierarchy t) {
		// TODO Auto-generated method stub

	    }
	});

	context.hierarchyChanging.addListener(changingClass);
	context.hierarchyChanged.addListener(changedClass);

	context.dimensionReductionCalculated.addListener(this::onDimensionReductionCalculated);

	comboBox.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		onDimensionReductionSelected(comboBox.getSelectedIndex());
	    }
	});
    }

    private void onDimensionReductionSelected(Integer index) {
	if (context.getHierarchy().getHierarchyWraper().getHierarchyWithoutChange(index) == null) {
	    int x = (int) getContentPane().getLocationOnScreen().getX();
	    int y = (int) getContentPane().getLocationOnScreen().getY();
	    DimensionReduction dimensionReduction = context.getDimensionReductionMenager().showDialog(index - 1,
		    context, x, y);
	    if (dimensionReduction == null) {
		comboBox.setSelectedIndex(0);
		onDimensionReductionSelected(0);
	    }
	    else {
		ConfirmationDialog confirmationDialog = new ConfirmationDialog();
		context.dimensionReductionCalculating.broadcast(dimensionReduction);
		dimensionReductionRunner = new DimensionReductionRunner(context, dimensionReduction);
		dimensionReductionRunner.start();
		confirmationDialog.showDialog();
	    }
	}
	else {
	    afterSelection(index);
	}
    }

    private void afterSelection(int index) {
	System.out.println("jest");
	context.getHierarchy().getHierarchyWraper().setHierarchy(index);

	context.hierarchyChanging.removeListener(changingClass);
	context.hierarchyChanged.removeListener(changedClass);

	context.hierarchyChanging.broadcast(context.getHierarchy());
	context.hierarchyChanged.broadcast(context.getHierarchy());

	context.hierarchyChanging.addListener(changingClass);
	context.hierarchyChanged.addListener(changedClass);
    }

    private void onDimensionReductionCalculated(CalculatedDimensionReduction reduction) {
	if (reduction.inputLoadedHierarchy == context.getHierarchy()) {
	    context.getHierarchy().getHierarchyWraper().addReducedHierarchy(reduction);
	    int index = context.getDimensionReductionMenager().getIndex(reduction.dimensionReduction) + 1;
	    comboBox.setSelectedIndex(index);
	    afterSelection(index);
	}
	else
	    for (LoadedHierarchy l : context.getHierarchyList()) {
		if (l == reduction.inputLoadedHierarchy) {
		    l.getHierarchyWraper().addReducedHierarchy(reduction);
		}
	    }
    }

    public void disableSelection() {
	comboBox.setEnabled(false);
    }

    public void enableSelection() {
	comboBox.setEnabled(true);
    }
}
