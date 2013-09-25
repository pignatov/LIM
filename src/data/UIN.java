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
/* Date: Sep 29, 2002 * Time: 10:00:37 PM */

import java.io.BufferedReader;
import java.io.PrintWriter;

/**
 * <code>data.mUIN</code> class holds information about input and output streams for certain user
 */
final class UIN {
    private String mUIN;
    private BufferedReader mIn;
    private PrintWriter mOut;

    /**
     * Default constructor
     */
    public UIN() {
        // Still unsure what to add here
    }

    /**
     * Custom constructor
     *
     * @param aUIN data.mUIN of user
     * @param aIn  Input stream
     * @param aOut Output stream
     */
    public UIN(final String aUIN, final BufferedReader aIn, final PrintWriter aOut) {
        this.mUIN = aUIN;
        this.mIn = aIn;
        this.mOut = aOut;
    }

    public String getUIN() {
        return mUIN;
    }

    public void setUIN(final String aUIN) {
        this.mUIN = aUIN;
    }

    public BufferedReader getIn() {
        return mIn;
    }

    public void setIn(final BufferedReader aIn) {
        this.mIn = aIn;
    }

    public PrintWriter getOut() {
        return mOut;
    }

    public void setOut(final PrintWriter aOut) {
        this.mOut = aOut;
    }
}
