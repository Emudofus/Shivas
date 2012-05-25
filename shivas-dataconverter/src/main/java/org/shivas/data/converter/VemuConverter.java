package org.shivas.data.converter;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.atomium.util.Action1;
import org.atomium.util.query.Order;
import org.shivas.common.random.Dofus1Dice;
import org.shivas.protocol.client.enums.ItemEffectEnum;
import org.shivas.protocol.client.enums.ItemTypeEnum;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class VemuConverter extends MySqlUserConverter {
	
	private final Map<Integer, Structs.GameMap> maps = Maps.newHashMap();
	private final Map<Integer, Structs.ItemSet> itemsets = Maps.newHashMap();

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
		App.log("Vous avez choisis le convertisseur de base de donnée Vemu.");
		
		init();
		
		//////////// EXPERIENCES /////////////////

		if (canWrite("Souhaitez-vous écrire les niveaux d'expérience ?")) {
			App.log("Les niveaux d'expérience vont être chargés puis écris, cela peut prendre quelques secondes.");
			super.query(q.select("exp_data").toQuery(), new Action1<ResultSet>() {
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
		}

		//////////// ITEMS /////////////////

		if (canWrite("Souhaitez-vous écrire les panoplies et les objets ?")) {
			App.log("Les panoplies vont être chargés puis écris, cela peut prendre quelques secondes.");
			super.query(q.select("items_pano").orderBy("id", Order.ASC).toQuery(), new Action1<ResultSet>() {
				public Void invoke(ResultSet arg1) throws Exception {
					createItemSets(
							arg1,
							App.prompt("Veuillez entrer le répertoire où seront stockés les panoplies"),
							out
					);
					return null;
				}
			});
			App.log("Toutes les panoplies ont été écris.");
			
			App.log("Les objets vont être chargés puis écris, cela peut prendre quelques secondes.");
			super.query(q.select("items_data").orderBy("ID", Order.ASC).toQuery(), new Action1<ResultSet>() {
				public Void invoke(ResultSet arg1) throws Exception {
					createItems(
							arg1,
							App.prompt("Veuillez entrer le répertoire où seront stockés les objets"),
							out
					);
					return null;
				}
			});
			App.log("Tous les objets ont été écris.");
		}

		//////////// MAPS /////////////////

		if (canWrite("Souhaitez-vous écrire les cartes et les triggers ?")) {
			App.log("Les cartes vont être chargées, cela peut prendre quelques minutes.");
			super.query(q.select("maps_data").toQuery(), new Action1<ResultSet>(){
				public Void invoke(ResultSet arg1) throws Exception {
					loadMaps(arg1);
					return null;
				}
			});
			App.log("%d cartes ont été chargées.", maps.size());
			
			App.log("Les triggers de cartes vont être chargés, cela peut prendre quelques minutes.");
			super.query(q.select("maps_triggers").toQuery(), new Action1<ResultSet>() {
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
		}
		
		App.log("Tout s'est bien passé, vous pouvez à présent lancer l'émulateur");
	}
	
	private void createExperiences(ResultSet r, String directory, DataOutputter out) throws SQLException, IOException {
		List<Structs.Experience> exps = Lists.newArrayList();
		
		while (r.next()) {
			/*** INPUT : MySQL ***/
			Structs.Experience exp = new Structs.Experience();
			exp.level = r.getInt("Level");
			exp.player = r.getLong("Character");
			exp.job = r.getInt("Job");
			exp.mount = r.getInt("mount");
			exp.alignment = r.getShort("Pvp");
			
			/*** OUTPUT ***/
			exps.add(exp);
		}
		
		out.outputExperiences(exps, directory + "experiences");
	}
	
	private void loadMaps(ResultSet r) throws SQLException {
		while (r.next()) {
			Structs.GameMap map = new Structs.GameMap();
			map.id = r.getInt("ID");
			map.position = AncestraConverter.parsePosition(r.getString("pos"));
			map.width = r.getInt("Width");
			map.height = r.getInt("Height");
			map.data = r.getString("MapData");
			map.date = r.getString("CreateTime");
			map.key = r.getString("DecryptKey");
			map.subscriber = r.getBoolean("NeedRegister");
			
			maps.put(map.id, map);
		}
	}
	
	private void loadTriggers(ResultSet r) throws SQLException {
		int nextId = 0;
		
		while (r.next()) {
			Structs.GameMap map = maps.get(r.getInt("MapID"));
			if (map == null) {
				App.log("Un trigger fait référence à une carte inconnue (%d), il est par conséquent ignoré.", r.getInt("MapID"));
				continue;
			}
			
			Structs.GameMapTrigger trigger = new Structs.GameMapTrigger();
			trigger.id = ++nextId;
			trigger.cell = r.getShort("CellID");
			
			String action = r.getString("Action");
			
			int actionType = Integer.parseInt(action.substring(0, action.indexOf('/')));
			if (actionType != 1) continue; // not a trigger
			
			String[] args = action.substring(action.indexOf('/') + 1).split(";");
			if (args.length == 2) {
				trigger.nextMap = maps.get(Integer.parseInt(args[0]));
				trigger.nextCell = Short.parseShort(args[1]);
			}
			
			map.triggers.add(trigger);
		}
	}
	
	private void createItemSets(ResultSet r, String directory, DataOutputter out) throws SQLException, IOException {
		while (r.next()) {
			Structs.ItemSet itemset = new Structs.ItemSet();
			
			itemset.id = r.getInt("id");
			
			for (int level = 2; level <= 8; ++level) {
				String effects = r.getString("effects" + level);
				if (effects.isEmpty()) continue;
				for (String effect : effects.split(";")) {
					String[] args = effect.split(",");
					
					Structs.ItemEffect e = new Structs.ItemEffect();
					e.effect = ItemEffectEnum.valueOf(Integer.parseInt(args[0]));
					e.bonus = Integer.parseInt(args[1].trim());
					
					itemset.effects.put(level, e);
				}
			}
			
			itemsets.put(itemset.id, itemset);
		}
		
		out.outputItemSets(itemsets.values(), directory + "itemsets");
	}

	private void createItems(ResultSet r, String directory, DataOutputter out) throws SQLException, IOException {
		List<Structs.ItemTemplate> items = Lists.newArrayList();
		
		while (r.next()) {
			Structs.ItemTemplate item = new Structs.ItemTemplate();
			
			item.id = r.getInt("ID");
			item.type = ItemTypeEnum.valueOf(r.getInt("Type"));
			item.level = r.getShort("Level");
			item.weight = r.getShort("Weight");
			item.forgemageable = r.getBoolean("Forgemageable");
			item.price = r.getLong("Price");
			item.conditions = r.getString("Conditions");
			
			for (String effect : r.getString("Stats").split(",")) {
				if (effect.isEmpty()) continue;
				String[] args = effect.split("#");
				
				Structs.ItemEffectTemplate e = new Structs.ItemEffectTemplate();
				e.effect = ItemEffectEnum.valueOf(Integer.parseInt(args[0], 16));
				try {
					e.bonus = Dofus1Dice.parseDice(args[4].replace("\n", "").replace("\r", ""));
				} catch (IndexOutOfBoundsException ex) {
					e.bonus = new Dofus1Dice();
				}
				
				item.effects.add(e);
			}
			
			items.add(item);
		}
		
		out.outputItems(items, directory + "items");
	}

}
