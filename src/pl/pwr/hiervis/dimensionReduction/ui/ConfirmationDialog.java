package pl.pwr.hiervis.dimensionReduction.ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class ConfirmationDialog extends JDialog {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private JButton okButton;

    public void showDialog(int x, int y) {
	setLocation(x, y);
	// setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
	setVisible(true);
	setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

    }

    /**
     * Create the dialog.
     */
    public ConfirmationDialog() {
	this.setResizable(false);
	setBounds(100, 100, 270, 150);
	getContentPane().setLayout(null);

	// Border border = BorderFactory.createLineBorder(Color.BLACK);

	okButton = new JButton("OK");
	okButton.setBounds(105, 84, 54, 26);
	getContentPane().add(okButton);
	okButton.setActionCommand("OK");
	okButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		dispose();
	    }
	});
	getRootPane().setDefaultButton(okButton);

	JLabel label = new JLabel("<html>�          Dimension Reduction <br> computering task started");
	label.setFont(new Font("Tahoma", Font.PLAIN, 16));
	label.setHorizontalAlignment(SwingConstants.CENTER);
	label.setBounds(21, 0, 222, 83);
	getContentPane().add(label);

    }
}
