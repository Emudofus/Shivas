package org.shivas.data.converter;

import org.shivas.data.converter.loaders.DataLoader;
import org.shivas.data.converter.loaders.DataLoaders;
import org.shivas.data.converter.outputters.DataOutputter;
import org.shivas.data.converter.outputters.DataOutputters;
import org.shivas.data.entity.*;

import java.util.Map;
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
                Map<Integer, Breed> breeds = loader.loadBreeds();
                outputter.outputBreeds(breeds.values());

                outln("%d classes ont été écrites", breeds.size());
            } catch (Exception e) {
                outln(e);
            }
        }

        if (confirmation("Souhaitez-vous écrire les maps ?")) {
            try {
                Map<Integer, MapData> maps = loader.loadMaps();
                outputter.outputMaps(maps.values());

                outln("%d maps ont été écrites", maps.size());

                if (confirmation("Souhaitez-vous écrire les zaaps ?")) {
                    Map<Integer, Waypoint> waypoints = loader.loadWaypoints();
                    outputter.outputWaypoints(waypoints.values());

                    outln("%d zaaps ont été écris", waypoints.size());
                }
            } catch (Exception e) {
                outln(e);
            }
        }

        if (confirmation("Souhaitez-vous écrire les seuils d'expérience ?")) {
            try {
                Map<Short, Experience> experiences = loader.loadExperiences();
                outputter.outputExperiences(experiences.values());

                outln("%d seuils d'expérience ont été écris", experiences.size());
            } catch (Exception e) {
                outln(e);
            }
        }

        if (confirmation("Souhaitez-vous écrire les objets ?")) {
            try {
                Map<Short, ItemSet> itemSets = loader.loadItemSets();
                Map<Short, ItemTemplate> itemTemplates = loader.loadItems();

                outputter.outputItemSets(itemSets.values());
                outputter.outputItems(itemTemplates.values());

                outln("%d objets et %d panoplies ont été écris", itemTemplates.size(), itemSets.size());
            } catch (Exception e) {
                outln(e);
            }
        }

        if (confirmation("Souhaitez-vous écrire les sorts ?")) {
            try {
                Map<Short, SpellTemplate> spells = loader.loadSpells();
                outputter.outputSpells(spells.values());

                outln("%d sorts ont été écris", spells.size());
            } catch (Exception e) {
                outln(e);
            }
        }

        if (confirmation("Souhaitez-vous écrire les PNJ ?")) {
            try {
                Map<Integer, NpcTemplate> npcs = loader.loadNpcTemplates();
                outputter.outputNpcTemplates(npcs.values());

                outln("%d pnj ont été écris", npcs.size());
            } catch (Exception e) {
                outln(e);
            }
        }

        outln("Vous pouvez désormais démarrer Shivas");
    }
}
