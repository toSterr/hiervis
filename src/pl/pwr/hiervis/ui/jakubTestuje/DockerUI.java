package pl.pwr.hiervis.ui.jakubTestuje;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexdock.dockbar.DockbarManager;
import org.flexdock.dockbar.event.DockbarEvent;
import org.flexdock.dockbar.event.DockbarListener;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.util.SwingUtility;
import org.flexdock.view.View;
import org.flexdock.view.Viewport;

import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.core.HVConfig;
import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.dimensionReduction.CalculatedDimensionReduction;
import pl.pwr.hiervis.dimensionReduction.DimensionReductionRunner;
import pl.pwr.hiervis.dimensionReduction.methods.DimensionReduction;
import pl.pwr.hiervis.dimensionReduction.ui.ConfirmationDialog;
import pl.pwr.hiervis.dimensionReduction.ui.DimensionReductionDialog;
import pl.pwr.hiervis.dimensionReduction.ui.DimensionReductionWrapInstanceVisualizationsFrame;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;
import pl.pwr.hiervis.ui.VisualizerFrame;
import pl.pwr.hiervis.util.Event;
import pl.pwr.hiervis.util.HierarchyUtils;
import pl.pwr.hiervis.util.SwingUIUtils;

@SuppressWarnings("serial")
public class DockerUI extends JFrame implements DockingConstants
{
	private static final Logger log = LogManager.getLogger(VisualizerFrame.class);

	/** Sent when a hierarchy tab is closed. */
	public final Event<Integer> hierarchyTabClosed = new Event<>();
	/** Sent when a hierarchy tab is selected. */
	public final Event<Integer> hierarchyTabSelected = new Event<>();

	private HVContext context;

	private JMenuItem mntmCloseFile;
	private JMenuItem mntmSaveFile;
	private JMenuItem mntmFlatten;
	private JMenuItem mntmCut;
	private JMenuItem mntmReduce[];
	private View viewHier;
	private View viewVis;
	private View viewStats;
	private DimensionReductionRunner dimensionReductionRunner;

