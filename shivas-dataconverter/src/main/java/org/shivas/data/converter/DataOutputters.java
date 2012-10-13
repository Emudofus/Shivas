package org.shivas.data.converter;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 13/10/12
 * Time: 14:51
 */
public final class DataOutputters {
    private DataOutputters() {}

    public static DataOutputter prompt() {
        while (true) {
            String input = App.prompt("Choisissez le type de base de donnée :\n\tXML\n\tYAML\n\tbin\n");
            if (input.equalsIgnoreCase("XML")) {
                return new XMLDataOutputter(
                        App.prompt("Entrez le nom de répertoire où seront écris les données")
                );
            } else if (input.equalsIgnoreCase("YAML")) {
            } else if (input.equalsIgnoreCase("bin")) {
            }

            App.outln("Le format de sortie %s n'est pas supporté", input);
        }
    }
}
