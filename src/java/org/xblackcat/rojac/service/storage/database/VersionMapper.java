package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.data.Version;
import org.xblackcat.sjpu.storage.typemap.IMapFactory;
import org.xblackcat.sjpu.storage.typemap.ITypeMap;
import org.xblackcat.sjpu.storage.typemap.NullPassTypeMap;

public class VersionMapper implements IMapFactory<Version, byte[]> {
    @Override
    public boolean isAccepted(Class<?> obj) {
        return Version.class.isAssignableFrom(obj);
    }

    @Override
    public ITypeMap<Version, byte[]> mapper(Class<Version> clazz) {
        return new NullPassTypeMap<>(Version.class, byte[].class, Version::getBytes, Version::new);
    }
}
