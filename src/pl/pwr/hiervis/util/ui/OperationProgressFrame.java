package pl.pwr.hiervis.util.ui;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import pl.pwr.hiervis.util.SwingUIUtils;


/**
 * A simple dialog that can report progress of any operation that can quantify its progress as
 * an integer from range [0, 100].
 * 
 * @author Tomasz Bachmiński
 *
 */
@SuppressWarnings("serial")
public class OperationProgressFrame extends JDialog
{
	private JLabel status;
	private JProgressBar progressBar;
	private JButton button;

	private Supplier<Integer> progressCallback;
	private Supplier<String> statusCallback;
	private Timer timer;


	public OperationProgressFrame( Window owner, String title )
	{
		super( owner, title );

		setDefaultCloseOperation( DISPOSE_ON_CLOSE );
		setMinimumSize( new Dimension( 200, 100 ) );

		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 0, 0 };
		layout.rowHeights = new int[] { 0, 0, 0 };
		layout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 1.0, 1.0, 0.0 };
		getContentPane().setLayout( layout );

		GridBagConstraintsBuilder builder = new GridBagConstraintsBuilder();

		status = new JLabel();
		status.setHorizontalAlignment( SwingConstants.CENTER );
		getContentPane().add( status, builder.insets( 5, 5, 5, 5 ).position( 0, 0 ).fillHorizontal().anchorSouth().build() );

		progressBar = new JProgressBar();
		progressBar.setIndeterminate( true );
		getContentPane().add( progressBar, builder.insets( 0, 5, 5, 5 ).position( 0, 1 ).fillHorizontal().anchorNorth().build() );

		button = new JButton( "Abort" );
		button.setEnabled( false );
		getContentPane().add( button, builder.anchorCenter().insets( 0, 5, 5, 5 ).position( 0, 2 ).build() );

		SwingUIUtils.addCloseCallback( this, () -> button.doClick() );
	}

	/**
	 * Sets the operation to execute when the user presses the 'Abort' button.
	 * 
	 * @param abortOperation
	 *            the operation to perform when the 'Abort' button is pressed.
	 *            If null, the button is disabled.
	 */
	public void setAbortOperation( ActionListener abortOperation )
	{
		ActionListener[] listeners = button.getActionListeners();
		for ( ActionListener listener : listeners )
			button.removeActionListener( listener );

		if ( abortOperation != null )
			button.addActionListener( abortOperation );
		button.setEnabled( abortOperation != null );
	}

	/**
	 * Sets the callback that supplies an integer representing the operation's progress.
	 * 
	 * @param progressSupplier
	 *            a callback that provides an integer, which represents the operation's progress.
	 *            If null, the progress bar is made indeterminate.
	 */
	public void setProgressUpdateCallback( Supplier<Integer> progressSupplier )
	{
		if ( progressSupplier == null && statusCallback == null ) {
			cleanupTimer();
		}

		progressBar.setIndeterminate( progressSupplier == null );
		progressCallback = progressSupplier;
	}

	public void setStatusUpdateCallback( Supplier<String> statusSupplier )
	{
		if ( statusSupplier == null && progressCallback == null ) {
			cleanupTimer();
		}

		statusCallback = statusSupplier;
	}

	/**
	 * Sets the interval at which to poll the {@code updateCallback} and update the progress bar.
	 * 
	 * @param intervalMs
	 *            the interval, in milliseconds. If value is less than or equal to 0, polling is disabled.
	 */
	public void setProgressPollInterval( int intervalMs )
	{
		cleanupTimer();

		if ( intervalMs > 0 ) {
			timer = new Timer( true );
			TimerTask tt = new TimerTask() {

				@Override
				public void run()
				{
					updateProgressLater();
				}
			};

			timer.scheduleAtFixedRate( tt, 0, intervalMs );
		}
	}

	/**
	 * Schedules an update, so that the progress bar displays the most recent progress value.
	 * If {@code updateCallback} is not set, this method does nothing.
	 */
	public void updateProgressLater()
	{
		// The actual update call is deferred and performed on the main thread,
		// so updateCallback's value *might* have changed.
		if ( progressCallback != null || statusCallback != null ) {
			SwingUtilities.invokeLater(
				() -> {
					if ( progressCallback != null ) {
						int value = progressCallback.get();
						if ( value < 0 ) {
							progressBar.setIndeterminate( true );
						}
						else {
							progressBar.setValue( value );
						}
					}

					if ( statusCallback != null ) {
						status.setText( statusCallback.get() );
					}
				}
			);
		}
	}

	private void cleanupTimer()
	{
		if ( timer != null ) {
			timer.cancel();
			timer = null;
		}
	}

	@Override
	public void dispose()
	{
		super.dispose();

		cleanupTimer();
	}
}
