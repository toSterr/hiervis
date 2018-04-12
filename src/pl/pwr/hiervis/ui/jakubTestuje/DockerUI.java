package pl.pwr.hiervis.ui.jakubTestuje;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.LinkedList;
import java.util.Vector;

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
import org.flexdock.dockbar.MinimizationAdapter;
import org.flexdock.dockbar.event.DockbarEvent;
import org.flexdock.dockbar.event.DockbarListener;
import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.defaults.DefaultDockingStrategy;
import org.flexdock.docking.event.DockingEvent;
import org.flexdock.docking.event.DockingListener;
import org.flexdock.docking.state.MinimizationManager;
import org.flexdock.util.SwingUtility;
import org.flexdock.view.View;
import org.flexdock.view.Viewport;


import pl.pwr.hiervis.core.HVConfig;
import pl.pwr.hiervis.core.HVConstants;
import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;
import pl.pwr.hiervis.ui.VisualizerFrame;
import pl.pwr.hiervis.util.Event;
import pl.pwr.hiervis.util.HierarchyUtils;
import pl.pwr.hiervis.util.SwingUIUtils;
import prefuse.Display;


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
	
	private View viewHier;
	private View viewVis;
	private View viewStats;
	
	public View getView(int i)
	{
		if (i==0)
			return viewHier;
		else if (i==1)
			return viewVis;
		else
			return viewStats;
	}
	
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
		context.getHierarchyFrame().hierarchyTabClosed.addListener ( this::onHierarchyTabClosed );
		
        SwingUIUtils.addCloseCallback( this, this::onWindowClosing );
    }
    
    private JPanel createContentPane() {
        JPanel p = new JPanel(new BorderLayout(0, 0));
        p.setBorder(new EmptyBorder(5, 5, 5, 5));

        Viewport viewport = new Viewport();
        p.add(viewport, BorderLayout.CENTER);

        System.out.println( DockingManager.getMinimizeManager() );
        
        
        viewHier = new View ("Hier", "Hierarchy");
        viewHier.setName("Hier");
        viewHier.addAction(PIN_ACTION);
        viewHier.setContentPane( context.getHierarchyFrame().getContentPane());
        viewHier.setTerritoryBlocked(CENTER_REGION, true);
        viewHier.getDockingProperties().setDockingEnabled(false);
        
        viewVis = new View("Visu", "Visualisation");
        viewVis.setName("Visu");
        viewVis.setContentPane( context.getInstanceFrame().getContentPane());
        viewVis.setTerritoryBlocked(CENTER_REGION, true);
        viewVis.getDockingProperties().setDockingEnabled(false);
        
        
        viewStats = new View("Stat", "Statistics");
        viewStats.setName("Stat");
        viewStats.addAction(PIN_ACTION);
        viewStats.setTerritoryBlocked(CENTER_REGION, true);
        //viewStats.addAction(CLOSE_ACTION);
        viewStats.setContentPane( context.getStatisticsFrame().getContentPane());
        viewStats.getDockingProperties().setDockingEnabled(false);
        
       // MinimizationManager minimizationManager= new MinimizationAdapter();
        
        
        DockbarManager.addListener(new DockbarListener() {
			
			@Override
			public void minimizeStarted(DockbarEvent arg0) {
				System.out.println("DockbarEvent minimizeStarted");
				System.out.println( ((View )arg0.getSource()).getName() );	
				//((View )arg0.getSource()).getDockingProperties().setDockingEnabled(false);
			
			}
			
			@Override
			public void minimizeCompleted(DockbarEvent arg0) {
				System.out.println("DockbarEvent minimizeCompleted");
				System.out.println( ((View )arg0.getSource()).getName() );
				//((View )arg0.getSource()).getDockingProperties().setDockingEnabled(false);
				//System.out.println( arg0.getSource() == viewHier);
				//System.out.println( viewStats.isMinimized() );
				
				if (arg0.getSource() == viewHier && viewStats.isMinimized())
				{
					//DockingManager.setMinimized(viewStats, false);
					//DockingManager.setMinimized(viewStats, true);
				}
			}
			
			@Override
			public void dockableLocked(DockbarEvent arg0) {
				System.out.println("DockbarEvent dockableLocked");
				System.out.println( ((View )arg0.getSource()).getName() );
				//((View )arg0.getSource()).getDockingProperties().setDockingEnabled(false);
			}
			
			@Override
			public void dockableExpanded(DockbarEvent arg0) {
				System.out.println("DockbarEvent dockableExpanded");
				System.out.println( ((View )arg0.getSource()).getName() );
				//((View )arg0.getSource()).getDockingProperties().setDockingEnabled(false);
				if ( arg0.getSource() == viewStats )
				{
					viewStats.repaint();
					viewStats.revalidate();	
				}
			}
			
			@Override
			public void dockableCollapsed(DockbarEvent arg0) {
				System.out.println("DockbarEvent dockableCollapsed");
				System.out.println( ((View )arg0.getSource()).getName() );
				((View )arg0.getSource()).getDockingProperties().setDockingEnabled(false);
				
				if ( arg0.getSource()==viewHier && !viewHier.isMinimized() && !viewStats.isMinimized())
				{
						viewHier.dock(viewStats, SOUTH_REGION, .5f);		
				}
			}
		});
        
        viewport.dock(viewVis);
        
        viewVis.dock(viewHier, WEST_REGION, .3f);
        viewHier.dock(viewStats, SOUTH_REGION, .5f);
        
        viewStats.addDockingListener(new DockingListener() {

			@Override
			public void dockingCanceled(DockingEvent arg0) {
				// TODO Auto-generated method stub	
			}
			@Override
			public void dockingComplete(DockingEvent arg0) 
			{			
				if(viewStats.getSibling(NORTH_REGION)==viewHier )
				{
					System.out.println("stats have sibling hier");
					//viewStats.setSize( viewStats.getWidth(), viewVis.getHeight()/2 );
					//viewHier.setSize( viewHier.getWidth(), viewVis.getHeight()/2 );
					
					System.out.println( viewStats.getParent().getParent().getComponent(0) );
					
					System.out.println( viewStats.getParent().getParent().getComponent(1) );
					System.out.println( viewStats.getParent().getParent().getComponent(2) );
					//.setSize(viewStats.getWidth(), viewVis.getHeight()/2);
					
					System.out.println(viewStats.getParent().getParent());
					//DockingManager.setSplitProportion( ((Dockable)viewHier), .5f);
					
					EventQueue.invokeLater( () ->
					{
						//DockingManager.setSplitProportion( ((Dockable)viewHier), .5f); 
					});
				}
			}

			@Override
			public void dragStarted(DockingEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void dropStarted(DockingEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void undockingComplete(DockingEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void undockingStarted(DockingEvent arg0) 
			{
				
				
			}});

		viewHier.addDockingListener(new DockingListener() {
			@Override
			public void dockingCanceled(DockingEvent arg0) {
				System.out.println("DockingListener dockingCanceled");
			}

			@Override
			public void dockingComplete(DockingEvent arg0) {
				System.out.println("DockingListener dockingComplete " );
				//((View)arg0.getSource()).getDockingProperties().setDockingEnabled(false);		
				if (!viewStats.isMinimized() )
				{
					//viewVis.dock(viewHier,WEST_REGION,.43f);
					//viewHier.dock(viewStats, SOUTH_REGION, .8f);				
					//viewStats.dock(viewHier, NORTH_REGION, 0.5f);				
				}
			}

			@Override
			public void undockingStarted(DockingEvent arg0) 
			{
				System.out.println("DockingListener undockingStarted");
			}
			
			@Override
			public void dragStarted(DockingEvent arg0) {
				System.out.println("DockingListener dragStarted");
			}

			@Override
			public void dropStarted(DockingEvent arg0) {
				System.out.println("DockingListener dropStarted");	
			}

			@Override
			public void undockingComplete(DockingEvent arg0) 
			{
				System.out.println( viewStats.isMinimized() );
				if (!viewStats.isMinimized() )
				{
					//DockingManager.setMinimized(viewStats, true);
					//DockingManager.undock( (Dockable)viewStats );
				}
				System.out.println("DockingListener undockingComplete");
	
			}		
		});
        
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
       /* 
        System.out.println();
       // View wiev= (View) ((DockingSplitPane) ((Viewport)f.getContentPane().getComponents()[0]).getComponents()[0]).getComponents()[0];
      // System.out.println( ((WindowsSplitPaneDivider)((DockingSplitPane) ((Viewport)f.getContentPane().getComponents()[0]).getComponents()[0]).getComponents()[0]).getComponentCount() );
     
       DockingSplitPane cos=((DockingSplitPane)((Viewport)((DockingSplitPane) ((Viewport)f.getContentPane().getComponents()[0]).getComponents()[0]).getComponents()[1]).getComponents()[0]);
      
       System.out.println (((Viewport)cos.getComponents()[1]).getComponents()[0].getName());
       System.out.println (((Viewport)cos.getComponents()[1]).getComponents()[0].getWidth());
       System.out.println (((Viewport)cos.getComponents()[1]).getComponents()[0].getHeight());
       
       System.out.println (((Viewport)cos.getComponents()[2]).getComponents()[0].getName());
       System.out.println (((Viewport)cos.getComponents()[2]).getComponents()[0].getWidth());
       System.out.println (((Viewport)cos.getComponents()[2]).getComponents()[0].getHeight());
       
       System.out.println();
       System.out.println( ((View)((Viewport)((DockingSplitPane) ((Viewport)f.getContentPane().getComponents()[0]).getComponents()[0]).getComponents()[2]).getComponents()[0]).getName() );
       System.out.println( ((View)((Viewport)((DockingSplitPane) ((Viewport)f.getContentPane().getComponents()[0]).getComponents()[0]).getComponents()[2]).getComponents()[0]).getWidth() );
       System.out.println( ((View)((Viewport)((DockingSplitPane) ((Viewport)f.getContentPane().getComponents()[0]).getComponents()[0]).getComponents()[2]).getComponents()[0]).getHeight() );
       //System.out.println( wiev.getAlignmentY() );*/
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
		//System.out.println("Hier changing");

		viewVis.repaint();
    	viewVis.revalidate();	
		
		if ( context.isHierarchyDataLoaded() )
		{
			mntmCloseFile.setEnabled( true );
			mntmSaveFile.setEnabled( true );
			mntmFlatten.setEnabled( true );
		}
	}

	private void onHierarchyTabClosed(int index)
	{
		if (index==0)
	    {
			//context.getInstanceFrame().recreateUI();
	    	viewVis.repaint();
	    	viewVis.revalidate();	
	    }
	}
	
	private void getPositions()
	{
		
		String [] directions= {NORTH_REGION, EAST_REGION, SOUTH_REGION, WEST_REGION };
		View [] views= {viewHier,viewVis,viewStats};
		int [] siblings= {0,0,0};
		
		String layout="";
		File file= new File( "LayoutConfig.json");
		
		Vector <View> vect= new Vector<View>();
		
		for (int j=0; j<views.length; j++ )
		{
			View i= views[j];
	        System.out.println();
			System.out.println( i.getName() );
			System.out.println("Width - "+ i.getWidth() );
			System.out.println("Height - " + i.getHeight() );
			//System.out.println( i.getAlignmentX());
	        //System.out.println( i.getAlignmentY());
			for (String s: directions)
			{
				  //System.out.println( i.getSibling( s ) );
				//  if( i.getSibling( s )!=null  )
				  { 
					  siblings[j]++;
					//  System.out.println(s+" -  "+  ((View)i.getSibling( s )).getName());
					  System.out.println(s+" -  "+   i.getSibling( s ) );
					//  layout+= i.getName() +" ; "+ s +" ; "+ ((View)i.getSibling( s )).getName()+" ;\n" ;
				  }
			}
			//System.out.println("find reg - " +  DefaultDockingStrategy.findRegion(i));	
		}
		
		
		int min=3;
		int index=-1;
		for (int j=0; j<3; j++ )
		{
			if (siblings[j]<min)
			{
				min=siblings[j];
				index=j;
			}
		}
		vect.add( views[index]);
		siblings[index]=3;
		
		min=3;
		index=-1;
		for (int j=0; j<3; j++ )
		{
			if (siblings[j]<min)
			{
				min=siblings[j];
				index=j;
			}
		}
		vect.add( views[index]);
		siblings[index]=3;
		min=3;
		index=-1;
		for (int j=0; j<3; j++ )
		{
			if (siblings[j]<min)
			{
				min=siblings[j];
				index=j;
			}
		}
		vect.add( views[index]);
		siblings[index]=3;
		
		for (int j=0; j<3; j++ )
		{
			View i=  vect.get(j);
	
			for (String s: directions)
			{
				  
				  if( i.getSibling( s )!=null  )
				  { 
					  //layout+= i.getName() +" ; "+ s +" ; "+ ((View)i.getSibling( s )).getName()+" ;\n" ;
				  }
			}
			
		}
		System.out.println();
		System.out.println(layout);
		
		LinkedList <View> list =new LinkedList<View>() ;
		LinkedList <View> list2 =new LinkedList<View>() ;
		list.push(viewVis);
		
		String laout2="";
		String laout3="";
		
		/*
		for (int i=0;i<3;i++)
		{
			View elem=list.get(i);
			for (String s: directions)
			{
				  if( elem.getSibling( s )!=null  )
				  { 
					  laout2+= elem.getName() +" ; "+ s +" ; "+ ((View)elem.getSibling( s )).getName()+" ;\n" ;
					
					  if (!list2.contains( (View)elem.getSibling( s ) ))
					  {
						  list.add ( (View)elem.getSibling( s ) ); 
						  
						  laout3+= elem.getName() +" ; "+ s +" ; "+ ((View)elem.getSibling( s )).getName()+" ;\n" ;
					  }
				  }
			}
			list2.push(elem);
		}*/
		System.out.println();
		System.out.println(laout2);
		
		
		System.out.println();
		System.out.println(laout3);
	}
	
	//TODO zobaczyc czy da sie zapisac pozycje elementow przy zamknieciu i przy otworzeniu wczytac
	//teoretycznie sie da, praktycznie nie da, 
	private void onWindowClosing()
	{
		log.trace( "Closing application..." );

		//getPositions();
		
		// Save the current configuration on application exit.
		context.getConfig().to( new File( HVConfig.FILE_PATH ) );

		context.getStatisticsFrame().dispose();
		context.getInstanceFrame().dispose();
		context.getMeasureManager().dispose();
		
		
		
	}
	

}
