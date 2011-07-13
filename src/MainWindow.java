import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.mapviewer.GeoPosition;


public class MainWindow  extends JFrame implements ActionListener, ChangeListener{
	/**
	 * Das Hauptfenster der Anwendung
	 */
	private static final long serialVersionUID = 1L;
	//verfügbare Items
	//Hauptfenster
	JFrame mainFrame;
	//Panels für Registerkarten
	JPanel panelVar1;
	JPanel panelVar2;
	JPanel panelVar1_topGrid;
	//Register
	JTabbedPane register;
	//Menü
	JMenuBar 	bar;
    JMenu 		menuDatei;
    JMenu 		menuOptions;
    JMenu		menuHelp;
    JMenuItem 	itemOeffnen;
    JMenuItem 	itemPrintKoords;
    JMenuItem 	itemExport2KML;
    JMenuItem	itemAbout;
    JMenuItem	itemInvertKoords;
    JCheckBoxMenuItem cbToggleDrawNames;
    //Toolbars und deren Buttons
    JToolBar	toolbar;
    JButton		buttonExport2KML;
    JButton		buttonInvertKoords;
    JButton		buttonOpen;
    JButton		buttonToggleDrawNames;
    final Icon 	iconExport2KML;
    final Icon 	iconInvertKoords;
    final Icon 	iconOpen;
    final Icon	iconToggleDrawNames_on;
    final Icon	iconToggleDrawNames_off;

    //Textfeld
    JTextArea 	textfeld;
    //Buttons außer Toolbarbuttons
    JButton buttonCalc;
    //Labels
    JLabel lblAusgangsdruck;
    JLabel lblDurchflussmenge;
    JLabel lblMindeseingangsdruck;
    //Comboboxen
    JComboBox cmbAusgangsdruck;
    JComboBox cmbDurchflussmenge;
    JComboBox cmbMindesteingangsdruck;
    //Hilfsobjekte
    WFLW wflw = null;
    //Scrollpanes
    JScrollPane scrollPaneTextfeld;
    JScrollPane scrollPaneTabelle;
    //Tabelle
    JTable pumpentabelle;
    //Splitter
    JSplitPane panelVar1SplitPane;
    //Kartenmaterial
    JXMapKit map;
    OwnWaypointPainter painter;
	//Bilder und Bildtracker
	Image img;
	MediaTracker imgTrack;
	//zusätzliche Dialoge
	JFileChooser fileDialog;
	AboutDialog aboutDialog;
    
    
	public MainWindow(WFLW wflw) {	
		//Look&Feel setzen
		try { 
			  UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() ); 
			} catch( Exception e ) { e.printStackTrace(); }
		//Hilfsobjekte
        this.wflw = wflw;
		//Hauptfenster
		mainFrame = new JFrame("Wasserförderung über lange Wege");
		mainFrame.setSize(800,600);
		//Panels für Registerkarten
		panelVar1 	= new JPanel(new BorderLayout());
        panelVar2	= new JPanel(new BorderLayout());
        panelVar1_topGrid = new JPanel(new GridLayout(0,2));
        //Register
        register = new JTabbedPane (JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT );
        register.addTab("Standorte", panelVar1);
        register.addTab("Karte", panelVar2);
        //Menü
        bar 			= new JMenuBar();
        menuDatei 		= new JMenu("Datei");
        menuOptions 	= new JMenu("Optionen");
        menuHelp		= new JMenu("Hilfe");
        itemOeffnen 	= new JMenuItem("NMEA-Log öffnen...");
        itemPrintKoords = new JMenuItem("Koordinaten ausgeben");
        itemExport2KML	= new JMenuItem("Ergebnis exportieren...");
        itemInvertKoords= new JMenuItem("Start/Ziel tauschen");
        itemAbout		= new JMenuItem("Über WFLW");
        itemPrintKoords.setEnabled(false);
        itemExport2KML.setEnabled(false);
        
