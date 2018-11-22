package pl.pwr.hiervis.dimensionReduction.ui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.dimensionReduction.methods.DimensionReduction;
import pl.pwr.hiervis.util.HierarchyUtils;

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
		setLocation(100, 100);
		setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
		remodel();
		setVisible(true);
		
		
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) 
		    {
		       result=null;
		    }
		});
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		return result;
	}
	public DimensionReduction showDialog(HVContext context, int x, int y)
	{
		this.maxOutputDimensions = HierarchyUtils.getFirstInstance(context.getHierarchy().getMainHierarchy())
				.getData().length;
		this.pointsAmount = context.getHierarchy().getMainHierarchy().getOverallNumberOfInstances();
		setLocation(x, y);
		setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
		remodel();
		
		
		
		setVisible(true);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) 
		    {
		       result=null;
		    }
		});
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		return result;
	
	}
	public DimensionReduction showDialog(HVContext context)
	{
		return showDialog(context, 100, 100);
	}
	
	public abstract Class<? extends DimensionReduction> getResultClass();
	
	
	public abstract void remodel();
	
	/*
	 * Need to be manually added to constructor of child class to work
	 *  (didn't find any solution to add keybind to this class)
	 * */
	public void setKeybind(JPanel contentPanel)
	{
		contentPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put( KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESC" );
		contentPanel.getInputMap(JComponent.WHEN_FOCUSED).put( KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESC" );
		contentPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put( KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESC" );
		contentPanel.getActionMap().put("ESC", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				result=null;
				dispose();
			}
		});
	}
}
