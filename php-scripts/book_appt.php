<?php

$response = array();

if (isset($_POST['visit_date']) && isset($_POST['visit_slot']) && isset($_POST['id']) && isset($_POST['visit_petid'])) {
 
    $visit_petid = $_POST['visit_petid'];
    $appt_date = $_POST['visit_date'];
    $timeslot = $_POST['visit_slot'];
    $id = $_POST['id'];

    require_once __DIR__ . '/db_connect.php';

    $myConnection= new DB_CONNECT();
    $myConnection->connect();

    $sqlCommand="INSERT INTO visits(visit_petid, visit_userid, visit_date, visit_slot) VALUES('$visit_petid', '$id', '$appt_date', '$timeslot')";
    $result =mysqli_query($myConnection->myconn, "$sqlCommand");

    if ($result) {
        $response["success"] = 1;
        $response["message"] = "Booking Successful.";
 
        echo json_encode($response);
    } else {
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
 
        echo json_encode($response);
    }
} else {
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    echo json_encode($response);
}
?>
