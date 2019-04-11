/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package monitoringfolder;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class FileBrowser extends JPanel
        implements ActionListener {

    private static final String newline = "\n";
    JTextField tf1;
    JTextField tf2;
    JLabel l1;
    JLabel l2;
    JButton openButton1;
    JButton openButton2;
    JButton saveButton;
    JButton monitorButton;
    JTextArea log;
    JFileChooser fc;
    static Thread t;

    public FileBrowser() {
        super(new BorderLayout());

        this.log = new JTextArea(5, 20);
        this.log.setMargin(new Insets(5, 5, 5, 5));
        this.log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(this.log);

        this.fc = new JFileChooser();

        this.fc.setFileSelectionMode(1);

        this.openButton1 = new JButton("Folder");
        this.openButton2 = new JButton("Folder");
        this.openButton1.addActionListener(this);
        this.openButton2.addActionListener(this);
        this.tf1 = new JTextField(20);
        this.tf2 = new JTextField(20);
        this.tf1.addActionListener(this);
        this.tf2.addActionListener(this);
        this.monitorButton = new JButton("monitor");
        this.monitorButton.addActionListener(this);
        this.l1 = new JLabel("<html>Select the folder to Monitor:<br></html>");
        this.l2 = new JLabel("<html>Select the location for the log file to be created:<br></html>");

        JPanel panel1 = new JPanel(new GridLayout(3, 4));
        panel1.add(this.l1);
        panel1.add(this.tf1);
        panel1.add(this.openButton1);
        panel1.add(this.l2);
        panel1.add(this.tf2);
        panel1.add(this.openButton2);
        panel1.add(this.monitorButton);

        add(panel1, "First");
    }

    public void actionPerformed(ActionEvent e) {
        int returnVal;
        File file;
        Object obj = e.getSource();
        if ((obj == this.openButton1) || (obj == this.openButton2)) {
            returnVal = this.fc.showOpenDialog(this);

            if (returnVal == 0) {
                file = this.fc.getSelectedFile();

                if (obj == this.openButton1) {
                    this.tf1.setText(file.getAbsolutePath());
                } else {
                    this.tf2.setText(file.getAbsolutePath());
                }

                this.log.append("Opening: " + file.getName() + "." + "\n");
            } else {
                this.log.append("Open command cancelled by user.\n");
            }
            this.log.setCaretPosition(this.log.getDocument().getLength());
        } else if (e.getSource() == this.saveButton) {
            returnVal = this.fc.showSaveDialog(this);
            if (returnVal == 0) {
                file = this.fc.getSelectedFile();

                this.log.append("Saving: " + file.getName() + "." + "\n");
            } else {
                this.log.append("Save command cancelled by user.\n");
            }
            this.log.setCaretPosition(this.log.getDocument().getLength());
        } else if (e.getSource() == this.monitorButton) {
            FileTest test = new FileTest(this.tf1.getText(), this.tf2.getText());

            t = new Thread(test);
            t.start();
        }
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("FileBrowser");
        frame.setDefaultCloseOperation(3);

        frame.add(new FileBrowser());

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                //FileBrowser.access$0();
                FileBrowser f = new FileBrowser();
                f.createAndShowGUI();
            }
        });
    }
}
