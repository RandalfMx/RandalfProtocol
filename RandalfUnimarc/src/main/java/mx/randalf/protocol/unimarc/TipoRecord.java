/**
 * 
 */
package mx.randalf.protocol.unimarc;

/**
 * @author massi
 *
 */
public enum TipoRecord {
	a("materiale a stampa"), 
	b("materiale manoscritto"), 
	c("partiture musicali a stampa"),
	d("partiture musicali manoscritte"), 
	e("materiale cartografico a stampa"), 
	f("materiale cartografico manoscritto"),
	g("materiali video e proiettato (film, filmine, diapositive, trasparenti, videoregistrazioni)"),
	i("registrazioni sonore non musicali"), 
	j("registrazioni sonore musicali"),
	k("grafica bidimensionale (dipinti, disegni etc.)"), 
	l("risorsa elettronica"), 
	m("materiale misto"),
	r("manufatti tridimensionali o oggetti presenti in natura");
	
    private final String value;


	TipoRecord(String v) {
        value = v;
	}

    public String value() {
        return value;
    }

    public static TipoRecord fromValue(String v) {
        for (TipoRecord c: TipoRecord.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
