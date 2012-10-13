package org.shivas.data.converter;

import org.shivas.data.converter.loaders.DataLoaders;
import org.shivas.data.entity.Breed;

import java.util.Collection;
import java.util.Scanner;

public class App {

    private static Scanner scan = new Scanner(System.in);
    private static String CONFIRMATION_STR = "oui";

    public static void out(String format, Object... args) {
        System.out.format(format, args);
    }

    public static void outln(String format, Object... args) {
        System.out.format(format + "\n", args);
    }

    public static void outln(Exception e) {
        e.printStackTrace();
    }

    public static String readln() {
        return scan.nextLine();
    }

    public static String prompt(String format, Object... args) {
        out(format + " => ", args);
        return readln();
    }

    public static boolean confirmation(String format, Object... args) {
        return prompt(format + " (oui/non)", args).equalsIgnoreCase(CONFIRMATION_STR);
    }

    public static void main(String[] args) {
        DataLoader loader = DataLoaders.prompt();
        DataOutputter outputter = DataOutputters.prompt();

        if (confirmation("Souhaitez-vous écrire les classes ?")) {
            try {
                Collection<Breed> breeds = loader.loadBreeds();
                outputter.outputBreeds(breeds);

                outln("%d classes ont été écrites", breeds.size());
            } catch (Exception e) {
                outln(e);
            }
        }

        if (confirmation("Souhaitez-vous écrire les maps ?")) {
            try {
                Collection<MapData> maps = loader.loadMaps();
                outputter.outputMaps(maps);

                outln("%d maps ont été écrites", maps.size());
            } catch (Exception e) {
                outln(e);
            }
        }

        outln("Vous pouvez désormais démarrer Shivas");
    }
}
