package model;

public class User {
	
	private Long id;
	private String name;
	private String password;
	private boolean is_admin;
	
    public Long get_id() {
        return id;
    }

    public void set_id(Long id) {
        this.id = id;
    }

    public String get_name() {
        return name;
    }

    public void set_name(String name) {
        this.name = name;
    }
    
    public String get_password() {
        return password;
    }

    public void set_password(String password) {
        this.password = password;
    }
    
    public boolean get_admin_state() {
    	return is_admin;
    }
    
    public void set_admin_state(boolean is_admin) {
    	this.is_admin = is_admin;
    }
}
