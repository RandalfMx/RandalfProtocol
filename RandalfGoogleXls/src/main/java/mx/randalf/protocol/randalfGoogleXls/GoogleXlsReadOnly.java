/**
 * 
 */
package mx.randalf.protocol.randalfGoogleXls;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;

/**
 * @author massi
 *
 */
public abstract class GoogleXlsReadOnly extends GoogleXlsTools {

	static {
		SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY);
	}

	/**
	 * 
	 */
	public GoogleXlsReadOnly() {
		super();
	}

	@Override
	protected Sheets getSheetsService(File clientSecret) throws IOException {
		Credential credential = authorize(clientSecret);
		return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME)
				.build();
	}

}
