var map;
function GetData() {
    document.getElementById("button2").disabled = true;
    SSS_WEB_APPLICATION.SSS_SERVICE_LOCAL.RETURN_DEVICE_COORDINATE_JS("7C:2E:DD:F5:6C:C4", getResults);
}

function getResults(results) {
    var myLatLng = { lat: parseFloat(results[results.length - 1].Latitude), lng: parseFloat(results[results.length - 1].Longitude) };
   
    // using global variable:
    map.panTo(myLatLng);
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
window.onload = GetData;