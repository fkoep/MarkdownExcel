package markdown_excel.doc;

import markdown_excel.table.Table;
import java.util.stream.*;

// TODO add Table/BlockElement IDs
public interface Document {
    public Stream<BlockElement> streamElements();

    default public Stream<Table> streamTables() {
        return streamElements()
            .filter(e -> e instanceof TableElement)
            .map(e -> ((TableElement) e).getTable());
    }

    default public BlockElement getNthElement(int idx){
        return streamElements().skip(idx).findFirst().get();
    }
    default public Table getNthTable(int idx){
        return streamTables().skip(idx).findFirst().get();
    }

    public void addElement(int idx, BlockElement elem);

    default public void addElement(BlockElement elem){
        addElement(0, elem);
    }

    public void deleteElement(int idx);
}
