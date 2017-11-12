<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<html>

<head>
    <title>SD40 Performance indicator</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script src="/js/jquery-3.1.1.min.js"></script>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.5/css/bootstrap.min.css"
          integrity="sha384-AysaV+vQoT3kOAXZkl02PThvDr8HYKPZhNT5h/CXfBThSRXQ6jW5DO2ekP5ViFdi" crossorigin="anonymous"/>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.5/js/bootstrap.min.js"
            integrity="sha384-BLiI7JTZm+JWlgKa0M0kGRpJbF2J8q+qreVrKBC47e3K6BW78kGLrCkeRX6I9RoK"
            crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.2.0/js/tether.min.js"
            integrity="sha384-Plbmg8JY28KFelvJVai01l8WyZzrYWG825m+cZ0eDDS1f7d/js6ikvy1+X+guPIB"
            crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.17.1/moment.min.js"></script>
    <link rel="stylesheet" href="/css/style.css"/>
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker3.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.1.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="/css/clients.css"/>
</head>

<body>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
<script src="/js/clients.js"></script>

<div class="container">
    <br>
    <div class="alert alert-info">
        <h3><strong>Attention!</strong><br>All requests for stored data are made with UTC time.<br>Requested data will
            be displayed in your browsers timezone.</h3>
    </div>
    <br>
    <div class="row" id="header-container">
        <div class="col-md-8">
            <h1>Client list</h1>
        </div>
        <div class="col-md-4">
            <button type="button" class="btn btn-success" id="open-parser-window" data-toggle="modal"
                    data-target="#parseModal">Parse
            </button>
        </div>
    </div>
    <table class="table table-striped table-fixed"> <!-- table-bordered  -->
        <thead class="thead-inverse">
        <th class="col-xs-6">Name</th>
        <th class="col-xs-6">Link</th>
        </thead>
        <tbody>
        <% for (String client : (List<String>) request.getAttribute("clients")) { %>
        <tr>
            <td class="col-xs-6">
                <h2><span><%= client %></span></h2>
            </td>
            <td class="col-xs-6">
                <a class="btn btn-outline-primary"
                   href='<%= ((Map)request.getAttribute("prevMonthLinks")).get(client) %>'>Previous Month</a>
                <a class="btn btn-outline-primary" href='<%= ((Map)request.getAttribute("monthlinks")).get(client) %>'>Month</a>
                <a class="btn btn-outline-primary"
                   href='<%= ((Map)request.getAttribute("last2016links")).get(client) %>'>Last 7 days</a>
                <a class="btn btn-outline-primary"
                   href='<%= ((Map)request.getAttribute("last864links")).get(client) %>'>Last 3 days</a>
                <a class="btn btn-outline-primary" href='<%= ((Map)request.getAttribute("links")).get(client) %>'>Yesterday</a>
                <button type="button" class="btn btn-success" data-toggle="modal" data-target="#customModal"
                        onclick="setupModal('<%=client %>')">Custom request
                </button>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>
</div>

<div class="modal fade" id="customModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class=modal-dialog role="dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="myModalLabel">Select dates and max results</h4>
            </div>
            <form id="customForm">
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="formFrom">From</label>
                                <input type="text" class="form-control" id="formFrom" name="from">
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="formTo">To</label>
                                <input type="text" class="form-control" id="formTo" name="to">
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="formMaxResults">Max results</label>
                        <input class="form-control" type="number" value="42" id="formMaxResults" name="maxResults">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-success">Request</button>
                    <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                </div>
            </form>
        </div>
    </div>
</div>
<div class="modal fade" id="parseModal" tabindex="-1" role="dialog" aria-labelledby="myTitle" aria-hidden="true">
    <div class=modal-dialog role="dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="myTitle">Select parameters for parser</h4>
            </div>
            <form id="myForm">
                <div class="modal-body">
                    <div class="form-group">
                        <label for="DBName">Database name</label>
                        <input type="text" class="form-control" id="DBName" name="DBName">
                    </div>
                    <div class="form-group">
                        <label for="parsingMode">Parsing mode</label>
                        <select id="parsingMode" class="form-control" name="parsingMode">
                            <option>sdng</option>
                            <option>gc</option>
                            <option>top</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="file">Choose log file</label>
                        <input class="form-control" type="file" id="file" name="file">
                    </div>
                    <div class="form-group">
                        <label for="timeZone">Time zone</label>
                        <select id="timeZone" class="form-control" name="timeZone">
                            <option>GMT-12:00</option>
                            <option>GMT-11:00</option>
                            <option>GMT-10:00</option>
                            <option>GMT-10:00</option>
                            <option>GMT-9:30</option>
                            <option>GMT-9:00</option>
                            <option>GMT-8:00</option>
                            <option>GMT-7:00</option>
                            <option>GMT-6:00</option>
                            <option>GMT-5:00</option>
                            <option>GMT-4:00</option>
                            <option>GMT-3:30</option>
                            <option>GMT-3:00</option>
                            <option>GMT-2:00</option>
                            <option>GMT-1:00</option>
                            <option>GMT 0:00</option>
                            <option>GMT+1:00</option>
                            <option>GMT+2:00</option>
                            <option>GMT+3:00</option>
                            <option>GMT+3:30</option>
                            <option>GMT+4:00</option>
                            <option>GMT+4:30</option>
                            <option>GMT+5:00</option>
                            <option>GMT+5:30</option>
                            <option>GMT+5:45</option>
                            <option>GMT+6:00</option>
                            <option>GMT+6:30</option>
                            <option>GMT+7:00</option>
                            <option>GMT+8:00</option>
                            <option>GMT+8:30</option>
                            <option>GMT+8:45</option>
                            <option>GMT+9:00</option>
                            <option>GMT+9:30</option>
                            <option>GMT+10:00</option>
                            <option>GMT+10:30</option>
                            <option>GMT+11:00</option>
                            <option>GMT+12:00</option>
                            <option>GMT+12:45</option>
                            <option>GMT+13:00</option>
                            <option>GMT+14:00</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <input type="checkbox" id="needLog" name="needLog">
                        <label for="needLog">Print log</label>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" id="parse" class="btn btn-success">Parse</button>
                    <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                </div>
            </form>
        </div>
    </div>
</div>


<div class="modal fade" id="pleaseWaitDialog" tabindex="-1" data-backdrop="static" role="dialog"
     aria-labelledby="loading-header" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h1 id="loading-header">Processing...</h1>
            </div>
            <div class="modal-body">
                <code id="returned-message">

                </code>
            </div>
            <div class="modal-footer">
                <button type="button" id="loading-ok-button" data-dismiss="modal" class="btn hidden">OK
                </button>
            </div>
        </div>
    </div>
</div>

</body>

</html>