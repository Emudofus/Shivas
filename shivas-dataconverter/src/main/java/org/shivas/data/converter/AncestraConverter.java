package org.shivas.data.converter;

import java.sql.ResultSet;

import org.atomium.util.Action1;

public class AncestraConverter extends MySqlUserConverter {
	
	private void init() {
		initConnection(
				App.prompt("Veuillez entrer l'adresse de votre serveur MySQL"),
				App.prompt("Veuillez entrer le nom de votre base de donnée"),
				App.prompt("Veuillez entrer votre nom d'utilisateur"),
				App.prompt("Veuillez entrer votre mot de passe")
		);
	}

	@Override
	public void start(final DataOutputter out) {
		App.log("Vous avez choisit le convertisseur pour base de donnée Ancestra.");
		
		init();
		
		App.log("Les niveaux d'expérience vont être chargés puis écris, cela peut prendre quelques secondes.");
		super.query(q.select("").toQuery(), new Action1<ResultSet>() {
			public Void invoke(ResultSet arg1) throws Exception {
				
				return null;
			}
		});
		App.log("Tous les niveaux d'expériences ont été écris.");
	}

}