        cbToggleDrawNames		= new JCheckBoxMenuItem("Pumpennamen anzeigen");
        cbToggleDrawNames.setSelected(true);
        itemInvertKoords.setEnabled(false);
        mainFrame.setJMenuBar(bar);
        menuDatei.add(itemOeffnen);
        menuDatei.add(itemExport2KML);
        menuDatei.add(itemPrintKoords);
        menuOptions.add(itemInvertKoords);
        menuOptions.addSeparator();
        menuOptions.add(cbToggleDrawNames);
        menuHelp.add(itemAbout);
        bar.add(menuDatei);
        bar.add(menuOptions);
        bar.add(menuHelp);
        //Toolsbars und deren Buttons
        toolbar				= new JToolBar();
        buttonExport2KML	= new JButton();
        buttonInvertKoords 	= new JButton();
        buttonOpen			= new JButton();
        buttonToggleDrawNames= new JButton();
        iconExport2KML	 	= new ImageIcon(this.getClass().getResource("export2KML.gif"));
        iconInvertKoords 	= new ImageIcon(this.getClass().getResource("invertKoords.gif"));
        iconOpen		 	= new ImageIcon(this.getClass().getResource("open.gif"));
        iconToggleDrawNames_on 	= new ImageIcon(this.getClass().getResource("toggleDrawName_on.gif"));
        iconToggleDrawNames_off	= new ImageIcon(this.getClass().getResource("toggleDrawName_off.gif"));
        toolbar.setFloatable(false);
        toolbar.setRollover(true);
        buttonExport2KML.setIcon(iconExport2KML);
        buttonExport2KML.setToolTipText("Ergebnis in KML exportieren");
        buttonExport2KML.setEnabled(false);
        buttonInvertKoords.setIcon(iconInvertKoords);
        buttonInvertKoords.setToolTipText("Start/Ziel tauschen");
        buttonInvertKoords.setEnabled(false);
        buttonOpen.setIcon(iconOpen);
        buttonOpen.setToolTipText("Streckendatei öffnen");
        buttonToggleDrawNames.setIcon(iconToggleDrawNames_on);
        buttonToggleDrawNames.setToolTipText("Pumpennamen anzeigen/verstecken");
        toolbar.add(buttonOpen);
        toolbar.add(buttonExport2KML);
        toolbar.addSeparator();
        toolbar.add(buttonInvertKoords);
        toolbar.addSeparator();
        toolbar.add(buttonToggleDrawNames);
        //Textfeld
        textfeld = new JTextArea(4,1);
        scrollPaneTextfeld = new JScrollPane(textfeld);
        scrollPaneTextfeld.setMinimumSize(new Dimension(20,80));
        textfeld.setEditable(false);
        //Buttons
        buttonCalc = new JButton("berechnen");
        buttonCalc.setEnabled(false);
        //Labels
        lblAusgangsdruck = new JLabel("Ausgangsdruck (bar)");
        lblDurchflussmenge = new JLabel("Durchflussmenge (l/min)");
        lblMindeseingangsdruck = new JLabel("Mindesteingangsdruck (bar)");        
        //Comboboxen
        String cmbAusgangsdruckListe[] = {"8", "10", "12"};
        cmbAusgangsdruck = new JComboBox(cmbAusgangsdruckListe);
        cmbAusgangsdruck.setEditable(true);
        String cmbDurchflussmengeListe[] = {"200","300","400","500","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1800","2000","2200","2400"};
        cmbDurchflussmenge = new JComboBox(cmbDurchflussmengeListe);
        cmbDurchflussmenge.setSelectedIndex(6);
        String cmbMindesteingangsdruckListe[] = {"1", "2", "3"};
        cmbMindesteingangsdruck = new JComboBox(cmbMindesteingangsdruckListe);
        cmbMindesteingangsdruck.setSelectedIndex(1);
        cmbMindesteingangsdruck.setEditable(true);
        
        //Tabellen
        pumpentabelle = new JTable(wflw.getPumpen());
        scrollPaneTabelle = new JScrollPane(pumpentabelle);
        
        //Splitter
        panelVar1SplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        panelVar1SplitPane.setTopComponent(scrollPaneTextfeld);
        panelVar1SplitPane.setBottomComponent(scrollPaneTabelle);
        
        //Kartenmaterial
        // Initialize a custom WaypointPainter which connects all the
		// way points using red lines.
        map = new JXMapKit();
        map.setDefaultProvider(org.jdesktop.swingx.JXMapKit.DefaultProviders.OpenStreetMaps);
        map.setZoom(3);
        painter = new OwnWaypointPainter(map);
        map.getMainMap().setOverlayPainter(painter);
               
		
        //Actionlistener;
        buttonCalc.addActionListener(this);
        buttonExport2KML.addActionListener(this);
        buttonInvertKoords.addActionListener(this);
        buttonOpen.addActionListener(this);
        buttonToggleDrawNames.addActionListener(this);
        cbToggleDrawNames.addActionListener(this);
        itemAbout.addActionListener(this);
        itemExport2KML.addActionListener(this);
        itemInvertKoords.addActionListener(this);
        itemOeffnen.addActionListener(this);
        itemPrintKoords.addActionListener(this);
        register.addChangeListener(this);
        
