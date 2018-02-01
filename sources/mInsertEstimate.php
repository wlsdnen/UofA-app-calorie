<?php

require_once("./mDatabaseAdaptor.php");

$uid = isset($_POST['uid']) ? $_POST['uid'] : '';
$iid = isset($_POST['iid']) ? $_POST['iid'] : '';
$est = isset($_POST['est']) ? $_POST['est'] : '';
$loc = isset($_POST['loc']) ? $_POST['loc'] : '';
$inch = isset($_POST['inch']) ? $_POST['inch'] : '';
$width = isset($_POST['width']) ? $_POST['width'] : '';
$height = isset($_POST['height']) ? $_POST['height'] : '';
$device = isset($_POST['device']) ? $_POST['device'] : '';
$start = isset($_POST['start']) ? $_POST['start'] : '';
$end = isset($_POST['end']) ? $_POST['end'] : '';
// echo $uid."\n".$iid."\n".$est."\n".$loc."\n".$inch."\n".$width."\n".$height."\n".$device."\n".$start."\n".$end;

$result = $mDBConn->insertEstimate($uid, $iid, $est, $loc, $inch, $width, $height, $device, $start, $end);

if ($result === false)
{
  echo "fail";
}
else
{
  echo $_POST['uid']." "."success";
}

?>
