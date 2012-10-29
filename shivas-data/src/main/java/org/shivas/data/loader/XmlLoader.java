package org.shivas.data.loader;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;
import org.shivas.common.StringUtils;
import org.shivas.common.maths.Point;
import org.shivas.common.maths.Range;
import org.shivas.common.random.Dofus1Dice;
import org.shivas.common.statistics.CharacteristicType;
import org.shivas.data.EntityFactory;
import org.shivas.data.entity.*;
import org.shivas.data.entity.factory.ActionFactory;
import org.shivas.data.repository.BaseRepository;
import org.shivas.protocol.client.enums.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;

public class XmlLoader extends AbstractLoader {

    private final Logger log = LoggerFactory.getLogger(XmlLoader.class);
	
	private final SAXBuilder builder = new SAXBuilder();
	private final ActionFactory actionFactory;
	
	public XmlLoader(EntityFactory factory) {
		super(factory);
		actionFactory = factory.newActionFactory(ctner);
		
		loaders.put(Breed.class, new FileLoader<Breed>() {
			public int load(BaseRepository<Breed> repo, File file) throws Exception {
				return loadBreed(repo, file);
			}
		});
		
		loaders.put(Experience.class, new FileLoader<Experience>() {
			public int load(BaseRepository<Experience> repo, File file) throws Exception {
				return loadExperience(repo, file);
			}
		});
		
		loaders.put(MapTemplate.class, new FileLoader<MapTemplate>() {
			public int load(BaseRepository<MapTemplate> repo, File file) throws Exception {
				return loadMap(repo, file);
			}
		});
		
		loaders.put(SpellTemplate.class, new FileLoader<SpellTemplate>() {
			public int load(BaseRepository<SpellTemplate> repo, File file) throws Exception {
				return loadSpellTemplate(repo, file);
			}
		});
		
		loaders.put(ItemSet.class, new FileLoader<ItemSet>() {
			public int load(BaseRepository<ItemSet> repo, File file) throws Exception {
				return loadItemSet(repo, file);
			}
		});
		
		loaders.put(ItemTemplate.class, new FileLoader<ItemTemplate>() {
			public int load(BaseRepository<ItemTemplate> repo, File file) throws Exception {
				return loadItemTemplate(repo, file);
			}
		});
		
		loaders.put(Action.class, new FileLoader<Action>() {
			public int load(BaseRepository<Action> repo, File file) throws Exception {
				return loadItemAction(repo, file);
			}
		});
		
		loaders.put(Waypoint.class, new FileLoader<Waypoint>() {
			public int load(BaseRepository<Waypoint> repo, File file) throws Exception {
				return loadZaap(repo, file);
			}
		});

        loaders.put(NpcTemplate.class, new FileLoader<NpcTemplate>() {
            public int load(BaseRepository<NpcTemplate> repo, File file) throws Exception {
                return loadNpcTemplates(repo, file);
            }
        });

        loaders.put(Npc.class, new FileLoader<Npc>() {
            public int load(BaseRepository<Npc> repo, File file) throws Exception {
                return loadNpcs(repo, file);
            }
        });
	}

	@Override
	public String getFileExtension() {
		return "xml";
	}

	private int loadBreed(BaseRepository<Breed> repo, File file) throws Exception {
		int count = 0;
		
		Document doc = builder.build(file);
		Element root = doc.getDescendants(new ElementFilter("breeds")).next();
		for (Element element : root.getChildren("breed")) {
			Breed breed = factory.newBreed();
			
			breed.setId(element.getAttribute("id").getIntValue());
			breed.setStartActionPoints((short) element.getAttribute("startActionPoints").getIntValue());
			breed.setStartMovementPoints((short) element.getAttribute("startMovementPoints").getIntValue());
			breed.setStartLife((short) element.getAttribute("startLife").getIntValue());
			breed.setStartProspection((short) element.getAttribute("startProspection").getIntValue());
			
			Map<CharacteristicType, Map<Range, Breed.Level>> levels = Maps.newHashMap();
			for (Element child : element.getChildren("levels")) {
				Map<Range, Breed.Level> level = Maps.newHashMap();
				
				CharacteristicType type = CharacteristicType.valueOf(child.getAttributeValue("type"));
				for (Element child2 : child.getChildren("level")) {
					Range range = Range.parseRange(child2.getAttributeValue("range"));
					int bonus = child2.getAttribute("bonus").getIntValue(),
						cost  = child2.getAttribute("cost").getIntValue();
					
					level.put(range, new Breed.Level(cost, bonus));
				}
				
				levels.put(type, level);
			}
			breed.setLevels(levels);
			
			Map<Short, SpellBreed> spells = Maps.newHashMap();
			for (Element spell_elem : element.getChild("spells").getChildren()) {
				SpellBreed spell = factory.newSpellBreed();
				spell.setBreed(breed);
				spell.setTemplate(ctner.get(SpellTemplate.class).byId(spell_elem.getAttribute("id").getIntValue()));
				spell.setMinLevel((short) spell_elem.getAttribute("minLevel").getIntValue());
				spell.setPosition((byte) spell_elem.getAttribute("startPosition").getIntValue());
				
				spells.put(spell.getTemplate().getId(), spell);
			}
			breed.setSpells(spells);
			
			repo.put(breed.getId(), breed);
			++count;
		}
		
		return count;
	}
	
