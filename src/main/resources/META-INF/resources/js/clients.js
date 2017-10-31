function setupModal(client) {
    $('#formFrom').val(moment().format("DD/MM/YYYY"));
    $('#formTo').val(moment().format("DD/MM/YYYY"));
    $('#formTo').datepicker({
        format: "dd/mm/yyyy"
    });
    $('#formFrom').datepicker({
        format: "dd/mm/yyyy"
    });

    $('#formMaxResults').val(100);
    $('#customForm').attr('action', '/history/' + client + '/custom');
    console.log(moment().format('zz'))
}

$(document).ready(function () {
    $("#parse").click(function () {
        event.preventDefault();
        $("#parse").button('loading');
        $("#parseModal").modal("hide");
        $("#pleaseWaitDialog").modal("show");

        var DBName = $("#DBName").val();
        var parsingMode = $("#parsingMode").find("option:selected" ).text();
        var filePath = $("#filePath").val();
        var timeZone = $("#timeZone").find("option:selected" ).text();
        var needLog = $("#needLog").is(":checked");

        $.ajax({
            type: "POST",
            url: "/parser/parse",
            enctype: 'multipart/form-data',
            data: {
                DBName: DBName,
                parsingMode: parsingMode,
                filePath: filePath,
                timeZone: timeZone,
                needLog: needLog
            },
            success: function () {
                $("#loading-header").text("Success!")
            },
            error: function () {
                $("#loading-header").text("Error!")
            },
            complete: function () {
                $("#loading-ok-button").removeClass("hidden");
            }
        });
    });
});