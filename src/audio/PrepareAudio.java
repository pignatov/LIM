package audio;

/*
 * Created by IntelliJ IDEA.
 * User: Plamen Ignatov
 * Date: Dec 1, 2002
 * Time: 2:33:58 PM
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */

import utils.AttributedTextArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class PrepareAudio extends JFrame {
    final AttributedTextArea jta = new AttributedTextArea();
    final JPanel buttons = new JPanel();
    static vsjtalk myphone;
    final String UIN;
    final String IP;

    public PrepareAudio(final String _UIN, final String _IP) {
        super("Audio Test");
        UIN = _UIN;
        IP = _IP;
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        final Container content = getContentPane();
        content.setLayout(new BorderLayout());
        final JButton start = new JButton("Start");
        final JButton stop = new JButton("Stop");

        final JScrollPane jsp = new JScrollPane(jta);
        content.add(jsp, "Center");
        content.add(buttons, "South");
        buttons.add(stop);
        jta.append("Connecting... \n", Color.RED);
        jta.append(UIN + ":" + IP + "\n", Color.BLUE);

        final Thread thread = new Thread(new vsjtalk(IP));
        thread.start();

        final ActionListener stopListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                thread.stop();
                dispose();
            }
        };

        stop.addActionListener(stopListener);
    }

    public final void append(final String str) {
        jta.append(str + "\n", Color.BLUE);
    }

}
