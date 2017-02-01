/**
 * 
 */
package mx.randalf.protocol.amazonS3.protocol;

/**
 * @author massi
 *
 */
public enum TypeProtocol {
	MINIO("minio"),
	AMAZONAWS("amazonAws"),
	GOOGLECLOUD("googleGloud"),
	;
	
	public static final TypeProtocol DEFAULT = AMAZONAWS;

	private final String typeProtocol;

	private TypeProtocol(String typeProtocol){
		this.typeProtocol= typeProtocol;
	}

	public String getTypeProtocol(){
		return this.typeProtocol;
	}
}
