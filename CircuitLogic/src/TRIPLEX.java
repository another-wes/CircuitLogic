import java.awt.*;

public class TRIPLEX extends DUPLEX{
	public TRIPLEX(int xx, int yy){	
		super(xx, yy);
		ins=new InPin[3];
		lines=new Line[3];
	}
	public void draw(Graphics g){
		g.setColor(red_color);
		g.fillRect(x,y,30,60);	
		g.setColor(green_color);
		g.fillRect(x,y + (20 * switchy),30,20);
	}
	public void toggle(){
		if (switchy == 2){
			switchy = 0;
		}
		else{
			switchy += 1;
		}
	}
	public String posData(){
		return("\n3,"+x+","+y);
	}
}