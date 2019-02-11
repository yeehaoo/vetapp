<?php

$response = array();

if (isset($_POST['pet_name']) && isset($_POST['pet_userid']) && isset($_POST['pet_type']) && isset($_POST['pet_dob']) && isset($_POST['pet_sex'])) {
 
    $pet_name = $_POST['pet_name'];
    $pet_userid = $_POST['pet_userid'];
    $pet_type = $_POST['pet_type'];
    $pet_dob = $_POST['pet_dob'];
    $pet_sex = $_POST['pet_sex'];

    require_once __DIR__ . '/db_connect.php';

    $myConnection= new DB_CONNECT();
    $myConnection->connect();

    $sqlCommand="INSERT INTO pets(pet_name, pet_userid, pet_type, pet_dob, pet_sex) VALUES('$pet_name', '$pet_userid', '$pet_type', '$pet_dob', '$pet_sex')";
    $result =mysqli_query($myConnection->myconn, "$sqlCommand");

    if ($result) {
        $response["success"] = 1;
        $response["message"] = "Pet successfully registered.";
 
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
