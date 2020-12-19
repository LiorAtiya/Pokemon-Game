package gameClient;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class LoginGameFrame extends JFrame implements ActionListener {

//    private static JComboBox chooseField;

    public LoginGameFrame() {
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("data/pictures/agent.png"));
        initBackgroundImage();
        initLoginGame();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initBackgroundImage() {
        try {
            this.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("data/pictures/login.jpg")))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.pack();
        windowCenter();
    }

    private void initLoginGame() {

        JLabel id = new JLabel();
        id.setText("ID :");
        id.setBounds(85, 120, 100, 100);
        JTextField textfieldID = new JTextField();
        textfieldID.setBounds(110, 155, 130, 30);

        JLabel choose = new JLabel();
        choose.setText("Choose level :");
        choose.setBounds(20, 170, 100, 100);
        JTextField textfieldLevel = new JTextField();
        textfieldLevel.setBounds(110, 205, 130, 30);
//        String[] levels = new String[24];
//        for (int i = 0; i < levels.length; i++) {
//            levels[i] = "" + i;
//        }
//        chooseField = new JComboBox(levels);
//        chooseField.addActionListener(this);
//        chooseField.setBounds(110, 100, 130, 30);

        JButton buttonStart = new JButton("Start");
        buttonStart.setBounds(90, 270, 90, 30);

        this.add(id);
        this.add(textfieldID);
        this.add(choose);
        this.add(textfieldLevel);
        this.add(buttonStart);
        this.setSize(280, 470);
        this.setLayout(null);
        this.setVisible(true);

        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                int level = Integer.parseInt((String) (chooseField.getSelectedItem()));
                String sID = textfieldID.getText();
                String sLevel = textfieldLevel.getText();
                if (!sID.matches("[0-9]+") || !sLevel.matches("[0-9]+") || sID.length() == 0) {
                    JOptionPane.showMessageDialog(new LoginGameFrame(), "Invalid ID");
                } else {
                    int level = Integer.parseInt(sLevel);
                    int id = Integer.parseInt(sID);
                    Thread client = new Thread(new MainGame(id, level));
                    client.start();
                }
                dispose();
            }
        });

    }

    private void windowCenter() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
        this.setLocation(x, y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
