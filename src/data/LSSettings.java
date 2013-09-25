package data;

/*************************************************************************
 * LAN gui.Client/Server Instant Messaging
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
/* Date: Oct 24, 2002 * Time: 3:45:09 PM */

import java.io.*;

/**
 * Load Save Settings
 */
public final class LSSettings {
    public LSSettings() {
    }

    public static UserSettings read() throws FileNotFoundException, IOException {
        final ObjectInputStream f = new ObjectInputStream(new FileInputStream("settings.dat"));
        UserSettings temp = new UserSettings();

        try {
            temp = (UserSettings) f.readObject();
            f.close();
        } catch (ClassNotFoundException ioex) {
            System.err.println("Error in File" + ioex.getMessage());
        }
        return temp;
    }

    public static void write(final UserSettings USData) throws IOException {
        final ObjectOutputStream f = new ObjectOutputStream(new FileOutputStream("settings.dat"));

        try {
            f.writeObject(USData);
            f.flush();
            f.close();
        } catch (IOException ioex) {
            System.err.println("Error in File");
        }
    }

}
