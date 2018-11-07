var map;
var User_Phone_Number;
var Device_Mac;
function RETURN_DEVICE() {
    var id = getParameterByName('aefsregsfdssssdtadsryhgnju');
    SSS_WEB_APPLICATION.SSS_SERVICE_LOCAL.RETURN_DEVICE_WITH_USER_ID(id, GET_DEVICE_RESULT);
}
function GET_DEVICE_RESULT(results) {
    Device_Mac = results["Mac_Address"];
    User_Phone_Number = results["Current_Number"];
    GetData();
}
function GetData() {
    document.getElementById("button2").disabled = true;
    SSS_WEB_APPLICATION.SSS_SERVICE_LOCAL.RETURN_DEVICE_COORDINATE_JS(Device_Mac, getResults);
}

function getResults(results) {
    var myLatLng = { lat: parseFloat(results[results.length - 1].Latitude), lng: parseFloat(results[results.length - 1].Longitude) };

    map.panTo(myLatLng);
    map.setZoom(16);
    var marker = new google.maps.Marker({
        position: myLatLng,
        map: map,
        title: 'Hello World!'
    });
}

function initMap() {
    map = new google.maps.Map(document.getElementById('map'), {
        center: { lat: -34.397, lng: 150.644 },
        zoom: 14
    });
}
function RING_DEVICE() {
    document.getElementById("button1").disabled = true;
    document.getElementById("button2").disabled = false;
    SSS_WEB_APPLICATION.SSS_SERVICE_LOCAL.RING_DEVICE("RING_DEVICE", "+15312331112", User_Phone_Number, GET_DATA);
}
function GET_DATA(results) {
    //alert(results);
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
function STOP_RINGING_DEVICE() {
    document.getElementById("button1").disabled = false;
    document.getElementById("button2").disabled = true;
    SSS_WEB_APPLICATION.SSS_SERVICE_LOCAL.RING_DEVICE("STOP_RINGING", "+15312331112", User_Phone_Number, GET_DATA);
}
function GET_DATA(results) {
    //alert(results);
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
setInterval(GetData, 10000);
window.onload = RETURN_DEVICE;