let date = $(".date");
date.on("focusin", function (e) {
    e.target.type = "date"
})
date.on("focusout", function (e) {
    e.target.type = ""
})
let datetime = $(".datetime");
datetime.on("focusin", function (e) {
    e.target.type = "datetime-local"
})
datetime.on("focusout", function (e) {
    e.target.type = ""
})
let cssValueActive = ["#00BCD4FF", "0", "white", "0 5px", "17px"];
let cssValueNoActive = ["var(--black2)", "50%", "transparent", "0", "21px"];
$(".choices__inner").append('<i class="fa-solid fa-angle-down mt-2 me-1 float-end"' +
    ' style="font-size: 12px ; color:  var(--black2)"></i>')
let select = $("select");
let input = $(".form-input");

function labelActive(item, value) {
    let cssName = ["color", "top", "background-color", "padding", "left"];
    for (let i = 0; i < 5; i++) {
        item.css(cssName[i], value[i]);
    }
}
function labelNoActive() {
    for (let i = 0; i < select.length; i++) {
        if ($(select[i]).val()) {
            labelActive($(select[i]).next(), cssValueActive);
        }else {
            labelActive($(select[i]).next(), cssValueNoActive);
        }
    }
    for (let i = 0; i < input.length; i++) {
        if ($(input[i]).val()) {
            labelActive($(input[i]).next(), cssValueActive);
        }
    }
}
select.focusout(function () {
    if ($(this).val()) return;
    labelActive($(this).next(), cssValueNoActive);
})
select.focus(function () {
    labelActive($(this).next(), cssValueActive)
})
labelNoActive();

