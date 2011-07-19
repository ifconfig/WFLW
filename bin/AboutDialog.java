import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


class AboutDialog extends JDialog implements ActionListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Calendar myCal2 = new GregorianCalendar();
    private int jahr = myCal2.get( Calendar.YEAR  );
    private String year = Integer.toString(jahr);
    private String produkt = "Wasserförderung über lange Wegstrecke";
    private String version = "1.1";
    private String copyright = "GNU GPL v3 "+year+" Georg Heyne";
    private String beschreibung1 = "In Gedenken an den KatSchutz-Zug";
    private String beschreibung2 = "\"Löschen/Wasserversorgung Brand-Erbisdorf\"";

   public AboutDialog(JFrame f) {
      super(f);
      this.setTitle("Info über WFLW");
      this.setResizable(false);
      this.setSize(new Dimension(370, 210));
      this.setModal(true);
      this.getContentPane().setLayout(null);

      JLabel lbSymbol = new JLabel();

      JLabel lbProdukt = new JLabel(produkt);
      lbProdukt.setBounds(new Rectangle(96, 22, 200, 23));

      JLabel lbVersion = new JLabel(version);
      lbVersion.setBounds(new Rectangle(96, 45, 17, 16));

      JLabel lbCopyright = new JLabel(copyright);
      lbCopyright.setBounds(new Rectangle(96, 61, 200, 16));

      JLabel lbBeschreibung1 = new JLabel(beschreibung1);
      lbBeschreibung1.setFont(new java.awt.Font("Dialog", 2, 12));
      lbBeschreibung1.setBounds(new Rectangle(96, 95, 252, 29));
      JLabel lbBeschreibung2 = new JLabel(beschreibung2);
      lbBeschreibung2.setFont(new java.awt.Font("Dialog", 2, 12));
      lbBeschreibung2.setBounds(new Rectangle(96, 105, 252, 39));

      JButton btnOK = new JButton();
      btnOK.setBounds(new Rectangle(140, 150, 90, 22));
      btnOK.setText("OK");
      btnOK.addActionListener(this);

      this.getContentPane().add(lbProdukt, null);
      this.getContentPane().add(lbSymbol, null);
      this.getContentPane().add(lbVersion, null);
      this.getContentPane().add(lbCopyright, null);
      this.getContentPane().add(lbBeschreibung1, null);
      this.getContentPane().add(lbBeschreibung2, null);
      this.getContentPane().add(btnOK, null);
   }

   public void actionPerformed(ActionEvent e) {
      setVisible(false);
   }
}

