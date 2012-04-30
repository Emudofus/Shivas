package org.shivas.data.usertype;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

public abstract class ImmutableUserType implements UserType {

    @Override
    public boolean isMutable() {
        return false;
    }
 
    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
    	if (x == null || y == null) return false;
        if (x == y) return true;
        if (x.getClass() != y.getClass()) return false;
        return x.equals(y);
    }
 
    @Override
    public int hashCode(Object x) throws HibernateException {
        assert (x != null);
        return x.hashCode();
    }
 
    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }
 
    @Override
    public Object replace(Object original, Object target, Object owner)
            throws HibernateException {
        return original;
    }
 
    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }
 
    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

}
