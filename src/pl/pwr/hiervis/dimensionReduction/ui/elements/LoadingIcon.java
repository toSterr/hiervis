package pl.pwr.hiervis.dimensionReduction.ui.elements;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class LoadingIcon extends JLabel
{
	private static final long serialVersionUID = 6914976916388241044L;

	public LoadingIcon(int x, int y)
	{
		super();
		setBounds(x, y, 200, 200);
		ImageIcon img = new ImageIcon(
				this.getClass().getResource("/pl/pwr/hiervis/dimensionReduction/ui/elements/loading.gif"));
		setIcon(img);
	}

	public LoadingIcon()
	{
		super();
		setSize(200, 200);
		ImageIcon img = new ImageIcon(
				this.getClass().getResource("/pl/pwr/hiervis/dimensionReduction/ui/elements/loading.gif"));
		setIcon(img);
	}

	public void showIcon(int x, int y)
	{
		setLocation(x, y);
		setVisible(true);
	}

	public void hideIcon()
	{
		setVisible(false);
	}
}
