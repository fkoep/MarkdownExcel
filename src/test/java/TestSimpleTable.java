import markdown_excel.table.*;
import org.junit.rules.ExpectedException;
import org.junit.Test;
import org.junit.Rule;
import static org.junit.Assert.assertEquals;
import java.util.stream.*;
import java.util.*;

public class TestSimpleTable {
    @Test
    public void emptyConstruct(){
        Table tbl = new SimpleTable();
        assertEquals(tbl.getWidth(), 0);
        assertEquals(tbl.getHeight(), 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void emptyGetCell(){
        (new SimpleTable()).getCell(0, 0);
    }
    @Test(expected = IndexOutOfBoundsException.class)
    public void emptySetCell(){
        (new SimpleTable()).setCell(0, 0, "foo");
    }
    @Test(expected = IndexOutOfBoundsException.class)
    public void emptyApplyCell(){
        (new SimpleTable()).applyCell(0, 0, val -> val);
    }
    @Test(expected = IndexOutOfBoundsException.class)
    public void emptyDeleteColumn(){
        (new SimpleTable()).deleteColumn(0);
    }
    @Test(expected = IndexOutOfBoundsException.class)
    public void emptyDeleteRow(){
        (new SimpleTable()).deleteRow(0);
    }

    @Test
    public void emptyCursors(){
        Table tbl = new SimpleTable();
        assertEquals(tbl.rowCursor().count(), 0);
        assertEquals(tbl.columnCursor().count(), 0);
        assertEquals(tbl.stream(tbl.rowCursor()).count(), 0);
        tbl.setAll(tbl.rowCursor(), (_col, _row) -> { assert(false); return "foo"; });
        tbl.apply(tbl.rowCursor(), (_col, _row, _val) -> { assert(false); return "foo"; });
    }

    private Table someTable(){
        return new SimpleTable(Stream.of(
                Stream.of("A", "B", "C"),
                Stream.of("1", "2", "3"),
                Stream.of("4", "5", "6"),
                Stream.of("7", "8", "9")));
    }

    @Test
    public void someConstruct(){
        Table tbl = someTable();
        assertEquals(tbl.getWidth(), 3);
        assertEquals(tbl.getHeight(), 4);
    }

    @Test
    public void someGetCell(){
        Table tbl = someTable();
        assertEquals(tbl.getCell(0, 0), "A");
        assertEquals(tbl.getCell(2, 0), "C");
        assertEquals(tbl.getCell(0, 3), "7");
        assertEquals(tbl.getCell(2, 3), "9");
    }

    @Test
    public void someSetCell(){
        Table tbl = someTable();
        tbl.setCell(0, 0, "Foo");
        assertEquals(tbl.getCell(0, 0), "Foo");
        tbl.setCell(2, 3, "Bar");
        assertEquals(tbl.getCell(2, 3), "Bar");
    }

    @Test
    public void someApplyCell(){
        Table tbl = someTable();
        tbl.applyCell(0, 0, val -> val + "ndre");
        assertEquals(tbl.getCell(0, 0), "Andre");
        tbl.applyCell(2, 3, val -> val + "99");
        assertEquals(tbl.getCell(2, 3), "999");
    }

    @Test
    public void someGetColumnName(){
        Table tbl = someTable();
        assertEquals(tbl.getColumnName(0), "A");
        assertEquals(tbl.getColumnName(2), "C");
    }

    @Test
    public void someGetColumnIdx(){
        Table tbl = someTable();
        assertEquals(tbl.getColumnIdx("A"), 0);
        assertEquals(tbl.getColumnIdx("C"), 2);
    }

    @Test
    public void someDeleteColumn(){
        Table tbl = someTable();
        tbl.deleteColumn(0);
        assertEquals(tbl.getWidth(), 2);
        assertEquals(tbl.getCell(0, 0), "B");
        tbl.deleteColumn(1);
        assertEquals(tbl.getWidth(), 1);
        assertEquals(tbl.getCell(0, 0), "B");
    }

    @Test
    public void someRowCursors(){
        Table tbl = someTable();

        List<IndexPair> expected = Stream.of(
                new IndexPair(0, 0), new IndexPair(1, 0), new IndexPair(2, 0),
                new IndexPair(0, 1), new IndexPair(1, 1), new IndexPair(2, 1),
                new IndexPair(0, 2), new IndexPair(1, 2), new IndexPair(2, 2),
                new IndexPair(0, 3), new IndexPair(1, 3), new IndexPair(2, 3)).collect(Collectors.toList());
        List<IndexPair> found = tbl.rowCursor().collect(Collectors.toList());

        for (int i = 0; i < expected.size(); ++i){
            assertEquals(expected.get(i).getColumn(), found.get(i).getColumn());
            assertEquals(expected.get(i).getRow(), found.get(i).getRow());
        }

        List<IndexPair> expected2 = Stream.of(
                new IndexPair(1, 1), new IndexPair(2, 1),
                new IndexPair(1, 2), new IndexPair(2, 2)).collect(Collectors.toList());
        List<IndexPair> found2 = tbl.rowCursor(1, 1, 2).collect(Collectors.toList());

        for (int i = 0; i < expected2.size(); ++i){
            assertEquals(expected2.get(i).getColumn(), found2.get(i).getColumn());
            assertEquals(expected2.get(i).getRow(), found2.get(i).getRow());
        }
    }
}

//     @Test(expected = NoSuchElementException.class)
//     public void emptyEntries(){
//         (new SimpleTable()).streamEntries().findFirst().get();
//     }
//     @Test
//     public void emptyAddRow(){
//         Table tbl = new SimpleTable();
//         tbl.addRow();
//         assertEquals(tbl.getHeight(), 1);
//         assertEquals(tbl.getWidth(), 0);
//         tbl.addRow();
//         assertEquals(tbl.getHeight(), 2);
//         assertEquals(tbl.getWidth(), 0);
//     }
//     @Test
//     public void emptyAddColumn(){
//         Table tbl = new SimpleTable();
//         tbl.addColumn("Foo");
//         assertEquals(tbl.getHeight(), 1);
//         assertEquals(tbl.getWidth(), 1);
//         tbl.addColumn("Bar");
//         assertEquals(tbl.getHeight(), 1);
//         assertEquals(tbl.getWidth(), 2);
//     }

//     @Test(expected = IllegalArgumentException.class)
//     public void nullConstruct(){
//         Table tbl = new SimpleTable(Stream.of(Stream.<Value>of(null, null, null)));
//     }

//     @Test
//     public void fullConstruct() {
//         Table tbl = new SimpleTable(fullContent.stream().map(List::stream));
//         assertEquals(tbl.getWidth(), fullContent.get(0).size());
//         assertEquals(tbl.getHeight(), fullContent.size());
//     }

//     @Test
//     public void fullEntries() {
//         Table tbl = new SimpleTable(fullContent.stream().map(List::stream));
//         tbl.streamEntries()
//             .forEach(e -> assertEquals(e.getValue(), fullContent.get(e.getRowIdx()).get(e.getColumnIdx())));
//     }
// }
