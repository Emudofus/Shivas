# Shivas [![Build Status](https://travis-ci.org/Emudofus/Shivas.svg?branch=master)](https://travis-ci.org/Emudofus/Shivas)

[Fonctionnalités terminées](https://github.com/Emudofus/Shivas/milestone/1?closed=1) -
[Fonctionnalités en cours](https://github.com/Emudofus/Shivas/issues?q=is%3Aopen+is%3Aissue+label%3Ainprogress) -
[Fonctionnalités restantes](https://github.com/Emudofus/Shivas/milestone/1) -
[Wiki pas à jour](https://github.com/Emudofus/Shivas/wiki)

### Contributeurs

* Blackrush <blackrushx@gmail.com>

### Comment lancer ?

Temps estimé : ~5 min

1. [Téléchargez la dernière version](https://github.com/Emudofus/Shivas/releases)
2. Dé-zippez l'archive (sur Linux: `unzip shivas-host-*.zip`)
3. Déplacez le dossier de l'émulateur vers le répertoire où vous souhaitez installer l'émulateur
4. Modifiez le fichier de config `config.yaml` à votre guise
    * veillez à renseigner des informations de connexion vers votre serveur MariaDB
    * veillez à renseigner le bon chemin `data.path` vers le dossier `data` de Shivas
    * veillez à renseigner le bon chemin `mods.path` vers le dossier `mods` de Shivas, si vous avez des mods d'installés
    * il est fortement déconseillé de modifier des valeurs de la configuration dont vous n'avez pas une idée précise de son utilité, dans le cas où cela arrive demandez de l'aide sur un forum
5. Démarrez votre serveur MariaDB et créez une base de donnée pour Shivas
6. Lancez le fichier de migration `shivas.sql` sur la base de donnée créée
7. Démarrez le serveur depuis l'un des scripts présents dans `bin/`
    * si vous êtes sur Windows, vous pouvez vous contenter de double-cliquer sur `bin/shivas-host.bat`
    * si vous êtes sur Linux/macOS/BSD/Solaris/…, lancez votre terminal préféré et exécutez `bin/shivas-host`
8. (Optionnel) un fichier `config.xml` servant de configuration au client est disponible dans `resources/` si comme moi vous oubliez tout le temps sa structure

### Comment compiler ?

1. Faites en sorte d'avoir un JDK à jour, au minimum `1.8.0`
2. Récupérez les sources
    * vous êtes contributeur : lancez `git clone git@github.com:Emudofus/Shivas.git`
    * vous n'êtes pas contributeur : télécharger [les sources au format ZIP sur Github](https://github.com/Emudofus/Shivas/archive/master.zip)
3. Lancez la commande `./gradlew clean build` dans votre terminal préféré
4. Lancez la commande `./gradlew test` à chaque modification pour vérifier que votre code ne casse pas le noyau de Shivas

### Comment contribuer ?

* [Vous avez découvert un bug](https://github.com/Emudofus/Shivas/issues/new)
* [Vous souhaitez améliorer le code source](https://github.com/Emudofus/Shivas/compare)
* Vous souhaitez devenir contributeur : contactez `blackrushx@gmail.com` par email
* Vous souhaitez améliorer la base de données : contactez `blackrushx@gmail.com` par email
* Vous souhaitez améliorer la documentation : contactez `blackrushx@gmail.com` par email
