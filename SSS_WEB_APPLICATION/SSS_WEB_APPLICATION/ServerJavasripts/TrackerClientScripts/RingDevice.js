function RING_DEVICE() {
    document.getElementById("button1").disabled = true;
    document.getElementById("button2").disabled = false;
    SSS_WEB_APPLICATION.SSS_SERVICE_LOCAL.RING_DEVICE("RING_DEVICE", "+15312331112", "+27796559782", GET_DATA);
}
function GET_DATA(results) {
    alert(results);
}

