<?php

// 1 create class Student
class Student{
	function Student($name, $yearOfBird, $address){
		$this->name = $name;
		$this->yearOfBird = $yearOfBird;
		$this->address = $address;
	}
}

// 2. Create an array of Student
$studentArray = array();

// 3. Add student into the array
array_push($studentArray, new Student("Hoang Ngoc Tan", 1991, "Hue"));
array_push($studentArray, new Student("Tran Thanh Phong", 1995, "Da Nang"));

// 4. convert to json format
echo json_encode($studentArray);

?>