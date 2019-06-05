import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class LadderDrawingManual
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI()
    {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.getContentPane().add(new LadderDrawingManualPanel());

        f.setSize(800, 800);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}


class LadderDrawingManualPanel extends JPanel 
    implements MouseListener, MouseMotionListener
{
    private Point2D point0 = new Point2D.Double(300,300);
    private Point2D point1 = new Point2D.Double(500,700);
    private Point2D draggedPoint = null;

    LadderDrawingManualPanel()
    {
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics gr)
    {
        super.paintComponent(gr);
        Graphics2D g = (Graphics2D)gr;
        g.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.RED);
        paintDot(g, point0, 8);
        paintDot(g, point1, 8);

        drawLadderBetweenPoints(g, point0, point1);
    }

    private static void paintDot(Graphics2D g, Point2D p, double radius)
    {
        g.fill(new Ellipse2D.Double(
            p.getX() - radius, p.getY() - radius,
            radius + radius, radius + radius));
    }

    private static void drawLadderBetweenPoints(
        Graphics2D g, Point2D p0, Point2D p1)
    {
        final double ladderWidth = 40;
        final double distanceBetweenSteps = 30;
        final double barWidth = 5;

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

        drawBar(g, lineL, barWidth);
        drawBar(g, lineR, barWidth);

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
            drawBar(g, step, barWidth);
        }
    }

    private static void drawBar(Graphics2D g, Line2D line, double barWidth)
    {
        Stroke stroke = new BasicStroke(
            (float)barWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        Shape bar = stroke.createStrokedShape(line);
//        g.setColor(new Color(200,100,0));
        g.setColor(Color.BLUE);
        g.fill(bar);
        g.setColor(Color.YELLOW);
//        g.setColor(Color.BLACK);
        g.draw(bar);
    }


    @Override
    public void mouseDragged(MouseEvent e)
    {
        if (draggedPoint != null)
        {
            draggedPoint.setLocation(e.getPoint());
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        // Nothing to do here
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        // Nothing to do here
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        draggedPoint = null;
        double thresholdSquared = 10*10;

        if (e.getPoint().distanceSq(point0) < thresholdSquared)
        {
            draggedPoint = point0;
        }
        if (e.getPoint().distanceSq(point1) < thresholdSquared)
        {
            draggedPoint = point1;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        draggedPoint = null;
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        // Nothing to do here
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        // Nothing to do here
    }

}