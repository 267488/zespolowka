/* 
 * Copyright 2016 Agata Kostrzewa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

'use strict';

/* Controllers */

var biking2Controllers = angular
        .module('biking2Controllers', ['highcharts-ng'])
        .constant('emptyChart', {
            options: {credits: {enabled: false}},
            title: {text: ''},
            loading: true
        });

biking2Controllers.controller('IndexCtrl', ['$scope', '$http', '$interval', function ($scope, $http, $interval) {
        $http.get('/api/ulubioneSzkoly?all=true').success(function (data) {
            $scope.ulubione_szkoly = data;
        });

        $http.get('/api/ProponowaneSzkoly?all=true').success(function (data) {
            $scope.proponowaneSzkoly = data;
        });
        
        $http.get('/api/zainteresowaniaUcznia?all=true').success(function (data) {
            $scope.zainteresowania = data;
        });
        
        $http.get('/api/CurrentUczen?all=true').success(function (data) {
            $scope.uczen = data;
        });
//    $http.get('/api/summary').success(function(data) {
//	$scope.summary = data;
//    });
        
        $scope.Ulubione = function (event) {
            $scope.id = event.target.id;
            $scope.dodawanie = true;
            $scope.submitting = true;
            $http.delete('/api/ulubionaSzkolaDelete/' + $scope.id)
                    .success(function (data) {
                        if (data) {
                            $scope.dodawanie = false;
                        }
                    })
                    .error(function (data) {
                    });
            if ($scope.dodawanie) {
                $http({
                    method: 'POST',
                    url: '/api/ulubionaSzkola/' + $scope.id
                }).success(function (data) {
                    $scope.submitting = false;
                    if (!data) {
                        alert('Usunięto z ulubionych szkołę o id = ' + $scope.id);
                        $http.get('/api/ulubioneSzkoly?all=true').success(function (data) {
                            $scope.ulubione_szkoly = data;
                        });
                        $http.get('/api/ProponowaneSzkoly?all=true').success(function (data) {
                            $scope.proponowaneSzkoly = data;
                        });
                    } else {
                        alert('Dodano do ulubionych szkołę o id = ' + $scope.id);
                        $http.get('/api/ulubioneSzkoly?all=true').success(function (data) {
                            $scope.ulubione_szkoly = data;
                        });
                        $http.get('/api/ProponowaneSzkoly?all=true').success(function (data) {
                            $scope.proponowaneSzkoly = data;
                        });
                    }
                }).error(function (data, status) {
                    $scope.submitting = false;
                    if (status === 400)
                        $scope.badRequest = data;
                    else if (status === 409)
                        $scope.badRequest = 'Ulubiona Szkola o takiej nazwie juz istnieje';
                });
            }
            $http.get('/api/ulubioneSzkoly?all=true').success(function (data) {
                $scope.ulubione_szkoly = data;
            });
        };

        $scope.currentBikingPicture = '/img/p1.jpg';

        $http.get('/api/bikingPictures').success(function (data) {
            var bikingPictures = data.randomize();

            var timer = $interval(function (count) {
                var thePicture = bikingPictures[count % bikingPictures.length];
                $scope.currentBikingPicture = '/api/bikingPictures/' + thePicture.id + '.jpg';
                $scope.currentLinkToBikingPicture = thePicture.link;
            }, 5000);
            $scope.$on("$destroy", function () {
                $interval.cancel(timer);
            });
        });
    }]);

//////////////////////// UCZEN CONTROLLER /////////////////

biking2Controllers.controller('UczenCtrl', ['$scope', '$http', '$modal', function ($scope, $http, $modal) {
        $http.get('/api/uczen?all=true').success(function (data) {
            $scope.uczen = data;
        });

        $scope.openNewUczenDlg = function () {
            var modalInstance = $modal.open({
                templateUrl: '/partials/_new_uczen.html',
                controller: 'AddNewUczenCtrl',
                scope: $scope
            });

            modalInstance.result.then(
                    function (newUczen) {
                        $scope.uczen.push(newUczen);
                    },
                    function () {
                    }
            );
        };

        $scope.openEditUczenDlg = function () {
            var modalInstance = $modal.open({
                templateUrl: '/partials/_edit_uczen.html',
                controller: 'EditUczenCtrl',
                scope: $scope
            });

            modalInstance.result.then(
                    function (newUczen) {
                        $http.get('/api/uczen?all=true').success(function (data) {
                            $scope.uczen = data;
                        });
                    },
                    function () {
                    }
            );
        };

    }]);

///////////////// ADD NEW UCZEN CONTROLLER /////////////////////

