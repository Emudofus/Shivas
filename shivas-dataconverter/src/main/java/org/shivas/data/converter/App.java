package org.shivas.data.converter;

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

    public static String readln() {
        return scan.nextLine();
    }

    public static String prompt(String format, Object... args) {
        out(format, args);
        return readln();
    }

    public static boolean confirmation(String format, Object... args) {
        return prompt(format, args).equalsIgnoreCase(CONFIRMATION_STR);
    }

    public static void main(String[] args) {
    }
}
