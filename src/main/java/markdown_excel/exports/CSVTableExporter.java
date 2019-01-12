package markdown_excel.exports;

import markdown_excel.table.*;
import org.apache.commons.text.StringEscapeUtils;
import java.util.stream.*;
import java.util.*;

public class CSVTableExporter implements Exporter<Table> {
    private final String delim;

    public CSVTableExporter(String delim){
        this.delim = delim;
    }

    @Override
    public String exportToString(Table tbl){
        // FIXME escapeCsv only works for ',', not other delimiters...
        
        return IntStream.range(0, tbl.getHeight())
            .mapToObj(row -> tbl.stream(tbl.rowCursor((_col, row2) -> row == row2))
                    .map(StringEscapeUtils::escapeCsv)
                    .collect(Collectors.joining(this.delim)))
            .collect(Collectors.joining(System.lineSeparator()));
    }
}