	private int loadExperience(BaseRepository<Experience> repo, File file) throws Exception {
		int count = 0;
		
		Document doc = builder.build(file);
		Element root = doc.getDescendants(new ElementFilter("experiences")).next();
		for (Element element : root.getChildren("experience")) {
			Experience experience = factory.newExperience();
			experience.setLevel((short) element.getAttribute("level").getIntValue());
			experience.setPlayer(element.getAttribute("player").getLongValue());
            experience.setGuild(element.getAttribute("guild").getLongValue());
			experience.setJob(element.getAttribute("job").getIntValue());
			experience.setMount(element.getAttribute("mount").getIntValue());
			experience.setAlignment((short) element.getAttribute("alignment").getIntValue());
			
			repo.put(experience.getLevel(), experience);
			++count;
		}

		for (int i = 2; i <= repo.count(); ++i) {
			Experience previous = repo.byId(i - 1);
			Experience current = repo.byId(i);
			
			previous.setNext(current);
			current.setPrevious(previous);
		}
		
		return count;
	}
	
	private int loadMap(BaseRepository<MapTemplate> repo, File file) throws Exception {
		int count = 0;
		Document doc = builder.build(file);
		
		Element root = doc.getDescendants(new ElementFilter("maps")).next();
		for (Element element : root.getChildren("map")) {
			MapTemplate map = factory.newMapTemplate();
			map.setId(element.getAttribute("id").getIntValue());
			map.setPosition(new Point(
					element.getAttribute("abscissa").getIntValue(),
					element.getAttribute("ordinate").getIntValue()
			));
			map.setWidth(element.getAttribute("width").getIntValue());
			map.setHeight(element.getAttribute("height").getIntValue());
			map.getCells().set(CellLoader.parse(element.getChild("data").getAttributeValue("value"), factory));
			map.setDate(element.getAttribute("date").getValue());
			map.setKey(element.getChild("key").getAttributeValue("value"));
			map.setSubscriber(element.getAttributeValue("subscriber").equals("1"));

            for (Element startCells_elem : element.getChildren("startCells")) {
                FightSideEnum side = FightSideEnum.valueOf(startCells_elem.getAttribute("side").getIntValue());
                String places = startCells_elem.getAttributeValue("cells");

                for (int i = 0; i < places.length(); i += 2) {
                    short cellId = (short) ((StringUtils.EXTENDED_ALPHABET.indexOf(places.charAt(i)) << 6) +
                                            StringUtils.EXTENDED_ALPHABET.indexOf(places.charAt(i + 1)));

                    GameCell cell = map.getCell(cellId);
                    if (cell != null) {
                        cell.setStartFightSide(side);
                    }
                }

                map.getCells().setEncodedStartCells(side, places);
            }
			
			repo.put(map.getId(), map);
			++count;
		}
		
		for (Element element : root.getChildren("map")) {
			MapTemplate map = repo.byId(element.getAttribute("id").getIntValue());
			
			Map<Short, MapTrigger> triggers = Maps.newHashMap();
			for (Element trigger_elem : element.getChildren("trigger")) {
				MapTrigger trigger = factory.newMapTrigger();
				trigger.setMap(map);
				trigger.setCell((short) trigger_elem.getAttribute("cell").getIntValue());
				if (!trigger_elem.getAttribute("next_map").getValue().isEmpty()) {
					trigger.setNextMap(repo.byId(trigger_elem.getAttribute("next_map").getIntValue()));
				}
				trigger.setNextCell((short) trigger_elem.getAttribute("next_cell").getIntValue());
				
				triggers.put(trigger.getCell(), trigger);
			}
			map.setTrigger(triggers);
		}
		
		return count;
	}
	
