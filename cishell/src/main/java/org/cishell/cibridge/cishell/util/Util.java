package org.cishell.cibridge.cishell.util;

import com.google.common.base.Preconditions;
import io.reactivex.BackpressureStrategy;
import io.reactivex.observables.ConnectableObservable;
import org.cishell.cibridge.core.model.*;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.MetaTypeInformation;
import org.osgi.service.metatype.ObjectClassDefinition;
import org.reactivestreams.Publisher;

import java.util.*;
import java.util.stream.Collectors;

public class Util {
    private static Map<Integer, AttributeType> attributeTypeMap = new HashMap<>();
    private static HashMap<Integer, LogLevel> logLevelMap = new HashMap<>();

    static {
        //map integers with AttributeType values
        attributeTypeMap.put(1, AttributeType.STRING);
        attributeTypeMap.put(2, AttributeType.LONG);
        attributeTypeMap.put(3, AttributeType.INTEGER);
        attributeTypeMap.put(4, AttributeType.SHORT);
        attributeTypeMap.put(5, AttributeType.CHARACTER);
        attributeTypeMap.put(6, AttributeType.BYTE);
        attributeTypeMap.put(7, AttributeType.DOUBLE);
        attributeTypeMap.put(8, AttributeType.FLOAT);
        attributeTypeMap.put(11, AttributeType.BOOLEAN);

        // Maps integers with LogLevel
        logLevelMap.put(1, LogLevel.ERROR);
        logLevelMap.put(2, LogLevel.WARNING);
        logLevelMap.put(3, LogLevel.INFO);
        logLevelMap.put(4, LogLevel.DEBUG);
    }

    public static AttributeType getAttributeTypeFromInteger(int i) {
        Preconditions.checkArgument(attributeTypeMap.containsKey(i), "AttributeType for given integer value doesn't exist");
        return attributeTypeMap.get(i);
    }

    public static LogLevel getLogLevelFromInteger(int i) {
        Preconditions.checkArgument(logLevelMap.containsKey(i), "LogLevel for given integer value doesn't exist");
        return logLevelMap.get(i);
    }

    public static InputParameters getInputParametersFromMetaTypeInfo(MetaTypeInformation metaTypeInformation, String pid) {

        String[] pids = metaTypeInformation.getPids() != null ? metaTypeInformation.getPids() : new String[0];
        Set<String> pidSet = Arrays.stream(pids).collect(Collectors.toCollection(HashSet::new));

        if (pidSet.contains(pid)) {
            ObjectClassDefinition ocd = metaTypeInformation.getObjectClassDefinition(pid, null);

            InputParameters inputParameters = new InputParameters(ocd.getID());
            inputParameters.setDescription(ocd.getDescription());
            inputParameters.setTitle(ocd.getName());
            inputParameters.setParameters(new ArrayList<>());

            for (AttributeDefinition ad : ocd.getAttributeDefinitions(ObjectClassDefinition.ALL)) {
                ParameterDefinition parameter = new ParameterDefinition(ad.getID());
                parameter.setName(ad.getName());
                parameter.setDescription(ad.getDescription());
                parameter.setType(Util.getAttributeTypeFromInteger(ad.getType()));
                parameter.setCardinality(ad.getCardinality());
                if (ad.getDefaultValue() != null) {
                    parameter.setDefaultValues(Arrays.asList(ad.getDefaultValue()));
                }
                parameter.setOptions(new ArrayList<>());

                String[] optionLabels = ad.getOptionLabels();
                String[] optionValues = ad.getOptionValues();
                for (int i = 0; i < optionLabels.length; i++) {
                    parameter.getOptions().add(new Property(optionLabels[i], optionValues[i]));
                }

                inputParameters.getParameters().add(parameter);
            }

            return inputParameters;
        }

        return null;
    }

    public static Object getParsedValue(String value, AttributeType attributeType) {

        if(attributeType == null){
            return value;
        }

        switch (attributeType) {
            case BYTE:
                return value.getBytes();
            case LONG:
                return Long.parseLong(value);
            case FLOAT:
                return Float.parseFloat(value);
            case SHORT:
                return Short.parseShort(value);
            case DOUBLE:
                return Double.parseDouble(value);
            case BOOLEAN:
                return Boolean.parseBoolean(value);
            case INTEGER:
                return Integer.parseInt(value);
            case CHARACTER:
                return value.toCharArray();
            default:
                return value;
        }
    }

    public static <T> Publisher<T> asPublisher(ConnectableObservable<T> updateObservable) {
        return updateObservable.toFlowable(BackpressureStrategy.BUFFER);
    }

}
