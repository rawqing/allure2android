
package com.yq.allure2_androidj.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class FixtureResult
    extends ExecutableItem
    implements Serializable
{

    private final static long serialVersionUID = 1L;

    @Override
    public FixtureResult withName(String value) {
        setName(value);
        return this;
    }

    @Override
    public FixtureResult withStatus(Status value) {
        setStatus(value);
        return this;
    }

    @Override
    public FixtureResult withStatusDetails(StatusDetails value) {
        setStatusDetails(value);
        return this;
    }

    @Override
    public FixtureResult withStage(Stage value) {
        setStage(value);
        return this;
    }

    @Override
    public FixtureResult withDescription(String value) {
        setDescription(value);
        return this;
    }

    @Override
    public FixtureResult withDescriptionHtml(String value) {
        setDescriptionHtml(value);
        return this;
    }

    @Override
    public FixtureResult withStart(Long value) {
        setStart(value);
        return this;
    }

    @Override
    public FixtureResult withStop(Long value) {
        setStop(value);
        return this;
    }

    @Override
    public FixtureResult withSteps(StepResult... values) {
        if (values!= null) {
            for (StepResult value: values) {
                getSteps().add(value);
            }
        }
        return this;
    }

    @Override
    public FixtureResult withSteps(Collection<StepResult> values) {
        if (values!= null) {
            getSteps().addAll(values);
        }
        return this;
    }

    @Override
    public FixtureResult withSteps(List<StepResult> steps) {
        setSteps(steps);
        return this;
    }

    @Override
    public FixtureResult withAttachments(Attachment... values) {
        if (values!= null) {
            for (Attachment value: values) {
                getAttachments().add(value);
            }
        }
        return this;
    }

    @Override
    public FixtureResult withAttachments(Collection<Attachment> values) {
        if (values!= null) {
            getAttachments().addAll(values);
        }
        return this;
    }

    @Override
    public FixtureResult withAttachments(List<Attachment> attachments) {
        setAttachments(attachments);
        return this;
    }

    @Override
    public FixtureResult withParameters(Parameter... values) {
        if (values!= null) {
            for (Parameter value: values) {
                getParameters().add(value);
            }
        }
        return this;
    }

    @Override
    public FixtureResult withParameters(Collection<Parameter> values) {
        if (values!= null) {
            getParameters().addAll(values);
        }
        return this;
    }

    @Override
    public FixtureResult withParameters(List<Parameter> parameters) {
        setParameters(parameters);
        return this;
    }

}
