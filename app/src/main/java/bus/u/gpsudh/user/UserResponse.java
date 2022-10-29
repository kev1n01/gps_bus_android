package bus.u.gpsudh.user;

public class UserResponse {
    public int id;
    public String username;
    public String password;
    public String type;
    public String status;

    public UserResponse(String username, String password, String status, int id, String type) {
        this.id = id;
        this.password = password;
        this.username = username;
        this.type = type;
        this.status = status;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() { return id; }

    public String getType() { return type; }

    public String getStatus() { return status; }
}
