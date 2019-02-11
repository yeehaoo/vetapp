<?php

if (isset($_POST["id"])) {
    $id = $_POST["id"];
    
    require_once __DIR__ . '/db_connect.php';
    $db = new DB_CONNECT();
    $db->connect();

    $sqlCommand = "SELECT * FROM pets WHERE pet_userid = '$id'";
    $result = mysqli_query($db->myconn,"$sqlCommand");

    if (mysqli_num_rows($result) > 0) {
        $response["pets"] = array();
        while ($row = mysqli_fetch_array($result)) {
            $pet = array();
            $pet["pet_id"] = $row["pet_id"];
            $pet["pet_name"] = $row["pet_name"];
            $pet["pet_userid"] = $row["pet_userid"];
            $pet["pet_type"] = $row["pet_type"];
            $pet["pet_dob"] = $row["pet_dob"];      
            $pet["pet_sex"] = $row["pet_sex"];
            
            array_push($response["pets"], $pet);
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