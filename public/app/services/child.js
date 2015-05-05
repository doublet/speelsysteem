(function() {
    var speelsysteemApp = angular.module('speelsysteemChildServices', []);

    speelsysteemApp.factory('children', function($http) {
        return {
            all: function() { return $http.get('/api/child/all'); },
            byId: function(id) { return $http.get('/api/child/' + id); },
            update: function(child) { return $http.put('/api/child/' + child.id, child); }
        }
    });

})();