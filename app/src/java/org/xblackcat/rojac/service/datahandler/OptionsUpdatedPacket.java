package org.xblackcat.rojac.service.datahandler;

import org.xblackcat.rojac.service.options.Property;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author xBlackCat
 */
public class OptionsUpdatedPacket implements IPacket {
    private final Set<Property<?>> affectedProperties = new HashSet<>();

    public OptionsUpdatedPacket(Collection<Property<?>> affectedProperties) {
        this.affectedProperties.addAll(affectedProperties);
    }

    public boolean isPropertyAffected(Property<?> p) {
        return affectedProperties.contains(p);
    }
}
