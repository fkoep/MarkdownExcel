import markdown_excel.table.*;
import markdown_excel.imports.*;
import markdown_excel.exports.*;
import org.junit.rules.ExpectedException;
import org.junit.Test;
import org.junit.Rule;
import static org.junit.Assert.assertEquals;
import java.util.stream.*;
import java.util.*;

public class TestCSVImportExport {
    @Test
    public void emptyCSVImport(){
        Table tbl = (new CSVTableImporter(",")).importFromString("");
        assertEquals(tbl.getWidth(), 0);
        assertEquals(tbl.getHeight(), 0);
    }

    @Test
    public void emptyCSVExport(){
        Table tbl = new SimpleTable();
        assertEquals((new CSVTableExporter(",")).exportToString(tbl), "");
    }

    private Table someTable(){
        return new SimpleTable(Stream.of(
                Stream.of("A", "B", "C"),
                Stream.of("1", "2", "3"),
                Stream.of("4", "5", "6"),
                Stream.of("7", "8", "9")));
    }

    private String someCSV(){
        return String.join(
            System.lineSeparator(),
            "A,B,C", 
            "1,2,3",
            "4,5,6",
            "7,8,9");
    }

    @Test
    public void someCSVImport(){
        Table some = someTable();
        Table imported = (new CSVTableImporter(",")).importFromString(someCSV());

        assertEquals(some.getWidth(), imported.getWidth());
        assertEquals(some.getHeight(), imported.getHeight());
        some.rowCursor().forEach(idx -> {
            assertEquals(some.getCell(idx.getColumn(), idx.getRow()), imported.getCell(idx.getColumn(), idx.getRow()));
        });
    }

    @Test
    public void someCSVExport(){
        Table some = someTable();
        assertEquals((new CSVTableExporter(",")).exportToString(some), someCSV());
    }
}
