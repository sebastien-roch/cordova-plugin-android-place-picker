var exec = require('cordova/exec');
var channel = require('cordova/channel');
var utils = require('cordova/utils');

var AndroidPlacePicker = (function() {
    return {
        pickPlace: function(onSuccess, onError) {
            exec(onSuccess, onError, 'AndroidPlacePicker', 'pickPlace', []);
        }
    };
})();


// Export the module
module.exports = AndroidPlacePicker;
