//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.03.30 at 03:35:08 PM CEST 
//


package it.mibac.san.eac_san;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://san.mibac.it/eac-san/}existDates"/>
 *         &lt;element ref="{http://san.mibac.it/eac-san/}placeDates" minOccurs="0"/>
 *         &lt;element ref="{http://san.mibac.it/eac-san/}descriptiveEntries" minOccurs="0"/>
 *         &lt;element ref="{http://san.mibac.it/eac-san/}biogHist" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "existDates",
    "placeDates",
    "descriptiveEntries",
    "biogHist"
})
@XmlRootElement(name = "description")
public class Description {

    @XmlElement(required = true)
    protected ExistDates existDates;
    protected PlaceDates placeDates;
    protected DescriptiveEntries descriptiveEntries;
    protected BiogHist biogHist;

    /**
     * Gets the value of the existDates property.
     * 
     * @return
     *     possible object is
     *     {@link ExistDates }
     *     
     */
    public ExistDates getExistDates() {
        return existDates;
    }

    /**
     * Sets the value of the existDates property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExistDates }
     *     
     */
    public void setExistDates(ExistDates value) {
        this.existDates = value;
    }

    /**
     * Gets the value of the placeDates property.
     * 
     * @return
     *     possible object is
     *     {@link PlaceDates }
     *     
     */
    public PlaceDates getPlaceDates() {
        return placeDates;
    }

    /**
     * Sets the value of the placeDates property.
     * 
     * @param value
     *     allowed object is
     *     {@link PlaceDates }
     *     
     */
    public void setPlaceDates(PlaceDates value) {
        this.placeDates = value;
    }

    /**
     * Gets the value of the descriptiveEntries property.
     * 
     * @return
     *     possible object is
     *     {@link DescriptiveEntries }
     *     
     */
    public DescriptiveEntries getDescriptiveEntries() {
        return descriptiveEntries;
    }

    /**
     * Sets the value of the descriptiveEntries property.
     * 
     * @param value
     *     allowed object is
     *     {@link DescriptiveEntries }
     *     
     */
    public void setDescriptiveEntries(DescriptiveEntries value) {
        this.descriptiveEntries = value;
    }

    /**
     * Gets the value of the biogHist property.
     * 
     * @return
     *     possible object is
     *     {@link BiogHist }
     *     
     */
    public BiogHist getBiogHist() {
        return biogHist;
    }

    /**
     * Sets the value of the biogHist property.
     * 
     * @param value
     *     allowed object is
     *     {@link BiogHist }
     *     
     */
    public void setBiogHist(BiogHist value) {
        this.biogHist = value;
    }

}
