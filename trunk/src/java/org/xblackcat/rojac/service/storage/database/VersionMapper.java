package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.data.Version;
import org.xblackcat.sjpu.storage.typemap.ATypeMap;
import org.xblackcat.sjpu.storage.typemap.IMapFactory;
import org.xblackcat.sjpu.storage.typemap.ITypeMap;

public class VersionMapper implements IMapFactory<Version, byte[]> {
    @Override
    public boolean isAccepted(Class<?> obj) {
        return Version.class.isAssignableFrom(obj);
    }

    @Override
    public ITypeMap<Version, byte[]> mapper(Class<Version> clazz) {
        return new ATypeMap<Version, byte[]>(clazz, byte[].class) {
            @Override
            public byte[] forStore(Version obj) {
                if (obj == null) {
                    return null;
                }
                return obj.getBytes();
            }

            @Override
            public Version forRead(byte[] obj) {
                if (obj == null) {
                    return null;
                }

                return new Version(obj);
            }
        };
    }
}
