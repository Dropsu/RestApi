# Turrest

## 1. What is Turrest

Turrest is a tool for making sightseeing easier. It let its users create routes in cities they want to visit.
They can store the routes for themselfes, but the most interesting part is that they can use routes of other people.
So, if you accidentaly happen to have a few hours in the foreign city (in the middle of the longer trip for example)
you can just use a sighteeing route prepared by other user and quicly see what the city has to offer!

## 2. Technologies and structure

Client side lets user create routes. Route is a set of points on the map with additional data like time of travel and distances.
Site uses Google Maps API along with Jquery. Data is exchanged with server via JSON. Site is made prettier with Bootstrap.  

On the server side there is REST API served using Jersey framework. 
Server is responsible mainly for saving, retreiving and searching for routes.

Database used is Oracle Database 11g.
