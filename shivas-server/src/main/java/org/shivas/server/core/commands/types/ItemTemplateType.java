package org.shivas.server.core.commands.types;

import org.shivas.common.params.ParsingException;
import org.shivas.common.params.Type;
import org.shivas.data.Containers;
import org.shivas.data.entity.ItemTemplate;

public class ItemTemplateType implements Type {
	
	public static final ItemTemplateType INSTANCE = new ItemTemplateType();
	
	public static final ItemTemplate DEFAULT_VALUE = null;

	@Override
	public Class<?> getJavaClass() {
		return ItemTemplate.class;
	}

	@Override
	public Object getDefaultValue() {
		return DEFAULT_VALUE;
	}

	@Override
	public Object parse(String string) throws ParsingException {
		try {
			int itemId = Integer.parseInt(string);
			ItemTemplate item = Containers.instance().get(ItemTemplate.class).byId(itemId);
			if (item == null) throw new ParsingException("unknown ItemTemplate (id=" + itemId + ")");
			return item;
		} catch (NumberFormatException e) {
			throw new ParsingException("invalid ItemTemplate id : " + string);
		}
	}

}
