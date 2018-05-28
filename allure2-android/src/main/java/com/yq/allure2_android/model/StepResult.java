
package com.yq.allure2_android.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class StepResult
    extends ExecutableItem
    implements Serializable
{

    private final static long serialVersionUID = 1L;

    @Override
    public StepResult withName(String value) {
        setName(value);
        return this;
    }

    @Override
    public StepResult withStatus(Status value) {
        setStatus(value);
        return this;
    }

    @Override
    public StepResult withStatusDetails(StatusDetails value) {
        setStatusDetails(value);
        return this;
    }

    @Override
    public StepResult withStage(Stage value) {
        setStage(value);
        return this;
    }

    @Override
    public StepResult withDescription(String value) {
        setDescription(value);
        return this;
    }

    @Override
    public StepResult withDescriptionHtml(String value) {
        setDescriptionHtml(value);
        return this;
    }

    @Override
    public StepResult withStart(Long value) {
        setStart(value);
        return this;
    }

    @Override
    public StepResult withStop(Long value) {
        setStop(value);
        return this;
    }

    @Override
    public StepResult withSteps(StepResult... values) {
        if (values!= null) {
            for (StepResult value: values) {
                getSteps().add(value);
            }
        }
        return this;
    }

    @Override
    public StepResult withSteps(Collection<StepResult> values) {
        if (values!= null) {
            getSteps().addAll(values);
        }
        return this;
    }

    @Override
    public StepResult withSteps(List<StepResult> steps) {
        setSteps(steps);
        return this;
    }

    @Override
    public StepResult withAttachments(Attachment... values) {
        if (values!= null) {
            for (Attachment value: values) {
                getAttachments().add(value);
            }
        }
        return this;
    }

    @Override
    public StepResult withAttachments(Collection<Attachment> values) {
        if (values!= null) {
            getAttachments().addAll(values);
        }
        return this;
    }

    @Override
    public StepResult withAttachments(List<Attachment> attachments) {
        setAttachments(attachments);
        return this;
    }

    @Override
    public StepResult withParameters(Parameter... values) {
        if (values!= null) {
            for (Parameter value: values) {
                getParameters().add(value);
            }
        }
        return this;
    }

    @Override
    public StepResult withParameters(Collection<Parameter> values) {
        if (values!= null) {
            getParameters().addAll(values);
        }
        return this;
    }

    @Override
    public StepResult withParameters(List<Parameter> parameters) {
        setParameters(parameters);
        return this;
    }

}
