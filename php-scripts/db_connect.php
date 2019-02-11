<?php
/**
 * A class file to connect to database
 */
class DB_CONNECT {
    var $myconn;
  
    /**
     * Function to connect with the database
     */
    function connect() {
        // import database connection variables
        require_once __DIR__ . '/db_config.php';
        // Connecting to mysql database
        $con = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD,DB_DATABASE) or die(mysqli_error($con));
        $this->myconn = $con;
        // returning connection cursor
        return $this->myconn;
    }
    /**
     * Function to close db connection
     */
     

function close($myconn) { // closing db connection

        mysqli_close($myconn);
    }
}?>
