package pachet;

import javax.swing.JFrame;

public class Simulator extends JFrame {

	private static final long serialVersionUID = 1L;
	private Menu menu;

	public Simulator() {

		this.setTitle("Crossroad simulation");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		menu = new Menu(this);
		menu.initialize(this.getContentPane());

		this.setSize(900, 600);
		this.setVisible(true);

	}

	public static void main(String[] args) {
		new Simulator();
	}

}