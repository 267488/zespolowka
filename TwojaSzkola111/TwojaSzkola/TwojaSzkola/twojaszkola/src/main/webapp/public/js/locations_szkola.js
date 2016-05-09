var dane = [];
var nazwy = [];
var kod = [];
var ids = [];
var dystans;


$(document).ready(function () {

    //Trzeba id zrobić tak, aby był aktywnego. Teraz jest przypisywany ręcznie id=6  
    var service = new google.maps.DistanceMatrixService();
    var geocoder = new google.maps.Geocoder();
    var czegoSzukam;
    var adresUcznia;
    var id = 6;
    var adresULatLng;
    var adresSLatLng;

    console.log("ID:" + id);

    $.getJSON('http://localhost:8090/api/uczen/' + id, function (data2) {

        $.getJSON('http://localhost:8090/api/szkola/', function (data) {
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
        var k = 0;
        if (status == google.maps.DistanceMatrixStatus.OK) {
            var origins = response.originAddresses;
            //var destinations = response.destinationAddresses;
            console.log("Rozmiar: " + origins.length);
            for (var i = 0; i < origins.length; i++) {
                var results = response.rows[i].elements;
                console.log("Rezultat", results);
                for (var j = 0; j < results.length; j++) {
                    console.log("Res: " + results.length);
                    var element = results[j];
                    var distance = element.distance.text;
                    console.log("Distance:  ", distance);
                    console.log("Iteration: " + k);
                    //$(".dystans").append(dystans);
                    $(".widget.searching-result .school > .info > .city > .dist:eq("+ i +")").html(distance);
                    k++;
                }
            }
        }
    }
    function codeAddress(dane, nazwy) {

        console.log("Jestem w codeAddress: " + nazwy[i]);
        for (var i = 0; i < dane.length; i++) {
            console.log("Kod pocztowy : " + kod[i]);
            console.log("pokaz1: " + nazwy[i]);
            {
                geocoder.geocode({'componentRestrictions':
                            {country: 'PL'}, address: dane[i]}, function (results, status) {
                    if (status == google.maps.GeocoderStatus.OK) {
                        adresSLatLng = results[0].geometry.location;
                        service.getDistanceMatrix(
                                {
                                    origins: [adresULatLng], //adres Ucznia
                                    destinations: [adresSLatLng], //adres Szkoły
                                    travelMode: google.maps.TravelMode.WALKING,
                                }, callback);
                        console.log("Adres Ucznia: " + adresULatLng);
                        console.log("Adres Szkoły: " + adresSLatLng);
                        console.log("Wynik: " + results[0].geometry.location);
                    } else {
                        alert("Nie można wyliczyć dystansu: " + status);
                    }
                });
            }
        }
    }
});