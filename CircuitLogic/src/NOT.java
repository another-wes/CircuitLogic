import java.awt.*;

public class NOT extends OutPin{
	private int[] xpts, ypts;
	public NOT(int xx, int yy){	
		super(xx, yy);
		ins = new InPin[1];
		xpts= new int[]{xx,xx,xx+35};
		ypts=new int[]{yy,yy+30,yy+15};
		}
	public void attach_in(InPin one){
		ins[0] = one;
	}	
	public void draw(Graphics g){
		g.setColor(is_on?Color.green:Color.red);
		g.fillPolygon(xpts,ypts,3);
		g.setFont(new Font("Verdana", Font.PLAIN, 10));
		g.setColor(Color.black);
		g.drawString("NOT", x + 1, y + 18);
	}
	public boolean hitTest(int mx, int my){
		return ((x <= mx && mx <= x + width()) && (y <= my && my <= y+height()));// || (mx > x + 40) && (my <= (.5 * x)) && (my >= ));
	}
	public String posData(){
		return("\nNOT,"+x+","+y);
	}
	public boolean check(){
		if (ins[0]==null)is_on=true;
		else is_on = !ins[0].check();
		return (is_on);
	}
	public int height(){
		return 31;
	}
	public int width(){
		return 36;
	}
}