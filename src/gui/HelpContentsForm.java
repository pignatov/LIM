package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public final class HelpContentsForm extends JDialog {
    private String mHelpFile;

    {
        mHelpFile = "doc/help.html";
    }

    public HelpContentsForm(final String aHelpFile) {
        this.mHelpFile = aHelpFile;
        showHelpForm();
    }

    private void showHelpForm() {
        setTitle("Съдържание");
        setResizable(false);
        final Container content = getContentPane();
        content.setLayout(new BorderLayout());
        final JEditorPane pane = new JEditorPane();
        pane.setEditable(false);
        try {
            final File file = new File(mHelpFile);
            pane.setPage(file.toURL());
        } catch (Exception ex) {
            pane.setText("Грешка: " + mHelpFile + " не може да бъде открит");
        }

        pane.setPreferredSize(new Dimension(400, 300));
        pane.setContentType("text/html");
        final JScrollPane scroll_pane = new JScrollPane(pane);

        //        scroll_pane.setMinimumSize(new Dimension(300, 150));

        final JPanel pnlMain = new JPanel();
        final JPanel pnlButtons = new JPanel();
        final JButton btnOK = new JButton("OK", new ImageIcon("img/Ok.gif"));
        pnlButtons.add(btnOK);
        pnlMain.add(scroll_pane);

        content.add("Center", pnlMain);
        content.add("South", pnlButtons);

        btnOK.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                dispose();
            }
        });
    }

    public HelpContentsForm() {
        showHelpForm();
    }
}
