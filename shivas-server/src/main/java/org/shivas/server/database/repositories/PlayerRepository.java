package org.shivas.server.database.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.atomium.EntityManager;
import org.atomium.repository.EntityRepository;
import org.atomium.repository.impl.AbstractEntityRepository;
import org.atomium.util.pk.IntegerPrimaryKeyGenerator;
import org.atomium.util.query.DeleteQueryBuilder;
import org.atomium.util.query.InsertQueryBuilder;
import org.atomium.util.query.Op;
import org.atomium.util.query.Query;
import org.atomium.util.query.SelectQueryBuilder;
import org.atomium.util.query.UpdateQueryBuilder;
import org.shivas.common.statistics.CharacteristicType;
import org.shivas.data.Container;
import org.shivas.data.entity.Breed;
import org.shivas.data.entity.Experience;
import org.shivas.protocol.client.enums.Gender;
import org.shivas.protocol.client.enums.OrientationEnum;
import org.shivas.server.config.Config;
import org.shivas.server.core.Colors;
import org.shivas.server.core.Location;
import org.shivas.server.core.Look;
import org.shivas.server.core.experience.PlayerExperience;
import org.shivas.server.core.maps.GameMap;
import org.shivas.server.core.statistics.PlayerStatistics;
import org.shivas.server.database.models.Account;
import org.shivas.server.database.models.Player;

@Singleton
public class PlayerRepository extends AbstractEntityRepository<Integer, Player> {
	
	public static final String TABLE_NAME = "players";
	
	private final Config config;
	private final Container ctner;
	private final EntityRepository<Integer, Account> accounts;
	
	private DeleteQueryBuilder deleteQuery;
	private InsertQueryBuilder persistQuery;
	private UpdateQueryBuilder saveQuery;
	private SelectQueryBuilder loadQuery;

	@Inject
	public PlayerRepository(EntityManager em, Config config, Container ctner, EntityRepository<Integer, Account> accounts) {
		super(em, new IntegerPrimaryKeyGenerator());
		this.config = config;
		this.ctner = ctner;
		this.accounts = accounts;
		
		this.deleteQuery = em.builder().delete(TABLE_NAME).where("id", Op.EQ);
		this.persistQuery = em.builder()
				.insert(TABLE_NAME)
				.values("id", "owner_id", "name", "breed_id", "gender",
						"skin", "size", "color1", "color2", "color3",
						"level", "experience",
						"map_id", "cell", "orientation",
						"stat_points", "spell_points", "energy", "life", "action_points", "movement_points",
						"vitality", "wisdom", "strength", "intelligence", "chance", "agility");
		this.saveQuery = em.builder()
				.update(TABLE_NAME)
				.value("gender").value("skin").value("size").value("color1").value("color2").value("color3")
				.value("level").value("experience").value("map_id").value("cell").value("orientation")
				.where("id", Op.EQ);
		this.loadQuery = em.builder().select(TABLE_NAME);
	}

	public Player createDefault(Account owner, String name, int breed, Gender gender, int color1, int color2, int color3) {
		Player player = new Player(
				owner.toReference(),
				name,
				ctner.get(Breed.class).byId(breed),
				gender,
				new Look(
						(short) (breed * 10 + gender.ordinal()),
						(short) 100,
						new Colors(color1, color2, color3)
				),
				new PlayerExperience(ctner.get(Experience.class).byId(config.startLevel())),
				new Location(config.startMap(), config.startCell(), OrientationEnum.SOUTH_EAST)
		);
		player.setStats(new PlayerStatistics(
				player,
				config.startActionPoints() != null ? config.startActionPoints() : player.getBreed().getStartActionPoints(),
				config.startMovementPoints() != null ? config.startMovementPoints() : player.getBreed().getStartMovementPoints(),
				config.startVitality(),
				config.startWisdom(),
				config.startStrength(),
				config.startIntelligence(),
				config.startChance(),
				config.startAgility()
		));
		
		persist(player);
		owner.getPlayers().put(player.id(), player);
		
		return player;
	}
	
