import java.awt.GridLayout;

import javax.swing.JPanel;

public class Cell extends JPanel {
	private static final long serialVersionUID = 1L;

	public Cell(GridLayout gridLayout) {
		super(gridLayout);
	}

	public int label;
}
