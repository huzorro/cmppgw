package com.enorbus.sms.gw.cmpp.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Service {
    public static final int FEETYPE_FREE = 1;      		// 对“计费用户号码”免费
    public static final int FEETYPE_ITEM_BY_ITEM = 2;   // 对“计费用户号码”按条计信息费
    public static final int FEETYPE_BY_THE_MONTH = 3;   // 对“计费用户号码”按包月收取信息费
    
	private Long id;
	private String serviceCode;
	private int feeType;
	private int feeCode;
	private Provider provider;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public int getFeeType() {
		return feeType;
	}
	public void setFeeType(int feeType) {
		this.feeType = feeType;
	}
	public int getFeeCode() {
		return feeCode;
	}
	public void setFeeCode(int feeCode) {
		this.feeCode = feeCode;
	}
	public void setProvider(Provider provider) {
		this.provider = provider;
	}
	public Provider getProvider() {
		return provider;
	}
	
	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof Service)) {
			return false;
		}
		Service rhs = (Service) object;
		return new EqualsBuilder().appendSuper(super.equals(object))
				.append(this.serviceCode, rhs.serviceCode)
				.append(this.provider.getId(), rhs.provider.getId()).isEquals();
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-2038111737, 1780350343).appendSuper(
				super.hashCode()).append(this.serviceCode)
				.append(this.provider.getId())
				.toHashCode();
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("provider", this.provider)
				.append("feeCode", this.feeCode)
				.append("feeType", this.feeType).append("serviceCode",
						this.serviceCode).append("id", this.id).toString();
	}
	
}
