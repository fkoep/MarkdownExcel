/env -class-path build/libs/markdown_excel-0.0-all.jar

import markdown_excel.table.Table;
import markdown_excel.table.SimpleTable;
import java.util.stream.*;

// Create a 3x4 table
Table tbl = new SimpleTable();
tbl.ensureSize(3, 4);
tbl.fill(tbl.rowCursor(), Stream.generate(() -> "1"));

System.out.println("\n# Create a 3x4 table:\n" + tbl.toString());

// Prepend a head-row
tbl.addRow(0);
tbl.fill(tbl.rowCursor(), Stream.of("A", "B", "C"));

System.out.println("\n# Prepend a head-row:\n" + tbl.toString());

// Set cells to ($value + $col + $row - 1)
tbl.apply(tbl.rowCursor((_col, row) -> row > 0), (col, row, val) -> Integer.toString(Integer.parseInt(val) + col - 1 + row));

System.out.println("\n# Set cells to {value + col + (row - 1)}:\n" + tbl.toString());

// Add Sum column
tbl.addColumn();
tbl.setCell(3, 0, "Sum");
tbl.setAll(tbl.columnCursor((col, row) -> col == 3 && row > 0), (_col, row) ->
        Integer.toString(tbl.stream(tbl.rowCursor((col, row2) -> row2 == row && col < 3)).mapToInt(Integer::parseInt).sum()));

// last statement is identical to:
// for (int row = 1; row < tbl.getHeight(); ++row) {
//     tbl.setCell(3, row, Integer.toString(tbl.stream(tbl.rowCursor(0, row, 3, 1)).mapToInt(Integer::parseInt).sum()));
// }

System.out.println("\n# Add Sum column:\n" + tbl.toString());
