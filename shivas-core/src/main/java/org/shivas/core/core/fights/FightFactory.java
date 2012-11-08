package org.shivas.core.core.fights;

import org.shivas.common.threads.Timer;
import org.shivas.core.config.ConfigProvider;
import org.shivas.core.core.GameActor;
import org.shivas.core.core.maps.GameMap;
import org.shivas.core.database.models.Player;
import org.shivas.protocol.client.enums.FightTypeEnum;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 28/10/12
 * Time: 19:05
 */
@Singleton
public class FightFactory {
    private ConfigProvider config;
    private ExecutorService worker;

    private final Timer<Fight> timer = new Timer<Fight>("fight");
    private final AtomicInteger id = new AtomicInteger();

    @Inject
    public void init(ConfigProvider config) {
        this.config = config;
        this.worker = Executors.newSingleThreadExecutor(); // TODO bug: event dispatcher with thread pool (n>1)
    }

    protected DuelFight newDuel(GameMap map, Player source, Player target) {
        PlayerFighter challenger = new PlayerFighter(source),
                      defender = new PlayerFighter(target);

        source.setFighter(challenger);
        target.setFighter(defender);

        return new DuelFight(
                id.incrementAndGet(),
                config, timer, worker,
                map,
                challenger,
                defender
        );
    }

    public Fight build(FightTypeEnum type, GameMap map, GameActor challenger, GameActor defender) {
        if (type == FightTypeEnum.DUEL) {
            if (challenger instanceof Player && defender instanceof Player) {
                return newDuel(map, (Player) challenger, (Player) defender);
            } else {
                throw new IllegalArgumentException("challenger and defender must be both players");
            }
        }

        throw new NotImplementedException();
    }
}
