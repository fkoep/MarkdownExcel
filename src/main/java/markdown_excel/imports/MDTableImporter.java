package markdown_excel.imports;

import markdown_excel.table.*;
import org.jparsec.error.ParserException;

public class MDTableImporter implements Importer<Table> {
    public MDTableImporter(){}

    @Override
    public Table importFromString(String str) throws ParserException {
        return MDParsers.table().parse(str);
    }
}
