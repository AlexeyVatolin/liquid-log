<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="ru.naumen.perfhouse.parser.constants.TableTitle" %>
<%@ page import="ru.naumen.perfhouse.parser.constants.ParserDataForGUI" %>
<%@ page import="ru.naumen.perfhouse.parser.constants.DefaultConstants" %>
<%@ page import="java.util.*" %>
<%@ page import="ru.naumen.perfhouse.parser.constants.SeriesInfo" %>
<%@ page import="static ru.naumen.perfhouse.controllers.utils.ArrayUtils.getArrayString" %>
<%@ page import="static ru.naumen.perfhouse.controllers.utils.ArrayUtils.getTwoValuesArrayString" %>

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
    ParserDataForGUI parserDataForGUI = (ParserDataForGUI) request.getAttribute("parserDataForGUI");
    List<TableTitle> tableTitleList = parserDataForGUI.getTableTitles();
    List<SeriesInfo> seriesTitleList = parserDataForGUI.getSeriesInfo();
    List<ParserDataForGUI> parserDataForGUIList = (List<ParserDataForGUI>) request.getAttribute("parserDataForGUIList");
    String currentParserName = (String) request.getAttribute("currentParserName");

    //Заполнением массива времени. Рассчитывается, что для каждого пасера в данных должно приходить время
    List<Date> datesList = new ArrayList<>();
    for(Number number:(Number[]) request.getAttribute(DefaultConstants.TIME))
    {
        datesList.add(new Date(number.longValue()));
    }

    List<Number[]> tableData = new ArrayList<>();
    for (TableTitle aTableTitle : tableTitleList) {
        Number dataRow[] = (Number[]) request.getAttribute(aTableTitle.getDataName());
        tableData.add(dataRow);
    }
    List<Number[]> seriesData = new ArrayList<>();
    for (SeriesInfo aSeriesTitle : seriesTitleList) {
        Number dataRow[] = (Number[]) request.getAttribute(aSeriesTitle.getDataName());
        seriesData.add(dataRow);
    }

    //Prepare links
    String path = "";
    String custom = "";
    if (request.getAttribute("custom") == null) {
        Object year = request.getAttribute("year");
        Object month = request.getAttribute("month");
        Object day = request.getAttribute("day");

        String countParam = request.getParameter("count");

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
        <!-- Создание кнопок, по которым можно перейти к другим парсерам -->
        <% for (ParserDataForGUI myParserDataForGUI : parserDataForGUIList) {
            if (myParserDataForGUI.getClass().getSimpleName().equals(currentParserName)) {
        %>
        <li class="nav-item"><a class="nav-link active"><%= myParserDataForGUI.getName()%>
        </a></li>
        <% } else { %>
        <li class="nav-item"><a class="btn btn-outline-primary"
                                href="/history/${client}<%=custom %>/<%= myParserDataForGUI.getClass().getSimpleName() %><%=path%>">
            <%= myParserDataForGUI.getName() %>
        </a></li>
        <% } %>
        <% } %>

    </ul>
</div>

<div class="container" id="gc">
    <div id="gc-chart-container" style="height: 600px"></div>
    <div class="scroll-container">
        <table class="table table-fixed header-fixed">
            <thead class="thead-inverse">
            <%
                //Вычисление оптимального распределения колонок таблицы под графиком
                int firstWidth = 0;
                int nextWidth = 0;
                int columnsCount = tableTitleList.size() + 1;
                if (tableTitleList.size() > 12) {
                    firstWidth = 1;
                    nextWidth = 1;
                } else if (12 % columnsCount == 0) {
                    firstWidth = 12 / columnsCount;
                    nextWidth = 12 / columnsCount;
                } else {
                    firstWidth = 12 - columnsCount + 1;
                    nextWidth = 1;
                }

            %>
            <%--Заполнение шапки таблицы--%>
            <th class="col-xs-<%=firstWidth%>">Time</th>
            <% for (int i = 0; i < tableTitleList.size(); i++) {%>
            <th class="col-xs-<%=nextWidth%>"><%=tableTitleList.get(i).getName()%></th>
            <% } %>
            </thead>
            <tbody>
            <%--Заполнение тела таблицы--%>
            <% for (int i = 0; i < tableData.get(0).length; i++) {%>
            <tr class="row">
                <td class="col-xs-<%=firstWidth%>" style="text-align:center;">
                    <%= datesList.get(i).toString() %>
                </td>
                <% for (Number[] aTableData : tableData) { %>
                <td class="col-xs-<%=nextWidth%>" style="text-align:center;">
                    <%= aTableData[i] %>
                </td>
                <% }%>
            </tr>
            <% }%>
            </tbody>
        </table>
    </div>
</div>
<script type="text/javascript">

    // Передача массивов в JS
    var times = <%= getArrayString(datesList) %>;
    var otherRows = [];

    <% for(int i = 0; i < seriesData.size(); i++) { %>
    otherRows[<%= i %>] = <%= getTwoValuesArrayString(datesList, seriesData.get(i))%>;
    <% } %>

    debugger;

    document.getElementById('date_range').innerHTML += 'From: ' + times[times.length - 1] +
        '<br/>To:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' + times[0];

    //Заполнение локального хранилища значениями видимости рядов графика
    for (i = 0; i < otherRows.length; i++) {
        if (localStorage.getItem('series' + i) == null) {
            localStorage.setItem('series' + i, (i === 0).toString());
        }
    }
    var seriesVisible = [];
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
            text: '<%= parserDataForGUI.getChartTitle() %>'
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
                text: '<%= parserDataForGUI.getYAxisTitle() %>'
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
        //Создание рядов данных на графике
        series: [
            <% for (int i = 0; i < seriesTitleList.size(); i++) { %>
            {
                name: '<%= seriesTitleList.get(i).getName() %>',
                data: otherRows[<%= i %>],
                visible: seriesVisible[<%= i %>],
                unit: '<%= seriesTitleList.get(i).getUnit() %>',
                turboThreshold: 10000
            },
            <% } %>
        ]
    });

</script>

</body>

</html>