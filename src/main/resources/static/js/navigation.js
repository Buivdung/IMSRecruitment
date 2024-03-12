$(document).ready(function () {
    let navigation = $(".navigation");
    let main = $(".main");
    let toggle = $(".toggle");
    let content = $(".content");

    function active() {
        navigation.toggleClass("active");
        main.toggleClass("active");
        content.toggleClass("active");
    }

    toggle.on("click", active);
    let time = setTimeout(function () {
        $(".alerts").hide();
        clearTimeout(time);
    }, 3000)

})

function deleteById(id) {
    let url = "/admin/" + event.target.getAttribute("id") + "/delete/" + id;
    addUrl.empty();
    addUrl.append(`
             <button type="button"  class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <a href="${url}" class="btn btn-primary" data-bs-dismiss="modal">Delete</a>`)
}
let addUrl = $(".add_url");
