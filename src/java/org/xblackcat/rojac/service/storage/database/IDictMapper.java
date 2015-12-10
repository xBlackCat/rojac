package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.data.IDictionary;
import org.xblackcat.sjpu.storage.typemap.ATypeMap;
import org.xblackcat.sjpu.storage.typemap.IMapFactory;
import org.xblackcat.sjpu.storage.typemap.ITypeMap;

import java.sql.Connection;

public class IDictMapper<T extends Enum<T> & IDictionary> implements IMapFactory<T, Integer> {
    @Override
    public boolean isAccepted(Class<?> obj) {
        return IDictionary.class.isAssignableFrom(obj) && Enum.class.isAssignableFrom(obj);
    }

    @Override
    public ITypeMap<T, Integer> mapper(Class<T> clazz) {
        return new ATypeMap<T, Integer>(clazz, Integer.class) {
            @Override
            public Integer forStore(Connection c, T obj) {
                if (obj == null) {
                    return null;
                }
                return obj.getId();
            }

            @Override
            public T forRead(Integer obj) {
                if (obj == null) {
                    return null;
                }

                int id = obj;
                for (T e : getRealType().getEnumConstants()) {
                    if (e.getId() == id) {
                        return e;
                    }
                }

                throw new IllegalArgumentException("Invalid id of queue item: " + id);
            }
        };
    }
}
