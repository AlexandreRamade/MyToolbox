package stringtools;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * stringtools.Increment <br>
 * <p>
 * Classe permettant d'incrémenter facilement une séquence de chiffre et ou de
 * lettre
 * </p>
 *
 * @author a-ramade
 * @since 09/2022
 */
public class Increment extends IncrementAbstr {

	/**
	 * Séquence incrémentée
	 */
	private char[] chars;

	/**
	 * Défini si tous les caractères ont le même type
	 */
	private IncrementType uniqueType;

	
	/**
	 * Défini les types et les limites des caractères incrémentables
	 */
	private enum IncrementType {
		DIGIT('1', '0', '9'), LOWERCASE('a', 'a', 'z'), UPPERCASE('A', 'A', 'Z');

		/**
		 * caractère à ajouter en début de séquence
		 */
		private char newChar;
		
		/**
		 * première valeur de la séquence
		 */
		private char start;
		
		/**
		 * dernière valeur possible de la séquence
		 */
		private char end;

		private IncrementType(char newChar, char start, char end) {
			this.newChar = newChar;
			this.start = start;
			this.end = end;
		}
	}

	/* ***** ***** ***** ***** CONSTRUCTEUR ***** ***** ***** ***** */

	/**
	 * Constructeur par défaut<br>
	 * <p>
	 * Créé une instance de type stringtools.Increment permettant d'incrémenter
	 * facilement une séquence de caractère
	 * </p>
	 *
	 * @param firstValue valeur de départ
	 */
	public Increment(String firstValue) {
		this.initIncrement(firstValue);
	}

	/**
	 * Constructeur avec option<br>
	 * <p>
	 * Créé une instance de type stringtools.Increment permettant d'incrémenter
	 * facilement une séquence de caractère<br>
	 * L'option addNewChar permet d'ajouter un nouveau caractère UNIQUEMENT si la
	 * séquence contient des caractères de même type<br>
	 * L'option incrementOnFirstCall permet de définir si la première valeur
	 * retournée doit être la valeur définit à l'initialisation ou la valeur
	 * incrémentée
	 * </p>
	 * <div>
	 * <ul>
	 * <li>Exemple: valeur = 'ZZ'</li>
	 * <li>valeur suivante avec addNewChar = false : 'AA'</li>
	 * <li>valeur suivante avec addNewChar = true : 'AAA'</li>
	 * </ul>
	 * </div>
	 *
	 * @param firstValue           valeur de départ
	 * @param addNewChar           active ou non l'ajout d'un nouveau caractère
	 *                             lorsque la séquence ateint sa plus grande valeur
	 *                             possible
	 * @param incrementOnFirstCall définit si le premier appel à la méthode
	 *                             increment() retourne la première valeur ou son
	 *                             incrémentation
	 */
	public Increment(String firstValue, boolean addNewChar, boolean incrementOnFirstCall) {
		this.initIncrement(firstValue);
		this.addNewChar = addNewChar;
		this.activeIncrement = incrementOnFirstCall;
	}

	/**
	 * initIncrement<br>
	 * <p>
	 * Méthode d'initialisation : découpe la première valeur de la séquence en
	 * tableau de caractères
	 * </p>
	 *
	 * @param firstValue première valeur de la séquence
	 */
	@Override
	public void initIncrement(String firstValue) {
		if (firstValue != null && firstValue.trim().length() > 0) {
			this.chars = firstValue.toCharArray();
		}
		if (firstValue.matches("\\d+")) {
			this.uniqueType = IncrementType.DIGIT;
		} else if (firstValue.matches("[A-Z]+")) {
			this.uniqueType = IncrementType.UPPERCASE;
		} else if (firstValue.matches("[a-z]+")) {
			this.uniqueType = IncrementType.LOWERCASE;
		}
	}

	/* ***** ***** ***** ***** METHODES ***** ***** ***** ***** */

	/* ***** ***** METHODES D'APPEL ***** ***** */

	/**
	 * increment<br>
	 * <p>
	 * Méthode permettant d'obtenir la séquence incrémenté d'une unité
	 * </p>
	 *
	 * @return la séquence incrémentée d'une unité
	 */
	@Override
	public String increment() {
		this.count++;
		if (this.activeIncrement) {
			this.chars = incrementChars(chars, chars.length - 1);
		} else {
			this.activeIncrement = !this.activeIncrement;
		}
		return String.valueOf(chars);
	}

	
	/* ***** ***** METHODES DE TRAITEMENT ***** ***** */

	/**
	 * incrementChars<br>
	 * <p>
	 * Méthode permetant d'incrémenter la chaine de caractère
	 * </p>
	 * <div>
	 * <ul>
	 * <li>teste le type du caractère en cours</li>
	 * <li>si le caractère a atteint la limite de sa séquence, réinitialise le
	 * caractère et incrémente le caractère supérieur par récursivité</li>
	 * <li>sinon, incrémente le caractère d'une unité</li>
	 * <li>si tous les caractères de la séquence ont ateint leur limite ET qu'ils
	 * sont tous de même type ET que l'option est activée, alors ajoute un caractère
	 * suplémentaire au début de la séquence</li>
	 * </ul>
	 * </div>
	 *
	 * @param chars tableau de caractères à incrémenter
	 * @param index du caractère à traiter
	 */
	private char[] incrementChars(char[] chars, int index) {
		if (index > -1) {
			IncrementType incrementType = findCharType.apply(chars[index]);

			if (chars[index] == incrementType.end) {
				chars[index] = incrementType.start;
				return incrementChars(chars, index - 1);
			} else {
				++chars[index];
			}
		} else if (this.addNewChar && Objects.nonNull(uniqueType)) {
			chars = new char[chars.length + 1];
			chars[0] = uniqueType.newChar;
			for (int i = 1; i < chars.length; i++) {
				chars[i] = uniqueType.start;
			}
		}
		return chars;
	}
	
	private Function<Character, IncrementType> findCharType = (c) -> Character.isDigit(c) ? IncrementType.DIGIT : 
		Character.isUpperCase(c) ? IncrementType.UPPERCASE : 
			IncrementType.LOWERCASE;
	

	/* ***** ***** ***** ***** GETTEURS / SETTEURS ***** ***** ***** ***** */

	@Override
	public String getCurrentValue() {
		return String.valueOf(chars);
	}

}
