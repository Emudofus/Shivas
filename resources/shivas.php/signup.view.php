<?php if ($success): ?>
	<h3>Bienvenue sur <?php echo SERVER_NAME ?> <u><?php echo $fields['name'] ?></u> !</h3>
	<p>
		Vous pouvez dès maintenant vous connecter !
	</p>
<?php else: ?>
	<form action="<?php echo SCRIPT_NAME ?>" method="post">
		<?php print_errors($errors, 'global') ?>
		<div>
			<?php print_errors($errors, 'name') ?>
			<label for="name">Nom de compte :</label>
			<input type="text" name="name" id="name" value="<?php echo $fields['name'] ?>" />
		</div>
		<div>
			<?php print_errors($errors, 'password') ?>
			<label for="password">Mot de passe :</label>
			<input type="password" name="password" id="password" />
		</div>
		<div>
			<?php print_errors($errors, 'nickname') ?>
			<label for="nickname">Pseudo :</label>
			<input type="text" name="nickname" id="nickname" value="<?php echo $fields['nickname'] ?>" />
		</div>
		<div>
			<?php print_errors($errors, 'question') ?>
			<label for="question">Question secrète :</label>
			<input type="text" name="question" id="question" value="<?php echo $fields['question'] ?>" />
		</div>
		<div>
			<?php print_errors($errors, 'answer') ?>
			<label for="answer">Réponse secrète :</label>
			<input type="text" name="answer" id="answer" value="<?php echo $fields['answer'] ?>" />
		</div>
		<input type="submit" />
	</form>
<?php endif ?>

<?php if (DEBUG) { var_dump($errors); var_dump($fields); var_dump($_SERVER); var_dump($_POST); } ?>