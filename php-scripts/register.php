<?php
if (isset($_POST['Token'])) {
 
    $token = $_POST['Token'];

    require_once __DIR__ . '/db_connect.php';

    $myConnection= new DB_CONNECT();
    $myConnection->connect();

    $sqlCommand="UPDATE users set user_token = '$token' where user_name = '$test'";
    $result = mysqli_query($myConnection->myconn, "$sqlCommand");

    if ($result) {
        $response["success"] = 1;
        $response["message"] = "Token successfully created.";
 
        echo json_encode($response);
    } else {
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
 
        echo json_encode($response);
    }
    
    mysqli_close($myConnection);
    
} else {
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    echo json_encode($response);
}
?>
