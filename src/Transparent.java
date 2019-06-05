import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JPanel;

public class Transparent extends JPanel {
	private static final long serialVersionUID = 1L;
	private ArrayList<Point2D> snakes;
    private ArrayList<Point2D> ladders;
    
    private ArrayList<Integer> snakesCounter;
    private ArrayList<Integer> laddersCounter;
    private ArrayList<Color> snakesColors;
    private ArrayList<Color> laddersColors;
    
    public Transparent(ArrayList<Point2D> snakes, ArrayList<Point2D> ladders, ArrayList<Integer> snakesCounter, ArrayList<Integer> laddersCounter) {
    	this.snakes = snakes;
    	this.ladders = ladders;
    	this.snakesCounter = snakesCounter;
    	this.laddersCounter = laddersCounter;
    	this.setBackground(new Color(0, 0, 0, 0));
    	
    	snakesColors = new ArrayList<Color>();
    	snakesColors.addAll(Arrays.asList(Color.RED, Color.DARK_GRAY, Color.GREEN, Color.BLUE, Color.YELLOW, Color.GRAY, Color.CYAN, Color.MAGENTA, Color.WHITE, Color.LIGHT_GRAY, Color.ORANGE, Color.PINK));
    	
//    	laddersColors = new ArrayList<Color>();
//    	laddersColors.addAll(Arrays.asList(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.WHITE, Color.GRAY, Color.LIGHT_GRAY, Color.DARK_GRAY, Color.ORANGE, Color.PINK));
    }
    
	protected void paintComponent(Graphics gr) {
		super.paintComponent(gr);
        Graphics2D g = (Graphics2D)gr;
        g.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        
        while(!snakes.isEmpty()) {
        	Snakes s = new Snakes();
        	Color head = snakesColors.remove(0);
        	Color body = head;
        	
        	int h = snakesCounter.remove(0);
        	int b = snakesCounter.remove(0);
        	
//        	s.setPoints(snakes.remove(0), snakes.remove(0), new Color(head.getRGB()-h), new Color(body.getRGB() - b));
        	s.setPoints(snakes.remove(0), snakes.remove(0), new Color(Color.GREEN.getRGB() + h), new Color(Color.GREEN.getRGB() + b));
            s.draw(g);
        }
        
        while(!ladders.isEmpty()) {
        	int top = laddersCounter.remove(0);
        	int bottom = laddersCounter.remove(0);
        	drawLadderBetweenPoints(g, ladders.remove(0), ladders.remove(0), new Color(Color.BLACK.getRGB() + top), new Color(Color.BLACK.getRGB() + bottom));
        }
	}

    private static void drawLadderBetweenPoints(
        Graphics2D g, Point2D p0, Point2D p1, Color outline, Color shade)
    {
        final double ladderWidth = 20;
        final double distanceBetweenSteps = 20;
        final double barWidth = 3;

        double dx = p1.getX() - p0.getX();
        double dy = p1.getY() - p0.getY();
        double distance = p1.distance(p0);

        double dirX = dx / distance;
        double dirY = dy / distance;

        double offsetX = dirY * ladderWidth * 0.5;
        double offsetY = -dirX * ladderWidth * 0.5;

        Line2D lineR = new Line2D.Double(
            p0.getX() + offsetX, 
            p0.getY() + offsetY,
            p1.getX() + offsetX,
            p1.getY() + offsetY);

        Line2D lineL = new Line2D.Double(
            p0.getX() - offsetX, 
            p0.getY() - offsetY,
            p1.getX() - offsetX,
            p1.getY() - offsetY);

        drawBar(g, lineL, barWidth, outline, shade);
        drawBar(g, lineR, barWidth, outline, shade);

        int numSteps = (int)(distance / distanceBetweenSteps);
        for (int i=0; i<numSteps; i++)
        {
            double stepOffsetX = (i+1) * distanceBetweenSteps;
            double stepOffsetY = (i+1) * distanceBetweenSteps;

            Line2D step = new Line2D.Double(
                p0.getX() + stepOffsetX * dirX - offsetX, 
                p0.getY() + stepOffsetY * dirY - offsetY,
                p0.getX() + stepOffsetX * dirX + offsetX,
                p0.getY() + stepOffsetY * dirY + offsetY);
            drawBar(g, step, barWidth, outline, shade);
        }
    }

