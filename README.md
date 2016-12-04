# Shivas [![Build Status](https://travis-ci.org/Emudofus/Shivas.svg?branch=master)](https://travis-ci.org/Emudofus/Shivas)

### Contributeurs

* Blackrush <blackrushx@gmail.com>

### Description

Shivas est un émulateur Dofus 1.29 développé en Java. Aucune version stable n'est prévue pour le moment.

### Comment compiler Shivas ?

Shivas utilise Maven, le célèbre gestionnaire de dépendances externes (et bien plus) créé par Apache. Vous devez l'avoir d'installé et prêt à l'usage.

```sh
$ git clone git://github.com/Emudofus/Shivas.git Shivas
$ cd ./Shivas
$ cd shivas-common/ && mvn install
$ cd ../shivas-protocol/ && mvn install
$ cd ../shivas-data/ && mvn install
$ cd ../shivas-server/ && mvn assembly:single
$ cp ./target/shivas-server-*-jar-with-dependencies.jar ../shivas-server.jar & cd ..
$ ls | grep shivas-server.jar
```

Ce script shell va d'abord cloner les sources du dépôt GitHub pour ensuite compiler les dépendances et ensuite créer un `.jar` exécutable.
Vous pouvez ensuite lancer l'émulateur à l'aide cette commande : `java -jar shivas-server.jar`
