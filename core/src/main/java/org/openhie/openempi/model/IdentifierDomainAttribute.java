/**
 *
 * Copyright (C) 2002-2012 "SYSNET International, Inc."
 * support@sysnetint.com [http://www.sysnetint.com]
 *
 * This file is part of OpenEMPI.
 *
 * OpenEMPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.openhie.openempi.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.TableGenerator;
import javax.persistence.Table;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
/**
 * IdentifierDomainAttribute entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@Entity
@Table(name = "identifier_domain_attribute")
@GenericGenerator(name = "identifier_domain_attribute_gen", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
        @Parameter(name = "sequence_name", value = "identifier_domain_attribute_seq"),
        @Parameter(name = "optimizer", value = "hilo")}
    )
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class IdentifierDomainAttribute implements Serializable
{
	private static final long serialVersionUID = 2611151383140968220L;

	private Integer identifierDomainAttributeId;
	private Integer identifierDomainId;
	private String attributeName;
	private String attributeValue;
	
	/** default constructor */
	public IdentifierDomainAttribute() {
	}

	/** minimal constructor */
	public IdentifierDomainAttribute(Integer identifierDomainAttributeId, Integer identifierDomainId) {
		this.identifierDomainAttributeId = identifierDomainAttributeId;
		this.identifierDomainId = identifierDomainId;
	}

	/** full constructor */
	public IdentifierDomainAttribute(Integer identifierDomainId, String attributeName, String attributeValue) {
		this.identifierDomainId = identifierDomainId;
		this.attributeName = attributeName;
		this.attributeValue = attributeValue;
	}

	// Property accessors
	@Id
	@GeneratedValue(generator="identifier_domain_attribute_gen")
	@Column(name = "identifier_domain_attribute_id", unique = true, nullable = false)
	@XmlElement
	public Integer getIdentifierDomainAttributeId() {
		return identifierDomainAttributeId;
	}

	public void setIdentifierDomainAttributeId(Integer identifierDomainAttributeId) {
		this.identifierDomainAttributeId = identifierDomainAttributeId;
	}

	@Column(name = "identifier_domain_id", nullable = false)
	@XmlElement
	public Integer getIdentifierDomainId() {
		return identifierDomainId;
	}

	public void setIdentifierDomainId(Integer identifierDomainId) {
		this.identifierDomainId = identifierDomainId;
	}

	@Column(name = "attribute_name")
	@XmlElement
	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	@Column(name = "attribute_value")
	@XmlElement
	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof IdentifierDomainAttribute))
			return false;
		IdentifierDomainAttribute castOther = (IdentifierDomainAttribute) other;
		return new EqualsBuilder().append(identifierDomainAttributeId,
				castOther.identifierDomainAttributeId).append(
				identifierDomainId, castOther.identifierDomainId).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(identifierDomainAttributeId)
				.append(identifierDomainId).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("identifierDomainAttributeId",
				identifierDomainAttributeId).append("identifierDomainId",
				identifierDomainId).append("attributeName", attributeName)
				.append("attributeValue", attributeValue).toString();
	}	
}
