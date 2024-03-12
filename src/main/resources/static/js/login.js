$(document).ready(function () {
    let forgot = $(".forgot");
    let login = $(".login");
    let btnPassword = $(".btn-password");
    let btnHidePassword = $(".btn-hidePassword");
    let password = $("#password");
    let candidate = $(".candidate");
    let recruiter = $(".recruiter");
    let btnRecruiter = $(".btn-recruiter");
    let btnCandidate = $(".btn-candidate")
    $(".btn-login").on("click", function () {
        forgot.hide();
        login.show();
    })
    $(".btn-forgot").on("click", function () {
        forgot.show();
        login.hide();
    })
    btnPassword.on("click", function () {
        password.attr("type", "text");
        btnPassword.hide();
        btnHidePassword.show();
    })
    btnHidePassword.on("click", function () {
        password.attr("type", "password");
        btnPassword.show();
        btnHidePassword.hide();
    })

    function cssRecruiter() {
        btnRecruiter.css("border-bottom", "4px solid #dd3f24");
        // btnRecruiter.css("font-weight", "bold");
        btnCandidate.css("border-bottom", "1px solid black");
        recruiter.show();
        candidate.hide();
    }

    function cssCandidate() {
        btnCandidate.css("border-bottom", "4px solid #dd3f24");
        // btnCandidate.css("font-weight", "bold");
        btnRecruiter.css("border-bottom", "1px solid black");
        candidate.show();
        recruiter.hide();
    }

    btnRecruiter.on("click", cssRecruiter)
    btnCandidate.on("click", cssCandidate)
    recruiter.hide();
    btnHidePassword.hide();
    if ($(".message").text() === "") {
        cssCandidate();
    } else {
        cssRecruiter()
    }
    forgot.hide();

    let time = setTimeout(function () {
        $(".alerts").hide();
        clearTimeout(time);
    }, 5000)

})
