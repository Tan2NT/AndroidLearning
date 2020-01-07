<?php

require "dbCon.php";

$query = "SELECT * FROM student";

$data = mysqli_query($connect, $query);

// 1 create class Student
class Student{
	function Student($id, $name, $yearOfBird, $address){
		$this->id = $id;
		$this->name = $name;
		$this->yearOfBird = $yearOfBird;
		$this->address = $address;
	}
}

// 2. Create an array of Student
$studentArray = array();

// 3. Add student into the array
while($row = mysqli_fetch_assoc($data)){
	array_push($studentArray, new Student($row['id'], $row['name'], $row['yearOfBird'], $row['address']));
}

// 4. convert to json format
echo json_encode($studentArray);


?>