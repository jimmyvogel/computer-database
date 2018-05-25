package com.excilys.cdb.validator;

public class SecurityTextValidation {

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
        String test = s.replace("<[^>]*>", "");
        return test.length() == s.length();
    }

}
