package bus.u.gpsudh.user;

public class UserResponse {
    public int id;
    public String username;
    public String type;
    public String status;

    public UserResponse(String username, String status,int id, String type) {
        this.id = id;
        this.username = username;
        this.type = type;
        this.status = status;
    }

    public String getUsername() { return username; }

    public int getId() { return id; }

    public String getType() { return type; }

    public String getStatus() { return status; }
}
