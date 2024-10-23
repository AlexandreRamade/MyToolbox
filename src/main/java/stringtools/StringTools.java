package stringtools;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTools {

    /**
     * isNumeric <br>
     * <p>
     * Méthode utilitaire déterminant si une chaine de caractère contient une valeur numérique
     * </p>
     *
     * @param strNum chaine de caractère à tester
     * @return true si la chaine contient un nombre entier ou décimal relatif, false sinon
     */
    public static boolean isNumeric(String strNum) {
        if (strNum != null) {
            return strNum.matches("-?\\d+(\\.\\d+)?");
        }
        return false;
    }


    /**
     * isEmptyOrBlank <br>
     * <p>
     * Méthode utilitaire permettant de déterminer si une chaine de caractère est nulle ou ne contient que des caractères blancs
     * </p>
     *
     * @param s chaine à tester
     * @return true si nulle, vide ou ne contenant que des caractères blancs, fasle sinon
     */
    public static boolean isEmptyOrBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    /**
     * extractFirstOccurrence <br>
     * <p>
     * Methode utilitaire permettant d'extraire d'une chaine de caractère le premier élément correspondant au pattern
     * </p>
     *
     * @param pattern à extraire
     * @param source  chaine de caractère
     * @return valeur extraite correspondant au pattern si trouvée, null sinon
     */
    public static String extractFirstOccurrence(Pattern pattern, String source) {
        Matcher matcher = pattern.matcher(source);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    /**
     * extractAllOccurrences <br>
     * <p>
     * Methode utilitaire permettant d'extraire tous les éléments correspondant au pattern d'une chaine de caractère
     * et les retourne dans une liste
     * </p>
     *
     * @param pattern à extraire
     * @param source  chaine de caractère
     * @return la liste des valeurs extraites et correspondant au pattern
     */
    public static List<String> extractAllOccurrences(Pattern pattern, String source) {
        List<String> liste = new ArrayList<>();
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            liste.add(matcher.group());
        }
        return liste;
    }


    public static List<Integer> extractAllIntegerNumbers(String source) {
        List<Integer> integers = new ArrayList<>();
        Matcher matcher = Pattern.compile("\\d+").matcher(source);
        while (matcher.find()) {
            integers.add(Integer.valueOf(matcher.group()));
        }
        return integers;
    }
    
    public static String replaceNthOccurrence(String regex, int nth, String replacement, String source) {
    	Matcher matcher = Pattern.compile(regex).matcher(source);
    	StringBuilder sourceSB = new StringBuilder(source);
    	int currentOccurrence = 0;
    	while(matcher.find()) {
    		if(++currentOccurrence == nth) {
    			return sourceSB.replace(matcher.start(), matcher.end(), replacement).toString();
    		}
    	}
    	return source;
    }
    
    public static String replaceLast(String regex, String replacement, String source) {
    	int lastIndex = source.lastIndexOf(regex);
    	if(lastIndex != -1) {
    		return new StringBuilder(source).replace(lastIndex, lastIndex + regex.length(), replacement).toString();
    	}
    	return source;
    }
    
}
