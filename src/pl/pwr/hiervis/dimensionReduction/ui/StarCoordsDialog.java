package pl.pwr.hiervis.dimensionReduction.ui;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import pl.pwr.hiervis.dimensionReduction.methods.DimensionReduction;
import pl.pwr.hiervis.dimensionReduction.methods.StarCoordinates;
import pl.pwr.hiervis.dimensionReduction.ui.elements.HelpIcon;

public class StarCoordsDialog extends DimensionReductionDialog
{

	private static final long serialVersionUID = 1L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			StarCoordsDialog dialog = new StarCoordsDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public StarCoordsDialog()
	{
		this.setResizable(false);
		setBounds(100, 100, 270, 150);
		getContentPane().setLayout(null);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 78, 254, 33);
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				okButton.addActionListener(this::setResult);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						result = null;
						dispose();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
		setKeybind((JPanel) getContentPane());

		JLabel lbl = new HelpIcon(235, 0);
		lbl.setVerticalAlignment(SwingConstants.TOP);
		lbl.setHorizontalAlignment(SwingConstants.TRAILING);
		getContentPane().add(lbl);

		{
			JLabel lblConfirmUsingStar = new JLabel(
					"<html>Confirm using Star Coordinates <br> as dimension reduction method?\r\n");
			lblConfirmUsingStar.setBounds(11, 27, 231, 40);
			getContentPane().add(lblConfirmUsingStar);
			lblConfirmUsingStar.setFont(new Font("Tahoma", Font.PLAIN, 16));
		}
	}

	@Override
	public String getName()
	{
		return "StarCoordinates";
	}

	@Override
	public String getSimpleName()
	{
		return "StarCoors";
	}

	@Override
	public void remodel()
	{
		// No needed for a body for this dialog

	}

	@Override
	public Class<? extends DimensionReduction> getResultClass()
	{
		return StarCoordinates.class;
	}

	@Override
	public void setResult(ActionEvent e)
	{
		result = new StarCoordinates();
		dispose();
	}

}
