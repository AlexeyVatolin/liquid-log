<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="ru.naumen.perfhouse.parser.constants.TableTitle" %>
<%@ page import="ru.naumen.perfhouse.parser.constants.Constant" %>
<%@ page import="ru.naumen.perfhouse.parser.constants.DefaultConstants" %>
<%@ page import="java.util.*" %>
<%@ page import="ru.naumen.perfhouse.parser.constants.SeriesInfo" %>

<html>

<head>
    <title>SD40 Performance indicator</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.5/css/bootstrap.min.css"
          integrity="sha384-AysaV+vQoT3kOAXZkl02PThvDr8HYKPZhNT5h/CXfBThSRXQ6jW5DO2ekP5ViFdi" crossorigin="anonymous"/>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.5/js/bootstrap.min.js"
            integrity="sha384-BLiI7JTZm+JWlgKa0M0kGRpJbF2J8q+qreVrKBC47e3K6BW78kGLrCkeRX6I9RoK"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="/css/style.css"/>
</head>

<body>

<script src="http://code.highcharts.com/highcharts.js"></script>
<%
    List<TableTitle> tableTitleList = ((Constant) request.getAttribute("constant")).getTableTitles();
    List<SeriesInfo> seriesTitleList = ((Constant) request.getAttribute("constant")).getSeriesInfo();
    List<Constant> constantsList = (List<Constant>) request.getAttribute("constantsList");
    String currentParserName = (String) request.getAttribute("currentParserName");
    Constant constant = (Constant) request.getAttribute("constant");
    List<Number[]> tableData = new ArrayList<>();
    int timesTableIndex = -1;
    for (int i = 0; i < tableTitleList.size(); i++) {
        Number dataRow[] = (Number[]) request.getAttribute(tableTitleList.get(i).getDataName());
        if (tableTitleList.get(i).getDataName().equals(DefaultConstants.TIME)) {
            timesTableIndex = i;
        }
        tableData.add(dataRow);
    }
    List<Number[]> seriesData = new ArrayList<>();
    int timesSeriesIndex = -1;
    for (int i = 0; i < seriesTitleList.size(); i++) {
        Number dataRow[] = (Number[]) request.getAttribute(seriesTitleList.get(i).getDataName());
        if (seriesTitleList.get(i).getDataName().equals(DefaultConstants.TIME)) {
            timesSeriesIndex = i;
        }
        seriesData.add(dataRow);
    }

    //Prepare links
    String path = "";
    String custom = "";
    if (request.getAttribute("custom") == null) {
        Object year = request.getAttribute("year");
        Object month = request.getAttribute("month");
        Object day = request.getAttribute("day");

        String countParam = (String) request.getParameter("count");

        String params = "";
        String datePath = "";

        StringBuilder sb = new StringBuilder();


        if (countParam != null) {
            params = sb.append("?count=").append(countParam).toString();
        } else {
            sb.append('/').append(year).append('/').append(month);
            if (!day.toString().equals("0")) {
                sb.append('/').append(day);
            }
            datePath = sb.toString();
        }
        path = datePath + params;
    } else {
        custom = "/custom";
        Object from = request.getAttribute("from");
        Object to = request.getAttribute("to");
        Object maxResults = request.getAttribute("maxResults");

        StringBuilder sb = new StringBuilder();
        path = sb.append("?from=").append(from).append("&to=").append(to).append("&maxResults=").append(maxResults).toString();
    }


%>

<div class="container">
    <br>
    <h1>Performance data for "${client}"</h1>
    <h3><a class="btn btn-success btn-lg" href="/">Client list</a></h3>
    <h4 id="date_range"></h4>
    <p>
        Feel free to hide/show specific data by clicking on chart's legend
    </p>
    <ul class="nav nav-pills">
        <% for (Constant myConstant : constantsList) {
            if (myConstant.getClass().getSimpleName().equals(currentParserName)) {
        %>
        <li class="nav-item"><a class="nav-link active"><%= myConstant.getName() %>
        </a></li>
        <% } else { %>
        <li class="nav-item"><a class="btn btn-outline-primary"
                                href="/history/${client}<%=custom %>/<%= myConstant.getClass().getSimpleName() %><%=path%>">
            <%= myConstant.getName() %>
        </a></li>
        <% } %>
        <% } %>

    </ul>
</div>

