package markdown_excel.exports;

import markdown_excel.table.*;
import java.util.*;

public class MDTableExporter implements Exporter<Table> {
    public MDTableExporter(){}
    
    @Override
    public String exportToString(Table tbl){
        /* FIXME escape content... there's no escapeMarkdown() in StringEscapeUtils */

        StringBuilder out = new StringBuilder();

        int width = tbl.getWidth();
        int height = tbl.getHeight();

        // header row
        for (int x = 0; x < width; ++x) {
            out.append("|");
            out.append(tbl.getCell(x, 0));
            if (x == width - 1) out.append("|" + System.lineSeparator());
        }

        // seperation row
        for (int x = 0; x < width; ++x) {
            out.append("|-");
            if (x == width - 1) out.append("|" + System.lineSeparator());
        }

        // rest rows
        for (int y = 1; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                out.append("|");
                out.append(tbl.getCell(x, y));
                if (x == width - 1) out.append("|" + System.lineSeparator());
            }
        }

        return new String(out);
    }
}
