package pl.pwr.hiervis.dimensionReduction.ui.elements;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SpinnerMouseWheelChanger implements MouseWheelListener {

	private static final Logger log = LogManager.getLogger(SpinnerMouseWheelChanger.class);

	private double smallStepMulti;
	private double BigStepMulti;
	private JSpinner jSpinner;

	public SpinnerMouseWheelChanger(JSpinner jSpinner, double smallStepMulti, double BigStepMulti) {
		this.smallStepMulti = smallStepMulti;
		this.BigStepMulti = BigStepMulti;
		this.jSpinner = jSpinner;
	}

	public SpinnerMouseWheelChanger(double smallStepMulti, double BigStepMulti) {
		this((JSpinner) null, smallStepMulti, BigStepMulti);
	}

	public SpinnerMouseWheelChanger() {
		this((JSpinner) null, 0.5, 5);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		JSpinner spinner;

		if (jSpinner != null) {
			spinner = jSpinner;
		}
		else if (e.getComponent().getClass() == JSpinner.class) {
			spinner = (JSpinner) e.getComponent();
		}
		else
			return;

		if (!spinner.isEnabled())
			return;

		SpinnerNumberModel spinnerModel = (SpinnerNumberModel) spinner.getModel();
		double multi = 1;

		if (e.isAltDown())
			multi = BigStepMulti;
		if (e.isControlDown())
			multi = smallStepMulti;

		if (spinnerModel.getValue().getClass() == Integer.class) {
			Integer min = ((Integer) spinnerModel.getMinimum());
			Integer max = ((Integer) spinnerModel.getMaximum());
			Integer step = (int) (((Integer) spinnerModel.getStepSize()) * multi);
			Integer value = ((Integer) spinner.getValue());

			if (value - e.getWheelRotation() * step >= min && value - e.getWheelRotation() * step <= max)
				spinner.setValue(value - e.getWheelRotation() * step);
		}
		else if (spinnerModel.getValue().getClass() == Double.class) {
			Double min = (Double) spinnerModel.getMinimum();
			Double max = (Double) spinnerModel.getMaximum();
			Double step = (Double) spinnerModel.getStepSize() * multi;
			Double value = (Double) spinner.getValue();

			if (value - e.getWheelRotation() * step >= min && value - e.getWheelRotation() * step <= max)
				spinner.setValue(value - e.getWheelRotation() * step);
		}
		else {
			log.debug(spinnerModel.getValue().getClass());
		}
	}

}