<!-- Gc chart -->
<div class="container" id="gc">
    <div id="gc-chart-container" style="height: 600px"></div>
    <div class="scroll-container">
        <table class="table table-fixed header-fixed">
            <thead class="thead-inverse">
            <%
                int firstWidth = 0;
                int nextWidth = 0;
                if (tableTitleList.size() > 12) {
                    firstWidth = 1;
                    nextWidth = 1;
                } else if (12 % tableTitleList.size() == 0) {
                    firstWidth = 12 / tableTitleList.size();
                    nextWidth = 12 / tableTitleList.size();
                } else {
                    firstWidth = 12 - tableTitleList.size() + 1;
                    nextWidth = 1;
                }

            %>
            <th class="col-xs-<%=firstWidth%>"><%=tableTitleList.get(0).getName()%>
            </th>
            <% for (int i = 1; i < tableTitleList.size(); i++) {%>
            <th class="col-xs-<%=nextWidth%>"><%=tableTitleList.get(i).getName()%>
            </th>
            <% } %>
            </thead>
            <tbody>
            <% for (int i = 0; i < tableData.get(0).length; i++) {%>
            <tr class="row">
                <% for (int j = 0; j < tableData.size(); j++) {
                    if (timesTableIndex == j) {
                        if (j == 0) {
                %>
                <td class="col-xs-<%=firstWidth%>" style="text-align:center;">
                    <%= new java.util.Date(tableData.get(j)[i].longValue()).toString() %>
                </td>
                <%
                } else {
                %>
                <td class="col-xs-<%=nextWidth%>">
                    <%= new java.util.Date(tableData.get(j)[i].longValue()).toString() %>
                </td>
                <%
                    }
                } else {
                    if (j == 0) {
                %>
                <td class="col-xs-<%=firstWidth%>" style="text-align:center;">
                    <%= tableData.get(j)[i] %>
                </td>
                <%
                } else {
                %>
                <td class="col-xs-<%=nextWidth%>">
                    <%= tableData.get(j)[i] %>
                </td>
                <%
                        }
                    }
                %>
                <% }%>
            </tr>
            <% }%>
            </tbody>
        </table>
    </div>
</div>
<script type="text/javascript">
    var times = [];
    var otherRows = [];

    <% for(int i = 0; i < seriesData.size() - 1; i++) { %>
    otherRows[<%= i %>] = [];
    <% } %>

    <% for(int i = 0; i < seriesData.get(timesSeriesIndex).length; i++) { %>
    times.push((<%=seriesData.get(timesSeriesIndex)[i]%>));
    <% } %>
    <% int index = 0;
    for(int j = 0; j < seriesData.size(); j++) {
        if (j != timesSeriesIndex) {
            for(int i = 0; i < seriesData.get(timesSeriesIndex).length; i++) { %>
    otherRows[<%= index %>].push([new Date(times[<%= i %>]), <%= seriesData.get(j)[i] %>]);
    <% } %>
    <% index++; %>
    <% } %>
    <% } %>

    debugger;

    document.getElementById('date_range').innerHTML += 'From: ' + new Date(times[times.length - 1]) +
        '<br/>To:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' + new Date(times[0]);

    var seriesVisible = [];
    for (i = 0; i < otherRows.length; i++) {
        if (localStorage.getItem('series' + i) == null) {
            localStorage.setItem('series' + i, (i === 0).toString());
        }
    }

    for (i = 0; i < otherRows.length; i++) {
        seriesVisible.push(localStorage.getItem('series' + i) === 'true');
    }

    Highcharts.setOptions({
        global: {
            useUTC: false
        }
    });

    var myChart = Highcharts.chart('gc-chart-container', {
        chart: {
            zoomType: 'x,y'
        },

        title: {
            text: '<%= constant.getChartTitle() %>'
        },

        tooltip: {
            formatter: function () {
                var index = this.point.index;
                var date = new Date(times[index]);
                return Highcharts.dateFormat('%a %d %b %H:%M:%S', date)
                    + '<br/> <b>' + this.series.name + '</b> ' + this.y + ' ' + this.series.options.unit + '<br/>'
            }
        },

        xAxis: {
            labels: {
                formatter: function (obj) {
                    return Highcharts.dateFormat('%a %d %b %H:%M:%S', new Date(times[this.value]));
                }
            },
            reversed: true
        },

        yAxis: {
            title: {
                text: '<%= constant.getYAxisTitle() %>'
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        plotOptions: {
            line: {
                marker: {
                    enabled: false
                },
                events: {
                    legendItemClick: function (event) {
                        var series = this.yAxis.series;
                        seriesLen = series.length;

                        var index = event.target.index;
                        localStorage.setItem('series' + index, !series[index].visible);
                    }
                }
            }
        },
        series: [
            <% index = 0;
            for (int i = 0; i < seriesTitleList.size(); i++) {
                if (i != timesSeriesIndex) { %>
            {
                name: '<%= seriesTitleList.get(i).getName() %>',
                data: otherRows[<%= index %>],
                visible: seriesVisible[<%= index %>],
                unit: '<%= seriesTitleList.get(i).getUnit() %>',
                turboThreshold: 10000
            },
            <% index++;
            }
            %>
            <% } %>
        ]
    });


</script>

</body>

</html>