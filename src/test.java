
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

final class test {
    public static void main(final String[] args) throws Exception {
        final URL yahoo = new URL("http://lim.hit.bg/version.lim");
        final BufferedReader in = new BufferedReader(new InputStreamReader(yahoo.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null)
            System.out.println(inputLine);

        in.close();
    }
}
