package Interface_GUI;

import javax.swing.*;

import Client_CM.Controller;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class AdminDialog extends JDialog{

    JTextField idtf = new JTextField("",12);
    JTextField nicknametf = new JTextField("",12);
    JPasswordField pwtf = new JPasswordField("",12);
    JButton ok = new JButton("OK");
    Container pane = this.getContentPane();

    public AdminDialog() {
        this.setSize(330,160);
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(Color.white);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        init();
        this.setVisible(true);
    }
    public void init() {
        pwtf.setEchoChar('*');
        pane.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
        JLabel idLabel = new JLabel("ID:");
        idLabel.setPreferredSize(new Dimension(80,20));
        pane.add(idLabel);
        pane.add(idtf);
        ok.setForeground(Color.white);
        ok.setBackground(new Color(120,80,80));
        pane.add(ok);
        JLabel pwLabel = new JLabel("PW:");
        pwLabel.setPreferredSize(new Dimension(80,20));
        pane.add(pwLabel);
        pane.add(pwtf);
        JLabel nicknameLabel = new JLabel("nickname:");
        nicknameLabel.setPreferredSize(new Dimension(80,20));
        pane.add(nicknameLabel);
        pane.add(nicknametf);
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
            	Controller controller = new Controller(getID(),getNickname(),getPW());
            	dispose();
            }
        });
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // TODO Auto-generated method stub
                super.windowClosing(e);
                idtf.setText("");
                pwtf.setText("");
                dispose();
            }
        });
    }
    public String getID() {
        if(idtf.getText().length() == 0)
            return null;
        else
            return idtf.getText();
    }
    public String getPW() {
        String str = String.valueOf(pwtf.getPassword());
        if(str.length() == 0)
            return "";
        else
            return str;
    }
    public String getNickname() {
    	 if(nicknametf.getText().length() == 0)
             return null;
         else
             return nicknametf.getText();
    }

}
