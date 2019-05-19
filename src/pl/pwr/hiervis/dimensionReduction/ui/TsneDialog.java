package pl.pwr.hiervis.dimensionReduction.ui;

import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import pl.pwr.hiervis.dimensionReduction.methods.DimensionReduction;
import pl.pwr.hiervis.dimensionReduction.methods.Tsne;
import pl.pwr.hiervis.dimensionReduction.ui.elements.HelpIcon;
import pl.pwr.hiervis.dimensionReduction.ui.elements.SpinnerMouseWheelChanger;

public class TsneDialog extends DimensionReductionDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JSpinner spinner_pca;
	private JLabel lblPcaInitialDimensions;
	private JCheckBox chckbxUsePca;
	private JSpinner spinner_iter;
	private JSpinner spinner_per;
	private JSpinner spinner_tetha;
	private JLabel lblPerplexity;
	private JLabel lblMaxIterations;
	private JLabel lblTetha;
	private JLabel lblMaxIterDesc;
	private JLabel lblPerDesc;
	private JLabel lblThetaDesc;
	private JLabel lblParallerDesc;
	private JLabel lblUsePcaDesc;
	private JLabel lblPcaDesc;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			TsneDialog dialog = new TsneDialog();
			dialog.showDialog(10, 200);

			// dialog.setVisible(true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public TsneDialog() {
		this.setResizable(true);
		setBounds(100, 100, 408, 508);
		setTitle(getName());
		setKeybind((JPanel) getContentPane());
		getContentPane().setLayout(new FormLayout(
				new ColumnSpec[] { ColumnSpec.decode("16dlu:grow"), FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, ColumnSpec.decode("left:max(150dlu;default)"),
						ColumnSpec.decode("right:16dlu:grow"), },
				new RowSpec[] { FormSpecs.UNRELATED_GAP_ROWSPEC, RowSpec.decode("23px"), RowSpec.decode("fill:default"),
						FormSpecs.UNRELATED_GAP_ROWSPEC, RowSpec.decode("23px"), RowSpec.decode("fill:default"),
						FormSpecs.UNRELATED_GAP_ROWSPEC, RowSpec.decode("23px"), RowSpec.decode("fill:default"),
						FormSpecs.UNRELATED_GAP_ROWSPEC, RowSpec.decode("23px"), RowSpec.decode("fill:default"),
						FormSpecs.UNRELATED_GAP_ROWSPEC, RowSpec.decode("23px"),
						RowSpec.decode("fill:max(7dlu;default)"), FormSpecs.UNRELATED_GAP_ROWSPEC,
						RowSpec.decode("23px"), RowSpec.decode("fill:default"), RowSpec.decode("bottom:50px"),
						RowSpec.decode("30dlu:grow"), }));

		spinner_iter = new JSpinner();
		getContentPane().add(spinner_iter, "2, 2");
		spinner_iter.setModel(new SpinnerNumberModel(1000, 100, 5000, 100));
		spinner_iter.addMouseWheelListener(new SpinnerMouseWheelChanger());

		lblMaxIterations = new JLabel("Max iterations");
		getContentPane().add(lblMaxIterations, "4, 2");

		lblMaxIterDesc = new JLabel("<html>Positive integer specifying the maximum number of optimization iterations.");
		lblMaxIterDesc.setFont(new Font("Tahoma", Font.PLAIN, 10));
		getContentPane().add(lblMaxIterDesc, "2, 3, 3, 1");

		// maxIterationsPanel.addMouseWheelListener(new spin(spinner_iter));

		spinner_per = new JSpinner();
		getContentPane().add(spinner_per, "2, 5");
		spinner_per.setModel(new SpinnerNumberModel(20.0, 5.0, 50, 5.0));
		spinner_per.addMouseWheelListener(new SpinnerMouseWheelChanger());

		// spinner_per.setBounds(146, 137, 53, 20);

		lblPerplexity = new JLabel("Perplexity");
		getContentPane().add(lblPerplexity, "4, 5");

		lblPerDesc = new JLabel(
				"<html>The perplexity is related to the number of nearest neighbors that is used in other manifold learning algorithms. Larger datasets usually require a larger perplexity. Consider selecting a value between 5 and 50. The choice is not extremely critical since t-SNE is quite insensitive to this parameter.");
		lblPerDesc.setFont(new Font("Tahoma", Font.PLAIN, 10));
		getContentPane().add(lblPerDesc, "2, 6, 3, 1");

		// perplexityPanel.addMouseWheelListener(new spin(spinner_per));

		spinner_tetha = new JSpinner();
		getContentPane().add(spinner_tetha, "2, 8");
		spinner_tetha.setModel(new SpinnerNumberModel(0.5, 0.0, 1.0, 0.05));
		spinner_tetha.addMouseWheelListener(new SpinnerMouseWheelChanger());

		chckbxUsePca = new JCheckBox();
		chckbxUsePca.setText("Use PCA for pre reduction");
		chckbxUsePca.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (((JCheckBox) e.getSource()).isSelected()) {
					spinner_pca.setEnabled(true);
					lblPcaInitialDimensions.setEnabled(true);
					lblPcaDesc.setEnabled(true);
				}
				else {
					spinner_pca.setEnabled(false);
					lblPcaInitialDimensions.setEnabled(false);
					lblPcaDesc.setEnabled(false);
				}
			}
		});

		lblTetha = new JLabel("Theta");
		getContentPane().add(lblTetha, "4, 8");

		lblThetaDesc = new JLabel(
				"<html>Barnes-Hut tradeoff parameter, specified as a scalar from 0 through 1. Higher values give a faster but less accurate optimization.");
		lblThetaDesc.setFont(new Font("Tahoma", Font.PLAIN, 10));
		getContentPane().add(lblThetaDesc, "2, 9, 3, 1");

		JCheckBox chckbxParallerCalculations = new JCheckBox();
		chckbxParallerCalculations.setText("Paraller calculations");
		chckbxParallerCalculations.setSelected(true);
		getContentPane().add(chckbxParallerCalculations, "2, 11, 3, 1, left, center");

		lblParallerDesc = new JLabel("<html>Perform parraller calculations using multithreading.");
		lblParallerDesc.setFont(new Font("Tahoma", Font.PLAIN, 10));
		getContentPane().add(lblParallerDesc, "2, 12, 3, 1");
		getContentPane().add(chckbxUsePca, "2, 14, 3, 1, left, center");

		lblUsePcaDesc = new JLabel("<html>Using PCA for predimension reduction before t-SNE.");
		lblUsePcaDesc.setFont(new Font("Tahoma", Font.PLAIN, 10));
		getContentPane().add(lblUsePcaDesc, "2, 15, 3, 1");

		spinner_pca = new JSpinner();
		getContentPane().add(spinner_pca, "2, 17");
		spinner_pca.setEnabled(false);
		spinner_pca.setModel(new SpinnerNumberModel(2, 2, maxOutputDimensions, 1));
		spinner_pca.addMouseWheelListener(new SpinnerMouseWheelChanger());

		lblPcaInitialDimensions = new JLabel("PCA initial dimensions");
		getContentPane().add(lblPcaInitialDimensions, "4, 17");
		lblPcaInitialDimensions.setEnabled(false);

		lblPcaDesc = new JLabel("<html>Number of initial dimension for predimension reduction.");
		lblPcaDesc.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblPcaDesc.setEnabled(false);

		getContentPane().add(lblPcaDesc, "2, 18, 3, 1");

		JPanel buttonPane = new JPanel();
		getContentPane().add(buttonPane, "2, 19, 3, 1, center, bottom");

		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(this::setResult);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(this::closeDialog);
		GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
		gl_buttonPane.setAutoCreateContainerGaps(true);
		gl_buttonPane.setAutoCreateGaps(true);
		gl_buttonPane.setHorizontalGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_buttonPane.createSequentialGroup().addComponent(okButton).addComponent(cancelButton)));
		gl_buttonPane.setVerticalGroup(
				gl_buttonPane.createParallelGroup(Alignment.LEADING).addComponent(okButton).addComponent(cancelButton));
		buttonPane.setLayout(gl_buttonPane);

		JLabel lbl = new HelpIcon(300, 0);
		getContentPane().add(lbl, "5, 1, 1, 2, right, top");

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	@Override
	public String getName() {
		return Tsne.sGetName();
	}

	@Override
	public String getSimpleName() {
		return Tsne.sGetSimpleName();
	}

	@Override
	public void remodel() {
		spinner_pca.setModel(new SpinnerNumberModel(2, 2, maxOutputDimensions, 1));
		double min = 0.5;
		double max = Math.round((pointsAmount - 1) / 3);
		double value = Math.max(min, Math.min(20.0, max));
		double stepSize = Math.max(1, Math.round((max - min) / 30));
		spinner_per.setModel(new SpinnerNumberModel(value, min, max, stepSize));
	}

	@Override
	public Class<? extends DimensionReduction> getResultClass() {
		return Tsne.class;
	}

	@Override
	public void setResult(ActionEvent e) {
		result = new Tsne(chckbxUsePca.isSelected(), (int) spinner_pca.getValue(), 2, (int) spinner_iter.getValue(),
				(double) spinner_per.getValue(), chckbxUsePca.isSelected(), (double) spinner_tetha.getValue(), true,
				true);
		dispose();

	}
}
