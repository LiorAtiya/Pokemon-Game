package gameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGameFrame extends JFrame implements ActionListener {

    private static JComboBox chooseField;

    public LoginGameFrame() {
        initLoginGame();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initLoginGame() {

        JLabel id = new JLabel();
        id.setText("ID :");
        id.setBounds(80, 13, 100, 100);
        JTextField textfieldID = new JTextField();
        textfieldID.setBounds(110, 50, 130, 30);

        JLabel choose = new JLabel();
        choose.setText("Choose level :");
        choose.setBounds(20, 63, 100, 100);
        String[] levels = new String[24];
        for (int i = 0; i < levels.length; i++) {
            levels[i] = "" + i;
        }
        chooseField = new JComboBox(levels);
        chooseField.addActionListener(this);
        chooseField.setBounds(110, 100, 130, 30);

        JButton buttonStart = new JButton("Start");
        buttonStart.setBounds(75, 170, 140, 40);

        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int level = Integer.parseInt((String) (chooseField.getSelectedItem()));
                String sID = textfieldID.getText();
                if (!sID.matches("[0-9]+") || sID.length() < 2) {
                    JOptionPane.showMessageDialog(new LoginGameFrame(), "Invalid ID");
                } else {
                    int id = Integer.parseInt(sID);
                    Thread client = new Thread(new MainGame(level, id));
                    client.start();
                }
                dispose();
            }
        });

        this.add(id);
        this.add(textfieldID);
        this.add(choose);
        this.add(chooseField);
        this.add(buttonStart);
        this.setSize(300, 300);
        this.setLayout(null);
        this.setVisible(true);
        windowCenter();
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
