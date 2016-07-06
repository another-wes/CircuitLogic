//This file is basically a complicated image for now.  Once it works I'll reimplement so it exists as a separate component in the JFrame
//rooted at

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Options{
	public Options(){
		return;
	}
	public void draw(Graphics g){
		g.setColor(Color.WHITE);
		g.fillRect(0, 654, 1100, 30);
		g.setColor(Color.BLACK);
		g.drawLine(0, 654, 1100, 654);
		for (int i=100;i<1200;i=100+i){
			g.drawLine(i, 654, i, 720);
		}
		g.setFont(new Font("Arial", Font.BOLD, 15));
		g.drawString("Add AND          Add OR         Add NAND        Add NOR        Add XOR     Add 2to1Multi Add 3to1Multi     Add NOT     Attach output  Remove gate         Save",22, 671);
		g.setColor(Color.BLUE);
		g.fillRect(1001, 655, 28, 28);
		g.setColor(Color.lightGray);
		g.fillRect(1007, 655, 18, 8);
		g.setColor(Color.BLUE);
		g.fillRect(1020, 656, 4, 6);
		g.setColor(Color.WHITE);
		g.fillRect(1006, 667, 20, 14);
	}
}	