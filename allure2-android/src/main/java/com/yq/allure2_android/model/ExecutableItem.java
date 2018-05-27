
package com.yq.allure2_android.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.qameta.allure.model.Attachment;
import io.qameta.allure.model.Parameter;
import io.qameta.allure.model.Stage;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.model.WithAttachments;
import io.qameta.allure.model.WithParameters;
import io.qameta.allure.model.WithStatusDetails;
import io.qameta.allure.model.WithSteps;


/**
 * <p>Java class for ExecutableItem complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 *
 */

public abstract class ExecutableItem implements Serializable, WithAttachments, WithParameters, WithStatusDetails, WithSteps
{

    private final static long serialVersionUID = 1L;
    protected String name;
    protected String status;
    protected StatusDetails statusDetails;
    protected String stage;
    protected String description;
    protected String descriptionHtml;
    protected List<StepResult> steps;
    protected List<Attachment> attachments;
    protected List<Parameter> parameters;
    protected Long start;
    protected Long stop;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link Status }
     *     
     */
    public Status getStatus() {
        return status == null || "".equals(status)? null : Status.fromValue(status);
    }
    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link Status }
     *     
     */
    public void setStatus(Status value) {
        this.status = value.value();
    }

    /**
     * Gets the value of the statusDetails property.
     * 
     * @return
     *     possible object is
     *     {@link StatusDetails }
     *     
     */
    public StatusDetails getStatusDetails() {
        return statusDetails;
    }

    /**
     * Sets the value of the statusDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link StatusDetails }
     *     
     */
    public void setStatusDetails(StatusDetails value) {
        this.statusDetails = value;
    }

    /**
     * Gets the value of the stage property.
     * 
     * @return
     *     possible object is
     *     {@link Stage }
     *     
     */
    public Stage getStage() {
        return stage == null || "".equals(stage)? null : Stage.fromValue(stage);
    }

    /**
     * Sets the value of the stage property.
     * 
     * @param value
     *     allowed object is
     *     {@link Stage }
     *     
     */
    public void setStage(Stage value) {
        this.stage = value.value();
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the descriptionHtml property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescriptionHtml() {
        return descriptionHtml;
    }

    /**
     * Sets the value of the descriptionHtml property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescriptionHtml(String value) {
        this.descriptionHtml = value;
    }

    /**
     * Gets the value of the start property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getStart() {
        return start;
    }

    /**
     * Sets the value of the start property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setStart(Long value) {
        this.start = value;
    }

    /**
     * Gets the value of the stop property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getStop() {
        return stop;
    }

    /**
     * Sets the value of the stop property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setStop(Long value) {
        this.stop = value;
    }

    public List<StepResult> getSteps() {
        if (steps == null) {
            steps = new ArrayList<StepResult>();
        }
        return steps;
    }

    public void setSteps(List<StepResult> steps) {
        this.steps = steps;
    }

    public List<Attachment> getAttachments() {
        if (attachments == null) {
            attachments = new ArrayList<Attachment>();
        }
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public List<Parameter> getParameters() {
        if (parameters == null) {
            parameters = new ArrayList<Parameter>();
        }
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public ExecutableItem withName(String value) {
        setName(value);
        return this;
    }

    public ExecutableItem withStatus(Status value) {
        setStatus(value);
        return this;
    }

    public ExecutableItem withStatusDetails(StatusDetails value) {
        setStatusDetails(value);
        return this;
    }

    public ExecutableItem withStage(Stage value) {
        setStage(value);
        return this;
    }

    public ExecutableItem withDescription(String value) {
        setDescription(value);
        return this;
    }

    public ExecutableItem withDescriptionHtml(String value) {
        setDescriptionHtml(value);
        return this;
    }

    public ExecutableItem withStart(Long value) {
        setStart(value);
        return this;
    }

    public ExecutableItem withStop(Long value) {
        setStop(value);
        return this;
    }

    public ExecutableItem withSteps(StepResult... values) {
        if (values!= null) {
            for (StepResult value: values) {
                getSteps().add(value);
            }
        }
        return this;
    }

    public ExecutableItem withSteps(Collection<StepResult> values) {
        if (values!= null) {
            getSteps().addAll(values);
        }
        return this;
    }

    public ExecutableItem withSteps(List<StepResult> steps) {
        setSteps(steps);
        return this;
    }

    public ExecutableItem withAttachments(Attachment... values) {
        if (values!= null) {
            for (Attachment value: values) {
                getAttachments().add(value);
            }
        }
        return this;
    }

    public ExecutableItem withAttachments(Collection<Attachment> values) {
        if (values!= null) {
            getAttachments().addAll(values);
        }
        return this;
    }

    public ExecutableItem withAttachments(List<Attachment> attachments) {
        setAttachments(attachments);
        return this;
    }

    public ExecutableItem withParameters(Parameter... values) {
        if (values!= null) {
            for (Parameter value: values) {
                getParameters().add(value);
            }
        }
        return this;
    }

    public ExecutableItem withParameters(Collection<Parameter> values) {
        if (values!= null) {
            getParameters().addAll(values);
        }
        return this;
    }

    public ExecutableItem withParameters(List<Parameter> parameters) {
        setParameters(parameters);
        return this;
    }

}
