package pl.pwr.hiervis.dimensionReduction.ui;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import pl.pwr.hiervis.dimensionReduction.methods.DimensionReduction;
import pl.pwr.hiervis.dimensionReduction.methods.PrincipalComponentAnalysis;

public class PcaDialog extends DimensionReductionDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JSpinner spinner;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			PcaDialog dialog = new PcaDialog();
			dialog.showDialog(20, 10);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public PcaDialog()
	{

		this.setResizable(false);
		setBounds(100, 100, 300, 190);
		setKeybind((JPanel) getContentPane());
		getContentPane().setLayout(null);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(48, 118, 188, 33);
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

		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(2, 2, maxOutputDimensions, 1));
		spinner.setBounds(86, 47, 111, 33);

		this.addMouseWheelListener(new MouseWheelListener()
		{

			@Override
			public void mouseWheelMoved(MouseWheelEvent e)
			{
				if ((int) spinner.getValue() - e.getWheelRotation() >= 2
						&& (int) spinner.getValue() - e.getWheelRotation() <= maxOutputDimensions)
					spinner.setValue((int) spinner.getValue() - e.getWheelRotation());

			}
		});

		getContentPane().add(spinner);

		JLabel lblNewLabel = new JLabel("Final number of dimensions");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setBounds(40, 11, 204, 33);
		getContentPane().add(lblNewLabel);

		JLabel lbl = new JLabel("");
		lbl.setToolTipText(
				"<html> Controls: <br>\r\nESC      - Closes the dialog window <br>\r\nENTER - Confirms all the choises and closes window<br>\r\n&#9(same behaviour as presing \"OK\" button)<br>\r\nSPACE  - Same as ENTER<br>\r\nMOUSE SCROL - Changes the values of spines if current <br>\r\nCTRL + MOUSE SCROLL - the change steep value is halved <br>\r\nALT +  MOUSE SCROLL - the change steep value is multiplyed by 5");
		lbl.setIcon(new ImageIcon(MdsDialog.class.getResource("/pl/pwr/hiervis/dimensionReduction/ui/hl25.png")));
		lbl.setBounds(265, 0, 25, 25);
		getContentPane().add(lbl);
	}

	@Override
	public String getName()
	{
		return "PrincipalComponentAnalysis";
	}

	@Override
	public String getSimpleName()
	{
		return "PCA";
	}

	@Override
	public void remodel()
	{
		spinner.setModel(new SpinnerNumberModel(2, 2, maxOutputDimensions, 1));
	}

	@Override
	public Class<? extends DimensionReduction> getResultClass()
	{
		return PrincipalComponentAnalysis.class;
	}

	@Override
	public void setResult(ActionEvent e)
	{
		result = new PrincipalComponentAnalysis((int) spinner.getValue());
		dispose();
	}
}
