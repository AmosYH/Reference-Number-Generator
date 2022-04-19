package model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
public class DataMaster {
	 
	private Timestamp create_date_time;
	private Timestamp update_date_time;
	private int master_id;
	private String ref_no;
	private String form_id;	
	private String ip_address;
	private String browser_agent;
	private String channel_code;
	private String submit_lang;
	private String channel;
	private String status;
	private HashMap<String, DataDetail> details; 
	
	public DataMaster() {
		this.master_id=-1;
		this.create_date_time = null;
		this.update_date_time = null;
		this.ref_no = "";
		this.form_id = "";
		this.ip_address = "";
		this.browser_agent = "";
		this.channel_code = "";
		this.submit_lang = "";
		this.channel = "";
		this.status = "";
		this.details = null;
	}
	
	

	public DataMaster(Timestamp create_date_time, Timestamp update_date_time, int master_id, String ref_no, String form_id, String ip_address, String browser_agent, String channel_code, String submit_lang, String channel, String status, HashMap<String, DataDetail> details) {
		super();
		this.create_date_time = create_date_time;
		this.update_date_time = update_date_time;
		this.master_id = master_id;
		this.ref_no = ref_no;
		this.form_id = form_id;
		this.ip_address = ip_address;
		this.browser_agent = browser_agent;
		this.channel_code = channel_code;
		this.submit_lang = submit_lang;
		this.channel = channel;
		this.status = status;
		this.details = details;
	}


		

	public int getMaster_id() {
		return master_id;
	}



	public void setMaster_id(int master_id) {
		this.master_id = master_id;
	}



	public Timestamp getCreate_date_time() {
		return create_date_time;
	}



	public void setCreate_date_time(Timestamp create_date_time) {
		this.create_date_time = create_date_time;
	}



	public Timestamp getUpdate_date_time() {
		return update_date_time;
	}



	public void setUpdate_date_time(Timestamp update_date_time) {
		this.update_date_time = update_date_time;
	}



	public String getRef_no() {
		return ref_no;
	}

	public void setRef_no(String ref_no) {
		this.ref_no = ref_no;
	}

	public String getForm_id() {
		return form_id;
	}

	public void setForm_id(String form_id) {
		this.form_id = form_id;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	public String getBrowser_agent() {
		return browser_agent;
	}

	public void setBrowser_agent(String browser_agent) {
		this.browser_agent = browser_agent;
	}

	public String getChannel_code() {
		return channel_code;
	}

	public void setChannel_code(String channel_code) {
		this.channel_code = channel_code;
	}

	public String getSubmit_lang() {
		return submit_lang;
	}

	public void setSubmit_lang(String submit_lang) {
		this.submit_lang = submit_lang;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public HashMap<String, DataDetail> getDetails() {
		return details;
	}

	public void setDetails(HashMap<String, DataDetail> details) {
		this.details = details;
	}
	
	
	
}
