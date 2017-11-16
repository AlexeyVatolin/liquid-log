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
    $("#myForm").submit(function () {
        event.preventDefault();
        $("#parseModal").modal("hide");
        $("#pleaseWaitDialog").modal("show");
        let formData = new FormData($(this)[0]);
        formData.append("needLog", $("needLog").is(":checked"));

        $.ajax({
            type: "POST",
            url: "/parser/parse",
            enctype: 'multipart/form-data',
            data: formData,
            contentType: false,
            processData: false,
            success: function () {
                $("#loading-header").text("Success!");
                $("#loading-ok-button").click(function () {
                    window.location.reload();
                });
            },
            error: function (response) {
                $("#loading-header").text("Error!");
                $("#returned-message").html(response.responseJSON.error + "</br>" + response.responseJSON.exception);
            },
            complete: function () {
                $("#loading-ok-button").removeClass("hidden");
            }
        });
    });

});

