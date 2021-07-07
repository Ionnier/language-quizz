import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class Exercise
{
    private Integer id_exercise;
    private Integer id_problem;
    private String content;
    private Double mark;

    static JPanel toPanel(Exercise exercitiu){
        JPanel jPanel = new JPanel();
        jPanel.setSize(new Dimension(100,100));
        // (\$BLANK\([0-9]*\))
        // (\\$BLANK)
        exercitiu.content=" "+exercitiu.content+" ";
        String [] cuvinte = exercitiu.content.split("((\\n)*\\$BLANK(\\([0-9]*\\))*)");
        LinkedList<JTextField> list = new LinkedList<>();
        for(int i=0;i<cuvinte.length;i++){
            jPanel.add(new JLabel(cuvinte[i]));
            if(i!=cuvinte.length-1){
                list.add(new JTextField("",5));
                jPanel.add(list.getLast());
            }
        }
        jPanel.setVisible(true);
        return  jPanel;
    }
    static String constructJson(Integer id, String answer){
        StringBuilder stringBuilder = new StringBuilder("{");
        stringBuilder.append("\"id_exercise\": "+id);
        stringBuilder.append(",");
        stringBuilder.append("\"content\": \""+answer+"\"");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public Integer getId_exercise() {
        return id_exercise;
    }
}