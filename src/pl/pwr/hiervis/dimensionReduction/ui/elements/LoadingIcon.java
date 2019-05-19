package pl.pwr.hiervis.dimensionReduction.ui.elements;

import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class LoadingIcon extends JLabel {
	private static final long serialVersionUID = 6914976916388241044L;
	private JLabel message;

	public LoadingIcon(int x, int y) {
		super();
		setBounds(x, y, 200, 200);
		ImageIcon img = new ImageIcon(
				this.getClass().getResource("/pl/pwr/hiervis/dimensionReduction/ui/elements/loading.gif"));
		setIcon(img);
		message = new JLabel("<html>Dimension<br>&#xa0Reduction<br>Computing");
		message.setVerticalAlignment(JLabel.CENTER);
		message.setHorizontalAlignment(JLabel.CENTER);
		message.setBounds(0, 0, 200, 200);
		message.setFont(new Font("Tahoma", Font.PLAIN, 16));
		add(message);
	}

	public LoadingIcon() {
		this(0, 0);
	}

	public void showIcon(int x, int y) {
		setLocation(x, y);
		setVisible(true);
	}

	public void hideIcon() {
		setVisible(false);
	}
}
