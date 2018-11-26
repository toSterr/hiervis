package pl.pwr.hiervis.ui.jakubTestuje;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;

import pl.pwr.hiervis.util.ui.VerticalLabelUI;

public class testui {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					testui window = new testui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public testui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 615, 440);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));

		//aby wygladalo oki
		try 
		{
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}

		//pasek zakladek z boku
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		frame.getContentPane().add(tabbedPane);
		
		
		//TAblica Zakladek moze byc ale nie widac tego w disanjerze teraz
		int ileZakladek=3;
		ArrayList <JPanel> listaZakladek= new ArrayList<JPanel>() ;
		ArrayList<JLabel> listaNazwZakladek= new ArrayList<JLabel>();
		for (int i=0; i<ileZakladek; i++)
		{
			listaZakladek.add(new JPanel());
			listaNazwZakladek.add( new JLabel("Tab #"+i) );
			listaNazwZakladek.get(i).setUI(new VerticalLabelUI(false));
		}
		for (int i=0;i<ileZakladek;i++)
		{
			listaZakladek.get(i).add(new JLabel("Jakas tam tresc panelu "+i));
			tabbedPane.addTab(null,listaZakladek.get(i));
			tabbedPane.setTabComponentAt(i, listaNazwZakladek.get(i));
		}
		
		//panel z boku
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.GRAY);
		frame.getContentPane().add(panel_2);
		
		Label label_3 = new Label("New label");
		label_3.setBounds(68, 10, 78, 43);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 164, 184, 141);
		panel_2.add(scrollPane);
		
		JTextPane lab = new JTextPane ();
		scrollPane.setViewportView(lab);
		lab.setText( ""+((JLabel)listaZakladek.get(0).getComponents()[0]).getText() );
		
		JButton btnNewButton_3 = new JButton("hide");
		btnNewButton_3.setBounds(42, 59, 119, 43);
		btnNewButton_3.addActionListener
		(
			new ActionListener()
			{
		        public void actionPerformed(ActionEvent e)
		        {
		        	//tabbedPane.setVisible(false);
		        	//tabbedPane.remove( listaZakladek.get(2) );
		        	tabbedPane.setBounds( 0,0,30, 400);
		        }
			}
		);
			
		JButton btnShow = new JButton("Show");
		btnShow.setBounds(57, 130, 89, 23);
		btnShow.addActionListener
		(
			new ActionListener()
			{
		        public void actionPerformed(ActionEvent e)
		        {
		        	tabbedPane.setVisible(true);
		        	tabbedPane.addTab(null, listaZakladek.get(2));
		        	tabbedPane.setTabComponentAt(2, listaNazwZakladek.get(2));
		        }
			}
		);
		
		
		panel_2.setLayout(null);
		panel_2.add(label_3);
		panel_2.add(btnNewButton_3);
		panel_2.add(btnShow);
		
	}
}
