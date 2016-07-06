import java.awt.*;
public class DUPLEX extends OutPin{
	public int switchy;
	public DUPLEX(int xx, int yy){	
		super(xx, yy);
		ins = new InPin[2];
		switchy = 0;
	}
	public void attach_in(InPin one, InPin two){
		ins[0] = one;
		ins[1] = two;
	}	
	public void draw(Graphics g){
		g.setColor(Color.green);
		g.fillRect(x,y + (30 * switchy),30,30);	
		g.setColor(Color.red);
		g.fillRect(x,(int) (y + (30 * Math.pow(switchy - 1,2))),30,30);
	}
	public boolean check(){
		return (ins[switchy].check());
	}
	public boolean hitTest(int mx, int my){
		return ((x <= mx && mx <= x + 30) && (y <= my && my <= y+60));
	}
	public void toggle(){
		switchy = (int) Math.pow(switchy - 1,2);
	}
	public int width(){
		return 30;
	}
	public int height(){
		return 60;
	}
	public String posData(){
		return("\n2,"+x+","+y);
	}
}