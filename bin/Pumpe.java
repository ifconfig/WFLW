
public class Pumpe extends Koordinate {
	private double pa;
	private double deltaL; 
	private double deltaH;
	private double pe;
	private double gesLaenge;
	
	public double getPa() {
		return pa;
	}

	public void setPa(double pa) {
		this.pa = pa;
	}

	public double getDeltaL() {
		return deltaL;
	}

	public void setDeltaL(double deltaL) {
		this.deltaL = deltaL;
	}

	public double getDeltaH() {
		return deltaH;
	}

	public void setDeltaH(double deltaH) {
		this.deltaH = deltaH;
	}

	public double getPe() {
		return pe;
	}

	public void setPe(double pe) {
		this.pe = pe;
	}

	public double getGesLaenge() {
		return gesLaenge;
	}

	public void setGesLaenge(double gesLaenge) {
		this.gesLaenge = gesLaenge;
	}

	Pumpe(){
		super();
		pa = 8;
		deltaL = 0;
		deltaH = 0;
		pe = 0;
		gesLaenge = 0;
	}
	
	// Pumpe mit Ausgangsdruck erzeugen
	Pumpe(double newPa){
		super();
		pa = newPa;
		deltaL = 0;
		deltaH = 0;
		pe  = 0;
		gesLaenge = 0;
	}
	
	// Pumpe mit Ausgangsdruck erzeugen und Koordinaten
	Pumpe(double newPa, Koordinate newKoord){
		super(newKoord);
		pa = newPa;
		deltaL = 0;
		deltaH = 0;
		pe = 0;
		gesLaenge = 0;
	}
	
	Pumpe(Koordinate newKoord){
		super(newKoord);
		pa = 8;
		deltaL = 0;
		deltaH = 0;
		pe = 0;
		gesLaenge = 0;
	} 
	
	String string(){
		return String.format("Pe:%.2fbar\tDeltaL:%.2fm\tDeltaH:%.2fm\tLat:%.5f\t Lon:%.5f\tHöhe:%.2fm", pe, deltaL, deltaH, getLat(), getLon(), getHeight());
	}
}
