<?php


     
    // array for JSON response
    $response = array();
     
    $latitude = $_POST['latitude'];
    $longitude = $_POST['longitude'];
    $user_name = $_POST['user_name'];
    $time = $_POST['time'];
    $sharing_on = $_POST['sharing_on'];
    $history = $_POST['history'];
    $onDuty= $_POST['onDuty'];
    $imei= $_POST['IMEI'];
$trueIME = null;

     
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
     
    // connecting to db
    $db = new DB_CONNECT();


   $result = mysql_query("SELECT *FROM Geo WHERE user_name = '$user_name'");

    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {

            $result = mysql_fetch_array($result);           
            $trueIMEI = $result["IMEI"];
}
}

if($imei== $trueIMEI){

       $sql = "INSERT INTO $user_name (latitude,longitude,time)
VALUES ('$latitude','$longitude','$time')";

mysql_query($sql);
     
     // get a product from products table
     $result = mysql_query("UPDATE Geo SET longitude = '$longitude', latitude = '$latitude', time='$time' , history = '$history' , sharing_on = '$sharing_on' WHERE user_name= '$user_name'");
     $result = mysql_fetch_array($result);
    if (($result)) {
     

         
     $response["success"] = 1;
      $response["message"] = "Product successfully created.";
      
      
    
    // echoing JSON response
    echo json_encode($response);
    } 

    else {
    // failed to insert row
    $response["success"] = 0;
    $response["message"] = "Oops! An error occurred.";
     
    // echoing JSON response
    echo json_encode($response);
    }
    
}


    ?>