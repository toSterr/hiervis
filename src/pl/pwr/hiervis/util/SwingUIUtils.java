package pl.pwr.hiervis.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Locale;
import java.util.function.Consumer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.RootPaneContainer;
import javax.swing.SwingWorker;

import org.apache.logging.log4j.Logger;

import pl.pwr.hiervis.util.ui.OperationProgressFrame;


/**
 * This class contains methods that are used to show customizable dialogs
 * and UI prompts to the user.
 * 
 * @author Tomasz Bachmiński
 * 
 */
@SuppressWarnings("serial")
public final class SwingUIUtils
{
	public static final String AMK_DISPATCH_WINDOW_CLOSING = SwingUIUtils.class.getCanonicalName() + ":WINDOW_CLOSING";
	public static final String AMK_HIDE_WINDOW = SwingUIUtils.class.getCanonicalName() + ":WINDOW_HIDE";
	public static final String AMK_IGNORE_OPERATION = SwingUIUtils.class.getCanonicalName() + ":IGNORE_OPERATION";

	public static final KeyStroke KS_ESCAPE = KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 );

	private static final Action ignoreAction = new AbstractAction( AMK_IGNORE_OPERATION ) {
		public void actionPerformed( ActionEvent e )
		{
			// Nothing.
		}
	};


	private SwingUIUtils()
	{
		// Static class -- disallow instantiation.
		throw new RuntimeException( "Attempted to instantiate a static class: " + getClass().getName() );
	}

	/**
	 * <p>
	 * Installs a close action in the specified dialog.
	 * This action sends the window closing event, so all listeners should be correctly triggered.
	 * </p>
	 * - Bound to the Escape key.<br/>
	 * - Active when the dialog is focused.
	 * 
	 * @see #AMK_DISPATCH_WINDOW_CLOSING
	 */
	public static void installEscapeCloseOperation( final JDialog dialog )
	{
		installOperation(
			dialog, JComponent.WHEN_IN_FOCUSED_WINDOW, KS_ESCAPE, AMK_DISPATCH_WINDOW_CLOSING,
			new AbstractAction( AMK_DISPATCH_WINDOW_CLOSING ) {
				public void actionPerformed( ActionEvent e )
				{
					dialog.dispatchEvent( new WindowEvent( dialog, WindowEvent.WINDOW_CLOSING ) );
				}
			}
		);
	}

	/**
	 * <p>
	 * Installs a close action in the specified frame.
	 * This action sends the window closing event, so all listeners should be correctly triggered.
	 * </p>
	 * - Bound to the Escape key.<br/>
	 * - Active when the frame is focused.
	 * 
	 * @see #AMK_DISPATCH_WINDOW_CLOSING
	 */
	public static void installEscapeCloseOperation( final JFrame frame )
	{
		installOperation(
			frame, JComponent.WHEN_IN_FOCUSED_WINDOW, KS_ESCAPE, AMK_DISPATCH_WINDOW_CLOSING,
			new AbstractAction( AMK_DISPATCH_WINDOW_CLOSING ) {
				public void actionPerformed( ActionEvent e )
				{
					frame.dispatchEvent( new WindowEvent( frame, WindowEvent.WINDOW_CLOSING ) );
				}
			}
		);
	}

	/**
	 * <p>
	 * Installs a hide action in the specified dialog.
	 * </p>
	 * - Bound to the Escape key.<br/>
	 * - Active when the dialog is focused.
	 * 
	 * @see #AMK_HIDE_WINDOW
	 */
	public static void installEscapeHideOperation( final JDialog dialog )
	{
		installOperation(
			dialog, JComponent.WHEN_IN_FOCUSED_WINDOW, KS_ESCAPE, AMK_HIDE_WINDOW,
			new AbstractAction( AMK_HIDE_WINDOW ) {
				public void actionPerformed( ActionEvent e )
				{
					dialog.setVisible( false );
				}
			}
		);
	}

	/**
	 * Attaches an empty action to the specified keybind, so that its default behaviour
	 * can be disabled without having to modify the parent's action.
	 * 
	 * 
	 * @see #AMK_IGNORE_OPERATION
	 */
	public static void installIgnoredOperation(
		final JDialog dialog,
		final int condition,
		final KeyStroke keyStroke )
	{
		installOperation( dialog, condition, keyStroke, AMK_IGNORE_OPERATION, ignoreAction );
	}

	/**
	 * Installs a keybind in the specified frame for the specified action.
	 * 
	 * @param frame
	 *            The frame in which this keybind can be activated
	 * @param condition
	 *            When should this keybind be activated.
	 *            Either {@link JComponent#WHEN_FOCUSED}, {@link JComponent#WHEN_IN_FOCUSED_WINDOW}, or
	 *            {@link JComponent#WHEN_ANCESTOR_OF_FOCUSED_COMPONENT}.
	 * @param keyStroke
	 *            The keystroke used to activate the keybind
	 * @param actionKey
	 *            Identifier of the action
	 * @param action
	 *            The action to execute when the keybind is activated
	 */
	public static void installOperation(
		final RootPaneContainer frame,
		final int condition,
		final KeyStroke keyStroke,
		final String actionKey,
		Action action )
	{
		JRootPane root = frame.getRootPane();
		root.getInputMap( condition ).put( keyStroke, actionKey );
		root.getActionMap().put( actionKey, action );
	}

	/**
	 * {@linkplain #installOperation(RootPaneContainer, int, KeyStroke, String, Action)}
	 */
	public static Action installOperation(
		final RootPaneContainer frame,
		final int condition,
		final KeyStroke keyStroke,
		final String actionKey,
		final Runnable runnable )
	{
		Action result = new AbstractAction( actionKey ) {
			public void actionPerformed( ActionEvent e )
			{
				runnable.run();
			}
		};

		installOperation( frame, condition, keyStroke, actionKey, result );
		return result;
	}

	/**
	 * Removes an operation installed in the specified frame for the specified
	 * keystroke and condition.
	 * 
	 * @param frame
	 *            The frame from which the keybind is to be uninstalled.
	 * @param condition
	 *            When should this keybind be activated.
	 *            Either {@link JComponent#WHEN_FOCUSED}, {@link JComponent#WHEN_IN_FOCUSED_WINDOW}, or
	 *            {@link JComponent#WHEN_ANCESTOR_OF_FOCUSED_COMPONENT}.
	 * @param keyStroke
	 *            The keystroke used to activate the keybind
	 */
	public static void uninstallOperation(
		final RootPaneContainer frame,
		final int condition,
		final KeyStroke keyStroke )
	{
		JRootPane root = frame.getRootPane();

		InputMap inputMap = root.getInputMap( condition );
		InputMap parentMap = inputMap.getParent();

		// Temporarily remove the parent input map, so that we don't receive the
		// action key from the parent's input map if the current input map has
		// no action key bound to the key stroke.
		inputMap.setParent( null );

		Object actionKey = root.getInputMap( condition ).get( keyStroke );
		if ( actionKey == null )
			throw new OperationNotInstalledException( keyStroke );
		root.getInputMap( condition ).remove( keyStroke );
		root.getActionMap().remove( actionKey );

		inputMap.setParent( parentMap );
	}

	/**
	 * Returns the Action installed under the specified action key in the specified frame.
	 * 
	 * @param frame
	 *            The frame in which the action is installed
	 * @param actionKey
	 *            The action key to which the action is bound
	 * @param selfOnly
	 *            If true, will only check the frame specified in argument for actions bound
	 *            to the action key.
	 *            If false, will check any parents of the action map for actions bound to the
	 *            action key, if none was found in the first one.
	 */
	public static Action getInstalledOperation(
		final RootPaneContainer frame,
		final Object actionKey,
		boolean selfOnly )
	{
		JRootPane root = frame.getRootPane();

		if ( selfOnly ) {
			ActionMap actionMap = root.getActionMap();
			ActionMap parentMap = actionMap.getParent();

			actionMap.setParent( null );
			Action result = actionMap.get( actionKey );
			actionMap.setParent( parentMap );

			return result;
		}
		else {
			return root.getActionMap().get( actionKey );
		}
	}

	/**
	 * @param component
	 *            the component whose index is to be returned
	 * @return index of the specified component in its parent
	 * @author http://www.java2s.com/Code/Java/Swing-JFC/GetComponentIndex.htm
	 */
	public static final int getComponentIndex( Component component )
	{
		if ( component != null && component.getParent() != null ) {
			Container c = component.getParent();
			for ( int i = 0; i < c.getComponentCount(); i++ ) {
				if ( c.getComponent( i ) == component )
					return i;
			}
		}

		return -1;
	}

	/**
	 * Creates and registers a {@link WindowListener} to {@code parent} window, which causes
	 * the {@code child} window to close when the {@code parent} window is closed.
	 * 
	 * @param parent
	 *            the window whose closing will cause child to close as well.
	 * @param child
	 *            the window that will be closed when parent is closed.
	 * @return the created listener
	 */
	public static WindowListener addCloseCallback( Window parent, Window child )
	{
		return addCloseCallback(
			parent, () -> {
				// Dispatch closing event instead of calling dispose() directly,
				// so that event listeners are notified.
				child.dispatchEvent(
					new WindowEvent(
						child,
						WindowEvent.WINDOW_CLOSING
					)
				);
			}
		);
	}

	/**
	 * Creates and registers a {@link WindowListener} to {@code window}, which executes the callback expression.
	 * 
	 * @param window
	 *            the window whose closing will cause the callback to be executed.
	 * @param callback
	 *            the callback to execute when the window closes.
	 * @return the created listener
	 */
	public static WindowListener addCloseCallback( Window window, Runnable callback )
	{
		WindowListener listener = new WindowAdapter() {
			@Override
			public void windowClosing( WindowEvent e )
			{
				callback.run();
			}
		};
		window.addWindowListener( listener );
		return listener;
	}

	/**
	 * Executes backgroundTask in the background asynchronously, and displays a dialog
	 * to inform the user that the operation is being performed.
	 * 
	 * Does not support progress or current operation status.
	 * 
	 * @param window
	 *            the window that serves as owner of the wait dialog
	 * @param title
	 *            title of the wait dialog
	 * @param log
	 *            logger for errors that occur during the task
	 * @param modal
	 *            whether the wait dialog should be modal, ie. prevent the user from
	 *            interacting with the rest of the application while the dialog is visible
	 * @param backgroundTask
	 *            the task to perform in the background
	 * @param doneTask
	 *            the task to perform if the backgroundTask succeeds. Can be null.
	 * @param failTask
	 *            the task to perform if the backgroundTask fails. Can be null.
	 */
	public static void executeAsyncWithWaitWindow(
		Window window, String title, Logger log, boolean modal,
		Runnable backgroundTask, Runnable doneTask, Consumer<Exception> failTask )
	{
		OperationProgressFrame progressFrame = new OperationProgressFrame( window, title );
		progressFrame.setSize( new Dimension( 300, 150 ) );
		progressFrame.setLocationRelativeTo( window );
		progressFrame.setModal( modal );

		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception
			{
				backgroundTask.run();
				return null;
			}

			@Override
			public void done()
			{
				progressFrame.dispose();

				try {
					get();
					if ( doneTask != null )
						doneTask.run();
				}
				catch ( Exception e ) {
					log.error( "Error while performing async operation titled '" + title + "'!", e );
					if ( failTask != null )
						failTask.accept( e );
				}
			}
		};

		worker.execute();

		progressFrame.setVisible( true );
	}

	/**
	 * Displays an information dialog with the specified title and message.
	 * 
	 * @param title
	 *            title of the dialog
	 * @param message
	 *            message in the dialog
	 */
	public static void showDialog( String title, String message )
	{
		JOptionPane.showMessageDialog( null, message, title, JOptionPane.INFORMATION_MESSAGE );
	}

	/**
	 * Displays an error dialog with the specified message.
	 */
	public static void showErrorDialog( String message )
	{
		JOptionPane.showMessageDialog( null, message, "Error", JOptionPane.ERROR_MESSAGE );
	}

	/**
	 * Displays a warning dialog with the specified message.
	 */
	public static void showWarningDialog( String message )
	{
		JOptionPane.showMessageDialog( null, message, "Warning", JOptionPane.WARNING_MESSAGE );
	}

	/**
	 * Displays an information dialog with the specified message.
	 */
	public static void showInfoDialog( String message )
	{
		JOptionPane.showMessageDialog( null, message, "Information", JOptionPane.INFORMATION_MESSAGE );
	}

	/**
	 * @return true if the user's system reports XFCE desktop environment.
	 */
	public static boolean isXFCE()
	{
		// Normally we can rely on XDG_CURRENT_DESKTOP to identify XFCE,
		// but *sometimes* it returns null (when program is ran using sudo?).
		// So, we check all vars that can contain information about the desktop env.
		String[] vars = {
			getAnyVar( "XDG_CURRENT_DESKTOP" ),
			getAnyVar( "GDMSESSION" ),
			getAnyVar( "DESKTOP_SESSION" ),
			getAnyVar( "XDG_SESSION_DESKTOP" )
		};

		for ( String var : vars ) {
			if ( var == null ) continue;

			var = var.toLowerCase( Locale.ENGLISH );
			if ( var.contains( "xfce" ) )
				return true;
		}

		return false;
	}

	/**
	 * @return true if the Java version installed on the user's system is identified as OpenJDK
	 *         (lowercased version string contains 'open jdk' or 'openjdk')
	 */
	public static boolean isOpenJDK()
	{
		String vmName = System.getProperty( "java.vm.name" );

		if ( vmName == null ) return false; // Can't tell, assume it's not OpenJDK.

		vmName = vmName.toLowerCase( Locale.ENGLISH );
		return vmName.contains( "open jdk" ) || vmName.contains( "openjdk" );
	}

	/**
	 * Attempts to retrieve the value of the specified system or environment variable, or
	 * its uppercase / lowercase variants (in that order) if the original is not found.
	 * 
	 * @param key
	 *            the variable to look for
	 * @return value of the variable, or null if not found.
	 */
	public static String getAnyVar( String key )
	{
		String keyUp = key.toUpperCase( Locale.ENGLISH );
		String keyLow = key.toLowerCase( Locale.ENGLISH );

		String v = System.getenv( key );
		if ( v == null ) v = System.getProperty( key );
		if ( v == null ) v = System.getenv( keyUp );
		if ( v == null ) v = System.getProperty( keyUp );
		if ( v == null ) v = System.getenv( keyLow );
		if ( v == null ) v = System.getProperty( keyLow );

		return v;
	}

	/**
	 * <p>
	 * Turns the specified message into HTML code, for use with JLabels, tooltips and such,
	 * to achieve multiline text.
	 * </p>
	 * 
	 * <pre>
	 * {@literal
	 * Replaces '\n' with '<br/>', '>' and '<' with their HTML equivalents, and wraps
	 * the message in <html></html> tags.
	 * }
	 * </pre>
	 */
	public static String toHTML( String msg )
	{
		msg = msg.replaceAll( "<", "&lt;" );
		msg = msg.replaceAll( ">", "&gt;" );
		msg = msg.replaceAll( "\n", "<br/>" );
		return "<html>" + msg + "</html>";
	}

	public static String unescapeNewline( String msg )
	{
		return msg.replace( "\\n", "\n" );
	}

	/**
	 * Calculates the effective display area of the specified device, ie. the screen area
	 * without stuff like system task bars, etc.
	 * 
	 * @param gd
	 *            the graphics device to get the effective display area for.
	 *            If null, the default screen device is used.
	 * @return the effective display area - screen area without task bars, etc.
	 */
	public static Rectangle getEffectiveDisplayArea( GraphicsDevice gd )
	{
		if ( gd == null ) {
			gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		}

		GraphicsConfiguration gc = gd.getDefaultConfiguration();

		Rectangle bounds = gc.getBounds();
		Insets insets = Toolkit.getDefaultToolkit().getScreenInsets( gc );

		// In graphics environments, top-left corner is usually the (0,0) point.
		return new Rectangle(
			bounds.x + insets.left, bounds.y + insets.top,
			bounds.width - ( insets.left + insets.right ),
			bounds.height - ( insets.top + insets.bottom )
		);
	}


	public static final class OperationNotInstalledException extends RuntimeException
	{
		private int condition;
		private KeyStroke keyStroke;


		public OperationNotInstalledException( KeyStroke keyStroke )
		{
			condition = 0;
			this.keyStroke = keyStroke;
		}

		public OperationNotInstalledException( int condition, KeyStroke keyStroke )
		{
			this.condition = condition;
			this.keyStroke = keyStroke;
		}

		/**
		 * The condition for whose inputMap no operation was found.
		 */
		public int getCondition()
		{
			return condition;
		}

		/**
		 * The keystroke for which no operation was found in the inputMap specified by
		 * the {@link #getCondition() condition}.
		 */
		public KeyStroke getKeyStroke()
		{
			return keyStroke;
		}
	}
}
