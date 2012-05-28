package org.shivas.data.converter;

import java.util.List;
import java.util.Map;

import org.shivas.common.maths.Point;
import org.shivas.common.maths.Range;
import org.shivas.common.random.Dice;
import org.shivas.common.statistics.CharacteristicType;
import org.shivas.protocol.client.enums.ItemEffectEnum;
import org.shivas.protocol.client.enums.ItemTypeEnum;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

public final class Structs {
	private Structs(){}
	
	public static final class BreedLevel {
		public int cost, bonus;
	}
	
	public static final class Breed {
		public int id;
		public String name;
		public short startLife, startProspection, startActionPoints, startMovementPoints;
		public Map<CharacteristicType, Map<Range, BreedLevel>> levels = Maps.newHashMap();
	}
	
	public static final class Experience {
		public int level;
		public long player;
		public int job;
		public int mount;
		public short alignment;
	}
	
	public static final class GameMap {
		public int id;
		public Point position = new Point();
		public int width, height;
		public String data;
		public String date;
		public String key;
		public boolean subscriber;
		public List<GameMapTrigger> triggers = Lists.newArrayList();
	}
	
	public static final class GameMapTrigger {
		public int id;
		public GameMap nextMap;
		public short cell, nextCell;
	}
	
	public static final class ItemSet {
		public int id;
		public Multimap<Integer, ItemEffect> effects = ArrayListMultimap.create();
		public List<ItemTemplate> items = Lists.newArrayList();
	}
	
	public static class ItemTemplate {
		public int id;
		public ItemTypeEnum type;
		public short level;
		public short weight;
		public boolean forgemageable;
		public long price;
		public String conditions;
		public List<ItemEffectTemplate> effects = Lists.newArrayList();
	}
	
	public static final class WeaponItemTemplate extends ItemTemplate {
		public boolean twoHands;
		public boolean ethereal;
	}
	
	public static final class ItemEffect {
		public ItemEffectEnum effect;
		public int bonus;
	}
	
	public static final class ItemEffectTemplate {
		public ItemEffectEnum effect;
		public Dice bonus;
	}
}
