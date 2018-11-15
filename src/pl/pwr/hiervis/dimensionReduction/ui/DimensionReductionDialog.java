package pl.pwr.hiervis.dimensionReduction.ui;

import javax.swing.JDialog;

import pl.pwr.hiervis.dimensionReduction.methods.DimensionReduction;

public abstract class DimensionReductionDialog extends JDialog
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int maxOutputDimensions = 2;
	protected int pointsAmount = 0;
	DimensionReduction result = null;

	public abstract String getName();

	public abstract String getSimpleName();

	public DimensionReduction showDialog(int maxOutputDimensions, int pointsAmount)
	{
		this.maxOutputDimensions = maxOutputDimensions;
		this.pointsAmount = pointsAmount;
		setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
		remodel();
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		return result;
	}

	public abstract void remodel();
}
