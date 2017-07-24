<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>History || FindX</title>
    <script src="http://maps.google.com/maps/api/js?key=AIzaSyCRDMbxW-eSFC37FZ6efhiQTqxpyjw__RA" type="text/javascript"></script>
   


 <script type="text/javascript">
    //<![CDATA[

 
 
    var customIcons = {
      restaurant: {
        icon: 'http://labs.google.com/ridefinder/images/mm_20_blue.png',
        shadow: 'http://labs.google.com/ridefinder/images/mm_20_shadow.png'
      },
      bar: {
        icon: 'http://labs.google.com/ridefinder/images/mm_20_red.png',
        shadow: 'http://labs.google.com/ridefinder/images/mm_20_shadow.png'
      }
    };

    function load() {

var map = new google.maps.Map(document.getElementById("map"), {
        center: new google.maps.LatLng(10,10),
        zoom: 10,
        mapTypeId: 'roadmap'
    
      });
      var infoWindow = new google.maps.InfoWindow;

      // Change this depending on the name of your PHP file
<?php
	if(isset($_GET['user_name'])&&isset($_GET['num'])){
		$user_name=$_GET['user_name'];
		$num=$_GET['num'];	
	}
?>

      downloadUrl("phpsqlajax_genxml2.php?user_name=<?php echo $user_name; ?>&num=<?php echo $num; ?>", function(data) {

  

        var xml = data.responseXML;
        var markers = xml.documentElement.getElementsByTagName("marker");
        for (var i = 0; i < markers.length; i++) {
          var time = markers[i].getAttribute("time");
          var status = markers[i].getAttribute("status");

	if(status != "" && status != null){

          var point = new google.maps.LatLng(
              parseFloat(markers[i].getAttribute("lat")),
              parseFloat(markers[i].getAttribute("lng")));
             
          map.setCenter(new google.maps.LatLng(markers[i].getAttribute("lat"),markers[i].getAttribute("lng")));

          var html = "<b>" + time+ "</b></br> "+status ;
          //var icon = customIcons[bar] || {};
          var marker = new google.maps.Marker({
            map: map,
            position: point,
           
          });
          bindInfoWindow(marker, map, infoWindow, html);}
        }
      });
    }

    function bindInfoWindow(marker, map, infoWindow, html) {
      google.maps.event.addListener(marker, 'click', function() {
        infoWindow.setContent(html);
        infoWindow.open(map, marker);
      });
    }

    function downloadUrl(url, callback) {
      var request = window.ActiveXObject ?
          new ActiveXObject('Microsoft.XMLHTTP') :
          new XMLHttpRequest;

      request.onreadystatechange = function() {
        if (request.readyState == 4) {
          request.onreadystatechange = doNothing;
          callback(request, request.status);
        }
      };

      request.open('GET', url, true);
      request.send(null);
    }

    function doNothing() {}

    //]]>
  </script>
  </head>

  <body onload="load()">
    <div id="map" style="width: 100%; height: 100%"></div>
  </body>
</html>