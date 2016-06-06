package dbapplication.program;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author RLH
 */
public class ProgramTableModel extends AbstractTableModel{
    
    private ArrayList<ExProgram> searchProgram = new ArrayList<>();
    private final String[] columnNames = new String[]{"Code", "Name"};
    
    public ProgramTableModel(){
        super();
    }
    
    public void setResults(ArrayList<ExProgram> results){
        searchProgram = results;
        this.fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return searchProgram.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    @Override
    public String getColumnName(int column){
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return searchProgram.get(rowIndex).getDataAt(columnIndex);
    }
    
}
