public class Test {
    Integer id_test;
    String test_name;
    Integer restricted;

    @Override
    public String toString() {
        return "Test{" +
                "id_test=" + id_test +
                ", test_name='" + test_name + '\'' +
                ", restricted=" + restricted +
                '}';
    }

    public String getTest_name() {
        return test_name;
    }

    public Integer getId_test() {
        return id_test;
    }
}

