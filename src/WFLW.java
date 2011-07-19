import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;


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
		 * Errechnet anhand der Wegpunkte die genauen Punkenabsetzpunkte, mit den Standardvorgaben
		 * Es ist dafür vorbereitet, fest gesetzte Pumpen zu berücksichtigen. Das ist erst dann sinvoll,
		 * wenn der Pumpenstandort geändert werden kann
		 */
		if(koordList.isEmpty()) return false;
		this.pa = pa;
		this.rv = calcReibungsverlust(durchflussmenge);
		this.pe = pe;
		double aktDruck=0;
		double tmpDruck = 0;
		double tmpDist = 0;
		double tmpHoehe = 0;
		double deltaL = 0;
		double gesLaenge = 0;
		
		Pumpen fixedPumpen;
		if(pumpen.size()>=2) fixedPumpen = pumpen.getFixed();
		else fixedPumpen = new Pumpen();
		pumpen.clear();
		Pumpe nextFixedPumpe;
		if(!fixedPumpen.isEmpty()){
			nextFixedPumpe = fixedPumpen.get(0);
			fixedPumpen.removePumpe(0);
		}
		else nextFixedPumpe = null;
		for(int i=0;i<koordList.size()-1;i++){
			// Entfernung zwischen den Koords
			tmpDist = NmeaFunc.distM(koordList.get(i), koordList.get(i+1));
			// Delta Höhe
			tmpHoehe = koordList.get(i+1).getHeight() - koordList.get(i).getHeight();
			// Druckabzug
			tmpDruck = ((tmpDist*rv/100) + (tmpHoehe/10));
			if(pumpeIsOnKoord(koordList.get(i),nextFixedPumpe)){
				nextFixedPumpe.setPe(aktDruck);
				nextFixedPumpe.setDeltaL(deltaL);
				nextFixedPumpe.setGesLaenge(gesLaenge);
				deltaL = 0;
				aktDruck = nextFixedPumpe.getPa();
				if(pumpen.size()>=2) nextFixedPumpe.setDeltaH(nextFixedPumpe.getHeight()-pumpen.get(pumpen.size()-1).getHeight());
				pumpen.add(nextFixedPumpe);
				// neue nächste fixe Pumpe
				if(!fixedPumpen.isEmpty()){
					nextFixedPumpe = fixedPumpen.get(0);
					fixedPumpen.removePumpe(0);
				}else nextFixedPumpe = null;
			}else{
				if(aktDruck-tmpDruck <= pe){
					pumpen.add(new Pumpe(koordList.get(i),aktDruck,deltaL,gesLaenge));
					if(pumpen.size()>=2) pumpen.get(pumpen.size()-1).setDeltaH(pumpen.get(pumpen.size()-1).getHeight() - pumpen.get(pumpen.size()-2).getHeight());
					aktDruck = pa;
					deltaL = 0;
				}		
			}
			aktDruck -= tmpDruck;
			deltaL += tmpDist;	
			gesLaenge += tmpDist;
		}
		if(pumpeIsOnKoord(koordList.get(koordList.size()-1),nextFixedPumpe)){
			nextFixedPumpe.setPe(aktDruck);
			nextFixedPumpe.setDeltaL(deltaL);
			nextFixedPumpe.setGesLaenge(gesLaenge);
			if(pumpen.size()>=2) nextFixedPumpe.setDeltaH(nextFixedPumpe.getHeight()-pumpen.get(pumpen.size()-1).getHeight());
			pumpen.add(nextFixedPumpe);
		}else{
			pumpen.add(new Pumpe(koordList.get(koordList.size()-1),aktDruck,deltaL,gesLaenge));
			if(pumpen.size()>=2) pumpen.get(pumpen.size()-1).setDeltaH(pumpen.get(pumpen.size()-1).getHeight() - pumpen.get(pumpen.size()-2).getHeight());
		}
		pumpen.fireTableDataChanged();
		return true;
	}
	
	public boolean calcEnginePoints(int durchflussmenge, double pe, Pumpen verfPumpen){
		double aktDruck = 0;
		double druckVerl;
		double tmpDist = 0;
		double tmpHoehe = 0;
		double gesLaenge = 0;
		int indexNextVerfPumpe = 0;
		this.rv = calcReibungsverlust(durchflussmenge);
		this.pe = pe;
		
		//Wenn Koordliste oder verfPumpenListe leer, dann hats sich eh erledigt
		if(koordList.isEmpty() || verfPumpen.isEmpty()){
			JOptionPane.showMessageDialog(null, "Die Liste der verfügbaren Pumpen oder Streckenliste ist leer.\nBerechnung wird abgebrochen.", "Berechnungsfehler", JOptionPane.ERROR_MESSAGE); 
			return false;	
		}
		//Reibungskoeffizient holen;
				
		//Pumpenliste leeren
		this.pumpen.clear();
		
		//restliche Pumpen setzen;
		for(int i=0;i<this.koordList.size()-1;i++){
			//sind noch verfügbare Pumpen vorhanden?
			if(indexNextVerfPumpe != verfPumpen.size()){
				//Berechne Druckverlust im kommenden abschnitt
				// Entfernung zwischen den Koords
				tmpDist = NmeaFunc.distM(koordList.get(i), koordList.get(i+1));
				// Delta Höhe
				tmpHoehe = koordList.get(i+1).getHeight() - koordList.get(i).getHeight();
				// Druckabzug
				druckVerl = ((tmpDist*rv/100) + (tmpHoehe/10));
				
				//muss Pumpe gesetzt werden?
				if(aktDruck-druckVerl <= pe){
					//setze die aktuellen Werte der Pumpe
					if(!pumpen.isEmpty()){
						verfPumpen.get(indexNextVerfPumpe).setPe(aktDruck);
						verfPumpen.get(indexNextVerfPumpe).setDeltaH(koordList.get(i).getHeight()-pumpen.getLast().getHeight());
						verfPumpen.get(indexNextVerfPumpe).setDeltaL(gesLaenge-pumpen.getLast().getGesLaenge());
						verfPumpen.get(indexNextVerfPumpe).setGesLaenge(gesLaenge);
						verfPumpen.get(indexNextVerfPumpe).setKoord(koordList.get(i));
					}else{
						verfPumpen.get(indexNextVerfPumpe).setKoord(koordList.get(i));
					}
					//nimm nächste Pumpe aus verf. Pumpen
					pumpen.add(verfPumpen.get(indexNextVerfPumpe));
					indexNextVerfPumpe++;
					//vor dem zurücksetzen noch schnell die gesamtlänge aufaddieren
					gesLaenge += tmpDist;
					//setze aktDruck nach pa der gesetzten Pumpe
					aktDruck = pumpen.getLast().getPa();
				} else{
					//Mache den nächsten Schritt und aktualisiere die Abschnitsvariablen und den aktDruck
					gesLaenge += tmpDist;
				}
				aktDruck -= druckVerl;
			}else{
				JOptionPane.showMessageDialog(null, "Keine Pumpe mehr verfügbar.\nBerechnung wird abgebrochen.", "Berechnungsfehler", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		//sind noch verfügbare Pumpen vorhanden?
		if(indexNextVerfPumpe != verfPumpen.size()){
			//setze die aktuellen Werte der Pumpe
			verfPumpen.get(indexNextVerfPumpe).setPe(aktDruck);
			verfPumpen.get(indexNextVerfPumpe).setKoord(koordList.get(koordList.size()-1));
			verfPumpen.get(indexNextVerfPumpe).setDeltaH(koordList.get(koordList.size()-1).getHeight()-pumpen.getLast().getHeight());
			verfPumpen.get(indexNextVerfPumpe).setDeltaL(gesLaenge-pumpen.getLast().getGesLaenge());
			verfPumpen.get(indexNextVerfPumpe).setGesLaenge(gesLaenge);
			//setze E-stellenpumpe;
			pumpen.add(verfPumpen.get(indexNextVerfPumpe));
			indexNextVerfPumpe++;
			pumpen.fireTableDataChanged();
			return true;
		}else{	
			JOptionPane.showMessageDialog(null, "Keine Pumpe für die Einsatzstelle verfügbar.\nBerechnung wird abgebrochen.", "Berechnungsfehler", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	private boolean pumpeIsOnKoord(Koordinate koordinate, Pumpe nextFixedPumpe) {
		if(nextFixedPumpe != null){
			return(koordinate.getLat()==nextFixedPumpe.getLat() && koordinate.getLon()==nextFixedPumpe.getLon());
		}
		return false;
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
