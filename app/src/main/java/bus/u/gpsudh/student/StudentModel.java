package bus.u.gpsudh.student;


public class StudentModel {
    public int user_id;
    public double latitude;
    public double longitude;

    public StudentModel(int user_id) {
        this.user_id = user_id;
    }

    public StudentModel(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
