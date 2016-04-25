        
     // *** TWORZENIE MAPY I KOD OBSLUGUJACY TWORZENIE TRASY ***  
        
      function initMap() {
        
        
        var miejsca = [];
        var miejsca_posr = [];
        var i = -1;
        var travel_mode = google.maps.TravelMode.WALKING;
        
        var map = new google.maps.Map(document.getElementById('map'), {
          mapTypeControl: false,
          center: {lat: 51.107, lng: 17.038},
          zoom: 15
        });
        var directionsService = new google.maps.DirectionsService;  //wymagane obiekty do DIRECTIONS API
        var directionsDisplay = new google.maps.DirectionsRenderer;
        directionsDisplay.setMap(map);
        
        map.data.loadGeoJson('https://storage.googleapis.com/maps-devrel/google.json');

        var origin_input = document.getElementById('origin-input');
        var modes = document.getElementById('mode-selector');

        map.controls[google.maps.ControlPosition.TOP_LEFT].push(origin_input);
        map.controls[google.maps.ControlPosition.TOP_LEFT].push(modes);

        var origin_autocomplete = new google.maps.places.Autocomplete(origin_input);
        origin_autocomplete.bindTo('bounds', map);

        
        function setupClickListener(id, mode) {
          var radioButton = document.getElementById(id);
          radioButton.addEventListener('click', function() {
            travel_mode = mode;
          });
        }
        setupClickListener('changemode-walking', google.maps.TravelMode.WALKING);
        setupClickListener('changemode-driving', google.maps.TravelMode.DRIVING);



        origin_autocomplete.addListener('place_changed', function() {
          document.getElementById('origin-input').value = "";
          miejsca.push(origin_autocomplete.getPlace().formatted_address);
          i++;
          miejsca_posr = [];
          for(var j=1;j<i;j++)
          {
              miejsca_posr.push({
        location: miejsca[j],
        stopover: true
      });
          }
          route(miejsca[0], miejsca[i], travel_mode,
                directionsService, directionsDisplay, miejsca_posr);
                map.setZoom(17);
        });

        function route(origin_place_id, destination_place_id, travel_mode,
                       directionsService, directionsDisplay, miejsca_posr) {
          if (!origin_place_id) { //pozwala sie zaladowac stronie
            return;
          }
          directionsService.route({
            origin:origin_place_id,
            destination: destination_place_id,
            waypoints: miejsca_posr,
            travelMode: travel_mode,
            optimizeWaypoints: false
          }, function(response, status) {
            if (status === google.maps.DirectionsStatus.OK) {
              directionsDisplay.setDirections(response);
            } else {
              window.alert('Directions request failed due to ' + status);
            }
          });
        }
        
        // *** WYSYLANIE REQUESTOW ***
        
                
          var saveButton = document.getElementById("saveButton");
       saveButton.addEventListener('click',function () {
         $.ajax({
    type: 'POST',
    url: 'http://localhost:8080/Turrest/api/receive_route',
    data:  JSON.stringify(miejsca),
    success: function(data) { alert('data: ' + data); }, // przy sukcesie wyswietl odp servera
    contentType: "application/json", // typ wysylanych danych
    dataType: 'text' // typ odpowiedzi jakiej oczekuje
});
    });
        
        
       
    // *** DODAWANIE MIEJSC Z INFOWINDOWSOW ***
 
      }