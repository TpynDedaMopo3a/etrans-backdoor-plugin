 (function(cordova){
    var BackDoor = function() {};

    BackDoor.prototype.setCallback = function(success, fail) {
      return cordova.exec(function(args) {
          success(args);
      }, function(args) {
          fail(args);
      }, 'BackDoor', 'setCallback', []);
    };


    window.BackDoor = new BackDoor();
    
    // backwards compatibility
    window.plugins = window.plugins || {};
    window.plugins.BackDoor = window.BackDoor;
})(window.PhoneGap || window.Cordova || window.cordova);
