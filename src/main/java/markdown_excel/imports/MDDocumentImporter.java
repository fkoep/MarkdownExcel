package markdown_excel.imports;

import markdown_excel.doc.*;
import org.jparsec.error.ParserException;

public class MDDocumentImporter implements Importer<Document> {
    public MDDocumentImporter(){}

    @Override
    public Document importFromString(String str) throws ParserException {
        return MDParsers.document().parse(str);
    }
}
