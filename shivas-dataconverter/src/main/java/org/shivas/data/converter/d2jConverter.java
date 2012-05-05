package org.shivas.data.converter;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.atomium.util.Action1;
import org.atomium.util.Function1;
import org.atomium.util.query.Query;
import org.atomium.util.query.QueryBuilderFactory;
import org.atomium.util.query.mysql.MySqlQueryBuilderFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.shivas.common.maths.Range;
import org.shivas.common.statistics.CharacteristicType;
import org.shivas.data.entity.Breed;

import com.google.common.collect.Maps;

public class d2jConverter implements Converter {
	public static final int ID = 1;
	
	private Connection connection;
	private QueryBuilderFactory q;
	private SAXBuilder builder = new SAXBuilder();
	
	private void init() {		
		String hostname = App.prompt("Veuillez entrer l'adresse du serveur MySQL"),
			   user = App.prompt("Veuillez entrer le nom d'utilisateur"),
			   password = App.prompt("Veuillez entrer le mot de passe"),
			   database = App.prompt("Veuillez entrer le nom de la base de donnée");
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://" + hostname + ":3306/" + database + "?zeroDateTimeBehavior=convertToNull", user, password);
			q = new MySqlQueryBuilderFactory();
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
			App.log("%s : %s", e.getClass().getSimpleName(), e.getMessage());
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					App.log("SQLException : %s", e.getMessage());
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
		App.log("Vous avez choisis le convertisseur pour base de donnée Vemu");
		
		init();
		
		query(q.select("breed_templates").toQuery(), new Action1<ResultSet>() {
			public Void invoke(ResultSet arg1) throws Exception {
				createBreeds(arg1, App.prompt("Veuillez entrer le répertoire où seront stockés les races"));
				return null;
			}
		});
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
			
			Range range = Range.parseRange(args[0], ",");
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
			
			File file = new File(directory + name + ".xml");
			Document doc = builder.build(file);

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
			doc.addContent(root_elem);

			file.createNewFile();
		}
	}
}
