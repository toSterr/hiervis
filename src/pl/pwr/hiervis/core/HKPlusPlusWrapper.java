package pl.pwr.hiervis.core;

import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import basic_hierarchy.interfaces.Hierarchy;
import basic_hierarchy.interfaces.Node;
import basic_hierarchy.reader.GeneratedCSVReader;
import pl.pwr.hiervis.ui.OperationProgressFrame;
import pl.pwr.hiervis.util.Event;
import pl.pwr.hiervis.util.HierarchyUtils;


public class HKPlusPlusWrapper
{
	private static final File hkBaseDir = new File( "./hk" );
	private static final File hkOutDir = new File( hkBaseDir, "out" );
	private static final File hkJarFile = new File( hkBaseDir, "hk.jar" );
	private static final File hkInputFile = new File( hkBaseDir, "in.csv" );
	private static final File hkOutputFile = new File( hkOutDir, "finalHierarchyOfGroups.csv.csv" );

	/**
	 * Sent when the subprocess terminates by itself.
	 * 
	 * @param first
	 *            the exit code of the subprocess
	 */
	public final Event<Integer> subprocessFinished = new Event<>();
	/** Sent when the subprocess is terminated by the main process. */
	public final Event<Void> subprocessAborted = new Event<>();

	private volatile Process process;
	private InputStreamObserverThread outObserver;
	private OperationProgressFrame waitFrame;


	public HKPlusPlusWrapper()
	{
	}

	/**
	 * Creates and starts the subprocess with the specified arguments, and creates a wait dialog
	 * at the specified owner window.
	 * 
	 * @param owner
	 *            frame at which the wait dialog will be created.
	 * @param trueClassAttribute
	 *            indicates that FIRST column of data is true class attribute, class should be indicated by string.
	 * @param instanceNames
	 *            indicates that SECOND (if class attribute is present) or FIRST (otherwise) column is the name of every instance.
	 * @param diagonalMatrix
	 *            use simple diagonal matrix instead of full matrix as a covariance matrix
	 * @param disableStaticCenter
	 *            disable feature of placing static (background) center while going down in hierarchy
	 * @param generateImages
	 *            store clusterisation results also as images (ONLY first two dimensions are visualized!).
	 *            The dimension of each image is set to 800x800.
	 * @param epsilon
	 *            epsilon value expressed as 10^-epsilon, used in comparing values to 0.0, reducing round-off error.
	 *            Default value is 10.
	 * @param littleValue
	 *            value of diagonal matrix elements expressed as 10^-littleValue, used in forcing covariance matrix to be non-singular.
	 *            Default value is 5.
	 * @param clusters
	 *            number of clusters generated by clusterisation algorithm
	 * @param iterations
	 *            number of maximum iterations made by clusterisation algorithm
	 * @param repeats
	 *            number of algorithm repeats (new initialization of clusters)
	 * @param dendrogramSize
	 *            max dendrogram height
	 * @param maxNodeCount
	 *            maximum number of created nodes
	 * @throws IOException
	 *             if an I/O error occurs while starting the subprocess
	 */
	public void start(
		Window owner,
		boolean trueClassAttribute, boolean instanceNames,
		boolean diagonalMatrix, boolean disableStaticCenter,
		boolean generateImages,
		int epsilon, int littleValue,
		int clusters, int iterations, int repeats,
		int dendrogramSize, int maxNodeCount ) throws IOException
	{
		hkOutDir.mkdirs();

		// Clear the output dir so's not to litter
		Arrays.stream( hkOutDir.listFiles() ).forEach( file -> file.delete() );

		// Set HK's working dir to the output directory, so that we keep all output files in one place
		process = new ProcessBuilder(
			buildArgsList(
				trueClassAttribute, instanceNames, diagonalMatrix, disableStaticCenter, generateImages,
				epsilon, littleValue, clusters, iterations, repeats, dendrogramSize, maxNodeCount
			)
		).redirectErrorStream( true ).directory( hkOutDir ).start();

		// Create a separate thread to wait for HK to terminate
		Thread subprocessObserver = new Thread(
			() -> {
				try {
					int exitCode = process.waitFor();

					if ( process == null ) {
						subprocessAborted.broadcast( null );
					}
					else {
						destroy();
						subprocessFinished.broadcast( exitCode );
					}
				}
				catch ( InterruptedException e ) {
					// Ignore.
				}
			}
		);

		subprocessObserver.setDaemon( true );
		subprocessObserver.start();

		outObserver = new InputStreamObserverThread( process.getInputStream() );
		outObserver.start();

		waitFrame = new OperationProgressFrame( owner, "HK++ subprocess" );
		waitFrame.setAbortOperation( e -> destroy() );
		waitFrame.setStatusUpdateCallback( this::getLatestMessage );
		waitFrame.setProgressPollInterval( 100 );

		waitFrame.setModal( true );
		waitFrame.setSize( 300, 150 );
		waitFrame.setLocationRelativeTo( owner );
		waitFrame.setVisible( true );
	}

