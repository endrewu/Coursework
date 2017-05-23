import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


class Vindu extends JFrame {
    private JButton knapp;
    private Lytter knappelytter;

    public Vindu() {
        super("Hei test");
        Container samling = getContentPane();
        samling.setLayout(new FlowLayout());
        setSize(300,200);
        knapp = new JButton ("Hei");
        samling.add(knapp);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        knappelytter = new Lytter();
        knapp.addActionListener(knappelytter);
    }
}

class Lytter implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        System.out.println("Noen sa hei");
    }
}

class Test {
    public static void main(String[] args) {
        JFrame vindu = new Vindu();
    }
}
