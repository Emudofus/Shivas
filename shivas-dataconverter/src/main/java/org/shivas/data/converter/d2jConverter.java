package org.shivas.data.converter;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.atomium.util.Action1;
import org.shivas.common.maths.Range;
import org.shivas.common.random.Dofus1Dice;
import org.shivas.common.statistics.CharacteristicType;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

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

	private Map<Integer, Structs.Breed> breeds;
	private Map<Integer, Structs.SpellTemplate> spells;
	private Map<Integer, Structs.GameMap> maps;
	
	private void init() {
		initConnection(
				App.prompt("Veuillez entrer l'adresse de votre serveur MySQL"),
				App.prompt("Veuillez entrer le nom de votre base de donnée"),
				App.prompt("Veuillez entrer votre nom d'utilisateur"),
				App.prompt("Veuillez entrer votre mot de passe")
		);
	}
	
	private boolean canWrite(String message) {
		return App.prompt(message).equalsIgnoreCase("OUI");
	}

	@Override
	public void start(final DataOutputter out) {
		App.log("Vous avez choisis le convertisseur pour base de donnée d2j");
		
		init();
		
		if (canWrite("Souhaitez-vous écrire les sorts ?")) {
			spells = Maps.newHashMapWithExpectedSize(1882);
			
			App.log("Les sorts vont être chargés puis écris, cela peut prendre quelques secondes");
			query(q.select("spell_templates").toQuery(), new Action1<ResultSet>() {
				public Void invoke(ResultSet arg1) throws Exception {
					createSpells(arg1, App.prompt("Veuillez entrer le répertoire où seront stockés les sorts"), out);
					return null;
				}
			});
			App.log("Tous les sorts ont été écris");
		}

		if (spells != null && canWrite("Souhaitez-vous écrire les races ?")) {
			breeds = Maps.newHashMapWithExpectedSize(12);
			
			App.log("Les races vont être chargées, cela peut prendre quelques secondes");
			query(q.select("breed_templates").toQuery(), new Action1<ResultSet>() {
				public Void invoke(ResultSet arg1) throws Exception {
					loadBreeds(arg1);
					return null;
				}
			});
			App.log("Toutes les races ont été chargées");
			
			App.log("Les sorts de races vont être chargés, cela peut prendre quelques secondes");
			query(q.select("spell_breed_templates").toQuery(), new Action1<ResultSet>() {
				public Void invoke(ResultSet arg1) throws Exception {
					loadSpellBreeds(arg1);
					return null;
				}
			});
			App.log("Tous les sorts de races ont été chargés");
			
			try {
				App.log("Les races vont maintenant être écrites, cela peut prendre quelques secondes");
				out.outputBreeds(breeds.values(), App.prompt("Veuillez entrer le répertoire où seront stockés les races"));
				App.log("Toutes les races ont été écrites");
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
			spells.clear();
			breeds.clear();
			spells = null;
			breeds = null;
		}

		if (canWrite("Souhaitez-vous écrire les niveaux d'expériences ?")) {
			App.log("Les niveaux d'expériences vont être chargés puis écrit, cela peut prendre quelques secondes");
			query(q.select("experience_templates").toQuery(), new Action1<ResultSet>() {
				public Void invoke(ResultSet arg1) throws Exception {
					createExperiences(arg1, App.prompt("Veuillez entrer le répertoire où seront stockés les niveaux d'expérience"), out);
					return null;
				}
			});
			App.log("Tous les niveaux d'expériences ont été stockés");
		}

		if (canWrite("Souhaitez-vous écrire les cartes ?")) {
			maps = Maps.newHashMapWithExpectedSize(7000);
			
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
			
			maps.clear(); // release the krakken
			maps = null;
		}
		
		App.log("Tout s'est bien passé, vous pouvez à présent lancer l'émulateur");
	}
	
	private void loadEffects(String string, List<Structs.SpellEffect> effects) {
		for (String str : string.split("\\|")) {
			if (str.equalsIgnoreCase("-1") || str.isEmpty()) continue;
			
			String[] args = str.split(";");
			if (args.length <= 1) continue;
			
			Structs.SpellEffect effect = new Structs.SpellEffect();
			effect.type = Integer.parseInt(args[0]);
			effect.first = Short.parseShort(args[1]);
			effect.second = Short.parseShort(args[2]);
			effect.third = Short.parseShort(args[3]);
			if (args.length > 4) effect.turns = Short.parseShort(args[4]);
			if (args.length > 5) effect.chance = Short.parseShort(args[5]);
			if (args.length > 6) effect.dice = Dofus1Dice.parseDice(args[6]);
			if (args.length > 7) effect.target = args[7];
			
			effects.add(effect);
		}
	}
	
	private String integerToReadableString(int i) {
		switch (i) {
		case 1:
			return "first";
		case 2:
			return "second";
		case 3:
			return "third";
		case 4:
			return "fourth";
		case 5:
			return "fifth";
		case 6:
			return "sixth";
		default:
			return "NIL";
		}
	}

	private void createSpells(ResultSet r, String directory, DataOutputter out) throws Exception {		
		while (r.next()) {
			Structs.SpellTemplate spell = new Structs.SpellTemplate();
			spell.id = r.getShort("id");
			spell.sprite = r.getShort("sprite");
			spell.spriteInfos = r.getString("spriteInfos");
			
			for (byte i = 1; i <= 6; ++i) {
				String str = r.getString(integerToReadableString(i));
				if (str.equalsIgnoreCase("-1") || str.isEmpty()) continue;
				String[] args = str.split(",");
				
				Structs.SpellLevel level = new Structs.SpellLevel();
				level.id = i;
				level.costAP = args[2].isEmpty() ? 6 : Byte.parseByte(args[2].trim());
				level.minRange = Byte.parseByte(args[3].trim());
				level.maxRange = Byte.parseByte(args[4].trim());
				level.criticalRate = Short.parseShort(args[5].trim());
				level.failureRate = Short.parseShort(args[6].trim());
				level.inline = args[7].trim().equalsIgnoreCase("true");
				level.los = args[8].trim().equalsIgnoreCase("true");
				level.emptyCell = args[9].trim().equalsIgnoreCase("true");
				level.adjustableRange = args[10].trim().equalsIgnoreCase("true");
				level.maxPerTurn = Byte.parseByte(args[12].trim());
				level.maxPerPlayer = Byte.parseByte(args[13].trim());
				level.turns = Byte.parseByte(args[14].trim());
				level.rangeType = args[15].trim();
				level.endsTurnOnFailure = args[19].trim().equalsIgnoreCase("true");
				
				loadEffects(args[0], level.effects);
				loadEffects(args[1], level.criticalEffects);
				
				spell.levels[i - 1] = level;
			}
			
			this.spells.put((int)spell.id, spell);
		}
		
		out.outputSpells(spells.values(), directory + "spells");
	}

	private void loadBreeds(ResultSet result) throws Exception {		
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

			breeds.put(breed.id, breed);
		}
	}

	private void loadSpellBreeds(ResultSet r) throws Exception {
		while (r.next()) {
			/*** INPUT : MySQL ***/
			
			int breed_id = r.getInt("breed");
			Structs.Breed breed = breeds.get(breed_id);
			if (breed == null) {
				App.log("Impossible de trouver la race N°%d", breed_id);
				continue;
			}
			
			Structs.SpellBreed s = new Structs.SpellBreed();
			s.template = spells.get(r.getInt("spell"));
			s.level = r.getInt("level");
			s.position = r.getInt("default_position");
			
			/*** OUTPUT ***/
			
			breed.spells.add(s);
		}
	}
	
	private void createExperiences(ResultSet result, String directory, DataOutputter out) throws Exception {
		List<Structs.Experience> exps = Lists.newArrayList();
		
		while (result.next()) {
			/*** INPUT : MySQL ***/
			
			Structs.Experience exp = new Structs.Experience();
			exp.level = result.getInt("level");
			exp.player = result.getLong("character");
            exp.guild = exp.player * 10;
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
