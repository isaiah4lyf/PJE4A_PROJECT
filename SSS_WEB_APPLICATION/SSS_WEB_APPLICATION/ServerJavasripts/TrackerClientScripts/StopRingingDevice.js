function STOP_RINGING_DEVICE() {
    document.getElementById("button1").disabled = false;
    document.getElementById("button2").disabled = true;
    SSS_WEB_APPLICATION.SSS_SERVICE_LOCAL.RING_DEVICE("STOP_RINGING", "+15312331112", "+27796559782", GET_DATA);
}
function GET_DATA(results) {
    alert(results);
}