package org.shivas.data.usertype;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.IntegerType;
import org.shivas.data.Containers;
import org.shivas.data.entity.Breed;

public class BreedType extends ImmutableUserType {

	@Override
	public int[] sqlTypes() {
		return new int[] { IntegerType.INSTANCE.sqlType() };
	}

	@Override
	public Class<Breed> returnedClass() {
		return Breed.class;
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
		assert names.length == 1;
		
		Integer id = (Integer) IntegerType.INSTANCE.get(rs, names[0], session);
		
		return Containers.instance().get(Breed.class).byId(id);
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
		assert value instanceof Breed;
		
		IntegerType.INSTANCE.set(st, ((Breed)value).getId(), index, session);
	}
	
}
