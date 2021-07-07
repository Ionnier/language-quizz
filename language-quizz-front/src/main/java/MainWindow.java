import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class MainWindow {

    static JFrame jFrame = new JFrame("Language Quizz");

    static void setMenu(){
        JPanel jPanel = new JPanel();
        jPanel.setSize(jFrame.getSize());
        JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,null,null);
        jSplitPane.setLeftComponent(setLeftPanelMainMenu());
        jSplitPane.setRightComponent(setRightPanelMainMenu());
        jSplitPane.setSize(jPanel.getSize());
        jPanel.add(jSplitPane);
        jPanel.setVisible(true);
        jFrame.setContentPane(jPanel);
        jFrame.setVisible(true);
    }

    static JPanel setLeftPanelMainMenu(){
        JPanel jPanel = new JPanel();
        jPanel.add(new JLabel("Learn"));
        JButton jButton = new JButton("Take me to the lessons");
        jButton.addActionListener(e -> setLessonScreen());
        jPanel.add(jButton);
        jPanel.setVisible(true);
        return jPanel;
    }

    static void setLessonScreen(){
        JPanel jPanel = new JPanel();
        jPanel.setSize(jFrame.getSize());
        jPanel.setLayout(new BorderLayout());

        JButton backbutton = new JButton("Go back to Main Menu");
        backbutton.addActionListener(e -> setMenu());
        jPanel.add(backbutton,BorderLayout.SOUTH);

        JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,null,null);
        jPanel.add(jSplitPane,BorderLayout.CENTER);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DefaultListModel<String> model = new DefaultListModel<>();
                    JList<String> jList = new JList<>(model);
                    jSplitPane.setLeftComponent(new JScrollPane(jList));
                    String json = Utils.run(Utils.apiLink+"api/v1/lessons");
                    Moshi moshi = new Moshi.Builder().build();
                    Type type = Types.newParameterizedType(List.class,Lesson.class);
                    JsonAdapter<List<Lesson>> adapter = moshi.adapter(type);
                    List<Lesson> lessons = adapter.fromJson(json);
                    lessons.stream().map(Lesson::getLesson_name).forEach(model::addElement);
                    jList.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) {
                            if (e.getClickCount() == 2) {
                                int selectedIndex = jList.getSelectedIndex();
                                jSplitPane.setRightComponent(Lesson.toPanel(lessons.get(selectedIndex)));
                                jSplitPane.setEnabled(true);
                            }
                        }
                    });
                }
                catch(IOException ioException){
                    ioException.printStackTrace();
                }
            }
        });
        thread.start();
        jPanel.setVisible(true);
        jPanel.revalidate();
        jPanel.repaint();
        jFrame.setVisible(true);
        jFrame.setContentPane(jPanel);
    }

    static JPanel setRightPanelMainMenu(){
        JPanel jPanel = new JPanel();
        jPanel.add(new JLabel("Practice"));
        JButton jButton = new JButton("Take me to the tests");
        jButton.addActionListener(e -> setTestsScreen());
        jPanel.add(jButton);
        jPanel.setVisible(true);
        return jPanel;
    }

    static void setTestsScreen(){
        JPanel jPanel = new JPanel();
        jPanel.setSize(jFrame.getSize());
        jPanel.setLayout(new BorderLayout());

        JButton backbutton = new JButton("Go back to Main Menu");
        backbutton.addActionListener(e -> setMenu());
        JPanel jPanel1 = new JPanel();
        jPanel1.add(backbutton);

        JButton jButton = new JButton("Get test list");
        jButton.addActionListener(e -> {
            BorderLayout layout = (BorderLayout)jPanel.getLayout();
            jPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
            jPanel.add(setTestListScreen(),BorderLayout.CENTER);
            jPanel.revalidate();
            jPanel.repaint();
        });
        JButton jButton1 = new JButton("Get random test");

        jPanel1.add(jButton);
        jPanel1.add(jButton1);


        jPanel.add(setTestListScreen(),BorderLayout.CENTER);
        jPanel.add(jPanel1,BorderLayout.SOUTH);


        JPanel jPanel3 = new JPanel();
        if(Utils.user==null){
            JButton jButton2 = new JButton("Log in");
            jButton2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JTextField username = new JTextField();
                    JTextField password = new JPasswordField();
                    Object[] message = {
                            "Email:", username,
                            "Password:", password
                    };

                    int option = JOptionPane.showConfirmDialog(null, message, "Login", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        try {
                            String json = Utils.apiLink + "api/v1/login";
                            System.out.println("{ \"email\": \"" + username.getText() + "\",\"password\":\"" + password.getText() + "\"}");
                            json = Utils.postJSON(json, "{ \"email\": \"" + username.getText() + "\",\"password\":\"" + password.getText() + "\"}");
                            JsonAdapter<User> adapter = Utils.moshi.adapter(User.class);
                            User user =adapter.lenient().fromJson(json);
                            if(user.id_user==null){
                                JOptionPane.showConfirmDialog(null,"Incorect","Message",JOptionPane.OK_OPTION);
                            } else{
                                Utils.user = user;
                            }
                            System.out.println(Utils.user);
                        }catch (IOException ioException){
                            ioException.printStackTrace();
                        }
                    }
                }
            });
            jPanel3.add(jButton2);
        } else{
            jPanel3.add(new JLabel(Utils.user.get_name()));
        }

        jPanel.add(jPanel3,BorderLayout.NORTH);

        jPanel.revalidate();
        jPanel.repaint();
        jPanel.setVisible(true);
        jFrame.setVisible(true);
        jFrame.setContentPane(jPanel);
    }

    static JPanel setTestListScreen(){
        JPanel jPanel = new JPanel();
        try {
            String json=Utils.apiLink+"api/v1/tests";
            if(Utils.user!=null){
                json+=("?id_user="+Utils.user.getId_user());
            }
            json=Utils.run(json);
            Type type = Types.newParameterizedType(List.class,Test.class);
            JsonAdapter<List<Test>> adapter = Utils.moshi.adapter(type);
            List<Test> tests = adapter.nullSafe().fromJson(json);
            tests.stream().forEach(t->{
                JPanel jPanel2 = new JPanel();
                jPanel2.add(new JLabel(t.getTest_name()));
                JButton jButton = new JButton("Practice");
                jButton.addActionListener(e -> {
                    try {
                        jPanel.removeAll();
                        String json1 = Utils.run(Utils.apiLink+"api/v1/tests/"+t.getId_test());
                        Type type1 = Types.newParameterizedType(List.class,Problem.class);
                        JsonAdapter<List<Problem>> adapter1 = Utils.moshi.adapter(type1);
                        List<Problem> problems = adapter1.nullSafe().fromJson(json1);
                        jPanel.add(setTestScreen(problems));
                        jPanel.setSize(new Dimension(300,300));
                        jPanel.revalidate();
                        jPanel.repaint();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

                });
                JButton jButton1 = new JButton("Go to test");
                jButton1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(Utils.user==null)
                            JOptionPane.showMessageDialog(jFrame,"Not signed in");
                    }
                });
                jPanel2.add(jButton);
                jPanel2.add(jButton1);
                jPanel.add(jPanel2);
            });
            System.out.println(tests);

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return jPanel;
    }

    static JPanel setTestScreen(List<Problem> problems){
        JPanel jPanel = new JPanel();
        jPanel.setSize(new Dimension(300,300));
        problems.stream().forEach(t->{
            jPanel.add(Problem.toPanel2(t));
        });
        return jPanel;
    }
    public static void main(String[] args) throws IOException {
        jFrame.setSize(new Dimension(800,800));
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setMenu();
        jFrame.setVisible(true);
    }
}
