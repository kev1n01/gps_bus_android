package bus.u.gpsudh.student;

public class StudentResponse {

    public int id;
    public int user_id;
    public double latitude;
    public double longitude;

    public StudentResponse(int id, int user_id, double latitude, double longitude) {
        this.id = id;
        this.user_id = user_id;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
