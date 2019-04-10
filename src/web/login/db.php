<?php
	try{
		$mongo = new MongoDB\Driver\Manager("mongodb://localhost:27017");

		$command = new MongoDB\Driver\Command(['ping'=>1]);
	} catch (MongoDB\Driver\Exception\Exception $e){
		$filename = basename(__FILE__);

		echo "FAIL";
		echo "Exception:", $e->getMassage(), "\n";
		echo "In file:", $e->getFile(), "\n";
		echo "On line:", $e->getLine(), "\n";
	}
?>
