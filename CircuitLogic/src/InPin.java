import java.awt.*;
import java.util.ArrayList;

public class InPin{
	protected int x,y;
	protected boolean is_on;
	public boolean rotated=false; //not really used anymore
	//public boolean flipped=false; could reimplement in later version
	protected String name="";
	protected ArrayList<OutPin> outs=new ArrayList<OutPin>();//used almost exclusively for the detach operation
	public InPin(int xx, int yy){
		x = xx;
		y = yy;
		is_on = false;
	}
	public InPin(InPin src) {	// copy constructor
		x = src.x;
		y = src.y;
		is_on = src.is_on;
	}
	public void draw(Graphics g){//no longer accounts for rotation;
		g.setColor(is_on?Color.green:Color.red);
		g.fillRect(10,y,40,21);
		g.fillPolygon(new int[]{50,60,50},new int[]{y,y+10,y+20},3);
		if (name != null){
			g.setFont(new Font("Verdana", Font.BOLD, 10));
			g.setColor(Color.black);
			g.drawString(name, 14, y + 14);
		}
	}		
	public boolean hitTest(int mx, int my){//for simplicity's sake, this function checks a rectangular hitbox equal to the dimensions of the gate.
		return ((x <= mx && mx <= x+width()) && (y <= my && my <= y+height()));
	}
	public void attach_out(OutPin o){
		outs.add(o);
	}
	public boolean check(){
		return (is_on);
	}
	public boolean check(OutPin out){
		return check();
	}
	public int find(OutPin out){
		return 0;
	}
	public void toggle(){
		is_on = !is_on;
	}	
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int height(){
		//return (rotated?50:21);
		return 21;
	}
	public int width(){
		return 50;
	}
	public String getName(){
		return name;
	}
	public void setX(int newx){
		x = newx;
	}
	public void setY(int newy){
		y = newy;
	}
	public void setName(String ding){
		if (ding==null) return;
		name = ding;
	}
	public String saveData(){
		return ("\n"+x+","+y);
	}
//	public void rotate(){ maybe one day
//		if (!flipped){
//			if (!rotated)rotated=true;
//			else{
//				flipped=true;
//				rotated=false;
//			}
//		}else{
//			if (rotated)flipped=rotated=false;
//			else rotated=true;
//		}
//	}
	public void detach(){//detaches all
		for (int i=0;i<outs.size();i++){
			outs.get(i).detach_in(this);
		}
		outs=new ArrayList<OutPin>();
	}
	public void detach(OutPin that){//second step method
		outs.remove(that);
		}
}