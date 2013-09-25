package utils;

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

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

/**
 * subclass of JTextPane allowing color attributes java.sun.com example
 */
public final class AttributedTextArea extends JTextPane {
    private final DefaultStyledDocument mStyle = new DefaultStyledDocument();
    private String mFontName;
    private int mFontSize;
    private Style def;

    public String getFontName() {
        return mFontName;
    }

    public void setFontName(String mFontName) {
        this.mFontName = mFontName;
        StyleConstants.setFontFamily(def, mFontName);
    }

    public int getFontSize() {
        return mFontSize;
    }

    public void setFontSize(int mFontSize) {
        this.mFontSize = mFontSize;
        StyleConstants.setFontSize(def, mFontSize);
    }

    public AttributedTextArea() {
        this.setDocument(mStyle);
        def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

        Style regular = mStyle.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "Arial");

        Style s = null;

        s = mStyle.addStyle("link", regular);
        StyleConstants.setUnderline(s, true);
        StyleConstants.setBold(s, true);
        StyleConstants.setForeground(s, Color.BLUE);
    }

    /**
     * append text
     */
    public void append(final String aMessage, final Color aColor) {
        try {

            final SimpleAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setForeground(attr, aColor);
            setEditable(true);
            final Emoticons mEmoticons = new Emoticons();
            retEmotion retValue = new retEmotion(0, "", "");
            int last = 0;
            int finish = 0;
            while (finish != -1) {
                retValue = mEmoticons.findNextEmoticon(last, aMessage);
                finish = retValue.getPosition();
                if (finish != -1) {
                    mStyle.insertString(mStyle.getLength(), aMessage.substring(last, retValue.getPosition()), attr);
                    setCaretPosition(getDocument().getLength());
                    insertIcon(new ImageIcon("img/" + retValue.getImageName() + ".gif"));
                    last = retValue.getPosition() + retValue.getEmotion().length();
                }
            }
            mStyle.insertString(mStyle.getLength(), aMessage.substring(last), attr);
            setCaretPosition(getDocument().getLength());
            setEditable(false);
        } catch (Exception e) {

        }
    }
}
