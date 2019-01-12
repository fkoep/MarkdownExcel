package markdown_excel.exports;

import markdown_excel.table.*;
import org.apache.commons.text.StringEscapeUtils;
import java.util.stream.*;
import java.util.*;

/** Google chart table exporter. */
public class GChartTableExporter implements Exporter<Table> {
    public GChartTableExporter(){}

    @Override
    public String exportToString(Table tbl){
        StringBuilder builder = new StringBuilder();
        builder.append(String.join(
                System.lineSeparator(),
                "<html>",
                " <head>",
                "   <script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>",
                "   <script type=\"text/javascript\">",
                "     google.charts.load('current', {'packages':['table']});",
                "     google.charts.setOnLoadCallback(drawTable);",
                "     function drawTable() {",
                "       var data = new google.visualization.DataTable();"));

        tbl.stream(tbl.rowCursor((_col, row) -> row == 0)).forEach(val -> {
            builder.append("data.addColumn('string', \"");
            builder.append(StringEscapeUtils.escapeJson(val));
            builder.append("\");\n");
        });

        builder.append("data.addRows([");
        IntStream.range(1, tbl.getHeight()).forEach(row -> {
            builder.append("[");
            builder.append(tbl.stream(tbl.rowCursor((_col, row2) -> row == row2))
                    .map(StringEscapeUtils::escapeJson)
                    .map(esc -> "\"" + esc + "\"")
                    .collect(Collectors.joining(",")));
            builder.append("],\n");
        });
        builder.append("]);\n");

        builder.append(String.join(
                System.lineSeparator(),
                "      var table = new google.visualization.Table(document.getElementById('table_div'));",
                "      table.draw(data, {showRowNumber: true, width: '100%', height: '100%'});",
                "    }",
                "    </script>",
                "  </head>",
                "  <body>",
                "    <div id=\"table_div\"></div>",
                "  </body>",
                "</html>"));

        return new String(builder);
    }
}
