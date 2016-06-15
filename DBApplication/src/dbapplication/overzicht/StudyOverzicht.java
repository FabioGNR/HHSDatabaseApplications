package dbapplication.overzicht;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author Sishi
 */
public class StudyOverzicht extends JDialog {
    
    public StudyOverzicht(JFrame owner) {
        super(owner, true);
        setupFrame();
        createComponents();
    }

    private void setupFrame() {
        setSize(400, 400);
        setTitle("Country overzicht");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(null);
    }

    private void createComponents() {
        
    }
    
} 
    
    /*public StudyOverzicht(String appTitle, String chartTitle) {
        PieDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset, chartTitle);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 300));
        setContentPane(chartPanel);

    }

    private PieDataset createDataset() {
        DefaultPieDataset result = new DefaultPieDataset();
        result.setValue("Brain power", 30);
        result.setValue("Stupidity", 50);
        result.setValue("Something else", 20);
        return result;
    }

    private JFreeChart createChart(PieDataset dataset, String chartTitle) {
        JFreeChart chart = ChartFactory.createPieChart3D("title", dataset, true, true, false);
        
        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(0);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        return chart;
        
    }
} */
    