	public boolean nameExists(String name) {
		for (Player player : entities.values()) {
			if (player.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public Player find(String name){
		for (Player player : entities.values()) {
			if (player.getName().equals(name)) {
				return player;
			}
		}
		return null;
	}
	
	public Player findByIdOrName(String raw){
		try {
			int playerId = Integer.parseInt(raw);
			return find(playerId);
		} catch (NumberFormatException e) {
			return find(raw);
		}
	}

	@Override
	protected Query buildDeleteQuery(Player entity) {
		Query query = deleteQuery.toQuery();
		query.setParameter("id", entity.id());
		
		return query;
	}

	@Override
	protected Query buildPersistQuery(Player entity) {
		Query query = persistQuery.toQuery();
		query.setParameter("id", entity.id());
		query.setParameter("owner_id", entity.getOwner());
		query.setParameter("name", entity.getName());
		query.setParameter("breed_id", entity.getBreed().getId());
		query.setParameter("gender", entity.getGender());
		query.setParameter("skin", entity.getLook().getSkin());
		query.setParameter("size", entity.getLook().getSize());
		query.setParameter("color1", entity.getLook().getColors().first());
		query.setParameter("color2", entity.getLook().getColors().second());
		query.setParameter("color3", entity.getLook().getColors().third());
		query.setParameter("level", entity.getExperience().level());
		query.setParameter("experience", entity.getExperience().current());
		query.setParameter("map_id", entity.getLocation().getMap().getId());
		query.setParameter("cell", entity.getLocation().getCell());
		query.setParameter("orientation", entity.getLocation().getOrientation());
		query.setParameter("stat_points", entity.getStats().statPoints());
		query.setParameter("spell_points", entity.getStats().spellPoints());
		query.setParameter("energy", entity.getStats().energy().current());
		query.setParameter("life", entity.getStats().life().current());
		query.setParameter("action_points", entity.getStats().get(CharacteristicType.ActionPoints).base());
		query.setParameter("movement_points", entity.getStats().get(CharacteristicType.MovementPoints).base());
		query.setParameter("vitality", entity.getStats().get(CharacteristicType.Vitality).base());
		query.setParameter("wisdom", entity.getStats().get(CharacteristicType.Wisdom).base());
		query.setParameter("strength", entity.getStats().get(CharacteristicType.Strength).base());
		query.setParameter("intelligence", entity.getStats().get(CharacteristicType.Intelligence).base());
		query.setParameter("chance", entity.getStats().get(CharacteristicType.Chance).base());
		query.setParameter("agility", entity.getStats().get(CharacteristicType.Agility).base());
		
		return query;
	}

	@Override
	protected Query buildSaveQuery(Player entity) {
		Query query = saveQuery.toQuery();
		query.setParameter("id", entity.id());
		query.setParameter("gender", entity.getGender());
		query.setParameter("skin", entity.getLook().getSkin());
		query.setParameter("size", entity.getLook().getSize());
		query.setParameter("color1", entity.getLook().getColors().first());
		query.setParameter("color2", entity.getLook().getColors().second());
		query.setParameter("color3", entity.getLook().getColors().third());
		query.setParameter("level", entity.getExperience().level());
		query.setParameter("experience", entity.getExperience().current());
		query.setParameter("map_id", entity.getLocation().getMap().getId());
		query.setParameter("cell", entity.getLocation().getCell());
		query.setParameter("orientation", entity.getLocation().getOrientation());
		query.setParameter("stat_points", entity.getStats().statPoints());
		query.setParameter("spell_points", entity.getStats().spellPoints());
		query.setParameter("energy", entity.getStats().energy().current());
		query.setParameter("life", entity.getStats().life().current());
		query.setParameter("action_points", entity.getStats().get(CharacteristicType.ActionPoints).base());
		query.setParameter("movement_points", entity.getStats().get(CharacteristicType.MovementPoints).base());
		query.setParameter("vitality", entity.getStats().get(CharacteristicType.Vitality).base());
		query.setParameter("wisdom", entity.getStats().get(CharacteristicType.Wisdom).base());
		query.setParameter("strength", entity.getStats().get(CharacteristicType.Strength).base());
		query.setParameter("intelligence", entity.getStats().get(CharacteristicType.Intelligence).base());
		query.setParameter("chance", entity.getStats().get(CharacteristicType.Chance).base());
		query.setParameter("agility", entity.getStats().get(CharacteristicType.Agility).base());
		
		return query;
	}

	@Override
	protected Query buildLoadQuery() {
		return loadQuery.toQuery();
	}

	@Override
	protected Player load(ResultSet result) throws SQLException {		
		Player player = new Player(
				result.getInt("id"),
				accounts.getLazyReference(result.getInt("owner_id")),
				result.getString("name"),
				ctner.get(Breed.class).byId(result.getInt("breed_id")),
				Gender.valueOf(result.getInt("gender")),
				new Look(
						result.getShort("skin"),
						result.getShort("size"),
						new Colors(
								result.getInt("color1"),
								result.getInt("color2"),
								result.getInt("color3")
						)
				),
				new PlayerExperience(
						ctner.get(Experience.class).byId(result.getInt("level")),
						result.getLong("experience")
				),
				new Location(
						ctner.get(GameMap.class).byId(result.getInt("map_id")), 
						result.getShort("cell"),
						OrientationEnum.valueOf(result.getInt("orientation"))
				)
		);
		
		player.setStats(new PlayerStatistics(
				player,
				result.getShort("stat_points"),
				result.getShort("spell_points"),
				result.getInt("energy"),
				result.getInt("life"),
				result.getShort("action_points"),
				result.getShort("movement_points"),
				result.getShort("vitality"),
				result.getShort("wisdom"),
				result.getShort("strength"),
				result.getShort("intelligence"),
				result.getShort("chance"),
				result.getShort("agility")
		));
		
		return player;
	}

	@Override
	protected void unhandledException(Exception e) {
	}
	
}
