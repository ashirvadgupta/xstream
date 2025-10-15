package com.thoughtworks.xstream.core.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.thoughtworks.xstream.converters.ConversionException;

public class PlatformDependentTypeFactory {

    private static Class<?> activationDataFlavorType;
    private static Constructor<?> activationDataFlavorCtor1;
    private static Constructor<?> activationDataFlavorCtor2;
    private static Constructor<?> activationDataFlavorCtor3;

    private static Class<?> datatypeConverterType;

    static {
        try {
            activationDataFlavorType = Class.forName("jakarta.activation.ActivationDataFlavor");
            try {
                activationDataFlavorCtor1 = activationDataFlavorType.getConstructor(String.class, String.class);
            } catch (NoSuchMethodException e) {
                // ignore
            }
            try {
                activationDataFlavorCtor2 = activationDataFlavorType.getConstructor(Class.class, String.class);
            } catch (NoSuchMethodException e) {
                // ignore
            }
            try {
                activationDataFlavorCtor3 = activationDataFlavorType.getConstructor(Class.class, String.class, String.class);
            } catch (NoSuchMethodException e) {
                // ignore
            }
        } catch (ClassNotFoundException e) {
            try {
                activationDataFlavorType = Class.forName("javax.activation.ActivationDataFlavor");
                try {
                    activationDataFlavorCtor1 = activationDataFlavorType.getConstructor(String.class, String.class);
                } catch (NoSuchMethodException ex) {
                    // ignore
                }
                try {
                    activationDataFlavorCtor2 = activationDataFlavorType.getConstructor(Class.class, String.class);
                } catch (NoSuchMethodException ex) {
                    // ignore
                }
                try {
                    activationDataFlavorCtor3 = activationDataFlavorType.getConstructor(Class.class, String.class, String.class);
                } catch (NoSuchMethodException ex) {
                    // ignore
                }
            } catch (ClassNotFoundException ex) {
                // Neither Jakarta nor Javax ActivationDataFlavor found
                activationDataFlavorType = null;
            }
        }

        try {
            datatypeConverterType = Class.forName("jakarta.xml.bind.DatatypeConverter");
        } catch (ClassNotFoundException e) {
            try {
                datatypeConverterType = Class.forName("javax.xml.bind.DatatypeConverter");
            } catch (ClassNotFoundException ex) {
                // Neither Jakarta nor Javax DatatypeConverter found
                datatypeConverterType = null;
            }
        }
    }

    public static Class<?> getActivationDataFlavorType() {
        return activationDataFlavorType;
    }

    public static Object createActivationDataFlavor(String mimeType, String name) {
        if (activationDataFlavorType == null || activationDataFlavorCtor1 == null) {
            return null;
        }
        try {
            return activationDataFlavorCtor1.newInstance(mimeType, name);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ConversionException(e);
        }
    }

    public static Object createActivationDataFlavor(Class<?> type, String name) {
        if (activationDataFlavorType == null || activationDataFlavorCtor2 == null) {
            return null;
        }
        try {
            return activationDataFlavorCtor2.newInstance(type, name);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ConversionException(e);
        }
    }

    public static Object createActivationDataFlavor(Class<?> type, String mimeType, String name) {
        if (activationDataFlavorType == null || activationDataFlavorCtor3 == null) {
            return null;
        }
        try {
            return activationDataFlavorCtor3.newInstance(type, mimeType, name);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ConversionException(e);
        }
    }

    public static byte[] parseBase64Binary(String lexicalXsBase64Binary) {
        if (datatypeConverterType == null) {
            throw new ConversionException("Neither jakarta.xml.bind.DatatypeConverter nor javax.xml.bind.DatatypeConverter is available on the classpath");
        }
        try {
            return (byte[]) datatypeConverterType.getMethod("parseBase64Binary", String.class).invoke(null, lexicalXsBase64Binary);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ConversionException(e);
        }
    }

    public static String printBase64Binary(byte[] val) {
        if (datatypeConverterType == null) {
            throw new ConversionException("Neither jakarta.xml.bind.DatatypeConverter nor javax.xml.bind.DatatypeConverter is available on the classpath");
        }
        try {
            return (String) datatypeConverterType.getMethod("printBase64Binary", byte[].class).invoke(null, val);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ConversionException(e);
        }
    }
}
