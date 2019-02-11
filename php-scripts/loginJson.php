<?php

$response = array();

if (isset($_POST['username']) && isset($_POST['password'])) {
    $username = $_POST['username'];
    $password = $_POST['password'];
    $password = md5($password);

    require_once __DIR__ . '/db_connect.php';
    $myConnection= new DB_CONNECT();
    $myConnection->connect();
 
    $sqlCommand="SELECT user_passwordmd5 FROM users WHERE user_name = '" . $username . "'";
    $result =mysqli_query($myConnection->myconn, "$sqlCommand");

    if(mysqli_num_rows($result) > 0) {
        while ($row = mysqli_fetch_row($result)) {
            foreach($row as $cell) {
                if($password == $cell){
                    //password match
                    $response["success"] = 1;
                    $response["message"] = "Login Correct";
                    
                    $sqlCommand = "SELECT user_id FROM users WHERE user_name = '" . $username . "'";
                    $result = mysqli_query($myConnection->myconn, "$sqlCommand");
                    $id = mysqli_fetch_row($result);
                    $response["id"] = $id[0];
                } else {
                    //password does not match
                    $response["success"] = 0;
                    $response["message"] = "Passsword does not match";
                }
            }
        }
    } else {
        //username not found
        $response["success"] = 0;
        $response["message"] = "Username not found in database";
    }
    
}

echo json_encode($response);
?>
