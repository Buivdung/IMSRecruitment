
$(document).ready(function () {
    let lbLevel = $(".form-lb-level");
    let job = $("#jobId");
    let formCv = $(".form-cv");
    let labelCv = $(".lb-cv");
    let cv = $("#cv");
    // let cssValueActive = ["#00BCD4FF", "0", "white", "0 5px", "17px"];
    // let cssValueNoActive = ["var(--black2)", "50%", "transparent", "0", "21px"];
    function labelActive(item, value) {
        let cssName = ["color", "top", "background-color", "padding", "left"];
        for (let i = 0; i < 5; i++) {
            item.css(cssName[i], value[i]);
        }
    }
    function labelNoActive(){
        if (job.val() !== null) {
            labelActive(lbLevel, cssValueActive);
        }
        if (formCv.html() !== "") {
            labelActive(labelCv, cssValueActive);
        }
        if (formCv.html() === "") {
            labelActive(labelCv, cssValueNoActive);
        }
    }
    job.on("focus",() => labelActive(lbLevel, cssValueActive))
    job.on("focusout", function () {
        if (job.val() === null) {
            labelActive(lbLevel, cssValueNoActive);
        }
    })
    cv.on("change",function () {
        let fileName = this.files[0].name;
        formCv.text(fileName);
        labelActive(labelCv, cssValueActive);
    })
    labelNoActive();
});

