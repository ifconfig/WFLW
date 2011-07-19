import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;


public class PumpenTable extends JTable implements MouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPopupMenu popupMenu = new JPopupMenu();
	public PumpenTable(Pumpen pumpen) {
		super(pumpen);
		initPopup();
	}
	
	private void initPopup() {
		JMenuItem editItem = new JMenuItem("Pumpe setzen/entfernen");
		popupMenu.add(editItem);
		editItem.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        }
		});
	}
	
	public void mouseClicked(MouseEvent e){
	}

		
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
}


