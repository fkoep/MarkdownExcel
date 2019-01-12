/env -class-path build/libs/markdown_excel-0.0-all.jar

import markdown_excel.table.*;
import java.util.stream.*;
import java.util.*;

// Cursor which traverses a table as-if you were to sort the rows by a specific column.
//
// TODO eventually, this should be a feature of Table...
Stream<IndexPair> sortingCursor(Table tbl, int sortColIdx){
    List<String> sortCol = tbl.stream(tbl.columnCursor((col, row) -> col == sortColIdx && row > 0)).collect(Collectors.toList());
    Collections.sort(sortCol);
    return tbl.rowCursor().map(idx -> {
        if (idx.getRow() > 0) {
            return idx.withRow(sortCol.indexOf(tbl.getCell(sortColIdx, idx.getRow())) + 1);
        } else {
            return idx;
        }
    });
}

Table tbl = new SimpleTable();
tbl.ensureSize(3, 5);
tbl.fill(tbl.rowCursor(), Stream.of(
    "First", "Second", "Third",
    "D", "D1", "D2",
    "A", "A1", "A2",
    "C", "C1", "C2",
    "B", "B1", "B2"
));
System.out.println("# Unsorted:\n" + tbl.toString());

System.out.println("# Sorting Indicies:\n");
sortingCursor(tbl, 0).forEach(System.out::println);

Table sorted = new SimpleTable();
sorted.ensureSize(3, 5);
sorted.fill(sortingCursor(tbl, 0), tbl.stream(tbl.rowCursor()));
System.out.println("\n# Sorted:\n" + sorted.toString());
