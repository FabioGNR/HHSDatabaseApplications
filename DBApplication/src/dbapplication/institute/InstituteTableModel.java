package dbapplication.institute;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Sishi
 */
public class InstituteTableModel extends AbstractTableModel{
    private ArrayList<Institute> searchResults = new ArrayList<>();
    private final String[] columnNames = new String[] { "Name", "City", "Country", "Address" };
    public InstituteTableModel() {
        super();
    }
    
    public void setResults(ArrayList<Institute> results) {
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
    
    public Institute getInstituteAt(int rowIndex) {
        return searchResults.get(rowIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return searchResults.get(rowIndex).getDataAt(columnIndex);
    }
    
}