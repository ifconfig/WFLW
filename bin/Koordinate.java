
public class Koordinate {
	private double lon;
	private double lat;
	private double height;
	
	
	
	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	Koordinate(){
		lon = lat = height = 0;
	}
	
	Koordinate(double newLat, double newLon, double newHeight){
		lon 	= newLon;
		lat 	= newLat;
		height 	= newHeight;
	}
	
	Koordinate(Koordinate newKoord){
		setLat(newKoord.getLat());
		setLon(newKoord.getLon());
		setHeight(newKoord.getHeight());
	}
	
	String string(){
		return String.format("Latitude: %.5f\tLongitude: %.5f\tHöhe: %.2f", lat, lon, height);
	}
}
