import java.awt.Color;

public class Snake {
	int headX, headY, tailX, tailY;
	int headCtr, tailCtr;
	Color head, tail;
	
	public Snake(int headX, int headY, int tailX, int tailY, int headCtr, int tailCtr) {
		this.headX = headX;
		this.headY = headY;
		this.tailX = tailX;
		this.tailY = tailY;
		this.headCtr = headCtr;
		this.tailCtr = tailCtr;
	}
}
