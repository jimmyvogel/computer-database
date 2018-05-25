package com.excilys.cdb.validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateValidation {

    /**
     * Classe non instanciable.
     */
    private DateValidation() { }

    /**
     * Traitement de l'input de la date par l'utilisateur.
     * @param ligne le String tappé par l'utilisateur.
     * @return Une LocalDateTime spécifié par l'utilisateur ou null si une
     *         erreur.
     */
    public static LocalDateTime validDateFormat(final String ligne) {
        LocalDateTime dateTime = null;
        if (ligne != null) {
            String regex = "^\\d{4}-\\d{2}-\\d{2}$"; // (yyyy-MM-dd)
            String regexPlusTime = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}[.]\\d+";
            try {
                if (ligne.matches(regex)) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    String line = ligne + " 00:00";
                    dateTime = LocalDateTime.parse(line, formatter);
                }
                if (ligne.matches(regexPlusTime)) {
                    dateTime = LocalDateTime.parse(ligne);
                }
            } catch (java.time.format.DateTimeParseException e) {
                return null;
            }
        }
        return dateTime;
    }

    /**
     * Return if a date is strictly between two others.
     * @param ldt la date à valider
     * @param first la première date not included
     * @param last la seconde date not included
     * @return un boolean définissant la validité de la date
     */
    public static boolean validDateInBetween(LocalDateTime ldt, LocalDateTime first, LocalDateTime last) {
        return ldt.isAfter(first) && ldt.isBefore(last);
    }

}
