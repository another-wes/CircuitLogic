import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Ellipse2D;
//import java.util.ArrayList;

public class Line { //held by Board, drawn by outpin's traceback.
	protected InPin src;
	protected int start_x,start_y,midpoint,end_x,end_y; //should always consist of three segments
	private boolean bump_one = false, bump_two = false; //turns on when adding "corners"
	protected Line dependency = null; //points to line whose midpoint is being USED
	public Line(int x0,int y0,int m,int x1,int y1) {
		start_x = x0;
		start_y = y0;
		midpoint = m;
		end_x = x1;
		end_y = y1;
	}
	public Line(int x0,int y0,int m,int x1,int y1, InPin s) {
		start_x = x0;
		start_y = y0;
		midpoint = m;
		end_x = x1;
		end_y = y1;
		src = s;
	}
	public void setBump(int i)
	{
		if (i==2) bump_two = true; 
		else bump_one = true;
	}
	public void setDependency(Line d) //ONLY ONE CAN BE SET AT A TIME
	{
		dependency = d;
	}
	public void neighborConsolidate(Line other) { //for lines leading into same gate
		//no issues identified thus far
		return;
	}
	public boolean consolidateWith(Line other) { //the general case
		boolean collided = false;
		//check if drop-line overlaps with other's end line
		if (((other.midpoint <= midpoint) && (midpoint <= other.end_x)) //drop-line's x is within domain of other's end-line
				&& ((((start_y<start_x)?start_y:start_x) <= other.end_y) && (other.end_y <= ((start_y<start_x)?start_y:start_x))))
		{ //this.drop overlaps with other.end
			collided = true;
		}
		
		//check if end line overlaps with other's drop line; should be an unnecessary check
		if (((midpoint <= other.midpoint) && (other.midpoint <= end_x)) //drop-line's x is within domain of other's end-line
				&& ((((other.start_y<other.start_x)?other.start_y:other.start_x) <= end_y) && (end_y <= ((other.start_y<other.start_x)?other.start_y:other.start_x))))
		{ //this.drop overlaps with other.end
			collided = true;
			
		}
		//check if both have same source POINT
		if ((other.start_x == start_x)&&(other.start_y == start_y)) //memoize?
		{  //if so, shift one to the vertex of another
			if (other.midpoint>midpoint)
			{ //detect closer midpoint
				other.start_x = midpoint; //shorten longer starting segment
				other.setDependency(this);
				bump_one = true; //add bump to vertex where two diverge
			}
			else if (other.midpoint<midpoint)
			{
				start_x = other.midpoint;
				dependency = other;
				other.setBump(1);
			}
			else
			{ //outputs are aligned
				
			}
		}
		return collided;
	}

	public void draw(Graphics g){//no longer accounts for rotation;
		g.setColor(Color.black);
		g.drawLine (start_x,start_y,midpoint,start_y);
		g.drawLine (midpoint,start_y,midpoint,end_y);
		g.drawLine (midpoint,end_y,end_x,end_y);
		if (bump_one) g.fillOval(midpoint-4,start_y-4,8,8);
		if (bump_two) g.fillOval(midpoint-4,end_y-4,8,8);
	}
	int getStartX() {
		return start_x;
	}
	int getStartY() {
		return start_y; //THIS SHOULD NOT CHANGE
	}
	int getEndX() {
		return end_x;
	}
	int getEndY() {
		return end_y; //does not change in current implementation
	}

	int getMidpoint() {
		return midpoint;
	}

	void approachMidpoint(int m) {
		midpoint = (midpoint + m)/2;
	}
}