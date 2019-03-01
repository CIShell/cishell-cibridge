package org.cishell.cibridge.cishell.impl;

import org.cishell.cibridge.core.model.AlgorithmDefinition;
import org.cishell.cibridge.core.model.AlgorithmType;
import org.cishell.cibridge.core.model.ConversionType;
import org.cishell.cibridge.core.model.Property;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.osgi.framework.ServiceReference;

import java.util.Arrays;
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
			.of(IN_DATA, OUT_DATA, LABEL, DESCRIPTION, ALGORITHM_TYPE, REMOTEABLE, MENU_PATH, CONVERSION, AUTHORS, // standard
																													// algorithm
																													// properties
					"author", IMPLEMENTERS, INTEGRATORS, DOCUMENTATION_URL, REFERENCE, REFERENCE_URL, WRITTEN_IN, // standard
																													// algorithm
																													// properties
					SERVICE_PID, SERVICE_ID, COMPONENT_ID, COMPONENT_NAME, OBJECTCLASS, SERVICE_BUNDLEID, SERVICE_SCOPE // other
																														// service
																														// related
																														// properties
			).collect(Collectors.toCollection(HashSet::new));

	public CIShellCIBridgeAlgorithmDefinition(ServiceReference<AlgorithmFactory> reference,
			AlgorithmFactory algorithmFactory) {
		super(reference.getProperty(SERVICE_PID).toString());

		// TODO check why we are getting so many null values for algorithm factory
		// service
		// Preconditions.checkNotNull(algorithmFactory, "algorithmFactory cannot be null
		// for service: " + getId());
		this.algorithmFactory = algorithmFactory;

		// TODO parameters

		if (reference.getProperty(IN_DATA) != null) {
			Arrays.stream(reference.getProperty(IN_DATA).toString().split(",")).sequential().map(String::trim)
					.forEachOrdered(getInData()::add);
		} else {
			// todo should we set the default value of a singleton list with a null value??
			getInData().add("null");
		}
		if (reference.getProperty(OUT_DATA) != null) {
			Arrays.stream(reference.getProperty(OUT_DATA).toString().split(",")).sequential().map(String::trim)
					.forEachOrdered(getOutData()::add);
		} else {
			// TODO should we set the default value of a singleton list with a null value??
			getOutData().add("null");
		}
		if (reference.getProperty(LABEL) != null) {
			setLabel(reference.getProperty(LABEL).toString());
		}
		if (reference.getProperty(DESCRIPTION) != null) {
			setDescription(reference.getProperty(DESCRIPTION).toString());
		}

		// TODO parentOutputData

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
}
