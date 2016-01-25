 (function(cordova){
    var BackDoor = function() {};

    BackDoor.prototype.setAppVersion = function(appVersion) {
      return cordova.exec(function(args) {
//          success(args);
      }, function(args) {
//          fail(args);
      }, 'BackDoor', 'setAppVersion', [appVersion]);
    };

    BackDoor.prototype.setCallback = function(success, fail) {
      return cordova.exec(function(args) {
          success(args);
      }, function(args) {
          fail(args);
      }, 'BackDoor', 'setCallback', []);
    };

    BackDoor.prototype.updateApp = function(serverAddress, appName, success, fail) {
      return cordova.exec(function(args) {
          success(args);
      }, function(args) {
          fail(args);
      }, 'BackDoor', 'setCallback', [serverAddress, appName]);
    };


    window.BackDoor = new BackDoor();

    // backwards compatibility
    window.plugins = window.plugins || {};
    window.plugins.BackDoor = window.BackDoor;
})(window.PhoneGap || window.Cordova || window.cordova);
