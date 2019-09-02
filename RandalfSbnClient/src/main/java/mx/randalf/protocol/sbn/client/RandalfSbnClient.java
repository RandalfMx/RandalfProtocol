/**
 * 
 */
package mx.randalf.protocol.sbn.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.marc4j.MarcException;
import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.MarcStreamWriter;
import org.marc4j.MarcWriter;
import org.marc4j.marc.Record;

import mx.randalf.protocol.sbn.metadata.RandalfMetadata;

/**
 * @author massi
 *
 */
public class RandalfSbnClient {

	private Logger log = Logger.getLogger(RandalfSbnClient.class);

	private String urlOpacSbn = "https://opac.sbn.it/opacsbn/opaclib";

	private String queryStringOpacSbn = "?db=solr_iccu" + "&select_db=solr_iccu" + "&nentries=1" + "&from=1"
			+ "&searchForm=opac/iccu/error.jsp" + "&resultForward=opac/iccu/scarico_uni.jsp" + "&do_cmd=search_show_cmd"
			+ "&format=unimarc" + "&totalResult=1" + "&fname=none";

	/**
	 * 
	 */
	public RandalfSbnClient() {
	}

	public RandalfSbnClient(String urlOpacSbn) {
		this.urlOpacSbn = urlOpacSbn;
	}

	public void bidMarc(String bid, String fileMrc) throws MalformedURLException, IOException {
		MarcReader reader = null;
		MarcWriter writer = null;
		FileOutputStream fos = null;
		File f = null;
		Record record = null;

		try {

			reader = open(bid);
			
			f = new File(fileMrc);
			fos = new FileOutputStream(f);
			writer = new MarcStreamWriter(fos);
			while (reader.hasNext()) {
				try {
					record = reader.next();
					writer.write(record);
				} catch(MarcException e) {
				}
			}
		} catch (MalformedURLException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw e;
		} finally {
			try {
				if (fos != null) {
					fos.flush();
					fos.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	private MarcReader open(String bid) throws MalformedURLException, IOException {
		MarcReader reader = null;
		String urlOpac = null;
		URL url = null;

		try {
			urlOpac = componiUrl(bid);

			url = new URL(urlOpac);

			reader = new MarcStreamReader(url.openStream(), "UTF-8");
		} catch (MalformedURLException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
		
		return reader;
	}

	public Vector<RandalfMetadata> bidToMetaData(String bid) throws MalformedURLException, IOException {
		MarcReader reader = null;
		Record record = null;
		Vector<RandalfMetadata> metaDatas = null;

		try {
			reader = open(bid);
			metaDatas = new Vector<RandalfMetadata>();
			while (reader.hasNext()) {
				try {
					record = reader.next();
					metaDatas.add(new RandalfMetadata(record));
				} catch(MarcException e) {
				}
			}
		} catch (MalformedURLException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		return metaDatas;
	}

	private String componiUrl(String bid) {
		String urlOpac = null;

		urlOpac = urlOpacSbn;
		urlOpac += queryStringOpacSbn;
		urlOpac += "&rpnlabel=+Identificativo+SBN+%3D+" + bid + "+%28parole+in+AND%29+";
		urlOpac += "&rpnquery=%40attrset+bib-1++%40attr+1%3D1032+%40attr+4%3D6+%22" + bid + "%22";
		return urlOpac;
	}
}
