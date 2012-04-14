package org.shivas.login.database.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;

import org.shivas.common.Account;
import org.shivas.login.database.models.GameServer;
import org.shivas.protocol.client.types.GameServerType;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Singleton
public class GameServerRepository {
	
	private Map<Integer, GameServer> entities = Maps.newHashMap();
	
	@Inject
	public GameServerRepository(EntityManager em) {		
		for (GameServer server : em.createQuery("SELECT gs FROM GameServer gs", GameServer.class).getResultList()){
			entities.put(server.getId(), server);
		}
	}
	
	public GameServer findById(int id) {
		return entities.get(id);
	}
	
	public GameServer findByName(String name) {
		for (GameServer server : entities.values()) {
			if (server.getName().equals(name)) {
				return server;
			}
		}
		return null;
	}
	
	public Collection<GameServer> findAll() {
		return entities.values();
	}
	
	public Collection<GameServerType> findAllToGameServerType(Account account) {
		List<GameServerType> result = Lists.newArrayListWithCapacity(entities.size());
		for (GameServer server : entities.values()) {
			result.add(server.toGameServerType(account));
		}
		return result;
	}
	
}