	private int loadItemSet(BaseRepository<ItemSet> repo, File file) throws Exception {
		int count = 0;
		Document doc = builder.build(file);
		
		Element root = doc.getDescendants(new ElementFilter("itemsets")).next();
		
		for (Element elem : root.getChildren("itemset")) {
			ItemSet itemset = factory.newItemSet();
			itemset.setId((short) elem.getAttribute("id").getIntValue());
			
			List<ItemTemplate> items = Lists.newArrayList();
			for (Element item_elem : elem.getChildren("item")) {
				int itemid = item_elem.getAttribute("id").getIntValue();
				
				ItemTemplate item = get(ItemTemplate.class).byId(itemid);
				if (item != null) {
					item.setItemSet(itemset);
					items.add(item);
				}
			}
			itemset.setItems(items);
			
			Multimap<Integer, ConstantItemEffect> effects = ArrayListMultimap.create();
			
			for (Element effects_elem : elem.getChildren("effects")) {
				int level = effects_elem.getAttribute("level").getIntValue();
				
				for (Element effect_elem : effects_elem.getChildren("effect")) {					
					ConstantItemEffect effect = factory.newConstantItemEffect();
					effect.setType(ItemEffectEnum.valueOf(effect_elem.getAttribute("type").getIntValue()));
					effect.setBonus((short) effect_elem.getAttribute("bonus").getIntValue());
					
					effects.put(level, effect);
				}
			}
			
			itemset.setEffects(effects);
			
			repo.put(itemset.getId(), itemset);
			++count;
		}
		
		return count;
	}
	
	private Action makeAction(Element elem) throws Exception {
		String type = elem.getAttributeValue("type");
		
		Map<String, String> parameters = Maps.newHashMap();
		for (Attribute attr : elem.getAttributes()) {
			if (attr.getName().equalsIgnoreCase("type")) continue;
			
			parameters.put(attr.getName(), attr.getValue());
		}
		
		return actionFactory.make(type, parameters);
	}
	
	private ItemTemplate makeItemTemplate(Element elem, ItemTypeEnum type) throws Exception {
		if (type.isWeapon()) {
			WeaponTemplate weapon = factory.newWeaponTemplate();

            weapon.setCost((short) elem.getAttribute("cost").getIntValue());
            weapon.setMinRange((short) elem.getAttribute("minRange").getIntValue());
            weapon.setMaxRange((short) elem.getAttribute("maxRange").getIntValue());
            weapon.setCriticalRate((short) elem.getAttribute("criticalRate").getIntValue());
            weapon.setFailureRate((short) elem.getAttribute("failureRate").getIntValue());
            weapon.setCriticalBonus((short) elem.getAttribute("criticalBonus").getIntValue());
            weapon.setTwoHands(elem.getAttribute("twoHands").getBooleanValue());
            weapon.setEthereal(elem.getAttribute("ethereal").getBooleanValue());
			
			return weapon;
		} else if (type.isUsable()) {
			return factory.newUsableItemTemplate();
		} else {
			return factory.newItemTemplate();
		}
	}
	
	private int loadItemTemplate(BaseRepository<ItemTemplate> repo, File file) throws Exception {
		int count = 0;
		Document doc = builder.build(file);
		
		Element root = doc.getDescendants(new ElementFilter("items")).next();
		for (Element item_elem : root.getChildren("item")) {
			ItemTypeEnum type = ItemTypeEnum.valueOf(item_elem.getAttribute("type").getIntValue());
			ItemTemplate item = makeItemTemplate(item_elem, type);
			
			item.setId((short) item_elem.getAttribute("id").getIntValue());			
			item.setType(type);
			item.setLevel((short) item_elem.getAttribute("level").getIntValue());
			item.setWeight((short) item_elem.getAttribute("weight").getIntValue());
			item.setForgemageable(item_elem.getAttribute("forgemageable").getBooleanValue());
			item.setPrice((short) item_elem.getAttribute("price").getIntValue());
			item.setConditions(item_elem.getChildText("conditions"));
			
			List<ItemEffectTemplate> effects = Lists.newArrayList();
			for (Element effect_elem : item_elem.getChildren("effect")) {
				ItemEffectTemplate effect = factory.newItemEffectTemplate();
				
				effect.setEffect(ItemEffectEnum.valueOf(effect_elem.getAttribute("type").getIntValue()));
				effect.setBonus(Dofus1Dice.parseDice(effect_elem.getAttributeValue("bonus")));
				
				effects.add(effect);
			}
			item.setEffects(effects);
			
			repo.put(item.getId(), item);
			++count;
		}
		
		return count;
	}
	
