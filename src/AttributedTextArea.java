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
/* Date: Dec 1, 2002 * Time: 2:43:17 PM */

import utils.Emoticons;
import utils.retEmotion;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

/**
 * subclass of JTextPane allowing color attributes java.sun.com example
 */
final class AttributedTextArea extends JTextPane {
    private final DefaultStyledDocument m_defaultStyledDocument = new DefaultStyledDocument();

    public AttributedTextArea() {
        this.setDocument(m_defaultStyledDocument);
    }

    /**
     * Appends text
     *
     * @param string Text to append
     * @param color  Color to use
     */
    public void append(final String string, final Color color) {
        try {

            final SimpleAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setForeground(attr, color);
            setEditable(true);
            final Emoticons mEmoticons = new Emoticons();
            retEmotion retValue = new retEmotion(0, "", "");
            int last = 0;
            int finish = 0;
            while (finish != -1) {

                retValue = mEmoticons.findNextEmoticon(last, string);
                finish = retValue.getPosition();
                if (finish != -1) {
                    m_defaultStyledDocument.insertString(m_defaultStyledDocument.getLength(), string.substring(last, retValue.getPosition()), attr);
                    setCaretPosition(getDocument().getLength());
                    insertIcon(new ImageIcon(retValue.getImageName() + ".gif"));
                    last = retValue.getPosition() + retValue.getEmotion().length();
                }
            }
            m_defaultStyledDocument.insertString(m_defaultStyledDocument.getLength(), string.substring(last), attr);
            setCaretPosition(getDocument().getLength());
            setEditable(false);
        } catch (Exception e) {

        }
    }
}
