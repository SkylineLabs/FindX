<?php
 

// array for JSON response
$response = array();
 
// include db connect class
require_once __DIR__ . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
    $password = $_GET["password"];
    $user_name = $_GET["user_name"];
    $self = $_GET["self"];
 
    $result = mysql_query("SELECT * FROM Geo WHERE user_name = '$user_name'");
 
    if (!empty($result)) {

 
            $result = mysql_fetch_array($result);
 
            $product = array();
            $product["latitude"] = $result["latitude"];
            $product["longitude"] = $result["longitude"];
            $product["time"] = $result["time"];
            $product["sharing_on"] = $result["sharing_on"];
            $product["contact"] = $result["contact"];
            
            $product["self"] = $self;

            if($result["sharing_on"] == "1")
            {// success
            $response["success"] = 1;
 
            // user node
            $response["gps_coordinates"] = array();
 
            array_push($response["gps_coordinates"], $product);
 
            // echoing JSON response
            echo json_encode($response);
            }

	else{

if($self== $result["fav1"])
	      {
$response["success"] = 1;
 
            // user node
            $response["gps_coordinates"] = array();
 
            array_push($response["gps_coordinates"], $product);
 
            // echoing JSON response
            echo json_encode($response);

	}

elseif($self== $result["fav2"])
	 {
$response["success"] = 1;
 
            // user node
            $response["gps_coordinates"] = array();
 
            array_push($response["gps_coordinates"], $product);
 
            // echoing JSON response
            echo json_encode($response);

	}
elseif($self== $result["fav3"])
	 {
$response["success"] = 1;
 
            // user node
            $response["gps_coordinates"] = array();
 
            array_push($response["gps_coordinates"], $product);
 
            // echoing JSON response
            echo json_encode($response);

	}
elseif($self== $result["fav4"])
	 {
$response["success"] = 1;
 
            // user node
            $response["gps_coordinates"] = array();
 
            array_push($response["gps_coordinates"], $product);
 
            // echoing JSON response
            echo json_encode($response);

	}

 else {
        // no product found
        $response["success"] = 0;
        $response["message"] = "No product found";
 
        // echo no users JSON
        echo json_encode($response);
    }



}


           
           


    } else {
        // no product found
        $response["success"] = 0;
        $response["message"] = "No product found";
 
        // echo no users JSON
        echo json_encode($response);
    }

?>