package gui;

/*************************************************************************
 * LAN Client/Server Instant Messaging
 * Copyright (C) 2002  Plamen Ignatov <plig@mail.bg>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class YesNoFrame extends JDialog {
    public int Result;

    public YesNoFrame(final String message) {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        final Container content = getContentPane();
        content.setLayout(new BorderLayout());
        final JPanel mButtonPanel = new JPanel(new FlowLayout());
        setResizable(false);

        final JButton btnYes = new JButton("Yes", new ImageIcon("img/ok.gif"));
        final JButton btnNo = new JButton("No", new ImageIcon("img/cancel.gif"));

        mButtonPanel.add(btnYes);
        mButtonPanel.add(btnNo);

        final JLabel msg = new JLabel(message);
        msg.setFont(new Font("Tahoma", Font.BOLD, 15));

        content.add(msg, "Center");
        content.add(mButtonPanel, "South");

        final ActionListener okListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                Result = 0;
                setVisible(false);
            }
        };
        btnYes.addActionListener(okListener);

        final ActionListener noListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                Result = 1;
                setVisible(false);
            }
        };
        btnNo.addActionListener(noListener);
    }

}
