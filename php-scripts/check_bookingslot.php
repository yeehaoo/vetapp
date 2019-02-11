<?php

if (isset($_POST["visit_date"])) {
    $visit_date = $_POST["visit_date"];
    require_once __DIR__ . '/db_connect.php';
    $db = new DB_CONNECT();
    $db->connect();

    $sqlCommand = "SELECT * FROM visits WHERE visit_date = '$visit_date'";
    $result = mysqli_query($db->myconn,"$sqlCommand");

    if (mysqli_num_rows($result) > 0) {
        $response["pets"] = array();
        while ($row = mysqli_fetch_array($result)) {
            $pet = array();
            $pet["visit_slot"] = $row["visit_slot"];
            array_push($response["pets"], $pet);
        }
        $response["success"] = 1;
    } else {
    $response["success"] = 0;
    $response["message"] = "Slot available";
}
    echo json_encode($response);
    $db->close($db->myconn);
}
?>