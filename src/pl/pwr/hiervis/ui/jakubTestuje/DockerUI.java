package pl.pwr.hiervis.ui.jakubTestuje;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Rectangle;
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
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.util.SwingUtility;
import org.flexdock.view.View;
import org.flexdock.view.Viewport;

import pl.pwr.hiervis.core.HVConfig;
import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;
import pl.pwr.hiervis.ui.VisualizerFrame;
import pl.pwr.hiervis.util.Event;
import pl.pwr.hiervis.util.HierarchyUtils;
import pl.pwr.hiervis.util.SwingUIUtils;


@SuppressWarnings("serial")
public class DockerUI extends JFrame implements DockingConstants  {
	private static final Logger log = LogManager.getLogger( VisualizerFrame.class );
	
	/** Sent when a hierarchy tab is closed. */
	public final Event<Integer> hierarchyTabClosed = new Event<>();
	/** Sent when a hierarchy tab is selected. */
	public final Event<Integer> hierarchyTabSelected = new Event<>();
	
	private HVContext context;
	
	private JMenuItem mntmCloseFile;
	private JMenuItem mntmSaveFile;
	private JMenuItem mntmFlatten;
	
	
	public static void main(String[] args) {
        SwingUtility.setPlaf("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

        HVContext HVcontext = new HVContext();
        HVcontext.createGUI( "tes" );
        
        EventQueue.invokeLater( () -> startup(HVcontext) );
    }
	
    public DockerUI(HVContext hvContext) {
        super("Hiervis");
        context = hvContext;
        
        setContentPane( createContentPane() );
        createMenu();
        
        context.hierarchyChanging.addListener( this::onHierarchyChanging );
		context.hierarchyChanged.addListener( this::onHierarchyChanged );
		
        SwingUIUtils.addCloseCallback( this, this::onWindowClosing );
    }
    
    private JPanel createContentPane() {
        JPanel p = new JPanel(new BorderLayout(0, 0));
        p.setBorder(new EmptyBorder(5, 5, 5, 5));

        Viewport viewport = new Viewport();
        p.add(viewport, BorderLayout.CENTER);

        View viewHier = new View ("id1", "Hierarchy");
        viewHier.addAction(PIN_ACTION);
        viewHier.setContentPane( context.getHierarchyFrame().getContentPane());
        
        View viewVis = new View("id2", "Visualisation");
        viewVis.setContentPane( context.getInstanceFrame().getContentPane());
        viewVis.setTerritoryBlocked(CENTER_REGION, true);
        
        View viewStats = new View("id3", "Statistics");
        viewStats.addAction(PIN_ACTION);
        //viewStats.addAction(CLOSE_ACTION);
        viewStats.setContentPane( context.getStatisticsFrame().getContentPane());
        
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
    
    private static void startup(HVContext hvContext) {
        DockingManager.setFloatingEnabled(false);

        JFrame f = new DockerUI(hvContext);
        Rectangle r = SwingUIUtils.getEffectiveDisplayArea( null );
       
        f.setSize((int) r.getWidth(), (int) r.getHeight());
        f.setVisible(true);
    }
    
    private void createMenu()
	{
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar( menuBar );

		createFileMenu( menuBar );
		createEditMenu( menuBar );
	
	}

	private void createFileMenu( JMenuBar menuBar )
	{
		JMenu mnFile = new JMenu( "File" );
		mnFile.setMnemonic( 'F' );
		menuBar.add( mnFile );

		JMenuItem mntmOpenFile = new JMenuItem( "Open file..." );
		mntmOpenFile.setMnemonic( 'O' );
		mntmOpenFile.addActionListener( e -> context.getHierarchyFrame().openFileSelectionDialog() );
		mnFile.add( mntmOpenFile );

		mntmCloseFile = new JMenuItem( "Close current hierarchy" );
		mntmCloseFile.setMnemonic( 'W' );
		mntmCloseFile.addActionListener( e -> context.getHierarchyFrame().closeCurrentTab() );
		mntmCloseFile.setEnabled( false );
		mnFile.add( mntmCloseFile );

		mntmSaveFile = new JMenuItem( "Save current hierarchy..." );
		mntmSaveFile.setMnemonic( 'S' );
		mntmSaveFile.addActionListener( e -> context.getHierarchyFrame().openSaveDialog() );
		mntmSaveFile.setEnabled( false );
		mnFile.add( mntmSaveFile );

		mnFile.add( new JSeparator() );

		JMenuItem mntmConfig = new JMenuItem( "Config" );
		mntmConfig.setMnemonic( 'C' );
		mntmConfig.addActionListener( e -> context.getHierarchyFrame().openConfigDialog() );
		mnFile.add( mntmConfig );
	}

	private void createEditMenu( JMenuBar menuBar )
	{
		JMenu mnEdit = new JMenu( "Edit" );
		mnEdit.setMnemonic( 'E' );
		menuBar.add( mnEdit );

		mntmFlatten = new JMenuItem( "Flatten Hierarchy" );
		mntmFlatten.setMnemonic( 'F' );
		mntmFlatten.addActionListener(
			e -> {
				String tabTitle = "[F] " + context.getHierarchyFrame().getSelectedTabTitle();
				context.loadHierarchy( tabTitle, HierarchyUtils.flattenHierarchy( context.getHierarchy() ) );
			}
		);
		mntmFlatten.setEnabled( false );
		mnEdit.add( mntmFlatten );
	}
	
	private void onHierarchyChanging( LoadedHierarchy oldHierarchy )
	{
		mntmCloseFile.setEnabled( false );
		mntmSaveFile.setEnabled( false );
		mntmFlatten.setEnabled( false );
	}
	
	private void onHierarchyChanged( LoadedHierarchy newHierarchy )
	{
		if ( context.isHierarchyDataLoaded() )
		{
			mntmCloseFile.setEnabled( true );
			mntmSaveFile.setEnabled( true );
			mntmFlatten.setEnabled( true );
		}
	}

	//TODO zobaczyc czy da sie zapisac pozycje elementow przy zamknieciu i przy otworzeniu wczytac
	private void onWindowClosing()
	{
		log.trace( "Closing application..." );

		// Save the current configuration on application exit.
		context.getConfig().to( new File( HVConfig.FILE_PATH ) );

		context.getStatisticsFrame().dispose();
		context.getInstanceFrame().dispose();
		context.getMeasureManager().dispose();
	}
	

}
