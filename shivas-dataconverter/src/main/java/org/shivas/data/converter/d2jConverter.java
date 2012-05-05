package org.shivas.data.converter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.atomium.util.Action1;
import org.atomium.util.Function1;
import org.atomium.util.query.Query;
import org.atomium.util.query.QueryBuilderFactory;
import org.atomium.util.query.mysql.MySqlQueryBuilderFactory;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.shivas.common.maths.Point;
import org.shivas.common.maths.Range;
import org.shivas.common.statistics.CharacteristicType;
import org.shivas.data.entity.Breed;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class d2jConverter implements Converter {
	public static final int ID = 1;
	
	private Connection connection;
	private QueryBuilderFactory q;
	private XMLOutputter output;
	
	private void init() {		
		String hostname = "localhost";
		String user 	= "root";
		String password = "";
		String database = "d2j_static";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://" + hostname + ":3306/" + database + "?zeroDateTimeBehavior=convertToNull", user, password);
			q = new MySqlQueryBuilderFactory();
			output = new XMLOutputter(Format.getPrettyFormat());
		} catch (ClassNotFoundException e) {
			App.log("can't load driver because : %s", e.getMessage());
			System.exit(1);
		} catch (SQLException e) {
			App.log("can't open connection because : %s", e.getMessage());
			System.exit(1);
		}
	}
	
	protected <T> T useStatement(Function1<T, Statement> action) {
		Statement statement = null;
		T result = null;
		try {
			statement = connection.createStatement();
			
			result = action.invoke(statement);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	protected void query(final Query query, final Action1<ResultSet> action) {
		useStatement(new Action1<Statement>() {
			public Void invoke(Statement arg1) throws Exception {
				ResultSet result = arg1.executeQuery(query.toString());
				action.invoke(result);
				result.close();
				return null;
			}
		});
	}

	@Override
	public void start() {
		App.log("Vous avez choisis le convertisseur pour base de donnée d2j");
		
		init();

		App.log("Les races vont être chargées puis écrite, cela peut prendre quelques secondes");
		query(q.select("breed_templates").toQuery(), new Action1<ResultSet>() {
			public Void invoke(ResultSet arg1) throws Exception {
				createBreeds(arg1, App.prompt("Veuillez entrer le répertoire où seront stockés les races"));
				return null;
			}
		});
		App.log("Toutes les races ont été stockées");

		App.log("Les niveaux d'expériences vont être chargés puis écrit, cela peut prendre quelques secondes");
		query(q.select("experience_templates").toQuery(), new Action1<ResultSet>() {
			public Void invoke(ResultSet arg1) throws Exception {
				createExperiences(arg1, App.prompt("Veuillez entrer le répertoire où seront stockés les niveaux d'expérience"));
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
			writeMaps(App.prompt("Veuillez entrer le répertoire où seront stockés les cartes"));
			App.log("Toutes les cartes ont été stockées");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		App.log("Tout s'est bien passé, vous pouvez à présent lancer l'émulateur");
	}
	
	private Breed.Level parseLevel(String string) {
		String[] args = string.split("-");
		int bonus = Integer.parseInt(args[0]),
			cost = Integer.parseInt(args[1]);
		return new Breed.Level(cost, bonus);
	}
	
	private Map<Range, Breed.Level> loadLevels(String string) {
		Map<Range, Breed.Level> levels = Maps.newHashMap();
		for (String s : string.split("\\|")) {
			String[] args = s.split(":");
			
			Range range = Range.parseRange(args[0], "\\,");
			Breed.Level level = parseLevel(args[1]);
			
			levels.put(range, level);
		}
		return levels;
	}

	private void createBreeds(ResultSet result, String directory) throws Exception {		
		while (result.next()) {
			/*** INPUT : MySQL ***/
			
			int id = result.getInt("id");
			String name = result.getString("name");
			short startLife = result.getShort("startLife"),
				  startProspection = result.getShort("startProspection"),
				  startActionPoints = result.getShort("startAP"),
				  startMovementPoints = result.getShort("startMP");
			
			Map<CharacteristicType, Map<Range, Breed.Level>> levels = Maps.newHashMap();
			levels.put(CharacteristicType.Vitality, loadLevels(result.getString("vitality")));
			levels.put(CharacteristicType.Wisdom, loadLevels(result.getString("wisdom")));
			levels.put(CharacteristicType.Strength, loadLevels(result.getString("strength")));
			levels.put(CharacteristicType.Intelligence, loadLevels(result.getString("intelligence")));
			levels.put(CharacteristicType.Chance, loadLevels(result.getString("chance")));
			levels.put(CharacteristicType.Agility, loadLevels(result.getString("agility")));
			
			/*** OUTPUT : XML ***/

			Element root_elem = new Element("breeds");
			
			Element breed_elem = new Element("breed");
			breed_elem.setAttribute("id", String.valueOf(id));
			breed_elem.setAttribute("startActionPoints", String.valueOf(startActionPoints));
			breed_elem.setAttribute("startMovementPoints", String.valueOf(startMovementPoints));
			breed_elem.setAttribute("startLife", String.valueOf(startLife));
			breed_elem.setAttribute("startProspection", String.valueOf(startProspection));
			
			for (Map.Entry<CharacteristicType, Map<Range, Breed.Level>> entry1 : levels.entrySet()) {
				Element levels_elem = new Element("levels");
				levels_elem.setAttribute("type", entry1.getKey().name());
				
				for (Map.Entry<Range, Breed.Level> entry2 : entry1.getValue().entrySet()) {
					Element level_elem = new Element("level");
					level_elem.setAttribute("range", entry2.getKey().toString());
					level_elem.setAttribute("bonus", String.valueOf(entry2.getValue().bonus()));
					level_elem.setAttribute("cost", String.valueOf(entry2.getValue().cost()));
					
					levels_elem.addContent(level_elem);
				}
				breed_elem.addContent(levels_elem);
			}
			
			root_elem.addContent(breed_elem);

			output.output(root_elem, new BufferedWriter(new FileWriter(directory + name + ".xml", false)));
		}
	}
	
	private void createExperiences(ResultSet result, String directory) throws Exception {
		Element root_elem = new Element("experiences");
		
		while (result.next()) {
			/*** INPUT : MySQL ***/
			int level = result.getInt("level");
			long player = result.getLong("character");
			int job = result.getInt("job");
			int mount = result.getInt("mount");
			short alignment = result.getShort("alignment");
			
			/*** OUTPUT : XML ***/
			Element exp_elem = new Element("experience");
			exp_elem.setAttribute("level", String.valueOf(level));
			exp_elem.setAttribute("player", String.valueOf(player));
			exp_elem.setAttribute("job", String.valueOf(job));
			exp_elem.setAttribute("mount", String.valueOf(mount));
			exp_elem.setAttribute("alignment", String.valueOf(alignment));
			
			root_elem.addContent(exp_elem);
		}
		
		output.output(root_elem, new BufferedWriter(new FileWriter(directory + "experiences.xml", false)));
	}

	static class GameMap {
		public int id;
		public Point position = new Point();
		public int width, height;
		public String data;
		public String date;
		public String key;
		public boolean subscriber;
		public List<Trigger> triggers = Lists.newArrayList();
	}
	
	static class Trigger {
		public int id;
		public GameMap nextMap;
		public short cell, nextCell;
	}
	
	private void writeMaps(String directory) throws Exception {
		Element root_elem = new Element("maps");
		
		for (GameMap map : maps.values()) {			
			Element map_elem = new Element("map");
			map_elem.setAttribute("id", String.valueOf(map.id));
			map_elem.setAttribute("abscissa", String.valueOf(map.position.abscissa()));
			map_elem.setAttribute("ordinate", String.valueOf(map.position.ordinate()));
			map_elem.setAttribute("width", String.valueOf(map.width));
			map_elem.setAttribute("height", String.valueOf(map.height));
			map_elem.setAttribute("date", String.valueOf(map.date));
			map_elem.setAttribute("subscriber", map.subscriber ? "1" : "0");
			
			Element data_elem = new Element("data");
			data_elem.setAttribute("value", map.data);
			map_elem.addContent(data_elem);
			
			Element key_elem = new Element("key");
			key_elem.setAttribute("value", map.key);
			map_elem.addContent(key_elem);
			
			for (Trigger trigger : map.triggers) {
				Element trigger_elem = new Element("trigger");
				trigger_elem.setAttribute("id", String.valueOf(trigger.id));
				trigger_elem.setAttribute("cell", String.valueOf(trigger.cell));
				trigger_elem.setAttribute("next_map", trigger.nextMap != null ? String.valueOf(trigger.nextMap.id) : "");
				trigger_elem.setAttribute("next_cell", String.valueOf(trigger.nextCell));
				
				map_elem.addContent(trigger_elem);
			}
			
			root_elem.addContent(map_elem);
		}
		
		output.output(root_elem, new BufferedWriter(new FileWriter(directory + "maps.xml", false)));
	}
	
	private Map<Integer, GameMap> maps = Maps.newHashMap();
	
	private void loadMaps(ResultSet result) throws Exception {
		while (result.next()) {
			GameMap map = new GameMap();
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
			Trigger trigger = new Trigger();
			trigger.id = result.getInt("id");
			trigger.nextMap = maps.get(result.getInt("nextMap"));
			trigger.cell = result.getShort("cell");
			trigger.nextCell = result.getShort("nextCell");
			
			GameMap map = maps.get(result.getInt("map"));
			if (map != null) {
				map.triggers.add(trigger);
			}
		}
	}
}
