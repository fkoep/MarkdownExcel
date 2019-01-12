/env -class-path build/libs/markdown_excel-0.0-all.jar

import markdown_excel.table.*;
import markdown_excel.imports.*;
import markdown_excel.exports.*;
import java.util.stream.*;
import java.util.*;

// import from CSV file
Table tbl = (new CSVTableImporter(",")).importFromFile("examples/results.csv");

System.out.println("# Imported:\n" + tbl.toString());

// add head-row
tbl.addRow(0);
tbl.fill(tbl.rowCursor(), Stream.of("Name", "Testat 1", "Testat 2"));

System.out.println("\n# CSV:\n" + (new CSVTableExporter(",")).exportToString(tbl));
System.out.println("\n# MD:\n" + (new MDTableExporter()).exportToString(tbl));
System.out.println("\n# Google charts:\n" + (new GChartTableExporter()).exportToString(tbl));
