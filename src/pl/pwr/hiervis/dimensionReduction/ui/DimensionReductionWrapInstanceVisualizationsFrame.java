package pl.pwr.hiervis.dimensionReduction.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
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
import pl.pwr.hiervis.dimensionReduction.ui.elements.LoadingIcon;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class DimensionReductionWrapInstanceVisualizationsFrame extends JFrame {

    private class HierarchyChangingClass implements Consumer<LoadedHierarchy> {
	@Override
	public void accept(LoadedHierarchy t) {
	    disableSelection();
	    disableBtn();
	}
    }

    private class HierarchyChangedClass implements Consumer<LoadedHierarchy> {
	@Override
	public void accept(LoadedHierarchy t) {
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
    private LoadingIcon loadingIcon;

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

	comboBox.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		if (previousSelection != comboBox.getSelectedIndex()) {
		    onDimensionReductionSelected(comboBox.getSelectedIndex());
		    previousSelection = comboBox.getSelectedIndex();
		}

	    }
	});
	forceBtn.setFocusable(false);

	loadingIcon = new LoadingIcon();
	loadingIcon.hideIcon();
	contentPane.add(loadingIcon);

	contentPane.add(this.context.getInstanceFrame().getContentPane(), BorderLayout.CENTER);
	forceBtn.addActionListener(this::forceReductionSelect);

	getContentPane().addComponentListener(new ComponentListener() {
	    @Override
	    public void componentShown(ComponentEvent e) {
	    }

	    @Override
	    public void componentResized(ComponentEvent e) {
		if (loadingIcon.isVisible()) {
		    displayLoadingIcon();
		}
	    }

	    @Override
	    public void componentMoved(ComponentEvent e) {
	    }

	    @Override
	    public void componentHidden(ComponentEvent e) {
	    }
	});
    }

    private void forceReductionSelect(ActionEvent e) {
	int index = comboBox.getSelectedIndex();
	int x = (int) getContentPane().getLocationOnScreen().getX();
	int y = (int) getContentPane().getLocationOnScreen().getY();

	DimensionReduction dimensionReduction = context.getDimensionReductionMenager().showDialog(index - 1,
		context.getHierarchy().getHierarchyWraper().getOriginalHierarchy(), x, y);

	if (dimensionReduction != null) {
	    context.getHierarchy().getHierarchyWraper().getReducedHierarchy()[index - 1] = null;
	    disableBtn();
	    ifReductonCalculating(index);
	    afterConfirmedReduction(context.getHierarchy(), dimensionReduction, x, y);
	}

    }

    private void onDimensionReductionSelected(int index) {
	if (index == -1) {
	    previousSelection = -1;
	    comboBox.setSelectedIndex(0);
	}
	else if (context.getHierarchy().getHierarchyWraper().getHierarchyWithoutChange(index) != null) {
	    afterSelection(index, true);
	    loadingIcon.hideIcon();
	}
	else if (context.getDimensionReductionMenager().isInQueue(context.getHierarchy(), index - 1)) {
	    ifReductonCalculating(index);
	    context.dimensionReductionCalculating.broadcast(null);
	}
	else {
	    int x = (int) getContentPane().getLocationOnScreen().getX();
	    int y = (int) getContentPane().getLocationOnScreen().getY();

	    DimensionReduction dimensionReduction = context.getDimensionReductionMenager().showDialog(index - 1,
		    context.getHierarchy().getHierarchyWraper().getOriginalHierarchy(), x, y);

	    if (dimensionReduction == null) {
		comboBox.setSelectedIndex(previousSelection);
		onDimensionReductionSelected(previousSelection);
	    }
	    else {
		ifReductonCalculating(index);
		afterConfirmedReduction(context.getHierarchy(), dimensionReduction, x, y);
	    }
	}
    }

    private void ifReductonCalculating(int index) {
	context.getInstanceFrame().clearUI();
	displayLoadingIcon();
    }

    private void afterConfirmedReduction(LoadedHierarchy loadedHierarchy, DimensionReduction dimensionReduction, int x,
	    int y) {
	context.dimensionReductionCalculating.broadcast(dimensionReduction);

	context.getDimensionReductionMenager().addToQueue(loadedHierarchy, dimensionReduction.getClass());

	dimensionReductionRunner = new DimensionReductionRunner(context, dimensionReduction);
	dimensionReductionRunner.start();
	confirmationDialog.showDialog(x, y);
    }

    /**
     * ustawia hierarchie na nowa zalezna od indexu oraz odswieza widoki omijajac
     * liste z wyborem aby nie ustawila sie zpowrotem na poczatkowych wartosciach
     * dodatkowo sprawdza czy nie zostala wybrana ponownie ta sama hierarchia co
     * poprzednio aby nadmiarowo nie odswierzac
     */
    private void afterSelection(int index, boolean recalculateInstanceTable) {
	if (comboBox.getSelectedIndex() > 0) {
	    enableBtn();
	}
	else {
	    disableBtn();
	}
	if (previousSelection != comboBox.getSelectedIndex()) {
	    context.getHierarchy().getHierarchyWraper().setHierarchy(index);

	    if (recalculateInstanceTable) {
		context.getHierarchy().recalculateInstanceTable();
	    }

	    context.hierarchyChanging.removeListener(changingClass);
	    context.hierarchyChanged.removeListener(changedClass);

	    context.hierarchyChanging.broadcast(context.getHierarchy());
	    context.hierarchyChanged.broadcast(context.getHierarchy());

	    context.hierarchyChanging.addListener(changingClass);
	    context.hierarchyChanged.addListener(changedClass);
	}
    }

    private void onDimensionReductionCalculated(CalculatedDimensionReduction reduction) {
	context.getDimensionReductionMenager().removeFromQueue(reduction.inputLoadedHierarchy,
		reduction.dimensionReduction.getClass());

	if (comboBox.getSelectedIndex() == context.getDimensionReductionMenager()
		.getIndex(reduction.dimensionReduction)) {
	    loadingIcon.hideIcon();
	}
	if (reduction.outputHierarchy != null) {
	    loadingIcon.hideIcon();
	    int index = context.getDimensionReductionMenager().getIndex(reduction.dimensionReduction) + 1;

	    if (reduction.inputLoadedHierarchy == context.getHierarchy() && comboBox.getSelectedIndex() == index) {
		context.getHierarchy().getHierarchyWraper().addReducedHierarchy(reduction);
		previousSelection = -1;
		comboBox.setSelectedIndex(index);
		afterSelection(index, true);
	    }
	    else
		for (LoadedHierarchy l : context.getHierarchyList()) {
		    if (l == reduction.inputLoadedHierarchy) {
			l.getHierarchyWraper().addReducedHierarchy(reduction);
		    }
		}
	}
	else {
	    previousSelection = -1;
	    onDimensionReductionSelected(comboBox.getSelectedIndex());
	}
    }

    private void disableSelection() {
	comboBox.setEnabled(false);
    }

    private void enableSelection() {
	comboBox.setEnabled(true);
    }

    private void disableBtn() {
	forceBtn.setEnabled(false);
	forceBtn.setVisible(false);
    }

    private void enableBtn() {
	forceBtn.setEnabled(true);
	forceBtn.setVisible(true);
    }

    // TODO create a handler for hiding after it is not more needed and difrent
    // handler for display depending on if specific reduction is calculating
    private void displayLoadingIcon() {
	Component component = context.getInstanceFrame().getContentPane().getComponent(2);
	double x = component.getX();
	double y = component.getY();
	double width = component.getWidth();
	double height = component.getHeight();
	double x1 = getContentPane().getComponent(2).getX();
	double y1 = getContentPane().getComponent(2).getY();
	double posX = 0;
	double posY = 0;
	if (width < 210) {
	    posX = x + x1 + 10;
	}
	else {
	    posX = x + x1 + width / 2 - 100 + 10;
	}
	if (height < 210) {
	    posY = y + y1 + 10;
	}
	else {
	    posY = y + y1 + height / 2 - 100 + 10;
	}

	loadingIcon.showIcon((int) (posX), (int) (posY));

    }
}
