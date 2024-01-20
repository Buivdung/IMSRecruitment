let btnSearchPropose = $(".btn-search-propose");
let searchTitle = $(".search-title");
let skillsRandom = ["Java", "Javascript", "C#", "C++", "ReactJs", "Laravel", "PHP"]
let i = 0;
btnSearchPropose.on("click", function () {
    console.log($(this).text());
    $(".search-input").val($(this).text());
})
setInterval(function(){
    if( i < skillsRandom.length){
        searchTitle.empty();
        searchTitle.append(" Tìm kiếm <span>" + skillsRandom[i] + "</span>");
        i++;
    }
    if( i === skillsRandom.length){
        i = 0;
    }
}, 2000)