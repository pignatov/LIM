package utils;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: Oct 31, 2003
 * Time: 9:30:47 PM
 * To change this template use Options | File Templates.
 */
public final class EmoticonsPanel extends JPanel {
    private final JTextField mCommandLine;
    private final JWindow mWindow;
    private final Timer timer;


    public EmoticonsPanel(final JTextField aCommandLine, final JWindow aWindow) {
        super(new GridLayout(5, 5));
        setBorder(new BevelBorder(BevelBorder.RAISED));
        final JButton m00 = new JButton(new ImageIcon("img/" + CommonValues.emoticons[0].getImageName() + ".gif"));
        final JButton m01 = new JButton(new ImageIcon("img/" + CommonValues.emoticons[1].getImageName() + ".gif"));
        final JButton m02 = new JButton(new ImageIcon("img/" + CommonValues.emoticons[2].getImageName() + ".gif"));
        final JButton m03 = new JButton(new ImageIcon("img/" + CommonValues.emoticons[3].getImageName() + ".gif"));
        final JButton m04 = new JButton(new ImageIcon("img/" + CommonValues.emoticons[4].getImageName() + ".gif"));
        final JButton m05 = new JButton(new ImageIcon("img/" + CommonValues.emoticons[5].getImageName() + ".gif"));
        final JButton m06 = new JButton(new ImageIcon("img/" + CommonValues.emoticons[6].getImageName() + ".gif"));
        final JButton m07 = new JButton(new ImageIcon("img/" + CommonValues.emoticons[7].getImageName() + ".gif"));
        final JButton m08 = new JButton(new ImageIcon("img/" + CommonValues.emoticons[8].getImageName() + ".gif"));
        final JButton m09 = new JButton(new ImageIcon("img/" + CommonValues.emoticons[9].getImageName() + ".gif"));
        final JButton m10 = new JButton(new ImageIcon("img/" + CommonValues.emoticons[10].getImageName() + ".gif"));
        final JButton m11 = new JButton(new ImageIcon("img/" + CommonValues.emoticons[11].getImageName() + ".gif"));
        final JButton m12 = new JButton(new ImageIcon("img/" + CommonValues.emoticons[12].getImageName() + ".gif"));
        final JButton m13 = new JButton(new ImageIcon("img/" + CommonValues.emoticons[13].getImageName() + ".gif"));
        final JButton m14 = new JButton(new ImageIcon("img/" + CommonValues.emoticons[14].getImageName() + ".gif"));
        final JButton m15 = new JButton(new ImageIcon("img/" + CommonValues.emoticons[15].getImageName() + ".gif"));
        final JButton m16 = new JButton(new ImageIcon("img/" + CommonValues.emoticons[16].getImageName() + ".gif"));
        final JButton m17 = new JButton(new ImageIcon("img/" + CommonValues.emoticons[17].getImageName() + ".gif"));
        final JButton m18 = new JButton(new ImageIcon("img/" + CommonValues.emoticons[18].getImageName() + ".gif"));
        final JButton m19 = new JButton(new ImageIcon("img/" + CommonValues.emoticons[19].getImageName() + ".gif"));
        final JButton m20 = new JButton(new ImageIcon("img/" + CommonValues.emoticons[20].getImageName() + ".gif"));
        final JButton m21 = new JButton(new ImageIcon("img/" + CommonValues.emoticons[21].getImageName() + ".gif"));
        final JButton m22 = new JButton(new ImageIcon("img/" + CommonValues.emoticons[22].getImageName() + ".gif"));
        final JButton m23 = new JButton(new ImageIcon("img/" + CommonValues.emoticons[23].getImageName() + ".gif"));
//        JButton m24= new JButton(new ImageIcon((utils.values.emoticons[24]).getImageName()+".gif") );
//        JButton m25= new JButton(new ImageIcon((utils.values.emoticons[25]).getImageName()+".gif") );
        add(m00);
        add(m01);
        add(m02);
        add(m03);
        add(m04);
        add(m05);
        add(m06);
        add(m07);
        add(m08);
        add(m09);
        add(m10);
        add(m11);
        add(m12);
        add(m13);
        add(m14);
        add(m15);
        add(m16);
        add(m17);
        add(m18);
        add(m19);
        add(m20);
        add(m21);
        add(m22);
        add(m23);
//        add(m24);
//        add(m25);

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(final MouseEvent evt) {
                mWindow.setVisible(false);
                mWindow.dispose();
            }
        });
        final ActionListener m00Listener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                mCommandLine.setText(mCommandLine.getText() + CommonValues.emoticons[0].getEmoticon());
                mWindow.setVisible(false);
                mWindow.dispose();
                mCommandLine.requestFocus();
            }
        };
        m00.addActionListener(m00Listener);

        final ActionListener m01Listener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                mCommandLine.setText(mCommandLine.getText() + CommonValues.emoticons[1].getEmoticon());
                mWindow.setVisible(false);
                mWindow.dispose();
                mCommandLine.requestFocus();
            }
        };
        m01.addActionListener(m01Listener);

        final ActionListener m02Listener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                mCommandLine.setText(mCommandLine.getText() + CommonValues.emoticons[2].getEmoticon());
                mWindow.setVisible(false);
                mWindow.dispose();
                mCommandLine.requestFocus();
            }
        };
        m02.addActionListener(m02Listener);

        final ActionListener m03Listener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                mCommandLine.setText(mCommandLine.getText() + CommonValues.emoticons[3].getEmoticon());
                mWindow.setVisible(false);
                mWindow.dispose();
                mCommandLine.requestFocus();
            }
        };
        m03.addActionListener(m03Listener);

        final ActionListener m04Listener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                mCommandLine.setText(mCommandLine.getText() + CommonValues.emoticons[4].getEmoticon());
                mWindow.setVisible(false);
                mWindow.dispose();
                mCommandLine.requestFocus();
            }
        };
        m04.addActionListener(m04Listener);

        final ActionListener m05Listener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                mCommandLine.setText(mCommandLine.getText() + CommonValues.emoticons[5].getEmoticon());
                mWindow.setVisible(false);
                mWindow.dispose();
                mCommandLine.requestFocus();
            }
        };
        m05.addActionListener(m05Listener);

        final ActionListener m06Listener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                mCommandLine.setText(mCommandLine.getText() + CommonValues.emoticons[6].getEmoticon());
                mWindow.setVisible(false);
                mWindow.dispose();
                mCommandLine.requestFocus();
            }
        };
        m06.addActionListener(m06Listener);

        final ActionListener m07Listener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                mCommandLine.setText(mCommandLine.getText() + CommonValues.emoticons[7].getEmoticon());
                mWindow.setVisible(false);
                mWindow.dispose();
                mCommandLine.requestFocus();
            }
        };
        m07.addActionListener(m07Listener);

        final ActionListener m08Listener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                mCommandLine.setText(mCommandLine.getText() + CommonValues.emoticons[8].getEmoticon());
                mWindow.setVisible(false);
                mWindow.dispose();
                mCommandLine.requestFocus();
            }
        };
        m08.addActionListener(m08Listener);

        final ActionListener m09Listener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                mCommandLine.setText(mCommandLine.getText() + CommonValues.emoticons[9].getEmoticon());
                mWindow.setVisible(false);
                mWindow.dispose();
                mCommandLine.requestFocus();
            }
        };
        m09.addActionListener(m09Listener);

        final ActionListener m10Listener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                mCommandLine.setText(mCommandLine.getText() + CommonValues.emoticons[10].getEmoticon());
                mWindow.setVisible(false);
                mWindow.dispose();
                mCommandLine.requestFocus();
            }
        };
        m10.addActionListener(m10Listener);

        final ActionListener m11Listener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                mCommandLine.setText(mCommandLine.getText() + CommonValues.emoticons[11].getEmoticon());
                mWindow.setVisible(false);
                mWindow.dispose();
                mCommandLine.requestFocus();
            }
        };
        m11.addActionListener(m11Listener);

        final ActionListener m12Listener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                mCommandLine.setText(mCommandLine.getText() + CommonValues.emoticons[12].getEmoticon());
                mWindow.setVisible(false);
                mWindow.dispose();
                mCommandLine.requestFocus();
            }
        };
        m12.addActionListener(m12Listener);

        final ActionListener m13Listener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                mCommandLine.setText(mCommandLine.getText() + CommonValues.emoticons[13].getEmoticon());
                mWindow.setVisible(false);
                mWindow.dispose();
                mCommandLine.requestFocus();
            }
        };
        m13.addActionListener(m13Listener);

        final ActionListener m14Listener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                mCommandLine.setText(mCommandLine.getText() + CommonValues.emoticons[14].getEmoticon());
                mWindow.setVisible(false);
                mWindow.dispose();
                mCommandLine.requestFocus();
            }
        };
        m14.addActionListener(m14Listener);

        final ActionListener m15Listener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                mCommandLine.setText(mCommandLine.getText() + CommonValues.emoticons[15].getEmoticon());
                mWindow.setVisible(false);
                mWindow.dispose();
                mCommandLine.requestFocus();
            }
        };
        m15.addActionListener(m15Listener);

        final ActionListener m16Listener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                mCommandLine.setText(mCommandLine.getText() + CommonValues.emoticons[16].getEmoticon());
                mWindow.setVisible(false);
                mWindow.dispose();
                mCommandLine.requestFocus();
            }
        };
        m16.addActionListener(m16Listener);

        final ActionListener m17Listener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                mCommandLine.setText(mCommandLine.getText() + CommonValues.emoticons[17].getEmoticon());
                mWindow.setVisible(false);
                mWindow.dispose();
                mCommandLine.requestFocus();
            }
        };
        m17.addActionListener(m17Listener);

        final ActionListener m18Listener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                mCommandLine.setText(mCommandLine.getText() + CommonValues.emoticons[18].getEmoticon());
                mWindow.setVisible(false);
                mWindow.dispose();
                mCommandLine.requestFocus();
            }
        };
        m18.addActionListener(m18Listener);

        final ActionListener m19Listener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                mCommandLine.setText(mCommandLine.getText() + CommonValues.emoticons[19].getEmoticon());
                mWindow.setVisible(false);
                mWindow.dispose();
                mCommandLine.requestFocus();
            }
        };
        m19.addActionListener(m19Listener);

        final ActionListener m20Listener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                mCommandLine.setText(mCommandLine.getText() + CommonValues.emoticons[20].getEmoticon());
                mWindow.setVisible(false);
                mWindow.dispose();
                mCommandLine.requestFocus();
            }
        };
        m20.addActionListener(m20Listener);

        final ActionListener m21Listener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                mCommandLine.setText(mCommandLine.getText() + CommonValues.emoticons[21].getEmoticon());
                mWindow.setVisible(false);
                mWindow.dispose();
                mCommandLine.requestFocus();
            }
        };
        m21.addActionListener(m21Listener);

        final ActionListener m22Listener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                mCommandLine.setText(mCommandLine.getText() + CommonValues.emoticons[22].getEmoticon());
                mWindow.setVisible(false);
                mWindow.dispose();
                mCommandLine.requestFocus();
            }
        };
        m22.addActionListener(m22Listener);

        final ActionListener m23Listener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                mCommandLine.setText(mCommandLine.getText() + CommonValues.emoticons[23].getEmoticon());
                mWindow.setVisible(false);
                mWindow.dispose();
                mCommandLine.requestFocus();
            }
        };
        m23.addActionListener(m23Listener);


        this.mCommandLine = aCommandLine;
        this.mWindow = aWindow;
        timer = new Timer(15 * 1000, new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                timer.stop();
                mWindow.setVisible(false);
                mWindow.dispose();
            }
        });
        timer.start();
    }

}
