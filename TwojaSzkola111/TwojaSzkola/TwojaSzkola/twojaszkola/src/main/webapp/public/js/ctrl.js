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

          
var app = angular.module('myApp', []);

app.controller('myCtrl',function($rootScope,$scope, $http,$location,$window) {
            $scope.user = {
              login: '',
              password: ''
            };
            
            
            $scope.login = function(){
                
                console.log("login function");
                $http({
                method : 'POST',
                url : '/test',
                headers: {'Content-Type': 'application/json','Accept': 'application/json'},
                data: {login:$scope.user.login,password:$scope.user.password}
                }).then(function mySucces(response) {
                    console.log(response.data);
                                                   
                    $scope.id=response.data.id; 
                    $window.sessionStorage.setItem('myItem',$scope.id); 
                    
                       
                            switch(response.data.role.toString())
                            {
                                case 'SZKOLA': $window.location.href='/szkola.html';console.log("przechodze przez SZKOLA");break;
                                case 'ADMIN': $window.open('/admin.html','_self');alert("przechodze przez ADMIN");break;
                                case 'UCZEN': $window.location.href='/index.html';console.log("przechodze przez UCZEN");break;
                                default: $window.location.href='/error.html';break;    
                            }; 
                                       
                               
                }, function myError(response) {
                    $scope.myWelcome = response.statusText;
                    console.log("error");
                });
            };
        
            }); 
            
            
app.controller("reg",function($rootScope,$scope, $http,$location,$window){ 
    
    $scope.userReg = 
            {
                email:'',
                login: '',
                password: '',
                passwordConfirm: '',
                role: 'UCZEN'
            };
        
        $scope.signUp = function(){
                alert("sign-up function");
                
                $http({
                method : 'POST',
                url : '/reg',
                headers: {'Content-Type': 'application/json','Accept': 'application/json'},
                data: {login:$scope.userReg.login,email:$scope.userReg.email,password:$scope.userReg.password,role:$scope.userReg.role}
                }).then(function mySucces(response) {
                    console.log(response.data);                    
                    
                   
                }, function myError(response) {
                    
                    console.log("error");
                });
            };
    
        
});              

