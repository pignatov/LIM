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
/* Date: Oct 9, 2002
 * Time: 5:44:48 PM */


import java.util.Calendar;

/**
 * data.Data container, which represents single message,
 * <i>from</i>, <i>to</i>, <i>message</i> and <i>date</i> fields
 */
public final class entry {
    private String mFrom;
    private String mTo;
    private String mMessage;
    private Calendar mDate;

    /**
     * Default constructor. Does nothing.
     */
    public entry() {
    }

    /**
     * @param aFrom    Sender of message
     * @param aTo      Receipient of message
     * @param aMessage Message itself
     * @param aDate    Date of sending
     */
    public entry(final String aFrom, final String aTo, final String aMessage, final Calendar aDate) {
        mFrom = aFrom;
        mTo = aTo;
        mMessage = aMessage;
        mDate = aDate;
    }

    public String getFrom() {
        return mFrom;
    }

    public void setFrom(final String aFrom) {
        this.mFrom = aFrom;
    }

    public String getTo() {
        return mTo;
    }

    public void setTo(final String aTo) {
        this.mTo = aTo;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(final String aMessage) {
        this.mMessage = aMessage;
    }

    public Calendar getDate() {
        return mDate;
    }

    public void setDate(final Calendar aDate) {
        this.mDate = aDate;
    }
}