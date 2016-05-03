var dane = [];
var nazwy = [];
var kod = [];
var ids = [];
var dystans;


$(document).ready(function () {

    //Trzeba id zrobić tak, aby był aktywnego. Teraz jest przypisywany ręcznie id=6  
    var geocoder;
    var map;
    var service = new google.maps.DistanceMatrixService();
    var czegoSzukam;
    var adresUcznia;
    var id = 6;
    var adresULatLng;
    var adresSLatLng;

    console.log("ID:" + id);

    $.getJSON('http://localhost:8090/api/uczen/' + id, function (data2) {

        $.getJSON('http://localhost:8090/api/szkolaBy/' + data2.czegoSzukam, function (data) {
            czegoSzukam = data2.czegoSzukam;
            adresUcznia = data2.adres + " " + data2.kodpocztowy;
            console.log("Adres ucznia: " + adresUcznia);
            console.log("Wybór ucznia: " + czegoSzukam);

            for (var i = 0; i < data.length; i++)
            {
                dane[i] = data[i].adres + " " + data[i].kodpocztowy;
                nazwy[i] = data[i].name;
                kod[i] = data[i].kodpocztowy;
                ids[i] = data[i].id;

                console.log("Nazwa: " + nazwy[i]);
                console.log("Adres: " + dane[i]);
                console.log("Kod pocztowy: " + kod[i]);
            }

            codeAddressUczen(adresUcznia);
            codeAddress(dane, nazwy);

        });
    });

    function codeAddressUczen(adresUcznia) {
        geocoder.geocode({'componentRestrictions':
                    {country: 'PL'}, address: adresUcznia}, function (results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
                adresULatLng = results[0].geometry.location;
            }
        });
    }

    function callback(response, status) {
        console.log(response);
        console.log(status);
        if (status == google.maps.DistanceMatrixStatus.OK) {
            var origins = response.originAddresses;
            var destinations = response.destinationAddresses;
            for (var i = 0; i < origins.length; i++) {
                var results = response.rows[i].elements;
                for (var j = 0; j < results.length; j++) {
                    var element = results[j];
                    var dystans = element.distance.text;
                    console.log("Dystans: " + dystans);
                    //$('.list-container').append(dystans);
                }
            }
        }
    }

    function initialize() {
        geocoder = new google.maps.Geocoder();
        var srodekPolski = new google.maps.LatLng(52.069167, 19.480556);
        var mapOptions = {
            zoom: 6,
            center: srodekPolski,
            disableDoubleClickZoom: true
        }
        map = new google.maps.Map(document.getElementById("map"), mapOptions);
    }
    function codeAddress(dane, nazwy) {

        console.log("Jestem w codeAddress: " + nazwy[i]);
        var infowindow = new google.maps.InfoWindow;
        for (var i = 0; i < dane.length; i++) {
            console.log("Kod pocztowy : " + kod[i]);
            console.log("pokaz1: " + nazwy[i]);
            (function (k) {
                geocoder.geocode({'componentRestrictions':
                            {country: 'PL'}, address: dane[k]}, function (results, status) {
                    if (status == google.maps.GeocoderStatus.OK) {
                        var marker = new google.maps.Marker({
                            map: map,
                            position: results[0].geometry.location
                        });
                        adresSLatLng = results[0].geometry.location;
                        service.getDistanceMatrix(
                                {
                                    origins: [adresULatLng], //adres Ucznia
                                    destinations: [adresSLatLng], //adres Szkoły
                                    travelMode: google.maps.TravelMode.WALKING,
                                }, callback);
                        console.log("Adres Ucznia: " + adresULatLng);
                        console.log("Adres Szkoły: " + adresSLatLng);
                        console.log("pokaz2: " + nazwy[k]);
                        console.log("pokaz2: " + ids[k]);
                        console.log("Wynik: " + results[0].geometry.location);

                        var contentString =
                                "<a href=profilSzkoly/" + ids[k] + ">" + nazwy[k] + "</a>";
                        bindInfoW(marker, contentString, infowindow);
                        function bindInfoW(marker, contentString, infowindow)
                        {
                            console.log("pokaz: " + contentString);
                            google.maps.event.addListener(marker, 'click', function () {
                                infowindow.close();
                                infowindow.setContent(contentString);
                                infowindow.open(map, marker);
                            });
                            google.maps.event.addListener(marker, 'dblclick', function () {
                                map.setZoom(16);
                                map.setCenter(marker.getPosition());
                            });
                        }
                    } else {
                        alert("Geolokacja się nie udała: " + status);
                    }
                });
            })(i);
        }
    }
    initialize();
});