    private static void drawBar(Graphics2D g, Line2D line, double barWidth, Color outline, Color shade)
    {
        Stroke stroke = new BasicStroke(
            (float)barWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        Shape bar = stroke.createStrokedShape(line);
//        g.setColor(new Color(200,100,0));
        g.setColor(shade);
        g.fill(bar);
        g.setColor(outline);
//        g.setColor(Color.BLACK);
        g.draw(bar);
    }
	
	class Snakes
	{
	    private Point2D point0 = new Point2D.Double(100,500);
	    private Point2D point1 = new Point2D.Double(700,500);

	    double bodyWidth = 3;
	    int waves = 4;
	    double waveHeight = 0.02;
	    double tailStart = 0.8;
	    double headLength = 8;
	    double headWidth = 7;
	    double eyeRadius = 3;
	    double irisRadius = 1.5;

	    private Shape body;
	    private Shape head;
	    private Shape eyeR;
	    private Shape eyeL;
	    private Shape irisR;
	    private Shape irisL;
	    
	    private Color headColor;
	    private Color bodyColor;

	    void setPoints(Point2D point0, Point2D point1, Color headColor, Color bodyColor)
	    {
	    	this.headColor = headColor;
	    	this.bodyColor = bodyColor;
	    	
	        this.point0.setLocation(point0);
	        this.point1.setLocation(point1);

	        AffineTransform at = AffineTransform.getRotateInstance(
	            currentAngleRad(), point0.getX(), point0.getY());
	        at.translate(point0.getX(), point0.getY());

	        createBody(at);
	        createHead(at);
	    }

	    void draw(Graphics2D g)
	    {
	        g.setColor(this.bodyColor);
	        g.fill(body);
	        g.setColor(this.headColor);
	        g.fill(head);
	        g.setColor(Color.WHITE);
	        g.fill(eyeR);
	        g.fill(eyeL);
	        g.setColor(Color.BLACK);
	        g.fill(irisR);
	        g.fill(irisL);
	    }

	    private void createBody(AffineTransform at)
	    {
	        double distance = point1.distance(point0);
	        int steps = 100;
	        Path2D body = new Path2D.Double();
	        Point2D previousPoint = null;
	        for (int i=0; i<steps; i++)
	        {
	            double alpha = (double)i/(steps-1);
	            Point2D point = computeCenterPoint(alpha, distance);
	            if (previousPoint != null)
	            {
	                Point2D bodyPoint = 
	                    computeBodyPoint(alpha, point, previousPoint);
	                if (i==1)
	                {
	                    body.moveTo(bodyPoint.getX(), bodyPoint.getY());
	                }
	                else
	                {
	                    body.lineTo(bodyPoint.getX(), bodyPoint.getY());
	                }
	            }
	            previousPoint = point;
	        }
	        previousPoint = null;

	        for (int i=steps-1; i>=0; i--)
	        {
	            double alpha = (double)i/(steps-1);
	            Point2D point = computeCenterPoint(alpha, distance);
	            if (previousPoint != null)
	            {
	                Point2D bodyPoint = 
	                    computeBodyPoint(alpha, point, previousPoint);
	                body.lineTo(bodyPoint.getX(), bodyPoint.getY());
	            }
	            previousPoint = point;
	        }
	        this.body = at.createTransformedShape(body);
	    }

	    private Point2D computeBodyPoint(
	        double alpha, Point2D point, Point2D previousPoint)
	    {
	        double dx = point.getX() - previousPoint.getX();
	        double dy = point.getY() - previousPoint.getY();
	        double rdx = -dy;
	        double rdy = dx;
	        double d = Math.hypot(dx, dy);
	        double localBodyWidth = bodyWidth;
	        if (alpha > tailStart)
	        {
	            localBodyWidth *= (1 - (alpha - tailStart) / (1.0 - tailStart));
	        }
	        double px = point.getX() + rdx * (1.0 / d) * localBodyWidth;
	        double py = point.getY() + rdy * (1.0 / d) * localBodyWidth;
	        return new Point2D.Double(px, py);
	    }

	    private Point2D computeCenterPoint(
	        double alpha, double distance)
	    {
	        double r = alpha * Math.PI * 2 * waves;
	        double verticalScaling = 1 - (alpha * 2 - 1) * (alpha * 2 - 1); 
	        double y = Math.sin(r) * distance * waveHeight * verticalScaling;
	        double x = alpha * distance;
	        return new Point2D.Double(x,y);
	    }

	    private void createHead(AffineTransform at)
	    {
	        Shape head = new Ellipse2D.Double(
	            -headLength, -headWidth,
	            headLength + headLength,
	            headWidth + headWidth);
	        this.head = at.createTransformedShape(head);

	        Shape eyeR = new Ellipse2D.Double(
	            -headLength * 0.5 - eyeRadius,
	            -headWidth * 0.6 - eyeRadius,
	            eyeRadius + eyeRadius,
	            eyeRadius + eyeRadius);
	        Shape eyeL = new Ellipse2D.Double(
	            -headLength * 0.5 - eyeRadius,
	            headWidth * 0.6 - eyeRadius,
	            eyeRadius + eyeRadius,
	            eyeRadius + eyeRadius);
	        this.eyeR = at.createTransformedShape(eyeR);
	        this.eyeL = at.createTransformedShape(eyeL);

	        Shape irisR = new Ellipse2D.Double(
	            -headLength * 0.4 - eyeRadius,
	            -headWidth * 0.6 - irisRadius,
	            irisRadius + irisRadius,
	            irisRadius + irisRadius);
	        Shape irisL = new Ellipse2D.Double(
	            -headLength * 0.4 - eyeRadius,
	            headWidth * 0.6 - irisRadius,
	            irisRadius + irisRadius,
	            irisRadius + irisRadius);
	        this.irisR = at.createTransformedShape(irisR);
	        this.irisL = at.createTransformedShape(irisL);
	    }

	    private double currentAngleRad()
	    {
	        double dx = point1.getX() - point0.getX();
	        double dy = point1.getY() - point0.getY();
	        double angleRad = Math.atan2(dy, dx);
	        return angleRad;
	    }
	}
}
