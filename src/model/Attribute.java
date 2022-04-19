package model;

import java.io.Serializable;
import java.util.Date;

public class Attribute implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8674498213739548517L;
	
	private String attribute_name;
	private String text_en, text_tc, text_sc;
	private String value;
	private Date createDate;
	private Date eff_date;
	private Date updateDate;
	
	public Attribute(String attribute_name, String text_en,
			String text_tc, String text_sc, String value, Date createDate, Date eff_date, Date updateDate) {
		super();
		this.attribute_name = attribute_name;
		this.text_en = text_en;
		this.text_tc = text_tc;
		this.text_sc = text_sc;
		this.value = value;
		this.createDate = createDate;
		this.eff_date = eff_date;
		this.updateDate = updateDate;
	}

	public String getAttribute_name() {
		return attribute_name;
	}

	public void setAttribute_name(String attribute_name) {
		this.attribute_name = attribute_name;
	}

	public String getText_en() {
		return text_en;
	}

	public void setText_en(String text_en) {
		this.text_en = text_en;
	}

	public String getText_tc() {
		return text_tc;
	}

	public void setText_tc(String text_tc) {
		this.text_tc = text_tc;
	}

	public String getText_sc() {
		return text_sc;
	}

	public void setText_sc(String text_sc) {
		this.text_sc = text_sc;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getEff_date() {
		return eff_date;
	}

	public void setEff_date(Date eff_date) {
		this.eff_date = eff_date;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}


	
	
}
