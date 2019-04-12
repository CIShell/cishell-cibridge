package org.cishell.cibridge.cishell.model;

import com.google.common.base.Preconditions;
import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.cishell.util.Util;
import org.cishell.cibridge.core.model.AlgorithmDefinition;
import org.cishell.cibridge.core.model.AlgorithmType;
import org.cishell.cibridge.core.model.ConversionType;
import org.cishell.cibridge.core.model.Property;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.service.conversion.Converter;
import org.osgi.framework.ServiceReference;
import org.osgi.service.metatype.MetaTypeInformation;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.cishell.framework.algorithm.AlgorithmProperty.*;
import static org.osgi.framework.Constants.*;
import static org.osgi.service.component.ComponentConstants.COMPONENT_ID;
import static org.osgi.service.component.ComponentConstants.COMPONENT_NAME;

public class CIShellCIBridgeAlgorithmDefinition extends AlgorithmDefinition {
    private AlgorithmFactory algorithmFactory;

    private static final HashSet<String> GENERAL_PROPERTIES = Stream
            .of(    // standard algorithm properties
                    IN_DATA, OUT_DATA, LABEL, DESCRIPTION, ALGORITHM_TYPE, REMOTEABLE, MENU_PATH, CONVERSION, AUTHORS,
                    "author", IMPLEMENTERS, INTEGRATORS, DOCUMENTATION_URL, REFERENCE, REFERENCE_URL, WRITTEN_IN,
                    // other service related properties
                    SERVICE_PID, SERVICE_ID, COMPONENT_ID, COMPONENT_NAME, OBJECTCLASS, SERVICE_BUNDLEID, SERVICE_SCOPE
            ).collect(Collectors.toCollection(HashSet::new));

    public CIShellCIBridgeAlgorithmDefinition(Converter converter) {
        super("ConverterAlgorithm@" + Integer.toHexString(converter.hashCode()));

        algorithmFactory = converter.getAlgorithmFactory();
        Preconditions.checkNotNull(algorithmFactory, "algorithmFactory cannot be null for service: " + getId());

        setInData(converter.getProperties().get(IN_DATA));

        setOutData(converter.getProperties().get(OUT_DATA));

        if (converter.getProperties().get(LABEL) != null) {
            setLabel(converter.getProperties().get(LABEL).toString());
        }

        if (converter.getProperties().get(DESCRIPTION) != null) {
            setDescription(converter.getProperties().get(DESCRIPTION).toString());
        }

        setType(AlgorithmType.CONVERTER);

        if (converter.getProperties().get(CONVERSION) != null) {
            setConversion(ConversionType.valueOf(converter.getProperties().get(CONVERSION).toString().toUpperCase()));
        }
    }

    public CIShellCIBridgeAlgorithmDefinition(CIShellCIBridge cibridge, ServiceReference<AlgorithmFactory> reference) {
        super(reference.getProperty(SERVICE_PID).toString());

        algorithmFactory = cibridge.getBundleContext().getService(reference);
        Preconditions.checkNotNull(algorithmFactory, "algorithmFactory cannot be null for service: " + getId());

        //read parameters from metatype service
        MetaTypeInformation metaTypeInformation = cibridge.getMetaTypeService().getMetaTypeInformation(reference.getBundle());
        setParameters(Util.getInputParametersFromMetaTypeInfo(metaTypeInformation, getId()));

        setInData(reference.getProperty(IN_DATA));

        setOutData(reference.getProperty(OUT_DATA));

        if (reference.getProperty(LABEL) != null) {
            setLabel(reference.getProperty(LABEL).toString());
        }

        if (reference.getProperty(DESCRIPTION) != null) {
            setDescription(reference.getProperty(DESCRIPTION).toString());
        }

        // TODO parentOutputData. check if the parentage property is set to default. page 10 of cishell spec pdf

        if (reference.getProperty(ALGORITHM_TYPE) != null) {
            setType(AlgorithmType.valueOf(reference.getProperty(ALGORITHM_TYPE).toString().toUpperCase()));
        } else {
            setType(AlgorithmType.STANDARD);
        }

        if (reference.getProperty(REMOTEABLE) != null) {
            setRemoteable(Boolean.valueOf(reference.getProperty(REMOTEABLE).toString()));
        }

        if (reference.getProperty(MENU_PATH) != null) {
            setMenuPath(reference.getProperty(MENU_PATH).toString());
        }

        if (reference.getProperty(CONVERSION) != null) {
            setConversion(ConversionType.valueOf(reference.getProperty(CONVERSION).toString().toUpperCase()));
        }

        if (reference.getProperty(AUTHORS) != null) {
            setAuthors(reference.getProperty(AUTHORS).toString());
        }

        if (reference.getProperty("author") != null) {
            setAuthors(reference.getProperty("author").toString());
        }

        if (reference.getProperty(IMPLEMENTERS) != null) {
            setImplementers(reference.getProperty(IMPLEMENTERS).toString());
        }

        if (reference.getProperty(INTEGRATORS) != null) {
            setIntegrators(reference.getProperty(INTEGRATORS).toString());
        }

        if (reference.getProperty(DOCUMENTATION_URL) != null) {
            setDocumentationUrl(reference.getProperty(DOCUMENTATION_URL).toString());
        }

        if (reference.getProperty(REFERENCE) != null) {
            setReference(reference.getProperty(REFERENCE).toString());
        }

        if (reference.getProperty(REFERENCE_URL) != null) {
            setReferenceUrl(reference.getProperty(REFERENCE_URL).toString());
        }

        if (reference.getProperty(WRITTEN_IN) != null) {
            setWrittenIn(reference.getProperty(WRITTEN_IN).toString());
        }

        Arrays.stream(reference.getPropertyKeys()).filter(propertyKey -> !GENERAL_PROPERTIES.contains(propertyKey))
                .forEach(propertyKey -> getOtherProperties()
                        .add(new Property(propertyKey, reference.getProperty(propertyKey).toString())));
    }

    public AlgorithmFactory getAlgorithmFactory() {
        return algorithmFactory;
    }

    @Override
    public String toString() {
        return "CIShellCIBridgeAlgorithmDefinition{" + "algorithmFactory=" + algorithmFactory + "} " + super.toString();
    }

    private void setInData(Object inData) {
        if (inData != null) {
            Arrays.stream(inData.toString().split(",")).sequential().map(String::trim)
                    .forEachOrdered(getInData()::add);
        } else {
            setInData(Collections.singletonList("null"));
        }
    }

    private void setOutData(Object outData) {
        if (outData != null) {
            Arrays.stream(outData.toString().split(",")).sequential().map(String::trim)
                    .forEachOrdered(getOutData()::add);
        } else {
            setOutData(Collections.singletonList("null"));
        }
    }
}
