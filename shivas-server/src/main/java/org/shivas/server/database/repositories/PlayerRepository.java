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
import org.shivas.data.Container;
import org.shivas.data.entity.Breed;
import org.shivas.data.entity.Experience;
import org.shivas.data.entity.GameMap;
import org.shivas.protocol.client.enums.Gender;
import org.shivas.server.config.Config;
import org.shivas.server.core.Colors;
import org.shivas.server.core.Location;
import org.shivas.server.core.experience.PlayerExperience;
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
				.values("id", "owner_id", "name", "breed_id", "gender", "skin",
						"color1", "color2", "color3", "level", "experience", "map_id", "cell");
		this.saveQuery = em.builder()
				.update(TABLE_NAME)
				.value("gender").value("skin").value("color1").value("color2").value("color3")
				.value("level").value("experience").value("map_id").value("cell")
				.where("id", Op.EQ);
		this.loadQuery = em.builder().select(TABLE_NAME);
	}

	public Player createDefault(Account owner, String name, int breed, Gender gender, int color1, int color2, int color3) {
		Player player = new Player(
				owner.toReference(),
				name,
				ctner.get(Breed.class).byId(breed),
				gender,
				(short) (breed * 10 + gender.ordinal()),
				new Colors(color1, color2, color3),
				new PlayerExperience(ctner.get(Experience.class).byId(config.startLevel())),
				new Location(config.startMap(), config.startCell())
		);
		owner.getPlayers().put(player.id(), player);
		return player;
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
		query.setParameter("owner_id", entity.getOwner().id());
		query.setParameter("name", entity.getName());
		query.setParameter("breed_id", entity.getBreed().getId());
		query.setParameter("gender", entity.getGender().ordinal());
		query.setParameter("skin", entity.getSkin());
		query.setParameter("color1", entity.getColors().first());
		query.setParameter("color2", entity.getColors().second());
		query.setParameter("color3", entity.getColors().third());
		query.setParameter("level", entity.getExperience().level());
		query.setParameter("experience", entity.getExperience().current());
		query.setParameter("map_id", entity.getLocation().getMap().getId());
		query.setParameter("cell", entity.getLocation().getCell());
		
		return query;
	}

	@Override
	protected Query buildSaveQuery(Player entity) {
		Query query = saveQuery.toQuery();
		query.setParameter("id", entity.id());
		query.setParameter("gender", entity.getGender().ordinal());
		query.setParameter("skin", entity.getSkin());
		query.setParameter("color1", entity.getColors().first());
		query.setParameter("color2", entity.getColors().second());
		query.setParameter("color3", entity.getColors().third());
		query.setParameter("level", entity.getExperience().level());
		query.setParameter("experience", entity.getExperience().current());
		query.setParameter("map_id", entity.getLocation().getMap().getId());
		query.setParameter("cell", entity.getLocation().getCell());
		
		return query;
	}

	@Override
	protected Query buildLoadQuery() {
		return loadQuery.toQuery();
	}

	@Override
	protected Player load(ResultSet result) throws SQLException {		
		return new Player(
				result.getInt("id"),
				accounts.getLazyReference(result.getInt("owner_id")),
				result.getString("name"),
				ctner.get(Breed.class).byId(result.getInt("breed_id")),
				Gender.valueOf(result.getInt("gender")),
				result.getShort("skin"),
				new Colors(
						result.getInt("color1"),
						result.getInt("color2"),
						result.getInt("color3")
				),
				new PlayerExperience(
						ctner.get(Experience.class).byId(result.getInt("level")),
						result.getLong("experience")
				), 
				new Location(
						ctner.get(GameMap.class).byId(result.getInt("map_id")), 
						result.getShort("cell")
				)
		);
	}

	@Override
	protected void unhandledException(Exception e) {
	}
	
}
