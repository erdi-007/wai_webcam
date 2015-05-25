package model;

public class User {
	
	private Long id;
	private String name;
	private String password;
	private boolean admin;
	
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean isAdmin() {
    	return admin;
    }
    
    public void setAdmin(boolean admin) {
    	this.admin = admin;
    }
}
