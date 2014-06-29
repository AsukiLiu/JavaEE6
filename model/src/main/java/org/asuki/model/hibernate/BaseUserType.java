package org.asuki.model.hibernate;

import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BaseUserType<T extends Serializable> implements UserType {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    protected abstract Class<T> getClazz();

    @Override
    public int[] sqlTypes() {
        return new int[] { Types.CLOB };
    }

    @Override
    public Class<T> returnedClass() {
        return getClazz();
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == null && y == null) {
            return true;
        }

        if (x == null) {
            return false;
        }

        return x.equals(y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names,
            SessionImplementor session, Object owner)
            throws HibernateException, SQLException {

        if (rs.wasNull()) {
            return null;
        }

        String json = rs.getString(names[0]);
        if (json == null) {
            return null;
        }

        T result = null;
        try {
            result = OBJECT_MAPPER.readValue(json, getClazz());
        } catch (IOException e) {
            throw new HibernateException(e);
        }

        return result;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index,
            SessionImplementor session) throws HibernateException, SQLException {

        if (value == null) {
            st.setNull(index, Types.VARCHAR);
            return;
        }

        String json;
        try {
            json = OBJECT_MAPPER.writeValueAsString(value);
        } catch (IOException e) {
            throw new HibernateException(e);
        }

        st.setString(index, json);
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {

        if (!getClazz().isInstance(value)) {
            throw new UnsupportedOperationException("Convert error: "
                    + value.getClass());
        }

        return (Serializable) value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner)
            throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner)
            throws HibernateException {
        return original;
    }

}
