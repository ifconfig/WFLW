import java.io.*;
import java.util.List;
import java.util.Locale;


public class Save {
	public static void saveKML(String filename, Pumpen pumpen, List<Koordinate> koords){
		if(filename != null){
			File filehandler = new File(filename);
			try {
				filehandler.createNewFile();
				FileWriter fw = new FileWriter(filehandler); 
				BufferedWriter bw = new BufferedWriter(fw); 
				bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				bw.write("<kml xmlns=\"http://www.opengis.net/kml/2.2\">");
				bw.write("<Document>");
				for(int i=0; i<pumpen.size();i++){
					bw.write("<Placemark>");
				    	bw.write("<name>Pumpe"+(i+1)+"</name>");
				    	if(i==0) bw.write("<description>Wasserentnahmestelle</description>");
				    	else if(i==pumpen.size()-1) bw.write("<description>Einsatzstellenpumpe</description>");
				    	else bw.write("<description>VKS "+i+"</description>");
				    	bw.write("<Point>");
				    		bw.write(String.format(Locale.US,"<coordinates>%.5f,%.5f,%.2f</coordinates>", pumpen.get(i).getLat(), pumpen.get(i).getLon(), pumpen.get(i).getHeight()));
				    	bw.write("</Point>");
					bw.write("</Placemark>");
				}
				//Strecke hinzufügen
				bw.write("<Placemark><name>Leitungsstrecke</name><styleUrl>#style_track</styleUrl><LineString><altitudeMode /><coordinates>");
				for(Koordinate item:koords){
					bw.write(String.format(Locale.US,"%.5f,%.5f,%.2f ", item.getLat(), item.getLon(), item.getHeight()));
				}
				bw.write("</coordinates></LineString></Placemark>");
				bw.write("</Document>");
				bw.write("</kml>");
				 bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
//			<?xml version="1.0" encoding="UTF-8"?>
//			<kml xmlns="http://www.opengis.net/kml/2.2">
//			<Document>
//			  <Placemark>
//			    <name>Zürich</name>
//			    <description>Zürich</description>
//			    <Point>
//			      <coordinates>8.55,47.3666667,0</coordinates>
//			    </Point>
//			  </Placemark>
//			</Document>
//			</kml>

		}
	}
}
