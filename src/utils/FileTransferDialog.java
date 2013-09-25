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
package utils;

import javax.swing.*;
import java.awt.*;


public class FileTransferDialog extends JDialog {
    private static final JProgressBar jpb = new JProgressBar(0, 100);
    private final JLabel mFromLabel = new JLabel("Save from ");
    private final JLabel mFromText = new JLabel();
    private final JLabel mSaveLabel = new JLabel("To ");
    private final JLabel mSaveText = new JLabel();
    private final JLabel mStatusLabel = new JLabel("Status");
    private final JLabel mStatusText = new JLabel();
    private final JLabel mTimeLeftLabel = new JLabel("Time Left");
    private final JLabel mTimeLeftText = new JLabel();
    private final JLabel mTimeElapsedLabel = new JLabel("Time Elapsed");
    private final JLabel mTimeElapsedText = new JLabel();
    private final JLabel mProgressLabel = new JLabel("Progress");
    private final JButton okCancel = new JButton("Cancel");


    private FileTransferDialog(final String title, final FileInfo fI) throws HeadlessException {
        setTitle(title);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        final Container content = getContentPane();
        content.setLayout(new GridLayout(7, 1, 0, 0));
        mFromText.setText(fI.getSource());
        mSaveText.setText(fI.getDest() + "/" + fI.getFilename());
        mTimeLeftText.setText(new Long(fI.getTimeLeft()).toString());
        mTimeElapsedText.setText(new Long(fI.getTimeLeft()).toString());

        mFromLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
        mFromText.setFont(new Font("Tahoma", Font.PLAIN, 10));

        final JPanel mFrom = new JPanel(new GridLayout(1, 2));
        mFrom.add(mFromLabel);
        mFrom.add(mFromText);
        final JPanel mSave = new JPanel(new GridLayout(1, 2));
        mSave.add(mSaveLabel);
        mSave.add(mSaveText);
        final JPanel mStatus = new JPanel(new GridLayout(1, 2));
        mStatus.add(mStatusLabel);
        mStatusText.setText((new Long(fI.getCompleted())).toString() + "KB of " + (new Long(fI.getSize())).toString() + "KB (at " + new Double(fI.getAvgSpeed()).toString() + "KB/s)");
        mStatus.add(mStatusText);
        final JPanel mTimeLeft = new JPanel(new GridLayout(1, 2));
        mTimeLeft.add(mTimeLeftLabel);
        mTimeLeft.add(mTimeLeftText);
        final JPanel mTimeElapsed = new JPanel(new GridLayout(1, 2));
        mTimeElapsed.add(mTimeElapsedLabel);
        mTimeElapsed.add(mTimeElapsedText);
        mTimeElapsedLabel.setPreferredSize(new Dimension(50, 20));

        final JPanel mProgress = new JPanel(new GridLayout(1, 2));
        mProgress.add(mProgressLabel);
        mProgress.add(jpb);
        jpb.setStringPainted(true);

        final JPanel mButtons = new JPanel(new FlowLayout());
        mButtons.add(okCancel, "CENTER");

        content.add(mFrom);
        content.add(mSave);
        content.add(mStatus);
        content.add(mTimeLeft);
        content.add(mTimeElapsed);
        content.add(mProgress);
        content.add(mButtons);


    }

    public static void main(final String[] args) {
        final FileInfo fI = new FileInfo("abc.txt", "VANKO", "/usr/local", 115000);
        fI.setCompleted(23000);
        jpb.setValue(fI.getPercent());
        final FileTransferDialog ft = new FileTransferDialog("Hello World", fI);
        ft.setSize(200, 200);
        ft.pack();
        ft.setVisible(true);
    }
}
