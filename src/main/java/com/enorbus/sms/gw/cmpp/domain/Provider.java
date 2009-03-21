package com.enorbus.sms.gw.cmpp.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Provider {
	public static final String PROVIDER_TYPE_SMS = "SMS";
	public static final String PROVIDER_TYPE_MMS = "MMS";
	
	private Long id;
	private String spId;
	private String serviceNumber;
	private String type;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSpId() {
		return spId;
	}
	public void setSpId(String spId) {
		this.spId = spId;
	}
	public String getServiceNumber() {
		return serviceNumber;
	}
	public void setServiceNumber(String serviceNumber) {
		this.serviceNumber = serviceNumber;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof Provider)) {
			return false;
		}
		Provider rhs = (Provider) object;
		return new EqualsBuilder().appendSuper(super.equals(object))
				.append(this.spId, rhs.spId).append(this.type, rhs.type)
				.isEquals();
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(157689219, -730861791).appendSuper(
				super.hashCode()).append(this.spId)
				.append(this.type).toHashCode();
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("type", this.type).append(
				"spId", this.spId).append("serviceNumber", this.serviceNumber)
				.append("id", this.id).toString();
	}
}
