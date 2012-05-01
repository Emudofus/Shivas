package org.shivas.data.converter;

public class VemuConverter implements Converter {
	public static final int ID = 1;
	
	private String host, user, passwd, db;
	
	private void init() {		
		host = App.prompt("Veuillez entrer l'adresse du serveur MySQL");
		user = App.prompt("Veuillez entrer le nom d'utilisateur");
		passwd = App.prompt("Veuillez entrer le mot de passe");
		db = App.prompt("Veuillez entrer le nom de la base de donnée");
	}

	@Override
	public void start() {
		App.log("Vous avez choisis le convertisseur pour base de donnée Vemu");
		
		init();
	}
}
