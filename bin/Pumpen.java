import java.util.*;
import javax.swing.table.AbstractTableModel;

public class Pumpen extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Pumpe> pumpen;
	
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
			return pumpen.indexOf(tmpPumpe)+1;
		case 1:
			return String.format("%.2f", tmpPumpe.getDeltaL());
		case 2:
			return String.format("%.2f", tmpPumpe.getDeltaH());
		case 3:
			return String.format("%.2f", tmpPumpe.getPe());
		case 4:
			return String.format("%.5f", tmpPumpe.getLat());
		case 5:
			return String.format("%.5f", tmpPumpe.getLon());
		case 6:
			return String.format("%.2f", tmpPumpe.getGesLaenge());
		case 7:
			return String.format("%.2f", tmpPumpe.getHeight());
		default:
		return null;		
		}
	}

	@Override
	public String getColumnName( int columnIndex ){
		switch(columnIndex){
		case 0:
			return "Nr";
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
		default:
			return "";
		}
	}	
}
