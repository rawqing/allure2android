
package com.yq.allure2_androidj.model;

import java.io.Serializable;

public class TestRunResult implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected String uuid;
    protected String name;

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

    public TestRunResult withUuid(String value) {
        setUuid(value);
        return this;
    }

    public TestRunResult withName(String value) {
        setName(value);
        return this;
    }

}
