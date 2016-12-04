package org.atomium.util.query.mysql;

import org.atomium.util.Entity;
import org.atomium.util.query.Op;
import org.atomium.util.query.Order;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class MySqlOp {
	
	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss.S";
	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern(DATE_TIME_PATTERN);

	public static String print(Op op) {
		switch (op) {
		case DIF:
			return " != ";
		case EQ:
			return " = ";
		case INF:
			return " < ";
		case INF_EQ:
			return " <= ";
		case LIKE:
			return " LIKE ";
		case SUP:
			return " > ";
		case SUP_EQ:
			return " >= ";
		}
		return null;
	}
	
	public static String print(Order order) {
		switch (order){
		case ASC:
			return "ASC";
		case DESC:
			return "DESC";
		}
		return null;
	}
	
	public static String toString(Object obj) {
		if (obj == null) {
			return "null";
		}
		else if (obj instanceof Boolean) {
			return ((Boolean)obj) ? "1" : "0";
		}
		else if (obj instanceof Enum<?>) {
			return Integer.toString(((Enum<?>)obj).ordinal());
		}
		else if (obj instanceof Entity<?>) {
			return ((Entity<?>)obj).getId().toString();
		}
		else if (obj instanceof ReadableInstant) {
			return DATE_TIME_FORMATTER.print((ReadableInstant) obj);
		}
		return obj.toString();
	}
	
}
