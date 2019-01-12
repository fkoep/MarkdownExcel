package markdown_excel.table;

import java.util.stream.*;
import java.util.*;

/** A simple implementation of Table, backed by nested ArrayList's which are used
 * in a row-major fashion.
 */
public class SimpleTable implements Table {
    private ArrayList<ArrayList<String>> content;

    /** Constructs a SimpleTable from a list of rows (which are lists of values).
     */
    public SimpleTable(ArrayList<ArrayList<String>> content){
        // ensure all rows have the same size
        if (!content.isEmpty()) {
            int width = content.get(0).size();

            content.stream().skip(1).forEach(row -> {
                for (int i = row.size(); i < width; ++i) {
                    row.add(Table.EMPTY_VALUE);
                }
            });
        }

        this.content = content;
    }

    private static ArrayList<ArrayList<String>> collectContent(Stream<Stream<String>> content){
        return content.map(row -> row.collect(Collectors.toCollection(ArrayList::new)))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    /** Constructs a SimpleTable from a stream of rows (which are streams of values).
     */
    public SimpleTable(Stream<Stream<String>> content){
        this(collectContent(content));
    }

    /** Constructs an empty SimpleTable
     */
    public SimpleTable(){
        this(new ArrayList<>());
    }

    /** Constructs a SimpleTable from the content of another Table.
     */
    public SimpleTable(Table tbl){
        this();
        ensureSize(tbl.getWidth(), tbl.getHeight());
        fill(rowCursor(), tbl.stream(tbl.rowCursor()));
    }

    @Override
    public int getWidth(){
        return (this.content.isEmpty()) ? 0 : this.content.get(0).size();
    }

    @Override
    public int getHeight(){
        return this.content.size();
    }

    @Override
    public String getCell(int col, int row){
        return this.content.get(row).get(col);
    }

    @Override
    public void setCell(int col, int row, String val){
        this.content.get(row).set(col, val);
    }

    @Override
    public void addColumn(int col){
        if (getHeight() == 0) addRow();
        this.content.stream().forEach(row -> row.add(col, Table.EMPTY_VALUE));
    }

    @Override
    public void swapColumn(int colA, int colB){
        for (int row = 0; row < getWidth(); ++row) {
            Collections.swap(this.content.get(row), colA, colB);
        }
    }

    @Override
    public void deleteColumn(int col){
        if (getWidth() <= col) throw new IndexOutOfBoundsException();
        this.content.stream().forEach(row -> row.remove(col));
    }

    @Override
    public void addRow(int row){
        this.content.add(row, new ArrayList<>(Collections.nCopies(getWidth(), Table.EMPTY_VALUE)));
    }

    @Override
    public void swapRow(int rowA, int rowB){
        Collections.swap(this.content, rowA, rowB);
    }

    @Override
    public void deleteRow(int row){
        this.content.remove(row);
    }

    @Override
    public String toString(){
        return this.content.stream()
            .map(row -> row.stream().collect(Collectors.joining(",", "{", "}")))
            .collect(Collectors.joining(",\n\t", "{\n\t", "\n}"));
    }
}
