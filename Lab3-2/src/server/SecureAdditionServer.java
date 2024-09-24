
// An example class that uses the secure server socket class

import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.security.*;
import java.util.StringTokenizer;

public class SecureAdditionServer {
	private int port;
	// This is not a reserved port number
	static final int DEFAULT_PORT = 8189;
	static final String KEYSTORE = "Lab3-2\\src\\server\\LIUkeystore.ks";
	static final String TRUSTSTORE = "Lab3-2\\src\\server\\LIUtruststore.ks";
	static final String KEYSTOREPASS = "123456";
	static final String TRUSTSTOREPASS = "abcdef";

	/**
	 * Constructor
	 * 
	 * @param port The port where the server
	 *             will listen for requests
	 */
	SecureAdditionServer(int port) {
		this.port = port;
	}

	/** The method that does the work for the class */
	public void run() {
		try {
			KeyStore ks = KeyStore.getInstance("JCEKS");
			ks.load(new FileInputStream(KEYSTORE), KEYSTOREPASS.toCharArray());

			KeyStore ts = KeyStore.getInstance("JCEKS");
			ts.load(new FileInputStream(TRUSTSTORE), TRUSTSTOREPASS.toCharArray());

			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(ks, KEYSTOREPASS.toCharArray());

			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
			tmf.init(ts);

			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
			SSLServerSocketFactory sslServerFactory = sslContext.getServerSocketFactory();
			SSLServerSocket sss = (SSLServerSocket) sslServerFactory.createServerSocket(port);
			sss.setEnabledCipherSuites(sss.getSupportedCipherSuites());

			System.out.println("\n>>>> SecureAdditionServer: active ");
			SSLSocket incoming = (SSLSocket) sss.accept();

			// in is what we get from the client
			BufferedReader in = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
			// out is how we send information back to the client
			PrintWriter out = new PrintWriter(incoming.getOutputStream(), true);

			// String str;
			// while (!(str = in.readLine()).equals("")) {
			// double result = 0;
			// StringTokenizer st = new StringTokenizer(str);
			// try {
			// while (st.hasMoreTokens()) {
			// Double d = new Double(st.nextToken());
			// result += d.doubleValue();
			// }
			// out.println("The result is " + result);
			// } catch (NumberFormatException nfe) {
			// out.println("Sorry, your list contains an invalid number");
			// }
			// }
			String str;


			str = in.readLine();
			try {
				//int choice = Integer.parseInt(str);
				String choice = str;
				switch (choice) {
					case "1": // upload
						// try catch runt med IOException e
						String fileName = in.readLine();

						File uploadedFile = new File(System.getProperty("user.dir") + "/" + fileName); // kanske	 /server/files/

						uploadedFile.createNewFile(); // only creates a file if there is no file with that name

						FileWriter writer = new FileWriter(uploadedFile);
						
						String readLine;

						while ((readLine = in.readLine()) != null) { 

							writer.write(readLine + "\n");
						}
						writer.close();

						break;
					case "2": // download

						while ((readLine = in.readLine()) != null) {
							out.println(readLine);
						}


						break;

					case "3": // delete
						str = in.readLine();
						File file = new File(System.getProperty("user.dir") + "/" + str);

						if(file.delete())

						break;

					default:
						break;
				}

			} catch (NumberFormatException nfe) {
				out.println("Sorry, your list contains an invalid number");
			}

			incoming.close();
		} catch (Exception x) {
			System.out.println(x);
			x.printStackTrace();
		}
	}

	/**
	 * The test method for the class
	 * 
	 * @param args[0] Optional port number in place of
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
