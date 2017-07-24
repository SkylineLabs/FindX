<?php


$jsonData = array();
$jsonData2 = array();
$jsonData3 = array();
$count = 1;


  require_once __DIR__ . '/db_connect.php';

    // connecting to db
    $db = new DB_CONNECT();


$category= $_GET["category"];

$result = mysql_query("SELECT *FROM Geo WHERE category= '$category'");

while ($row= mysql_fetch_array($result )) {

if($row["onDuty"] == "1"){

$jsonData2["latitude"] = $row["latitude"];
$jsonData2["longitude"] = $row["longitude"];
$jsonData2["user_name"] = $row["user_name"];
$jsonData2["time"] = $row["time"];
$jsonData2["contact"] = $row["contact"];

$jsonData[] = $jsonData2;
}
else
{
continue;
}
}
$jsonData3["result"] = $jsonData;
echo json_encode($jsonData3);

?>