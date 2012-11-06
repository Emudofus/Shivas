<?php
define('DEBUG', false);
define('SERVER_NAME', 'Shivas');
define('SCRIPT_NAME', $_SERVER['SCRIPT_NAME']);

define('DB_HOST', 'localhost');
define('DB_NAME', 'shivas');
define('DB_USER', 'root');
define('DB_PASSWD', '');
define('DB_INSERT_QUERY', 'INSERT INTO accounts(name, password, salt, nickname, question, answer) VALUES(:name, :password, :salt, :nickname, :question, :answer);');

require_once "common.lib.php";

$success = false;

$fields = get_fields(array('name', 'password', 'nickname', 'question', 'answer'), $_POST);
$validators = array(
	'name'     => Validators::with($required)->plus($length_between, 4, 15)->build(),
	'password' => Validators::with($required)->plus($min_length, 4)->build(),
	'nickname' => Validators::with($required)->plus($min_length, 4)->build(),
	'question' => Validators::with($required)->plus($min_length, 4)->build(),
	'answer'   => Validators::with($required)->plus($min_length, 4)->build()
);
$errors = is_method('POST') ? get_errors($validators, $fields) : array();

if (is_method('POST') && $success = count($errors) <= 0) {
	$fields['salt'] = random_string(32);
	$fields['password'] = shivas_encrypt($fields['password'], $fields['salt']);

	try {
		$db = new PDO('mysql:host='.DB_HOST.';dbname='.DB_NAME, DB_USER, DB_PASSWD);
		$db->prepare(DB_INSERT_QUERY)->execute($fields);
	} catch (Exception $e) {
		$success = false;
		$errors['global'] = array($e->getMessage());
	}
}

include "signup.view.php";