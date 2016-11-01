cordova.define("cordova-plugin-sdsesm3", function(require, exports, module) {

var exec = require('cordova/exec');

exports.initBluetooth = function( success, error) {
    exec(success, error, "SDSESM3_CR", "initBluetooth", []);
};
exports.readIDCard = function( success, error) {
    exec(success, error, "SDSESM3_CR", "readIDCard", []);
};
exports.initMagClient = function( success, error) {
    exec(success, error, "SDSESM3_CR", "initMagClient", []);
};
exports.readICCardNum = function( success, error) {
    exec(success, error, "SDSESM3_CR", "readICCardNum", []);
};
exports.readMagCardNo = function( success, error) {
    exec(success, error, "SDSESM3_CR", "readMagCardNo", []);
};


});
