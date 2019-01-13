# markdown_excel

## [Link to generated API documentation](https://fkoep.github.io/MarkdownExcel/)

## 1. Usage

```sh
# compile (generates one .jar which bundles all dependencies)
gradle shadowJar

# run unit-tests
gradle test

# generate API documentation
gradle javadoc
```

### Examples

Examples are located in the [examples/](./examples/) directory.  Execute them like this:

```sh
jshell examples/TableMaths.java
jshell examples/ImportExport.java
jshell examples/DiagonalCursor.java
jshell examples/Sorting.java
```

Then inspect the output together with the code.

## 2. Working with documents

See markdown_excel.doc;</br>

Document's are simply lists of toplevel markdown elements (BlockElement).
You get a few methods to work with them, but be aware currently we only really 
support table Markdown elements (TableElement), all other kinds of markdown
elements are being represented by RawBlockElement.

```java
Document doc = ...; // you'll usually use SimpleDocument

doc.addElement(idx, elem); // inserts a toplevel element
doc.addElement(elem);      // appends a toplevel element
doc.deleteElement(elem);   // deletes a toplevel element

Stream<BlockElement> elems = doc.stream();  // stream of toplevel elements
BlockElement elem = doc.getNthElement(idx); // gets the nth element of the stream
TableElement tbl = doc.getNthTable(idx);    // gets the nth element which is a TableElement
```

### 3. Working with tables

Tables are two-dimensional (always rectangular) collections of values (currently,
always `String`'s).

"Columns" are vertical, numbered from 0 to `getWidth() - 1`.

"Rows" are horizontal, numbered from 0 to `getHeight() - 1`.
The first row is considered the "head row". You can use it to associate a name to
each column (work with `setCell(row, 0)`, `getColumnIdx(name)`, `getColumnName(idx)`), 
but you don't have to.

#### 3.1. Basic table manipulation

See markdown_excel.table.Table</br>
See markdown_excel.table.SimpleTable</br>

```java
Table tbl = ...; // you'll usually use SimpleTable

int width = tbl.getWidth();   // returns the amount of columns
int height = tbl.getHeight(); // returns the amount of rows

String val = tbl.getCell(columnIdx, rowIdx); // gets the value of a cell
tbl.setCell(columnIdx, rowIdx, value);       // sets the value of a cell

String colName = tbl.getColumnName(colIdx); // gets the name of a column from its index
int colIdx = tbl.getColumnIdx(colName);     // gets the index of a colum from its name

tbl.addRow(idx);          // inserts a row
tbl.addRow();             // appends a row
tbl.swapRows(idxA, idxB); // swaps two rows
tbl.deleteRow(idx);       // deletes a row

tbl.addColumn(idx);          // inserts a column
tbl.addColumn();             // appends a column
tbl.swapColumns(idxA, idxB); // swaps two columns
tbl.deleteColumn(idx);       // deletes a columns
```

#### 3.2. Working with cursors

See markdown_excel.table.IndexPair</br>
See markdown_excel.table.Table.rowCursor()</br>
See markdown_excel.table.Table.columnCursor()</br>

"Cursors", as we dub them, are streams of `IndexPair`'s. An index pair is simply a
pair of a column index and a row index.

When you create a cursors from a table, it's "traverses" it: Every index pair
of the stream corresponds to a cell, but not all cells may be covered 
(you can traverse subsections).

For example, `rowCursor()` traverses a 3x3 table in this order:

|0|1|2|
|-|-|-|
|3|4|5|
|6|7|8|

Or: `[0|0], [1|0], [2|0], [0|1], [1|1], [2|1], [0|2], [1|2], [2|2]`

It's sibling `columnCursor()` would traverses it in this order:

|0|3|6|
|-|-|-|
|1|4|7|
|2|5|8|

**[TODO]**: document here how cursor-predicates work

For implementing cursors yourself, see [examples/DiagonalCursor](./examples/DiagonalCursor.java) and
examples/SortingCursor.

---

Table provides a few methods which take cursors directly.

`Table.stream(cursor)` returns a stream of traversed cell values.

`Table.fill(cursor, values)` traverses the table while taking off values, setting
each cell to the taken value.

`Table.apply(cursor, op)` traverses the table and calls `Table.applyCell(op(col, row))` for each cell.

`Table.setAll(cursor, supply)` traverses the table and calls `Table.setCell(supply(col, row))` for each cell.

### 4. Working with importers

See package markdown_excel.imports</br>
See markdown_excel.imports.Importer</br>

```java
Importer<T> importer = ...; // you'll use Exporter<Table> or Exporter<Document>

T obj = importer.importFromString(str); // import object from a string
T obj = importer.importFromFile(path);  // ^directly loads string from file
```

### 5. Working with exporters

See package markdown_excel.exports</br>
See markdown_excel.imports.Importer</br>

```java
Exporter<T> exporter = ...; // you'll use Exporter<Table> or Exporter<Document>

String str = exporter.exportToString(obj); // export object to a string
exporter.exportToFile(path);               // ^directly saves string to file
```