	/**
	 * Prepares the input file that the HK subprocess will load by serializing the specified hierarchy
	 * and saving it in the file that HK is configured to load.
	 * 
	 * @param hierarchy
	 *            the source hierarchy
	 * @param selectedNode
	 *            the node in the specified hierarchy denoting the subtree that is to be
	 *            serialized to the file
	 * @param withTrueClass
	 *            whether the input file should include true class column
	 * @param withInstanceNames
	 *            whether the input file should include instance name column
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public void prepareInputFile(
		Hierarchy hierarchy, Node selectedNode,
		boolean withTrueClass, boolean withInstanceNames ) throws IOException
	{
		Hierarchy subHierarchy = HierarchyUtils.subHierarchyShallow( hierarchy, selectedNode.getId() );
		HierarchyUtils.save( hkInputFile.getAbsolutePath(), subHierarchy, false, withTrueClass, withInstanceNames, true );
	}

	/**
	 * Loads and returns the output hierarchy that was created by the HK subprocess.
	 * 
	 * @param withTrueClass
	 *            whether the output file should be read assuming that it contains true class column
	 * @param withInstanceNames
	 *            whether the output file should be read assuming that it contains instance names column
	 * @param useSubtree
	 *            whether nodes should be created with 'useSubtree' option
	 * @return the HK output hierarchy
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public Hierarchy getOutputHierarchy( boolean withTrueClass, boolean withInstanceNames, boolean useSubtree ) throws IOException
	{
		return new GeneratedCSVReader( false ).load(
			hkOutputFile.getAbsolutePath(),
			withInstanceNames, withTrueClass, true, false, useSubtree
		);
	}

	/**
	 * Attempts to terminate the HK++ subprocess, and clean up.
	 */
	public void destroy()
	{
		waitFrame.dispose();
		outObserver.interrupt();
		process.destroy();

		outObserver = null;
		waitFrame = null;
		process = null;
	}

	// ---------------------------------------------------------------------------------------

	private String getLatestMessage()
	{
		return outObserver == null ? "" : outObserver.getLatestMessage();
	}

	/**
	 * Build the args list based on options selected by the user.
	 * 
	 * @param trueClassAttribute
	 *            indicates that FIRST column of data is class attribute, class should be indicated by string.
	 * @param instanceNames
	 *            indicates that SECOND (if class attribute is present) or FIRST (otherwise) column is the name of every instance.
	 * @param diagonalMatrix
	 *            use simple diagonal matrix instead of full matrix as a covariance matrix
	 * @param disableStaticCenter
	 *            disable feature of placing static (background) center while going down in hierarchy
	 * @param generateImages
	 *            store clusterisation results also as images (ONLY first two dimensions are visualized!).
	 *            The dimension of each image is set to 800x800.
	 * @param epsilon
	 *            epsilon value expressed as 10^-epsilon, used in comparing values to 0.0, reducing round-off error.
	 *            Default value is 10.
	 * @param littleValue
	 *            value of diagonal matrix elements expressed as 10^-littleValue, used in forcing covariance matrix to be non-singular.
	 *            Default value is 5.
	 * @param clusters
	 *            number of clusters generated by clusterisation algorithm
	 * @param iterations
	 *            number of maximum iterations made by clusterisation algorithm
	 * @param repeats
	 *            number of algorithm repeats (new initialization of clusters)
	 * @param dendrogramSize
	 *            max dendrogram height
	 * @param maxNodeCount
	 *            maximum number of created nodes
	 * @return list of arguments passed to ProcessBuilder to create the subprocess
	 */
	private List<String> buildArgsList(
		boolean trueClassAttribute, boolean instanceNames,
		boolean diagonalMatrix, boolean disableStaticCenter,
		boolean generateImages,
		int epsilon, int littleValue,
		int clusters, int iterations, int repeats,
		int dendrogramSize, int maxNodeCount )
	{
		List<String> args = new ArrayList<>();

		args.add( "java" );
		// Set encoding to UTF-8 so that files are loaded correctly
		args.add( "-Dfile.encoding=utf8" );
		args.add( "-jar" );
		args.add( hkJarFile.getAbsolutePath() );

		// Hardcode several parameters.
		args.add( "-lgmm" );
		// args.add( "-v" ); // verbose mode
		args.add( "-cf" );
		args.add( "1.0" );
		args.add( "-rf" );
		args.add( "1.0" );

		if ( trueClassAttribute )
			args.add( "-c" );
		if ( instanceNames )
			args.add( "-in" );
		if ( diagonalMatrix )
			args.add( "-dm" );
		if ( disableStaticCenter )
			args.add( "-ds" );

		if ( generateImages ) {
			args.add( "-gi" );
			args.add( "800" );
		}

		args.add( "-e" );
		args.add( Integer.toString( epsilon ) );

		args.add( "-l" );
		args.add( Integer.toString( littleValue ) );

		args.add( "-k" );
		args.add( Integer.toString( clusters ) );

		args.add( "-n" );
		args.add( Integer.toString( iterations ) );

		args.add( "-r" );
		args.add( Integer.toString( repeats ) );

		args.add( "-s" );
		args.add( Integer.toString( dendrogramSize ) );

		args.add( "-w" );
		args.add( Integer.toString( maxNodeCount ) );

		args.add( "-i" );
		args.add( hkInputFile.getAbsolutePath() );

		args.add( "-o" );
		args.add( hkOutDir.getAbsolutePath() );

		return args;
	}
}
