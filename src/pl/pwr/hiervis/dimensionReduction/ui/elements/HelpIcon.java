package pl.pwr.hiervis.dimensionReduction.ui.elements;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class HelpIcon extends JLabel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4914876513575847817L;

	public HelpIcon(int x, int y)
	{
		super();
		setToolTipText("<html> Controls: <br>\r\nESC      - Closes the dialog window <br>\r\n"
				+ "ENTER - Confirms all the choises and closes window<br>\r\n"
				+ "&#9(same behaviour as presing \"OK\" button)<br>\r\n" + "SPACE  - Same as ENTER<br>\r\n"
				+ "MOUSE SCROL - Changes the values of spiner or list if current <br>\r\n"
				+ "CTRL + MOUSE SCROLL - the change steep value is halved <br>\r\n"
				+ "ALT +  MOUSE SCROLL - the change steep value is multiplyed by 5");
		setIcon(new ImageIcon(this.getClass().getResource("/pl/pwr/hiervis/dimensionReduction/ui/elements/hl25.png")));
		setBounds(x, y, 25, 25);
	}
}
