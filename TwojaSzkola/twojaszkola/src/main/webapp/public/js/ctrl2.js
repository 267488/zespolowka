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


var app1 = angular.module('login',[]);

app1.controller('szkolaCtrl',function ($scope,$http,$location,$window)    
{    
    $scope.tmp2 = "hej";
    $http.get('/currentSchool').success(function(data) {
        console.log("get method");
        console.log(data);
	$scope.currentSchool = data;
        
    });
    
    $http.get('/currentProfile').success(function(data) {
        console.log("get method");
        console.log(data);
	$scope.currentProfile = data;
        
    });
    
    
});

app1.controller('userCtrl',function ($scope,$http,$location,$window)    
{       
    $http.get('/api/currentUser').success(function(data) {
        console.log(data);
	$scope.currentUser = data;
    });
    
    
    $scope.tmp = $window.sessionStorage.getItem('myItem');
    console.log($scope.tmp);
});

app1.controller('adminCtrl',function ($scope,$http,$location,$window)    
{   
    $scope.boxArray;
    $scope.mailTemat = '';
    $scope.mailTresc = '';   
    $scope.newPassword = '';
    $scope.tmp1;
    
    $scope.templates =
      [ { name: 'uczniowie', url: '/partials2/uczniowie.html'},
        { name: 'szkoly', url: '/partials2/szkoly.html'} ];
    $scope.template = $scope.templates[0];
    
    
    
    $http.get('/getAllSchools').success(function(data) {
        console.log("get schools");
	$scope.allSchools = data;
    });
    
    $http.get('/getAllUsers').success(function(data) {
        console.log("get users");
	$scope.allUsers = data;
    });
    $http.get('/currentID').success(function(data) {
        
	$scope.myId = data;
    });
    
    $scope.box = function()
    {
        $scope.boxArray = [];
        angular.forEach($scope.allUsers, function(user){
        if (!!user.selected) $scope.boxArray.push(user.id);
        });
        console.log("odczytalem");
        console.log($scope.boxArray);
        
    }
    
    $scope.ban = function(){
                
                $scope.boxArray = [];
                angular.forEach($scope.allUsers, function(user){
                if (!!user.selected) $scope.boxArray.push(user.id);
                });
                console.log($scope.boxArray);
                $http({
                method : 'POST',
                url : '/setBan',
                headers: {'Content-Type': 'application/json','Accept': 'application/json'},
                data: $scope.boxArray
                }).then(function mySucces(response) {                        
                   console.log("ok");
                   $http.get('/getAllUsers').success(function(data) {
                        console.log("get users");
                        $scope.allUsers = data;
                    });
                   
                }, function myError(response) {
                    console.log("error");
                });
                
            };
    $scope.uban = function(){
                
                $scope.boxArray = [];
                angular.forEach($scope.allUsers, function(user){
                if (!!user.selected) $scope.boxArray.push(user.id);
                });
                console.log($scope.boxArray);
                $http({
                method : 'POST',
                url : '/setUBan',
                headers: {'Content-Type': 'application/json','Accept': 'application/json'},
                data: $scope.boxArray
                }).then(function mySucces(response) {                        
                   console.log("ok");
                   $http.get('/getAllUsers').success(function(data) {
                        console.log("get users");
                        $scope.allUsers = data;
                    });
                   
                }, function myError(response) {
                    console.log("error");
                });
                
            };
    $scope.banAll = function(){

                console.log($scope.boxArray);
                $http({
                method : 'POST',
                url : '/setBanAll',
                headers: {'Content-Type': 'application/json','Accept': 'application/json'},               
                }).then(function mySucces(response) {                        
                   console.log("ok");
                   $http.get('/getAllUsers').success(function(data) {
                        console.log("get users");
                        $scope.allUsers = data;
                    });
                   
                }, function myError(response) {
                    console.log("error");
                });
                
            };
    $scope.ubanAll = function(){
                
                console.log($scope.boxArray);
                $http({
                method : 'POST',
                url : '/setUBanAll',
                headers: {'Content-Type': 'application/json','Accept': 'application/json'},               
                }).then(function mySucces(response) {                        
                   console.log("ok");
                   $http.get('/getAllUsers').success(function(data) {
                        console.log("get users");
                        $scope.allUsers = data;
                    });
                   
                }, function myError(response) {
                    console.log("error");
                });
                
            }; 
    
            
    $scope.del = function(){
                
                $scope.boxArray = [];
                angular.forEach($scope.allUsers, function(user){
                if (!!user.selected) $scope.boxArray.push(user.id);
                });
                console.log($scope.boxArray);
                $http({
                method : 'POST',
                url : '/del',
                headers: {'Content-Type': 'application/json','Accept': 'application/json'},
                data: $scope.boxArray
                }).then(function mySucces(response) {                        
                   console.log("ok");
                   $http.get('/getAllUsers').success(function(data) {
                        console.log("get users");
                        $scope.allUsers = data;
                    });
                   
                }, function myError(response) {
                    console.log("error");
                });
                
            };           
    $scope.sendAdminMail = function()
    {
        $scope.boxArray = [];
                angular.forEach($scope.allUsers, function(user){
                if (!!user.selected) $scope.boxArray.push(user.id);
                });
                
                $http({
                method : 'POST',
                url : '/sendAdminMail',
                headers: {'Content-Type': 'application/json','Accept': 'application/json'},
                data: {ids:$scope.boxArray,temat:$scope.mailTemat,tresc:$scope.mailTresc}
                }).then(function mySucces(response) {                        
                   console.log("ok");
                   $http.get('/getAllUsers').success(function(data) {
                        console.log("get users");
                        $scope.allUsers = data;
                    });
                   $scope.mailTemat = '';
                   $scope.mailTresc = '';
                   alert("wiadomosc wyslano");
                }, function myError(response) {
                    console.log("error");
                    $scope.mailTemat = '';
                    $scope.mailTresc = '';
                    alert("wiadomosc wyslano");
                });
    };
    
    $scope.sendAdminMailToAll = function()
    {        
                
                $http({
                method : 'POST',
                url : '/sendAdminMailToAll',
                headers: {'Content-Type': 'application/json','Accept': 'application/json'},
                data: {ids:$scope.boxArray,temat:$scope.mailTemat,tresc:$scope.mailTresc}
                }).then(function mySucces(response) {                        
                   console.log("ok");
                   $http.get('/getAllUsers').success(function(data) {
                        console.log("get users");
                        $scope.allUsers = data;
                    });
                   $scope.mailTemat = '';
                   $scope.mailTresc = '';
                   alert("wiadomosc wyslano");
                }, function myError(response) {
                    console.log("error");
                    $scope.mailTemat = '';
                    $scope.mailTresc = '';
                    alert("wiadomosc wyslano");
                });
    };
    
    $scope.selectAll = function()
    {
        $scope.boxArray = [];
                angular.forEach($scope.allUsers, function(user){
                console.log(user.selected);
                user.selected='true';
                });
    };
    
    $scope.changePassword = function()
    {
        $scope.boxArray = [];
                angular.forEach($scope.allUsers, function(user){
                if (!!user.selected) $scope.boxArray.push(user.id);
                });
                
        $http({
                method : 'POST',
                url : '/changePassword',
                headers: {'Content-Type': 'application/json','Accept': 'application/json'},
                data: {ids:$scope.boxArray,newPassword:$scope.newPassword}
                }).then(function mySucces(response) {                        
                   console.log("ok");
                   $http.get('/getAllUsers').success(function(data) {
                        console.log("get users");
                        $scope.allUsers = data;
                    });
                   $scope.newPassword = '';
                   alert("haslo zmieniono");
                }, function myError(response) {
                    console.log("error");
                    $scope.newPassword = '';
                    alert("blad");
                });        
    };
    
    $scope.getOne = function(user){
        console.log(user);
        $scope.tmp1=user.id;
    };
    
    $scope.zaz = function(){
        $scope.boxArray = [];
                angular.forEach($scope.allUsers, function(user){
                if (!!user.selected) $scope.boxArray.push(user.login);
                });
        console.log($scope.boxArray);        
    };
});


app1.directive('ngActiveTab', ['$location', function($location) {
	    return {
		link: function postLink(scope, element, attrs) {
		    scope.$on("$routeChangeSuccess", function(event, current, previous) {
			// this var grabs the tab-level off the attribute, or defaults to 1
			var pathLevel = attrs.activeTab || 1,
				// this var finds what the path is at the level specified
				pathToCheck = $location.path().split('/')[pathLevel],
				// this var finds grabs the same level of the href attribute
				tabLink = attrs.href.split('/')[pathLevel];

			if (pathToCheck === tabLink) {
			    element.parent().addClass("active");
			} else {
			    element.parent().removeClass("active");
			}
		    });
		}
	    };
	}]);