 (function(cordova){
    var BackDoor = function() {};

    BackDoor.prototype.setAppVersion = function(appVersion) {
      console.log("BackDoor.js - setAppVersion|  appVersion: " + appVersion + ";");
      return cordova.exec(function(args) {
//          success(args);
      }, function(args) {
//          fail(args);
      }, 'BackDoor', 'setAppVersion', [appVersion]);
    };

    BackDoor.prototype.keepScreenOn = function(success, fail) {
      return cordova.exec(function(args) {
          success(args);
      }, function(args) {
          fail(args);
      }, 'BackDoor', 'keepScreenOn', []);
    };

    BackDoor.prototype.setCallback = function(success, fail) {
      return cordova.exec(function(args) {
          success(args);
      }, function(args) {
          fail(args);
      }, 'BackDoor', 'setCallback', []);
    };

    BackDoor.prototype.updateApp = function(serverAddress, appName, success, fail) {
      console.log("BackDoor.js - updateApp|  serverAddress: " + serverAddress + "; appName: " + appName + ";");
      return cordova.exec(function(args) {
          console.log("BackDoor.js - updateApp SUCCESS|  serverAddress: " + serverAddress + "; appName: " + appName + ";");
          success(args);
      }, function(args) {
          console.log("BackDoor.js - updateApp FAIL|  serverAddress: " + serverAddress + "; appName: " + appName + ";");
          fail(args);
      }, 'BackDoor', 'updateApp', [serverAddress, appName]);
    };


    window.BackDoor = new BackDoor();

    // backwards compatibility
    window.plugins = window.plugins || {};
    window.plugins.BackDoor = window.BackDoor;
})(window.PhoneGap || window.Cordova || window.cordova);
