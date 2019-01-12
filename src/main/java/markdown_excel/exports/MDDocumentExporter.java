package markdown_excel.exports;

import markdown_excel.table.Table;
import markdown_excel.doc.*;
import java.util.*;

public class MDDocumentExporter implements Exporter<Document> {
    public MDDocumentExporter(){}
    
    @Override
    public String exportToString(Document doc){
        StringBuilder out = new StringBuilder();

        doc.streamElements().forEach(e -> {
            if (out.length() != 0) {
                out.append(System.lineSeparator());
            }
            if (e instanceof RawBlockElement) {
                RawBlockElement raw = (RawBlockElement) e;
                out.append(raw.getRaw());
                out.append(System.lineSeparator());
            } else if (e instanceof TableElement) {
                TableElement tbl = (TableElement) e;
                out.append((new MDTableExporter()).exportToString(tbl.getTable()));
            }
        });

        return new String(out);
    }
}
