package dbapplication.overzicht;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JComponent;
// SOURCE: http://www.tutorialspoint.com/javaexamples/gui_piechart.htm

class Slice {

    private final Object userData;
    private final double value;
    private final Color color;

    public Slice(double value, Color color, Object userData) {
        this.value = value;
        this.color = color;
        this.userData = userData;
    }

    public double getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }

    public Object getUserData() {
        return userData;
    }
}

class JPieChart extends JComponent {

    private Slice[] slices = {new Slice(100, Color.BLUE, null)};
    private Slice selected = null;
    private ArrayList<PieChartListener> listeners = new ArrayList<>();
    private static Stroke outlineStroke = new BasicStroke(5);
    private static Stroke fillStroke = new BasicStroke(1);

    public void setSlices(Slice[] slices) {
        this.slices = slices;
        selected = null;
        repaint();
    }

    public JPieChart() {
        this.addMouseListener(new MouseClickListener());
    }

    public void addListener(PieChartListener listener) {
        listeners.add(listener);
    }

    @Override
    public void paint(Graphics g) {
        drawPie((Graphics2D) g, new Rectangle(this.getSize()), slices);
    }

    private void drawPie(Graphics2D g, Rectangle area, Slice[] slices) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        double total = getTotalValue();
        double curValue = 0.0D;
        int startAngle, overlayAngle = 0, overlayArc = 0;
        for (int i = 0; i < slices.length; i++) {
            startAngle = (int) (curValue * 360 / total);
            int arcAngle = (int) (slices[i].getValue() * 360 / total);
            g.setColor(slices[i].getColor());
            g.fillArc(area.x, area.y, area.width, area.height,
                    startAngle, arcAngle);
            if (slices[i] == selected) {
                overlayAngle = startAngle;
                overlayArc = arcAngle;
            }
            curValue += slices[i].getValue();
        }
        if (selected != null) {
            // draw outline if selected
            g.setColor(Color.BLACK);
            g.setStroke(outlineStroke);
            g.drawArc(area.x, area.y, area.width, area.height,
                    overlayAngle, overlayArc);
            g.setStroke(fillStroke);
        }
    }

    private double getAngle(double x1, double y1, double x2, double y2) {
        float angle = (float) Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
        if (angle < 0) {
            angle += 360;
        }
        return angle;
    }

    private double getTotalValue() {
        double total = 0.0D;
        for (int i = 0; i < slices.length; i++) {
            total += slices[i].getValue();
        }
        return total;
    }

    class MouseClickListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            // detect click on correct slice
            double total = getTotalValue();
            Rectangle area = new Rectangle(getSize());
            double clickX = e.getX(), clickY = e.getY();
            double clickAngle = getAngle(area.getCenterX(), area.getCenterY(),
                    clickX, clickY);
            if (clickAngle < 0) {
                clickAngle += 360;
            }
            clickAngle = (clickAngle * -1) + 360;
            double curValue = 0.0D;
            int startAngle;
            for (int i = 0; i < slices.length; i++) {
                startAngle = (int) (curValue * 360 / total);
                int arcAngle = (int) (slices[i].getValue() * 360 / total);
                double endAngle = (startAngle + arcAngle) % 360;
                if (clickAngle > startAngle && clickAngle < endAngle) {
                    selected = slices[i];
                }
                curValue += slices[i].getValue();
            }
            // pass on to listeners
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).SliceClicked(selected);
            }
            repaint();
        }
    }
}
