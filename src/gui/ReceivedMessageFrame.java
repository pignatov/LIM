package gui;

/*************************************************************************
 * LAN Client/Server Instant Messaging
 * Copyright (C) 2002  Plamen Ignatov <plig@mail.bg>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *************************************************************************/
/* Date: 2002-10-4 * Time: 13:07:52 */

import data.Sender;
import data.entry;
import utils.CommonValues;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

/**
 * Name: ReceivedMessageFrame
 * Purpose: Displays mMessage in a JFrame with reply, close, data.history, btnNext buttons
 */
public final class ReceivedMessageFrame extends JFrame {
    private final JButton btnNext = new JButton(CommonValues.locale.Next, new ImageIcon("img/Next.gif"));
    private int mThisMsg;
    private final int mAllMsgs;

    private final String mSrc;
    private final String mDest;
    private String mMessage;
    private final Sender mSender;

    /**
     * Removes all characters but the last <code>aSize</code> number. If only one character is available, adds leading 0
     *
     * @param aInput String to trim
     * @param aSize  Currently not used
     * @return Trimmed string
     */
    private static String convert(final String aInput, final int aSize) {
        String result = new String();
        if (aInput.length() >= aSize)
            result = aInput;
        else
            result += "0" + aInput;
        return result;
    }

    /**
     * Constructor of <code>gui.ReceivedMessageFrame</code> class. Main purpose is constructing <code>JFrame</code>
     * for received mMessage.
     *
     * @param aSender  <code>Sender</code> object, which stores most information about the <b>RECEIVER</b> of mMessage
     * @param aSrc     Who sent the mMessage
     * @param aDest    Who is the receipient of that mMessage
     * @param aMessage The mMessage itself
     */
    public ReceivedMessageFrame(final Sender aSender, final String aSrc, final String aDest, final String aMessage, final int aCurrentMessage) {

        mSender = aSender;
        mSrc = aSrc;
        mDest = aDest;
        mMessage = aMessage;

        final Container content = getContentPane();
        content.setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        final Calendar now = Calendar.getInstance();
        mSender.getUI().setPopped(mSrc, true);
        mThisMsg = aCurrentMessage;

        btnNext.setEnabled(false);
        mAllMsgs = mSender.getUI().getHistorySize(mSrc);
        for (int i = mThisMsg; i < mAllMsgs; i++) {
            final entry temp = mSender.getUI().getHistoryAt(mSrc, i);
            if ((temp.getFrom()).equalsIgnoreCase(mSrc)) {
                mThisMsg = i;
                mMessage = temp.getMessage();
                break;
            }
        }

        for (int i = mThisMsg + 1; i < mAllMsgs; i++) {
            final entry temp = mSender.getUI().getHistoryAt(mSrc, i);
            if ((temp.getFrom()).equalsIgnoreCase(mSrc)) {
                enableNext();
                break;
            }
        }


        final JPanel butt_panel = new JPanel(new FlowLayout());
        final JButton btnReply = new JButton(CommonValues.locale.Reply, new ImageIcon("img/btnreply.gif"));
        final JButton read_prev = new JButton(CommonValues.locale.History, new ImageIcon("img/history.gif"));
        final JButton close = new JButton("Close", new ImageIcon("img/cancel.gif"));
        final JTextArea jta = new JTextArea(5, 40);
        final JFrame jf = new MessageHistoryFrame(mSender, mSrc, mDest);
        jta.setEditable(false);

        jta.setLineWrap(true);
        jta.setWrapStyleWord(true);

        jta.setFont(new Font(mSender.user.getRfont(), Font.BOLD, mSender.user.getRsize()));
        jta.setForeground(new Color(255, 0, 0));
        String hour = "";
        if (mSender.user.isShowtime()) hour = convert((new Long(now.get(Calendar.HOUR_OF_DAY))).toString(), 2) + ":" + convert((new Long(now.get(Calendar.MINUTE))).toString(), 2);
        setTitle(mSrc + " - [" + hour + "]");
        jta.setText(mMessage);
        final JScrollPane scrolltext = new JScrollPane(jta);

        content.add(scrolltext, "Center");
        content.add(butt_panel, "South");
        butt_panel.add(btnReply);
        butt_panel.add(read_prev);
        butt_panel.add(btnNext);
        butt_panel.add(close);

        final ActionListener closelistener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                jf.dispose();
                dispose();
                mSender.getUI().setPopped(mSrc, false);
            }
        };
        close.addActionListener(closelistener);

        final ActionListener replylistener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                if (mSender.getUI().exist(mSrc)) {
                    final JFrame msg = new SendMessageFrame(mSender, mDest, mSrc);
                    msg.setLocationRelativeTo(null);
                    msg.pack();
                    msg.setVisible(true);
                    jf.dispose();
                    dispose();
                    mSender.getUI().setPopped(mSrc, false);
                } else {
                    final JFrame msg = InfoFrame.instance("Error", "User is not online. Sorry.", 0);
                    msg.pack();
                    msg.setVisible(true);
                    dispose();
                }
            }
        };
        btnReply.addActionListener(replylistener);

        final ActionListener nextlistener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final ReceivedMessageFrame msg = new ReceivedMessageFrame(mSender, mSrc, mDest, mSender.getUI().getHistoryAt(mSrc, mThisMsg + 1).getMessage(), mThisMsg + 1);
                msg.pack();
                msg.setVisible(true);
                jf.dispose();
                dispose();
                mSender.getUI().setReceivedMessage(mSrc, msg);
                mSender.getUI().setPopped(mSrc, true);
            }
        };
        btnNext.addActionListener(nextlistener);

        final ActionListener prevlistener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                jf.pack();
                jf.setVisible(true);
            }
        };
        read_prev.addActionListener(prevlistener);
    }

    public void enableNext() {
        btnNext.setEnabled(true);
    }
}
