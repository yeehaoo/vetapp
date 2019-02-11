<?php

if (isset($_POST["visit_id"])) {
    $visit_id = $_POST["visit_id"];
    
    require_once __DIR__ . '/db_connect.php';
    $db = new DB_CONNECT();
    $db->connect();

    $sqlCommand = "SELECT * FROM visits WHERE visit_id = '$visit_id'";
    $result = mysqli_query($db->myconn,"$sqlCommand");

    if (mysqli_num_rows($result) > 0) {
        $response["visits"] = array();
        while ($row = mysqli_fetch_array($result)) {
            $visit = array();
            $visit["visit_id"] = $row["visit_id"];
            $visit["visit_petid"] = $row["visit_petid"];
            $visit["visit_userid"] = $row["visit_userid"];
            $visit["visit_date"] = $row["visit_date"];
            $visit["visit_comments"] = $row["visit_comments"];
            
            $sqlCommand2 = "SELECT user_name FROM users WHERE user_id = '".$row["visit_userid"]."'";
            $result2 = mysqli_query($db->myconn, "$sqlCommand2");
            $row = mysqli_fetch_array($result2);
            $visit["visit_vetname"] = $row[0];
            
            array_push($response["visits"], $visit);
        }
        $response["success"] = 1;
    } else {
    $response["success"] = 0;
    $response["message"] = "No visits found";
}
    echo json_encode($response);
    $db->close($db->myconn);
}
?>