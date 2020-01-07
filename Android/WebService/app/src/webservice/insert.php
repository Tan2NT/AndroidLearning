<?php
require "dbCon.php";
require "demo.php";
 
$name = $_POST['student_name'];
$yearOfBird = $_POST['student_year'];
$address = $_POST['student_address'];

$query = "INSERT INTO student VALUES(null, '$name', '$yearOfBird', '$address')";

if(mysqli_query($connect, $query)){

	$student = new Student(null, $name, $yearOfBird, $address)

	echo json_encode($student);
}else{
}
	$error = ""
	echo json_encode($error)
?>