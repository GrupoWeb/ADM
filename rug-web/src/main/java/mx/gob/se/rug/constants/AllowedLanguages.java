/**
 * 
 */
package mx.gob.se.rug.constants;

/**
 * @author Topiltzin Dominguez
 *
 */
public enum AllowedLanguages {

	es("es");
//	,en("en");
	
	private final String language;
	
	private AllowedLanguages(String language){
		this.language = language;
	}
	
	public String getLanguage() {
		return language;
	}
}
