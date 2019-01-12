package markdown_excel.table;

/** Simple tuple class, pairing a column index and a row index.
 */
// TODO override equals()?
public final class IndexPair {
    private final int col, row;

    public IndexPair(int col, int row){
        this.col = col;
        this.row = row;
    }

    public int getColumn(){ return this.col; }
    public int getRow(){ return this.row; }
    public IndexPair withColumn(int newCol){ return new IndexPair(newCol, this.row); }
    public IndexPair withRow(int newRow){ return new IndexPair(this.col, newRow); }

    @Override
    public String toString() { return "[" + this.col + "|" + this.row + "]"; }
}
