//Independent Project, Wesley Shiflet: code for OR GATE
import java.awt.*;

import javax.swing.JOptionPane;

public class OR extends OutPin{
	public OR(int xx, int yy){	
		super(xx, yy);
		ins=new InPin[0];
	}
	public void draw(Graphics g){
		g.setColor(is_on?Color.green:Color.red);
		g.fillRect(x,y,25,50);
		g.fillOval(x,y,50,50);
		g.setFont(new Font("Verdana", Font.BOLD, 20));
		g.setColor(Color.black);
		g.drawString("OR", x + 5, y + 30);
	}
	public String getName(){
		return ("OR Gate " + name);
	}
	public boolean check(){
		is_on=false;
		for (int i=0;i<ins.length;i++){
			if (ins[i].check()){
				is_on=true;
				continue;
			}
		}
		return (is_on);
	}
	public String posData(){
		return("\nOR,"+x+","+y);
	}
	public int height(){
		return 50;
	}
	public int linkLoad(){//for reattaching
		String[] options = new String[] {"2 inputs","3 inputs","4 inputs"};//OR
		return 2 + JOptionPane.showOptionDialog(null, "How many inputs would you like this gate to have this time?", "Adding Basic Logic Gate...",JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null, options, options[0]);
	}
}