        //Shortcuts
        itemOeffnen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        cbToggleDrawNames.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
        itemExport2KML.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        itemPrintKoords.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        itemInvertKoords.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK));
        
        
        //zeigen
        mainFrame.add(toolbar, BorderLayout.PAGE_START);
        mainFrame.add(register, BorderLayout.CENTER);
        //Register 1
        panelVar1_topGrid.add(lblAusgangsdruck);
        panelVar1_topGrid.add(cmbAusgangsdruck);
        panelVar1_topGrid.add(lblDurchflussmenge);
        panelVar1_topGrid.add(cmbDurchflussmenge);
        panelVar1_topGrid.add(lblMindeseingangsdruck);
        panelVar1_topGrid.add(cmbMindesteingangsdruck);
        panelVar1.add(panelVar1_topGrid, BorderLayout.PAGE_START);
        panelVar1.add(panelVar1SplitPane,BorderLayout.CENTER);
        panelVar1.add(buttonCalc, BorderLayout.PAGE_END);
        //Register 2
        panelVar2.add(map);
       
        //zusätzliche Dialoge
        fileDialog = new JFileChooser();
        aboutDialog = new AboutDialog(this);
        
        mainFrame.setVisible(true);
	}

	
	//Steuerung der ActionEvents
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if		(arg0.getSource() == this.cbToggleDrawNames)	toogleDrawNames();
		else if	(arg0.getSource() == this.buttonCalc)			funcCalc();
		else if	(arg0.getSource() == this.buttonExport2KML)		funcSaveKML();
		else if	(arg0.getSource() == this.buttonInvertKoords) 	invertKoords();
		else if	(arg0.getSource() == this.buttonOpen) 			funcOeffnen();
		else if (arg0.getSource() == this.buttonToggleDrawNames)toogleDrawNames();
		else if	(arg0.getSource() == this.itemAbout)			aboutDialog.setVisible(true);
		else if	(arg0.getSource() == this.itemExport2KML)		funcSaveKML();
		else if	(arg0.getSource() == this.itemInvertKoords) 	invertKoords();
		else if (arg0.getSource() == this.itemOeffnen) 			funcOeffnen();
		else if	(arg0.getSource() == this.itemPrintKoords) 		funcPrintKoords();
	}
	
	private void toogleDrawNames() {
		 if(painter.toggleDrawNames()) buttonToggleDrawNames.setIcon(iconToggleDrawNames_on);
		 else buttonToggleDrawNames.setIcon(iconToggleDrawNames_off);
		 
		 map.repaint();
	}


	private void invertKoords() {
		wflw.invertKoords();
		textfeld.append("Strecke wurde umgedreht. Neuberechnung erforderlich!\n");
		if(register.getSelectedIndex()==1) funcCalc();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == this.register)funcDrawMap();
	}	

	private void funcOeffnen() {
		fileDialog.resetChoosableFileFilters();
		fileDialog.addChoosableFileFilter(new FileNameExtensionFilter("NMEA-Logs (*.txt)", "txt"));
		fileDialog.addChoosableFileFilter(new FileNameExtensionFilter("GPS Exchange Format (*.gpx)", "gpx"));
		fileDialog.addChoosableFileFilter(new FileNameExtensionFilter("GPS-Tracks (*.txt),(*.gpx)", "txt", "gpx"));
		int state = fileDialog.showOpenDialog(null);
		String file = null;
		if ( state == JFileChooser.APPROVE_OPTION )
	    {
			file = fileDialog.getSelectedFile().getPath();
	    }
		if(file != null){
			if(wflw.open(file)){
				textfeld.append("\"" + file + "\" erfolgreich geladen\n");
				textfeld.append(wflw.anzKoordinaten() + " Koordinaten-Datensätze\n");
				setAllEnabled();
				
			}
		}
		funcDrawMap();	
	}
	private void setAllEnabled() {
		buttonCalc.setEnabled(true);
		buttonExport2KML.setEnabled(true);
		buttonInvertKoords.setEnabled(true);
		itemPrintKoords.setEnabled(true);
		itemExport2KML.setEnabled(true);
		itemInvertKoords.setEnabled(true);
	}

	private void funcPrintKoords(){
		textfeld.setText("");
		for(Koordinate koord:wflw.getKoordList()){
			textfeld.append(koord.string());
			textfeld.append("\n");		
		}
	}
	private void funcSaveKML() {
		String filename = null;
		fileDialog.resetChoosableFileFilters();
		fileDialog.addChoosableFileFilter(new FileNameExtensionFilter("KML-GPS-Logs (*.kml)", "kml"));
		int state = fileDialog.showSaveDialog(null);
		if ( state == JFileChooser.APPROVE_OPTION ) filename = fileDialog.getSelectedFile().getPath();
		if (filename != null) Save.saveKML(filename, wflw.getPumpen(), wflw.getKoordList());		
	}
	private void funcCalc() {
		textfeld.setText("");
    	textfeld.append("Pumpenstandortberechnung\nAusgangsdruck: " + 
    			Double.parseDouble((String)cmbAusgangsdruck.getSelectedItem()) +
    			"bar\nFördermenge: " +
    			Integer.parseInt((String)cmbDurchflussmenge.getSelectedItem()) +
    			"l/min\nReibungsverlust: " +
    			wflw.calcReibungsverlust(Integer.parseInt((String)cmbDurchflussmenge.getSelectedItem())) + 
    			"bar/100m\n");
    	if(wflw.calcEnginePoints(	
    			Double.parseDouble((String)cmbAusgangsdruck.getSelectedItem()), 
    			Integer.parseInt((String)cmbDurchflussmenge.getSelectedItem()),
    			Double.parseDouble((String)cmbMindesteingangsdruck.getSelectedItem()))){
        	funcDrawMap();
    	}
    		
	}
	private void funcDrawMap() {
			Koordinate k = wflw.getMitte();
			painter.setWflw(wflw);
			map.setAddressLocation(new GeoPosition(k.getLat(), k.getLon()));
			map.repaint();
	}
}
