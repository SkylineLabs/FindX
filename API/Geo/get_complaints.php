<?php


$jsonData = array();
$jsonData2 = array();
$jsonData3 = array();


  require_once __DIR__ . '/db_connect.php';

    // connecting to db
    $db = new DB_CONNECT();


$choice= $_GET["category"];

$result = mysql_query("SELECT * FROM Complaint_Portal");

while ($row= mysql_fetch_array($result )) {

$jsonData2["latitude"] = $row["Latitude"];
$jsonData2["longitude"] = $row["Longitude"];
$jsonData2["user_name"] = $row["user_name"];
$jsonData2["title"] = $row["Title"];
$jsonData2["Category"] = $row["Category"];
$jsonData2["Description"] = $row["Description"];
$jsonData2["Image_URL"] = $row["Image_URL"];
$jsonData[] = $jsonData2;
}
$jsonData3["result"] = $jsonData;
echo json_encode($jsonData3);

?>