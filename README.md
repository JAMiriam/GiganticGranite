# GiganticGranite - server
#### serves POST with image
Returns JSON with actors on image
```
http://127.0.0.1:5000/actors/image
```
#### Returns details about actor with chosen ID
```
http://127.0.0.1:5000/actordetails/<imdb_id>
```
#### serves POST with username and password
Returns status of registration - 'ok' or 'username is unavailable'
```
 http://127.0.0.1:5000/register
```
#### serves POST with username and password
Returns userId if authorization succeeded or error description if not
```
http://127.0.0.1:5000/login
```
