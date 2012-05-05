package org.shivas.data.converter;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class App {
	public static final String VERSION = "1.0ALPHA";
	public static final Scanner in = new Scanner(System.in);
	
	private static final Map<Integer, Converter> CONVERTERS = new HashMap<Integer, Converter>();
	
	public static void log(String pattern, Object... args) {
		System.out.println(String.format(pattern, args));
	}
	
	public static void log() {
		System.out.println();
	}
	
	public static void load() {
		log("Chargement, veuillez patienter ...");
		
		CONVERTERS.put(d2jConverter.ID, new d2jConverter());
		
		log();
	}
	
	public static String prompt(String pattern, Object... args) {
		log(pattern, args);
		return in.nextLine();
	}
	
	public static void welcome() {
    	log("               Bienvenue dans Shivas-Dataconverter V%s !", VERSION);
    	log("Ce programme a été créé par Blackrush et vous aidera a convertir");
    	log("votre base de donnée Ancestra ou Vemu pour Shivas.");
    	log();
    	log("Veuillez choisir l'un de ces convertisseurs :");
    	for (Map.Entry<Integer, Converter> entry : CONVERTERS.entrySet()) {
    		log("  %d. %s", entry.getKey(), entry.getValue().getClass().getSimpleName());
    	}
	}
	
	public static Converter choice() {
		Converter converter = null;
		while (converter == null) {
			int choice = in.nextInt();
			converter = CONVERTERS.get(choice);
			
			if (converter == null) {
				log("Choix impossible, veuillez recommencer");
			}
		}
		in.nextLine();
		return converter;
	}
	
    public static void main(String[] args) {
    	load();
    	
    	welcome();
    	
    	Converter converter = choice();
    	
    	converter.start();
    }
}
