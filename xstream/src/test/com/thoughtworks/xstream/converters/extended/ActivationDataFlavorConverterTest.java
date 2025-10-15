/*
 * Copyright (C) 2015 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 *
 * Created on 23.06.2015 by Joerg Schaible
 */
package com.thoughtworks.xstream.converters.extended;

import java.awt.datatransfer.DataFlavor;

import com.thoughtworks.acceptance.AbstractAcceptanceTest;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.PlatformDependentTypeFactory;


public class ActivationDataFlavorConverterTest extends AbstractAcceptanceTest {

    @Override
    protected void setupSecurity(XStream xstream) {
        super.setupSecurity(xstream);
        xstream.allowTypeHierarchy(DataFlavor.class);
        // Allow both javax and jakarta ActivationDataFlavor types
        final Class<?> activationDataFlavorType = PlatformDependentTypeFactory.getActivationDataFlavorType();
        if (activationDataFlavorType != null) {
            xstream.allowTypes(activationDataFlavorType);
        }
    }

    public void testMimeTypeOnly() {
        final Class<?> activationDataFlavorType = PlatformDependentTypeFactory.getActivationDataFlavorType();
        if (activationDataFlavorType == null) {
            // Skip test if ActivationDataFlavor is not available
            return;
        }
        final String expected = ""
            + "<activation-data-flavor>\n"
            + "  <mimeType>application/x-junit</mimeType>\n"
            + "  <representationClass>java.io.InputStream</representationClass>\n"
            + "</activation-data-flavor>";
        final Object dataFlavor = PlatformDependentTypeFactory.createActivationDataFlavor("application/x-junit", null);
        assertBothWays(dataFlavor, expected);
    }

    public void testMimeTypeAndRepresentation() {
        final Class<?> activationDataFlavorType = PlatformDependentTypeFactory.getActivationDataFlavorType();
        if (activationDataFlavorType == null) {
            // Skip test if ActivationDataFlavor is not available
            return;
        }
        final String expected = ""
            + "<activation-data-flavor>\n"
            + "  <mimeType>application/x-junit</mimeType>\n"
            + "  <humanRepresentableName>JUnit</humanRepresentableName>\n"
            + "  <representationClass>java.io.InputStream</representationClass>\n"
            + "</activation-data-flavor>";
        final Object dataFlavor = PlatformDependentTypeFactory.createActivationDataFlavor("application/x-junit", "JUnit");
        assertBothWays(dataFlavor, expected);
    }

    public void testWithAllArguments() {
        final Class<?> activationDataFlavorType = PlatformDependentTypeFactory.getActivationDataFlavorType();
        if (activationDataFlavorType == null) {
            // Skip test if ActivationDataFlavor is not available
            return;
        }
        final String expected = ""
            + "<activation-data-flavor>\n"
            + "  <mimeType>application/x-junit</mimeType>\n"
            + "  <humanRepresentableName>JUnit</humanRepresentableName>\n"
            + "  <representationClass>com.thoughtworks.xstream.converters.extended.ActivationDataFlavorConverterTest</representationClass>\n"
            + "</activation-data-flavor>";
        final Object dataFlavor = PlatformDependentTypeFactory.createActivationDataFlavor(ActivationDataFlavorConverterTest.class, "application/x-junit", "JUnit");
        assertBothWays(dataFlavor, expected);
    }
}
