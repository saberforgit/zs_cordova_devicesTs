  var initBt = function(){
            		         cordova.plugins.sdsesm3.initBlueTooth(function (res) {
                                                                             }, function (error) {
                                                                             })
            };

            var readCard = function(){
                  cordova.plugins.sdsesm3.readIDCard(function (res) {
                                                                             }, function (error) {
                                                                             })
            };
             var closeDevice = function(){
                  cordova.plugins.sdsesm3.closeDevice(function (res) {
                                                                             }, function (error) {
                                                                             })
            };
