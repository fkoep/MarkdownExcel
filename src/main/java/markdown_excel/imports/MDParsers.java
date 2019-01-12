package markdown_excel.imports;

import markdown_excel.table.*;
import markdown_excel.doc.*;
import org.jparsec.*;
import java.util.*;

class MDParsers {
    // Zero or more spaces
    private static Parser<Void> SPACES0 = Scanners.among(" \t").skipMany();
    // End Of Line
    private static Parser<Void> EOL = Parsers.or(Scanners.string("\r\n"), Scanners.isChar('\n'), Parsers.EOF.skipTimes(1));
    private static Parser<Void> SPACES0_EOL = Parsers.sequence(SPACES0, EOL);
    private static Parser<Void> VBAR = Scanners.isChar('|');
    private static Parser<Void> HBAR = Scanners.isChar('-');
    // End Of Block(Element)
    private static Parser<Void> EOB = Parsers.sequence(SPACES0_EOL, SPACES0_EOL);

    /* FIXME unescape content... there's no unescapeMarkdown() in StringEscapeUtils */
    static Parser<String> value() {
        return Scanners.notAmong("|\r\n").many1()
            .source()
            .optional(Table.EMPTY_VALUE)
            .map(v -> v);
    }
    static Parser<ArrayList<String>> row() {
        return VBAR.next(value().sepEndBy1(Parsers.sequence(VBAR)))
            .followedBy(SPACES0_EOL)
            .map(ArrayList::new);
    }

    static Parser<Void> sepRow() {
        // TODO best way to do this? can't use sepEndBy1... (because List<Void>)
        return Parsers.sequence(VBAR.followedBy(HBAR).skipMany1(), VBAR.skipMany(), SPACES0_EOL);
    }
    static Parser<Table> table() {
        return Parsers.sequence(
                row().followedBy(sepRow()),
                row().many().map(ArrayList::new),
                (header, rest) -> { 
                    rest.add(0, header);
                    return new SimpleTable(rest);
                });
    }

    static Parser<RawBlockElement> rawBlockElement() {
        return Scanners.ANY_CHAR.until(EOB).source().map(RawBlockElement::new);
    }
    static Parser<TableElement> tableElement() {
        return table().map(TableElement::new);
    }
    static Parser<Document> document() {
        return Parsers.or(tableElement().map(e -> e), rawBlockElement().map(e -> e))
            .sepEndBy(EOB)
            .map(ArrayList::new)
            .map(SimpleDocument::new);   
    }
}
