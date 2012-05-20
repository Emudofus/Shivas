package org.shivas.data.converter;

import java.util.Map;
import java.util.Scanner;

import com.google.common.collect.Maps;

public class App {
	public static final String VERSION = "2.0ALPHA";
	public static final Scanner in = new Scanner(System.in);
	
	private static final Map<Integer, Converter> CONVERTERS = Maps.newHashMap();
	private static final Map<String, DataOutputter> OUTPUTTERS = Maps.newHashMap();
	
	public static void log(String pattern, Object... args) {
		System.out.println(String.format(pattern, args));
	}
	
	public static void log() {
		System.out.println();
	}
	
	public static String prompt(String pattern, Object... args) {
		log(pattern, args);
		return in.nextLine();
	}
	
	public static void load() {
		log("Chargement, veuillez patienter ...");
		
		CONVERTERS.put(Converters.D2J_ID, new d2jConverter());
		CONVERTERS.put(Converters.ANCESTRA_ID, new AncestraConverter());
		
		OUTPUTTERS.put("XML", new XMLDataOutputter());
		
		log();
	}
	
	public static void welcome() {
    	log("               Bienvenue dans Shivas-Dataconverter V%s !", VERSION);
    	log("Ce programme a été créé par Blackrush et vous aidera a convertir");
    	log("votre base de donnée Ancestra ou Vemu pour Shivas.");
	}
	
	public static Converter converterChoice() {
		log();
    	log("Veuillez choisir l'un de ces convertisseurs :");
    	for (Map.Entry<Integer, Converter> entry : CONVERTERS.entrySet()) {
    		log("   %d. %s", entry.getKey(), entry.getValue().getClass().getSimpleName());
    	}
    	
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
	
	public static DataOutputter outputterChoice() {
		log();
    	log("Veuillez choisir l'un de ces formats de sortie :");
    	for (String format : OUTPUTTERS.keySet()) {
    		log("   %s", format);
    	}
    	
		DataOutputter out = null;
		while (out == null) {
			String choice = in.nextLine();
			out = OUTPUTTERS.get(choice);
			
			if (out == null) {
				log("Choix impossible, veuillez recommencer");
			}
		}
		in.nextLine();
		return out;
	}
	
    public static void main(String[] args) {
    	load();
    	
    	welcome();
    	
    	Converter converter = converterChoice();
    	DataOutputter out = outputterChoice();
    	
    	converter.start(out);
    }
}
