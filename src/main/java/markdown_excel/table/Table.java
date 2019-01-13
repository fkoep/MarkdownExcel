package markdown_excel.table;

import markdown_excel.util.TriFunction;
import java.util.function.*;
import java.util.stream.*;
import java.util.*;

public interface Table {
    /** Represents an empty value in our table.
     */
    public static final String EMPTY_VALUE = "";

    /** Gets the table width (= amount of columns).
     */
    public int getWidth();

    /** Gets the table height (= amount of rows).
     */
    public int getHeight();

    /** Gets a cells value.  
     *
     * @throws IndexOutOfBoundsException if col or row is out of range.
     */
    public String getCell(int col, int row);

    /** Sets a cells value.
     *
     * @throws IndexOutOfBoundsException if col or row is out of range.
     */
    public void setCell(int col, int row, String val);

    /** Shorthand for {@code tbl.setCell(col, row, op.apply(tbl.getCell(col, row))) }.
     *
     * @see #getCell
     * @see #setCell
     */
    default public void applyCell(int col, int row, UnaryOperator<String> op){
        setCell(col, row, op.apply(getCell(col, row)));
    }

    /** Gets the name of a column by its index.  
     *
     * @throws IndexOutOfBoundsException if col is out of range.
     */
    default public String getColumnName(int col){
        return getCell(col, 0);
    }

    /** Gets the index of a column by its name.  
     *
     * @throws NoSuchElementException is no column with for colName was found.
     */
    default public int getColumnIdx(String colName){
        return IntStream.range(0, getWidth())
            .filter(col -> getColumnName(col) == colName)
            .findFirst()
            .getAsInt();
    }

    /** Adds a new column at the specified position. Shifts column currently at this
     * position (if any) and any subsequent columns to the right. 
     *
     * @throws IndexOutOfBoundsException if col is out of range.
     */
    public void addColumn(int col);

    /** Appends a new column to the end of the table.
     */
    default public void addColumn(){ 
        addColumn(getWidth());
    }

    /** Swaps a column with another.
     *
     * @throws IndexOutOfBoundsException if colA or colB is out of range.
     */
    public void swapColumn(int colA, int colB);

    /** Deletes a column. Shifts any subsequent columns to the left. 
     *
     * @throws IndexOutOfBoundsException if col is out of range.
     */
    public void deleteColumn(int col);

    /** Adds a new row at the specified position. Shifts row currently at this
     * position (if any) and any subsequent rows down.
     *
     * @throws IndexOutOfBoundsException if row is out of range.
     */
    public void addRow(int rowPos);

    /** Appends a new row to the end of the table.
     */
    default public void addRow(){
        addRow(getHeight());
    }

    /** Swaps a row with another.
     *
     * @throws IndexOutOfBoundsException if rowA or rowB is out of range.
     */
    public void swapRow(int rowA, int rowB);

    /** Deletes a row. Shifts any subsequent row up.
     *
     * @throws IndexOutOfBoundsException if row is out of range.
     */
    public void deleteRow(int row);

    /** Appends new columns and rows to the table until it has reached the
     * specified minimum size.
     */
    default public void ensureSize(int minWidth, int minHeight){
        for (int i = getWidth(); i < minWidth; ++i){
            addColumn();
        }
        for (int i = getHeight(); i < minHeight; ++i){
            addRow();
        }
    }

    /** Takes a cursor, gets its the corresponding cell values. Identical to 
     * {@code cursor.map(idx -> tbl.getCell(idx.getColumn(), idx.getRow())) }.
     *
     * @see #getCell
     */
    default public Stream<String> stream(Stream<IndexPair> cursor){
        return cursor.map(idx -> getCell(idx.getColumn(), idx.getRow()));
    }

    /** Takes a cursor and a stream of values, zips them, then sets each corresponding
     * cell to the supplied value until either or both streams are exhausted.
     *
     * @see #setCell
     */
    default public void fill(Stream<IndexPair> cursor, Stream<String> values){
        Iterator<IndexPair> cursor_it = cursor.iterator();
        Iterator<String> values_it = values.iterator();
        while (cursor_it.hasNext() && values_it.hasNext()) {
            IndexPair idx = cursor_it.next();
            String val = values_it.next();
            setCell(idx.getColumn(), idx.getRow(), val);
        }
    }

