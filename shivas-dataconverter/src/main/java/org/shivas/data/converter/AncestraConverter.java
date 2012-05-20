package org.shivas.data.converter;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.atomium.util.Action1;
import org.atomium.util.query.Order;
import org.shivas.common.maths.Point;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class AncestraConverter extends MySqlUserConverter {
	
	private final Map<Integer, Structs.GameMap> maps = Maps.newHashMap();
	
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
		
		//////////// EXPERIENCES /////////////////
		
		App.log("Les niveaux d'expérience vont être chargés puis écris, cela peut prendre quelques secondes.");
		super.query(q.select("experience").orderBy("lvl", Order.ASC).toQuery(), new Action1<ResultSet>() {
			public Void invoke(ResultSet arg1) throws Exception {
				createExperiences(
						arg1,
						App.prompt("Veuillez entrer le répertoire où seront stockés les niveaux d'expérience"),
						out
				);
				return null;
			}
		});
		App.log("Tous les niveaux d'expériences ont été écris.");

		//////////// MAPS /////////////////
		
		App.log("Les cartes vont être chargées, cela peut prendre quelques minutes.");
		super.query(q.select("maps").toQuery(), new Action1<ResultSet>(){
			public Void invoke(ResultSet arg1) throws Exception {
				loadMaps(arg1);
				return null;
			}
		});
		App.log("%d cartes ont été chargées.", maps.size());
		
		App.log("Les triggers de cartes vont être chargés, cela peut prendre quelques minutes.");
		super.query(q.select("scripted_cells").toQuery(), new Action1<ResultSet>() {
			public Void invoke(ResultSet arg1) throws Exception {
				loadTriggers(arg1);
				return null;
			}
		});
		App.log("Tous les triggers de cartes ont été chargés.");
		
		try {
			App.log("Les cartes vont maintenant être écrites, cela peut prendre quelques minutes.");
			out.outputMaps(maps.values(), App.prompt("Veuillez entrer le répertoire où seront stockés les cartes") + "maps");
			App.log("Toutes les cartes ont été écrites.");
		} catch (IOException e) {
			System.exit(1); // FATAL ERROR
		}
		
		App.log("Tout s'est bien passé, vous pouvez à présent lancer l'émulateur");
	}
	
	private void createExperiences(ResultSet results, String directory, DataOutputter out) throws SQLException, IOException {
		List<Structs.Experience> exps = Lists.newArrayList();
		
		while (results.next()) {
			/*** INPUT : MySQL ***/
			Structs.Experience exp = new Structs.Experience();
			exp.level = results.getInt("lvl");
			exp.player = results.getLong("perso");
			exp.job = results.getInt("metier");
			exp.mount = results.getInt("dinde");
			exp.alignment = results.getShort("pvp");
			
			/*** OUTPUT ***/
			exps.add(exp);
		}
		
		out.outputExperiences(exps, directory + "experiences");
	}
	
	private Point parsePosition(String string) {
		String[] args = string.split(","); // <x>,<y>,<subarea>
		return new Point(
				Integer.parseInt(args[0]),
				Integer.parseInt(args[1])
		);
	}
	
	private void loadMaps(ResultSet r) throws SQLException {
		while (r.next()) {
			Structs.GameMap map = new Structs.GameMap();
			map.id = r.getInt("id");
			map.position = parsePosition(r.getString("mappos"));
			map.width = r.getInt("width");
			map.height = r.getInt("heigth");
			map.data = r.getString("mapData");
			map.date = r.getString("date");
			map.key = r.getString("key");
			map.subscriber = false; // there isn't column "subscriber" in ancestra's static db
			
			maps.put(map.id, map);
		}
	}
	
	private void loadTriggers(ResultSet r) throws SQLException {
		int nextId = 0;
		while (r.next()) {
			if (r.getInt("EventID") != 1) continue; // not a triggers
			
			Structs.GameMap map = maps.get(r.getInt("MapID"));
			if (map == null) {
				App.log("Un trigger fait référence à une carte inconnue (%d), il est par conséquent ignoré.", r.getInt("MapID"));
				continue;
			}
			
			Structs.GameMapTrigger trigger = new Structs.GameMapTrigger();
			trigger.id = ++nextId;
			trigger.cell = r.getShort("CellID");
			
			String[] args = r.getString("ActionsArgs").split(",");
			if (args.length == 2) {
				trigger.nextMap = maps.get(Integer.parseInt(args[0].trim()));
				trigger.nextCell = Short.parseShort(args[1].trim());
			} else {
				App.log("Un trigger n'a aucune référence à une autre carte, il est par conséquent ignoré.");
				continue;
			}
			
			map.triggers.add(trigger);
		}
	}

}
