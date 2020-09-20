/**
 * TableFilter to filter for containing substrings.
 *
 * @author Matthew Owen
 */
public class SubstringFilter extends TableFilter {
    Table _input;
    String _colName, _subStr;

    public SubstringFilter(Table input, String colName, String subStr) {
        super(input);
        _input = input;
        _colName = colName;
        _subStr = subStr;
    }

    @Override
    protected boolean keep() {
        int col = _input.colNameToIndex(_colName);
        if (_next.getValue(col).contains(_subStr)) {
            return true;
        }
        return false;
    }
}
