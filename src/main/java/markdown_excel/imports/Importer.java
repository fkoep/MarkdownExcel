package markdown_excel.imports;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public interface Importer<T> {
    // TODO should be `T import(String); T import(Path);`?
    T importFromString(String str) throws RuntimeException;

    default T importFromFile(String path) throws RuntimeException, IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        String str = new String(bytes, StandardCharsets.UTF_8);
        return importFromString(str);
    }
}
