package markdown_excel.doc;

import markdown_excel.table.Table;

/* Table Markdown element
 *
 * #see #markdown_excel.table.Table
 */
public class TableElement implements BlockElement {
    private final Table tbl;

    public TableElement(Table tbl){
        this.tbl = tbl;
    }

    public Table getTable(){ return this.tbl; }
}
