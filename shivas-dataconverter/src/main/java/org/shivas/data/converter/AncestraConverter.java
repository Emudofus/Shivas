package org.shivas.data.converter;

import com.google.common.base.Function;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import org.atomium.util.Action1;
import org.atomium.util.query.Order;
import org.shivas.common.collections.CollectionQuery;
import org.shivas.common.maths.Point;
import org.shivas.common.random.Dofus1Dice;
import org.shivas.protocol.client.enums.Gender;
import org.shivas.protocol.client.enums.ItemEffectEnum;
import org.shivas.protocol.client.enums.ItemTypeEnum;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AncestraConverter extends MySqlUserConverter {
	
	private final Map<Integer, Structs.GameMap> maps = Maps.newHashMap();
	private final Multimap<Integer, Structs.ItemTemplate> items = ArrayListMultimap.create();
	
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
		App.log("Vous avez choisit le convertisseur pour base de donnée Ancestra.");
		
		init();
		
		//////////// EXPERIENCES /////////////////
		
		if (canWrite("Souhaitez-vous écrire les niveaux d'expérience ?")) {
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
		}

		//////////// ITEMS /////////////////

		if (canWrite("Souhaitez-vous écrire les panoplies et objets ?")) {
			App.log("Les objets vont être chargés puis écris, cela peut prendre quelques secondes.");
			super.query(q.select("item_template").orderBy("id", Order.ASC).toQuery(), new Action1<ResultSet>() {
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
			
			App.log("Les panoplies vont être chargées puis écrites, cela peut prendre quelques secondes.");
			super.query(q.select("itemsets").orderBy("id", Order.ASC).toQuery(), new Action1<ResultSet>() {
				public Void invoke(ResultSet arg1) throws Exception {
					createItemSets(
							arg1,
							App.prompt("Veuillez entrer le répertoire où seront stockées les panoplies"),
							out
					);
					return null;
				}
			});
			App.log("Tous les panoplies ont été écrites.");
		}

        ////////////// NPCTEMPLATES //////////////

        if (canWrite("Souhaitez-vous écrire les NPC templates ?")) {
            App.log("Les NPC templates vont être chargés puis écris, cela peut prendre quelques secondes.");
            super.query(q.select("npc_template").toQuery(), new Action1<ResultSet>() {
                public Void invoke(ResultSet rset) throws Exception {
                    createNpcTemplates(
                            rset,
                            App.prompt("Veuillez entrer le répertoire où seront stockés les NPC templates."),
                            out
                    );
                    return null;
                }
            });
            App.log("Tous les NPC templates ont été écris.");
        }

        items.clear(); // NPC loading needs items

		//////////// MAPS /////////////////
		

		if (canWrite("Souhaitez-vous écrire les cartes et triggers ?")) {
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
				e.printStackTrace();
				System.exit(1); // FATAL ERROR
			}
			
			maps.clear();
		}
		
		////////////// WAYPOINTS //////////////
		
		if (canWrite("Souhaitez-vous écrire les zaaps ?")) {
			App.log("Les zaaps vont être chargés puis écris, cela peut prendre quelques secondes.");
			super.query(q.select("zaaps").toQuery(), new Action1<ResultSet>() {
				public Void invoke(ResultSet arg1) throws Exception {
					createWaypoints(
							arg1,
							App.prompt("Veuillez entrer le répertoire où seront stockées les zaaps"),
							out
					);
					return null;
				}
			});
			App.log("Tous les zaaps ont été écris.");
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
            exp.guild = exp.player * 10;
			exp.job = results.getInt("metier");
			exp.mount = results.getInt("dinde");
			exp.alignment = results.getShort("pvp");
			
			/*** OUTPUT ***/
			exps.add(exp);
		}
		
		out.outputExperiences(exps, directory + "experiences");
	}
	
	public static Point parsePosition(String string) {
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
	
	private void createItems(ResultSet r, String directory, DataOutputter out) throws SQLException, IOException {		
		while (r.next()) {
			ItemTypeEnum type = ItemTypeEnum.valueOf(r.getInt("type"));
			
			Structs.ItemTemplate item;
			
			if (type.isWeapon()) {
				String weaponInfos;
				if ((weaponInfos = r.getString("armesInfos")) == null || !weaponInfos.isEmpty()) continue;
				
				Structs.WeaponItemTemplate weapon = new Structs.WeaponItemTemplate();
				weapon.ethereal = false;
				weapon.twoHands = weaponInfos.split("\\|")[6].equals("1");
				
				item = weapon;
			} else if (type.isUsable()) {
				item = new Structs.ItemTemplate(); // TODO usable items
			} else {
				item = new Structs.ItemTemplate();
			}
			
			item.id = r.getInt("id");
			item.type = type;
			item.level = r.getShort("level");
			
			for (String e : r.getString("statsTemplate").split(",")) {
				if (e.isEmpty()) continue;
				String[] args = e.split("#");
				
				Structs.ItemEffectTemplate effect = new Structs.ItemEffectTemplate();
				effect.effect = ItemEffectEnum.valueOf(Integer.parseInt(args[0], 16));
				try {
					effect.bonus = Dofus1Dice.parseDice(args[4]);
				} catch (IndexOutOfBoundsException ex) {
					effect.bonus = new Dofus1Dice();
				}
				
				item.effects.add(effect);
			}
			
			item.weight = r.getShort("pod");
			item.price = r.getLong("prix");
			item.conditions = r.getString("condition");
			item.forgemageable = true; // there is not column "forgemageable"
			
			items.put(r.getInt("panoplie"), item);
		}
		
		out.outputItems(items.values(), directory + "items");
	}
	
	private void createItemSets(ResultSet r, String directory, DataOutputter out) throws SQLException, IOException {
		List<Structs.ItemSet> itemsets = Lists.newArrayList();
		
		while (r.next()) {
			Structs.ItemSet itemset = new Structs.ItemSet();
			
			itemset.id = r.getInt("id");
			
			int level = 2;
			for (String e : r.getString("bonus").split(";")) {
				if (e.isEmpty()) continue;
				String[] args = e.split(",");
				
				for (String ee : args) {
					String[] args2 = ee.split(":");
					
					Structs.ItemEffect effect = new Structs.ItemEffect();
					effect.effect = ItemEffectEnum.valueOf(Integer.parseInt(args2[0]));
					effect.bonus = Integer.parseInt(args2[1].replace("%", ""));
					
					itemset.effects.put(level, effect);
				}
				
				++level;
			}
			
			itemset.items.addAll(items.get(itemset.id));
			
			itemsets.add(itemset);
		}
		
		out.outputItemSets(itemsets, directory + "itemsets");
	}
	
	private void createWaypoints(ResultSet rset, String directory, DataOutputter out) throws Exception {
		List<Structs.Waypoint> waypoints = Lists.newArrayList();
		
		int id = 0;
		while (rset.next()) {
			Structs.Waypoint waypoint = new Structs.Waypoint();
			waypoint.id = ++id;
			waypoint.mapId = rset.getInt("mapID");
			waypoint.cell = rset.getShort("cellID");
			
			waypoints.add(waypoint);
		}
		
		out.outputWaypoints(waypoints, directory + "waypoints");
	}

    private void createNpcTemplates(ResultSet rset, String dir, DataOutputter out) throws Exception {
        List<Structs.NpcTemplate> npcTemplates = Lists.newArrayList();
        Map<Integer, Structs.ItemTemplate> items = CollectionQuery.from(this.items.values()).computeMap(new Function<Structs.ItemTemplate, Integer>() {
            public Integer apply(Structs.ItemTemplate itemTemplate) {
                return itemTemplate.id;
            }
        });

        while (rset.next()) {
            Structs.NpcTemplate npcTemplate = new Structs.NpcTemplate();
            npcTemplate.id = rset.getInt("id");
            npcTemplate.gender = Gender.valueOf(rset.getInt("sex"));
            npcTemplate.skin = rset.getShort("skin");
            npcTemplate.size = rset.getShort("scaleX");
            npcTemplate.color1 = rset.getInt("color1");
            npcTemplate.color2 = rset.getInt("color2");
            npcTemplate.color3 = rset.getInt("color3");

            String[] accessoriesStr = rset.getString("accessories").split(",");
            Structs.ItemTemplate[] accessories = new Structs.ItemTemplate[5];
            for (int i = 0; i < accessoriesStr.length; ++i) {
                int itemId = Integer.parseInt(accessoriesStr[i], 16);
                if (itemId == 0) continue;

                Structs.ItemTemplate item = items.get(itemId);
                if (item == null) {
                    App.log("Item N°%d inconnu !", itemId);
                }
                else {
                    accessories[i] = item;
                }
            }
            npcTemplate.accessories = accessories;

            npcTemplate.extraClip = rset.getInt("extraClip");
            npcTemplate.customArtwork = rset.getInt("customArtWork");

            npcTemplates.add(npcTemplate);
        }

        out.outputNpcTemplates(npcTemplates, dir + "npcTemplates");
    }

}
