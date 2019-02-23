/**
 * 
 */
package mx.randalf.protocol.randalfGoogleXls;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;

/**
 * @author massi
 *
 */
public abstract class GoogleXlsWriter extends GoogleXlsTools {


	static {
		SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS, SheetsScopes.DRIVE);
	}

	/**
	 * 
	 */
	public GoogleXlsWriter() {
		super();
	}

	@Override
	protected Sheets getSheetsService(File clientSecret) throws IOException {
		Credential credential = authorize(clientSecret);
		return new Sheets.Builder(
				HTTP_TRANSPORT, 
		          JacksonFactory.getDefaultInstance(), credential)
		          .setApplicationName(APPLICATION_NAME)
		          .build();
	}

}
