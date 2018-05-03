package com.excilys.cdb.validator;

public class TextValidation {

    /**
     * Méthode pour modifier en valide le format d'un string.
     * @param s le string a valider
     * @return String le string possiblement modifié valide.
     */
    public static String traitementString(String s) {
        if (s == null) {
            return null;
        }
        return s.trim().replaceAll("<[^>]*>", "");
    }

}
