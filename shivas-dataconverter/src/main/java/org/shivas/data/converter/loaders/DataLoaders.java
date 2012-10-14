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

    public static DataLoader prompt() {
        while (true) {
            String input = App.prompt("Choisissez le type de base de donnée :\n\tAncestra\n\tVemu\n\td2j\n");
            if (input.equalsIgnoreCase("Ancestra")) {
                return new AncestraLoader(
                        App.prompt("Entrez l'adresse du serveur MySQL"),
                        App.prompt("Entrez le nom d'utilisateur"),
                        App.prompt("Entrez le mot de passe"),
                        App.prompt("Entrez le nom de la base de donnée statique")
                );
            } else if (input.equalsIgnoreCase("Vemu")) {
                return new VemuLoader(
                        App.prompt("Entrez l'adresse du serveur MySQL"),
                        App.prompt("Entrez le nom d'utilisateur"),
                        App.prompt("Entrez le mot de passe"),
                        App.prompt("Entrez le nom de la base de donnée statique")
                );
            } else if (input.equalsIgnoreCase("d2j")) {
            }

            App.outln("Le convertisseur %s n'est pas supporté", input);
        }
    }
}
