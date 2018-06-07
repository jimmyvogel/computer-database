package com.excilys.cdb.validator;

public class SecurityTextValidation {

	// TEXT SECURITY
	public static final String INVALID_CHARACTERS = "\"<[^>]*>\"";
	public static final Integer MAX_LENGTH = 300;
	
    /**
     * Classe non instanciable.
     */
    private SecurityTextValidation() { }

    /**
     * Méthode pour valider un string.
     * @param s le string a valider
     * @return le resultat de la validation
     */
    public static boolean valideString(String s) {
        if (s == null) {
            return false;
        }
        if (s.length() > MAX_LENGTH) {
        	return false;
        }
        String test = s.replace(INVALID_CHARACTERS, "");
        return test.length() == s.length();
    }

}
