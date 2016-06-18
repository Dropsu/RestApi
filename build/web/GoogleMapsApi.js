        
     // Funkcja głównia - Inicjowanie GoogleMapsApi 
        
    function initMap() {
        
        var miejsca = []; // przechowuje miejsca na trasie
        var miejsca_posr = []; // wydzielone miejsca posrednie potrzebne do użytkowania GoogleMapsApi
        var distance = 0; // przechowuje dlugosc trasy w metrach
        var i = -1; // ilość miejsc(przy użytym sposobie inkrementacji zaczyna się od -1)
        var travel_mode = google.maps.TravelMode.WALKING;
        var city_name; // przechowuje nazwe miasta
        var map = new google.maps.Map(document.getElementById('map'), { //inicjowanie mapy
            mapTypeControl: false,
            center: {lat: 51.107, lng: 17.038},
            zoom: 15
        });
        var directionsService = new google.maps.DirectionsService;  //wymagane obiekty do DIRECTIONS API
        var directionsDisplay = new google.maps.DirectionsRenderer;
        directionsDisplay.setMap(map); 
        

        var origin_input = document.getElementById('origin-input'); // zmienne przechowujące wartości imputut fieldów
        var city_input = document.getElementById('city-input');
        var route_name = document.getElementById('route-name');
        
        var origin_autocomplete = new google.maps.places.Autocomplete(origin_input); //aby zapewnieć jednoznaczność używam, uzupełnionych przez google nazw jako kluczowych
        origin_autocomplete.bindTo('bounds', map);


        var city_autocomplete = new google.maps.places.Autocomplete(city_input);
        city_autocomplete.bindTo('bounds', map);
        var regions = ["(regions)"];
        city_autocomplete.setTypes(regions); //Google maps, w tym inpucie zaproponuje tylko miasta
        
        var routeAddBClicked = false; //przechowują czy użytkownik chce dodać czy wyszukać trasę
        var routeSearchBClicked = false;


        // Wydziela z wektora miejsc miejsca pośrednie
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
        
        function addToPointsList (miejsce) // dodaje punkty trasy do lity obok mapy
        {
            var listItem = document.createElement("li");
            var node = document.createTextNode(miejsce);
            listItem.appendChild(node);
            var element = document.getElementById("points-list");
            element.appendChild(listItem);
        }
        // *** DODAWANIE TRASY
        origin_autocomplete.addListener('place_changed', function() { //kiedy użytkownic wybierze miejsce z autocomplete...
            document.getElementById('origin-input').value = "";
            miejsca.push(origin_autocomplete.getPlace().formatted_address);//do wektora miejsca zostaje dodane miejsce
            addToPointsList(origin_autocomplete.getPlace().formatted_address); //funkcja umieszcza je na liscie obok mapy
            i++; 
            setMiejscaPosr();
            route(miejsca[0], miejsca[i], travel_mode, // zapytanie do GoogleMapsDirections, wyznaczy i narysuje trase
                directionsService, directionsDisplay, miejsca_posr);
            map.setZoom(17);
        });
        
        city_autocomplete.addListener('place_changed', function() { // reaguje na wybor miasta przez użytkownika
            city_name = city_autocomplete.getPlace().formatted_address;
            if (!city_autocomplete.getPlace().formatted_address) {
                window.alert("Wybierz jedno z sugerowanych miast w polu tekstowym"); 
                return;
            }
            map.setCenter(city_autocomplete.getPlace().geometry.location);
            map.setZoom(15);
        });

      
        function route(origin_place_id, destination_place_id, travel_mode,
            directionsService, directionsDisplay, miejsca_posr) {
            if (!origin_place_id) //musi być by strona się załadowała
                return;

            directionsService.route({ // zapytanie do GoogleMapsDirections, wyznaczy i narysuje trase
                origin:origin_place_id,
                destination: destination_place_id,
                waypoints: miejsca_posr, // rysuje korzystajac z punktow posrednich, dlatego sa one wydzielane
                travelMode: travel_mode,
                optimizeWaypoints: false
            }, function(response, status) {
                    if (status === google.maps.DirectionsStatus.OK)
                    {
                       var j =0;
                        if(i>1)  {j= i-1;} // aby dystans dodawal sie poprawnie
                       directionsDisplay.setDirections(response);
                       distance += response.routes[0]['legs'][j]['distance']['value'];// dystans uzyskuje z odpowiedzi GoogleMapsDirections
                       document.getElementById("distance").innerHTML = "Długość: "+(distance/1000).toFixed(2)+" km";
                    }
                        
                    else 
                        window.alert('Directions request failed due to ' + status);             
                });
        }
        
        var submit_button = document.getElementById('submit'); // przycisk potwierdzajacy akcje, uzależnioną od wybobru użytkownika czy dodać czy szukać tras
        submit_button.addEventListener('click',function () {
            
            if(routeAddBClicked===true)
            {
                var place = city_autocomplete.getPlace();
            if (!place) {
                window.alert("Wybierz jedno z sugerowanych miast w polu tekstowym");
                return;
            }
            document.getElementById("map").style.visibility = "visible"; // ujawnia okno do dodawania trasy
            $(".controls").css("visibility", "visible"); // ujawnia przyciski do dodawania trasy
            }
            
            if(routeSearchBClicked===true)
            {
                var place = city_autocomplete.getPlace();
            if (!place) {
                window.alert("Wybierz jedno z sugerowanych miast w polu tekstowym");
                return;
            }
                 $.ajax({ // wysyla zapytanie do Api o wektor tras, a nastepnie tworzy i wyswietla je w tabeli
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
                        
                        var button = document.createElement("BUTTON"); //dodaje przycisk do każdego wiersza który wyświetli trase   
                        var t = document.createTextNode("Wyświetl Trasę"); 
                        button.onclick = function(){
                            wyswietlOtrzymanaTrase(data[this.id]);
                            document.getElementById("map").style.visibility = "visible";
                            $("#routes-table").css("display", "none");
                        };
                        button.setAttribute("id", i);
                        button.setAttribute("class", "btn btn-success");
                        button.appendChild(t);                                                
                        cell5.appendChild(button);
                    }
                    $("#routes-table").css("display", "block");
                }
            });
            }
            
        });
        
        // *** WYSYLANIE TRAS ***
        
        var add_route_button = document.getElementById('add-route'); // przycisk wyboru trybu: dodawanie trasy
        add_route_button.addEventListener('click',function () {
           routeAddBClicked=true;  
           routeSearchBClicked=false;
           $("#city-input_div").css("display", "block"); // wyswietla lub ukrywa elementy potrzebne w tym trybie
           $("#add_img").css("display", "initial");
           $("#search_img").css("display", "none");
        });
        
       
        var saveButton = document.getElementById("saveButton"); // przycisk wysylający trase do API
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
                estimated_walk_time_in_mins:(distance*0.015)+(miejsca.length*10), // wzor na czas przejscia
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
        
        
        function wyswietlOtrzymanaTrase (json) { // funkcja wyświetlająca otzymana trase
            for(var j=0;j<json.miejsca.length;j++) { // wypelni wektor miejsc pobranymi miejscami
                miejsca[j]=json.miejsca[j];
                addToPointsList(miejsca[j]);
                i++;
            }
            setMiejscaPosr();
            route(miejsca[0], miejsca[i], travel_mode,// GoogleMapsApi wyrysuje trase
                directionsService, directionsDisplay, miejsca_posr);
        }
        
       var searchButton = document.getElementById("search"); // przycisk wyboru trybu: wyszukiwanie tras
       searchButton.addEventListener('click',function (){
          routeSearchBClicked=true;
          routeAddBClicked=false;
          $("#city-input_div").css("display", "block");
          $("#search_img").css("display", "initial");
          $("#add_img").css("display", "none");
        });   
    }