    /** Takes a cursor, applies operation to each corresponding cell.
     *
     * @see #applyCell
     */
    // TODO remove?
    default public void apply(Stream<IndexPair> cursor, TriFunction<Integer, Integer, String, String> op){
        cursor.forEach(idx -> applyCell(idx.getColumn(), idx.getRow(), val -> op.apply(idx.getColumn(), idx.getRow(), val)));
    }

    /** Takes a cursor, sets (overwrites) all corresponding cell.
     *
     * @see #setCell 
     */
    // TODO remove?
    default public void setAll(Stream<IndexPair> cursor, BiFunction<Integer, Integer, String> supply){
        cursor.forEach(idx -> setCell(idx.getColumn(), idx.getRow(), supply.apply(idx.getColumn(), idx.getRow())));
    }

    // NOTE: separated like this so we can reuse it for columnCursor()
    private Stream<IndexPair> rowCursor(int numCols, int numRows){
        return Stream.iterate(new IndexPair(0, 0), idx -> {
            Integer col = idx.getColumn();
            Integer row = idx.getRow();
            col += 1;
            if (col >= numCols){
                col = 0;
                row += 1;
            }
            return new IndexPair(col, row);
        }).limit(numCols * numRows);
    }
    
    /** Returns a cursor which traverses the table by its rows.
     *
     * For example, a 3x3 table will be traversed in this order:
     * <pre>
     * |0|1|2|
     * |-|-|-|
     * |3|4|5|
     * |6|7|8|
     * </pre>
     *
     * Or, in IndexPair's: 
     * <pre>
     * [0|0], [1|0], [2|0], [0|1], [1|1], [2|1], [0|2], [1|2], [2|2]
     * </pre>
     */
    default public Stream<IndexPair> rowCursor(){
        return rowCursor(getWidth(), getHeight());
    }

    /** Shorthand for {@code rowCursor().filter(idx -> pred.test(idx.getColumn(), idx.getRow())) }.
     *
     * @see #rowCursor()
     */
    default public Stream<IndexPair> rowCursor(BiPredicate<Integer, Integer> pred){
        return rowCursor().filter(idx -> pred.test(idx.getColumn(), idx.getRow())); 
    }

    /** Returns a cursor which traverses the table by its columns.
     *
     * For example, a 3x3 table will be traversed in this order:
     * <pre>
     * |0|3|6|
     * |-|-|-|
     * |1|4|7|
     * |2|5|8|
     * </pre>
     *
     * Or, in IndexPair's: 
     * <pre>
     * [0|0], [0|1], [0|2], [1|0], [1|1], [2|2], [2|0], [2|1], [2|2]
     * </pre>
     */
    default public Stream<IndexPair> columnCursor(){
        // we use rowCursor and swap the x and y coordinate
        return rowCursor(getHeight(), getWidth()).map(idx -> new IndexPair(idx.getRow(), idx.getColumn()));
    }

    /** Shorthand for {@code columnCursor().filter(idx -> pred.test(idx.getColumn(), idx.getRow())) }.
     *
     * @see #rowCursor()
     */
    default public Stream<IndexPair> columnCursor(BiPredicate<Integer, Integer> pred){
        return columnCursor().filter(idx -> pred.test(idx.getColumn(), idx.getRow())); 
    }

    default public Stream<String> streamRows(){ 
        return stream(rowCursor()); 
    }
    default public Stream<String> streamRows(BiPredicate<Integer, Integer> pred){ 
        return stream(rowCursor(pred)); 
    }

    default public void fillRows(Stream<String> values){ 
        fill(rowCursor(), values);
    }
    default public void fillRows(BiPredicate<Integer, Integer> pred, Stream<String> values){ 
        fill(rowCursor(pred), values);
    }

    default public Stream<String> streamColumns(){ 
        return stream(columnCursor()); 
    }
    default public Stream<String> streamColumns(BiPredicate<Integer, Integer> pred){ 
        return stream(columnCursor(pred)); 
    }

    default public void fillColumns(Stream<String> values){ 
        fill(columnCursor(), values);
    }
    default public void fillColumns(BiPredicate<Integer, Integer> pred, Stream<String> values){ 
        fill(columnCursor(pred), values);
    }

}
