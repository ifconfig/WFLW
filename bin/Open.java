import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;


public class Open {
	public static List<Koordinate> openXML(String file){
		List<Koordinate> koord = new ArrayList<Koordinate>();
		try {
			// zuerst eine neue XMLInputFactory erstellen
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			// einen neuen eventReader einrichten
			InputStream in = new FileInputStream(file);
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			// das XML-Dokument lesen
			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					// If we have a item element we create a new item
					if (startElement.getName().getLocalPart() == ("trkpt")) {
						koord.add(new Koordinate());
						// We read the attributes from this tag and add the date
						// attribute to our object
						Iterator<Attribute> attributes = startElement.getAttributes();
						while (attributes.hasNext()) {
							Attribute attribute = attributes.next();
							if (attribute.getName().toString().equals("lat")) {
								koord.get(koord.size()-1).setLat(Double.parseDouble(attribute.getValue()));
								continue;
							}
							if (attribute.getName().toString().equals("lon")) {
								koord.get(koord.size()-1).setLon(Double.parseDouble(attribute.getValue()));
								continue;
							}
						}
					}

					if (event.isStartElement()) {
						if (event.asStartElement().getName().getLocalPart().equals("ele")) {
							event = eventReader.nextEvent();
							koord.get(koord.size()-1).setHeight(Double.parseDouble(event.asCharacters().getData()));
							continue;
						}
					}					
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		return koord;
	}
	
	
	public static List<Koordinate> openNMEA(String file) {
		List<Koordinate> koord = new ArrayList<Koordinate>();
		List<String> nmeaStringList = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String zeile = null;
			while ((zeile = in.readLine()) != null) {
				nmeaStringList.add(zeile);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (String item: nmeaStringList) {
				if(item.startsWith("$GPGGA")) {
					koord.add(NmeaFunc.gpggaToKoord(item));
			   }			   
		}
		return koord;
	}
}
