import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;


public class startMenu extends JFrame implements ActionListener, KeyListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String[] TOOLS = {
	        "AND",
	        "OR",
	        "NAND",
	        "NOR",
	        "XOR",
	        "2",
	        "3",
	        "X",
	        "OUT",
	        "REM"
	    };
	private Board board;
	public ArrayList<String> bongo = new ArrayList<String>();
	public JTextArea text = new JTextArea(10, 50);
	public String[] visible_entries = new String[15]; 
	public int index;
	public int selection;
	public JButton AddButton = new JButton("New");
	public JButton EditButton = new JButton("Open");
	public JButton DeleteButton = new JButton("Delete");//this will now truly delete the file
	public JButton ClearButton = new JButton("Restore");
	public JButton FreshButton = new JButton("Refresh");
	public String header = "     Available Circuit Boards: ";
	public JList<String> slits;
	public JPanel pane;
	public JLabel label = new JLabel(header);
	public startMenu(){
		setSize(400,400);
		setTitle("Circuit Logic Simulator: Select a Board!");
		setBackground(Color.white);
		label.setFont(new Font("Helvetica", Font.BOLD, 12));
		index = 0;
		AddButton.addActionListener(this);
		AddButton.setActionCommand("N");
		AddButton.setMnemonic(KeyEvent.VK_N);
		EditButton.addActionListener(this);
		EditButton.setActionCommand("O");
		EditButton.setMnemonic(KeyEvent.VK_O);
		DeleteButton.addActionListener(this);
		DeleteButton.setActionCommand("D");
		DeleteButton.setMnemonic(KeyEvent.VK_D);
		ClearButton.addActionListener(this);
		ClearButton.setActionCommand("R");
		ClearButton.setMnemonic(KeyEvent.VK_R);
		FreshButton.addActionListener(this);
		FreshButton.setActionCommand("F");
		FreshButton.setMnemonic(KeyEvent.VK_F);
		//SaveButton was dummied out in favor of allowing saves from within the Circuit Board
		try {
			load(new Scanner(new FileReader("data")));
		} catch (FileNotFoundException e) {
			visible_entries[0] = "";
		}
		fresh();
	}
	public void scroll(boolean dir){
		if (dir){
			index++;
		}
		else{
			index--;
		}
		refresh();
	}
	public JList<String> listMaker(){
		if (bongo.size() < 15){
			for(int j = 0;j<bongo.size();j++){
				visible_entries[j] = (bongo.get(j));
			}
		}
		else if ((bongo.size() - index) < 15){
			for(int j = index;j<bongo.size();j++){
				visible_entries[j-index] = (bongo.get(j));
			}
		}
		else{
			for(int j = 0;j<15;j++){
				visible_entries[j] = (bongo.get(j+index));
			}
		}
		JList<String> bro = new JList<String>(visible_entries);
	    bro.setFont(new Font("Courier New", Font.PLAIN, 12));
	    bro.setFixedCellHeight(20);
	    bro.setFixedCellWidth(350);
	    bro.addKeyListener(this);
		return(bro);
	}
	public void refresh(){ // Updates and replaces the JList. Oddly enough, resizing the window is necessary to make the changes display in a reasonable amount of time.
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 0;
        c.weighty = 0;
		c.gridy = 1;
        c.gridheight = 2;
        c.gridwidth = 6;
        pane.remove(slits);
        try {
			load(new Scanner(new FileReader("data")));
		} catch (FileNotFoundException e) {	}
        slits = listMaker();
        pane.add(slits,c);
        getContentPane().removeAll();
		add(pane);
		setSize(401,400);//for some reason, resizing fixes a glitch
		setSize(400,400);
	}
	public void fresh(){
		pane= new JPanel(new GridBagLayout());
		slits = listMaker();
		GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 6;
        pane.add(label,c);
        c.gridy = 1;
        c.gridheight = 2;
        pane.add(slits,c);
        c.gridy = 3;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = .5; 
        c.fill = GridBagConstraints.CENTER;
        pane.add(AddButton,c);
        c.gridx = 1;
        pane.add(EditButton,c);
        c.gridx ++;
        pane.add(DeleteButton,c);
        c.gridx ++;
        pane.add(ClearButton,c);
        c.gridx ++;
        pane.add(FreshButton,c);
        selection = slits.getSelectedIndex()+index;
		//getContentPane().removeAll();
		add(pane);
	}
	public void addOne(){
		edit("");
	}
	public void edit(){
		if (bongo.isEmpty()){
			addOne();
		}
		else{
			if (selection > (bongo.size()-1)) selection = (bongo.size()-1);//this was left over from my old Jeep project.  I forget how it became necessary
			slits.setSelectedIndex(selection-index);
			edit(bongo.get(selection));
		}
	}
	public void edit(String edittee){
      	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		JFrame frame = new JFrame( "Circuit Board" );
		JMenuBar toolbar = new JMenuBar();
		int w = 820;//(int)(screenSize.getWidth() * 55)/100;
		int h = (int)(screenSize.getHeight() * 80)/100;  
		board = new Board(edittee,frame,w,h);
		frame.add(board);
		JMenu addMenu = new JMenu("Add...");
		JMenuItem andItem = new JMenuItem("AND Gate");
		andItem.setActionCommand("AND");
		andItem.addActionListener(this);
		addMenu.add(andItem);
		JMenuItem orItem = new JMenuItem("OR Gate");
		orItem.setActionCommand("OR");
		orItem.addActionListener(this);
		addMenu.add(orItem);
		JMenuItem nandItem = new JMenuItem("NAND Gate");//this will now truly delete the file
		nandItem.setActionCommand("NAND");
		nandItem.addActionListener(this);
		addMenu.add(nandItem);
		JMenuItem norItem = new JMenuItem("NOR Gate");
		norItem.setActionCommand("NOR");
		norItem.addActionListener(this);
		addMenu.add(norItem);
		JMenuItem xorItem = new JMenuItem("XOR Gate");
		xorItem.setActionCommand("XOR");
		xorItem.addActionListener(this);
		addMenu.add(xorItem);
		JMenuItem mult2Item = new JMenuItem("2-to-1 Multiplexer");
		mult2Item.setActionCommand("2");
		mult2Item.addActionListener(this);
		addMenu.add(mult2Item);
		JMenuItem mult3Item = new JMenuItem("3-to-1 Multiplexer");
		mult3Item.setActionCommand("3");
		mult3Item.addActionListener(this);
		addMenu.add(mult3Item);
		JMenuItem notItem = new JMenuItem("Inverter (NOT Gate)");
		notItem.setActionCommand("X");
		notItem.addActionListener(this);
		toolbar.add(addMenu);
		JMenu editMenu = new JMenu("Edit");
		JMenuItem attachItem = new JMenuItem("Connect output pin (RHS)");
		attachItem.setActionCommand("OUT");
		attachItem.addActionListener(this);
		editMenu.add(attachItem);
		JMenuItem removeItem = new JMenuItem("Remove gate");
		removeItem.setActionCommand("REM");
		removeItem.addActionListener(this);
		editMenu.add(removeItem);
		toolbar.add(editMenu);
		toolbar.add(Box.createHorizontalGlue());
		JMenuItem saveItem = new JMenuItem("Save");
		saveItem.setMaximumSize(saveItem.getPreferredSize() );
		toolbar.add(saveItem);
		frame.add(toolbar,BorderLayout.NORTH);
        frame.setPreferredSize(new Dimension(w, h+toolbar.getHeight()));
        frame.setSize(w, h+toolbar.getHeight());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);;
		frame.setVisible( true ); // display frame
	}
	public void remove() throws IOException{
		if (bongo.isEmpty()){
			return;
		}
		int n = JOptionPane.showConfirmDialog(
			    this,
			    "Are you sure you want to delete this board?\nThis cannot be undone.",
			    "Confirm delete...",
			    JOptionPane.YES_NO_OPTION);
		if (n==JOptionPane.NO_OPTION)return;
		String path=(bongo.get(selection));
		bongo.remove(selection);
		File leaving=new File("boards/"+path);
		leaving.delete();
		for (int i = slits.getSelectedIndex(); i<14;i++){
			visible_entries[i] = visible_entries[i+1];
		}
		if (bongo.size() > index + 14){
			visible_entries[14] = bongo.get(index + 14);
		}else{
			visible_entries[14] = "";
		}
		File inputFile = new File("data");
		File tempFile = new File("temp");
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
		String currentLine;
		while((currentLine = reader.readLine()) != null) {
		    // trim newline when comparing with lineToDelete
		    String trimmedLine = currentLine.trim();
		    if(trimmedLine.equals(path)) continue;
		    writer.write(currentLine + System.getProperty("line.separator"));
		}
		writer.close(); 
		reader.close();
		inputFile.delete();
		tempFile.renameTo(inputFile);//returns boolean of success
		if (selection>=bongo.size()) selection--;
		refresh();
	}public void clear() throws IOException{
		int n = JOptionPane.showConfirmDialog(
			    this,
			    "This will delete all circuit boards and restore the default set.\n\nContinue?",
			    "Restore mode",
			    JOptionPane.YES_NO_OPTION);
		if (n==JOptionPane.NO_OPTION)return;
		while (!bongo.isEmpty()){
			String path=bongo.remove(0);
			File leaving=new File("boards/"+path);
			leaving.delete();
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("data")));
		writer.write("Half Adder\nFull Adder\n2-Bit Ripple Carry Adder\nDemultiplexer\n4-to-2 Priority Encoder\n8-to-3 Digital Encoder\nIC 74147\n");
		writer.close();
		writer = new BufferedWriter(new FileWriter(new File("boards/Half Adder")));
		writer.write("i,5,Pin A\no,5,SUM\ni,6,Pin B\no,6,CARRY\np\nXOR,413,184\nAND,651,280\ng\n0,10,213,10,255\n1,10,213,10,255\ns\no\n0\n1\n2\n3\n4\n5,413,184\n6,651,280\n7\n8\n9\n10\n11\n12\n13\n14\n15\n\n");
		writer.close();
		writer = new BufferedWriter(new FileWriter(new File("boards/Full Adder")));
		writer.write("i,1,Pin A\no,3,  SUM   (Bit 0)\ni,4,Pin B\no,6,C-OUT  (Bit 1)\ni,7,C-IN\np\nXOR,196,42\nAND,176,171\nXOR,387,57\nAND,330,233\nOR,575,201\ng\n0,10,45,10,171\n1,10,45,10,171\n2,196,42,10,297\n3,196,42,10,297\n4,176,171,330,233\ns\no\n0\n1\n2\n3,387,57\n4\n5\n6,575,201\n7\n8\n9\n10\n11\n12\n13\n14\n15\n\n");
		writer.close();
		writer = new BufferedWriter(new FileWriter(new File("boards/2-Bit Ripple Carry Adder")));
		writer.write("i,1,A0\no,2,Q0\ni,3,B0\ni,7,A1\no,8,Q1\ni,9,B1\no,13,C-OUT\np\nXOR,611,52\nAND,144,68\nXOR,130,294\nAND,130,365\nXOR,475,227\nAND,364,306\nOR,544,327\ng\n0,10,45,10,129\n1,10,45,10,129\n2,10,297,10,381\n3,10,297,10,381\n4,144,68,130,294\n5,144,68,130,294\n6,364,306,130,365\ns\no\n0\n1\n2,611,52\n3\n4\n5\n6\n7\n8,475,227\n9\n10\n11\n12\n13,544,327\n14\n15\n\n");
		writer.close();
		writer = new BufferedWriter(new FileWriter(new File("boards/Demultiplexer")));
		writer.write("i,1,S0\ni,3,S1\no,3,O0\ni,6,DATA\no,6,O1\no,9,O2\no,12,O3\np\nNOT,436,37\nNOT,439,120\nAND,761,116\nAND,763,234\nAND,571,357\nAND,333,477\ng\n0,10,45\n1,10,129\n2,436,37,439,120,10,255\n3,10,45,439,120,10,255\n4,436,37,10,129,10,255\n5,10,45,10,129,10,255\ns\no\n0\n1\n2\n3,761,116\n4\n5\n6,763,234\n7\n8\n9,571,357\n10\n11\n12,333,477\n13\n14\n15\n\n");
		writer.close();
		writer = new BufferedWriter(new FileWriter(new File("boards/4-to-2 Priority Encoder")));
		writer.write("i,1,D0\no,1,Active\ni,3,D1\no,5,A0\ni,6,D2\no,7,A1\ni,8,D3\np\nNOT,135,178\nOR,807,32\nOR,806,277\nAND,385,139\nOR,910,191\ng\n0,10,255\n1,10,45,10,129,10,255,10,339\n2,10,255,10,339\n3,135,178,10,129\n4,385,139,10,339\ns\no\n0\n1,807,32\n2\n3\n4\n5,910,191\n6\n7,806,277\n8\n9\n10\n11\n12\n13\n14\n15\n\n");
		writer.close();
		writer = new BufferedWriter(new FileWriter(new File("boards/8-to-3 Digital Encoder")));
		writer.write("i,0,D0\ni,2,D1\no,3,Q0\ni,4,D2\ni,6,D3\no,6,Q1\ni,8,D4\ni,10,D5\no,10,Q2\ni,12,D6\ni,14,D7\np\nOR,831,111\nOR,668,247\nOR,821,399\ng\n0,10,87,10,255,10,423,10,591\n1,10,171,10,255,10,507,10,591\n2,10,339,10,423,10,507,10,591\ns\no\n0\n1\n2\n3,831,111\n4\n5\n6,668,247\n7\n8\n9\n10,821,399\n11\n12\n13\n14\n15\n\n");
		writer.close();
		writer = new BufferedWriter(new FileWriter(new File("boards/IC 74147")));
		writer.write("i,0,NC(#15)\ni,1,I1(#11)\ni,2,I2(#12)\no,2,A (#9)\ni,3,I3(#13)\ni,4,I4 (#1)\ni,5,I5 (#2)\ni,6,I6 (#3)\ni,7,I7 (#4)\no,7,B (#7)\ni,8,I8 (#5)\ni,9,I9(#10)\no,10,C (#6)\no,13,D(#14)\np\nNOT,80,40\nNOT,81,82\nNOT,82,122\nNOT,82,165\nNOT,82,207\nNOT,82,251\nNOT,82,291\nNOT,81,337\nNOT,81,375\nNOT,183,81\nNOT,278,164\nNOT,278,206\nNOT,228,246\nNOR,165,351\nAND,349,406\nAND,238,328\nAND,381,283\nAND,287,266\nNOR,599,391\nAND,602,313\nAND,531,228\nNOR,731,334\nAND,325,513\nAND,469,109\nAND,575,169\nAND,582,79\nNOR,980,69\nOR,734,446\ng\n0,10,45\n1,10,87\n2,10,129\n3,10,171\n4,10,213\n5,10,255\n6,10,297\n7,10,339\n8,10,381\n9,81,82\n10,82,165\n11,82,207\n12,82,251\n13,81,337,81,375\n14,165,351,82,291\n15,82,251,165,351\n16,82,207,165,351\n17,82,165,165,351\n18,287,266,381,283,238,328,349,406\n19,82,122,278,164,278,206,165,351\n20,81,82,278,164,278,206,165,351\n21,531,228,602,313,238,328,349,406\n22,228,246,165,351,82,207\n23,278,164,228,246,165,351\n24,469,109,82,122\n25,80,40,183,81,469,109\n26,582,79,575,169,734,446,325,513\n27,81,375,349,406\ns\no\n0\n1\n2,980,69\n3\n4\n5\n6\n7,731,334\n8\n9\n10,599,391\n11\n12\n13,165,351\n14\n15\n\n");
		writer.close();
	}
	public void load(Scanner in){
		bongo.clear();
		while (in.hasNextLine()){
			//String sting = in.nextLine();
			bongo.add(in.nextLine());
		}in.close();
	}
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		selection = slits.getSelectedIndex()+index;
		for (int i=0;i<10;i++) {
		    if (TOOLS[i].equals(e.getActionCommand())) {
		        board.pushIt(i);
				board.repaint();
		        return;
		    }
		}
		switch(e.getActionCommand()){
			case "N": 	addOne();
						break;
			case "O":	edit();
						break;
			case "D":	try {remove();}
						catch (IOException e1){e1.printStackTrace();}
						break;
			case "R":	try{
							clear();
						}
						catch (IOException e1){
							e1.printStackTrace();
						}
			case "AND":	try{
					board.pushIt(0);
				}
				catch (Exception e1){
					e1.printStackTrace();
				}
			default:	refresh();
		}
	}
	@Override
	public void keyTyped(java.awt.event.KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(java.awt.event.KeyEvent e) { //scroll methods work, just not very well- for some reason, the JList is unselected every time I scroll
		// TODO Auto-generated method stub
		selection = slits.getSelectedIndex()+index;
		int keyCode = e.getKeyCode();
	    switch( keyCode ) { 
	        case KeyEvent.VK_UP:
	            if ((slits.getSelectedIndex() == 0) && (index > 0)){
	            	scroll(false);
	            	slits.setSelectedIndex(0);
	    			selection = slits.getSelectedIndex()+index;
	            }break;
	        case KeyEvent.VK_DOWN:
	        	if ((slits.getSelectedIndex() == 14) && (selection < bongo.size()-1)){
	            	scroll(true);
	            	slits.setSelectedIndex(14);
	    			selection = slits.getSelectedIndex()+index;
	            }break;
	    }
	}
	@Override
	public void keyReleased(java.awt.event.KeyEvent e) {
		// TODO Auto-generated method stub
	}
}	
		
//}	