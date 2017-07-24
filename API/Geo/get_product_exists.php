<?php



// array for JSON response
$response = array();


// include db connect class
require_once __DIR__ . '/db_connect.php';

// connecting to db
$db = new DB_CONNECT();

// check for post data
if (isset($_GET["user_name"])) {
    $user_name = $_GET['user_name'];

    // get a product from products table
    $result = mysql_query("SELECT *FROM Geo WHERE user_name = '$user_name'");

    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {

            $result = mysql_fetch_array($result);

            $product = array();
            //$product["device_id"] = $result["device_id"];
            //$product["latitude"] = $result["latitude"];
            //$product["longitude"] = $result["longitude"];
            
            
            $response["success"] = 1;

            // user node
            $response["gps_coordinates"] = array();

            array_push($response["gps_coordinates"], $product);

            // echoing JSON response
            echo json_encode($response);
        } else {
            // no product found
            $response["success"] = 0;
            $response["message"] = "No product found";

            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no product found
        $response["success"] = 0;
        $response["message"] = "No product found";

        // echo no users JSON
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}
?>