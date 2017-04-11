package pl.pwr.hiervis.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import internal_measures.statistics.AvgWithStdev;
import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.core.LoadedHierarchy;
import pl.pwr.hiervis.core.MeasureManager;
import pl.pwr.hiervis.measures.MeasureTask;


/*
 * TODO:
 * This class uses a somewhat hacky solution to make the frame always-on-top ONLY
 * within the application. Normally we could use a JDialog for this, however we
 * also want the user to be able to disable this functionality at will.
 * 
 * Another possible solution to this problem would be having shared GUI-creation code,
 * and then calling it inside of a JFrame or a JDialog, depending on the user-selected setting.
 * Changing the setting while the frame was open would close the old frame and create the new one.
 */


@SuppressWarnings("serial")
public class HierarchyStatisticsFrame extends JFrame
{
	private static final Logger log = LogManager.getLogger( HierarchyStatisticsFrame.class );

	private HVContext context;
	private Window owner;

	private JPanel cMeasures;
	private JMenuItem mntmDump;

	private WindowListener ownerListener;

	private HashMap<String, JPanel> measurePanelMap = new HashMap<>();


	public HierarchyStatisticsFrame( HVContext context, Window frame, String subtitle )
	{
		super( "Statistics Frame" + ( subtitle == null ? "" : ( " [ " + subtitle + " ]" ) ) );
		this.context = context;
		this.owner = frame;

		setDefaultCloseOperation( HIDE_ON_CLOSE );
		setMinimumSize( new Dimension( 400, 200 ) );
		setSize( 400, 350 );

		ownerListener = new WindowAdapter() {
			@Override
			public void windowActivated( WindowEvent e )
			{
				HierarchyStatisticsFrame.this.setAlwaysOnTop( true );
			}

			@Override
			public void windowDeactivated( WindowEvent e )
			{
				if ( e.getOppositeWindow() == null ) {
					// Disable 'always on top' ONLY when the opposite window
					// (the one that stole focus from us) is not part of our
					// own application.
					HierarchyStatisticsFrame.this.setAlwaysOnTop( false );
				}
			}
		};

		addWindowListener(
			new WindowAdapter() {
				@Override
				public void windowDeactivated( WindowEvent e )
				{
					if ( e.getOppositeWindow() == null ) {
						// Disable 'always on top' ONLY when the opposite window
						// (the one that stole focus from us) is not part of our
						// own application.
						HierarchyStatisticsFrame.this.setAlwaysOnTop( false );
					}
				}
			}
		);

		createGUI();
		createMenu();

		if ( context.isHierarchyDataLoaded() ) {
			createMeasurePanels();
		}

		MeasureManager measureManager = context.getMeasureManager();

		measureManager.measureComputing.addListener( this::onMeasureComputing );
		measureManager.measureComputed.addListener( this::onMeasureComputed );
		measureManager.taskFailed.addListener( this::onTaskFailed );
		context.hierarchyChanging.addListener( this::onHierarchyChanging );
		context.hierarchyChanged.addListener( this::onHierarchyChanged );

		VisualizerFrame.createFileDrop( this, log, "csv", file -> context.loadFile( this, file ) );

		measureManager.forComputedMeasures(
			set -> {
				set.stream().forEach( this::updateMeasurePanel );
			}
		);
	}

	public void setKeepOnTop( boolean onTop )
	{
		setAlwaysOnTop( onTop );

		if ( onTop ) {
			owner.addWindowListener( ownerListener );
		}
		else {
			owner.removeWindowListener( ownerListener );
		}
	}

	// ----------------------------------------------------------------------------------------

