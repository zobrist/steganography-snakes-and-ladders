import java.awt.Color;

public class Ladder {
	int topX, topY, bottomX, bottomY;
	int topCtr, bottomCtr;
	Color top, bottom;
	
	public Ladder(int topX, int topY, int bottomX, int bottomY, int topCtr, int bottomCtr) {
		this.topX = topX;
		this.topY = topY;
		this.bottomX = bottomX;
		this.bottomY = bottomY;
		this.topCtr = topCtr;
		this.bottomCtr = bottomCtr;
	}
}
