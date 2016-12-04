package org.shivas.host;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.atomium.EntityManager;
import org.shivas.core.database.RepositoryContainer;
import org.shivas.core.services.game.GameService;
import org.shivas.core.services.login.LoginService;
import org.shivas.core.utils.ContainerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    private EntityManager em;
    private RepositoryContainer repos;
    private GameService gs;
    private LoginService ls;
    private ContainerProvider cp;

    public App(Module... modules) {
        Injector inject = Guice.createInjector(modules);
        em = inject.getInstance(EntityManager.class);
        repos = inject.getInstance(RepositoryContainer.class);
        gs = inject.getInstance(GameService.class);
        ls = inject.getInstance(LoginService.class);
        cp = inject.getInstance(ContainerProvider.class);
    }

    public void start() {
        cp.load(); // load static data

        em.start(); // start entity manager
        repos.load(); // load users' data

        gs.start(); // start game server
        ls.start(); // start login server

        System.runFinalization();
        System.gc();

        log.info("started");
    }

    public void stop() {
        ls.stop(); // stop login server
        gs.stop(); // stop game server

        repos.close(); // release users' data
        em.stop(); // stop entity manager

        log.info("stopped");
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        log.info("Birth of a new World!");
        final App server = new App(new HostModule());

        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
        server.start();
    }

}
