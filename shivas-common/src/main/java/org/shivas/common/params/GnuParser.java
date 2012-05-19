package org.shivas.common.params;

import java.util.Map;

import com.google.common.collect.Maps;

public class GnuParser implements ParametersParser {

	@Override
	public Parameters parse(String string, Conditions conds) throws ParsingException {
		Map<String, Object> values = Maps.newHashMap();
		
		Map<String, Condition> conditions = conds.asMap();
		
		for (String arg : string.split(" --")) if (!arg.isEmpty()) {
			String[] args = arg.split("=");
			
			String name = args[0], valueStr = args[1];
			
			Condition condition = conditions.get(name);
			if (condition == null) continue;
			else conditions.remove(name);
			
			Object value = condition.getType().parse(valueStr);
			
			values.put(name, value);
		}
		
		for (Condition condition : conditions.values()) {
			if (!condition.isOptional()) {
				throw new ParsingException(String.format("\"%s\" is not optional", condition.getName()));
			} else {
				values.put(condition.getName(), condition.getType().getDefaultValue());
			}
		}
		
		return new Parameters(values);
	}

	@Override
	public String buildHelpMessage(Conditions conditions) {
		StringBuilder sb = new StringBuilder();
		
		for (Condition cond : conditions) {
			sb.append("    * --").append(cond.getName()).append(" => ").append(cond.getHelp()).append('\n');
		}
		
		return sb.toString();
	}

}
