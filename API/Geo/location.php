<html>



<?php

$user_name = $_POST['user_name'];

$present_latitude;
$present_longitude;

require_once __DIR__ . '/db_connect.php';
$db = new DB_CONNECT();

 // get a product from products table
$result = mysql_query("SELECT *FROM Geo WHERE user_name = '$user_name'");
 
if (!empty($result)) {
 
$result = mysql_fetch_array($result);

if($result["sharing_on"]=="1")
{
$present_latitude = $result["latitude"]; 
$present_longitude = $result["longitude"];
}
else
{

header("Location:http://www.skylinelabs.in/FindX/FindX_fail.html");
$response["success"] = 0;

}

}
else
{

header("Location:http://www.skylinelabs.in/FindX/FindX_fail.html");
$response["success"] = 0;

}

?>


  <head>
    <title>FindX</title>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">



    <style>
      html, body, #map-canvas {
        height: 100%;
        margin: 0px;
        padding: 0px
      }
    </style>
    
  </head>
  <body>

    <div id="map-canvas"></div>

<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCRDMbxW-eSFC37FZ6efhiQTqxpyjw__RA"></script>
    <script>
var map;
function initialize() {
  var myLatlng = new google.maps.LatLng(<?php echo $present_latitude; ?>,<?php echo $present_longitude; ?>);
  var mapOptions = {
    zoom: 12,
    center: new google.maps.LatLng(<?php echo $present_latitude; ?>,<?php echo $present_longitude; ?>)
    
  };


  map = new google.maps.Map(document.getElementById('map-canvas'),
      mapOptions);

 var marker = new google.maps.Marker({
      position: myLatlng,
      map: map,
      title: 'Hello'
  });


}


google.maps.event.addDomListener(window, 'load', initialize);

    </script>
  </body>
</html>