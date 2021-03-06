package com.dc;

import java.io.Serializable;

public class Patient implements Serializable{

	
    private Integer id;    
    
    
    private Integer age;    
    
    
    private Integer discharged;    
    
   
    private String disease;
    
    
    private String name;
    
    
    private Integer room_no;
    
    
    private String status;
    
    
    private String email_id;
    
    
    private String created_at;
    
    
    private String updated_at;
    
    /**
     * Default Constructor
     */
    public Patient() {
        super();        
    }

    /**
     * Parameterized Constructor
     * @param id
     * @param name
     * @param age
     * @param salary
     */
    public Patient(Integer id, Integer age, Integer discharged,String disease, String name, Integer room_no, String status, String email_id, String created_at, String updated_at ) {
        super();
        this.id = id;
        this.age = age;
        this.discharged = discharged;
        this.disease = disease;
        this.name = name;
        this.room_no = room_no;
        this.status = status;
        this.email_id = email_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
    
    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getDischarged() {
		return discharged;
	}

	public void setDischarged(Integer discharged) {
		this.discharged = discharged;
	}

	public String getDisease() {
		return disease;
	}

	public void setDisease(String disease) {
		this.disease = disease;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getRoom_no() {
		return room_no;
	}

	public void setRoom_no(Integer room_no) {
		this.room_no = room_no;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEmail_id() {
		return email_id;
	}

	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}

	
	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Patient [id=" + id + ", name=" + name+ ", age=" + age+ ", discharged="+discharged +"]";
    } 

	
}
