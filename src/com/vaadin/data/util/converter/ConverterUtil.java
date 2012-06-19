/*
@VaadinApache2LicenseForJavaFiles@
 */
package com.vaadin.data.util.converter;

import java.util.Locale;

import com.vaadin.Application;

public class ConverterUtil {

    public static <UITYPE, MODELTYPE> Converter<UITYPE, MODELTYPE> getConverter(
            Class<UITYPE> uiType, Class<MODELTYPE> modelType) {
        Converter<UITYPE, MODELTYPE> converter = null;

        Application app = Application.getCurrentApplication();
        if (app != null) {
            ConverterFactory factory = app.getConverterFactory();
            converter = factory.createConverter(uiType, modelType);
        }
        return converter;

    }

    /**
     * Convert the given value from the data source type to the UI type.
     * 
     * @param modelValue
     *            The model value to convert
     * @param presentationType
     *            The type of the presentation value
     * @param converter
     *            The converter to (try to) use
     * @param locale
     *            The locale to use for conversion
     * @param <PRESENTATIONTYPE>
     *            Presentation type
     * 
     * @return The converted value, compatible with the presentation type, or
     *         the original value if its type is compatible and no converter is
     *         set.
     * @throws Converter.ConversionException
     *             if there is no converter and the type is not compatible with
     *             the model type.
     */
    @SuppressWarnings("unchecked")
    public static <PRESENTATIONTYPE, MODELTYPE> PRESENTATIONTYPE convertFromModel(
            MODELTYPE modelValue,
            Class<? extends PRESENTATIONTYPE> presentationType,
            Converter<PRESENTATIONTYPE, MODELTYPE> converter, Locale locale)
            throws Converter.ConversionException {
        if (converter != null) {
            return converter.convertToPresentation(modelValue, locale);
        }
        if (modelValue == null) {
            return null;
        }

        if (presentationType.isAssignableFrom(modelValue.getClass())) {
            return (PRESENTATIONTYPE) modelValue;
        } else {
            throw new Converter.ConversionException(
                    "Unable to convert value of type "
                            + modelValue.getClass().getName()
                            + " to presentation type "
                            + presentationType
                            + ". No converter is set and the types are not compatible.");
        }
    }

    /**
     * @param <MODELTYPE>
     * @param <PRESENTATIONTYPE>
     * @param presentationValue
     * @param modelType
     * @param converter
     * @param locale
     * @return
     * @throws Converter.ConversionException
     */
    public static <MODELTYPE, PRESENTATIONTYPE> MODELTYPE convertToModel(
            PRESENTATIONTYPE presentationValue, Class<MODELTYPE> modelType,
            Converter<PRESENTATIONTYPE, MODELTYPE> converter, Locale locale)
            throws Converter.ConversionException {
        if (converter != null) {
            /*
             * If there is a converter, always use it. It must convert or throw
             * an exception.
             */
            return converter.convertToModel(presentationValue, locale);
        }

        if (presentationValue == null) {
            // Null should always be passed through the converter but if there
            // is no converter we can safely return null
            return null;
        }

        // check that the value class is compatible with the model type
        if (modelType.isAssignableFrom(presentationValue.getClass())) {
            return modelType.cast(presentationValue);
        } else {
            throw new Converter.ConversionException(
                    "Unable to convert value of type "
                            + presentationValue.getClass().getName()
                            + " to model type "
                            + modelType
                            + ". No converter is set and the types are not compatible.");
        }

    }

}