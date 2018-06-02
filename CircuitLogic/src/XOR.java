import java.awt.*;

public class XOR extends OutPin{
	public XOR(int xx, int yy){	
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
		g.drawString("XOR", x + 1, y + 32);
	}
	public boolean check(){
		is_on = false;
		for (int i=0; i<ins.length; i++) {
			if (ins[i].check())is_on = !is_on;
		}
		return (is_on);
	}
	public String posData(){
		return("\nXOR,"+x+","+y);
	}
	public int height(){
		return 50;
	}
}