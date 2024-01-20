let navbarLeft = $(".navbar-left");
let btnMenuItems = $(".menu-items-left ul li");
let itemTitle = $(".item-title");
let menuItems = $(".menu-items-left");
let levels = $(".levels");
let skills = $(".skills");
let address = $(".address")
$(".btn-close").on("click", function () {
    navbarLeft.css("left", "-300px");
})
let time = setTimeout(function () {
    $(".alerts").hide();
    clearTimeout(time);
}, 5000)
$(".navbar-toggle").on("click", function () {
    navbarLeft.css("left", "0px");
})

btnMenuItems.on("click", function () {
    let option = $(this).attr("id");
    let items = [];
    $(".item-title").html('<ion-icon name="chevron-back-outline"></ion-icon>' + $(this).text());
    if (option === 'param') {
        for (let i = 0; i < skills.length; i++) {
            items.push($(skills[i]).text());
        }
    } else if (option === 'level') {
        for (let i = 0; i < levels.length; i++) {
            items.push($(levels[i]).text());
        }
    } else if (option === 'address') {
        for (let i = 0; i < address.length; i++) {
            items.push($(address[i]).text());
        }
    } else if (option === 'lo') {
        window.location.href = "./logout"
    } else {
        window.location.href = "./login"
    }
    items.forEach(e => {
        $(".menu-items-left .item").append("<li>" +
            " <a href='./search?" + option + "=" + e + "'" +
            "style='text-decoration: none'>" + e + " <a>" +
            "</li>");
    })
    menuItems.first().hide();
    menuItems.first().next().show();
})
itemTitle.on("click", function () {
    menuItems.first().show();
    menuItems.first().next().hide();
    $(".menu-items-left .item").empty();
})