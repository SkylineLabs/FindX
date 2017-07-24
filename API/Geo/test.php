<html>






  <head>
    <title>Geo</title>
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
  
  <?php


$latarray=array();
$lonarray=array();
$timearray=array();

/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */

// array for JSON response
$response = array();

if (isset($_GET['user_name'])) {

 
    
    $user_name = $_GET['user_name'];
     
    // include db connect class
    require_once __DIR__ . '/db_connect.php';

    // connecting to db
    $db = new DB_CONNECT();






	$result = mysql_query("SELECT * 
FROM  `sad` 
ORDER BY  `sad`.`time` DESC 
LIMIT 0 , 30");  
 
// $req = mysql_fetch_array($result);
  	
	$x=0;
	
while($rows=mysql_fetch_assoc($result)){
      
	  $latarray[$x]=$rows['latitude'];
	  $lonarray[$x]=$rows['longitude'];
	  $timearray[$x]=$rows['time'];
	  
	  $x=$x+1;
}	



	json_encode($latarray);

	json_encode($lonarray);	
}
else{
 echo "fail" ;
}
?>
  
  <?php 
	
  ?>
  
  var myLatlng = new google.maps.LatLng(<?php 
  $ans = json_decode($latarray);
  $present_latitude=$ans[1];
  echo $present_latitude; ?>,<?php 
  $ansq = json_decode($lonarray);
  $present_longitude=$ansq[1];
  echo $present_longitude; ?>);
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