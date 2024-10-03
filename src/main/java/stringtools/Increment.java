package stringtools;

import java.util.List;
import java.util.Objects;
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
public class Increment {

	public static final int BEFORE = 1;
	public static final int AFTER = 2;

	/**
	 * Compteur
	 */
	private int count = 1;

	/**
	 * Séquence incrémentée
	 */
	private char[] chars;

	/**
	 * Difini si tous les caractères ont le même type
	 */
	private IncrementType uniqueType;

	/**
	 * Option d'ajout de caractère si séquence limite atteinte
	 */
	private boolean addNewChar = false;

	/**
	 * Active l'incrémentation après avoir restitué la première valeur
	 */
	private boolean activeIncrement = false;

	/**
	 * Défini les types et les limites des caractères incrémentables
	 */
	private enum IncrementType {
		DIGIT('0', '9'), LOWERCASE('a', 'z'), UPPERCASE('A', 'Z');

		/**
		 * première valeur de la séquence
		 */
		private char start;
		/**
		 * dernière valeur possible de la séquence
		 */
		private char end;

		private IncrementType(char start, char end) {
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
		super();
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
		super();
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
	public String increment() {
		if (activeIncrement) {
			this.count++;
			this.chars = incrementChars(chars, chars.length - 1);
		} else {
			activeIncrement = !activeIncrement;
		}
		return String.valueOf(chars);
	}

	/**
	 * increment<br>
	 * <p>
	 * Méthode permettant d'obtenir une chaine de caractère incluant la séquence
	 * incrémenté d'une unité <br>
	 * Format de la chaine : prefix-sequenceincrémentée-sufixe.extension
	 * </p>
	 *
	 * @param prefix
	 * @param prefixSeparator
	 * @param suffixSeparator
	 * @param suffix
	 * @param extension
	 * @return chaine contenant la séquence incrémentée
	 */
	public String increment(String prefix, String prefixSeparator, String suffixSeparator, String suffix,
			String extension) {
		StringBuilder result = new StringBuilder(prefix).append(prefixSeparator);
		if (activeIncrement) {
			this.count++;
			this.chars = incrementChars(chars, chars.length - 1);
		} else {
			activeIncrement = !activeIncrement;
		}
		return result.append(chars).append(suffixSeparator).append(suffix).append(extension).toString();
	}

	/**
	 * getIncrementedList </br>
	 * <p>
	 * Méthode générant une liste de séquences incrémentées de taille indiquée en
	 * paramétre
	 * </p>
	 * 
	 * @param listSize taille de la liste de séquences à générée
	 * @return la liste des séquences incrémentée
	 */
	public List<String> getIncrementedList(int listSize) {
		return IntStream.range(0, listSize).mapToObj(i -> this.increment()).collect(Collectors.toList());
	}

	/**
	 * addIncrementedSequenceToListItems </br>
	 * <p>
	 * Méthode permétant d'ajouter une séquence incrémentée en début ou en fin de
	 * chaine de caractère pour chaque éléments d'une liste de chaine de caractère
	 * </p>
	 * 
	 * @param list      liste des chaines de caractères
	 * @param separator séparateur
	 * @param position  {@link Increment#BEFORE} = séquence ajoutée en début de
	 *                  chaine, {@link Increment#AFTER} = séquence ajoutée en fin de
	 *                  chaine
	 * @return la liste dont la séquence incrémentée à été ajoutée à chaque élément
	 */
	public List<String> addIncrementedSequenceToListItems(List<String> list, String separator, int position) {
		return list.stream().map(str -> addIncrementedSequenceToListItem(str, separator, position))
				.collect(Collectors.toList());
	}


	/* ***** ***** METHODES UTILITAIRES ***** ***** */

	
	private String addIncrementedSequenceToListItem(String item, String separator, int position) {
		if (position == AFTER) {
			return new StringBuilder(item).append(separator).append(increment()).toString();
		}
		return new StringBuilder(increment()).append(separator).append(item).toString();
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
			IncrementType incrementType = Character.isDigit(chars[index]) ? IncrementType.DIGIT
					: Character.isUpperCase(chars[index]) ? IncrementType.UPPERCASE : IncrementType.LOWERCASE;

			if (chars[index] == incrementType.end) {
				chars[index] = incrementType.start;
				return incrementChars(chars, index - 1);
			} else {
				++chars[index];
			}
		} else if (addNewChar && Objects.nonNull(uniqueType)) {
			chars = new char[chars.length + 1];
			for (int i = 0; i < chars.length; i++) {
				chars[i] = uniqueType.start;
			}
			if (uniqueType == IncrementType.DIGIT) {
				chars[0] = '1';
			}
		}
		return chars;
	}

	/* ***** ***** ***** ***** GETTEURS / SETTEURS ***** ***** ***** ***** */

	public int getCount() {
		return count;
	}

	public String getCurrentValue() {
		return String.valueOf(chars);
	}

}
