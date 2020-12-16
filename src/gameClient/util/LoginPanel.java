//package gameClient.util;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//public class LoginPanel extends JPanel {
//
//    private JButton b;
//
//    public LoginPanel(){
//        initPanel();
//        centerWindow();
//    }
//
//    private void initPanel() {
//        this.setName("Login to game");
//        b = new JButton("Start");
//        b.setBounds(75, 170, 140, 40);
//        JLabel label = new JLabel();
//        label.setText("ID :");
//        label.setBounds(80, 13, 100, 100);
//        JLabel label1 = new JLabel();
//        label1.setBounds(10, 110, 200, 100);
//        JTextField textfield = new JTextField();
//        textfield.setBounds(110, 50, 130, 30);
//
//        JLabel label2 = new JLabel();
//        label2.setText("Choose level :");
//        label2.setBounds(20, 63, 100, 100);
//        JLabel label3 = new JLabel();
//        label3.setBounds(10, 140, 200, 100);
//        JTextField textfield2 = new JTextField();
//        textfield2.setBounds(110, 100, 130, 30);
//
//        add(label);
//        add(label1);
//        add(textfield);
//        add(label2);
//        add(label3);
//        add(textfield2);
//        add(b);
//        setSize(300, 300);
//        setLayout(null);
//        setVisible(true);
//    }
//
//    public void centerWindow(){
//        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
//        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
//        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
//        this.setLocation(x, y);
//    }
//
//
//    public int levelNumber() {
//            b.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent arg0) {
//                    String text = textfield2.getText();
//                    scenario_num = Integer.parseInt(text);
//                }
//            });
//            }
//            return 0;
//    }
