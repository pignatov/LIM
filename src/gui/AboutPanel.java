package gui;

import utils.CommonValues;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: Nov 20, 2003
 * Time: 12:40:08 AM
 * To change this template use Options | File Templates.
 */
final class AboutPanel extends JPanel {
    public AboutPanel() {
        final Container content = this;
        content.setLayout(new FlowLayout());
        final JButton btnCheck = new JButton(CommonValues.locale.Check);
        btnCheck.setFont(new Font("Verdana", Font.PLAIN, 10));
        final JButton btnUpdate = new JButton(CommonValues.locale.Update);
        btnUpdate.setFont(new Font("Verdana", Font.PLAIN, 10));
        btnUpdate.setEnabled(false);
        final JLabel newVersion = new JLabel();
        newVersion.setBorder(new EtchedBorder());
        final JLabel curVersion = new JLabel(CommonValues.translateVersion(CommonValues.bVersion));
        curVersion.setBorder(new EtchedBorder());
        final JPanel updateButtons = new JPanel(new GridLayout(1, 4));
        final JPanel update = new JPanel(new BorderLayout());
        update.setBorder(new TitledBorder(CommonValues.locale.Update));

        final ActionListener checkVersion = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                try {
                    final URL lim = new URL("http://lim.hit.bg/version.lim");
                    final BufferedReader in = new BufferedReader(new InputStreamReader(lim.openStream()));
                    final String inputLine;
                    if ((inputLine = in.readLine()) != null)
                        newVersion.setText(CommonValues.translateVersion(inputLine));
                    if (inputLine.compareTo(CommonValues.bVersion) > 0) btnUpdate.setEnabled(true);
                    in.close();
                } catch (Exception ex) {
                    newVersion.setText("Error");
                }
            }
        };
        btnCheck.addActionListener(checkVersion);
        updateButtons.add(btnCheck);
        updateButtons.add(curVersion);
        updateButtons.add(newVersion);
        updateButtons.add(btnUpdate);
        update.add(updateButtons, "North");

        content.add(update);
    }
}
