package markdown_excel.doc;

/* Raw data for a toplevel Markdown element which we don't support (yet). */
public class RawBlockElement implements BlockElement {
    private final String raw;

    public RawBlockElement(String raw){
        this.raw = raw;
    }

    public String getRaw(){ return raw; }
}
