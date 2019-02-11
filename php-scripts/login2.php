<?php
session_start();
if (isset($_POST["username"]) && isset($_POST["password"])) {
    $username = $_POST['username'];
    $password = $_POST['password'];
    $password = md5($password);

    require_once __DIR__ . '/db_connect.php';
    $db= new DB_CONNECT();
    $db->connect();
    
    $sqlCommand="SELECT user_passwordmd5, user_vetstatus, user_id FROM users WHERE user_name = '" . $username . "'";
    $result = mysqli_query($db->myconn, "$sqlCommand");
    
    $fields_num = mysqli_num_fields($result);
    
    for($i=0; $i<$fields_num; $i++) { 
        $field = mysqli_fetch_field($result);
    }
    
    if (mysqli_num_rows($result) > 0) {
        while ($row = mysqli_fetch_row($result)) {
                if($password == $row[0] && $row[1] == "true"){
                    echo "Authenticated! Redirecting...";
                    $_SESSION["id"] = $row[2];
                    $_SESSION["username"] = $username;
                    echo '<script type="text/javascript">
           window.location = "http://mdadvetapp.atspace.cc/home.php"
      </script>';
                } else {
                    echo "No Match";
                }
        }
    } else {
        echo "Username does not exist";
    }
    
    $db->close($db->myconn);
}

?>
 

