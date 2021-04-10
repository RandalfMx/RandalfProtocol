/**
 * 
 */
package mx.randalf.protocol.unimarc.implement;

import java.util.Collection;
import java.util.Vector;

import org.marc4j.marc.Record;
import org.purl.dc.elements._1.SimpleLiteral;

/**
 * @author massi
 *
 */
public abstract class RandalfUnimarcLibroModerno extends RandalfUnimarcLibroAntico {

	/**
	 * @param segnatura
	 */
	public RandalfUnimarcLibroModerno(Vector<String> segnatura, String nomeIstituto, String codiceIsil) {
		super(segnatura, nomeIstituto, codiceIsil);
	}

	@Override
	protected Collection<? extends SimpleLiteral> getDescription(Record record) {
		Vector<SimpleLiteral> result = null;
		Vector<SimpleLiteral> result2 = null;

		result = getDescription950_316(record);

		result2 = getDescription300(record);
		if (result2 != null) {
			if (result == null) {
				result = new Vector<SimpleLiteral>();
			}
			result.addAll(result2);
		}

		return result;
	}

	@Override
	protected Collection<? extends SimpleLiteral> getRelation(Record record) {
		Vector<SimpleLiteral> result = null;
		Vector<SimpleLiteral> result2 = null;

		result = getRelation410(record);

		result2 = getRelation461(record);
		if (result2!= null) {
			if (result ==null) {
				result = new Vector<SimpleLiteral>();
			}
			result.addAll(result2);
		}

		result2 = getRelation462(record);
		if (result2!= null) {
			if (result ==null) {
				result = new Vector<SimpleLiteral>();
			}
			result.addAll(result2);
		}

		result2 = getRelation463(record);
		if (result2!= null) {
			if (result ==null) {
				result = new Vector<SimpleLiteral>();
			}
			result.addAll(result2);
		}

		result2 = getRelation464(record);
		if (result2!= null) {
			if (result ==null) {
				result = new Vector<SimpleLiteral>();
			}
			result.addAll(result2);
		}

		result2 = getRelation500(record);
		if (result2!= null) {
			if (result ==null) {
				result = new Vector<SimpleLiteral>();
			}
			result.addAll(result2);
		}

		result2 = getRelation510(record);
		if (result2!= null) {
			if (result ==null) {
				result = new Vector<SimpleLiteral>();
			}
			result.addAll(result2);
		}

		result2 = getRelation517(record);
		if (result2!= null) {
			if (result ==null) {
				result = new Vector<SimpleLiteral>();
			}
			result.addAll(result2);
		}
		return result;
	}

	@Override
	protected Collection<? extends SimpleLiteral> getPublisher(Record record) {
		Vector<SimpleLiteral> result = null;

		result = getPublisher210(record);
		return result;
	}

}
