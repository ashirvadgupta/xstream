/*
 * Copyright (C) 2015, 2020 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 *
 * Created on 21.06.2015 by Joerg Schaible
 */
package com.thoughtworks.xstream.converters.extended;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.core.util.PlatformDependentTypeFactory;

import java.lang.reflect.InvocationTargetException;


/**
 * Converts an ActivationDataFlavor (both javax.activation and jakarta.activation).
 *
 * @author J&ouml;rg Schaible
 * @since 1.4.9
 */
public class ActivationDataFlavorConverter implements Converter {

    @Override
    public boolean canConvert(final Class<?> type) {
        return type == PlatformDependentTypeFactory.getActivationDataFlavorType();
    }

    @Override
    public void marshal(final Object source, final HierarchicalStreamWriter writer, final MarshallingContext context) {
        final Object dataFlavor = PlatformDependentTypeFactory.getActivationDataFlavorType().cast(source);
        final String mimeType;
        try {
            mimeType = (String) dataFlavor.getClass().getMethod("getMimeType").invoke(dataFlavor);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ConversionException(e);
        }
        if (mimeType != null) {
            writer.startNode("mimeType");
            writer.setValue(mimeType);
            writer.endNode();
        }
        final String name;
        try {
            name = (String) dataFlavor.getClass().getMethod("getHumanPresentableName").invoke(dataFlavor);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ConversionException(e);
        }
        if (name != null) {
            writer.startNode("humanRepresentableName");
            writer.setValue(name);
            writer.endNode();
        }
        final Class<?> representationClass;
        try {
            representationClass = (Class<?>) dataFlavor.getClass().getMethod("getRepresentationClass").invoke(dataFlavor);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ConversionException(e);
        }
        if (representationClass != null) {
            writer.startNode("representationClass");
            context.convertAnother(representationClass);
            writer.endNode();
        }
    }

    @Override
    public Object unmarshal(final HierarchicalStreamReader reader, final UnmarshallingContext context) {
        String mimeType = null;
        String name = null;
        Class<?> type = null;
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            
            final String elementName = reader.getNodeName();
            if (elementName.equals("mimeType")) {
                mimeType = reader.getValue();
            } else if (elementName.equals("humanRepresentableName")) {
                name = reader.getValue();
            } else if (elementName.equals("representationClass")) {
                type = (Class<?>)context.convertAnother(null, Class.class);
            } else {
                final ConversionException exception = new ConversionException("Unknown child element");
                exception.add("element", reader.getNodeName());
                throw exception;
            }
            reader.moveUp();
        }
        Object dataFlavor = null;
        try {
            if (type == null) {
                dataFlavor = PlatformDependentTypeFactory.createActivationDataFlavor(mimeType, name);
            } else if (mimeType == null) {
                dataFlavor = PlatformDependentTypeFactory.createActivationDataFlavor(type, name);
            } else {
                dataFlavor = PlatformDependentTypeFactory.createActivationDataFlavor(type, mimeType, name);
            }
        } catch (final IllegalArgumentException | NullPointerException ex) {
            throw new ConversionException(ex);
        }
        return dataFlavor;
    }
}
