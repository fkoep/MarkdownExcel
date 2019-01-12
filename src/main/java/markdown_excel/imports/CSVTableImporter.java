package markdown_excel.imports;

import markdown_excel.table.*;
import org.apache.commons.text.StringEscapeUtils;
import java.util.stream.*;
import java.util.*;

public class CSVTableImporter implements Importer<Table> {
    private final String delim;

    public CSVTableImporter(String delim){
        this.delim = delim;
    }

    @Override
    public Table importFromString(String str){
        // FIXME unescapeCsv only works for ',', not other delimiters...
        return new SimpleTable(Arrays.stream(str.split(System.lineSeparator()))
                .filter(line -> line.trim().length() > 0)
                .map(line -> Arrays.stream(line.split(this.delim)).map(StringEscapeUtils::unescapeCsv)));
    }
}
