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
        
         $scope.ileDanych = 0;
        $scope.licznik = 0;
        $scope.licznikKolo = 0;
        



        
        $http.get('/api/ProponowaneSzkoly').success(function (data) {
            $scope.proponowaneSzkoly = data;

        });
        $http.get('/api/ulubioneSzkoly').success(function (data) {
            $scope.ulubione_szkoly = data;
        });
        $http.get('/api/CurrentUczen').success(function (data) {
            $scope.uczen = data;
            
            if($scope.uczen.mail!=null)$scope.licznik+=1;
            if($scope.uczen.password!=null)$scope.licznik+=1;
            if($scope.uczen.name!=null)$scope.licznik+=1;
            if($scope.uczen.lastname!=null)$scope.licznik+=1;
            if($scope.uczen.miasto!=null)$scope.licznik+=1;
            if($scope.uczen.kodpocztowy!=null)$scope.licznik+=1;
            if($scope.uczen.adres!=null)$scope.licznik+=1;
            if($scope.uczen.czegoSzukam!=null)$scope.licznik+=1;
            $scope.licznikKolo = ($scope.licznik/8)*100;
            
            console.log($scope.licznik);
            console.log($scope.licznikKolo);
            
            
            $scope.zdjecie = "";
            if ($scope.uczen.galleryId.id) {
                $scope.zdjecie = "/api/galleryUser/" + $scope.uczen.galleryId.id + ".jpg";
            } else {
                $scope.zdjecie = "img/brak.jpg";
            }
            console.log("get current user");
            
        });    
        

        $http.get('/api/aktualnosciSzkola').success(function (data) {
            $scope.aktualnosci = data;
        });

        $scope.NewAktualnosc = {
            userId: null,
            tytul: null,
            tekst: null,
            id: null
        };

        $scope.imageData = null;
        $scope.onFileSelect = function ($files) {
            $scope.imageData = $files[0];
        };

        $scope.submit = function () {
            $scope.submitting = true;
            $http({
                method: 'POST',
                url: '/api/aktualnosciSzkola',
                data: $scope.NewAktualnosc
            }).success(function (data) {
                $scope.aktual = data;
                $upload.upload({
                    method: 'POST',
                    url: '/api/galleryPictures/' + $scope.aktual.id,
                    file: $scope.imageData,
                    fileFormDataName: 'imageData',
                    withCredentials: true
                }).success(function (data) {
                    $http.get('/api/aktualnosciSzkola?all=true').success(function (data) {
                        $scope.aktualnosci = data;
                    });
                    $scope.imageData = null;
                    console.log("SUCCESS");
                }).error(function (data) {
                    $scope.submitting = false;
                    console.log("UNSUCCESS");
                    $scope.badRequest = 'There\'s something wrong with your input, please check!';
                });
                $scope.submitting = false;
                $http.get('/api/aktualnosciSzkola?all=true').success(function (data) {
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
            }).error(function (data, status) {
                $scope.submitting = false;
                if (status === 400)
                    $scope.badRequest = data;
                else if (status === 409)
                    $scope.badRequest = 'Blad, prawdopodobnie ID_szkoly';
            });
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
                $modal.close(data);
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
        $http.get('/api/CurrentSzkola').success(function (data) {
            $scope.szkola = data;
            console.log($scope.szkola);
            if (data.galleryId.id) {
                $scope.zdjecie = "/api/galleryUser/" + data.galleryId.id + ".jpg";
            } else {
                $scope.zdjecie = "img/brak.jpg";
            }
            
        });


        $scope.editSuperSzkola = function () {

        };

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

        $scope.imageData = null;
        $scope.onFileSelect = function ($files) {
            $scope.imageData = $files[0];
        };

        $scope.submit = function () {
            $scope.submitting = true;
            $http({
                method: 'POST',
                url: '/api/aktualnosciSzkola',
                data: $scope.NewAktualnosc
            }).success(function (data) {
                $scope.submitting = false;
                $scope.aktual = data;
                $upload.upload({
                    method: 'POST',
                    url: '/api/galleryPictures/' + $scope.aktual.id,
                    file: $scope.imageData,
                    fileFormDataName: 'imageData',
                    withCredentials: true
                }).success(function (data) {
                    $http.get('/api/aktualnosciCurrentUczen?all=true').success(function (data) {
                        $scope.aktualnosci = data;
                    });
                    $scope.imageData = null;
                    console.log("SUCCESS");
                }).error(function (data) {
                    $scope.submitting = false;
                    console.log("UNSUCCESS");
                    $scope.badRequest = 'There\'s something wrong with your input, please check!';
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
                $modal.close(data);
            }).error(function (data, status) {
                $scope.submitting = false;
                if (status === 400)
                    $scope.badRequest = data;
                else if (status === 409)
                    $scope.badRequest = 'Blad, prawdopodobnie ID_szkoly';
            });
        };

    }]);
//////////////////////// UCZEN CONTROLLER /////////////////

biking2Controllers.controller('UczenCtrl', ['$scope', '$http', '$modal', '$upload', function ($scope, $http, $modal, $upload) {
        $http.get('/api/CurrentUczen?all=true').success(function (data) {
            $scope.uczen = data;
            $scope.zdjecie = "";
            if ($scope.uczen.galleryId.id != null) {
                $scope.zdjecie = "/api/galleryUser/" + $scope.uczen.galleryId.id + ".jpg";
            } else {
                $scope.zdjecie = "img/brak.jpg";
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

        $scope.imageData = null;
        $scope.onFileSelect = function ($files) {
            $scope.imageData = $files[0];
        };

        $scope.submit = function () {
            $scope.submitting = true;
            $http({
                method: 'POST',
                url: '/api/aktualnosciSzkola',
                data: $scope.NewAktualnosc
            }).success(function (data) {
                $scope.aktual = data;
                $upload.upload({
                    method: 'POST',
                    url: '/api/galleryPictures/' + $scope.aktual.id,
                    file: $scope.imageData,
                    fileFormDataName: 'imageData',
                    withCredentials: true
                }).success(function (data) {
                    $http.get('/api/aktualnosciCurrentUczen?all=true').success(function (data) {
                        $scope.aktualnosci = data;
                    });
                    $scope.imageData = null;
                    console.log("SUCCESS");
                }).error(function (data) {
                    $scope.submitting = false;
                    console.log("UNSUCCESS");
                    $scope.badRequest = 'There\'s something wrong with your input, please check!';
                });
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

        $scope.ZaladujObrazek = function (id) {
            $upload.upload({
                method: 'POST',
                url: '/api/galleryPictures/' + id,
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

/////////////////// EDIT UCZEN CONTROLLER //////////////////////////

biking2Controllers.controller('EditUczenCtrl', ['$scope', '$modal', '$http', '$upload', function ($scope, $modal, $http, $upload) {

        $scope.editInfo = '';
        $scope.password2;
        $http.get('/api/CurrentUczen?all=true').success(function (data) {
            
            $scope.uczen = data;
            $scope.zdjecie = "";
            $scope.password2 = $scope.uczen.password;
            console.log($scope.uczen.password);
            console.log($scope.password2);
            console.log($scope.uczen);
            if (data.galleryId.id != null) {
                $scope.zdjecie = "/api/galleryUser/" + data.galleryId.id + ".jpg";
            } else {
                $scope.zdjecie = "img/brak.jpg";
            }
            
        });
         $scope.imageData = null;
        $scope.onFileSelect = function ($files) {
            $scope.imageData = $files[0];
        };
        $scope.submit = function () {
            console.log($scope.uczen);
            
            if($scope.uczen.password!==$scope.password2){$scope.editInfo="HASŁA NIE PASUJĄ DO SIEBIE";}
            else{
            
            $http({
                method: 'POST',
                url: '/api/editUczen',
                headers: {'Content-Type': 'application/json','Accept': 'application/json'},
                /*data:   {uczen_id:$scope.uczen.uczen_id,
                        name:$scope.uczen.name,
                        lastname:$scope.uczen.lastname,
                        mail:$scope.uczen.mail,
                        login:$scope.uczen.login,
                        password:$scope.uczen.password,
                        miasto:$scope.uczen.miasto,
                        kodpocztowy:$scope.uczen.kodpocztowy,
                        adres:$scope.uczen.adres,
                        czegoSzukam:$scope.uczen.czegoSzukam,
                        galleryId:$scope.uczen.galleryId
                        }*/
                data: $scope.uczen    
            }).success(function (data) {
                $scope.editInfo="EDYTOWANO POMYŚLNIE";
            }).error(function (data, status) {
                
                
                if (status === 400)
                {$scope.badRequest = data;
                    $scope.editInfo="BŁĄD EDYCJI";}
                else if (status === 409)
                {$scope.badRequest = 'Uczen o takim nr psl juz istnieje';   
                    $scope.editInfo="BŁĄD EDYCJI";
                }          
            });    
            }
        };
        
        $scope.imageData = null;
        $scope.onFileSelect = function ($files) {
            $scope.imageData = $files[0];
        };
        $scope.editPicture = function () {
            $http.delete('/api/pictureDelete')
                    .success(function (data) {
                    })
                    .error(function (data) {

                    });
            $upload.upload({
                method: 'POST',
                url: '/api/galleryUser',
                file: $scope.imageData,
                fileFormDataName: 'imageData',
                withCredentials: true
            }).success(function (data) {
                $scope.imageData = null;
                $http.get('/api/CurrentUczen?all=true').success(function (data) {
                    $scope.uczen = data;
                    $scope.zdjecie = "img/brak.jpg";
                    if ($scope.uczen.galleryId.id != null) {
                        $scope.zdjecie = "/api/galleryUser/" + $scope.uczen.galleryId.id + ".jpg";
                    }
                });
                alert("DODANO ZDJECIE");
                console.log("SUCCESS");
            }).error(function (data) {
                $scope.submitting = false;
                console.log("UNSUCCESS");
                $scope.badRequest = 'There\'s something wrong with your input, please check!';
            });
        };
        
        $scope.deletePicture = function () {
            $http.delete('/api/pictureDelete')
                    .success(function (data) {
                        $http.get('/api/CurrentUczen?all=true').success(function (data) {
                            $scope.uczen = data;
                            $scope.zdjecie = "img/brak.jpg";
                            if (data.galleryId.id != null) {
                                $scope.zdjecie = "/api/galleryUser/" + data.galleryId.id + ".jpg";
                            }
                            alert("DELETE");
                        });
                    })
                    .error(function (data) {

                    });
        };
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
    }]);
biking2Controllers.controller('Szkola2Ctrl', ['$scope', '$http', '$modal', function ($scope, $http, $modal) {
        $http.get('/api/szkola2?all=true').success(function (data) {
            $scope.szkola = data;
        });
        $http.get('/api/przedmioty?all=true').success(function (data) {
            $scope.przedmioty = data;
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
                $modalInstance.close(data);
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
biking2Controllers.controller('EditSzkolaCtrl', ['$scope', '$http', '$modal', '$upload', function ($scope, $http, $modal, $upload) {

        $scope.szkola1 = {
            id:null,
            login:null,
            password:null,
            mail:null,
            name:null,
            numer:null,
            miasto:null,
            adres:null,
            kodpocztowy:null,
            typSzkoly:null,
            rodzajSzkoly:null,
            galleryId:null
        };  
        $scope.tmp_password;
        $scope.editError;
        $http.get('/api/przedmioty?all=true').success(function (data) {
            $scope.przedmioty = data;
        });
        $http.get('/api/CurrentSzkola').success(function (data) {
            $scope.szkola = data;
            $scope.tmp_password = $scope.szkola.password;
            $scope.zdjecie = "";
            console.log($scope.szkola);
            if (data.galleryId.id) {
                $scope.zdjecie = "/api/galleryUser/" + data.galleryId.id + ".jpg";
            } else {
                $scope.zdjecie = "img/brak.jpg";
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
        $scope.editInfoColor = '';
        $scope.submit2 = function () {
               
            console.log($scope.szkola1); 
            //$scope.szkola.galleryId=null;

            $scope.submitting = true;

            if ($scope.szkola.password !== $scope.tmp_password)
            {
                $scope.editInfoColor = "red";
                $scope.editError = 'Hasła nie pasują do siebie';
            } else {
                $http({
                    method: 'POST',
                    url: '/api/editSchool',
                    headers: {'Content-Type': 'application/json','Accept': 'application/json'},
                    data: $scope.szkola
                }).success(function (data) {
                    $scope.editInfoColor = "green";
                    $scope.editError = "edytowano pomyślnie";
                    $modal.close(data);
                                $http.get('/api/CurrentSzkola').success(function (data) {
                                        $scope.szkola1 = data;
                                        $scope.tmp_password = $scope.szkola1.password;
                                        $scope.zdjecie = "";
                                        console.log($scope.szkola1);
                                        if (data.galleryId.id) {
                                            $scope.zdjecie = "/api/galleryUser/" + data.galleryId.id + ".jpg";
                                        } else {
                                            $scope.zdjecie = "img/brak.jpg";
                                        }
            
                                });
                }).error(function (data, status) {
                    
                    if (status === 400)
                    {
                        $scope.editInfoColor = "red";
                        $scope.editError = 'Szkola o takiej nazwie juz istnieje';
                    } else if (status === 409)
                    {
                        $scope.editInfoColor = "red";
                        $scope.editError = 'Szkola o takiej nazwie juz istnieje';
                    }
                });
            }
            //            $http.delete('/api/pictureDelete')
//                    .success(function (data) {
//                    })
//                    .error(function (data) {
//
//                    });
            $upload.upload({
                method: 'POST',
                url: '/api/galleryUser',
                file: $scope.imageData,
                fileFormDataName: 'imageData',
                withCredentials: true
            }).success(function (data) {
                $scope.imageData = null;
                $http.get('/api/CurrentSzkola').success(function (data) {
                    $scope.szkola = data;
                    if (data.galleryId.id) {
                        $scope.zdjecie = "/api/galleryUser/" + data.galleryId.id + ".jpg";
                    } else {
                        $scope.zdjecie = "img/brak.jpg";
                    }
                });
                alert("DODANO ZDJECIE");
                console.log("SUCCESS");
            }).error(function (data) {
                $scope.submitting = false;
                console.log("UNSUCCESS");
                $scope.badRequest = 'There\'s something wrong with your input, please check!';
            });
        };
        $scope.UsunProfil = function (profil) {
            $http.delete('/api/profilDelete/' + profil.id)
                    .success(function () {
                        $http.get('/api/profileCurrentSchool?all=true').success(function (data) {
                            $scope.profile = data;
                        });
                        alert(" USUNIETO PROFIL " + profil.id);
                    })
                    .error(function () {

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
        $scope.submitOsiagniecia = function () {
            $scope.submitting = true;
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
                $http.get('/api/osiagnieciaCurrentUser?all=true').success(function (data) {
                    $scope.osiagniecia = data;
                });
            }).error(function (data, status) {
                $scope.submitting = false;
                if (status === 400)
                    $scope.badRequest = data;
                else if (status === 409)
                    $scope.badRequest = 'Osiagniecie o takiej nazwie juz istnieje';
            });
        };
        $scope.kolko = {
            id: null,
            nazwa: null,
            termin: null,
            przedmiot: null,
            szkola: null
        };
        $scope.submit3 = function () {
            $scope.submitting = true;
            console.log("JESTEM TU");
            $http({
                method: 'POST',
                url: '/api/kolkaZainteresowan',
                data: $scope.kolko
            }).success(function (data) {
                $scope.submitting = false;
                $http.get('/api/kolkaZainteresowanCurrentSchool?all=true').success(function (data) {
                    $scope.kolka = data;
                });
                console.log("Dodałem kółko");
            }).error(function (data, status) {
                $scope.submitting = false;
                console.log("Błąd");
                if (status === 400)
                    $scope.badRequest = data;
                else if (status === 409)
                    $scope.badRequest = 'Kolko o takiej nazwie juz istnieje';
            });
        };
        
        $scope.imageData = null;
        $scope.onFileSelect = function ($files) {
            $scope.imageData = $files[0];
        };
    
        $scope.deletePicture = function () {
            alert("DELETE");
            $http.delete('/api/pictureDelete')
                    .success(function (data) {
                        $http.get('/api/CurrentSzkola').success(function (data) {
                            $scope.szkola = data;
                            if (data.galleryId.id) {
                                $scope.zdjecie = "/api/galleryUser/" + data.galleryId.id + ".jpg";
                            } else {
                                $scope.zdjecie = "img/brak.jpg";
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

/**********************************************************************/

//Osiagniecia
biking2Controllers.controller('OsiagnieciaCtrl', ['$scope', '$http', '$modal', function ($scope, $http, $modal) {
        $http.get('/api/osiagnieciaCurrentUser?all=true').success(function (data) {
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
                url: '/api/galleryUser',
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