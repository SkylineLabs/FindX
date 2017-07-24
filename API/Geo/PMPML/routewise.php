<?php


$jsonData = array();
$jsonData2 = array();
$jsonData3 = array();
$count = 1;


  require_once __DIR__ . '/db_connect.php';

    // connecting to db
    $db = new DB_CONNECT();


$route= $_GET["route"];

$start = mysql_query("SELECT * 
FROM  `$route` 
ORDER BY  `$route`.`id` ASC 
LIMIT 0,1");
$end = mysql_query("SELECT * 
FROM  `$route` 
ORDER BY  `$route`.`id` DESC
LIMIT 0,1");

$start1 = mysql_fetch_array($start);
$end1= mysql_fetch_array($end);


$result = mysql_query("SELECT * FROM PMPML_DEVICE WHERE route_no= '$route'");

while ($row= mysql_fetch_array($result )) {

if($row["direction"] == "up"){

$jsonData2["latitude"] = $row["latitude"];
$jsonData2["longitude"] = $row["longitude"];

$jsonData2["time"] = $row["time"];
$jsonData2["start"] = $start1["UP"];
$jsonData2["end"] = $end1["UP"];

$jsonData[] = $jsonData2;
}

else{
$jsonData2["latitude"] = $row["latitude"];
$jsonData2["longitude"] = $row["longitude"];

$jsonData2["time"] = $row["time"];
$jsonData2["start"] = $end1["UP"];
$jsonData2["end"] = $start1["UP"];

$jsonData[] = $jsonData2;

}

}
$jsonData3["result"] = $jsonData;
echo json_encode($jsonData3);

?>