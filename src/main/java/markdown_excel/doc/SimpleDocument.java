package markdown_excel.doc;

import java.util.stream.*;
import java.util.*;

/** A simple implementation of SimpleDocument, backed by ArrayList.
 */
// TODO override toString()?
public class SimpleDocument implements Document {
    private ArrayList<BlockElement> elems;

    /** Constructs a SimpleDocument from a list of BlockElement's.
     */
    public SimpleDocument(ArrayList<BlockElement> elems){
        this.elems = elems;
    }

    private static ArrayList<BlockElement> collectElements(Stream<BlockElement> elems){
        return elems.collect(Collectors.toCollection(ArrayList::new));
    }

    /** Constructs a SimpleDocument from a stream of BlockElement's.
     */
    public SimpleDocument(Stream<BlockElement> elems){
        this(collectElements(elems));
    }

    /** Constructs an empty SimpleDocument
     */
    public SimpleDocument(){
        this(new ArrayList<>());
    }

    /** Constructs a SimpleDocument from the content of another Document.
     */
    public SimpleDocument(Document doc){
        this(doc.streamElements());
    }

    @Override
    public Stream<BlockElement> streamElements(){
        return elems.stream();
    }

    @Override
    public void addElement(int idx, BlockElement elem){
        elems.add(idx, elem);
    }

    @Override
    public void deleteElement(int idx){
        elems.remove(idx);
    }
}
