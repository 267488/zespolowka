/* 
 * Copyright 2016 michael-simons.eu.
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


var app1 = angular.module('szkola',[]);

app1.controller('szkolaCtrl',function ($scope,$http,$location,$window)    
{
        $scope.box;
        $scope.szukane;
            
        $http.get('/currentSchool').success(function(data) {
        console.log("get method");
        console.log(data);
	$scope.currentSchool = data;
        
        });
        
        $http.get('/api/przedmioty?all=true').success(function (data) {
        $scope.przedmioty = data;
        console.log(data);
        });
        
        $http.get('/api/profile?all=true').success(function (data) {
            $scope.profile = data;
            console.log("profile: ");
            console.log(data);
        });
        
        $scope.profil = {
            nazwa: null,
            boxArray: null,
            boxArray2:null
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

        $scope.dodajProfil = function () {
            $scope.submitting = true;
            
            $scope.profil.boxArray = [];
            $scope.profil.boxArray2 = [];
            //pobieranie wartosci checkboxow przedmiotow
            angular.forEach($scope.przedmioty, function(przedmiot){
            if (!!przedmiot.selected) {$scope.profil.boxArray.push(przedmiot.id);$scope.profil.boxArray2.push(przedmiot.name);}
                
            });
            console.log($scope.profil.nazwa);
            console.log($scope.profil.boxArray);
            console.log($scope.profil.boxArray2);
            $http({
                method: 'POST',
                url: '/api/setProfil',
                headers: {'Content-Type': 'application/json','Accept': 'application/json'},
                data: {nazwa:$scope.profil.nazwa,przedmiotIds:$scope.profil.boxArray,przedmiotNazwy:$scope.profil.boxArray2}
            }).success(function (data) {
                if(data)
                {               
                console.log(data);
                $http.get('/api/profile?all=true').success(function (data) {
                $scope.profile = data;              
                });
                }else{console.log(data);}
            }).error(function (data, status) {
                console.log("error");
                console.log(data);
            });
        };
        
        
        $scope.Szukaj = function () {
            $scope.box = [];
            //$scope.szukane = document.getElementById("SzukajSzkoly").value;
            angular.forEach($scope.przedmioty, function(przedmiot){
            if(!!przedmiot.selected) {$scope.box.push(przedmiot.id);}});
            console.log("SZUKANE " + $scope.szukane);
            $http({
                method: 'GET',
                url: '/api/szukaneSzkoly/' + $scope.szukane
            }).success(function (data) {
                $scope.szkola = data;
            }).error(function (data, status) {

            });
        };
    
});