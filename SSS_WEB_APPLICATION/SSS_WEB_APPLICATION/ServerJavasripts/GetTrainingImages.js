var InitialArray;
function LOGIN() {
    var id = getParameterByName('aefsregsfdssssdtadsryhgnju');
    SSS_WEB_APPLICATION.SSS_SERVICE_LOCAL.RETURN_USER_WITH_ID(id, GET_LOGIN_RESULT);
}
function GET_LOGIN_RESULT(results) {
    TRAINING_IMAGES(results["Id"], results["User_Name"]);
}
function TRAINING_IMAGES(id,username) {
    SSS_WEB_APPLICATION.SSS_SERVICE_LOCAL.RETURN_TRAINING_IMAGES(id, username, GET_DATA);
}
function GET_DATA(results) {
    InitialArray = results;
    var images = "";
    var index;
    for (index = 0; index < results.length; ++index) {
        //images += "<div style='width: 200px;height:300px'>";
        images +=   "<img style='width:200px;height:200px' src='" + results[index]["Image_Path"] + "' />";
        //images += "</div>";
    }
    document.getElementById("Images").innerHTML = images;
    UPDATE_IMAGES();
}
function UPDATE_IMAGES() {
    SSS_WEB_APPLICATION.SSS_SERVICE_LOCAL.RETURN_TRAINING_IMAGES("20", "isaiah", GET_DATA_FOR_UPDATE);
}
function GET_DATA_FOR_UPDATE(results) {
    if (InitialArray.length < results.length) {
        var images = "";
       // images += "<div style='width: 200px;height:300px'>";
        images +=  "<img style='width:200px;height:200px' src='" + results[results.length - 1]["Image_Path"] + "' />";
        ///images += "</div>";
        document.getElementById("Images").innerHTML += images;
        InitialArray = results;
    }
}
function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}
setInterval(UPDATE_IMAGES, 3000);
window.onload = LOGIN;