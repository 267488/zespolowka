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
biking2Controllers.controller('IndexCtrl', ['$scope', '$http', '$interval', '$upload', '$modal', function ($scope, $http, $interval, $upload, $modal) {
        $http.get('/api/ulubioneSzkoly?all=true').success(function (data) {
            $scope.ulubione_szkoly = data;
        });
        $http.get('/api/ProponowaneSzkoly?all=true').success(function (data) {
            $scope.proponowaneSzkoly = data;

        });
        $http.get('/api/CurrentUczen?all=true').success(function (data) {
            $scope.uczen = data;
            $scope.zdjecie = "img/brak.jpg";
            if (data.galleryId.id != null) {
                $scope.zdjecie = "/api/galleryStudent/" + data.galleryId.id + ".jpg";
            }
        });
        $http.get('/api/aktualnosciSzkola?all=true').success(function (data) {
            $scope.aktualnosci = data;
        });

        $scope.NewAktualnosc = {
            userId: null,
            tytul: null,
            tekst: null,
            id: null
        };

        $scope.submit = function () {
            $scope.submitting = true;
            alert("dodaje aktualnosc");
            $http({
                method: 'POST',
                url: '/api/aktualnosciSzkola',
                data: $scope.NewAktualnosc
            }).success(function (data) {
                $scope.submitting = false;
                $http.get('/api/aktualnosciSzkola?all=true').success(function (data) {
                    $scope.aktualnosci = data;
                });
                $scope.NewAktualnosc = {
                    userId: null,
                    tytul: null,
                    tekst: null,
                    id: null
                };
                $modal.close(data);
            }).error(function (data, status) {
                $scope.submitting = false;
                if (status === 400)
                    $scope.badRequest = data;
                else if (status === 409)
                    $scope.badRequest = 'Blad, prawdopodobnie ID_szkoly';
            });
        };

        $scope.imageData = null;
        $scope.onFileSelect = function ($files) {
            $scope.imageData = $files[0];
        };

        $scope.ZaladujObrazek = function (event) {
            $scope.id = event.target.id;
            $upload.upload({
                method: 'POST',
                url: '/api/galleryPictures/' + $scope.id,
                file: $scope.imageData,
                fileFormDataName: 'imageData',
                withCredentials: true
            }).success(function (data) {
                $http.get('/api/aktualnosciSzkola?all=true').success(function (data) {
                    $scope.aktualnosci = data;
                });

                console.log("SUCCESS");
            }).error(function (data) {
                $scope.submitting = false;
                console.log("UNSUCCESS");
                $scope.badRequest = 'There\'s something wrong with your input, please check!';
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
    }]);

biking2Controllers.controller('Index2Ctrl', ['$scope', '$http', '$interval', '$upload', '$modal', function ($scope, $http, $interval, $upload, $modal) {
        $http.get('/api/CurrentSzkola?all=true').success(function (data) {
            $scope.szkola = data;
            $scope.zdjecie = "img/brak.jpg";
            if (data.userId.galleryId.id != null) {
                $scope.zdjecie = "/api/gallerySchool/" + data.userId.galleryId.id + ".jpg";
            }
        });

        $http.get('/api/profileCurrentSchool?all=true').success(function (data) {
            $scope.profile = data;
        });

        $http.get('/api/kolkaZainteresowanCurrentSchool?all=true').success(function (data) {
            $scope.kolka = data;
        });
        $http.get('/api/osiagnieciaCurrentUser?all=true').success(function (data) {
            $scope.osiagniecia = data;
        });
        $http.get('/api/aktualnosciCurrentSzkola?all=true').success(function (data) {
            $scope.aktualnosci = data;
        });
        $http.get('/api//CurrentUser/postcount?all=true').success(function (data) {
            $scope.ilosc = data;
        });
        $scope.NewAktualnosc = {
            userId: null,
            tytul: null,
            tekst: null,
            id: null
        };

        $scope.submit = function () {
            $scope.submitting = true;
            $http({
                method: 'POST',
                url: '/api/aktualnosciSzkola',
                data: $scope.NewAktualnosc
            }).success(function (data) {
                $scope.submitting = false;
                $http.get('/api/aktualnosciCurrentSzkola?all=true').success(function (data) {
                    $scope.aktualnosci = data;
                });
                $http.get('/api//CurrentUser/postcount?all=true').success(function (data) {
                    $scope.ilosc = data;
                });
                $scope.NewAktualnosc = {
                    userId: null,
                    tytul: null,
                    tekst: null,
                    id: null
                };
                $modal.close(data);
            }).error(function (data, status) {
                $scope.submitting = false;
                if (status === 400)
                    $scope.badRequest = data;
                else if (status === 409)
                    $scope.badRequest = 'Blad, prawdopodobnie ID_szkoly';
            });
        };

        $scope.imageData = null;
        $scope.onFileSelect = function ($files) {
            $scope.imageData = $files[0];
        };

        $scope.ZaladujObrazek = function (event) {
            $scope.id = event.target.id;
            $upload.upload({
                method: 'POST',
                url: '/api/galleryPictures/' + $scope.id,
                file: $scope.imageData,
                fileFormDataName: 'imageData',
                withCredentials: true
            }).success(function (data) {
                $http.get('/api/aktualnosciCurrentSzkola?all=true').success(function (data) {
                    $scope.aktualnosci = data;
                });

                console.log("SUCCESS");
            }).error(function (data) {
                $scope.submitting = false;
                console.log("UNSUCCESS");
                $scope.badRequest = 'There\'s something wrong with your input, please check!';
            });
        };
    }]);
//////////////////////// UCZEN CONTROLLER /////////////////

biking2Controllers.controller('UczenCtrl', ['$scope', '$http', '$modal', '$upload', function ($scope, $http, $modal, $upload) {
        $http.get('/api/CurrentUczen?all=true').success(function (data) {
            $scope.uczen = data;
            $scope.zdjecie = "img/brak.jpg";
            if (data.galleryId.id != null) {
                $scope.zdjecie = "/api/galleryUser/" + data.galleryId.id + ".jpg";
            }
        });
        $http.get('/api/zainteresowaniaUcznia?all=true').success(function (data) {
            $scope.zainteresowania = data;
        });
        $http.get('/api//StopnieZainteresowanUcznia?all=true').success(function (data) {
            $scope.stopnie = data;
        });
        $http.get('/api/CurrentUczen/zainteresowaniamax?all=true').success(function (data) {
            $scope.max = data;
        });
        $http.get('/api/aktualnosciCurrentUczen?all=true').success(function (data) {
            $scope.aktualnosci = data;
        });
        $http.get('/api//CurrentUser/postcount?all=true').success(function (data) {
            $scope.ilosc = data;
        });

        $scope.NewAktualnosc = {
            userId: null,
            tytul: null,
            tekst: null,
            id: null
        };

        $scope.submit = function () {
            $scope.submitting = true;
            $http({
                method: 'POST',
                url: '/api/aktualnosciSzkola',
                data: $scope.NewAktualnosc
            }).success(function (data) {
                $scope.submitting = false;
                $http.get('/api/aktualnosciCurrentUczen?all=true').success(function (data) {
                    $scope.aktualnosci = data;
                });
                $http.get('/api//CurrentUser/postcount?all=true').success(function (data) {
                    $scope.ilosc = data;
                });
                $scope.NewAktualnosc = {
                    userId: null,
                    tytul: null,
                    tekst: null,
                    id: null
                };
                $modal.close(data);
            }).error(function (data, status) {
                $scope.submitting = false;
                if (status === 400)
                    $scope.badRequest = data;
                else if (status === 409)
                    $scope.badRequest = 'Blad, prawdopodobnie ID_szkoly';
            });
        };

        $scope.imageData = null;
        $scope.onFileSelect = function ($files) {
            $scope.imageData = $files[0];
        };

        $scope.ZaladujObrazek = function (event) {
            $scope.id = event.target.id;
            $upload.upload({
                method: 'POST',
                url: '/api/galleryPictures/' + $scope.id,
                file: $scope.imageData,
                fileFormDataName: 'imageData',
                withCredentials: true
            }).success(function (data) {
                $http.get('/api/aktualnosciCurrentUczen?all=true').success(function (data) {
                    $scope.aktualnosci = data;
                });

                console.log("SUCCESS");
            }).error(function (data) {
                $scope.submitting = false;
                console.log("UNSUCCESS");
                $scope.badRequest = 'There\'s something wrong with your input, please check!';
            });
        };
    }]);
///////////////// ADD NEW UCZEN CONTROLLER /////////////////////

biking2Controllers.controller('AddNewUczenCtrl', ['$scope', '$modalInstance', '$http', '$upload', function ($scope, $modalInstance, $http, $upload) {
        $scope.uczen = {
            id: null,
            pesel: null,
            name: null,
            lastname: null,
            mail: null,
            czegoSzukam: null,
            kodpocztowy: null,
            galleryId: null
        };
        $scope.imageData = null;
        $scope.onFileSelect = function ($files) {
            $scope.imageData = $files[0];
        };
        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
        $scope.ZaladujObrazek = function () {
            console.log("Załaduj obrazek");
            $upload.upload({
                method: 'POST',
                url: '/api/galleryStudent',
                file: $scope.imageData,
                fileFormDataName: 'imageData',
                withCredentials: true
            }).success(function (data) {
                //$modalInstance.close(data);
                console.log("SUCCESS");
            }).error(function () {
                $scope.submitting = false;
                console.log("UNSUCCESS");
                $scope.badRequest = 'There\'s something wrong with your input, please check!';
            });
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
/////////////////// EDIT UCZEN CONTROLLER //////////////////////////

biking2Controllers.controller('EditUczenCtrl', ['$scope', '$modal', '$http', '$upload', function ($scope, $modal, $http, $upload) {
        $http.get('/api/CurrentUczen?all=true').success(function (data) {
            $scope.uczen = data;
            $scope.zdjecie = "img/brak.jpg";
            if (data.galleryId.id != null) {
                $scope.zdjecie = "/api/galleryStudent/" + data.galleryId.id + ".jpg";
            }
        });
        $scope.password = null;
        $scope.password2 = null;
        $scope.imageData = null;
        $scope.onFileSelect = function ($files) {
            $scope.imageData = $files[0];
        };

        $scope.EdytujObrazek = function () {
            $http.delete('/api/galleryStudentDelete')
                    .success(function (data) {
                        console.log("Poprzednie zdjęcie zostało usunięte");
                    })
                    .error(function (data) {
                        console.log("Poprzednie zdjęcie nie zostało usunięte");
                    });


            $upload.upload({
                method: 'POST',
                url: '/api/galleryStudent',
                file: $scope.imageData,
                fileFormDataName: 'imageData',
                withCredentials: true
            }).success(function (data) {
                //$modalInstance.close(data);
                console.log("SUCCESS");
            }).error(function () {
                $scope.submitting = false;
                console.log("UNSUCCESS");
                $scope.badRequest = 'There\'s something wrong with your input, please check!';
            });
        };
        $scope.submit = function () {
            $scope.submitting = true;
            if ($scope.password != null && $scope.password == $scope.password2) {
                console.log("Póki co nie mogę zmienić hasła");
            }
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
        $scope.editPicture = function () {
            alert("EDIT PICTURE");
            var modalInstance = $modal.open({
                templateUrl: '/partials/_new_picture.html',
                controller: 'AddNewPictureCtrl',
                scope: $scope
            });
            modalInstance.result.then(
                    function (newPicture) {
                        $http.get('/api/CurrentUczen?all=true').success(function (data) {
                            $scope.uczen = data;
                            $scope.zdjecie = "img/brak.jpg";
                            if (data.galleryId.id != null) {
                                $scope.zdjecie = "/api/galleryUser/" + data.galleryId.id + ".jpg";
                            }
                        });
                    },
                    function () {
                    }
            );
        };
        $scope.deletePicture = function () {
            alert("DELETE");
            $http.delete('/api/pictureDelete/')
                    .success(function (data) {
                        $http.get('/api/CurrentUczen?all=true').success(function (data) {
                            $scope.uczen = data;
                            $scope.zdjecie = "img/brak.jpg";
                            if (data.galleryId.id != null) {
                                $scope.zdjecie = "/api/galleryUser/" + data.galleryId.id + ".jpg";
                            }
                        });
                    })
                    .error(function (data) {

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
                    });
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
biking2Controllers.controller('AddNewSzkolaCtrl', ['$scope', '$modalInstance', '$http', '$upload', function ($scope, $modalInstance, $http, $upload) {
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
            rodzajGwiazdki: null,
            galleryId: null
        };
        $scope.imageData = null;
        $scope.onFileSelect = function ($files) {
            $scope.imageData = $files[0];
        };
        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
        $scope.ZaladujObrazek = function () {
            $upload.upload({
                method: 'POST',
                url: '/api/gallerySchool',
                file: $scope.imageData,
                fileFormDataName: 'imageData',
                withCredentials: true
            }).success(function (data) {
                if (data) {

                    $scope.szkola.galleryId = data.getBody();
                    console.log("GALLERY " + $scope.szkola.galleryId);
                }
                //$modalInstance.close(data);
            }).error(function () {
                $scope.submitting = false;
                $scope.badRequest = 'There\'s something wrong with your input, please check!';
            });
        };
        $scope.submit = function () {
            $scope.submitting = true;
            console.log("GALLERY ID" + $scope.szkola.galleryId);
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
biking2Controllers.controller('EditSzkolaCtrl', ['$scope', '$http', '$modal', function ($scope, $http, $modal) {
        $http.get('/api/przedmioty?all=true').success(function (data) {
            $scope.przedmioty = data;
        });
        $http.get('/api/CurrentSzkola?all=true').success(function (data) {
            $scope.szkola = data;
            $scope.zdjecie = "img/brak.jpg";
            if (data.galleryId.id != null) {
                $scope.zdjecie = "/api/galleryUser/" + data.galleryId.id + ".jpg";
            }
        });
        $http.get('/api/profileCurrentSchool?all=true').success(function (data) {
            $scope.profile = data;
        });

        $http.get('/api/kolkaZainteresowanCurrentSchool?all=true').success(function (data) {
            $scope.kolka = data;
        });
        $http.get('/api/osiagnieciaCurrentUser?all=true').success(function (data) {
            $scope.osiagniecia = data;
        });

        $scope.profil = {
            id: null,
            nazwa: null,
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
            } else {
                // remove item
                for (var i = 0; i < $scope.rozszerzonePrzedmioty.length; i++) {
                    if ($scope.rozszerzonePrzedmioty[i].przedmiotId === przedmiot.id) {
                        $scope.rozszerzonePrzedmioty.splice(i, 1);
                    }
                }
            }
        };

        $scope.submit = function () {
            $scope.submitting = true;
            console.log("SUBMIT " + $scope.profil.nazwa);
            $http({
                method: 'POST',
                url: '/api/profile/' + $scope.profil.nazwa,
                data: $scope.rozszerzonePrzedmioty
            }).success(function (data) {
                $scope.submitting = false;
                $http.get('/api/profileCurrentSchool?all=true').success(function (data) {
                    $scope.profile = data;
                });
            }).error(function (data, status) {
                $scope.submitting = false;
                if (status === 400)
                    $scope.badRequest = data;
                else if (status === 409)
                    $scope.badRequest = 'Profil o takiej nazwie juz istnieje';
            });
        };

        $scope.submit2 = function () {
            $scope.submitting = true;
            $http({
                method: 'PUT',
                url: '/api/szkola',
                data: $scope.szkola
            }).success(function (data) {
                $scope.submitting = false;
                $modal.close(data);
            }).error(function (data, status) {
                $scope.submitting = false;
                if (status === 400)
                    $scope.badRequest = data;
                else if (status === 409)
                    $scope.badRequest = 'Szkola o takiej nazwie juz istnieje';
            });
        };
        $scope.UsunProfil = function (event) {
            $scope.id = event.target.id;
            console.log("Profil ID");
            alert(" USUN PROFIL " + $scope.id);
            $http.delete('/api/profilDelete/' + $scope.id)
                    .success(function (data) {
                        $http.get('/api/profileCurrentSchool?all=true').success(function (data) {
                            $scope.profile = data;
                        });
                    })
                    .error(function (data) {

                    });
        };

        $scope.AddNewProfil = function () {
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
        $scope.openNewOsiagniecieDlg = function () {
            var modalInstance = $modal.open({
                templateUrl: '/partials/_new_osiagniecie.html',
                controller: 'AddNewOsiagniecieCtrl',
                scope: $scope
            });
            modalInstance.result.then(
                    function (newOsiagniecie) {
                        $http.get('/api/osiagnieciaCurrentUser?all=true').success(function (data) {
                            $scope.osiagniecia = data;
                        });
                        $scope.osiagniecia.push(newOsiagniecie);
                    },
                    function () {
                    }
            );
        };
        $scope.openNewKolkoDlg = function () {
            var modalInstance = $modal.open({
                templateUrl: '/partials/_new_kolko.html',
                controller: 'AddNewKolkoCtrl',
                scope: $scope
            });
            modalInstance.result.then(
                    function (newKolko) {
//                        $http.get('/api/kolkaZainteresowanCurrentSchool?all=true').success(function (data) {
//                            $scope.kolka = data;
//                        });
                        $scope.kolka.push(newKolko);
                    },
                    function () {
                    }
            );
        };
        $scope.editPicture = function () {
            alert("EDIT PICTURE");
            var modalInstance = $modal.open({
                templateUrl: '/partials/_new_picture.html',
                controller: 'AddNewPictureCtrl',
                scope: $scope
            });
            modalInstance.result.then(
                    function (newPicture) {
                        $http.get('/api/CurrentSzkola?all=true').success(function (data) {
                            $scope.szkola = data;
                            $scope.zdjecie = "img/brak.jpg";
                            if (data.galleryId.id != null) {
                                $scope.zdjecie = "/api/galleryUser/" + data.galleryId.id + ".jpg";
                            }
                        });
                    },
                    function () {
                    }
            );
        };
        $scope.deletePicture = function () {
            alert("DELETE");
            $http.delete('/api/pictureDelete/')
                    .success(function (data) {
                        $http.get('/api/CurrentSzkola?all=true').success(function (data) {
                            $scope.szkola = data;
                            $scope.zdjecie = "img/brak.jpg";
                            if (data.galleryId.id != null) {
                                $scope.zdjecie = "/api/galleryUser/" + data.galleryId.id + ".jpg";
                            }
                        });
                    })
                    .error(function (data) {

                    });
        };
    }]);
//AKTUALNOSCI_SZKOLA CONTROLLER

biking2Controllers.controller('AktualnosciSzkolaCtrl', ['$scope', '$http', '$modal', '$upload', function ($scope, $http, $modal, $upload) {
        $http.get('/api/aktualnosciSzkola?all=true').success(function (data) {
            $scope.aktualnosci = data;
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
        $scope.imageData = null;
        $scope.onFileSelect = function ($files) {
            $scope.imageData = $files[0];
        };
        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
        $scope.ZaladujObrazek = function (event) {
            $scope.id = event.target.id;
            console.log("Załaduj obrazek");
            $upload.upload({
                method: 'POST',
                url: '/api/galleryPictures/' + $scope.id,
                file: $scope.imageData,
                fileFormDataName: 'imageData',
                withCredentials: true
            }).success(function (data) {
                $http.get('/api/aktualnosciSzkola?all=true').success(function (data) {
                    $scope.aktualnosci = data;
                });

                console.log("SUCCESS");
            }).error(function (data) {
                $scope.submitting = false;
                console.log("UNSUCCESS");
                $scope.badRequest = 'There\'s something wrong with your input, please check!';
            });
        };
    }]);
//ADD NEW ATUALNOSCI_SZKOLA CONTROLLER

biking2Controllers.controller('AddNewAktualnosciSzkolaCtrl', ['$scope', '$modalInstance', '$http', function ($scope, $modalInstance, $http) {
        $scope.aktualnosci = {
            userId: null,
            tytul: null,
            tekst: null,
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
                data: $scope.aktualnosci
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

        $http.get('/api/przedmioty?all=true').success(function (data) {
            $scope.przedmioty = data;
        });
        $scope.profil = {
            id: null,
            nazwa: null,
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
            console.log("Profil_nazwaId" + $scope.profil.nazwa);
            $http({
                method: 'POST',
                url: '/api/profile/' + $scope.profil.nazwa,
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
//Kolka Zainteresowan
biking2Controllers.controller('KolkaCtrl', ['$scope', '$http', '$modal', function ($scope, $http, $modal) {
        $http.get('/api/kolkaZainteresowan?all=true').success(function (data) {
            $scope.kolka = data;
        });
        $scope.openNewKolkoDlg = function () {
            var modalInstance = $modal.open({
                templateUrl: '/partials/_new_kolko.html',
                controller: 'AddNewKolkoCtrl',
                scope: $scope
            });
            modalInstance.result.then(
                    function (newKolko) {
                        $http.get('/api/kolkaZainteresowan?all=true').success(function (data) {
                            $scope.kolka = data;
                        });
                        //$scope.kolkaZainteresowan.push(newKolko);
                    },
                    function () {
                    }
            );
        };
    }]);
biking2Controllers.controller('AddNewKolkoCtrl', ['$scope', '$modalInstance', '$http', function ($scope, $modalInstance, $http) {
        $http.get('/api/przedmioty?all=true').success(function (data) {
            $scope.przedmioty = data;
        });
        $scope.kolko = {
            id: null,
            nazwa: null,
            termin: null,
            przedmiot: null,
            szkola: null
        };
        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
        $scope.submit = function () {
            $scope.submitting = true;
            console.log("TERMIN " + $scope.kolko.termin);
            console.log("NAZWA " + $scope.kolko.nazwa);
            console.log("PRZEDMIOT" + $scope.kolko.przedmiot);
            $http({
                method: 'POST',
                url: '/api/kolkaZainteresowan/' + $scope.kolko.przedmiot,
                data: $scope.kolko
            }).success(function (data) {
                $scope.submitting = false;
                $modalInstance.close(data);
            }).error(function (data, status) {
                $scope.submitting = false;
                if (status === 400)
                    $scope.badRequest = data;
                else if (status === 409)
                    $scope.badRequest = 'Kolko o takiej nazwie juz istnieje';
            });
        };
    }]);
/**********************************************************************/

//Osiagniecia
biking2Controllers.controller('OsiagnieciaCtrl', ['$scope', '$http', '$modal', function ($scope, $http, $modal) {
        $http.get('/api/osiagniecia?all=true').success(function (data) {
            $scope.osiagniecia = data;
        });
        $scope.openNewOsiagniecieDlg = function () {
            var modalInstance = $modal.open({
                templateUrl: '/partials/_new_osiagniecie.html',
                controller: 'AddNewOsiagniecieCtrl',
                scope: $scope
            });
            modalInstance.result.then(
                    function (newOsiagniecie) {
                        $http.get('/api/osiagniecia?all=true').success(function (data) {
                            $scope.osiagniecia = data;
                        });
                        $scope.osiagniecia.push(newOsiagniecie);
                    },
                    function () {
                    }
            );
        };
    }]);
biking2Controllers.controller('AddNewOsiagniecieCtrl', ['$scope', '$modalInstance', '$http', function ($scope, $modalInstance, $http) {
        $http.get('/api/przedmioty?all=true').success(function (data) {
            $scope.przedmioty = data;
        });
        $scope.osiagniecie = {
            id: null,
            nazwakonkursu: null,
            termin: null,
            przedmiot: null,
            szczebel: null,
            nagroda: null,
            userId: null
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
            console.log("TERMIN " + $scope.osiagniecie.termin);
            $http({
                method: 'POST',
                url: '/api/osiagniecia/' + $scope.osiagniecie.przedmiot,
                data: $scope.osiagniecie,
                withCredentials: true,
                formDataAppender: function (formData, key, val) {
                    if (key !== null && key === 'termin')
                        formData.append(key, val.toISOString());
                    else
                        formData.append(key, val);
                }
            }).success(function (data) {
                $scope.submitting = false;
                $modalInstance.close(data);
            }).error(function (data, status) {
                $scope.submitting = false;
                if (status === 400)
                    $scope.badRequest = data;
                else if (status === 409)
                    $scope.badRequest = 'Osiagniecie o takiej nazwie juz istnieje';
            });
        };
    }]);
/**********************************************************************/

/*********************************************************************/

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
biking2Controllers.controller('AddNewPictureCtrl', ['$data', '$scope', '$modalInstance', '$upload', function ($data, $scope, $modalInstance, $upload) {
        $scope.imageData = null;
        $scope.onFileSelect = function ($files) {
            $scope.imageData = $files[0];
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
        $scope.submit = function () {
            console.log("DATA " + $data);
            $scope.submitting = true;
            $upload.upload({
                method: 'POST',
                url: '/api/galleryUserEdit/' + $data,
                file: $scope.imageData,
                fileFormDataName: 'imageData',
                withCredentials: true
            }).success(function (data) {
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

biking2Controllers.controller('myCtrl', function ($rootScope, $scope, $http, $location, $window) {
    $scope.user = {
        login: '',
        password: ''
    };


    $scope.login = function () {

        console.log("login function");
        $http({
            method: 'POST',
            url: '/test',
            headers: {'Content-Type': 'application/json', 'Accept': 'application/json'},
            data: {login: $scope.user.login, password: $scope.user.password}
        }).then(function mySucces(response) {
            console.log(response.data);

            $scope.id = response.data.id;
            $window.sessionStorage.setItem('myItem', $scope.id);


            switch (response.data.role.toString())
            {
                case 'SZKOLA':
                    $window.location.href = '/szkola.html';
                    console.log("przechodze przez SZKOLA");
                    break;
                case 'ADMIN':
                    $window.open('/admin.html', '_self');
                    alert("przechodze przez ADMIN");
                    break;
                case 'UCZEN':
                    $window.location.href = '/index.html';
                    console.log("przechodze przez UCZEN");
                    break;
                default:
                    $window.location.href = '/error.html';
                    break;
            }
            ;


        }, function myError(response) {
            $scope.myWelcome = response.statusText;
            console.log("error");
        });
    };

}); 