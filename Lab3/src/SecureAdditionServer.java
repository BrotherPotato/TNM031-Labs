// import javax.net.SocketFactory;
// import javax.net.*;
// import java.net.*; // behöver inte?

// public class Lab3 {
//     public static void main(String[] args) throws Exception {
//         System.out.println("Hello, World!");
//         SocketFactory asd = null;

//     }}



// Server Julia Karlsson
// Password for server: lab3_server_password

// Client Max Wiklundh
// Password for client: lab3_client_password


import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.security.*;
import java.util.StringTokenizer;

/* An example

 class that
 uses the
 secure server socket class
 */

public class SecureAdditionServer {
    private int port;
    // This is not a reserved port number
    static final int DEFAULT_PORT = 8189;
    static final String KEYSTORE = "jpatkeystore.ks";
    static final String TRUSTSTORE = "jpattruststore.ks";
    static final String STOREPASSWD = "changeit";
    static final String ALIASPASSWD = "changeit";
    /*
     * Constructor*
     * 
     * @param
     * port The
     * port where
     * the server*
     * will listen for requests
     */

    SecureAdditionServer(int port) {
        this.port = port;
    };

    // The method that does the work for the class

    public void run() {
        try {
            KeyStore ks = KeyStore.getInstance("JCEKS");
            ks.load(new FileInputStream(KEYSTORE), STOREPASSWD.toCharArray());
            KeyStore ts = KeyStore.getInstance("JCEKS");
            ts.load(new FileInputStream(TRUSTSTORE), STOREPASSWD.toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");

            kmf.init(ks, ALIASPASSWD.toCharArray());

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");

            tmf.init(ts);

            SSLContext sslContext = SSLContext.getInstance("TLS");

            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            SSLServerSocketFactory sslServerFactory = sslContext.getServerSocketFactory();

            SSLServerSocket sss = (SSLServerSocket) sslServerFactory.createServerSocket(port);

            sss.setEnabledCipherSuites(sss.getSupportedCipherSuites());

            SSLSocket incoming = (SSLSocket) sss.accept();
            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(incoming.getInputStream()));

            PrintWriter out = new PrintWriter(incoming.getOutputStream(), true);

            String str;

            while (!(str = in.readLine()).equals("")) {
                double result = 0;
                StringTokenizer st = new StringTokenizer(str);
                try {
                    while (st.hasMoreTokens()) {
                        Double d = new Double(st.nextToken());
                        result += d.doubleValue();
                    }
                    out.println("The result is " + result);
                } catch (NumberFormatException nfe) {
                    out.println("Sorry, your list "
                            + "contains an "
                            + "invalid number");
                }
            }
            incoming.close();
        } catch (Exception x) {
            System.out.println(x);
            x.printStackTrace();
        }
    }

    /**
     * The test method for the
     * 
     * @param args[O] Optional
     *                the default
     */

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        SecureAdditionServer addServe = new SecureAdditionServer(port);
        addServe.run();
    }
}
