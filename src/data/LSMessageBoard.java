/**
 * Created by IntelliJ IDEA.
 * User: plamen
 * Date: Dec 26, 2004
 * Time: 12:00:46 PM
 * To change this template use File | Settings | File Templates.
 */
package data;

import java.io.*;
import java.util.Vector;

public final class LSMessageBoard {
    private LSMessageBoard(){}
    
    public static Vector read() throws FileNotFoundException, IOException {
        final ObjectInputStream f = new ObjectInputStream(new FileInputStream("mbinfo.dat"));
        Vector temp = new Vector();

        try {
            temp = (Vector) f.readObject();
            f.close();
        } catch (ClassNotFoundException ioex) {
            System.err.println("Error in File" + ioex.getMessage());
        }
        return temp;
    }

    public static void write(final Vector aMBItems) throws IOException {
        File file = new File("mbinfo.dat");
        if (file.exists())  file.delete();
        final ObjectOutputStream f = new ObjectOutputStream(new FileOutputStream("mbinfo.dat"));

        try {
            f.writeObject(aMBItems);
            f.flush();
            f.close();
        } catch (IOException ioex) {
            System.err.println("Error in File");
        }
    }

}