package dbapplication;

/**
 *
 * @author fabio
 */
public class SearchFilter {
    private final String friendlyName, columnName;
    public SearchFilter(String friendly, String column) {
        friendlyName = friendly;
        columnName = column;
    }

    @Override
    public String toString() {
        return friendlyName;
    }
    
    public String getColumnName() {
        return columnName;
    }
}