	private int loadItemAction(BaseRepository<Action> repo, File file) throws Exception {
		int count = 0;
		Document doc = builder.build(file);
		
		for (Element actions_elem : doc.getDescendants(new ElementFilter("actions"))) {
            int tplId = actions_elem.getAttribute("item").getIntValue();
            ItemTemplate tpl = get(ItemTemplate.class).byId(tplId);
            if (tpl == null) continue;
            if (!(tpl instanceof UsableItemTemplate)) throw new Exception("you can't add action on non-usable item");

            UsableItemTemplate usable = (UsableItemTemplate) tpl;
            if (usable.getActions() != null) throw new Exception("you can't add anymore actions on this item");

            List<Action> actions = Lists.newArrayList();
            for (Element action_elem : actions_elem.getChildren("action")) {
                Action action = makeAction(action_elem);
                if (action != null) {
                    actions.add(action);
                }
            }
            usable.setActions(actions);
			
			++count;
		}
		
		return count;
	}
	
	private int loadSpellTemplate(BaseRepository<SpellTemplate> repo, File file) throws Exception {
		int count = 0;
		Document doc = builder.build(file);
		
		Element root = doc.getDescendants(new ElementFilter("spells")).next();
		for (Element spell_elem : root.getChildren("spell")) {
			Element sprite_elem = spell_elem.getChild("sprite");
			
			SpellTemplate spell = factory.newSpellTemplate();
			spell.setId((short) spell_elem.getAttribute("id").getIntValue());
			spell.setSprite((short) sprite_elem.getAttribute("id").getIntValue());
			spell.setSpriteInfos(sprite_elem.getAttributeValue("infos"));
			
			SpellLevel[] levels = new SpellLevel[SpellTemplate.MAX_LEVELS];
			for (Element level_elem : spell_elem.getChildren("level")) {
				SpellLevel level = factory.newSpellLevel();
				level.setId((byte) level_elem.getAttribute("id").getIntValue());
				level.setSpell(spell);
				level.setMinRange((byte) level_elem.getAttribute("minRange").getIntValue());
				level.setMaxRange((byte) level_elem.getAttribute("maxRange").getIntValue());
				level.setCriticalRate((short) level_elem.getAttribute("criticalRate").getIntValue());
				level.setFailureRate((short) level_elem.getAttribute("failureRate").getIntValue());
				level.setInline(level_elem.getAttribute("inline").getBooleanValue());
				level.setLos(level_elem.getAttribute("los").getBooleanValue());
				level.setEmptyCell(level_elem.getAttribute("emptyCell").getBooleanValue());
				level.setAdjustableRange(level_elem.getAttribute("adjustableRange").getBooleanValue());
				level.setEndsTurnOnFailure(level_elem.getAttribute("endsTurnOnFailure").getBooleanValue());
				level.setMaxPerTurn((byte) level_elem.getAttribute("maxPerTurn").getIntValue());
				level.setMaxPerPlayer((byte) level_elem.getAttribute("maxPerPlayer").getIntValue());
				level.setTurns((byte) level_elem.getAttribute("turns").getIntValue());
				level.setRangeType(level_elem.getAttributeValue("rangeType"));
				
				List<SpellEffect> effects = Lists.newArrayList(), criticalEffects = Lists.newArrayList();
				for (Element effect_elem : level_elem.getChildren("effect")) {
                    SpellEffectTypeEnum type = SpellEffectTypeEnum.valueOf(effect_elem.getAttribute("type").getIntValue());

					SpellEffect effect = factory.newSpellEffect(level, type);
					effect.setLevel(level);
					effect.setType(type);
					effect.setFirst((short) effect_elem.getAttribute("first").getIntValue());
					effect.setSecond((short) effect_elem.getAttribute("second").getIntValue());
					effect.setThird((short) effect_elem.getAttribute("third").getIntValue());
					
					if (effect_elem.getAttribute("turns") != null)
						effect.setTurns((short) effect_elem.getAttribute("turns").getIntValue());
					
					if (effect_elem.getAttribute("chance") != null)
						effect.setChance((short) effect_elem.getAttribute("chance").getIntValue());
					
					if (effect_elem.getAttribute("dice") != null)
						effect.setDice(Dofus1Dice.parseDice(effect_elem.getAttributeValue("dice")));
					else
						effect.setDice(Dofus1Dice.ZERO);
					
					if (effect_elem.getAttribute("target") != null)
						effect.setTarget(effect_elem.getAttributeValue("target"));
					
					if (effect_elem.getAttribute("critical") != null) {
						criticalEffects.add(effect);
					} else {
						effects.add(effect);
					}
				}
				level.setEffects(effects);
				level.setCriticalEffects(criticalEffects);
				
				levels[level.getId() - 1] = level;
			}
			spell.setLevels(levels);
			
			repo.put(spell.getId(), spell);
			++count;
		}
		
		return count;
	}

