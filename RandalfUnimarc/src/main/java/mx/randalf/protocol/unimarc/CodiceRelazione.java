/**
 * 
 */
package mx.randalf.protocol.unimarc;

/**
 * @author massi
 *
 */
public enum CodiceRelazione {
	_360("Acquafortista"),
	_010("Adattatore"),
	_570("Altro"),
	_018("Animatore"),
	_020("Annotatore"),
	_100("Antecedente bibliografico"),
	_030("Arrangiatore"),
	_040("Artista"),
	_050("Assegnatario [Detentore dei diritti di pubblicazione]"),
	_005("Attore"),
	_070("Autore"),
	_090("Autore del dialogo"),
	_770("Autore del fascicolo allegato"),
	_080("Autore dell'introduzione, ecc."),
	_305("Autore della dissertazione"),
	_075("Autore della postfazione, colophon, ecc."),
	_072("Autore in citazioni o estratti"),
	_330("Autore incerto"),
	_275("Ballerino"),
	_065("Banditore"),
	_170("Calligrafo"),
	_721("Cantante"),
	_180("Cartografo"),
	_190("Censore"),
	_202("Circense"),
	_205("Collaboratore"),
	_207("Comico"),
	_210("Commentatore"),
	_212("Commentatore di testo scritto"),
	_400("Committente"),
	_220("Compilatore"),
	_230("Compositore"),
	_902("Compositore della musica parafrasata"),
	_255("Consulente di progetto"),
	_695("Consulente scientifico"),
	_257("Continuatore"),
	_555("Controrelatore"),
	_700("Copista, scriba"),
	_200("Coreografo"),
	_270("Correttore"),
	_640("Correttore delle bozze"),
	_340("Curatore [Editor]"),
	_370("Curatore del filmato"),
	_273("Curatore di una mostra"),
	_290("Dedicante [Autore della dedica]"),
	_280("Dedicatario"),
	_130("Designer del libro"),
	_140("Designer della copertina"),
	_120("Designer della legatura"),
	_150("Designer delle tavole"),
	_660("Destinatario"),
	_587("Detentore del brevetto"),
	_250("Direttore d'orchestra"),
	_673("Direttore della ricerca"),
	_195("Direttore di coro"),
	_651("Direttore editoriale"),
	_901("Disegnatore"),
	_740("Disegnatore dei caratteri"),
	_310("Distributore"),
	_320("Donatore"),
	_650("Editore"),
	_595("Ente di ricerca"),
	_725("Ente di standardizzazione"),
	_590("Esecutore, interprete"),
	_365("Esperto"),
	_380("Falsificatore"),
	_720("Firmatario"),
	_450("Firmatario della dedica"),
	_395("Fondatore"),
	_600("Fotografo"),
	_295("Garante del titolo accademico"),
	_410("Grafico"),
	_245("Ideatore"),
	_440("Illustratore"),
	_240("Impaginatore"),
	_903("Impresario"),
	_350("Incisore"),
	_760("Incisore su legno"),
	_530("Incisore su metallo"),
	_460("Intervistato"),
	_470("Intervistatore"),
	_900("Inventore"),
	_584("Inventore del brevetto"),
	_110("Legatore [Rilegatore]"),
	_160("Libraio [Venditore]"),
	_480("Librettista"),
	_490("Licenziatario"),
	_500("Licenziatore"),
	_510("Litografo"),
	_A03("Luogo di copia"),
	_A06("Luogo di destinazione"),
	_A04("Luogo di provenienza"),
	_637("Manager di progetto"),
	_655("Marionettista"),
	_677("Membro del gruppo di ricerca"),
	_535("Mimo"),
	_430("Miniatore"),
	_545("Musicista"),
	_550("Narratore"),
	_060("Nome associato"),
	_A05("Nome estratto dal titolo"),
	_420("Onorato"),
	_557("Organizzatore dell'incontro"),
	_560("Originatore"),
	_540("Osservatore"),
	_520("Paroliere"),
	_633("Personale di produzione"),
	_390("Possessore precedente"),
	_605("Presentatore"),
	_630("Produttore"),
	_580("Produttore della carta"),
	_635("Programmatore"),
	_675("Recensore [Revisore]"),
	_710("Redattore"),
	_300("Regista [Direttore]"),
	_A07("Regista teatrale"),
	_727("Relatore"),
	_A01("Restauratore"),
	_582("Richiedente del brevetto"),
	_680("Rubricatore"),
	_690("Sceneggiatore"),
	_632("Scenografo"),
	_705("Scultore"),
	_723("Sponsor"),
	_620("Stampatore delle tavole [Stampatore dei clichés]"),
	_726("Stuntman"),
	_670("Tecnico della registrazione"),
	_610("Tipografo (Stampatore)"),
	_260("Titolare dei diritti d'autore"),
	_730("Traduttore"),
	_753("Venditore"),
	_910("Vidit"),
	_755("Vocalist");
	
    private final String value;

    CodiceRelazione(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CodiceRelazione fromValue(String v) {
        for (CodiceRelazione c: CodiceRelazione.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}
