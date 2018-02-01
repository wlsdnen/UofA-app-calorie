<?php

class DatabaseAdaptor {

	private $DB;

	public function __construct()
	{
		// PDO object arguments
		$db_name = 'mysql:dbname=calorie;host=localhost';
		$user = 'root';
		$password = 'admin';

		try
		{
			$this->DB = new PDO($db_name,$user,$password);
			$this->DB->setAttribute (PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
		}
		catch (PDOException $e)
		{
			echo ('Error establishing connection to database.');
			exit();
		}
	}

	public function updateUser($user_id, $age, $gender, $height, $weight)
	{
  		$stmt = $this->DB->prepare("UPDATE users SET age=?, gender=?, height=?, weight=? WHERE user_id=?");
  		$variables = [$age, $gender, $height, $weight, $user_id];
  		$result = $stmt->execute($variables);
			return $result;
	}

	public function initUser($user_id)
	{
		//create new dummy user
		$stmt = $this->DB->prepare("INSERT INTO users (user_id) SELECT * FROM (SELECT :user) AS tmp WHERE NOT EXISTS (SELECT user_id FROM users WHERE user_id = :user) LIMIT 1");
		$stmt->bindParam('user', $user_id);
		$result = $stmt->execute();
		return $result;
	}

	public function insertEstimate($uid, $iid, $est, $loc, $width, $height, $inch, $device, $start, $end)
	{
		$stmt = $this->DB->prepare("INSERT INTO estimates VALUES (NULL, :user_id, :image_id, :estimate, :location, :screen_width, :screen_height, :screen_inch, :device, :started, :ended)");
		$stmt->bindParam('user_id', $uid);
		$stmt->bindParam('image_id', $iid);
		$stmt->bindParam('estimate', $est);
		$stmt->bindParam('location', $loc);
		$stmt->bindParam('screen_width', $width);
		$stmt->bindParam('screen_height', $height);
		$stmt->bindParam('screen_inch', $inch);
		$stmt->bindParam('device', $device);
		$stmt->bindParam('started', $start);
		$stmt->bindParam('ended', $end);
		$result = $stmt->execute();
		return $result;
	}

}

$mDBConn = new DatabaseAdaptor();
