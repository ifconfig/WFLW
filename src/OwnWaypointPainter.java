import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.Point2D;

import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.mapviewer.GeoPosition;


@SuppressWarnings("rawtypes")
public class OwnWaypointPainter implements org.jdesktop.swingx.painter.Painter {
	private JXMapKit parentKit;
	private Image imgPumpe;
	private Boolean bDrawNames;
	
	private WFLW wflw;

	
	
	public WFLW getWflw() {
		return wflw;
	}



	public Boolean getbDrawNames() {
		return bDrawNames;
	}



	public void setbDrawNames(Boolean bDrawNames) {
		this.bDrawNames = bDrawNames;
	}

	public boolean toggleDrawNames(){
		this.bDrawNames = !this.bDrawNames;
		return this.bDrawNames;
	}

	public void setWflw(WFLW wflw) {
		this.wflw = wflw;
	}



	public OwnWaypointPainter(JXMapKit parentKit) {
		this.parentKit = parentKit;
		wflw = null;
		bDrawNames = true;
		loadImage();
	}

	protected void loadImage(){
		imgPumpe = Toolkit.getDefaultToolkit().getImage(getClass().getResource("Pumpe_Pfeil_32_blue.png"));
	}


	@Override
	public void paint(Graphics2D arg0, Object arg1, int arg2, int arg3) {
		Point2D aktKoordPt = null;
		
		if(wflw != null){
			// Strecke einzeichnen
			if(!wflw.getKoordList().isEmpty()){
				arg0.setColor(Color.red);
				Koordinate prevKoord = wflw.getKoordList().get(0);
				Point2D prevKoordPt = parentKit.getMainMap().convertGeoPositionToPoint(new GeoPosition(prevKoord.getLat(),prevKoord.getLon()));
				for(int i=1;i<wflw.getKoordList().size();i++){
					aktKoordPt = parentKit.getMainMap().convertGeoPositionToPoint(new GeoPosition(	wflw.getKoordList().get(i).getLat(),
																									wflw.getKoordList().get(i).getLon()));
					arg0.drawLine((int)prevKoordPt.getX(), (int)prevKoordPt.getY(), (int)aktKoordPt.getX(), (int)aktKoordPt.getY());
					prevKoordPt = aktKoordPt;
				}
			}
			// Pumpen einzeichnen
			if(!wflw.getKoordList().isEmpty()){
				arg0.setColor(Color.black);
				//Wasserentnahme einzeichnen
				aktKoordPt = 	parentKit.
								getMainMap().
								convertGeoPositionToPoint(
										new GeoPosition(
												wflw.getPumpen().get(0).getLat(),
												wflw.getPumpen().get(0).getLon()));
				arg0.drawImage(imgPumpe, (int)aktKoordPt.getX()-16, (int)aktKoordPt.getY()-46,parentKit);
				if(bDrawNames) arg0.drawString("Wasserentnahme", (int)aktKoordPt.getX()-40, (int)aktKoordPt.getY()-50);
				//VKS einzeichen
				for(int i=1;i<wflw.getPumpen().size()-1;i++){
					aktKoordPt = 	parentKit.
					getMainMap().
					convertGeoPositionToPoint(
							new GeoPosition(
									wflw.getPumpen().get(i).getLat(),
									wflw.getPumpen().get(i).getLon()));
					arg0.drawImage(imgPumpe, (int)aktKoordPt.getX()-16, (int)aktKoordPt.getY()-46,parentKit);
					if(bDrawNames) arg0.drawString(String.format("VKS%d", i), (int)aktKoordPt.getX()-10, (int)aktKoordPt.getY()-50);
				}
				//E-Stellenpumpe einzeichnen
				aktKoordPt = 	parentKit.
				getMainMap().
				convertGeoPositionToPoint(
						new GeoPosition(
								wflw.getPumpen().get(wflw.getPumpen().size()-1).getLat(),
								wflw.getPumpen().get(wflw.getPumpen().size()-1).getLon()));
				arg0.drawImage(imgPumpe, (int)aktKoordPt.getX()-16, (int)aktKoordPt.getY()-46,parentKit);
				if(bDrawNames) arg0.drawString("Einsatzstelle", (int)aktKoordPt.getX()-35, (int)aktKoordPt.getY()-50);
			}						
		}
	}
	
}
