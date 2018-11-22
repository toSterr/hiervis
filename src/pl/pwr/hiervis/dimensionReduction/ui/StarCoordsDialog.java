package pl.pwr.hiervis.dimensionReduction.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import pl.pwr.hiervis.dimensionReduction.methods.DimensionReduction;
import pl.pwr.hiervis.dimensionReduction.methods.StarCoordinates;

public class StarCoordsDialog extends DimensionReductionDialog
{

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

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
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblConfirmUsingStar = new JLabel("<html>Confirm using Star Coordinates <br> as dimension reduction method?\r\n");
			lblConfirmUsingStar.setBounds(16, 21, 231, 40);
			lblConfirmUsingStar.setFont(new Font("Tahoma", Font.PLAIN, 16));
			contentPanel.add(lblConfirmUsingStar);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				okButton.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						result = new StarCoordinates();
						dispose();
					}
				});
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
						result=null;
						dispose();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
		setKeybind( (JPanel)getContentPane() );
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
		//  No needed for a body for this dialog

	}

	@Override
	public Class<? extends DimensionReduction> getResultClass() {
		return StarCoordinates.class;
	}

}
