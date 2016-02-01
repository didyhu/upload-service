//admin.js

'use strict';

angular.module('myApp.admin', ['ngRoute'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('', {
            templateUrl: 'admin.html',
            controller: 'AdminCtrl'
        }).when('/foo', {
            templateUrl: 'admin.html',
            controller: 'AdminCtrl'
        })
    }])

    .directive("fileChange", function () {
        return {
            scope: {
                fileChange: "&"
            },
            link: function ($scope, $element) {
                $element.on("change", function () {
                    var files = this.files;
                    $scope.$apply(function () {
                        $scope.fileChange({file: files[0], files: files});
                    });
                })
            }
        }
    })
    .directive("pagination", function () {
        return {
            template: "\
        <li>\
            <a ng-click='goto(pagination.number-1)'>\
                <span aria-hidden='true'>&laquo;</span>\
            </a>\
        </li>\
        <li ng-repeat='n in [].constructor(pagination.totalPages) track by $index' ng-if='($index>pagination.number-5 && $index<pagination.number+5)'\
         ng-class='{active:$index==pagination.number}'>\
        <a title='{{$index}}' ng-click='goto($index)'>{{($index>pagination.number-4 && $index < pagination.number+4)?($index+1):'..'}}</a></li>\
        <li>\
            <a ng-click='goto(pagination.number+1)'>\
                <span aria-hidden='true'>&raquo;</span>\
            </a>\
        </li>\
        ",
            scope: {
                pagination: "=",
                changing: "&"
            },
            link: function ($scope, $element) {
                $scope.goto = function (p) {
                    if (p >= 0 && p < $scope.pagination.totalPages) {
                        $scope.changing({page: p});
                    }
                }
            }
        }
    })

    .controller('AdminCtrl', function ($scope, $http, $routeParams, $route) {

        $scope.init = function () {
            console.log("init");
            var page = $routeParams.page || 0;
            $http.get("getUploads?page=" + page).then(function (res) {
                $scope.uploads = res.data;
            });
        }

        $scope.fileChange = function (file) {
            var form = new FormData();
            form.append("file", file)
            form.append("domain", "test");
            $.ajax({
                url: "/upload",
                type: 'POST',
                data: form,
                processData: false,
                contentType: false,
                success: function (data) {
                    $scope.init();
                }
            });
        }

        $scope.changePage = function (page) {
            $scope.page = page;
            $route.updateParams({page: page});
        }
    });