package utils;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;

final class HTMLHelp extends JTextPane {
    public HTMLHelp(final String htmlFile) throws IOException {
        String s = null;
        try {
            s = "file:"
                    + System.getProperty("user.dir")
                    + System.getProperty("file.separator")
                    + htmlFile;
            /* ...  use the URL to initialize the editor pane  ... */
        } catch (Exception e) {
            System.err.println("Couldn't create help URL: " + s);
        }
        setPage(new URL(s));
        setEditable(false);
    }

    public static void main(final String[] args) {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            final HTMLHelp helpPage = new HTMLHelp("about.html");
            final JScrollPane jsp = new JScrollPane(helpPage);
            frame.getContentPane().add(jsp);
            frame.pack();
            frame.setSize(500, 300);
            frame.setVisible(true);
        } catch (IOException ex) {
            System.out.println("Error!");
            ex.printStackTrace();
        }

    }
}