	private void createMenu()
	{
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar( menuBar );

		JMenu mnOptions = new JMenu( "Options" );
		menuBar.add( mnOptions );

		mntmDump = new JMenuItem( "Dump Measures" );
		mntmDump.setEnabled( context.isHierarchyDataLoaded() );
		mnOptions.add( mntmDump );

		mntmDump.addActionListener(
			( e ) -> {
				JFileChooser fileDialog = new JFileChooser();
				fileDialog.setCurrentDirectory( new File( "." ) );
				fileDialog.setDialogTitle( "Choose a file" );
				fileDialog.setFileSelectionMode( JFileChooser.FILES_ONLY );
				fileDialog.setAcceptAllFileFilterUsed( false );
				fileDialog.setFileFilter( new FileNameExtensionFilter( "*.csv", "csv" ) );
				fileDialog.setSelectedFile( new File( "dump.csv" ) );

				if ( fileDialog.showSaveDialog( this ) == JFileChooser.APPROVE_OPTION ) {
					context.getMeasureManager().dumpMeasures(
						Paths.get( fileDialog.getSelectedFile().getAbsolutePath() ),
						context.getHierarchy()
					);
				}
			}
		);

		JCheckBoxMenuItem mntmAlwaysOnTop = new JCheckBoxMenuItem( "Always On Top" );
		mnOptions.add( mntmAlwaysOnTop );

		mntmAlwaysOnTop.addActionListener(
			( e ) -> {
				setKeepOnTop( mntmAlwaysOnTop.isSelected() );
			}
		);
	}

	private void createGUI()
	{
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
		scrollPane.getVerticalScrollBar().setUnitIncrement( 8 );
		scrollPane.setBorder( BorderFactory.createEmptyBorder() );
		getContentPane().add( scrollPane, BorderLayout.CENTER );

		cMeasures = new JPanel();

		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 0 };
		layout.rowHeights = new int[] { 0 };
		layout.columnWeights = new double[] { 1.0 };
		layout.rowWeights = new double[] { Double.MIN_VALUE };
		cMeasures.setLayout( layout );

