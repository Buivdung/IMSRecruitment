$(document).ready(function () {
    new Choices('#interviewId', {
        removeItemButton: true,
        maxItemCount: 4
    });

    let select = $(".form-lb-skills");
    let choicesInput = $(".choices__input");
    let choicesList = $(".choices__list");



    function labelActive(item, value) {
        let cssName = ["color", "top", "background-color", "padding", "left"];
        for (let i = 0; i < 5; i++) {
            item.css(cssName[i], value[i]);
        }
    }

    function labelNoActive() {

        if (choicesList.html() !== "") {
            labelActive(select, cssValueActive);
        }

    }

    choicesInput.on("focus", () => labelActive(select, cssValueActive))
    choicesInput.on("focusout", () => {
        if (choicesList.html() === "") {
            labelActive(select, cssValueNoActive);
        }
    })
    labelNoActive();


});
