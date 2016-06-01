package dbapplication.student;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author fabio
 */
public class StudentTableModel extends AbstractTableModel{
    private ArrayList<Student> searchResults = new ArrayList<>();
    private final String[] columnNames = new String[] { "Student ID", "Name", "Gender", "Email" };
    public StudentTableModel() {
        super();
    }
    
    public void setResults(ArrayList<Student> results) {
        searchResults = results;
        this.fireTableDataChanged();
    }
    
    @Override
    public int getRowCount() {
        return searchResults.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return searchResults.get(rowIndex).getDataAt(columnIndex);
    }
    
}
