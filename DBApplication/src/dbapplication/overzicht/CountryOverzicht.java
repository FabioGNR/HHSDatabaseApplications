package dbapplication.overzicht;

import dbapplication.DBConnection;
import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Sishi
 */
public class CountryOverzicht extends JDialog {
    
    private JPieChart popularDestinationsChart;
    private JLabel selectedDestionationLabel;
    private JPieChart popularOriginChart;
    private final static Color[] colors = new Color[] {
        Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN,
        Color.CYAN, Color.ORANGE, Color.PINK, Color.LIGHT_GRAY,
        Color.BLACK, Color.MAGENTA
    };
    
    public CountryOverzicht(JFrame owner) {
        super(owner, true);
        setupFrame();
        createComponents();
        generateCharts();
    }

    private void setupFrame() {
        setSize(420, 400);
        setTitle("Country overzicht");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(null);     
    }

    private void createComponents() {
        selectedDestionationLabel = new JLabel("Selected destination: ");
        selectedDestionationLabel.setBounds(10, 10, 185, 30);
        add(selectedDestionationLabel);
        popularDestinationsChart = new JPieChart();
        popularDestinationsChart.setBounds(10, 40, 185, 185);
        popularDestinationsChart.addListener(new DestinationSliceClicked());
        add(popularDestinationsChart);
        popularOriginChart = new JPieChart();
        popularOriginChart.setBounds(205, 40, 185, 185);
        add(popularOriginChart);
    }
    
    private void generateCharts() {
        generateDestinationChart();
        generateOriginChart();
    }
    
    private void generateDestinationChart() {
        try {
            Connection connection = DBConnection.getConnection();
            Statement stat = connection.createStatement();
            ResultSet set = stat.executeQuery(
                    "SELECT country, SUM(students) as total_students FROM (\n" +
                "		SELECT country, COUNT(EX.address) as students FROM institute IT \n" +
                "		LEFT JOIN internship I on I.org_id = IT.org_id\n" +
                "		LEFT JOIN enrollment E on I.`code` = E.ex_program\n" +
                "		LEFT JOIN exchange_student EX on EX.student_id = E.student_id\n" +
                "               GROUP BY country\n" +
                "	UNION ALL\n" +
                "		SELECT country, COUNT(EX.address) as students FROM institute IT \n" +
                "		LEFT JOIN study_program I on I.org_id = IT.org_id\n" +
                "		LEFT JOIN enrollment E on I.`code` = E.ex_program\n" +
                "		LEFT JOIN exchange_student EX on EX.student_id = E.student_id\n" +
                "               GROUP BY country\n" +
                    ") x\n" +
                    "GROUP BY country\n" +
                    "ORDER BY total_students DESC LIMIT 8");
            ArrayList<Slice> slices = new ArrayList<>();
            int colorIndex = 0;
            while(set.next()) {
                slices.add(new Slice(set.getInt("total_students"), colors[colorIndex], set.getString("country")));
                colorIndex++;
                if(colors.length <= colorIndex-1)
                    colorIndex = 0;
            }
            popularDestinationsChart.setSlices(slices.toArray(new Slice[slices.size()]));
        }
        catch (Exception error) {
            error.printStackTrace();
        }
    }
    
    private void generateOriginChart() {
        
    }
    
    class DestinationSliceClicked extends PieChartListener {

        @Override
        public void SliceClicked(Slice slice) {
            if(slice != null) 
                selectedDestionationLabel.setText("Selected destination: "+slice.getUserData()+": "+slice.getValue());
        }
    }
}
