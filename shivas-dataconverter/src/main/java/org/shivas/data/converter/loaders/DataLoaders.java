package org.shivas.data.converter.loaders;

import org.shivas.data.converter.App;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 13/10/12
 * Time: 14:42
 */
public final class DataLoaders {
    private DataLoaders() {}

    public static DataLoader combine(DataLoader first, DataLoader second) {
        return new CombinedLoader(first, second);
    }

    private static DataLoader fromString(String input) {
        if (input.equalsIgnoreCase("Ancestra")) {
            return new AncestraLoader(
                    App.prompt("(Ancestra) Entrez l'adresse du serveur MySQL"),
                    App.prompt("(Ancestra) Entrez le nom d'utilisateur"),
                    App.prompt("(Ancestra) Entrez le mot de passe"),
                    App.prompt("(Ancestra) Entrez le nom de la base de donnée statique")
            );
        } else if (input.equalsIgnoreCase("Vemu")) {
            return new VemuLoader(
                    App.prompt("(Vemu) Entrez l'adresse du serveur MySQL"),
                    App.prompt("(Vemu) Entrez le nom d'utilisateur"),
                    App.prompt("(Vemu) Entrez le mot de passe"),
                    App.prompt("(Vemu) Entrez le nom de la base de donnée statique")
            );
        } else if (input.equalsIgnoreCase("d2j")) {
        }
        return null;
    }

    public static DataLoader prompt() {
        while (true) {
            String input = App.prompt("Choisissez le type de base de donnée :\n\tAncestra\n\tVemu\n\td2j\n" +
                                      "(utilisez & pour faire plusieurs bases de données en même temps, ex: Ancestra & Vemu)\n");

            DataLoader last = null;
            for (String dataLoaderName : input.split("&")) {
                DataLoader loader = fromString(dataLoaderName.trim());
                if (loader == null) continue;

                if (last == null) {
                    last = loader;
                } else {
                    last = combine(last, loader);
                }
            }

            if (last == null) {
                App.outln("Le type de base de donnée %s n'est pas supporté pour l'instant");
            } else {
                return last;
            }
        }
    }
}
