package gui;

import javax.swing.*;
import java.awt.*;

final class ComboCellRenderer extends JLabel implements ListCellRenderer {
    private static int last_index = 0;
    private static final ImageIcon offline = new ImageIcon("img/offline.gif");
    private static final ImageIcon online = new ImageIcon("img/online.gif");
    private static final ImageIcon away = new ImageIcon("img/away.gif");
    private static final ImageIcon dnd = new ImageIcon("img/dnd.gif");
    private static final ImageIcon na = new ImageIcon("img/na.gif");

    public static void setLastindex(final int lastindex) {
        last_index = lastindex;
    }

    public Component getListCellRendererComponent(final JList list,
                                                  final Object value,
                                                  final int index,
                                                  final boolean isSelected,
                                                  final boolean cellHasFocus) {
        final String s = value.toString();
        setFont(new Font("Verdana", Font.PLAIN, 10));
        setText(s);
        setHorizontalTextPosition(SwingConstants.TRAILING);

        if (index == -1) {
            if (last_index == 0) setIcon(online);
            if (last_index == 1) setIcon(away);
            if (last_index == 2) setIcon(dnd);
            if (last_index == 3) setIcon(na);
            if (last_index == 4) setIcon(offline);
        }
        if (index == 0) setIcon(online);
        if (index == 1) setIcon(away);
        if (index == 2) setIcon(dnd);
        if (index == 3) setIcon(na);
        if (index == 4) setIcon(offline);

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        setEnabled(list.isEnabled());
        setFont(list.getFont());

        setOpaque(true);
        return this;
    }
}