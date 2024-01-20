$(document).ready(function () {
    new Choices('#skills', {
        removeItemButton: true,
        maxItemCount: 4
    });

    let select = $(".form-lb-skills");
    let lbLevel = $(".form-lb-level");
    let choicesInput = $(".choices__input");
    let choicesList = $(".choices__list");
    let level = $("#level");


    function labelActive(item, value) {
        let cssName = ["color", "top", "background-color", "padding", "left"];
        for (let i = 0; i < 5; i++) {
            item.css(cssName[i], value[i]);
        }
    }
    function labelNoActive(){
        if (level.val() !== null) {
            labelActive(lbLevel, cssValueActive);
        }
        if (choicesList.html() !== "") {
            labelActive(select, cssValueActive);
        }

    }
    level.on("focus",() => labelActive(lbLevel, cssValueActive))
    level.on("focusout", function () {
        if (level.val() === null) {
            labelActive(lbLevel, cssValueNoActive);
        }
    })

    choicesInput.on("focus", () => labelActive(select, cssValueActive))
    choicesInput.on("focusout",  () => {
        if (choicesList.html() === "") {
            labelActive(select, cssValueNoActive);
        }
    })
    labelNoActive();
});
