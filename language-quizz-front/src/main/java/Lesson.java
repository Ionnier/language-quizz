import javax.swing.*;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class Lesson {
    private int id_lesson;
    private String lesson_name;
    private String lesson_description;
    public String getLesson_name() {
        return lesson_name;
    }
    public int getId_lesson() {
        return id_lesson;
    }


    static JPanel toPanel(Lesson lesson){
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        jPanel.add(new JLabel(lesson.lesson_name));
        jPanel.add(new JLabel(lesson.lesson_description));
        try {
            String json = Utils.run(Utils.apiLink + "api/v1/lessons/" + lesson.id_lesson);
            Type type = Types.newParameterizedType(List.class, Problem.class);
            JsonAdapter<List<Problem>> adapter = Utils.moshi.adapter(type);
            List<Problem> problems = adapter.fromJson(json);
            jPanel.add(new JLabel("Exercises:"));
            // TODO: add mouse listener
            problems.stream().forEach(t->{
                JLabel jLabel = new JLabel("<html><font color='blue'>"+t.getProblem_text()+"</font></html>");
                jLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            Dimension dimension = jPanel.getSize();
                            jPanel.removeAll();
                            jPanel.setMaximumSize(dimension);
                            t.refreshExercises();
                            jPanel.add(Problem.toPanel(t));
                            jPanel.revalidate();
                            jPanel.repaint();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                    public void mouseExited(MouseEvent e) {
                        jLabel.setText("<html><font color='blue'>"+t.getProblem_text()+"</font></html>");
                    }
                    public void mouseEntered(MouseEvent e) {
                        jLabel.setText("<html><font color='red'>"+t.getProblem_text()+"</font></html>");
                    }
                });
                jPanel.add(jLabel);
            });
        }
        catch (IOException ioException){
            ioException.printStackTrace();
        }
        jPanel.setVisible(true);
        return jPanel;
    }
}
