package model;

public class DataDetail {
	private int master_id;
	private String field_name;
	private String field_value;	
	private int step_id;
	private int section_id;
	private String encrypted;
	
	public DataDetail() {		
		this.master_id = -1;
		this.field_name = "";
		this.field_value = "";
		this.step_id = -1;
		this.section_id = -1;
		this.encrypted = "";
	}
	
	public DataDetail(int master_id, String field_name, String field_value, int step_id, int section_id) {
		super();
		this.master_id = master_id;
		this.field_name = field_name;
		this.field_value = field_value;
		this.step_id = step_id;
		this.section_id = section_id;
	}
	
	public DataDetail(int master_id, String field_name, String field_value, int step_id, int section_id, String encrypted) {
		super();
		this.master_id = master_id;
		this.field_name = field_name;
		this.field_value = field_value;
		this.step_id = step_id;
		this.section_id = section_id;
		this.encrypted = encrypted;
	}

	public int getMaster_id() {
		return master_id;
	}

	public void setMaster_id(int master_id) {
		this.master_id = master_id;
	}

	public String getField_name() {
		return field_name;
	}

	public void setField_name(String field_name) {
		this.field_name = field_name;
	}

	public String getField_value() {
		return field_value;
	}

	public void setField_value(String field_value) {
		this.field_value = field_value;
	}

	public int getStep_id() {
		return step_id;
	}

	public void setStep_id(int step_id) {
		this.step_id = step_id;
	}

	public int getSection_id() {
		return section_id;
	}

	public void setSection_id(int section_id) {
		this.section_id = section_id;
	}

	public String getEncrypted() {
		return encrypted;
	}

	public void setEncrypted(String encrypted) {
		this.encrypted = encrypted;
	}
	
	
	
}
