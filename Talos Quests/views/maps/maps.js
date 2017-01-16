// Init View
var Observable = require("data/observable").Observable;
var GeoLocation = require("nativescript-geolocation");
var mapsModule = require("nativescript-google-maps-sdk");
var game = require("./gameQueries/gameQueries");
var quest = require("./questQueries/questQueries");
var Color = require("color").Color;
var disabledLocation = false;
var CurrentPage;
var mapView;

var userLocation = new Observable({
    userLatitude: 0,
    userLongitude: 0,
    userLocated: false
});

var mapLocation = new Observable({
  mapLatitude: 0,
  mapLongitude: 0,
  mapZoom: 1
});
// Unit Functions
function onNavigatingTo(args) {
    CurrentPage = args.object;
    CurrentPage.bindingContext = userLocation;
    CurrentPage.bindingContext = mapLocation;
    enableLocation();
    //game.createGame();
    game.getGame();
   game.activeGame();
    //game.getGame();
    quest.getQuest();
}
exports.onNavigatingTo = onNavigatingTo;

function enableLocation() {
    if (!GeoLocation.isEnabled()) {
        alert("Can't use GPS info.\nYou may see void map !!!");
        disabledLocation = true;
    }
}
exports.enableLocation = enableLocation;

function mapIsReady(args) {
    mapView = args.object;
    if (!disabledLocation) {
        trackLocation();
    }
}
exports.mapIsReady = mapIsReady;
function trackLocation() {
    watchId = GeoLocation.watchLocation(
        function(loc) {
            if (loc) {
                //console.log("lat " + loc.latitude + " lng " + loc.longitude);
                userLocation.userLatitude = loc.latitude;
                userLocation.userLongitude = loc.longitude;
                userLocation.userLocated = true;
                findMe();
            }
        },
        function(e) {
            alert("Can't use GPS info.\nYou may see void map !!!");
        }, {
            desiredAccuracy: 3,
            updateDistance: 10,
            minimumUpdateTime: 1000 * 2
        }
    );
}
exports.trackLocation = trackLocation;

function updateUserMarker() {
  var UserMarker = new mapsModule.Marker();
  mapView.findMarker(function(marker) {
      if (marker.userData.index == 1) {
          mapView.removeMarker(marker);
      }
  });
  UserMarker.position = mapsModule.Position.positionFromLatLng(userLocation.userLatitude, userLocation.userLongitude);
  UserMarker.title = "My Location";
  UserMarker.userData = {
      index: 1
  };
  mapView.addMarker(UserMarker);

  var UserCircle = new mapsModule.Circle();
  mapView.removeAllCircles();
  UserCircle.center = mapsModule.Position.positionFromLatLng(userLocation.userLatitude, userLocation.userLongitude);
  UserCircle.visible = true;
  UserCircle.radius = 50;
  UserCircle.fillColor = new Color('#99ff8800');
  UserCircle.strokeColor = new Color('#99ff0000');
  UserCircle.strokeWidth = 2;
  mapView.addCircle(UserCircle);
}
exports.updateUserMarker = updateUserMarker;

function findMe(args) {
    if (userLocation.userLocated) {
        mapLocation.mapLatitude = userLocation.userLatitude;
        mapLocation.mapLongitude = userLocation.userLongitude;
        mapLocation.mapZoom = 17;
        updateUserMarker();
    }
}
exports.findMe = findMe;

function changeTab(args) {
    if (userLocation.userLocated) {
        updateUserMarker();
    }
}
exports.changeTab = changeTab;