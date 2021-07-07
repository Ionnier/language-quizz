import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Problem
{
    private Integer id_problem;
    private Integer id_lesson;
    private Integer id_category;
    private String problem_text;
    private String category_name;
    private String category_description;
    private String lesson_name;
    private String lesson_description;
    List<Exercise> exerciseList;

    public void refreshExercises() throws IOException {
        refreshExercises(5);
    }
    public void refreshExercises(int limit) throws IOException {
        String json = Utils.run(Utils.apiLink+"api/v1/problems/"+id_problem);
        Moshi moshi = new Moshi.Builder().build();
        Type type = Types.newParameterizedType(List.class,Problem.class);
        JsonAdapter<List<Problem>> adapter = moshi.adapter(type);
        List<Problem> problems = adapter.fromJson(json);
        exerciseList=problems.get(0).exerciseList;
    }
    static JPanel toPanel2(Problem problem){
        JPanel jPanel = new JPanel();
        jPanel.setSize(new Dimension(100,100));
        jPanel.add(new JLabel(problem.problem_text));
        JScrollPane jScrollPane = new JScrollPane();
        problem.exerciseList.forEach(t->jPanel.add(Exercise.toPanel(t)));
        jPanel.add(jScrollPane);
        jScrollPane.setVisible(true);
        jPanel.setVisible(true);
        return jPanel;
    }
    static JPanel toPanel(Problem problem){
        JPanel jPanel = new JPanel();
        jPanel.add(new JLabel(problem.problem_text));
        JScrollPane jScrollPane = new JScrollPane();
        problem.exerciseList.forEach(t->jPanel.add(Exercise.toPanel(t)));
        jPanel.add(jScrollPane);
        jScrollPane.setVisible(true);
        JButton jButton = new JButton("Trimite raspunsuri");
        jPanel.add(jButton);
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> all_answers = new ArrayList<>();
                Component[] children = jPanel.getComponents();
                for (Component panel : children) {
                    if(panel instanceof JPanel){
                        Component[] children2 = ((JPanel) panel).getComponents();
                        List<String> answers = new ArrayList<>();
                        for (Component answer : children2) {
                            if (answer instanceof JTextField) {
                                answers.add(((JTextField) answer).getText());
                            }
                        }
                        all_answers.add(String.join(";", answers));
                    }
                }
                for(int i=0;i<all_answers.size();i++){
                    all_answers.set(i,Exercise.constructJson(problem.exerciseList.get(i).getId_exercise(),all_answers.get(i)));
                }
                try {
                    String json = Utils.postJSON(Utils.apiLink+"api/v1/responses","["+String.join(",", all_answers)+"]");
                    Moshi moshi = new Moshi.Builder().build();
                    Type type = Types.newParameterizedType(List.class,Response.class);
                    JsonAdapter<List<Response>> adapter = moshi.adapter(type);
                    List<Response> responses = adapter.fromJson(json);
                    int i=0;
                    for (Component panel : children) {
                        if(panel instanceof JPanel){
                            if(responses.get(i)==null || responses.get(0).percentage==null){
                                ((JPanel) panel).setBorder(BorderFactory.createLineBorder(Color.GRAY,3));
                            } else if (responses.get(i).percentage!=1){
                                    ((JPanel) panel).setBorder(BorderFactory.createLineBorder(Color.ORANGE,3));
                            } else {
                                ((JPanel) panel).setBorder(BorderFactory.createLineBorder(Color.GREEN,3));
                            }
                            i++;
                        }
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        jPanel.setVisible(true);
        return jPanel;
    }

    @Override
    public String toString() {
        return "Problem{" +
                "id_problem=" + id_problem +
                ", id_lesson=" + id_lesson +
                ", id_category=" + id_category +
                ", problem_text='" + problem_text + '\'' +
                ", category_name='" + category_name + '\'' +
                ", category_description='" + category_description + '\'' +
                ", lesson_name='" + lesson_name + '\'' +
                ", lesson_description='" + lesson_description + '\'' +
                ", exerciseList=" + exerciseList +
                '}';
    }

    public String getProblem_text() {
        return problem_text;
    }
}