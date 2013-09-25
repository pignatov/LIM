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
/* Date: Sep 29, 2002 * Time: 5:34:11 PM */

import utils.CommonValues;
import utils.IniReader;

import java.io.IOException;

/**
 * Main program file. Sole purpose is starting one <code>msgServer</code> thread
 */
final class ChatServer {

    public static void main(final String[] args) {
        System.out.println("LAN Client/Server Instant Messaging");
        System.out.println("Copyright (C) 2002,2004  Plamen Ignatov <plig@mail.bg>");
        System.out.println("This program is free software and it's licenced under GPL version 2 or any later at your opinion\n");
        int Port = 1001;
        try {
            final IniReader ini = new IniReader(CommonValues.inifile);
            if (ini.getValue("Port") != null) {
                final Integer integer = new Integer(ini.getValue("PORT"));
                Port = integer.intValue();
            }
        } catch (IOException ex) {
            System.out.println("Ini File cannot be read. Using defaults");
        }

        final MsgServer ms = new MsgServer(Port);
        ms.start();
    }
}
