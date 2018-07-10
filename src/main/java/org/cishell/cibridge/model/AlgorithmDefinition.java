package org.cishell.cibridge.model;

public class AlgorithmDefinition {
	private final String id;
	private final InputParameters parameters;
	private final String inData;
	private final String outData;
	private final String label;
	private final String description;
	private final Boolean parentOutputData;
	private final AlgorithmType type;
	private final Boolean remoteable;
	private final String menuPath;
	private final ConversionType conversion;
	private final String authors;
	private final String implementers;
	private final String integrators;
	private final String documentationUrl;
	private final String referenceUrl;
	private final String writtenIn;
	private final Property otherProperties;

	// constructor with id initialisation
	public AlgorithmDefinition(String id, InputParameters parameters, String inData, String outData, String label,
			String description, Boolean parentOutputData, AlgorithmType type, Boolean remoteable, String menuPath,
			ConversionType conversion, String authors, String implementers, String integrators, String documentationUrl,
			String referenceUrl, String writtenIn, Property otherProperties) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.parameters = parameters;
		this.inData = inData;
		this.outData = outData;
		this.label = label;
		this.description = description;
		this.parentOutputData = parentOutputData;
		this.type = type;
		this.remoteable = remoteable;
		this.menuPath = menuPath;
		this.conversion = conversion;
		this.authors = authors;
		this.implementers = implementers;
		this.integrators = integrators;
		this.documentationUrl = documentationUrl;
		this.referenceUrl = referenceUrl;
		this.writtenIn = writtenIn;
		this.otherProperties = otherProperties;
	}

	public AlgorithmDefinition(InputParameters parameters, String inData, String outData, String label,
			String description, Boolean parentOutputData, AlgorithmType type, Boolean remoteable, String menuPath,
			ConversionType conversion, String authors, String implementers, String integrators, String documentationUrl,
			String referenceUrl, String writtenIn, Property otherProperties) {
		this.id=null;
		this.parameters = parameters;
		this.inData = inData;
		this.outData = outData;
		this.label = label;
		this.description = description;
		this.parentOutputData = parentOutputData;
		this.type = type;
		this.remoteable = remoteable;
		this.menuPath = menuPath;
		this.conversion = conversion;
		this.authors = authors;
		this.implementers = implementers;
		this.integrators = integrators;
		this.documentationUrl = documentationUrl;
		this.referenceUrl = referenceUrl;
		this.writtenIn = writtenIn;
		this.otherProperties = otherProperties;
	}

	public String getId() {
		return id;
	}

	public InputParameters getParameters() {
		return parameters;
	}

	public String getInData() {
		return inData;
	}

	public String getOutData() {
		return outData;
	}

	public String getLabel() {
		return label;
	}

	public String getDescription() {
		return description;
	}

	public Boolean getParentOutputData() {
		return parentOutputData;
	}

	public AlgorithmType getType() {
		return type;
	}

	public Boolean getRemoteable() {
		return remoteable;
	}

	public String getMenuPath() {
		return menuPath;
	}

	public ConversionType getConversion() {
		return conversion;
	}

	public String getAuthors() {
		return authors;
	}

	public String getImplementers() {
		return implementers;
	}

	public String getIntegrators() {
		return integrators;
	}

	public String getDocumentationUrl() {
		return documentationUrl;
	}

	public String getReferenceUrl() {
		return referenceUrl;
	}

	public String getWrittenIn() {
		return writtenIn;
	}

	public Property getOtherProperties() {
		return otherProperties;
	}
	
	

}