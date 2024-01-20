$(document).ready(function () {


    let level = $("#level");
    let jobTitle = $("#jobTitle");
    let interviewer = $("#interviewer");
    let notes = $("#noteInterviewer");
    $("#candidateId").on("change", function () {
        $.get("/admin/offer/getResultCandidate/" + $(this).val()).done(function (data) {
            interviewer.empty();
            level.val(data.level);
            jobTitle.val(data.jobTitle);
            notes.text(data.note);
            for (let i = 0; i < data.interviewer.length; i++) {
                interviewer.append(`<span style="border: 1px solid grey; 
                                                padding: 0 3px;
                                                border-radius: 4px;
                                                margin-right: 3px;
                                                background-color: grey">
                                    ${data.interviewer[i]}
                                    </span>`)
            }
        })
    })

})
function approveStatus(id){
    let notes = $("#notes").val();
    $.get(
        "/admin/offer/approve/accepted/"+id + "?notes=" + notes
    ).done(function (){
        window.location.href = "/admin/offer/"+ id
    })
}

function rejectStatus(id){
    let notes = $("#notes").val();
    $.get(
        "/admin/offer/approve/rejected/"+id + "?notes=" + notes
    ).done(function (){
        window.location.href = "/admin/offer/"+ id
    })
}
