        
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
        var save_button = document.getElementById('saveButton');
        var route_name = document.getElementById('route-name');
        
        var origin_autocomplete = new google.maps.places.Autocomplete(origin_input);
        origin_autocomplete.bindTo('bounds', map);


        var city_autocomplete = new google.maps.places.Autocomplete(city_input);
        city_autocomplete.bindTo('bounds', map);
        var regions = ["(regions)"];
        city_autocomplete.setTypes(regions); //googlemaps dopusci tylko miasta do wyboru w tym inpucie


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

        var distance = 0;
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
                    {
                       var j =0;
                        if(i>1)  {j= i-1;}
                       directionsDisplay.setDirections(response);
                       distance += response.routes[0]['legs'][j]['distance']['value'];
                    }
                        
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
                route_length_km: String((distance/1000).toFixed(2)),
                estimated_walk_time_in_mins:(distance*0.015)+(miejsca.length*10),
                number_of_places:i+1,
                places:miejsca };
            $.ajax({//1638
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
        
       var searchButton = document.getElementById("search");
       searchButton.addEventListener('click',function (){
           $.ajax({ 
                type: 'GET', 
                url: 'http://localhost:8080/Turrest/api/search_route_by_city/'+city_name, 
                data: { get_param: 'value' }, 
                dataType: 'json',
                success: function (data) { 
                    var routesTable = document.getElementById("routes-table");
                    var row;
                    var cell1;
                    var cell2;
                    var cell3;
                    var cell4;
                    var cell5;
                    
                    
                    for(var i=0;i<data.length;i++)
                    {
                        row = routesTable.insertRow(i+1);
                        cell1 = row.insertCell(0);
                        cell2 = row.insertCell(1);
                        cell3 = row.insertCell(2);
                        cell4 = row.insertCell(3);
                        cell5 = row.insertCell(4);
                        cell1.innerHTML = data[i].route_id;
                        cell2.innerHTML = data[i].number_of_places;
                        cell3.innerHTML = data[i].route_length_km+" km";
                        cell4.innerHTML = Math.floor((data[i].estimated_walk_time_in_mins/60).toFixed(2))+" godzin "+
                                data[i].estimated_walk_time_in_mins%60+" minut";
                        
                        var button = document.createElement("BUTTON");        
                        var t = document.createTextNode("Wyświetl Trasę"); 
                        button.onclick = function(){wyswietlOtrzymanaTrase(data[this.id]);};
                        button.setAttribute("id", i);
                        button.appendChild(t);                                                
                        cell5.appendChild(button);


                    }
                    
                    //wyswietlOtrzymanaTrase(data[0]);
                }
            });
        });   
    }