cordova.define("cordova-plugin-watermark", function(require, exports, module) {

var exec = require('cordova/exec');

exports.createWaterMark = function( success, error,option) {
    exec(success, error, "WaterMark", "createWaterMark", [option]);
};
});
