var dane = [];
var nazwy = [];
var kod = [];
$(document).ready(function () {

    var url = window.location.pathname;
    var id = url.substring(url.lastIndexOf('/') + 1);

    console.log(id);

    $.getJSON('http://localhost:8090/api/szkola/' + id, function (data) {

        var nazwa = data.name;
        var miasto = data.miasto;
        var adres = data.adres;
        var kodpocztowy = data.kodpocztowy;

        dane[0] = data.adres + " " + data.kodpocztowy;
        nazwy[0] = data.name;
        kod[0] = data.kodpocztowy;

        $(".nazwaszkoly").append(nazwa);
        $(".miasto").append(miasto);
        $(".adres").append(adres);
        $(".kodpocztowy").append(kodpocztowy);

        codeAddress(dane, nazwy);
    });
    var geocoder;
    var map;
    function initialize() {
        geocoder = new google.maps.Geocoder();
        var lokalizacja = new google.maps.LatLng(52.069167, 19.480556);
        var mapOptions = {
            zoom: 6,
            center: lokalizacja,
            disableDoubleClickZoom: true
        }
        map = new google.maps.Map(document.getElementById("map"), mapOptions);
    }
    function codeAddress(dane, nazwy) {
        console.log("Jestem w codeAddress: " + nazwy[0]);
        for (var i = 0; i < dane.length; i++) {
            console.log("Nazwa2:" + nazwy[i]);
            console.log("Pelen adres: " + dane[0]);
            var contentString = nazwy[i];
            console.log("Nazwa3:" + contentString);
            geocoder.geocode({'componentRestrictions':
                        {country: 'Poland'}, address: dane[i]}, function (results, status) {
                if (status == google.maps.GeocoderStatus.OK) {
                    console.log("pokaz1: " + contentString);
                    var marker = new google.maps.Marker({
                        map: map,
                        position: results[0].geometry.location
                    });
                    console.log("Lokalicja: " + results[0].geometry.location);
                    var infowindow = new google.maps.InfoWindow;
                    bindInfoW(marker, contentString, infowindow);
                    function bindInfoW(marker, contentString, infowindow)
                    {
                        google.maps.event.addListener(marker, 'click', function () {
                            infowindow.setContent(contentString);
                            infowindow.open(map, marker);
                        });
                        google.maps.event.addListener(marker, 'dblclick', function () {
                            map.setZoom(18);
                            map.setCenter(marker.getPosition());
                        });
                    }
                } else {
                    alert("Geolokacja się nie udała: " + status);
                }
            });
        }
    }
    initialize();
});

