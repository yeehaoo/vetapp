<?php

$response = array();

if (isset($_POST['username']) && isset($_POST['password'])) {
 
    $username = $_POST['username'];
    $password = $_POST['password'];
    $password = md5($password);

    require_once __DIR__ . '/db_connect.php';

    $myConnection= new DB_CONNECT();
    $myConnection->connect();

    $sqlCommand="INSERT INTO users(user_name, user_passwordmd5) VALUES('$username', '$password')";
    $result =mysqli_query($myConnection->myconn, "$sqlCommand");

    if ($result) {
        $response["success"] = 1;
        $response["message"] = "User successfully created.";
 
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
