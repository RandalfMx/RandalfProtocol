/**
 * 
 */
package mx.randalf.protocol.randalfGoogleXls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

/**
 * @author massi
 *
 */
abstract class GoogleXlsTools {

	private Logger log = Logger.getLogger(GoogleXlsTools.class);

	/** Application name. */
	protected static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";

	/** Directory to store user credentials for this application. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
			".credentials/sheets.googleapis.com-java-quickstart");

	/** Global instance of the {@link FileDataStoreFactory}. */
	private static FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the JSON factory. */
	protected static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	protected static HttpTransport HTTP_TRANSPORT;

	/**
	 * Global instance of the scopes required by this quickstart.
	 *
	 * If modifying these scopes, delete your previously saved credentials at
	 * ~/.credentials/sheets.googleapis.com-java-quickstart
	 */
	protected static List<String> SCOPES;

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * 
	 */
	public GoogleXlsTools() {
	}

	protected abstract Sheets getSheetsService(File clientSecret) throws IOException;

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @return an authorized Credential object.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected Credential authorize(File clientSecret) throws FileNotFoundException, IOException {
		// Load client secrets.
		InputStream in = null;
		GoogleClientSecrets clientSecrets = null;
		GoogleAuthorizationCodeFlow flow = null;
		Credential credential = null;

		try {
			in = new FileInputStream(clientSecret);
			// in =ReadGoogle.class.getResourceAsStream("/client_secret.json");
			clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

			// Build flow and trigger user authorization request.
			flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
					.setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("online").build();
			credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
			log.debug("\n" + "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				throw e;
			}
		}
		return credential;
	}

	public int analizza(File clientSecret, String spreadsheetId, String page, int nRow, String colStart, String colEnd)
			throws IOException, Exception {
		List<Object> row = null;
		Sheets sheets = null;

		try {
			sheets = getSheetsService(clientSecret);
			while (true) {
				row = readRow(sheets, nRow, spreadsheetId, page, colStart, colEnd);
				if (row == null) {
					break;
				} else {
					analizza(row, page, nRow, sheets, spreadsheetId);
				}
				nRow++;
			}
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
		return nRow;
	}

	protected abstract void analizza(List<Object> row, String page, int nRow, Sheets sheets, String spreadsheetId)
			throws IOException, Exception;

	/**
	 * Metodo utilizzato per leggere la riga richiesta nel foglio
	 * 
	 * @param service
	 * @param nRow
	 * @return
	 * @throws IOException
	 */
	private List<Object> readRow(Sheets service, int nRow, String spreadsheetId, String page, String colStart,
			String colEnd) throws IOException {

		String range = null;
		ValueRange response = null;
		List<List<Object>> rows = null;
		List<Object> row = null;
		try {

			range = (page == null || page.trim().equals("") ? "" : page + "!") + colStart + nRow + ":" + colEnd + nRow;

			response = service.spreadsheets().values().get(spreadsheetId, range).execute();
			rows = response.getValues();
			if (rows != null && rows.size() > 0) {
				row = rows.get(0);
			}
		} catch (IOException e) {
			throw e;
		}
		return row;
	}
}
