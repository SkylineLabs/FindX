<?php


$jsonData = array();
$jsonData2 = array();
$jsonData3 = array();
$count = 1;


  require_once __DIR__ . '/db_connect.php';

    // connecting to db
    $db = new DB_CONNECT();







////////////////////////////////////////////////////////////////////////
$startloc = $_GET["startloc"];
$endloc = $_GET["endloc"];

$sqlquery = mysql_query("SELECT $startloc.ROUTE FROM $startloc");
$sqlquery2 = mysql_query("SELECT $endloc.ROUTE FROM $endloc");
$sqlquery3 = mysql_query("SELECT $startloc.ROUTE from $startloc INNER JOIN $endloc ON $startloc.ROUTE=$endloc.ROUTE");


////////////////////////////////////////////////////////////////////////
while($sqlrow = mysql_fetch_array($sqlquery3)){

	$route = $sqlrow["ROUTE"];
	
	//$startid1 = mysql_query("select * from $route where UP='$startloc'");
	//$endid1 = mysql_query("select * from $route where UP='$endloc'");
	//$startid = mysql_fetch_array($startid1);
	//$endid = mysql_fetch_array($endid1);
	//$sub = $startid["id"] - $endid["id"];
	
	//printf("route:%s..start:%s..end:%s..sub:%u..",$route,$startid["id"],$endid["id"],$sub);	

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
}
$jsonData3["result"] = $jsonData;
echo json_encode($jsonData3);

?>