package org.shivas.data.converter;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.atomium.util.Action1;
import org.shivas.common.maths.Range;
import org.shivas.common.statistics.CharacteristicType;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class d2jConverter extends MySqlUserConverter {
	
	public static Structs.BreedLevel parseLevel(String string) {
		String[] args = string.split("-");
		
		Structs.BreedLevel level = new Structs.BreedLevel();
		level.bonus = Integer.parseInt(args[0]);
		level.cost = Integer.parseInt(args[1]);
		
		return level;
	}
	
	public static Map<Range, Structs.BreedLevel> loadLevels(String string) {
		Map<Range, Structs.BreedLevel> levels = Maps.newHashMap();
		for (String s : string.split("\\|")) {
			String[] args = s.split(":");
			
			Range range = Range.parseRange(args[0], "\\,");
			Structs.BreedLevel level = parseLevel(args[1]);
			
			levels.put(range, level);
		}
		return levels;
	}
	
	private Map<Integer, Structs.GameMap> maps = Maps.newHashMap();
	
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
		App.log("Vous avez choisis le convertisseur pour base de donnée d2j");
		
		init();

		App.log("Les races vont être chargées puis écrite, cela peut prendre quelques secondes");
		query(q.select("breed_templates").toQuery(), new Action1<ResultSet>() {
			public Void invoke(ResultSet arg1) throws Exception {
				createBreeds(arg1, App.prompt("Veuillez entrer le répertoire où seront stockés les races"), out);
				return null;
			}
		});
		App.log("Toutes les races ont été stockées");

		App.log("Les niveaux d'expériences vont être chargés puis écrit, cela peut prendre quelques secondes");
		query(q.select("experience_templates").toQuery(), new Action1<ResultSet>() {
			public Void invoke(ResultSet arg1) throws Exception {
				createExperiences(arg1, App.prompt("Veuillez entrer le répertoire où seront stockés les niveaux d'expérience"), out);
				return null;
			}
		});
		App.log("Tous les niveaux d'expériences ont été stockés");

		App.log("Les cartes vont être chargées, cela peut prendre quelques minutes");
		query(q.select("map_templates").toQuery(), new Action1<ResultSet>() {
			public Void invoke(ResultSet arg1) throws Exception {
				loadMaps(arg1);
				return null;
			}
		});
		App.log("%d cartes ont été chargées", maps.size());

		App.log("Les triggers de cartes vont être chargés, cela peut prendre quelques minutes");
		query(q.select("map_triggers").toQuery(), new Action1<ResultSet>() {
			public Void invoke(ResultSet arg1) throws Exception {
				loadTriggers(arg1);
				return null;
			}
		});
		App.log("Tous les triggers ont été chargés");
		
		try {
			App.log("Les cartes vont maintenant être écrite");
			out.outputMaps(maps.values(), App.prompt("Veuillez entrer le répertoire où seront stockés les cartes") + "maps");
			App.log("Toutes les cartes ont été écrites");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		App.log("Tout s'est bien passé, vous pouvez à présent lancer l'émulateur");
	}

	private void createBreeds(ResultSet result, String directory, DataOutputter out) throws Exception {		
		while (result.next()) {
			/*** INPUT : MySQL ***/
			
			Structs.Breed breed = new Structs.Breed();
			
			breed.id = result.getInt("id");
			breed.name = result.getString("name");
			breed.startLife = result.getShort("startLife");
			breed.startProspection = result.getShort("startProspection");
			breed.startActionPoints = result.getShort("startAP");
			breed.startMovementPoints = result.getShort("startMP");
			breed.levels.put(CharacteristicType.Vitality, loadLevels(result.getString("vitality")));
			breed.levels.put(CharacteristicType.Wisdom, loadLevels(result.getString("wisdom")));
			breed.levels.put(CharacteristicType.Strength, loadLevels(result.getString("strength")));
			breed.levels.put(CharacteristicType.Intelligence, loadLevels(result.getString("intelligence")));
			breed.levels.put(CharacteristicType.Chance, loadLevels(result.getString("chance")));
			breed.levels.put(CharacteristicType.Agility, loadLevels(result.getString("agility")));
			
			/*** OUTPUT ***/

			out.outputBreed(breed, directory + breed.name);
		}
	}
	
	private void createExperiences(ResultSet result, String directory, DataOutputter out) throws Exception {
		List<Structs.Experience> exps = Lists.newArrayList();
		
		while (result.next()) {
			/*** INPUT : MySQL ***/
			
			Structs.Experience exp = new Structs.Experience();
			exp.level = result.getInt("level");
			exp.player = result.getLong("character");
			exp.job = result.getInt("job");
			exp.mount = result.getInt("mount");
			exp.alignment = result.getShort("alignment");
			
			/*** OUTPUT ***/
			exps.add(exp);
		}

		out.outputExperiences(exps, directory + "experiences");
	}
	
	private void loadMaps(ResultSet result) throws Exception {
		while (result.next()) {
			Structs.GameMap map = new Structs.GameMap();
			map.id = result.getInt("id");
			map.position.setAbscissa(result.getInt("abscissa"));
			map.position.setOrdinate(result.getInt("ordinate"));
			map.width = result.getInt("width");
			map.height = result.getInt("height");
			map.data = result.getString("data");
			map.date = result.getString("date");
			map.key = result.getString("key");
			map.subscriber = result.getBoolean("subscriberArea");
			
			maps.put(map.id, map);
		}
	}
	
	private void loadTriggers(ResultSet result) throws Exception {
		while (result.next()) {
			Structs.GameMapTrigger trigger = new Structs.GameMapTrigger();
			trigger.id = result.getInt("id");
			trigger.nextMap = maps.get(result.getInt("nextMap"));
			trigger.cell = result.getShort("cell");
			trigger.nextCell = result.getShort("nextCell");
			
			Structs.GameMap map = maps.get(result.getInt("map"));
			if (map != null) {
				map.triggers.add(trigger);
			}
		}
	}
}
