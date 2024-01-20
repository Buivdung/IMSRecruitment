$(document).ready(function () {
    $("#filter").on("click", function () {
        let address = $("#a").val();
        let level = $("#l").val();
        let param = $("#p").val();
        let href = "./search?pageNumber=1";
        if (param) {
            href += "&param=" + param;
        }
        if (level) {
            href += "&level=" + level;
        }
        if (address) {
            href += "&address=" + address;
        }
        window.location.href = href;
    })
})