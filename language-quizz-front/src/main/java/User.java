public class User {
    Integer id_user;
    String email;
    String first_name;
    String last_name;
    public Integer getId_user() {
        return id_user;
    }

    public String get_name() {
        return first_name+" "+last_name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id_user=" + id_user +
                ", email='" + email + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                '}';
    }
}
