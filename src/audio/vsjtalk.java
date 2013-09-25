package audio;

import org.jsresources.apps.am.audio.AMAudioFormat;
import org.jsresources.apps.am.audio.AudioCapture;
import org.jsresources.apps.am.audio.AudioPlayStream;

import javax.sound.sampled.AudioFormat;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public final class vsjtalk implements Runnable {
    private AudioCapture recordLine = null;
    private AudioPlayStream playbackLine = null;
    private String IpToContact = null;
    private static final String LOCAL_IP = "127.0.0.1";
    private static final int VSJ_TALK_PORT = 8085;
    public final boolean exit = false;

    // GSM 160 samples of 16 bits each becomes 33 GSM bytes
    // 16k bytes / 320 * 33  (1 sec GSM)

    private final int bufSize = 1650;
    private Session curSession = null;

    public vsjtalk(final String ip) {
        IpToContact = ip;
    }

    public final void startPhone() {
        initAudioHardware();
        Thread secondThread = null;
        curSession = new Session(IpToContact);
        secondThread = new Thread(new RecordSender(curSession));
        secondThread.start();
//        run();
    }

    public final boolean initAudioHardware() {

        AudioFormat format = null;
        final InputStream playbackInputStream = null;

        try {
            format = AMAudioFormat.getLineAudioFormat(AMAudioFormat.FORMAT_CODE_GSM);
            recordLine = new AudioCapture(AMAudioFormat.FORMAT_CODE_GSM);
            playbackLine = new AudioPlayStream(format);
            if (recordLine == null) {
                println("initAudioHardware: cannot create recordLine");
                throw new Exception();
            }
            if (playbackLine == null) {
                println("initAudioHardware: cannot create playbackLine");
                throw new Exception();
            }
            // this ordering is vital to some sound card drivers
            recordLine.open();

            playbackLine.open();
            println("after open");
            println("initAudioHardware: Opened audio channels..");
            return true;
        } catch (Exception ex) {
            println("initAudioHardware: hardware problem : " + ex);
            return false;
        }

    }

    public final void println(final String inText) {
        System.out.println(inText);
    }

    public final void run() {
        startPhone();
        FileOutputStream fos;
        InputStream playbackInputStream = null;
        boolean complete = false;
        final byte[] gsmdata = new byte[bufSize];
        int numBytesRead = 0;
        println("Waiting for remote voice connect");

        try {

            curSession.listen();
            playbackInputStream = curSession.getInputStream();

            println("Incoming voice detected");
            playbackLine.start();
            println("Start sending to speaker");


        } catch (Exception e) {
            println("Exception caught: " + e);
        }

        // read transport stream of compressed voice, write to speaker
        complete = false;
        while (!Thread.interrupted()) {
            try {
                numBytesRead = playbackInputStream.read(gsmdata);
                if (numBytesRead == -1) {
                    complete = true;
                    break;
                }
                playbackLine.write(gsmdata, 0, numBytesRead);
            } catch (IOException e) {
                println("Exception encountered while processing audio: " + e);
            }
        }

        if (!complete) {
            println("Program interrupted, cleaning up.");
        } else {
            println("Session Terminated, cleaning up.");
        }

        // clean up
        try {
            playbackLine.drain();
            playbackLine.close();
            playbackLine = null;
            recordLine.close();
            recordLine = null;
            curSession.close();
        } catch (IOException e) {
            println("Exception during cleanup: " + e);
        }
        println("Cleanup completed.");
    }


    final class RecordSender implements Runnable {
        Session mySession = null;
        OutputStream sendStream = null;

        public RecordSender(final Session openSession) {
            mySession = openSession;
        }

        public final void run() {
            try {
                //10 secs pause
                Thread.sleep(10 * 1000);
                mySession.open();
                sendStream = mySession.getOutputStream();
            } catch (Exception e) {
                println("RecordSender has problem connecting to remote");
            }

            final byte[] compressedVoice = new byte[bufSize];
            final int numBytesRead = 0;
            if (recordLine == null) {
                // should never happens
                println("RecordSender detects that recordLine was not initialized");
            }
            println("RecordSender connected successfully.");

            InputStream myIStream = null;

            try {
                recordLine.start();
                myIStream = recordLine.getAudioInputStream();

                int b = 0;
                while (!Thread.interrupted()) {
                    b = myIStream.read(compressedVoice, 0, bufSize);
                    sendStream.write(compressedVoice, 0, b);
                }
            } catch (Exception e) {
                println("RecordSender caught an exception while processing audio: " + e);
            }
            try {
                myIStream.close();
                recordLine.close();
                sendStream.flush();
                sendStream.close();
            } catch (IOException e) {
                println("RecordSender caught an exception during cleanup: " + e);
            }
            println("RecordSender terminated.");
        }
    }

    final class Session {
        String ipAddr = null;
        Socket outSock = null;
        ServerSocket inServSock = null;
        Socket inSock = null;

        Session(final String inIP) {
            ipAddr = inIP;
        }

        final void open() throws IOException, UnknownHostException {
            if (ipAddr != null)
                outSock = new Socket(ipAddr, vsjtalk.VSJ_TALK_PORT);
        }

        final void listen() throws IOException {
            inServSock = new ServerSocket(vsjtalk.VSJ_TALK_PORT);
            inSock = inServSock.accept();
        }

        public final InputStream getInputStream() throws IOException {
            if (inSock != null)
                return inSock.getInputStream();
            else
                return null;
        }

        public final OutputStream getOutputStream() throws IOException {
            if (outSock != null)
                return outSock.getOutputStream();
            else
                return null;
        }

        final void close() throws IOException {
            inSock.close();
            outSock.close();
        }
    }

}