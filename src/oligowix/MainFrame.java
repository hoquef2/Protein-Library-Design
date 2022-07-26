package oligowix;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class MainFrame extends JFrame implements ActionListener {
    JButton button;
    public void initialize(){
        setTitle("OligoWizard");
        setSize(450,300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    public static void main(String[] args) {
       MainFrame myFrame = new MainFrame();
        myFrame.initialize();
        JButton button = new JButton("Select File");
        button.addActionListener(myFrame);
        myFrame.add(button);
       
        
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button){
            JFileChooser fileChooser = new JFileChooser();
            
            fileChooser.showOpenDialog(null); //select file to open
            
            /* int fileChooser
            if repsonse == 0 or JfileChooser.APPROVE_OPTION
            F
            */
            
            
        }
        
    }
}