biking2Controllers.controller('AddNewUczenCtrl', ['$scope', '$modalInstance', '$http', function ($scope, $modalInstance, $http) {
        $scope.uczen = {
            id: null,
            pesel: null,
            name: null,
            lastname: null,
            mail: null,
            czegoSzukam: null,
            kodpocztowy: null
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

        $scope.submit = function () {
            $scope.submitting = true;
            $http({
                method: 'POST',
                url: '/api/uczen',
                data: $scope.uczen
            }).success(function (data) {
                $scope.submitting = false;
                $modalInstance.close(data);
            }).error(function (data, status) {
                $scope.submitting = false;
                if (status === 400)
                    $scope.badRequest = data;
                else if (status === 409)
                    $scope.badRequest = 'Uczen o takim nr psl juz istnieje';
            });
        };
    }]);

/////////////////// UCZEN CONTROLLER //////////////////////////

biking2Controllers.controller('EditUczenCtrl', ['$scope', '$modalInstance', '$http', function ($scope, $modalInstance, $http) {
        $http.get('/api/CurrentUczen').success(function (data) {
            $scope.uczen = data;
        });

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

        $scope.submit = function () {
            $scope.submitting = true;
            $http({
                method: 'PUT',
                url: '/api/CurrentUczen',
                data: $scope.uczen
            }).success(function (data) {
                $scope.submitting = false;
                $modalInstance.close(data);
            }).error(function (data, status) {
                $scope.submitting = false;
                if (status === 400)
                    $scope.badRequest = data;
                else if (status === 409)
                    $scope.badRequest = 'Uczen o takim nr psl juz istnieje';
            });
        };
    }]);

/////////////////// PROPONOWANE SZKOLY CONTROLLER ///////////////////

biking2Controllers.controller('ProponowaneCtrl', ['$scope', '$http', '$modal', function ($scope, $http, $modal) {
        $http.get('/api/proponowaneSzkoly?all=true').success(function (data) {
            $scope.proponowaneSzkoly = data;
        });
    }]);


//////////////// ZAINTERESOWANIA CONTROLLER /////////////////////////

biking2Controllers.controller('ZainteresowaniaCtrl', ['$scope', '$http', '$modal', function ($scope, $http, $modal) {
        $http.get('/api/zainteresowania?all=true').success(function (data) {
            $scope.zainteresowania = data;
        });

        $scope.openNewZainteresowaniaDlg = function () {
            var modalInstance = $modal.open({
                templateUrl: '/partials/_new_zainteresowania.html',
                controller: 'AddNewZainteresowaniaCtrl',
                scope: $scope
            });

            modalInstance.result.then(
                    function (newZainteresowania) {
                        if (!newZainteresowania) {
                            alert('Nie można dodać po raz drugi zainteresowania');
                            //console.log('new Zainteresowania');
                        } else {
                            $scope.zainteresowania.push(newZainteresowania);
                        }
                    },
                    function () {
                    }
            );
        };
    }]);

///////////////////// ADD NEW ZAINTERESOWANIA CONTROLLER ///////////////////////

biking2Controllers.controller('AddNewZainteresowaniaCtrl', ['$scope', '$modalInstance', '$http', function ($scope, $modalInstance, $http) {
        $http.get('/api/przedmioty?all=true').success(function (data) {
            $scope.przedmioty = data;
        });

        $http.get('/api/sumaZainteresowanUcznia?all=true').success(function (data) {
            $scope.max = 100 - data;
            console.log("MAX " + $scope.max);
        });

        $scope.zainteresowania = {
            id: null,
            uczenId: null,
            przedmiotName: null,
            stopienZainteresowania: null
        };

        $scope.przedmioty = {
            id: null,
            name: null,
            kategoria: null
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

        $scope.submit = function () {
            $scope.zainteresowania.przedmiotName = document.getElementById("WybranyPrzedmiot").options[document.getElementById("WybranyPrzedmiot").selectedIndex].text;
            $scope.submitting = true;
            $http({
                method: 'POST',
                url: '/api/zainteresowania',
                data: $scope.zainteresowania
            }).success(function (data) {
                $scope.submitting = false;
                $modalInstance.close(data);
            }).error(function (data, status) {
                $scope.submitting = false;
                if (status === 400)
                    $scope.badRequest = data;
                else if (status === 409)
                    $scope.badRequest = 'Zainteresowania o takiej nazwie juz istnieja';
            });

        };
    }]);

//////////////////////////// OCENA PRZEDMIOTU ///////////////////////////////

biking2Controllers.controller('OcenaPrzedmiotuCtrl', ['$scope', '$http', '$modal', function ($scope, $http, $modal) {
        $http.get('/api/ocenaPrzedmiotu?all=true').success(function (data) {
            $scope.ocenaPrzedmiotu = data;
        });

        $scope.openNewOcenaPrzedmiotuDlg = function () {
            var modalInstance = $modal.open({
                templateUrl: '/partials/_new_ocena_przedmiotu.html',
                controller: 'AddNewOcenaPrzedmiotuCtrl',
                scope: $scope
            });

            modalInstance.result.then(
                    function (newOcenaPrzedmiotu) {
                        $scope.ocenaPrzedmiotu.push(newOcenaPrzedmiotu);
                    },
                    function () {
                    }
            );
        };
    }]);

biking2Controllers.controller('AddNewOcenaPrzedmiotuCtrl', ['$scope', '$modalInstance', '$http', function ($scope, $modalInstance, $http) {
        $http.get('/api/przedmioty?all=true').success(function (data) {
            $scope.przedmioty = data;
        });

        $scope.ocenaPrzedmiotu = {
            id: null,
            profilId: null,
            przedmiotName: null,
            ocena: null
        };

        $scope.przedmioty = {
            id: null,
            name: null,
            kategoria: null
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

        $scope.submit = function () {
            $scope.ocenaPrzedmiotu.przedmiotName = document.getElementById("WybranyPrzedmiot").options[document.getElementById("WybranyPrzedmiot").selectedIndex].text;
            $scope.submitting = true;
            $http({
                method: 'POST',
                url: '/api/ocenaPrzedmiotu',
                data: $scope.ocenaPrzedmiotu
            }).success(function (data) {
                $scope.submitting = false;
                $modalInstance.close(data);
            }).error(function (data, status) {
                $scope.submitting = false;
                if (status === 400)
                    $scope.badRequest = data;
                else if (status === 409)
                    $scope.badRequest = 'Zainteresowania o takiej nazwie juz istnieja';
            });
        };
    }]);

////////////////////// SZKOŁA CONTROLLER ////////////////////////

biking2Controllers.controller('SzkolaCtrl', ['$scope', '$http', '$modal', function ($scope, $http, $modal) {
        $http.get('/api/szkola?all=true').success(function (data) {
            $scope.szkola = data;
        });

        $http.get('/api/przedmioty?all=true').success(function (data) {
            $scope.przedmioty = data;
        });

        $http.get('/api/ulubioneSzkoly?all=true').success(function (data) {
            $scope.ulubione_szkoly = data;
            console.log('ulubione_szkoly' + $scope.ulubione_szkoly);
        });

        $scope.szukane = null;

        $scope.Szukaj = function () {
            $scope.szukane = document.getElementById("SzukajSzkoly").value;
            console.log("SZUKANE " + $scope.szukane);
            $http({
                method: 'GET',
                url: '/api/szukaneSzkoly/' + $scope.szukane
            }).success(function (data) {
                $scope.szkola = data;
            }).error(function (data, status) {

            });
        };

        $scope.Ulubione = function (event) {
            $scope.id = event.target.id;
            $scope.dodawanie = true;
            $scope.submitting = true;
            $http.delete('/api/ulubionaSzkolaDelete/' + $scope.id)
                    .success(function (data) {
                        if (data) {
                            $scope.dodawanie = false;
                        }
                    })
                    .error(function (data) {
                    });
            if ($scope.dodawanie) {
                $http({
                    method: 'POST',
                    url: '/api/ulubionaSzkola/' + $scope.id
                            // data: $scope.id
                }).success(function (data) {
                    $scope.submitting = false;
                    //$modalInstance.close(data);
                    if (!data) {
                        alert('Usunięto z ulubionych szkołę o id = ' + $scope.id);
                        $http.get('/api/szkola?all=true').success(function (data) {
                            $scope.szkola = data;
                        });
                    } else {
                        alert('Dodano do ulubionych szkołę o id = ' + $scope.id);
                        $http.get('/api/szkola?all=true').success(function (data) {
                            $scope.szkola = data;
                        });
                    }
                }).error(function (data, status) {
                    $scope.submitting = false;
                    if (status === 400)
                        $scope.badRequest = data;
                    else if (status === 409)
                        $scope.badRequest = 'Ulubiona Szkola o takiej nazwie juz istnieje';
                });
            }
        };

        $scope.openNewSzkolaDlg = function () {
            var modalInstance = $modal.open({
                templateUrl: '/partials/_new_szkola.html',
                controller: 'AddNewSzkolaCtrl',
                scope: $scope
            });

            modalInstance.result.then(
                    function (newSzkola) {
                        $scope.szkola.push(newSzkola);
                    },
                    function () {
                    }
            );
        };

        $scope.openEditSzkolaDlg = function () {
            var modalInstance = $modal.open({
                templateUrl: '/partials/_edit_szkola.html',
                controller: 'EditSzkolaCtrl',
                scope: $scope
            });

            modalInstance.result.then(
                    function (newSzkola) {
                        $http.get('/api/szkola?all=true').success(function (data) {
                            $scope.szkola = data;
                        });
                    },
                    function () {
                    }
            );
        };

    }]);

biking2Controllers.controller('AddNewUlubioneCtrl', ['$scope', '$http', '$modal', function ($scope, $http, $modal) {
        $http.get('/api/szkola?all=true').success(function (data) {
            $scope.szkola = data;
        });

        $scope.Ulubione = function (event) {
            alert('Dodano do ulubionych szkołę o id = ' + event.target.id);
            $scope.id = event.target.id;
            var modalInstance = $modal.open({
                controller: 'AddNewUlubioneCtrl',
                scope: $scope
            });
        };

        $scope.openNewSzkolaDlg = function () {
            var modalInstance = $modal.open({
                templateUrl: '/partials/_new_szkola.html',
                controller: 'AddNewSzkolaCtrl',
                scope: $scope
            });

            modalInstance.result.then(
                    function (newSzkola) {
                        $scope.szkola.push(newSzkola);
                    },
                    function () {
                    }
            );
        };
    }]);

biking2Controllers.controller('AddNewSzkolaCtrl', ['$scope', '$modalInstance', '$http', function ($scope, $modalInstance, $http) {
        $scope.szkola = {
            id: null,
            name: null,
            numer: null,
            adres: null,
            mail: null,
            miasto: null,
            kodpocztowy: null,
            typSzkoly: null,
            rodzajSzkoly: null,
            rodzajGwiazdki: null
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

        $scope.submit = function () {
            $scope.submitting = true;
            console.log("NUMER SZKOLY" + $scope.szkola.numer);
            console.log("TYP SZKOLY" + $scope.szkola.typSzkoly);
            $http({
                method: 'POST',
                url: '/api/szkola',
                data: $scope.szkola
            }).success(function (data) {
                $scope.submitting = false;
                $modalInstance.close(data);
            }).error(function (data, status) {
                $scope.submitting = false;
                if (status === 400)
                    $scope.badRequest = data;
                else if (status === 409)
                    $scope.badRequest = 'Szkola o takiej nazwie juz istnieje';
            });
        };
    }]);

//////////EDIT SZKOLA///////////////
biking2Controllers.controller('EditSzkolaCtrl', ['$scope', '$modalInstance', '$http', function ($scope, $modalInstance, $http) {
        $http.get('/api/szkola/1').success(function (data) {
            $scope.szkola = data;
        });

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

        $scope.submit = function () {
            $scope.submitting = true;
            $http({
                method: 'PUT',
                url: '/api/szkola/1',
                data: $scope.szkola
            }).success(function (data) {
                $scope.submitting = false;
                $modalInstance.close(data);
            }).error(function (data, status) {
                $scope.submitting = false;
                if (status === 400)
                    $scope.badRequest = data;
                else if (status === 409)
                    $scope.badRequest = 'Szkola o takiej nazwie juz istnieje';
            });
        };
    }]);

//AKTUALNOSCI_SZKOLA CONTROLLER

biking2Controllers.controller('AktualnosciSzkolaCtrl', ['$scope', '$http', '$modal', function ($scope, $http, $modal) {
        $http.get('/api/aktualnosciSzkola?all=true').success(function (data) {
            $scope.aktualnosciSzkola = data;
        });

        $scope.imageInterval = 5000;
        $scope.allPictures = [];
        $scope.slides = [];

        $scope.reshuffle = function () {
            $scope.allPictures = $scope.allPictures.randomize();
            $scope.slides.length = 0;
            var max = Math.min(15, $scope.allPictures.length);
            for (var i = 0; i < max; ++i) {
                $scope.slides.push({
                    image: '/api/galleryPictures/' + $scope.allPictures[i].id + '.jpg',
                    takenOn: $scope.allPictures[i].takenOn,
                    text: $scope.allPictures[i].description
                });
            }
        };

        $http.get('/api/galleryPictures').success(function (data) {
            $scope.allPictures = data;
            $scope.reshuffle();
        });

        $scope.openNewAktualnosciSzkolaDlg = function () {
            console.log("test");
            var modalInstance = $modal.open({
                templateUrl: '/partials/_new_aktualnosci_szkola.html',
                controller: 'AddNewAktualnosciSzkolaCtrl',
                scope: $scope
            });

            modalInstance.result.then(
                    function (newAktualnosciSzkola) {
                        $scope.aktualnosciSzkola.push(newAktualnosciSzkola);
                    },
                    function () {
                    }
            );
        };
        $scope.openNewPictureDlg = function () {
            var modalInstance = $modal.open({
                templateUrl: '/partials/_new_picture.html',
                controller: 'AddNewPictureCtrl',
                scope: $scope
            });

            modalInstance.result.then(
                    function (newPicture) {
                        if ($scope.slides.length < 15) {
                            $scope.slides.push({
                                image: '/api/galleryPictures/' + newPicture.id + '.jpg',
                                takenOn: newPicture.takenOn,
                                text: newPicture.description
                            });
                        }
                    },
                    function () {
                    }
            );
        };
    }]);

//ADD NEW ATUALNOSCI_SZKOLA CONTROLLER

biking2Controllers.controller('AddNewAktualnosciSzkolaCtrl', ['$scope', '$modalInstance', '$http', function ($scope, $modalInstance, $http) {
        $scope.aktualnosciSzkola = {
            szkolaId: null,
            id: null
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

        $scope.submit = function () {
            $scope.submitting = true;
            $http({
                method: 'POST',
                url: '/api/aktualnosciSzkola',
                data: $scope.aktualnosciSzkola
            }).success(function (data) {
                $scope.submitting = false;
                $modalInstance.close(data);
            }).error(function (data, status) {
                $scope.submitting = false;
                if (status === 400)
                    $scope.badRequest = data;
                else if (status === 409)
                    $scope.badRequest = 'Blad, prawdopodobnie ID_szkoly';
            });
        };
    }]);

//Rozszerzone Przedmioty
biking2Controllers.controller('RozszerzonePrzedmiotyCtrl', ['$scope', '$http', '$modal', function ($scope, $http, $modal) {
        $http.get('/api/rozszerzonePrzedmioty?all=true').success(function (data) {
            $scope.rozszerzonePrzedmioty = data;
        });

        $scope.openNewRozszerzonyPrzedmiotDlg = function () {
            var modalInstance = $modal.open({
                templateUrl: '/partials/_new_rozszerzone_przedmioty.html',
                controller: 'AddNewRozszerzonePrzedmiotyCtrl',
                scope: $scope
            });

            modalInstance.result.then(
                    function (newRozszerzonePrzedmioty) {
                        $scope.profile.push(newRozszerzonePrzedmioty);
                    },
                    function () {
                    }
            );
        };
    }]);

biking2Controllers.controller('AddNewRozszerzonePrzedmiotyCtrl', ['$scope', '$modalInstance', '$http', function ($scope, $modalInstance, $http) {

        $scope.rozszerzonePrzedmioty = {
            id: null,
            profilId: null,
            przedmiotId: null
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

        $scope.submit = function () {
            //$scope.profil.profil_nazwaId=document.getElementById("WybranaNazwaProfilu").options[document.getElementById("WybranaNazwaProfilu").selectedIndex].text;
            $scope.submitting = true;
            $http({
                method: 'POST',
                url: '/api/rozszerzonePrzedmioty/',
                data: $scope.profil
            }).success(function (data) {
                $scope.submitting = false;
                $modalInstance.close(data);
            }).error(function (data, status) {
                $scope.submitting = false;
                if (status === 400)
                    $scope.badRequest = data;
                else if (status === 409)
                    $scope.badRequest = 'Profil o takiej nazwie juz istnieje';
            });
        };
    }]);
/**********************************************************************/


//Profil
biking2Controllers.controller('ProfileCtrl', ['$scope', '$http', '$modal', function ($scope, $http, $modal) {
        $http.get('/api/profile?all=true').success(function (data) {
            $scope.profile = data;
        });

        $scope.openNewProfilDlg = function () {
            var modalInstance = $modal.open({
                templateUrl: '/partials/_new_profil.html',
                controller: 'AddNewProfilCtrl',
                scope: $scope
            });

            modalInstance.result.then(
                    function (newProfil) {
                        if (!newProfil) {
                            alert('Nie można dodać po raz drugi tego samego profilu');
                        } else {
                            $scope.profile.push(newProfil);
                        }
                    },
                    function () {
                    }
            );
        };
    }]);

biking2Controllers.controller('AddNewProfilCtrl', ['$scope', '$modalInstance', '$http', function ($scope, $modalInstance, $http) {
        $http.get('/api/profile_nazwa?all=true').success(function (data) {
            $scope.profile_nazwa = data;
        });

        $http.get('/api/przedmioty?all=true').success(function (data) {
            $scope.przedmioty = data;
        });

        $scope.profil = {
            id: null,
            profil_nazwaId: null,
            szkolaId: null
        };

        $scope.rozszerzonePrzedmioty = [{
                id: null,
                profilId: null,
                przedmiotId: null
            },
        ];

        $scope.isChecked = function (id) {
            var match = false;
            for (var i = 0; i < $scope.rozszerzonePrzedmioty.length; i++) {
                if ($scope.rozszerzonePrzedmioty[i].przedmiotId == id) {
                    match = true;
                }
            }
            return match;
        };

        $scope.sync = function (bool, przedmiot) {
            if (bool) {
                $scope.rozszerzonePrzedmioty.push({id: null, profilId: null, przedmiotId: przedmiot.id});
                console.log("SYNC " + przedmiot.name);
            } else {
                // remove item
                for (var i = 0; i < $scope.rozszerzonePrzedmioty.length; i++) {
                    if ($scope.rozszerzonePrzedmioty[i].przedmiotId === przedmiot.id) {
                        console.log("reSYNC " + przedmiot.name);
                        $scope.rozszerzonePrzedmioty.splice(i, 1);
                    }
                }
            }
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

        $scope.submit = function () {
            $scope.submitting = true;
            console.log("Profil_nazwaId" + $scope.profil.profil_nazwaId);
            $http({
                method: 'POST',
                url: '/api/profile/' + $scope.profil.profil_nazwaId,
                data: $scope.rozszerzonePrzedmioty
            }).success(function (data) {
                $scope.submitting = false;
                $modalInstance.close(data);
            }).error(function (data, status) {
                $scope.submitting = false;
                if (status === 400)
                    $scope.badRequest = data;
                else if (status === 409)
                    $scope.badRequest = 'Profil o takiej nazwie juz istnieje';
            });
        };
    }]);

/**********************************************************************/


//Profil nazwa

biking2Controllers.controller('Profile_nazwaCtrl', ['$scope', '$http', '$modal', function ($scope, $http, $modal) {
        $http.get('/api/profile_nazwa?all=true').success(function (data) {
            $scope.profile_nazwa = data;
        });

        $scope.openNewProfil_nazwaDlg = function () {
            var modalInstance = $modal.open({
                templateUrl: '/partials/_new_profil_nazwa.html',
                controller: 'AddNewProfil_nazwaCtrl',
                scope: $scope
            });

            modalInstance.result.then(
                    function (newProfil_nazwa) {
                        $scope.profile_nazwa.push(newProfil_nazwa);
                    },
                    function () {
                    }
            );
        };
    }]);

biking2Controllers.controller('AddNewProfil_nazwaCtrl', ['$scope', '$modalInstance', '$http', function ($scope, $modalInstance, $http) {
        $scope.profil_nazwa = {
            id: null,
            nazwa: null
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

        $scope.submit = function () {
            $scope.submitting = true;
            $http({
                method: 'POST',
                url: '/api/profile_nazwa',
                data: $scope.profil_nazwa
            }).success(function (data) {
                $scope.submitting = false;
                $modalInstance.close(data);
            }).error(function (data, status) {
                $scope.submitting = false;
                if (status === 400)
                    $scope.badRequest = data;
                else if (status === 409)
                    $scope.badRequest = 'Profil o takiej nazwie juz istnieje';
            });
        };
    }]);

/*********************************************************************/

biking2Controllers.controller('AddNewBikeCtrl', ['$scope', '$modalInstance', '$http', function ($scope, $modalInstance, $http) {
        $scope.bike = {
            name: null,
            boughtOn: new Date(),
            color: null
        };

        $scope.boughtOnOptions = {
            'year-format': "'yyyy'",
            'starting-day': 1,
            open: false
        };

        $scope.openBoughtOn = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.boughtOnOptions.open = true;
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

        $scope.submit = function () {
            $scope.submitting = true;
            $http({
                method: 'POST',
                url: '/api/bikes',
                data: $scope.bike
            }).success(function (data) {
                $scope.submitting = false;
                $modalInstance.close(data);
            }).error(function (data, status) {
                $scope.submitting = false;
                if (status === 400)
                    $scope.badRequest = data;
                else if (status === 409)
                    $scope.badRequest = 'The name is already used.';
            });
        };
    }]);

biking2Controllers.controller('MilagesCtrl', ['$scope', '$http', '$modal', 'emptyChart', function ($scope, $http, $modal, emptyChart) {
        $scope.currentYearConfig = $scope.monthlyAverageConfig = $scope.historyConfig = emptyChart;
        $scope.worstYear = $scope.bestYear = null;
        $scope.alerts = [];
        var currentYear = new Date().getFullYear();
        $scope.historyRange = {
            start: currentYear - 3,
            end: currentYear - 1,
            max: currentYear - 1
        };

        $http.get('/api/charts/currentYear').success(function (data) {
            $scope.currentYearConfig = data;
        });

        $http.get('/api/charts/monthlyAverage').success(function (data) {
            $scope.monthlyAverageConfig = data;
        });

        $scope.updateHistory = function () {
            $http.get(
                    '/api/charts/history',
                    {params: {start: $scope.historyRange.start, end: ($scope.historyRange.end + 1)}}
            ).success(function (data) {
                $scope.historyConfig = data;
                if (data.userData.worstYear !== null) {
                    var year = Object.keys(data.userData.worstYear)[0];
                    $scope.worstYear = {
                        year: year,
                        value: data.userData.worstYear[year]
                    };
                }
                if (data.userData.bestYear !== null) {
                    var year = Object.keys(data.userData.bestYear)[0];
                    $scope.bestYear = {
                        year: year,
                        value: data.userData.bestYear[year]
                    };
                }
            });
        };
        $scope.updateHistory();

        $http.get('/api/bikes').success(function (data) {
            $scope.bikes = data;
        });

        $scope.closeAlert = function (index) {
            $scope.alerts.splice(index, 1);
        };

        $scope.openNewMilageDlg = function () {
            if ($scope.bikes.length === 0)
                $scope.alerts.push({type: 'danger', msg: 'Please define at least one bike'});
            else {
                var modalInstance = $modal.open({
                    templateUrl: '/partials/_new_milage.html',
                    controller: 'AddNewMilageCtrl',
                    scope: $scope
                });

                modalInstance.result.then(
                        function () {
                            $http.get('/api/charts/currentYear').success(function (data) {
                                $scope.currentYearConfig.series = data.series;
                                $scope.currentYearConfig.userData = data.userData;
                            });

                            $http.get('/api/charts/monthlyAverage').success(function (data) {
                                $scope.monthlyAverageConfig = data;
                            });
                        },
                        function () {
                        }
                );
            }
        };
    }]);

biking2Controllers.controller('AddNewMilageCtrl', ['$scope', '$modalInstance', '$http', function ($scope, $modalInstance, $http) {

        $scope.milage = {
            bikeId: $scope.bikes[0].id,
            recordedOn: new Date(),
            amount: null
        };

        $scope.recordedOnOptions = {
            'year-format': "'yyyy'",
            'starting-day': 1,
            open: false
        };

        $scope.openRecordedOn = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.recordedOnOptions.open = true;
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

        $scope.submit = function () {
            $scope.submitting = true;
            $http({
                method: 'POST',
                url: '/api/bikes/' + $scope.milage.bikeId + '/milages',
                data: $scope.milage
            }).success(function (data) {
                $scope.submitting = false;
                $modalInstance.close(data);
            }).error(function (data, status) {
                $scope.submitting = false;
                if (status === 400)
                    $scope.badRequest = data;
                else if (status === 404)
                    $scope.badRequest = 'Please do not temper with this form.';
            });
        };
    }]);

biking2Controllers.controller('GalleryCtrl', ['$scope', '$http', '$modal', function ($scope, $http, $modal) {
        $scope.imageInterval = 5000;
        $scope.allPictures = [];
        $scope.slides = [];

        $scope.reshuffle = function () {
            $scope.allPictures = $scope.allPictures.randomize();
            $scope.slides.length = 0;
            var max = Math.min(15, $scope.allPictures.length);
            for (var i = 0; i < max; ++i) {
                $scope.slides.push({
                    image: '/api/galleryPictures/' + $scope.allPictures[i].id + '.jpg',
                    takenOn: $scope.allPictures[i].takenOn,
                    text: $scope.allPictures[i].description
                });
            }
        };

        $http.get('/api/galleryPictures').success(function (data) {
            $scope.allPictures = data;
            $scope.reshuffle();
        });

        $scope.openNewPictureDlg = function () {
            var modalInstance = $modal.open({
                templateUrl: '/partials/_new_picture.html',
                controller: 'AddNewPictureCtrl',
                scope: $scope
            });

            modalInstance.result.then(
                    function (newPicture) {
                        if ($scope.slides.length < 15) {
                            $scope.slides.push({
                                image: '/api/galleryPictures/' + newPicture.id + '.jpg',
                                takenOn: newPicture.takenOn,
                                text: newPicture.description
                            });
                        }
                    },
                    function () {
                    }
            );
        };
    }]);

biking2Controllers.controller('AddNewPictureCtrl', ['$scope', '$modalInstance', '$upload', function ($scope, $modalInstance, $upload) {
        $scope.picture = {
            takenOn: null,
            description: null
        };
        $scope.imageData = null;

        $scope.onFileSelect = function ($files) {
            $scope.imageData = $files[0];
        };

        $scope.takenOnOptions = {
            'year-format': "'yyyy'",
            'starting-day': 1,
            open: false
        };

        $scope.openTakenOn = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.takenOnOptions.open = true;
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

        $scope.submit = function () {
            $scope.submitting = true;
            $upload.upload({
                method: 'POST',
                url: '/api/galleryPictures',
                data: $scope.picture,
                file: $scope.imageData,
                fileFormDataName: 'imageData',
                withCredentials: true,
                formDataAppender: function (formData, key, val) {
                    if (key !== null && key === 'takenOn')
                        formData.append(key, val.toISOString());
                    else
                        formData.append(key, val);
                }
            }).success(function (data) {
                $scope.submitting = false;
                $modalInstance.close(data);
            }).error(function () {
                $scope.submitting = false;
                $scope.badRequest = 'There\'s something wrong with your input, please check!';
            });
        };
    }]);

biking2Controllers.controller('TracksCtrl', ['$scope', '$http', '$modal', function ($scope, $http, $modal) {
        $http.get('/api/tracks').success(function (data) {
            $scope.tracks = data;
        });

        $scope.openNewTrackDlg = function () {
            var modalInstance = $modal.open({
                templateUrl: '/partials/_new_track.html',
                controller: 'AddNewTrackCtrl',
                scope: $scope
            });

            modalInstance.result.then(
                    function (newTrack) {
                        $scope.tracks.push(newTrack);
                    },
                    function () {
                    }
            );
        };
    }]);

biking2Controllers.controller('AddNewTrackCtrl', ['$scope', '$modalInstance', '$upload', function ($scope, $modalInstance, $upload) {
        $scope.track = {
            name: null,
            coveredOn: new Date(),
            description: null,
            type: 'biking'
        };
        $scope.trackData = null;

        $scope.types = ['biking', 'running'];

        $scope.onFileSelect = function ($files) {
            $scope.trackData = $files[0];
        };

        $scope.coveredOnOptions = {
            'year-format': "'yyyy'",
            'starting-day': 1,
            open: false
        };

        $scope.openCoveredOn = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.coveredOnOptions.open = true;
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

        $scope.submit = function () {
            $scope.submitting = true;
            $upload.upload({
                method: 'POST',
                url: '/api/tracks',
                data: $scope.track,
                file: $scope.trackData,
                fileFormDataName: 'trackData',
                withCredentials: true,
                formDataAppender: function (formData, key, val) {
                    // Without that, val.toJSON() is used which adds "...
                    if (key !== null && key === 'coveredOn')
                        formData.append(key, val.toISOString());
                    else
                        formData.append(key, val);
                }
            }).success(function (data) {
                $scope.submitting = false;
                $modalInstance.close(data);
            }).error(function (data, status) {
                $scope.submitting = false;
                if (status === 409)
                    $scope.badRequest = 'A track with the given name on that date already exists.';
                else
                    $scope.badRequest = 'There\'s something wrong with your input, please check!';
            });
        };
    }]);

biking2Controllers.controller('TrackCtrl', ['$scope', '$http', '$q', '$routeParams', function ($scope, $http, $q, $routeParams) {
        $q.all([$http.get('/api/tracks/' + $routeParams.id), $http.get('/api/home')]).then(function (values) {
            $scope.track = values[0].data;
            $scope.home = values[1].data;
        });
    }]);

biking2Controllers.controller('LocationCtrl', ['$scope', '$http', function ($scope, $http) {
        $scope.locations = [];
        $http.get('/api/locations').success(function (data) {
            $scope.locations = data;
            var stompClient = Stomp.over(new SockJS('/api/ws'));
            stompClient.connect({}, function () {
                stompClient.subscribe('/topic/currentLocation', function (greeting) {
                    $scope.$apply(function () {
                        $scope.locations.push(JSON.parse(greeting.body));
                    });
                });
            });

            $scope.$on("$destroy", function () {
                stompClient.disconnect(function () {
                });
            });
        });
    }]);

biking2Controllers.controller('AboutCtrl', ['$scope', '$q', '$http', '$filter', '$interval', function ($scope, $q, $http, $filter, $interval) {
        $scope.refreshInterval = 30;
        $scope.memoryConfig = {
            options: {
                chart: {
                    type: 'area'
                },
                credits: {
                    enabled: false
                },
                title: {
                    text: 'Memory usage'
                },
                subtitle: {
                    text: 'Refreshes every ' + $scope.refreshInterval + ' seconds'
                },
                tooltip: {
                    shared: true,
                    valueSuffix: 'MiB'
                },
                plotOptions: {
                    area: {
                        stacking: 'normal',
                        lineColor: '#666666',
                        lineWidth: 1,
                        marker: {
                            lineWidth: 1,
                            lineColor: '#666666'
                        }
                    }
                }
            },
            series: [{
                    name: 'Free',
                    data: []
                }, {
                    name: 'Used',
                    data: []
                }],
            loading: false,
            xAxis: {
                categories: [],
                tickmarkPlacement: 'on',
                title: {
                    enabled: false
                }
            },
            yAxis: {
                title: {
                    text: 'MiB'
                }
            }
        };


        $q.all([$http.get('/api/system/info'), $http.get('/api/system/env/java.(runtime|vm).*')]).then(function (values) {
            $scope.info = values[0].data;
            $scope.info.env = values[1].data;
        });

        $scope.refresh = function () {
            var formatKibiBytes = function (bytes) {
                return Math.round((bytes / Math.pow(1024, Math.floor(1))) * 10) / 10;
            };

            $http.get('/api/system/metrics').success(function (data) {
                $scope.metrics = data;
                $scope.metrics["mem.used"] = $scope.metrics.mem - $scope.metrics["mem.free"];
                $scope.humanizedUptime = moment.duration($scope.metrics.uptime).humanize();
                var max = 10;
                var cur = $scope.memoryConfig.series[0].data.length;

                if (cur === max) {
                    $scope.memoryConfig.series[0].data.splice(0, 1);
                    $scope.memoryConfig.series[1].data.splice(0, 1);
                    $scope.memoryConfig.xAxis.categories.splice(0, 1);
                }
                $scope.memoryConfig.series[0].data.push(formatKibiBytes($scope.metrics["mem.free"]));
                $scope.memoryConfig.series[1].data.push(formatKibiBytes($scope.metrics["mem.used"]));
                $scope.memoryConfig.xAxis.categories.push($filter('date')(new Date(), "HH:mm:ss"));
            });
        };

        var timer = $interval(function () {
            $scope.refresh();
        }, $scope.refreshInterval * 1000);
        $scope.$on("$destroy", function () {
            $interval.cancel(timer);
        });
        $scope.refresh();
    }]);