package utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: Jul 26, 2003
 * Time: 5:53:57 PM
 * To change this template use Options | File Templates.
 */
final class HTMLFrame extends JFrame {
    private HTMLFrame(final String title, final String aHTML) throws HeadlessException {
        super(title);
        final JButton close = new JButton("Close");
        final JPanel buttonPanel = new JPanel(new FlowLayout());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        try {
            final HTMLHelp mHelp = new HTMLHelp(aHTML);
            final JScrollPane jsp = new JScrollPane(mHelp);
            final Container content = getContentPane();
            content.setLayout(new BorderLayout());
            content.add(jsp, "Center");
            buttonPanel.add(close);
            content.add(buttonPanel, "South");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        final ActionListener closeListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                dispose();
            }
        };
        close.addActionListener(closeListener);
    }

    public static void main(final String[] args) {
        final HTMLFrame mHelpFrame = new HTMLFrame("LIM 3.1", "about.html");
        mHelpFrame.pack();
        mHelpFrame.setSize(500, 300);
        mHelpFrame.setVisible(true);
    }
}
