package com.yq.allure2_android.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class TestResult
        extends ExecutableItem
        implements Serializable, WithLinks
{

    private final static long serialVersionUID = 1L;
    protected String uuid;
    protected String historyId;
    protected String testCaseId;
    protected String rerunOf;
    protected String fullName;
    protected List<Label> labels;
    protected List<Link> links;

    /**
     * Gets the value of the uuid property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Sets the value of the uuid property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUuid(String value) {
        this.uuid = value;
    }

    /**
     * Gets the value of the historyId property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getHistoryId() {
        return historyId;
    }

    /**
     * Sets the value of the historyId property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setHistoryId(String value) {
        this.historyId = value;
    }

    /**
     * Gets the value of the testCaseId property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTestCaseId() {
        return testCaseId;
    }

    /**
     * Sets the value of the testCaseId property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTestCaseId(String value) {
        this.testCaseId = value;
    }

    /**
     * Gets the value of the rerunOf property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRerunOf() {
        return rerunOf;
    }

    /**
     * Sets the value of the rerunOf property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRerunOf(String value) {
        this.rerunOf = value;
    }

    /**
     * Gets the value of the fullName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the value of the fullName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFullName(String value) {
        this.fullName = value;
    }

    public List<Label> getLabels() {
        if (labels == null) {
            labels = new ArrayList<Label>();
        }
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public List<Link> getLinks() {
        if (links == null) {
            links = new ArrayList<Link>();
        }
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public TestResult withUuid(String value) {
        setUuid(value);
        return this;
    }

    public TestResult withHistoryId(String value) {
        setHistoryId(value);
        return this;
    }

    public TestResult withTestCaseId(String value) {
        setTestCaseId(value);
        return this;
    }

    public TestResult withRerunOf(String value) {
        setRerunOf(value);
        return this;
    }

    public TestResult withFullName(String value) {
        setFullName(value);
        return this;
    }

    public TestResult withLabels(Label... values) {
        if (values!= null) {
            for (Label value: values) {
                getLabels().add(value);
            }
        }
        return this;
    }

    public TestResult withLabels(Collection<Label> values) {
        if (values!= null) {
            getLabels().addAll(values);
        }
        return this;
    }

    public TestResult withLabels(List<Label> labels) {
        setLabels(labels);
        return this;
    }

    public TestResult withLinks(Link... values) {
        if (values!= null) {
            for (Link value: values) {
                getLinks().add(value);
            }
        }
        return this;
    }

    public TestResult withLinks(Collection<Link> values) {
        if (values!= null) {
            getLinks().addAll(values);
        }
        return this;
    }

    public TestResult withLinks(List<Link> links) {
        setLinks(links);
        return this;
    }

    @Override
    public TestResult withName(String value) {
        setName(value);
        return this;
    }

    @Override
    public TestResult withStatus(Status value) {
        setStatus(value);
        return this;
    }

    @Override
    public TestResult withStatusDetails(StatusDetails value) {
        setStatusDetails(value);
        return this;
    }

    @Override
    public TestResult withStage(Stage value) {
        setStage(value);
        return this;
    }

    @Override
    public TestResult withDescription(String value) {
        setDescription(value);
        return this;
    }

    @Override
    public TestResult withDescriptionHtml(String value) {
        setDescriptionHtml(value);
        return this;
    }

    @Override
    public TestResult withStart(Long value) {
        setStart(value);
        return this;
    }

    @Override
    public TestResult withStop(Long value) {
        setStop(value);
        return this;
    }

    @Override
    public TestResult withSteps(StepResult... values) {
        if (values!= null) {
            for (StepResult value: values) {
                getSteps().add(value);
            }
        }
        return this;
    }

    @Override
    public TestResult withSteps(Collection<StepResult> values) {
        if (values!= null) {
            getSteps().addAll(values);
        }
        return this;
    }

    @Override
    public TestResult withSteps(List<StepResult> steps) {
        setSteps(steps);
        return this;
    }

    @Override
    public TestResult withAttachments(Attachment... values) {
        if (values!= null) {
            for (Attachment value: values) {
                getAttachments().add(value);
            }
        }
        return this;
    }

    @Override
    public TestResult withAttachments(Collection<Attachment> values) {
        if (values!= null) {
            getAttachments().addAll(values);
        }
        return this;
    }

    @Override
    public TestResult withAttachments(List<Attachment> attachments) {
        setAttachments(attachments);
        return this;
    }

    @Override
    public TestResult withParameters(Parameter... values) {
        if (values!= null) {
            for (Parameter value: values) {
                getParameters().add(value);
            }
        }
        return this;
    }

    @Override
    public TestResult withParameters(Collection<Parameter> values) {
        if (values!= null) {
            getParameters().addAll(values);
        }
        return this;
    }

    @Override
    public TestResult withParameters(List<Parameter> parameters) {
        setParameters(parameters);
        return this;
    }

}
