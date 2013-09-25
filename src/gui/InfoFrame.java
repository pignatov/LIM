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
/* Date: Oct 4, 2002 * Time: 5:32:36 PM */

import utils.CommonValues;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class InfoFrame extends JFrame {
    private static InfoFrame _instance = null;

   static public InfoFrame instance(final String title, final String message, final int state) {
      if(null == _instance) {
         _instance = new InfoFrame(title, message, state);
      }
      return _instance;
   }

    /**
     * Shows informative mMessage
     *
     * @param title   Title of information mMessage
     * @param message Information mMessage
     */
    private InfoFrame(final String title, final String message, final int state) {
        super(title);
        if (state == 0) setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        if (state == 1) setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final Container content = getContentPane();
        content.setLayout(new BorderLayout());
        final JPanel mButtonPanel = new JPanel(new FlowLayout());

        final JButton ok = new JButton(CommonValues.locale.OK);

        mButtonPanel.add(ok);
        final JLabel msg = new JLabel(message);
        msg.setFont(new Font("Tahoma", Font.BOLD, 15));

        content.add(msg, "Center");
        content.add(mButtonPanel, "South");

        final ActionListener okListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                if (state == 0)
                    dispose();
                else
                    System.exit(12);
            }
        };
        ok.addActionListener(okListener);
    }
}
