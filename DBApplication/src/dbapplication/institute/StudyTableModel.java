package dbapplication.institute;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Sishi
 */
public class StudyTableModel extends AbstractTableModel{
    private ArrayList<Study> searchResults = new ArrayList<>();
    private final String[] columnNames = new String[] { "Code" };
    
    public StudyTableModel() {
        super();
    }
    
    public void setResults(ArrayList<Study> results) {
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
    
    public Study getStudyAt(int rowIndex) {
        return searchResults.get(rowIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return searchResults.get(rowIndex).getDataAt(columnIndex);
    }
    
}