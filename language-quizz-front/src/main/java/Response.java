public class Response {
    Integer id_exercise;
    String content;
    Double percentage;
    Double grade;

    @Override
    public String toString() {
        return "Response{" +
                "id_exercise=" + id_exercise +
                ", content='" + content + '\'' +
                ", percentage=" + percentage +
                ", grade=" + grade +
                '}';
    }
}
