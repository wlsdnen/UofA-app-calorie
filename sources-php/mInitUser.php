<?php

require_once("./mDatabaseAdaptor.php");

$uid = $_POST['user_id'];

$result = $mDBConn->initUser($uid);

if ($result === false)
{
  // $error = $db->errorInfo();
  // echo $error[0]." ".$error[1]." ".$error[2]."\n";
  echo "fail";
}
else
{
  echo $_POST['user_id']."success";
}

?>
