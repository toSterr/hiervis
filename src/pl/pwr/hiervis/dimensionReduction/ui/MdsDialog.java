package pl.pwr.hiervis.dimensionReduction.ui;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import pl.pwr.hiervis.dimensionReduction.distanceMeasures.DistanceMeasure;
import pl.pwr.hiervis.dimensionReduction.distanceMeasures.Euclidean;
import pl.pwr.hiervis.dimensionReduction.distanceMeasures.Manhattan;
import pl.pwr.hiervis.dimensionReduction.distanceMeasures.Minkowski;
import pl.pwr.hiervis.dimensionReduction.methods.DimensionReduction;
import pl.pwr.hiervis.dimensionReduction.methods.MultidimensionalScaling;

public class MdsDialog extends DimensionReductionDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFormattedTextField textField;
	private JLabel lblNewLabel_1;
	private JList<DistanceMeasure> list;
	private DistanceMeasure[] distanceMeasures;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			MdsDialog dialog = new MdsDialog();
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
	public MdsDialog()
	{
		this.setResizable(false);
		setKeybind( (JPanel)getContentPane() );
		setTitle("Multidimensional Scaling");
		setBounds(100, 100, 450, 272);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(30, 42, 374, 99);

		JPanel buttonPane = new JPanel();
		buttonPane.setBounds(42, 199, 349, 33);
		buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		{
			JButton okButton = new JButton("OK");
			okButton.setActionCommand("OK");
			buttonPane.add(okButton);
			okButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					if (list.getSelectedIndex() != 2)
						result = new MultidimensionalScaling(distanceMeasures[list.getSelectedIndex()]);
					else
					{
						result = new MultidimensionalScaling(new Minkowski((double) textField.getValue()));
					}
					dispose();
				}
			});
			getRootPane().setDefaultButton(okButton);
		}

		{
			JButton cancelButton = new JButton("Cancel");
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);
			cancelButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					result=null;
					dispose();
				}
			});
		}

		JLabel lblNewLabel = new JLabel("Select distance measure");
		lblNewLabel.setBounds(108, 11, 217, 25);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));

		list = new JList<DistanceMeasure>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(list);

		list.setFont(new Font("Tahoma", Font.BOLD, 20));

		distanceMeasures = new DistanceMeasure[3];
		distanceMeasures[0] = new Euclidean();
		distanceMeasures[1] = new Manhattan();
		distanceMeasures[2] = new Minkowski();

		list.setListData(distanceMeasures);

		if (list.getModel().getSize() >= 0)
		{
			list.setSelectedIndex(0);
		}
		list.getSelectionModel().addListSelectionListener(new SharedListSelectionHandler());

		getContentPane().setLayout(null);
		getContentPane().add(lblNewLabel);
		getContentPane().add(scrollPane);
		getContentPane().add(buttonPane);
		NumberFormat percentEditFormat = NumberFormat.getNumberInstance();
		percentEditFormat.setMinimumFractionDigits(1);
		percentEditFormat.setMaximumFractionDigits(5);
		percentEditFormat.setMaximumIntegerDigits(2);

		NumberFormatter percentEditFormatter = new NumberFormatter(percentEditFormat)
		{
			private static final long serialVersionUID = 1L;

			public String valueToString(Object o) throws ParseException
			{
				Number number = (Number) o;
				if (number != null)
				{
					double d = number.doubleValue();
					number = new Double(d);
				}
				return super.valueToString(number);
			}

			public Object stringToValue(String s) throws ParseException
			{
				Number number = (Number) super.stringToValue(s);
				if (number != null)
				{
					double d = number.doubleValue();
					number = new Double(d);
				}
				return number;
			}
		};

		NumberFormat percentDisplayFormat = NumberFormat.getNumberInstance();
		percentDisplayFormat.setMinimumFractionDigits(1);
		percentDisplayFormat.setMaximumFractionDigits(5);

		percentEditFormatter.setAllowsInvalid(false);

		textField = new JFormattedTextField(new DefaultFormatterFactory(new NumberFormatter(percentDisplayFormat),
				new NumberFormatter(percentDisplayFormat), percentEditFormatter));

		textField.setHorizontalAlignment(SwingConstants.LEFT);
		textField.setValue(new Double(1.5));
		textField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField.setBounds(167, 152, 99, 36);
		getContentPane().add(textField);
		textField.setColumns(10);

		lblNewLabel_1 = new JLabel(" Power value");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(274, 152, 117, 36);
		getContentPane().add(lblNewLabel_1);
		textField.addPropertyChangeListener("value", new propertyChanger());
		textField.setVisible(false);
		lblNewLabel_1.setVisible(false);

	}

	class propertyChanger implements PropertyChangeListener
	{
		public void propertyChange(PropertyChangeEvent e)
		{
			Object source = e.getSource();
			if (source == textField)
			{
				// System.out.println(textField.getValue());
			}
		}
	}

	class SharedListSelectionHandler implements ListSelectionListener
	{

		public void valueChanged(ListSelectionEvent e)
		{
			ListSelectionModel lsm = (ListSelectionModel) e.getSource();
			if (lsm.isSelectionEmpty())
			{
				System.out.println(" <none>");
			}
			else
			{
				if (lsm.isSelectedIndex(2))
				{
					textField.setVisible(true);
					lblNewLabel_1.setVisible(true);
				}
				else
				{
					textField.setVisible(false);
					lblNewLabel_1.setVisible(false);
				}
			}
		}
	}

	public String getName()
	{
		return "MultidimensionalScaling";
	}

	@Override
	public String getSimpleName()
	{
		return "MDS";
	}

	@Override
	public void remodel()
	{
		// No needed for a body for this dialog

	}

	@Override
	public Class<? extends DimensionReduction> getResultClass() 
	{
		return MultidimensionalScaling.class;
	}
}
