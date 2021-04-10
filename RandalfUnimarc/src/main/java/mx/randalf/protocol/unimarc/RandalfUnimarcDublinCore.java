/**
 * 
 */
package mx.randalf.protocol.unimarc;

import java.util.Collection;

import org.marc4j.marc.Record;
import org.purl.dc.elements._1.SimpleLiteral;

import it.sbn.iccu.metaag1.Bib;
import it.sbn.iccu.metaag1.Bib.Holdings;
import it.sbn.iccu.metaag1.Bib.LocalBib;
import it.sbn.iccu.metaag1.Bib.Piece;
import it.sbn.iccu.metaag1.BibliographicLevel;
import mx.randalf.mag.MagXsd;

/**
 * @author massi
 *
 */
public abstract class RandalfUnimarcDublinCore extends RandalfUnimarc<Bib> {

	protected MagXsd magXsd = null;

	/**
	 * @param prermissiveReader
	 * @param toUtf8
	 */
	public RandalfUnimarcDublinCore() {
		super(true, true);
		magXsd = new MagXsd(null);
	}

	@Override
	protected Bib init(Record record) {
		Bib bib = null;
		Piece piece = null;
		Collection<? extends SimpleLiteral> value = null;
		Collection<? extends LocalBib> localBibs = null;
		Collection<? extends Holdings> holdings = null;

		bib = new Bib();
//		System.out.println("------------------");
//		System.out.println("Bid: " + getCF(record.getControlFields(), "001").get(0).getData());
		
		bib.setLevel(getLevel(record));
		bib.getIdentifier().addAll(getIdentifier(record));

		value = getTitle(record);
		if (value != null) {
			bib.getTitle().addAll(value);
		}

		value = getCreator(record);
		if (value != null) {
			bib.getCreator().addAll(value);
		}

		value = getPublisher(record);
		if (value != null) {
			bib.getPublisher().addAll(value);
		}

		value = getSubject(record);
		if (value != null) {
			bib.getSubject().addAll(value);
		}

		value = getDescription(record);
		if (value != null) {
			bib.getDescription().addAll(value);
		}

		value = getContributor(record);
		if (value != null) {
			bib.getContributor().addAll(value);
		}

		value = getDate(record);
		if (value != null) {
			bib.getDate().addAll(value);
		}

		value = getType(record);
		if (value != null) {
			bib.getType().addAll(value);
		}

		value = getFormat(record);
		if (value != null) {
			bib.getFormat().addAll(value);
		}

		value = getSource(record);
		if (value != null) {
			bib.getSource().addAll(value);
		}

		value = getLanguage(record);
		if (value != null) {
			bib.getLanguage().addAll(value);
		}

		value = getRelation(record);
		if (value != null) {
			bib.getRelation().addAll(value);
		}

		value = getCoverage(record);
		if (value != null) {
			bib.getCoverage().addAll(value);
		}

		value = getRights(record);
		if (value != null) {
			bib.getRights().addAll(value);
		}

		holdings = getHoldings(record);
		if (holdings != null) {
			bib.getHoldings().addAll(holdings);
		}

		localBibs = getLocalBib(record);
		if (localBibs != null) {
			bib.getLocalBib().addAll(localBibs);
		}

		piece = getPiece(record);
		if (piece != null) {
			bib.setPiece(piece);
		}
		return bib;
	}

	protected abstract BibliographicLevel getLevel(Record record);

	protected abstract Collection<? extends SimpleLiteral> getIdentifier(Record record);

	protected abstract Collection<? extends SimpleLiteral> getTitle(Record record);

	protected abstract Collection<? extends SimpleLiteral> getCreator(Record record);

	protected abstract Collection<? extends SimpleLiteral> getPublisher(Record record);

	protected abstract Collection<? extends SimpleLiteral> getSubject(Record record);

	protected abstract Collection<? extends SimpleLiteral> getDescription(Record record);

	protected abstract Collection<? extends SimpleLiteral> getContributor(Record record);

	protected abstract Collection<? extends SimpleLiteral> getDate(Record record);

	protected abstract Collection<? extends SimpleLiteral> getType(Record record);

	protected abstract Collection<? extends SimpleLiteral> getFormat(Record record);

	protected abstract Collection<? extends SimpleLiteral> getSource(Record record);

	protected abstract Collection<? extends SimpleLiteral> getLanguage(Record record);

	protected abstract Collection<? extends SimpleLiteral> getRelation(Record record);

	protected abstract Collection<? extends SimpleLiteral> getCoverage(Record record);

	protected abstract Collection<? extends SimpleLiteral> getRights(Record record);

	protected abstract Collection<? extends Holdings> getHoldings(Record record);

	protected abstract Collection<? extends LocalBib> getLocalBib(Record record);

	protected abstract Piece getPiece(Record record);

}
