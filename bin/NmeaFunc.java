import java.util.List;


public class NmeaFunc {
	static double distKM(Koordinate koordA, Koordinate koordB){
		// Gibt die Distanz ohne Beachtung der Höhe zurück (in km)
		double dx, dy, lat; 
		lat = (koordA.getLat() + koordB.getLat()) / 2 * 0.01745;
		dx = 111.3 * Math.cos(lat) * (koordA.getLon() - koordB.getLon());
		dy = 111.3 * (koordA.getLat() - koordB.getLat());
		return Math.sqrt(dx * dx + dy * dy);
	}
	
	static double distM(Koordinate koordA, Koordinate koordB){
		// Gibt die Distanz ohne Beachtung der Höhe zurück (in m)
		double dx, dy, lat;
		lat = (koordA.getLat() + koordB.getLat()) / 2 * 0.01745;
		dx = 111.3 * Math.cos(lat) * (koordA.getLon() - koordB.getLon());
		dy = 111.3 * (koordA.getLat() - koordB.getLat());
		return Math.sqrt(dx * dx + dy * dy)*1000;
	}	
	
	static Koordinate gpggaToKoord(String gpggaString){
		Koordinate tmpKoord = new Koordinate();
		String tmpString[];
		double tmpDouble;
		
		tmpString = gpggaString.split(",");
		// Pos. 2 Latitude - Breitengrad
		tmpDouble  = Double.parseDouble(tmpString[2]);
		tmpKoord.setLat((int)tmpDouble/100);
		tmpKoord.setLat(tmpKoord.getLat()+(tmpDouble-(tmpKoord.getLat()*100))/60);
				
		// Pos. 4 Longitude - Längengrad
		tmpDouble  = Double.parseDouble(tmpString[4]);
		tmpKoord.setLon((int)tmpDouble/100);
		tmpKoord.setLon(tmpKoord.getLon()+(tmpDouble-(tmpKoord.getLon()*100))/60);
		// Pos. 9 Höhe über Grund in Metern
		tmpKoord.setHeight(Double.parseDouble(tmpString[9]));
		return tmpKoord;
	}
	static double getWholeDist(List<Koordinate> koordList){
		double dist = 0;
		for(int i=0;i<koordList.size()-1;i++){
			dist += distM(koordList.get(i), koordList.get(i+1));
		}
		return dist;
	}
}
