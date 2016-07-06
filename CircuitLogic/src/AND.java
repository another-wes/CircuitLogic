import java.awt.*;

import javax.swing.JOptionPane;
public class AND extends OutPin{
	public AND(int xx, int yy){	
		super(xx, yy);
		ins=new InPin[0];
	}
	public void draw(Graphics g){
		g.setColor(is_on?Color.green:Color.red);
		g.fillRect(x,y,25,50);
		g.fillOval(x,y,50,50);
		g.setFont(new Font("Verdana", Font.BOLD, 20));
		g.setColor(Color.black);
		g.drawString("AND", x + 2, y + 32);
	}
	public String getName(){
		return ("AND Gate " + name);
	}
	public String posData(){
		return("\nAND,"+x+","+y);
	}
	public boolean check(){
		is_on=(ins.length>1);
		for (int i=0;i<ins.length;i++)is_on &= (ins[i].check());
		return (is_on);
	}
	public int height(){
		return 50;
	}
	public int linkLoad(){//for reattaching
		String[] options = new String[] {"2 inputs","3 inputs","4 inputs"};//OR
		return 2 + JOptionPane.showOptionDialog(null, "How many inputs would you like this gate to have this time?", "Adding Basic Logic Gate...",JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null, options, options[0]);
	}
}