import java.awt.*;

import javax.swing.JOptionPane;

public class NOR extends OutPin{
	public boolean is_on;
	public NOR(int xx, int yy){	
		super(xx, yy);
		ins=new InPin[2];
		lines=new Line[2];
	}
	public void draw(Graphics g){
		g.setColor(is_on?green_color:red_color);
		g.fillRect(x,y,25,50);
		g.fillOval(x,y,50,50);
		g.setFont(new Font("Verdana", Font.BOLD, 18));
		g.setColor(Color.black);
		g.drawString("NOR", x + 1, y + 32);
	}
	public boolean check(){
		is_on=true;
		for (int i=0;i<ins.length;i++){
			if (ins[i].check()){
				is_on=false;
				continue;
			}
		}
		return (is_on);
	}
	public String posData(){
		return("\nNOR,"+x+","+y);
	}
	public int height(){
		return 50;
	}
	public int linkLoad(){//for reattaching
		String[] options = new String[] {"2 inputs","3 inputs","4 inputs"};//OR
		return 2 + JOptionPane.showOptionDialog(null, "How many inputs would you like this gate to have this time?", "Adding Basic Logic Gate...",JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null, options, options[0]);
	}
}