		scrollPane.setViewportView( cMeasures );
	}

	private void createMeasurePanels()
	{
		MeasureManager measureManager = context.getMeasureManager();

		Collection<MeasureTask> validMeasureTasks = measureManager.getMeasureTasks(
			task -> task.applicabilityFunction.apply( context.getHierarchy().data )
		);

		addMeasurePanels( createBulkTaskPanel( "Calculate All", validMeasureTasks ) );

		for ( String groupPath : measureManager.listMeasureTaskGroups() ) {
			Collection<MeasureTask> measureTasks = measureManager.getMeasureTaskGroup( groupPath ).stream()
				.filter( task -> task.applicabilityFunction.apply( context.getHierarchy().data ) )
				.collect( Collectors.toList() );

			if ( !measureTasks.isEmpty() ) {
				String friendlyGroupName = groupPath.contains( "/" )
					? groupPath.substring( groupPath.lastIndexOf( "/" ) + 1 )
					: groupPath;
				friendlyGroupName = toCamelCase( friendlyGroupName.replaceAll( "_([a-z])", " $1" ), " " );

				addMeasurePanels(
					createFillerPanel( 10 ),
					createSeparatorPanel( friendlyGroupName )
				);

				if ( measureTasks.size() > 1 ) {
					addMeasurePanels( createBulkTaskPanel( "Calculate All " + friendlyGroupName, measureTasks ) );
				}

				for ( MeasureTask task : measureTasks ) {
					addMeasurePanels( createPendingMeasurePanel( task ) );
				}
			}
		}
	}

	private void addMeasurePanels( JPanel... panels )
	{
		int curItems = cMeasures.getComponentCount();
		int newItems = curItems + panels.length;

		GridBagLayout layout = (GridBagLayout)cMeasures.getLayout();
		layout.rowHeights = new int[newItems + 1];
		layout.rowWeights = new double[newItems + 1];
		layout.rowWeights[newItems] = Double.MIN_VALUE;
		cMeasures.setLayout( layout );

		int i = curItems;
		for ( JPanel panel : panels ) {
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx = 0;
			constraints.gridy = i;
			constraints.insets = new Insets( 5, 5, 0, 5 );

			cMeasures.add( panel, constraints );

			++i;
		}

		cMeasures.revalidate();
		cMeasures.repaint();
	}

	private JPanel createFillerPanel( int height )
	{
		JPanel cFiller = new JPanel();

		cFiller.add( Box.createVerticalStrut( height ) );

		return cFiller;
	}

	private JPanel createSeparatorPanel( String title )
	{
		JPanel cSeparator = new JPanel();

		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 0, 0, 0, 0 };
		layout.rowHeights = new int[] { 0, 0 };
		layout.columnWeights = new double[] { 1.0, 0.0, 1.0, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		cSeparator.setLayout( layout );

		JSeparator sepLeft = new JSeparator();
		GridBagConstraints constraintsSepLeft = new GridBagConstraints();
		constraintsSepLeft.insets = new Insets( 0, 5, 5, 5 );
		constraintsSepLeft.fill = GridBagConstraints.HORIZONTAL;
		constraintsSepLeft.gridx = 0;
		constraintsSepLeft.gridy = 0;
		cSeparator.add( sepLeft, constraintsSepLeft );

		JLabel lblTitle = new JLabel( title );
		GridBagConstraints constraintsLabel = new GridBagConstraints();
		constraintsLabel.insets = new Insets( 0, 0, 5, 0 );
		constraintsLabel.fill = GridBagConstraints.VERTICAL;
		constraintsLabel.gridx = 1;
		constraintsLabel.gridy = 0;
		cSeparator.add( lblTitle, constraintsLabel );

		JSeparator sepRight = new JSeparator();
		GridBagConstraints constraintsSepRight = new GridBagConstraints();
		constraintsSepRight.insets = new Insets( 0, 5, 5, 5 );
		constraintsSepRight.fill = GridBagConstraints.HORIZONTAL;
		constraintsSepRight.gridx = 2;
		constraintsSepRight.gridy = 0;
		cSeparator.add( sepRight, constraintsSepRight );

		return cSeparator;
	}

	private JPanel createPendingMeasurePanel( MeasureTask task )
	{
		JPanel cMeasure = new JPanel();
		cMeasure.setBorder( new TitledBorder( null, task.identifier, TitledBorder.LEADING, TitledBorder.TOP, null, null ) );
		cMeasure.setLayout( new BorderLayout( 0, 0 ) );

		cMeasure.add( createTaskButton( task ), BorderLayout.NORTH );
		measurePanelMap.put( task.identifier, cMeasure );

		return cMeasure;
	}

	private JPanel createBulkTaskPanel( String title, Iterable<MeasureTask> tasks )
	{
		JPanel cMeasure = new JPanel();
		cMeasure.setLayout( new BorderLayout( 0, 0 ) );
		cMeasure.add( createTaskButton( title, tasks ), BorderLayout.NORTH );

		return cMeasure;
	}

	private JButton createTaskButton( String title, Iterable<MeasureTask> tasks )
	{
		JButton button = new JButton();
		button.addActionListener(
			( e ) -> {
				button.setEnabled( false );

				MeasureManager measureManager = context.getMeasureManager();
				for ( MeasureTask task : tasks ) {
					if ( !measureManager.isMeasureComputed( task.identifier )
						&& !measureManager.isMeasurePending( task.identifier ) ) {
						measureManager.postTask( task );
					}
				}
			}
		);

		button.setEnabled( context.isHierarchyDataLoaded() );
		button.setText( title );

		return button;
	}

	private JButton createTaskButton( MeasureTask task )
	{
		JButton button = new JButton();
		button.addActionListener(
			( e ) -> {
				MeasureManager measureManager = context.getMeasureManager();

				boolean pending = measureManager.isMeasurePending( task.identifier );
				updateTaskButton( button, !pending );

				if ( pending ) {
					measureManager.removeTask( task );
				}
				else {
					measureManager.postTask( task );
				}
			}
		);
		updateTaskButton( button, false );
		return button;
	}

	private void updateTaskButton( JButton button, boolean pending )
	{
		button.setEnabled( context.isHierarchyDataLoaded() );
		button.setText( pending ? "Abort" : "Calculate" );
	}

	/**
	 * Creates a GUI component used to represent the specified measure computation result.
	 * 
	 * @param result
	 *            the measure computation result to create the component for
	 * @return the GUI component
	 */
	private JComponent createMeasureContent( Object result )
	{
		if ( result == null ) {
			throw new IllegalArgumentException( "Result must not be null!" );
		}

		if ( result instanceof double[] ) {
			// Histogram data // TODO
			double[] data = (double[])result;

			StringBuilder buf = new StringBuilder();
			for ( int i = 0; i < data.length; ++i ) {
				buf.append( Integer.toString( i ) )
					.append( ": " )
					.append( Double.toString( data[i] ) );

				if ( i + 1 < data.length )
					buf.append( "\n" );
			}

			return createFixedTextComponent( buf.toString() );
		}
		else if ( result instanceof AvgWithStdev ) {
			AvgWithStdev avg = (AvgWithStdev)result;
			return createFixedTextComponent( String.format( "%s ± %s", avg.getAvg(), avg.getStdev() ) );
		}
		else if ( result instanceof Double ) {
			return createFixedTextComponent( result.toString() );
		}
		else if ( result instanceof String ) {
			return createFixedTextComponent( result.toString() );
		}
		else {
			throw new IllegalArgumentException(
				String.format(
					"No case defined for data type '%s'",
					result.getClass().getSimpleName()
				)
			);
		}
	}

	private JTextComponent createFixedTextComponent( String msg )
	{
		JTextArea result = new JTextArea( msg );
		result.setEditable( false );
		result.setBorder( UIManager.getBorder( "TextField.border" ) );

		return result;
	}

	private void updateMeasurePanel( Entry<String, Object> result )
	{
		JPanel panel = measurePanelMap.get( result.getKey() );
		panel.removeAll();

		panel.add( createMeasureContent( result.getValue() ), BorderLayout.NORTH );
		panel.revalidate();
		panel.repaint();
	}

	private void recreateMeasurePanel( MeasureTask task )
	{
		JPanel panel = measurePanelMap.get( task.identifier );
		panel.removeAll();

		panel.add( createTaskButton( task ), BorderLayout.NORTH );
		panel.revalidate();
		panel.repaint();
	}

	private static String toCamelCase( String input, String delimiter )
	{
		String[] words = input.split( delimiter );
		for ( int i = 0; i < words.length; ++i ) {
			String word = words[i];
			words[i] = Character.toUpperCase( word.charAt( 0 ) ) + word.substring( 1 ).toLowerCase();
		}
		return String.join( " ", words );
	}

	// ----------------------------------------------------------------------------------------
	// Listeners

	private void onMeasureComputing( String measureName )
	{
		SwingUtilities.invokeLater(
			() -> {
				if ( measurePanelMap.containsKey( measureName ) ) {
					JPanel panel = measurePanelMap.get( measureName );
					JButton button = (JButton)panel.getComponent( 0 );
					button.setEnabled( false );
					button.setText( "Calculating..." );
				}
				else {
					throw new IllegalArgumentException(
						String.format(
							"Implementation error: %s does not have UI component for measure '%s'.",
							this.getClass().getSimpleName(), measureName
						)
					);
				}
			}
		);
	}

	private void onMeasureComputed( Pair<String, Object> result )
	{
		SwingUtilities.invokeLater( () -> updateMeasurePanel( result ) );
	}

	private void onTaskFailed( MeasureTask task )
	{
		SwingUtilities.invokeLater( () -> recreateMeasurePanel( task ) );
	}

	private void onHierarchyChanging( LoadedHierarchy oldHierarchy )
	{
		measurePanelMap.clear();

		cMeasures.removeAll();
		cMeasures.revalidate();
		cMeasures.repaint();
	}

	private void onHierarchyChanged( LoadedHierarchy newHierarchy )
	{
		mntmDump.setEnabled( newHierarchy != null );
		createMeasurePanels();
	}
}
