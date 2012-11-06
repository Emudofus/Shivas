<?php
define('ALPHABET', 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789');
define('SHIVAS_ENCRYPT_ALGO', 'sha1');

function is_method($method) {
	return $_SERVER['REQUEST_METHOD'] == $method;
}

function random_string($length) {
	return substr(str_shuffle(ALPHABET), 0, $length);
}

function shivas_encrypt($password, $salt) {
	return hash(SHIVAS_ENCRYPT_ALGO, hash(SHIVAS_ENCRYPT_ALGO, $password).$salt);
}

function get_errors($validators, $values) {
	$errors = array();
	foreach ($validators as $key => $closure) {
		if (array_key_exists($key, $values)) {
			$value = $values[$key];
			$result = $closure($value);
			if ($result != NULL && is_array($result) && count($result) > 0) {
				$errors[$key] = $result;
			}
		}
	}
	return $errors;
}

function print_errors($errors, $key) {
	if (array_key_exists($key, $errors) && !empty($errors[$key])) {
		?>
		<ul class="errors">
			<?php foreach ($errors[$key] as $error): ?>
			<li class="error"><?php echo $error ?></li>
			<?php endforeach ?>
		</ul>
		<?php
	}
}

function get_fields($keys, $values) {
	$fields = array();
	foreach ($keys as $key) {
		if (array_key_exists($key, $values) && !empty($values[$key])) {
			$fields[$key] = $values[$key];
		} else {
			$fields[$key] = NULL;
		}
	}
	return $fields;
}

function array_all($array, $closure) {
	foreach ($array as $value) {
		if (!$closure($value)) {
			return false;
		}
	}
	return true;
}

function array_any($array, $closure) {
	foreach ($array as $value) {
		if ($closure($value)) {
			return true;
		}
	}
	return false;
}

$required = function($input) {
	if (empty($input)) {
		return array('Ce champ est obligatoire');
	}
};
$min_length = function($input, $min) {
	if (strlen($input) < $min) {
		return array('Minimum '.$min.' caractères');
	}
};
$max_length = function($input, $max) {
	if (strlen($input) > $max) {
		return array('Maximum '.$max.' caractères');
	}
};
$length_between = function($input, $min, $max) {
	$len = strlen($input);
	if ($len < $min || $len > $max) {
		return array('Nombre de caractères entre '.$min. ' et '.$max);
	}
};
function combine($a, $b) {
	return function($input) use($a, $b) {
		if ($result = $a($input)) {
			return $result;
		}
		return $b($input);
	};
}
function curry($closure, $param) {
	return function($input) use($closure, $param) {
		return $closure($input, $param);
	};
}
function curry2($closure, $param1, $param2) {
	return function($input) use($closure, $param1, $param2) {
		return $closure($input, $param1, $param2);
	};
}

class Validators {
	static function with($validator, $param1 = null, $param2 = null) {
		$validators = new Validators;
		$validators->plus($validator, $param1, $param2);
		return $validators;
	}

	private $validators = array();

	public function plus($validator, $param1 = null, $param2 = null) {
		$this->validators[] = curry2($validator, $param1, $param2);
		return $this;
	}

	public function build() {
		$validators = $this->validators;
		return function($input) use($validators) {
			foreach ($validators as $validator) {
				if ($result = $validator($input)) {
					return $result;
				}
			}
		};
	}
}
