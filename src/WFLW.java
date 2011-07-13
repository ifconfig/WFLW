import java.util.ArrayList;
import java.util.List;





public class WFLW{
	private List<Koordinate> koordList;
	private Pumpen pumpen;
	
	/*
	 * Annahmen:	Pumpenausgansdruck 	pa	8bar
	 * 				Reibungsverlust		rv	2bar/100m
	 * 				Pumpeneingansdruck	pe	2bar
	 */
	private double pa = 8;
	private double pe = 2;
	private double rv = 2;	
	public List<Koordinate> getKoordList() {
		return koordList;
	}

	public void setKoordList(List<Koordinate> koordList) {
		this.koordList = koordList;
	}

	public Pumpen getPumpen() {
		return pumpen;
	}

	public void setPumpen(Pumpen pumpen) {
		this.pumpen = pumpen;
	}

	public double getPa() {
		return pa;
	}

	public void setPa(double pa) {
		this.pa = pa;
	}

	public double getPe() {
		return pe;
	}

	public void setPe(double pe) {
		this.pe = pe;
	}

	public double getRv() {
		return rv;
	}

	public void setRv(double rv) {
		this.rv = rv;
	}

	public boolean isGeladen() {
		return geladen;
	}

	public void setGeladen(boolean geladen) {
		this.geladen = geladen;
	}

	private boolean geladen;
	
	WFLW(){
		koordList = new ArrayList<Koordinate>();
		pumpen = new Pumpen();
		geladen = false;
	}
	
	public boolean open(String file){
		if(file.endsWith("gpx")) koordList = Open.openXML(file);
		else if(file.endsWith("txt")) koordList = Open.openNMEA(file);
		else return false;
		
		if(!koordList.isEmpty()) return true;
		
		else return false;
	}

	
	public int anzKoordinaten(){
		return koordList.size();
	}
	
	public double calcReibungsverlust(int durchflussmenge){
		switch(durchflussmenge){
		case 200:
			return 0.1;
		case 300:
			return 0.2;
		case 400:
			return 0.3;
		case 500:
			return 0.5;
		case 600:
			return 0.7;
		case 700:
			return 0.9;
		case 800:
			return 1.1;
		case 900:
			return 1.4;
		case 1000:
			return 1.7;
		case 1100:
			return 2.1;
		case 1200:
			return 2.5;
		case 1300:
			return 3.0;
		case 1400:
			return 3.5;
		case 1500:
			return 4.0;
		case 1600:
			return 4.5;
		case 1800:
			return 5.7;
		case 2000:
			return 7.0;
		case 2200:
			return 8.4;
		case 2400:
			return 10.0;
		default:
			return 0;				
		}
	}
	
	public boolean calcEnginePoints(){
		return calcEnginePoints(8.0, 1000, 2);
	}
	
	public boolean calcEnginePoints(double pa, int durchflussmenge, double pe){	
		/**
		 * Errechnet anhand der Wegpunkte die kenauen Punkenabsetzpunkte, mit den Standardvorgaben
		 */
		this.pa = pa;
		this.rv = calcReibungsverlust(durchflussmenge);
		this.pe = pe;
		if(koordList.isEmpty()) return false;
		pumpen.clear();
		double aktDruck=0;
		double tmpDruck = 0;
		double tmpDist = 0;
		double tmpHoehe = 0;
		double deltaL = 0;
		double gesLaenge = 0;
		
		// erste Pumpe wird automatisch gesetzt
		
		// Berechnung der Pumpenaufstellorte
		for(int i=0;i<koordList.size()-1;i++){
			// Entfernung zwischen den Koords
			tmpDist = NmeaFunc.distM(koordList.get(i), koordList.get(i+1));
			// Delta Höhe
			tmpHoehe = koordList.get(i+1).getHeight() - koordList.get(i).getHeight();
			// Druckabzug
			tmpDruck = ((tmpDist*rv/100) + (tmpHoehe/10));
			// Wenn aktueller Druck unter oder gleich pe dann Pumpe setzen mit erster Koordinate
			if(aktDruck-tmpDruck <= pe){
				pumpen.add(new Pumpe(koordList.get(i)));
				pumpen.get(pumpen.size()-1).setPe(aktDruck);
				pumpen.get(pumpen.size()-1).setDeltaL(deltaL);
				gesLaenge += deltaL;
				pumpen.get(pumpen.size()-1).setGesLaenge(gesLaenge);
				if(pumpen.size()>=2) pumpen.get(pumpen.size()-1).setDeltaH(pumpen.get(pumpen.size()-1).getHeight() - pumpen.get(pumpen.size()-2).getHeight());
				aktDruck = pa;
				deltaL = 0;
			}
			aktDruck -= tmpDruck;
			deltaL += tmpDist;
		}
		pumpen.add(new Pumpe(koordList.get(koordList.size()-1)));
		pumpen.get(pumpen.size()-1).setPe(aktDruck);
		pumpen.get(pumpen.size()-1).setDeltaL(deltaL);
		gesLaenge += deltaL;
		pumpen.get(pumpen.size()-1).setGesLaenge(gesLaenge);
		if(pumpen.size()>=2) pumpen.get(pumpen.size()-1).setDeltaH(pumpen.get(pumpen.size()-1).getHeight() - pumpen.get(pumpen.size()-2).getHeight());
		pumpen.fireTableDataChanged();
		return true;
	}
	
	public int anzGesetzterPumpen(){
		return pumpen.size();
	}
	
	
	public Koordinate getMitte(){
		if(koordList.isEmpty()) return new Koordinate(50.869166666667, 13.321944444444, 0);
		else{
			return new Koordinate(
						koordList.get((int)(koordList.size()/2)).getLat(),
						koordList.get((int)(koordList.size()/2)).getLon(),
						0);			
		}
	}
	
	public Boolean invertKoords(){
		List<Koordinate> newKoordList = new ArrayList<Koordinate>();
		if(koordList.isEmpty()) return false;
		for(Koordinate koord:koordList){
			newKoordList.add(0, koord);
		}
		koordList = newKoordList;
		return true;
	}
}
