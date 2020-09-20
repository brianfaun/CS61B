/**
 * TableFilter to filter for entries greater than a given string.
 *
 * @author Matthew Owen
 */
public class GreaterThanFilter extends TableFilter {
    Table _input;
    String _colName, _ref;

    public GreaterThanFilter(Table input, String colName, String ref) {
        super(input);
        _input = input;
        _colName = colName;
        _ref = ref;
    }

    @Override
    protected boolean keep() {
        int col = _input.colNameToIndex(_colName);
        if (_next.getValue(col).length() > _ref.length()) {
            return true;
        } else {
            return false;
        }
    }
}
