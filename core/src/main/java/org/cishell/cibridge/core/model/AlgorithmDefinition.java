package org.cishell.cibridge.core.model;

import java.util.LinkedList;
import java.util.List;

public class AlgorithmDefinition {

    private final String id;
    private InputParameters parameters;
    private List<String> inData = new LinkedList<>();
    private List<String> outData = new LinkedList<>();
    private String label;
    private String description;
    private Boolean parentOutputData;
    private AlgorithmType type;
    private Boolean remoteable;
    private String menuPath;
    private ConversionType conversion;
    private String authors;
    private String implementers;
    private String integrators;
    private String documentationUrl;
    private String reference;
    private String referenceUrl;
    private String writtenIn;
    private List<Property> otherProperties = new LinkedList<>();

    public AlgorithmDefinition(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public InputParameters getParameters() {
        return parameters;
    }

    public void setParameters(InputParameters parameters) {
        this.parameters = parameters;
    }

    public List<String> getInData() {
        return inData;
    }

    public void setInData(List<String> inData) {
        this.inData = inData;
    }

    public List<String> getOutData() {
        return outData;
    }

    public void setOutData(List<String> outData) {
        this.outData = outData;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getParentOutputData() {
        return parentOutputData;
    }

    public void setParentOutputData(Boolean parentOutputData) {
        this.parentOutputData = parentOutputData;
    }

    public AlgorithmType getType() {
        return type;
    }

    public void setType(AlgorithmType type) {
        this.type = type;
    }

    public Boolean getRemoteable() {
        return remoteable;
    }

    public void setRemoteable(Boolean remoteable) {
        this.remoteable = remoteable;
    }

    public String getMenuPath() {
        return menuPath;
    }

    public void setMenuPath(String menuPath) {
        this.menuPath = menuPath;
    }

    public ConversionType getConversion() {
        return conversion;
    }

    public void setConversion(ConversionType conversion) {
        this.conversion = conversion;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getImplementers() {
        return implementers;
    }

    public void setImplementers(String implementers) {
        this.implementers = implementers;
    }

    public String getIntegrators() {
        return integrators;
    }

    public void setIntegrators(String integrators) {
        this.integrators = integrators;
    }

    public String getDocumentationUrl() {
        return documentationUrl;
    }

    public void setDocumentationUrl(String documentationUrl) {
        this.documentationUrl = documentationUrl;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReferenceUrl() {
        return referenceUrl;
    }

    public void setReferenceUrl(String referenceUrl) {
        this.referenceUrl = referenceUrl;
    }

    public String getWrittenIn() {
        return writtenIn;
    }

    public void setWrittenIn(String writtenIn) {
        this.writtenIn = writtenIn;
    }

    public List<Property> getOtherProperties() {
        return otherProperties;
    }

    public void setOtherProperties(List<Property> otherProperties) {
        this.otherProperties = otherProperties;
    }

    @Override
    public String toString() {
        return "AlgorithmDefinition{" +
                "id='" + id + '\'' +
                ", parameters=" + parameters +
                ", inData=" + inData +
                ", outData=" + outData +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", parentOutputData=" + parentOutputData +
                ", type=" + type +
                ", remoteable=" + remoteable +
                ", menuPath='" + menuPath + '\'' +
                ", conversion=" + conversion +
                ", authors='" + authors + '\'' +
                ", implementers='" + implementers + '\'' +
                ", integrators='" + integrators + '\'' +
                ", documentationUrl='" + documentationUrl + '\'' +
                ", reference='" + reference + '\'' +
                ", referenceUrl='" + referenceUrl + '\'' +
                ", writtenIn='" + writtenIn + '\'' +
                ", otherProperties=" + otherProperties +
                '}';
    }
}