	private int loadZaap(BaseRepository<Waypoint> repo, File file) throws Exception {
		int count = 0;
		Document doc = builder.build(file);
		
		Element root = doc.getDescendants(new ElementFilter("waypoints")).next();
		for (Element waypoint_elem : root.getChildren("waypoint")) {
			Waypoint waypoint = factory.newWaypoint();
			
			int id = waypoint_elem.getAttribute("id").getIntValue();
			int mapId = waypoint_elem.getAttribute("map").getIntValue();
			short cell = (short) waypoint_elem.getAttribute("cell").getIntValue();
			
			waypoint.setId(id);
			waypoint.setMap(ctner.get(MapTemplate.class).byId(mapId));
			waypoint.setCell(cell);
			
			if (waypoint.getMap() == null) {
				throw new Exception("unknown map " + mapId);
			}
			if (waypoint.getMap().getWaypoint() != null) {
				throw new Exception("map " + mapId + " has already a waypoint (id=" + waypoint.getMap().getWaypoint().getId() + ")");
			}
			waypoint.getMap().setWaypoint(waypoint);
			
			repo.put(waypoint.getId(), waypoint);
			++count;
		}
		
		return count;
	}

    private int loadNpcTemplates(BaseRepository<NpcTemplate> repo, File file) throws Exception {
        int count = 0;
        Document doc = builder.build(file);

        Element root = doc.getDescendants(new ElementFilter("npcTemplates")).next();
        for (Element npcTemplate_elem : root.getChildren("npcTemplate")) {
            NpcTemplate npcTemplate = factory.newNpcTemplate();
            npcTemplate.setId(npcTemplate_elem.getAttribute("id").getIntValue());
            npcTemplate.setType(NpcTypeEnum.valueOf(npcTemplate_elem.getAttributeValue("type")));
            npcTemplate.setGender(Gender.valueOf(npcTemplate_elem.getAttributeValue("gender")));
            npcTemplate.setSkin((short) npcTemplate_elem.getAttribute("skin").getIntValue());
            npcTemplate.setSize((short) npcTemplate_elem.getAttribute("size").getIntValue());
            npcTemplate.setExtraClip(npcTemplate_elem.getAttribute("extraClip").getIntValue());
            npcTemplate.setCustomArtwork(npcTemplate_elem.getAttribute("customArtwork").getIntValue());

            Element colors_elem = npcTemplate_elem.getChild("colors");
            npcTemplate.setColor1(colors_elem.getAttribute("first").getIntValue());
            npcTemplate.setColor2(colors_elem.getAttribute("second").getIntValue());
            npcTemplate.setColor3(colors_elem.getAttribute("third").getIntValue());

            Element accessories_elem = npcTemplate_elem.getChild("accessories");
            ItemTemplate[] accessories = new ItemTemplate[5];
            if (accessories_elem.getAttribute("weapon") != null) {
                accessories[0] = ctner.get(ItemTemplate.class).byId(accessories_elem.getAttribute("weapon").getIntValue());
            }
            if (accessories_elem.getAttribute("hat") != null) {
                accessories[1] = ctner.get(ItemTemplate.class).byId(accessories_elem.getAttribute("hat").getIntValue());
            }
            if (accessories_elem.getAttribute("cloak") != null) {
                accessories[2] = ctner.get(ItemTemplate.class).byId(accessories_elem.getAttribute("cloak").getIntValue());
            }
            if (accessories_elem.getAttribute("pet") != null) {
                accessories[3] = ctner.get(ItemTemplate.class).byId(accessories_elem.getAttribute("pet").getIntValue());
            }
            if (accessories_elem.getAttribute("shield") != null) {
                accessories[4] = ctner.get(ItemTemplate.class).byId(accessories_elem.getAttribute("shield").getIntValue());
            }
            npcTemplate.setAccessories(accessories);

            repo.put(npcTemplate.getId(), npcTemplate);
            ++count;
        }

        return count;
    }

