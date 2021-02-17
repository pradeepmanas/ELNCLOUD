package com.agaram.eln.primary.model.protocols;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="LSprotocolmaster")
public class LSprotocolmaster {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer protocolmastercode;
	@Column(columnDefinition = "varchar(120)")
	public String protocolmastername;
	public Integer protocolstatus;
	public Integer status;
	public Integer createdby;
//	@Column(columnDefinition = "date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdate;
//	@ManyToOne
//	private LSSiteMaster lssitemaster;
	public Integer sharewithteam = 0;
	@Column(columnDefinition = "varchar(120)")
	public String createdbyusername;
	@Column(name="lssitemaster_sitecode")
	private Integer lssitemaster;
	
	public Integer getLssitemaster() {
		return lssitemaster;
	}
	public void setLssitemaster(Integer lssitemaster) {
		this.lssitemaster = lssitemaster;
	}
	public Integer getProtocolmastercode() {
		return protocolmastercode;
	}
	public void setProtocolmastercode(Integer protocolmastercode) {
		this.protocolmastercode = protocolmastercode;
	}
	public String getProtocolmastername() {
		return protocolmastername;
	}
	public void setProtocolmastername(String protocolmastername) {
		this.protocolmastername = protocolmastername;
	}
	public Integer getProtocolstatus() {
		return protocolstatus;
	}
	public void setProtocolstatus(Integer protocolstatus) {
		this.protocolstatus = protocolstatus;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
//	public LSSiteMaster getLssitemaster() {
//		return lssitemaster;
//	}
//	public void setLssitemaster(LSSiteMaster lssitemaster) {
//		this.lssitemaster = lssitemaster;
//	}
	public Integer getCreatedby() {
		return createdby;
	}
	public void setCreatedby(Integer createdby) {
		this.createdby = createdby;
	}
	public Integer getSharewithteam() {
		return sharewithteam;
	}
	public void setSharewithteam(Integer sharewithteam) {
		this.sharewithteam = sharewithteam;
	}
	public String getCreatedbyusername() {
		return createdbyusername;
	}
	public void setCreatedbyusername(String createdbyusername) {
		this.createdbyusername = createdbyusername;
	}
	
	
}
