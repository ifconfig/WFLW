
public class Pumpe extends Koordinate {
	private double pa;
	private double deltaL; 
	private double deltaH;
	private double pe;
	private double gesLaenge;
	private boolean fixed;
	
	public boolean isFixed() {
		return fixed;
	}

	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}

	public boolean toggleFixed(){
		fixed = !fixed;
		return fixed;
	}
	
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
		fixed = false;
	}
	
	// Pumpe mit Ausgangsdruck erzeugen und Koordinaten
	Pumpe(double newPa, Koordinate newKoord){
		super(newKoord);
		pa = newPa;
		deltaL = 0;
		deltaH = 0;
		pe = 0;
		gesLaenge = 0;
		fixed = false;
	}
	
	Pumpe(Koordinate newKoord){
		super(newKoord);
		pa = 8;
		deltaL = 0;
		deltaH = 0;
		pe = 0;
		gesLaenge = 0;
		fixed = false;
	} 
	
	public Pumpe(Koordinate koordinate, double aktDruck, double deltaL, double gesLaenge) {
		super(koordinate);
		this.pa = 8;
		this.deltaL = deltaL;
		this.deltaH = 0;
		this.pe = aktDruck;
		this.gesLaenge = gesLaenge;
		this.fixed = false;
	}

	String string(){
		return String.format("Pe:%.2fbar\tDeltaL:%.2fm\tDeltaH:%.2fm\tLat:%.5f\t Lon:%.5f\tH�he:%.2fm", pe, deltaL, deltaH, getLat(), getLon(), getHeight());
	}
}
