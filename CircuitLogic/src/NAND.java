import java.awt.*;
import javax.swing.JOptionPane;
public class NAND extends OutPin{
	public NAND(int xx, int yy){	
		super(xx, yy);
		ins=new InPin[2];
		lines=new Line[2];
	}
	public void draw(Graphics g){
		g.setColor(is_on?green_color:red_color);
		g.fillRect(x,y,25,50);
		g.fillOval(x,y,50,50);
		g.setFont(new Font("Verdana", Font.BOLD, 15));
		g.setColor(Color.black);
		g.drawString("NAND", x + 1, y + 33);
	}
	public String posData(){
		return("\nNAND,"+x+","+y);
	}
	public boolean check(){//this is not true to how nand gates actually physically work, but it's so efficient
		boolean andy=(ins.length>1);
		for (int i=0;i<ins.length;i++)andy &= (ins[i].check());
		is_on=!andy;
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