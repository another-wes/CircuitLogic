import java.awt.*;

public class OutPin extends InPin{
	protected InPin[] ins;
	public OutPin(int xx, int yy) {
		super(xx,yy);
		ins=new InPin[0];
	}
	public void draw(Graphics g){//no longer accounts for rotation;
		g.setColor(is_on?Color.green:Color.red);
		g.fillRect(1110,y,40,21);
		g.fillPolygon(new int[]{1110,1100,1110},new int[]{y,y+10,y+20},3);
		if (name != null){
			g.setFont(new Font("Verdana", Font.BOLD, 10));
			g.setColor(Color.black);
			g.drawString(name, 1109, y + 14);
		}
	}
	public void traceback(Graphics g){
		g.setColor(Color.black);
		int gap = height()/(ins.length+1);
		for (int i=0; i<ins.length; i++) {
			if (ins[i]==null)continue;
			int start_x;
			int start_y;
			start_x = ins[i].getX() + ins[i].width();
			start_y = ins[i].getY() + (ins[i].height() / 2);
			int mid_x = ((start_x + x)/2)+5*(i-1);
			int end_y = y + ((i + 1) * gap);
			g.drawLine (start_x,start_y,mid_x,start_y);
			g.drawLine (mid_x,start_y,mid_x,end_y);
			g.drawLine (mid_x,end_y,x,end_y);
		}
	}	
	public void attach_in(InPin one){
		ins = new InPin[]{one};
		one.attach_out(this);
	}
	public void attach_in(InPin one, InPin two){
		ins = new InPin[]{one,two};
		one.attach_out(this);
		two.attach_out(this);
	}
	public void attach_in(InPin one, InPin two, InPin three){
		ins = new InPin[]{one,two,three};
		one.attach_out(this);
		two.attach_out(this);
		three.attach_out(this);
	}
	public void attach_in(InPin one, InPin two, InPin three, InPin four){
		ins = new InPin[]{one,two,three,four};
		one.attach_out(this);
		two.attach_out(this);
		three.attach_out(this);
		four.attach_out(this);
	}
	public boolean check(){
		if (ins.length > 0)is_on = (ins[0].check());
		return (is_on);
	}
	public void detach_in(){
		for (int i=0; i<ins.length;i++){
			ins[i].detach(this);
		}
		ins=new InPin[0];
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
}