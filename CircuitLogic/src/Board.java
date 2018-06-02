import java.util.*;
//import java.util.concurrent.Semaphore;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.*;

public class Board extends JPanel  
	implements MouseListener, MouseMotionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<InPin> inputs;  
	public ArrayList<OutPin> gates; //should probably switch to protected
	private ArrayList<OutPin> switchables;
	private ArrayList<OutPin> outputs;
	private ArrayList<Line> paths;
//	private boolean placing=false; //Replaces with integer check
	private Image backBuffer;
	private Graphics gBackBuffer;
//	private final Semaphore available = new Semaphore(0, true);//for placing objects with mouse I guess
	private int ogap,vgap,mode,to_place=0;
	public JFrame frame;
	private boolean initialized, needs_check = false;
	private InPin[] selections;//clicked inputs
	private OutPin selection=null;//clicked outpin
	public Board(String filename, JFrame application,int w, int h)
    {
		frame=application;
    	initialized=false;
    	inputs = new ArrayList<InPin>();
		gates = new ArrayList<OutPin>();
		switchables =  new ArrayList<OutPin>();
		outputs = new ArrayList<OutPin>();
		paths = new ArrayList<Line>();
		ogap = w - 50;
		vgap = ((h-30)/16); // used for input and output placement. 30 is the combined height of the upper and lower margins and bottom pin.
		for (int i=0; i<16; i++) {
			inputs.add(new InPin(10,3 + (vgap* i))); //calculated gap
			outputs.add(new OutPin(ogap,3+(vgap* i)));	
		}
        // handle mouse and mouse motion events
        if (filename!=""){
			try {
				load(new Scanner(new FileReader("boards/"+filename)));
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(frame, "Directory error?  File /boards/"+filename+"not found!");
			} catch (IndexOutOfBoundsException e){
				e.printStackTrace();
				JOptionPane.showMessageDialog(frame, "The selected file was improperly formatted!");
			} catch (NullPointerException e){
				e.printStackTrace();
				JOptionPane.showMessageDialog(frame, "The selected file was improperly formatted!");
			}
    	}
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }
	void init()//initial state
    {
		for (int i=0; i<16; i++) outputs.get(i).check();
		// create the back buffer
		backBuffer = createImage(getSize().width, getSize().height);
		gBackBuffer = backBuffer.getGraphics();
    }
	public String dataForm(){//
		StringBuilder all_data=new StringBuilder();
		for (int i=0; i<16; i++) {//saving names
			if (inputs.get(i).getName()!="")all_data.append("i,"+i+","+inputs.get(i).getName()+"\n");
			if (outputs.get(i).getName()!="")all_data.append("o,"+i+","+outputs.get(i).getName()+"\n");
		}
		all_data.append("p");
		for (int i=0; i<gates.size(); i++)all_data.append(gates.get(i).posData());
		for (int i=0; i<switchables.size(); i++) all_data.append(switchables.get(i).posData());
		all_data.append("\ng");//has to reiterate through these lists twice, so when the data is processed, it doesn't try to attach gates that haven't been placed yet
		for (int i=0; i<gates.size(); i++)all_data.append("\n"+i+gates.get(i).linkData());
		all_data.append("\ns");
		for (int i=0; i<switchables.size(); i++) all_data.append("\n"+i+switchables.get(i).linkData());
		all_data.append("\no");
		for (int i=0; i<16; i++)all_data.append("\n"+i+outputs.get(i).linkData());
		return all_data.toString()+"\n";
	}
	public void regenerate(String[] split){
		switch(split[0]){
			case "AND":	gates.add(new AND(Integer.parseInt(split[1]),Integer.parseInt(split[2])));
						break;
			case "OR":	gates.add(new OR(Integer.parseInt(split[1]),Integer.parseInt(split[2])));
						break;
			case "XOR":	gates.add(new XOR(Integer.parseInt(split[1]),Integer.parseInt(split[2])));
						break;				
			case "NOR":	gates.add(new NOR(Integer.parseInt(split[1]),Integer.parseInt(split[2])));
						break;				
			case "NOT":	gates.add(new NOT(Integer.parseInt(split[1]),Integer.parseInt(split[2])));
						break;
			case "NAND":gates.add(new NAND(Integer.parseInt(split[1]),Integer.parseInt(split[2])));
						break;
			case "2":	switchables.add(new DUPLEX(Integer.parseInt(split[1]),Integer.parseInt(split[2])));
						break;
			case "3":	switchables.add(new TRIPLEX(Integer.parseInt(split[1]),Integer.parseInt(split[2])));
						break;
		}
	}
	public void stringToWires(String[] split,ArrayList<OutPin> list){
		if (split.length>2){
			if (split.length>4){
				if (split.length>6){
					if (split.length>8)	list.get(Integer.parseInt(split[0])).attach_in(Finder(Integer.parseInt(split[1]),Integer.parseInt(split[2])),Finder(Integer.parseInt(split[3]),Integer.parseInt(split[4])),Finder(Integer.parseInt(split[5]),Integer.parseInt(split[6])),Finder(Integer.parseInt(split[7]),Integer.parseInt(split[8])));
					else list.get(Integer.parseInt(split[0])).attach_in(Finder(Integer.parseInt(split[1]),Integer.parseInt(split[2])),Finder(Integer.parseInt(split[3]),Integer.parseInt(split[4])),Finder(Integer.parseInt(split[5]),Integer.parseInt(split[6])));
				}
				else list.get(Integer.parseInt(split[0])).attach_in(Finder(Integer.parseInt(split[1]),Integer.parseInt(split[2])),Finder(Integer.parseInt(split[3]),Integer.parseInt(split[4])));
			}
			else list.get(Integer.parseInt(split[0])).attach_in(Finder(Integer.parseInt(split[1]),Integer.parseInt(split[2])));
		}
	}
	public void load(Scanner in) throws IndexOutOfBoundsException,NullPointerException{
		if (!in.hasNext())return;
		String line=in.nextLine();
		while (line.charAt(0)!='p'){//no idea why comparing to strings didn't work but w/e
			String[] split = line.split(",");
			if (split[0].charAt(0)=='i')inputs.get(Integer.parseInt(split[1])).setName(split[2]);
			else outputs.get(Integer.parseInt(split[1])).setName(split[2]);
			line=in.nextLine();
		}//line is now "p"
		line = in.nextLine();
		while (line.charAt(0)!='g'){//PLACING GATES AND MULTIPLEXERS
			regenerate(line.split(","));
			line=in.nextLine();
		}//line is now "g"
		line = in.nextLine();
		while (line.charAt(0)!='s'){
			stringToWires(line.split(","),gates);//ATTACHING GATES
			line = in.nextLine();
		}
		while (line.charAt(0)!='o'){
			stringToWires(line.split(","),switchables);//ATTACHING MULTIPLEXERS
			line = in.nextLine();
		}
		while (in.hasNext()){//ATTACHING PINS
			stringToWires(line.split(","),outputs);
			line=in.nextLine();
		}
		for (int o=0; o<16; o++) outputs.get(o).traceback(paths);
	}
	public void save() throws IOException{ //NEEDS REWORKING
		String s = (String)JOptionPane.showInputDialog(frame,"What is the name of this board?");
		if (s==null)return;
		PrintWriter writer;
		writer = new PrintWriter("boards/"+s);
		writer.println(dataForm());
		writer.close();
		Scanner check = new Scanner(new FileReader("data"));
		while (check.hasNext()){
			String line = check.nextLine();
			if (line.compareTo(s)==0){
				check.close();
				return;
			}
		}
		check.close();
		BufferedWriter bw = new BufferedWriter(new FileWriter("data", true));
		bw.write(s);//appends name of board to list of boards
		bw.newLine();
	    bw.flush();
	    bw.close();
		}
	public void startPlacing(int i){ //gonna dummy out some semaphores
		return;
	}
	public void finishPlacing(int i){ //gonna dummy out some semaphores
		return;
	}
	public void paintComponent( Graphics g )
    {
        super.paintComponent(g); // clears drawing area
    	if (!initialized) {
    		initialized=true;
    		init();
    	}
    	//gBackBuffer.setFont(BOLD);
		gBackBuffer.setColor(Color.white);
		gBackBuffer.clearRect(0, 0, frame.getWidth(), frame.getHeight());
		for (int i=0; i<16; i++) {
			inputs.get(i).draw(gBackBuffer);
		}
		for (int i=0; i<switchables.size(); i++) {
			switchables.get(i).draw(gBackBuffer);
			//switchables.get(i).traceback(gBackBuffer,paths));
		}
		for (int i=0; i<gates.size(); i++) {
			gates.get(i).draw(gBackBuffer);
			//gates.get(i).traceback(gBackBuffer,paths);
		}
		for (int i=0; i<switchables.size(); i++) {
			switchables.get(i).draw(gBackBuffer);
			//switchables.get(i).traceback(gBackBuffer,paths);
		}
		for (int i=0; i<16; i++) {
			outputs.get(i).draw(gBackBuffer);
			//outputs.get(i).traceback(gBackBuffer,paths);
		}
		for (int i=0; i<paths.size(); i++) {
			paths.get(i).draw(gBackBuffer);
		}
		g.drawImage(backBuffer, 0, 0, null);
    } // end method paintComponent
	public InPin Finder(int x, int y){//determine the clicked input
    	if (x<60)return inputs.get(Math.min(((Math.max(0,y-3))/vgap),15));
//		for (int i=0; i<16; i++) {
//			InPin p=inputs.get(i);
//			if (inputs.get(i).hitTest(x,y)) return p { //this means you should probably put Multiplexers in inputs
//				return p;
//			}
//		}
    	for (int i=0; i<gates.size(); i++) {
			InPin p=gates.get(i);
			if (p.hitTest(x,y)) { //this means you should probably put Multiplexers in inputs
				return p;
			}
		}
    	for (int i=0; i<switchables.size(); i++) {
			InPin p=switchables.get(i);
			if (p.hitTest(x,y)) { //this means you should probably put Multiplexers in inputs
				return p;
			}
		}
    	return null; //If no output-ready component underlies the clicked point, this return will cause the process to restart
    }
	public OutPin outFinder(int x, int y){//determine the clicked input
    	if (x>=ogap){
			OutPin s=outputs.get(Math.min(((Math.max(0,y-3))/vgap),15));
			to_place=2;
			return s;
		}
		else for (int i=0; i<gates.size(); i++){ 
			OutPin s = gates.get(i);
			if (s.hitTest(x,y)){
				to_place=1+s.linkLoad();
				return s;
			}
    	}
		for (int i=0; i<switchables.size(); i++) if (switchables.get(i).hitTest(x,y)){
			OutPin s = gates.get(i);
			if (s.hitTest(x,y)){
				to_place=1+s.linkLoad();
				return s;
			}
    	}
		return null; //If no component underlies the clicked point, this return will cause the process to restart
    }
	public OutPin finDelete(int x, int y){//determine the clicked input
    	for (int i=0; i<gates.size(); i++){
    		OutPin r=gates.get(i);
    		if (r.hitTest(x,y)){
    			r.detach();
    			r.detach_in(paths);
    			return gates.remove(i);
    		}
    	}	
    	for (int i=0; i<switchables.size(); i++) {
    		OutPin r=switchables.get(i);
    		if (r.hitTest(x,y)) {
    			r.detach();
    			r.detach_in(paths);
    			return switchables.remove(i);
    		}
    	}
    	return null; //If no output-ready component underlies the clicked point, this return will cause the process to restart
    }
	public void dropIt(int x, int y){//just 0-7
    	OutPin pin_to_drop;
		switch(mode){
			case 0:	pin_to_drop=new AND(x,y);
					break;
			case 1:	pin_to_drop = new OR(x,y);
					break;
			case 2:	pin_to_drop = new NAND(x,y);
					break;
			case 3:	pin_to_drop = new NOR(x,y);
					break;		
    		case 4: pin_to_drop = new XOR(x,y);
        			break;
    		case 5: pin_to_drop = new DUPLEX(x,y);
    				break;
    		case 6:	pin_to_drop = new TRIPLEX(x,y);//TRIPLEX
    				break;
    		default:pin_to_drop = new NOT(x,y);//NOT
					break;
    	}
		switch(selections.length){
    			case 1: pin_to_drop.attach_in(selections[0]);
    					break;
    			case 2: pin_to_drop.attach_in(selections[0],selections[1]);
    					break;
    			case 3: pin_to_drop.attach_in(selections[0],selections[1],selections[2]);
    					break;
    			case 4: pin_to_drop.attach_in(selections[0],selections[1],selections[2],selections[3]);
    					break;
    	}
		pin_to_drop.traceback(paths);
		needs_check = true;
		pin_to_drop.check();//just making sure it's the right color!
    	if ((mode==5)||(mode==6))switchables.add(pin_to_drop);
    	else gates.add(pin_to_drop);
	}
	public void pushIt(int i){
		if (i==11)return;
		mode=i;
		if (i<5){
			String[] options = new String[] {"Two","Three","Four"};//XOR
			to_place = 3 + JOptionPane.showOptionDialog(null, "How many inputs would you like this gate to have?\n\nWhen choosing inputs, select from top to bottom for a clear wire layout.", "Adding Basic Logic Gate...",
	        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
	        null, options, options[0]);
		}
		else switch(i){
    		case 5: JOptionPane.showMessageDialog(frame, "Select 2 inputs to multiplex.");
    				to_place=3; //DUPLEX
    				break;
    		case 6: JOptionPane.showMessageDialog(frame, "Select 3 inputs to multiplex.");
    				to_place=4;//TRIPLEX
    				break;
    		case 7:	JOptionPane.showMessageDialog(frame, "Select the gate or pin to be inverted.");
    				to_place=2;
    				break;//NOT
    		case 8: JOptionPane.showMessageDialog(frame, "Select your output first.");
    				to_place=2;//LINK_IN
    				break;
    		case 9:JOptionPane.showMessageDialog(frame, "Select a gate to remove.\nThis feature is currently in beta and may display minor bugs.\nTo cancel, click anywhere on the background.");
    				to_place=1;//REMOVE
    				break;
    		case 10: try {save();} 
    				catch (IOException e) {JOptionPane.showMessageDialog(frame,"Error saving(bad filename?); try again");}
    				return; //prevents NegativeArraySizeException
    	}
		selections=new InPin[to_place-1];
	}
	public void checkCollisions(int i)
	{
		for (int j = 0; j < paths.size(); j++) {
			if (j!=i) paths.get(i).consolidateWith(paths.get(j));
		}
	}
	public void checkCollisions()
	{
		boolean go_further = false; //will turn true if any collisions needed to be fixed
		do {
			go_further = false;
			for (int i = 0; i < paths.size(); i++) {
				for (int j = i+1; j < paths.size(); j++) {
					go_further |= paths.get(i).consolidateWith(paths.get(j));
				}
			}
		} while (go_further); //will not exit until all collisions resolved
		
	}
	public void showDialog(){//happens for cases 0-6
		String[] msg = {"AND gate","OR gate","NAND gate","NOR gate","exclusive OR (modulo-2) gate","multiplexer","multiplexer","inverter"};
		JOptionPane.showMessageDialog(frame, "Place your new "+msg[mode]);//AND
	}
    public void mouseClicked( MouseEvent e ){//System.out.println(e.getX()+","+e.getY()); for coordinate testing
    }
    public void mousePressed( MouseEvent e )
    {
    	if (e.isMetaDown()) {
    		if (e.getX()<=70){
    			for (int i=0; i<16; i++) {
        			InPin p=inputs.get(i);
        			if (p.hitTest(e.getX(), e.getY())) { //this means you should probably put Multiplexers in inputs
        				p.setName((String)JOptionPane.showInputDialog(frame,"What would you like to rename input pin "+i+"?"));
        				repaint();
        				return;
        			}
        		}
    		}
    		else if (e.getX()>=ogap){
    			for (int i=0; i<16; i++) {
        			OutPin p=outputs.get(i);
        			if (p.hitTest(e.getX(), e.getY())) { //this means you should probably put Multiplexers in inputs
        				p.setName((String)JOptionPane.showInputDialog(frame,"What would you like to rename input pin "+i+"?"));
        				repaint();
        				return;
        			}
        		}
    		}
    		return;	// ignores right click otherwise
    	}
    	if (to_place==1){
    		if (mode==9){
    			finDelete(e.getX(),e.getY());
    			to_place=0;
    		}
    		else{
    			dropIt(e.getX(),e.getY());
    			to_place=0;
    		}
    	}	
    	else if (to_place>1){
    		if (mode==8){
    			if (selection==null){
    				selection=outFinder(e.getX(),e.getY());
    				selections = new InPin[to_place-1];
    			}
    			else{
    				InPin next = Finder(e.getX(),e.getY());
        			if (next!=null)selections[selections.length-(--to_place)]=next;
        			if (to_place==1){
        				if (selections.length==1)selection.attach_in(selections[0]);
            			else if (selections.length==2)selection.attach_in(selections[0],selections[1]);
            			else if (selections.length==3)selection.attach_in(selections[0],selections[1],selections[2]);
                		else selection.attach_in(selections[0],selections[1],selections[2],selections[3]);
            			selection.traceback(paths);
        				selection=null;
            			to_place=0;
        			}
    			}
    		}
    		else{
    			InPin next = Finder(e.getX(),e.getY());
    			if (next!=null){
        			selections[selections.length-(--to_place)]=next;
        			if (to_place==1)showDialog();
        		}
    		}
    		
    	}
		// check 'em
    	else if (e.getY()<getHeight()){
    		for (int i=0; i<16; i++) {
    			InPin p=inputs.get(i);
    			if (p.hitTest(e.getX(), e.getY())) { //this means you should probably put Multiplexers in inputs
    				p.toggle();
    				repaint();
    				return;
    			}
    		}
    		for (int i=0; i<switchables.size(); i++) {
    			InPin p=switchables.get(i);
    			if (p.hitTest(e.getX(), e.getY())) { //this means you should probably put Multiplexers in inputs
    				p.toggle();
    				repaint();
    				return;
    			}
    		}
    	};
    		
    }
    
    public void mouseReleased( MouseEvent e )
    {
    	// runs a check from the end, backwards
    	for (int i=0; i<16; i++)outputs.get(i).check();
    	for (int i=0; i<switchables.size(); i++)switchables.get(i).check();
    	for (int i=0; i<gates.size(); i++) gates.get(i).check();
		for (int i=0; i<16; i++) outputs.get(i).check();
		if (needs_check) checkCollisions();
		needs_check = false;
		repaint();//originally was in loops
    }

    public void mouseEntered( MouseEvent e )
    {
    }

    public void mouseExited( MouseEvent e )
    {
    }

    public void mouseMoved( MouseEvent e )
    {
    }
    
    public void mouseDragged( MouseEvent e )
    {
	}
}
