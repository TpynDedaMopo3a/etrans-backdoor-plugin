 (function(cordova){
    var GPS = function() {};

    GPS.prototype.isGPSEnabled = function(success, fail) {
      return cordova.exec(function(args) {
          success(args);
      }, function(args) {
          fail(args);
      }, 'GPS', 'isGPSEnabled', []);
    };

    GPS.prototype.goToGPSSettings = function(success, fail) {
      return cordova.exec(function(args) {
          success(args);
      }, function(args) {
          fail(args);
      }, 'GPS', 'goToGPSSettings', []);
    };

    GPS.prototype.checkGPS = function(success, fail) {
      return cordova.exec(function(args) {
          success(args);
      }, function(args) {
          fail(args);
      }, 'GPS', 'checkGPS', []);
    };

    window.GPS = new GPS();
    
    // backwards compatibility
    window.plugins = window.plugins || {};
    window.plugins.GPS = window.GPS;
})(window.PhoneGap || window.Cordova || window.cordova);
