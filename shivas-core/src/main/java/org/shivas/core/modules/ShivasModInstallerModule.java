package org.shivas.core.modules;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.List;
import java.util.jar.JarFile;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 31/10/12
 * Time: 20:10
 */
public class ShivasModInstallerModule extends AbstractModule {
    private static final Logger log = LoggerFactory.getLogger(ShivasModInstallerModule.class);

    public static final FilenameFilter FILENAME_FILTER = new FilenameFilter() {
        public boolean accept(File dir, String name) {
            return name.startsWith("shivas-") && name.endsWith(".jar");
        }
    };

    private final String path;

    public ShivasModInstallerModule(String path) {
        this.path = path;
    }

    private void getNamesAndUrls(File directory, List<String> names, List<URL> urls) {
        for (File file : directory.listFiles(FILENAME_FILTER)) {
            try {
                JarFile jarfile = new JarFile(file);

                String moduleName = jarfile.getManifest().getMainAttributes().getValue("Module-Class");
                URL moduleUrl = file.toURI().toURL();

                names.add(moduleName);
                urls.add(moduleUrl);
            } catch (Throwable t) {
                addError(t);
            }
        }
    }

    private Collection<Module> loadModules(URLClassLoader classLoader, Collection<String> names) {
        List<Module> modules = Lists.newArrayList();

        for (String moduleName : names) {
            try {
                Class<?> moduleClass = classLoader.loadClass(moduleName);
                if (Module.class.isAssignableFrom(moduleClass)) {
                    Module module = (Module) moduleClass.newInstance();
                    modules.add(module);
                } else {
                    addError("Class \"%s\" is not a module !", moduleClass.getCanonicalName());
                }
            } catch (Exception e) {
                addError(e);
            }
        }

        return modules;
    }

    private Collection<Module> loadModules(File directory) {
        List<String> names = Lists.newArrayList();
        List<URL> urls = Lists.newArrayList();

        getNamesAndUrls(directory, names, urls);

        URLClassLoader classLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]));

        return loadModules(classLoader, names);
    }

    @Override
    protected void configure() {
        File directory = new File(path);
        if (!directory.exists()) {
            addError("unknown %s directory", path);
        } else {
            for (Module module : loadModules(directory)) {
                install(module);
                log.info("{} loaded", module.getClass().getCanonicalName());
            }
        }
    }
}
