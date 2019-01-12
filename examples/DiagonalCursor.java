/env -class-path build/libs/markdown_excel-0.0-all.jar

import markdown_excel.table.IndexPair;
import markdown_excel.table.Table;
import markdown_excel.table.SimpleTable;
import java.util.stream.*;

// cursor which traverses a table in a diagonal line
Stream<IndexPair> diagonalCursor(int startCol, int startRow, int dist){
    return IntStream.iterate(0, i -> i + 1)
        .mapToObj(i -> new IndexPair(startRow + i, startCol + i))
        .limit(dist);
}

System.out.println("# IndexPairs:");
diagonalCursor(0, 0, 5).forEach(System.out::println);

Table tbl = new SimpleTable();
tbl.ensureSize(5, 5);
tbl.setAll(tbl.rowCursor(), (col, row) -> Integer.toString(col + row));
System.out.println("\n# Table:\n" + tbl);

List<String> diag = tbl.stream(diagonalCursor(0, 0, 5)).collect(Collectors.toList());
System.out.println("\n# Diagonal Values:\n" + diag);

