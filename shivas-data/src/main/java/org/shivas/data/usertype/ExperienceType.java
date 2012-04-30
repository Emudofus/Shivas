package org.shivas.data.usertype;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.ShortType;
import org.shivas.data.Containers;
import org.shivas.data.entity.Experience;

public class ExperienceType extends ImmutableUserType {

	@Override
	public int[] sqlTypes() {
		return new int[] { ShortType.INSTANCE.sqlType() };
	}

	@Override
	public Class<Experience> returnedClass() {
		return Experience.class;
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
		assert names.length == 1;
		
		Short level = (Short) ShortType.INSTANCE.get(rs, names[0], session);
		
		return Containers.instance().get(Experience.class).byId(level);
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
		assert value instanceof Experience;
		
		ShortType.INSTANCE.set(st, ((Experience)value).getLevel(), index, session);
	}

}
