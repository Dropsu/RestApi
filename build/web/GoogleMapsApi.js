        
     // *** TWORZENIE MAPY I KOD OBSLUGUJACY TWORZENIE TRASY ***  
        
    function initMap() {
        
        var miejsca = [];
        var miejsca_posr = [];
        var i = -1;
        var travel_mode = google.maps.TravelMode.WALKING;
        var city_name;
        var map = new google.maps.Map(document.getElementById('map'), {
            mapTypeControl: false,
            center: {lat: 51.107, lng: 17.038},
            zoom: 15
        });
        var directionsService = new google.maps.DirectionsService;  //wymagane obiekty do DIRECTIONS API
        var directionsDisplay = new google.maps.DirectionsRenderer;
        directionsDisplay.setMap(map);
        

        var origin_input = document.getElementById('origin-input');
        var city_input = document.getElementById('city-input');
        var modes = document.getElementById('mode-selector');
        var save_button = document.getElementById('saveButton');
        var route_name = document.getElementById('route-name');
        

//        map.controls[google.maps.ControlPosition.TOP_LEFT].push(origin_input);
//        map.controls[google.maps.ControlPosition.TOP_LEFT].push(modes);
//        map.controls[google.maps.ControlPosition.TOP_LEFT].push(route_name);
//        map.controls[google.maps.ControlPosition.TOP_LEFT].push(save_button);
        
        var origin_autocomplete = new google.maps.places.Autocomplete(origin_input);
        origin_autocomplete.bindTo('bounds', map);


        var city_autocomplete = new google.maps.places.Autocomplete(city_input);
        city_autocomplete.bindTo('bounds', map);
        var regions = ["(regions)"];
        city_autocomplete.setTypes(regions); //googlemaps dopusci tylko miasta do wyboru w tym inpucie

        
        function setupClickListener(id, mode) {
            var radioButton = document.getElementById(id);
            radioButton.addEventListener('click', function() {
                travel_mode = mode;
            });
        }
        setupClickListener('changemode-walking', google.maps.TravelMode.WALKING);
        setupClickListener('changemode-driving', google.maps.TravelMode.DRIVING);

        function setMiejscaPosr ()
        {
            miejsca_posr = [];
            for(var j=1;j<i;j++) {
               miejsca_posr.push({
                    location: miejsca[j],
                    stopover: true
               });
            }
        }

        origin_autocomplete.addListener('place_changed', function() {
            document.getElementById('origin-input').value = "";
            miejsca.push(origin_autocomplete.getPlace().formatted_address);
            i++;
            setMiejscaPosr();
            route(miejsca[0], miejsca[i], travel_mode,
                directionsService, directionsDisplay, miejsca_posr);
            map.setZoom(17);
        });
        
        city_autocomplete.addListener('place_changed', function() {
            document.getElementById('city-input').style="background-color: #ff6666";
            city_name = city_autocomplete.getPlace().formatted_address;
            map.setCenter(city_autocomplete.getPlace().geometry.location);
            document.getElementById('city-input').style="background-color: #87c77d";
            map.setZoom(15);
        });

        function route(origin_place_id, destination_place_id, travel_mode,
            directionsService, directionsDisplay, miejsca_posr) {
            if (!origin_place_id) //pozwala sie zaladowac stronie 
                return;

            directionsService.route({
                origin:origin_place_id,
                destination: destination_place_id,
                waypoints: miejsca_posr,
                travelMode: travel_mode,
                optimizeWaypoints: false
            }, function(response, status) {
                    if (status === google.maps.DirectionsStatus.OK)
                        directionsDisplay.setDirections(response);
                    else 
                        window.alert('Directions request failed due to ' + status);             
                });
        }
        
        // *** WYSYLANIE TRAS ***
        
        var add_route_button = document.getElementById('add-route');
        add_route_button.addEventListener('click',function () {
             var place = city_autocomplete.getPlace();
            if (!place) {
                window.alert("Wybierz jedno z sugerowanych miast w polu tekstowym");
                return;
            }
            document.getElementById("map").style.visibility = "visible";
            $(".controls").css("visibility", "visible");
        });
        
       
        var saveButton = document.getElementById("saveButton");
        saveButton.addEventListener('click',function () {
            if(miejsca.length<2)
            {
                window.alert("Trasa musi zawierać przynajmniej 2 punkty");
                return;
            }
             if(route_name.value==="")
            {
                window.alert("Pole nazwa nie może być puste");
                return;
            }
            
            
            var route = { city_name:city_name,
                route_id:route_name.value,
                route_length_km:"5.5",
                estimated_walk_time_in_mins:120,
                number_of_places:i+1,
                places:miejsca };
            $.ajax({
                type: 'POST',
                url: 'http://localhost:8080/Turrest/api/send_route',
                data:  JSON.stringify(route),
                success: function(data) {
                    alert('wynik: ' + data); 
                }, 
                contentType: "application/json", // typ wysylanych danych
                dataType: 'text' // typ odpowiedzi jakiej oczekuje
            });
        });
        // *** ODBIERANIE TRAS ***
        
        
        function wyswietlOtrzymanaTrase (json) {
            for(var j=0;j<json.miejsca.length;j++) {
                miejsca[j]=json.miejsca[j];
                i++;
            }
            setMiejscaPosr();
            route(miejsca[0], miejsca[i], travel_mode,
                directionsService, directionsDisplay, miejsca_posr);
        }
        
        var testButton = document.getElementById("receiving-test");
        
        testButton.addEventListener('click',function (){
           
            $.ajax({ 
                type: 'GET', 
                url: 'http://localhost:8080/Turrest/api/search_route_by_id/Poznan123', 
                data: { get_param: 'value' }, 
                dataType: 'json',
                success: function (data) { 
                    wyswietlOtrzymanaTrase(data);
                }
            });
        });
       
       var testButton2 = document.getElementById("receiving-array-test");
       testButton2.addEventListener('click',function (){
           
           $.ajax({ 
                type: 'GET', 
                url: 'http://localhost:8080/Turrest/api/search_route_by_city/Oleśnica, Polska', 
                data: { get_param: 'value' }, 
                dataType: 'json',
                success: function (data) { 
                    wyswietlOtrzymanaTrase(data[0]);
                }
            });
        });
       
    }