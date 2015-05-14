package model;

public class Camera {

	private Long id;
	private String name;
	private String description;
	private String url;
	private String path;
	
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
    
    public String get_description() {
        return description;
    }

    public void set_description(String description) {
        this.description = description;
    }
    
    public String get_url() {
        return url;
    }

    public void set_url(String url) {
        this.url = url;
    }
    
    public String get_path() {
        return path;
    }

    public void set_path(String path) {
        this.path = path;
    }
}
