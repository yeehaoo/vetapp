<?php

if (isset($_POST["pet_name"]) && isset($_POST["pet_userid"])) {
    $pet_name = $_POST["pet_name"];
    $pet_userid = $_POST["pet_userid"]
    require_once __DIR__ . '/db_connect.php';
    $db = new DB_CONNECT();
    $db->connect();

    $sqlCommand = "SELECT pet_id FROM pets WHERE pet_name = '$pet_name' AND pet_userid = '$pet_userid'";
    $result = mysqli_query($db->myconn,"$sqlCommand");

    if (mysqli_num_rows($result) > 0) {
        $response["petname"] = array();
        while ($row = mysqli_fetch_array($result)) {
            $pet = array();
            $pet["pet_id"] = $row["pet_id"];
            array_push($response["petname"], $pet);
        }
        $response["success"] = 1;
    } else {
    $response["success"] = 0;
    $response["message"] = "No pets found";
}
    echo json_encode($response);
    $db->close($db->myconn);
}
?>