    private void loadNpcQuestions(Npc npc, Element elem) throws Exception {
        Map<Integer, NpcQuestion> questions = Maps.newHashMap();
        for (Element question_elem : elem.getChildren("question")) {
            NpcQuestion question = factory.newNpcQuestion();
            question.setId(question_elem.getAttribute("id").getIntValue());

            Map<Integer, NpcAnswer> answers = Maps.newHashMap();
            for (Element answer_elem : question_elem.getChildren("answer")) {
                NpcAnswer answer = factory.newNpcAnswer();
                answer.setId(answer_elem.getAttribute("id").getIntValue());

                List<Action> actions = Lists.newArrayList();
                for (Element action_elem : answer_elem.getChildren("action")) {
                    Action action = makeAction(action_elem);
                    actions.add(action);
                }
                answer.setActions(actions);

                answers.put(answer.getId(), answer);
            }
            question.setAnswers(answers);

            questions.put(question.getId(), question);
        }
        npc.setQuestions(questions);

        int startQuestionId = elem.getAttribute("start").getIntValue();
        NpcQuestion startQuestion = questions.get(startQuestionId);
        if (startQuestion == null) {
            throw new Exception("unknown question " + startQuestionId);
        }
        npc.setStartQuestion(startQuestion);
    }

    private void loadNpcSales(Npc npc, Element elem) throws Exception {
        Map<Integer, NpcSale> sales = Maps.newHashMap();
        for (Element sale_elem : elem.getChildren("sale")) {
            NpcSale sale = factory.newNpcSale();
            sale.setNpc(npc);
            sale.setItem(get(ItemTemplate.class).byId(sale_elem.getAttribute("item").getIntValue()));
            if (sale_elem.getAttribute("price") != null) {
                sale.setPrice((short) sale_elem.getAttribute("price").getIntValue());
            } else {
                sale.setPrice(sale.getItem().getPrice());
            }

            sales.put((int) sale.getItem().getId(), sale);
        }
        npc.setSales(sales);
    }

    private int loadNpcs(BaseRepository<Npc> repo, File file) throws Exception {
        int count = 0;
        Document doc = builder.build(file);

        Element root = doc.getDescendants(new ElementFilter("npcs")).next();
        for (Element npc_elem : root.getChildren("npc")) {
            Npc npc = factory.newNpc();
            npc.setId(npc_elem.getAttribute("id").getIntValue());
            npc.setTemplate(ctner.get(NpcTemplate.class).byId(npc_elem.getAttribute("template").getIntValue()));
            npc.setMap(ctner.get(MapTemplate.class).byId(npc_elem.getAttribute("map").getIntValue()));
            npc.setCell((short) npc_elem.getAttribute("cell").getIntValue());
            npc.setOrientation(OrientationEnum.valueOf(npc_elem.getAttributeValue("orientation")));

            if (npc_elem.getAttribute("start") != null) {
                loadNpcQuestions(npc, npc_elem);
            }

            switch (npc.getTemplate().getType()) {
            case BUY_SELL:
            case SELL:
                loadNpcSales(npc, npc_elem);
                break;

            default:
                if (npc_elem.getChildren("sale").size() > 0) {
                    log.warn("NPC (id={}) has 2 sales but type is neither SELL nor BUY_SELL");
                }
                break;
            }

            repo.put(npc.getId(), npc);
            ++count;
        }

        return count;
    }

}
