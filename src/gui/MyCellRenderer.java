package gui;

import data.Users;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
final class MyCellRenderer extends JLabel implements ListCellRenderer {
    private static final ImageIcon online = new ImageIcon("img/online.gif");
    private static final ImageIcon offline = new ImageIcon("img/offline.gif");
    private static final ImageIcon away = new ImageIcon("img/away.gif");
    private static final ImageIcon dnd = new ImageIcon("img/dnd.gif");
    private static final ImageIcon na = new ImageIcon("img/na.gif");
    private final Users usr;

    public MyCellRenderer(final Users _usr) {
        usr = _usr;
    }

    public Component getListCellRendererComponent(final JList list,
                                                  final Object value,
                                                  final int index,
                                                  final boolean isSelected,
                                                  final boolean cellHasFocus) {
        final String s = value.toString();

        setText(s);
        setFont(new Font("Verdana", Font.PLAIN, 11));
        setHorizontalTextPosition(SwingConstants.TRAILING);
        if (usr.getStatus(s).equalsIgnoreCase("ONLINE")) setIcon(online);
        if (usr.getStatus(s).equalsIgnoreCase("OFFLINE")) setIcon(offline);        
        if (usr.getStatus(s).equalsIgnoreCase("DND")) setIcon(dnd);
        if (usr.getStatus(s).equalsIgnoreCase("NA")) setIcon(na);
        if (usr.getStatus(s).equalsIgnoreCase("AWAY")) setIcon(away);

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        if (usr.getTyping(s))
            setForeground(Color.RED);
        else
            setForeground(list.getForeground());

        setEnabled(list.isEnabled());
        setFont(list.getFont());

        setOpaque(true);
        return this;
    }
}