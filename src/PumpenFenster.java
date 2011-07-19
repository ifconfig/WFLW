import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;

public class PumpenFenster extends JFrame implements ChangeListener, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VerfPumpen 	verfPumpen;
	private JTable 		verfPumpentable;
	private JScrollPane scrollPaneTabelle;
	private JCheckBox 	cbEnableVerfPumpen;
	private Boolean 	verfPumpenEnabled;
	private JToolBar 	toolbar;
	private Icon		iconAddPumpe;
	private Icon		iconDeletePumpe;
	private JButton		btAddPumpe;
	private JButton		btDeletePumpe;
	private MainWindow  parent;
	
	public PumpenFenster(String string, MainWindow parent) {
		super(string);
		this.parent = parent;
		setSize(300, 300);
		setLocation(parent.getLocationForChild());
		this.setIconImage(parent.getIconImage());
		
		verfPumpen 	= new VerfPumpen();
		verfPumpentable 	= new JTable(verfPumpen);
		scrollPaneTabelle 	= new JScrollPane(verfPumpentable);
		toolbar 			= new JToolBar(null,JToolBar.VERTICAL);
		iconAddPumpe	 	= new ImageIcon(this.getClass().getResource("addPumpe.gif"));
		iconDeletePumpe	 	= new ImageIcon(this.getClass().getResource("deletePumpe.gif"));
		btAddPumpe			= new JButton(iconAddPumpe);
		btDeletePumpe		= new JButton(iconDeletePumpe);
		
		verfPumpentable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		
		
		btAddPumpe.setToolTipText("Pumpe hinzufügen");
		btDeletePumpe.setToolTipText("ausgewählte Pumpe entfernen");
		
		cbEnableVerfPumpen = new JCheckBox("Diese Pumpenliste zur Berechnung nutzen");
		verfPumpenEnabled = false;
		
		toolbar.setFloatable(false);
        toolbar.setRollover(true);
		toolbar.add(btAddPumpe);
		toolbar.add(btDeletePumpe);
		
		setAllEnabled(verfPumpenEnabled);
		
		cbEnableVerfPumpen.addChangeListener(this);
		btAddPumpe.addActionListener(this);
		btDeletePumpe.addActionListener(this);
		
		add(cbEnableVerfPumpen,BorderLayout.PAGE_START);
		add(scrollPaneTabelle,BorderLayout.CENTER);
		add(toolbar,BorderLayout.WEST);
	}

	private void setAllEnabled(Boolean enabled){
		verfPumpentable.setEnabled(verfPumpenEnabled);
		btAddPumpe.setEnabled(verfPumpenEnabled);
		btDeletePumpe.setEnabled(verfPumpenEnabled);
		if(verfPumpenEnabled) verfPumpentable.setBackground(Color.white);
		else verfPumpentable.setBackground(Color.lightGray);
		parent.setEnabledPaEingabe(!enabled);
	}
	
	public Pumpen getVerfPumpen() {
		return verfPumpen;
	}

	public void setVerfPumpen(VerfPumpen verfPumpen) {
		this.verfPumpen = verfPumpen;
	}

	
	
	private class VerfPumpen extends Pumpen{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public VerfPumpen() {
			super();
		}

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public Object getValueAt(int arg0, int arg1) {
			Pumpe tmpPumpe = verfPumpen.get(arg0);
			switch(arg1){
			case 0:
				if(getPumpen().indexOf(tmpPumpe)==0)return "Wasserentnahme";
				else if(getPumpen().indexOf(tmpPumpe)==getPumpen().size()-1)return "Einsatzstelle";
				else return String.format("VKS%d", getPumpen().indexOf(tmpPumpe));
			case 1: return tmpPumpe.getPa();
			default: return null;
			}
		}

		@Override
		public String getColumnName(int columnIndex) {
			switch(columnIndex){
			case 0: return "Pumpe";
			case 1: return "pA";
			default: return ""; 
			}
		}

		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public Class getColumnClass(int columnIndex) {
			switch(columnIndex){
			case 0: return String.class;
			case 1: return Double.class;
			default: return null;
			}
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			if(columnIndex>0) return true;
			else return false;
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			switch(columnIndex){
			case 1:
				getPumpen().get(rowIndex).setPa((Double)aValue);
				break;
			default:
				break;
			}
		}
		
	}



	@Override
	public void stateChanged(ChangeEvent arg0) {
		if(arg0.getSource() == this.cbEnableVerfPumpen){
			verfPumpenEnabled = this.cbEnableVerfPumpen.isSelected();
			setAllEnabled(verfPumpenEnabled);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == btAddPumpe){
			verfPumpen.add(new Pumpe());
			verfPumpentable.tableChanged(new TableModelEvent(verfPumpen));
		}else if(arg0.getSource() == btDeletePumpe){
			if(verfPumpentable.getSelectedRow() != -1){
				verfPumpen.removePumpe(verfPumpentable.getSelectedRow());
				verfPumpentable.tableChanged(new TableModelEvent(verfPumpen));
			}else{
				System.out.println("Nichts markiert");
			}
		}
	}
	
	public Boolean verfPumpenBeachten(){
		return this.cbEnableVerfPumpen.isSelected();
	}
}
