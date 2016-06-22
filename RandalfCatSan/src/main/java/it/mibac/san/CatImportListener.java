package it.mibac.san;

import javax.xml.bind.Marshaller.Listener;

public class CatImportListener extends Listener {

	public CatImportListener() {
	}

	/**
	 * @see javax.xml.bind.Marshaller.Listener#beforeMarshal(java.lang.Object)
	 */
	@Override
	public void beforeMarshal(Object source) {
		
		super.beforeMarshal(source);
	}

	/**
	 * @see javax.xml.bind.Marshaller.Listener#afterMarshal(java.lang.Object)
	 */
	@Override
	public void afterMarshal(Object source) {
		super.afterMarshal(source);
	}

}