	public static void main(String[] args)
	{
		SwingUtility.setPlaf("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

		HVContext HVcontext = new HVContext();
		HVcontext.createGUI("HierVis");

		EventQueue.invokeLater(() -> startup(HVcontext));
	}

	public DockerUI(HVContext hvContext)
	{
		super("HierVis");
		context = hvContext;

		setContentPane(createContentPane());
		createMenu();

		context.hierarchyChanging.addListener(this::onHierarchyChanging);
		context.hierarchyChanged.addListener(this::onHierarchyChanged);
		context.getHierarchyFrame().hierarchyTabClosed.addListener(this::onHierarchyTabClosed);

		context.dimensionReductionSelected.addListener(this::onDimensionReductionSelected);
	
		context.dimensionReductionCalculated.addListener(this::onDimensionReductionCalculated);
		
		SwingUIUtils.addCloseCallback(this, this::onWindowClosing);
	}

	private JPanel createContentPane()
	{
		JPanel p = new JPanel(new BorderLayout(0, 0));
		p.setBorder(new EmptyBorder(5, 5, 5, 5));

		Viewport viewport = new Viewport();
		p.add(viewport, BorderLayout.CENTER);

		viewHier = new View("Hier", "Hierarchy");
		viewHier.setName("Hier");
		viewHier.addAction(PIN_ACTION);
		viewHier.setContentPane(context.getHierarchyFrame().getContentPane());
		viewHier.setTerritoryBlocked(CENTER_REGION, true);
		viewHier.getDockingProperties().setDockingEnabled(false);

		viewVis = new View("Visu", "Visualisation");
		viewVis.setName("Visu");
		DimensionReductionWrapInstanceVisualizationsFrame wraper = new DimensionReductionWrapInstanceVisualizationsFrame(
				context);
		viewVis.setContentPane(wraper.getContentPane());
		viewVis.setTerritoryBlocked(CENTER_REGION, true);
		viewVis.getDockingProperties().setDockingEnabled(false);

		viewStats = new View("Stat", "Statistics");
		viewStats.setName("Stat");
		viewStats.addAction(PIN_ACTION);
		viewStats.setTerritoryBlocked(CENTER_REGION, true);
		viewStats.setContentPane(context.getStatisticsFrame().getContentPane());
		viewStats.getDockingProperties().setDockingEnabled(false);

		DockbarManager.addListener(new DockbarListener()
		{
			@Override
			public void minimizeStarted(DockbarEvent arg0)
			{
			}

			@Override
			public void minimizeCompleted(DockbarEvent arg0)
			{
			}

			@Override
			public void dockableLocked(DockbarEvent arg0)
			{
			}

			@Override
			public void dockableExpanded(DockbarEvent arg0)
			{
				if (arg0.getSource() == viewStats)
				{
					viewStats.repaint();
					viewStats.revalidate();
				}
			}

			@Override
			public void dockableCollapsed(DockbarEvent arg0)
			{
				((View) arg0.getSource()).getDockingProperties().setDockingEnabled(false);
				if (arg0.getSource() == viewHier && !viewHier.isMinimized() && !viewStats.isMinimized())
				{
					viewHier.dock(viewStats, SOUTH_REGION, .5f);
				}
			}
		});

		viewport.dock(viewVis);
		viewVis.dock(viewHier, WEST_REGION, .3f);
		viewHier.dock(viewStats, SOUTH_REGION, .5f);

		if (!Boolean.getBoolean("disable.system.exit"))
		{
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		else
		{
			this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		}

		return p;
	}

	private static void startup(HVContext hvContext)
	{
		DockingManager.setFloatingEnabled(false);

		JFrame f = new DockerUI(hvContext);
		//Rectangle r = SwingUIUtils.getEffectiveDisplayArea(null);
		//f.setSize((int) r.getWidth(), (int) r.getHeight());
		
		f.setSize(400, 400);
		f.setVisible(true);
	}

	private void createMenu()
	{
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		createFileMenu(menuBar);
		createEditMenu(menuBar);
		createDimRedMenu(menuBar);

	}

	private void createFileMenu(JMenuBar menuBar)
	{
		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic('F');

		menuBar.add(mnFile);

		JMenuItem mntmOpenFile = new JMenuItem("Open file...");
		mntmOpenFile.setMnemonic('O');
		mntmOpenFile.addActionListener(e -> context.getHierarchyFrame().openFileSelectionDialog());
		mnFile.add(mntmOpenFile);

		mntmCloseFile = new JMenuItem("Close current hierarchy");
		mntmCloseFile.setMnemonic('W');
		mntmCloseFile.addActionListener(e -> context.getHierarchyFrame().closeCurrentTab());
		mntmCloseFile.setEnabled(false);
		mnFile.add(mntmCloseFile);

		mntmSaveFile = new JMenuItem("Save current hierarchy...");
		mntmSaveFile.setMnemonic('S');
		mntmSaveFile.addActionListener(e -> context.getHierarchyFrame().openSaveDialog());
		mntmSaveFile.setEnabled(false);
		mnFile.add(mntmSaveFile);

		mnFile.add(new JSeparator());

		JMenuItem mntmConfig = new JMenuItem("Config");
		mntmConfig.setMnemonic('C');
		mntmConfig.addActionListener(e -> context.getHierarchyFrame().openConfigDialog());
		mnFile.add(mntmConfig);
	}

	//TODO delete this and all references to this menu bar
	private void createDimRedMenu(JMenuBar menuBar)
	{
		JMenu mnRedu = new JMenu("Dimension Reduction");
		mnRedu.setMnemonic(KeyEvent.VK_R);
		menuBar.add(mnRedu);


		DimensionReductionDialog dimReductionDialogs[] = context.getDimensionReductionMenager().getDialogs();
		mntmReduce = new JMenuItem[dimReductionDialogs.length];
		
		ConfirmationDialog confirmationDialog = new ConfirmationDialog();

		for (int i = 0; i < 0; i++)
		{
			final int j = i;
			mntmReduce[i] = new JMenuItem(dimReductionDialogs[j].getName());

			mntmReduce[i].addActionListener(e ->
			{
				DimensionReduction dimensionReduction = dimReductionDialogs[j].showDialog(context);
				if (dimensionReduction != null)
				{
					try
					{
						dimensionReductionRunner = new DimensionReductionRunner(context, dimensionReduction);
						dimensionReductionRunner.start();
						confirmationDialog.showDialog();
						context.dimensionReductionCalculating.broadcast(dimensionReduction);
					}
					catch (Exception exception)
					{
						log.trace("Exception when computing dimension reduction :" + exception);
					}
				}

			});
			mntmReduce[i].setEnabled(false);
			mnRedu.add(mntmReduce[i]);
		}
	}

	private void createEditMenu(JMenuBar menuBar)
	{
		JMenu mnEdit = new JMenu("Edit");
		mnEdit.setMnemonic('E');
		menuBar.add(mnEdit);

		mntmFlatten = new JMenuItem("Flatten Hierarchy");
		mntmFlatten.setMnemonic('F');
		mntmFlatten.addActionListener(e ->
		{
			String tabTitle = "[F] " + context.getHierarchyFrame().getSelectedTabTitle();
			context.loadHierarchy(tabTitle, HierarchyUtils.flattenHierarchy(context.getHierarchy()));
		});
		mntmFlatten.setEnabled(false);
		mnEdit.add(mntmFlatten);

		mntmCut = new JMenuItem("Create a subtree based on the curent node");
		mntmCut.setMnemonic('C');
		mntmCut.addActionListener(e ->
		{
			String nodeName = context.getHierarchy().getTree().getNode(context.getHierarchy().getSelectedRow()).get(0)
					.toString();
			String tabTitle = "[" + nodeName + "] " + context.getHierarchyFrame().getSelectedTabTitle();
			Hierarchy cutHierarchy = HierarchyUtils.subHierarchy(context.getHierarchy().getMainHierarchy(), nodeName,
					(String) null);
			context.loadHierarchy(tabTitle, new LoadedHierarchy(cutHierarchy, context.getHierarchy().options));
		});
		mntmCut.setEnabled(false);
		mnEdit.add(mntmCut);
	}

	private void onHierarchyChanging(LoadedHierarchy oldHierarchy)
	{
		mntmCloseFile.setEnabled(false);
		mntmSaveFile.setEnabled(false);
		mntmFlatten.setEnabled(false);
		mntmCut.setEnabled(false);

		for (JMenuItem item : mntmReduce)
		{
			if (item != null)
				item.setEnabled(false);
		}

	}

	private void onHierarchyChanged(LoadedHierarchy newHierarchy)
	{
		viewVis.repaint();
		viewVis.revalidate();

		if (context.isHierarchyDataLoaded())
		{
			mntmCloseFile.setEnabled(true);
			mntmSaveFile.setEnabled(true);
			mntmFlatten.setEnabled(true);
			mntmCut.setEnabled(true);
			for (JMenuItem item : mntmReduce)
			{
				if (item != null)
					item.setEnabled(true);
			}
		}
	}

	private void onHierarchyTabClosed(int index)
	{
		if (index == 0)
		{
			viewVis.repaint();
			viewVis.revalidate();
		}
	}

	private void onDimensionReductionSelected(DimensionReduction dimensionReduction)
	{
		if (dimensionReduction!=null)
		{
			ConfirmationDialog confirmationDialog = new ConfirmationDialog();
			dimensionReductionRunner = new DimensionReductionRunner(context, dimensionReduction);
			dimensionReductionRunner.start();
			confirmationDialog.showDialog();
			context.dimensionReductionCalculating.broadcast(dimensionReduction);
		}	
	}
	
	private void onDimensionReductionCalculated(CalculatedDimensionReduction t)
	{
			String tabTitle = "[" + t.dimensionReduction.getClass().getSimpleName() + "] "
					+ context.getHierarchyFrame().getSelectedTabTitle();
			context.loadHierarchy(tabTitle, t.outputLoadedHierarchy);
	}
	private void onWindowClosing()
	{
		log.trace("Closing application...");

		// Save the current configuration on application exit.
		context.getConfig().to(new File(HVConfig.FILE_PATH));

		context.getStatisticsFrame().dispose();
		context.getInstanceFrame().dispose();
		context.getMeasureManager().dispose();
	}
}
