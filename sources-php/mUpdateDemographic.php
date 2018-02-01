<?php

require_once("./mDatabaseAdaptor.php");

$uid = isset($_POST['uid']) ? $_POST['uid'] : '';
$age = isset($_POST['age']) ? $_POST['age'] : '';
$gender = isset($_POST['gender']) ? $_POST['gender'] : '';
$height = isset($_POST['height']) ? $_POST['height'] : '';
$weight = isset($_POST['weight']) ? $_POST['weight'] : '';

echo $uid." ".$age." ".$gender." ".$height." ".$weight;

$result = $mDBConn->updateUser($uid, $age, $gender, $height, $weight);
// echo $result;

// if ($result === false)
// {
//   echo "fail";
// }
// else
// {
//   echo $uid." "."success";
// }

?>
