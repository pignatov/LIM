package data;

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
/* Date: Oct 24, 2002 * Time: 8:42:57 PM */

import utils.CommonValues;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Shows information about specified user
 * Currently data.UIN and IP
 */
public final class UserInfo extends JFrame {
    /**
     * Constructor
     *
     * @param aUIN    The User, whom we are interested in
     * @param aSender That is issuer's <code>data.Sender</code> object
     */
    public UserInfo(final String aUIN, final Sender aSender) {
        super(CommonValues.locale.infoAbout + " " + aUIN);
        final JPanel pnlUserInformation = new JPanel(new GridLayout(3, 2));
        final JPanel pnlButtons = new JPanel();
        final JButton ok = new JButton(CommonValues.locale.OK, new ImageIcon("img/ok.gif"));
        final JLabel mUINLabel = new JLabel("UIN");
        final JLabel mUINValue = new JLabel(aUIN);
        final JLabel mIPLabel = new JLabel("IP");
        final JLabel mIPValue = new JLabel(aSender.getUI().getIp(aUIN));
        final JLabel mColorLabel = new JLabel(CommonValues.locale.Color);
        final JFrame mThisFrame = this;

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        final Container content = getContentPane();
        content.setLayout(new GridLayout(2, 1));

        mUINValue.setFont(new Font("Tahoma", Font.ITALIC, 12));
        mIPValue.setFont(new Font("Tahoma", Font.ITALIC, 12));
        final JButton mColor = new JButton("");
        mColor.setBackground(aSender.getUI().getColor(aUIN));

        pnlButtons.add(ok);
        pnlUserInformation.add(mUINLabel);
        pnlUserInformation.add(mUINValue);
        pnlUserInformation.add(mIPLabel);
        pnlUserInformation.add(mIPValue);
        pnlUserInformation.add(mColorLabel);
        pnlUserInformation.add(mColor);
        content.add(pnlUserInformation);
        content.add(pnlButtons);

        final ActionListener okListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                dispose();
            }
        };
        ok.addActionListener(okListener);

        final ActionListener mColorListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final Color mInitialColor = aSender.getUI().getColor(aUIN);
                final Color newColor = JColorChooser.showDialog(mThisFrame, "Choose Color", mInitialColor);
                mColor.setBackground(newColor);
                aSender.getUI().setColor(aUIN, newColor);
            }
        };
        mColor.addActionListener(mColorListener);
    }
}