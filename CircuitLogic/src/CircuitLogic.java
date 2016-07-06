import javax.swing.JFrame;
public class CircuitLogic{
	public static void main( String[] args ){
		startMenu menu = new startMenu();//will change how this works when finally implementing beginning menu
		menu.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		menu.setVisible( true ); // display frame
	}
}