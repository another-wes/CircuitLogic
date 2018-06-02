import java.awt.*;
import java.util.ArrayList;

public class OutPin extends InPin{
	//private boolean line_added = false;
	protected InPin[] ins;
	protected Line[] lines;
	public OutPin(int xx, int yy) {
		super(xx,yy);
		ins=new InPin[0];
		lines=new Line[0];
	}
	public void draw(Graphics g){//no longer accounts for rotation;
		g.setColor(is_on?green_color:red_color);
		g.fillRect(x,y,40,21);
		g.fillPolygon(new int[]{x,1100,x},new int[]{y,y+10,y+20},3);
		if (name != null){
			g.setFont(new Font("Verdana", Font.BOLD, 10));
			g.setColor(Color.black);
			g.drawString(name, 1109, y + 14);
		}
	}
	public void traceback(ArrayList<Line> p){ //just ADDS the lines
		int gap = height()/(ins.length+1);
		for (int i = 0; i<lines.length; i++) p.remove(lines[i]);
		lines = new Line[ins.length];
		for (int i=0; i<ins.length; i++) { //should only do this once
			
			if (ins[i]==null)continue;
			int start_x;
			int start_y;
			start_x = ins[i].getX() + ins[i].width();
			start_y = ins[i].getY() + (ins[i].height() / 2);
			int mid_x = ((start_x + x)/2)+5*(i-1);
			int end_y = y + ((i + 1) * gap);
			lines[i] = new Line(start_x,start_y,mid_x,x,end_y);
			p.add(lines[i]); 
		}
	}	
	public void attach_in(InPin one){
		ins = new InPin[]{one};
		one.attach_out(this);
		
		return;
	}
	public void attach_in(InPin one, InPin two){
		ins = new InPin[]{one,two};
		one.attach_out(this);
		two.attach_out(this);
		lines=new Line[2];
	}
	public void attach_in(InPin one, InPin two, InPin three){
		ins = new InPin[]{one,two,three};
		one.attach_out(this);
		two.attach_out(this);
		three.attach_out(this);
		lines=new Line[3];
	}
	public void attach_in(InPin one, InPin two, InPin three, InPin four){
		ins = new InPin[]{one,two,three,four};
		one.attach_out(this);
		two.attach_out(this);
		three.attach_out(this);
		four.attach_out(this);
		lines=new Line[4];
	}
	public boolean check(){
		if (ins.length > 0) is_on = (ins[0].check());
		return (is_on);
	}
	public void detach_in(ArrayList<Line> p){
		int leng = ins.length;
		for (int i=0; i<leng;i++){
			ins[i].detach(this);
			p.remove(lines[i]);
		}
		ins=new InPin[leng];
		lines = new Line[leng];
	}
	public void detach_in(InPin that){//second step method
		InPin[] temp=ins;
		ins=new InPin[ins.length-1];
		int j = 0;
		for (int i=0; i<temp.length;i++){
			if (temp[i]!=that){
				ins[j]=temp[i];
				j++;
			}
			
		}
	}
	public String posData(){return"";}//unnecessary for this one, but must be present for saving
	public String linkData(){
		StringBuilder data=new StringBuilder();
		for (int i=0;i<ins.length;i++)data.append(","+ins[i].x+","+ins[i].y);
		return data.toString();
	}
	public int linkLoad(){return 1;	} //used for reattach method.  Shared by NOT
	void swapInputs(int x, int y) { //reorders inputs for cleaner look
		//for now, instead of using this, will edit attach_in to ensure that inputs are added in proper order
		Line templ = lines[x];
		InPin tempi = ins[x];
		lines[x] = lines[y];
		ins[x] = ins[y];
		lines[y] = templ;
		ins[y] = tempi;
	}
	void consolidate() {
		for (int i = 1; i < lines.length; i++) {
	
		     if (lines[i].getStartY() >= lines[i-1].getStartY()) { //reorder; should not occur after attach_in accounted for
		     	swapInputs(i-1, i);
		     	i = 1;//have to redo midpoint checks
		     	continue;
		     }
		     if (lines[i].getMidpoint() >= lines[i-1].getMidpoint()) { //push midpoint
		     	if (i == (lines.length-1)) lines[i].approachMidpoint(lines[i].getStartX()); //last input gets pushed left
		     	else lines[i-1].approachMidpoint(lines[i-1].getEndX()); //all but second-to-last get pushed left
		     }
		}
	}
}