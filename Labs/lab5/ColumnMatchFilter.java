/**
 * TableFilter to filter for entries whose two columns match.
 *
 * @author Matthew Owen
 */
public class ColumnMatchFilter extends TableFilter {
    Table _input;
    String _colName1, _colName2;

    public ColumnMatchFilter(Table input, String colName1, String colName2) {
        super(input);
        _input = input;
        _colName1 = colName1;
        _colName2 = colName2;
    }

    @Override
    protected boolean keep() {
        int col1 = _input.colNameToIndex(_colName1);
        int col2 = _input.colNameToIndex(_colName2);
        if (_next.getValue(col1).equals(_next.getValue(col2))) {
            return true;
        } else {
            return false;
        }
    }
}
