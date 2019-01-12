package markdown_excel.exports;

import java.io.IOException;
import java.io.PrintWriter;

public interface Exporter<T> {
    public String exportToString(T obj);

    default public void exportToFile(T obj, String path) throws IOException {
        try (PrintWriter out = new PrintWriter(path)) {
            out.print(exportToString(obj));
        }
    }
}
