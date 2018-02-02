(function () {

    'use strict';

    /* App Module */

    var app = angular.module("app", ['controllers', 'directives', 'ui.router', 'ngMap']).run(
        ['$rootScope', '$state', function ($rootScope, $state) {

            $rootScope.check = "rootScope is checkd !";

        }]);

    app.config(function ($stateProvider, $urlRouterProvider) {

        $urlRouterProvider.otherwise("/home");

        var header = {
            templateUrl: "views/header.html",
            controller: "headerCntrl"
        };

        var footer = {
            templateUrl: "views/footer.html",
            controller: "footerCntrl"
        };

        addRout('home', '/home', 'homebody');

        addRout('about', '/about', 'about');

        addRout('activities', '/activities', 'activities');

        addRout('contact', '/contact', 'contact');

        addRout('formation-and-administraion', '/services/formation-and-administraion', 'formation-and-administraion',
            'services');

        addRout('trust-formation', '/services/trust-formation', 'trust-formation', 'services');

        addRout('consolting-and-legal', '/services/consolting-and-legal', 'consolting-and-legal', 'services');

        addRout('director-shareholders-services', '/services/director-shareholders-services',
            'director-shareholders-services', 'services');

        addRout('financial-and-regulation', '/services/financial-and-regulation', 'financial-and-regulation',
            'services');

        addRout('bank-account-operations', '/services/bank-account-operations', 'bank-account-operations', 'services');

        function addRout(state, url, viewTemplate, cntrlTemplate) {
            cntrlTemplate = cntrlTemplate == undefined ? viewTemplate : cntrlTemplate;
            $stateProvider.state(state, {
                url: url,
                views: {
                    "header": header,
                    "footer": footer,
                    "": {
                        templateUrl: "views/" + viewTemplate + ".html",
                        controller: cntrlTemplate + "Cntrl"
                    }
                }
            });
        };
    });

})();