
package com.yq.allure2_android.model;

import java.io.Serializable;


/**
 * <p>Java class for StatusDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * 
 */
public class StatusDetails implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected boolean known;
    protected boolean muted;
    protected boolean flaky;
    protected String message;
    protected String trace;

    /**
     * Gets the value of the known property.
     * 
     */
    public boolean isKnown() {
        return known;
    }

    /**
     * Sets the value of the known property.
     * 
     */
    public void setKnown(boolean value) {
        this.known = value;
    }

    /**
     * Gets the value of the muted property.
     * 
     */
    public boolean isMuted() {
        return muted;
    }

    /**
     * Sets the value of the muted property.
     * 
     */
    public void setMuted(boolean value) {
        this.muted = value;
    }

    /**
     * Gets the value of the flaky property.
     * 
     */
    public boolean isFlaky() {
        return flaky;
    }

    /**
     * Sets the value of the flaky property.
     * 
     */
    public void setFlaky(boolean value) {
        this.flaky = value;
    }

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * Gets the value of the trace property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrace() {
        return trace;
    }

    /**
     * Sets the value of the trace property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrace(String value) {
        this.trace = value;
    }

    public StatusDetails withKnown(boolean value) {
        setKnown(value);
        return this;
    }

    public StatusDetails withMuted(boolean value) {
        setMuted(value);
        return this;
    }

    public StatusDetails withFlaky(boolean value) {
        setFlaky(value);
        return this;
    }

    public StatusDetails withMessage(String value) {
        setMessage(value);
        return this;
    }

    public StatusDetails withTrace(String value) {
        setTrace(value);
        return this;
    }

}
