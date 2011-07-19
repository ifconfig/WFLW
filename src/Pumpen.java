import java.util.*;

import javax.swing.table.AbstractTableModel;

public class Pumpen extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected List<Pumpe> pumpen;
	
	public Pumpen(){
		pumpen = new ArrayList<Pumpe>();
	}
	
	public void clear(){
		pumpen.clear();
	}
	
	public boolean add(Pumpe newPumpe){
		return pumpen.add(newPumpe);
	}
	
	public Pumpe get(int arg0){
		return pumpen.get(arg0);
	}
	
	public int size(){
		return pumpen.size();
	}
	
	public boolean isEmpty(){
		return pumpen.isEmpty();
	}
	
	@Override
	public int getColumnCount() {
		// Nr|DelteL|DeltaH|Lon|Lat|Länge|Höhe
		return 8;
	}

	@Override
	public int getRowCount() {
		return pumpen.size();
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		Pumpe tmpPumpe = pumpen.get(arg0);
		switch(arg1){
		case 0:
			if(pumpen.indexOf(tmpPumpe)==0)return "Wasserentnahme";
			else if(pumpen.indexOf(tmpPumpe)==pumpen.size()-1)return "Einsatzstelle";
			else return String.format("VKS%d", pumpen.indexOf(tmpPumpe));
		case 1:
			return tmpPumpe.getDeltaL();
		case 2:
			return tmpPumpe.getDeltaH();
		case 3:
			return tmpPumpe.getPe();
		case 4:
			return tmpPumpe.getLat();
		case 5:
			return tmpPumpe.getLon();
		case 6:
			return tmpPumpe.getGesLaenge();
		case 7:
			return tmpPumpe.getHeight();
		//case 8:
		//	return tmpPumpe.isFixed();
		default:
		return null;		
		}
	}

	public Pumpen(List<Pumpe> pumpen) {
		this.pumpen = pumpen;
	}

	public Pumpen(Pumpen pumpen) {
		this.pumpen = pumpen.getPumpen();
	}

	public List<Pumpe> getPumpen() {
		return pumpen;
	}

	public void setPumpen(List<Pumpe> pumpen) {
		this.pumpen = pumpen;
	}

	@Override
	public String getColumnName( int columnIndex ){
		switch(columnIndex){
		case 0:
			return "Pumpe";
		case 1:
			return "DeltaL";
		case 2:
			return "DeltaH";
		case 3:
			return "Eingangsdruck";
		case 4:
			return "Breitengrad";
		case 5:
			return "Längengrad";
		case 6:
			return "Ges.Länge";
		case 7:
			return "Ges.Höhe";
		//case 8:
		//	return "fest";
		default:
			return "";
		}
	}	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int columnIndex) {
        switch( columnIndex ){
            case 0: return String.class;
            case 1: return Double.class;
            case 2: return Double.class;
            case 3: return Double.class;
            case 4: return Double.class;
            case 5: return Double.class;
            case 6: return Double.class;
            case 7: return Double.class;
            //case 8: return Boolean.class;
            default: return null;
        }
    }
	
	// Jede Zelle ist editierbar
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if(columnIndex == 8)return true;
        else return false;
    }
    
 // Wird aufgerufen, falls der Wert einer Zelle verändert wurde
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        switch( columnIndex ){
            case 8:
                pumpen.get(rowIndex).setFixed((Boolean)aValue);
                break;
        }
    }
	
	public Pumpen getFixed(){
		Pumpen fixedPumpen = new Pumpen();
		for(Pumpe aktPumpe:pumpen){
			if(aktPumpe.isFixed()) fixedPumpen.add(aktPumpe);
		}
		return fixedPumpen;
	}

	public Pumpe getLast(){
		return this.get(this.size()-1);
	}
	
	public void removePumpe(int index){
		pumpen.remove(index);
	}
}
