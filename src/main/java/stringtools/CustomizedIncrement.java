package stringtools;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CustomizedIncrement extends IncrementAbstr {

	/**
	 * Tableau d'index de la séquence à incrémenter
	 */
	private int[] indexTab = new int[1];

	/**
	 * Valeurs de l'incrémentation personnalisée
	 */
	private String[] incrementValues = new String[0];

	/* ***** ***** ***** ***** CONSTRUCTEUR ***** ***** ***** ***** */

	/**
	 * Constructeur par défaut <br>
	 * <p>
	 * Instancie un CustomizedIncrement avec une liste constituant les valeurs à
	 * utiliser lors de l'incrémentation. Par défaut la première valeur utilisée
	 * sera la première valeur de la liste.
	 * </p>
	 * 
	 * @param incrementValues tableau des valeurs à utiliser lors de
	 *                        l'incrémentation
	 */
	public CustomizedIncrement(String[] incrementValues) {
		this.incrementValues = incrementValues;
	}

	/**
	 * Constructeur avec index de la première valeur <br>
	 * <p>
	 * Instancie un CustomizedIncrement avec une liste constituant les valeurs à
	 * utiliser lors de l'incrémentation et l'index de la valeur à retourner lors du
	 * premier appel
	 * </p>
	 * <u>Alternative à {@link this#CustomizedIncrement(String, String[])} lorsque
	 * la longueur des valeurs dans le tableau d'incrémentation n'est pas constante
	 * </u>
	 * 
	 * @param firstValueIndex index de la première valeur à utiliser
	 * @param incrementValues tableau des valeurs à utiliser lors de
	 *                        l'incrémentation
	 */
	public CustomizedIncrement(int firstValueIndex, String[] incrementValues) {
		this.incrementValues = incrementValues;
		int[] tab = { firstValueIndex };
		initIncrement(tab);
	}

	/**
	 * Constructeur avec tableau d'index de la première séquence et options <br>
	 * <p>
	 * Instancie un CustomizedIncrement avec une liste constituant les valeurs à
	 * utiliser lors de l'incrémentation et tableau d'index de la séquence à
	 * retourner lors du premier appel.<br>
	 * L'option addNewChar permet d'ajouter une nouvelle valeur en début de séquence
	 * lorsque tous les éléments du tableau d'incrémentation ont été passés <br>
	 * L'option incrementOnFirstCall permet de définir si la première valeur
	 * retournée doit être la valeur définie à l'initialisation ou la valeur
	 * incrémentée
	 * </p>
	 * <u>Alternative à {@link this#CustomizedIncrement(String, String[], boolean,
	 * boolean)} lorsque la longueur des valeurs dans le tableau d'incrémentation
	 * n'est pas constante </u>
	 * 
	 * @param firstValuesIndex     index de la première séquence à utiliser
	 * @param incrementValues      tableau des valeurs à utiliser lors de
	 *                             l'incrémentation
	 * @param addNewChar           active ou non l'ajout d'un nouveau caractère
	 *                             lorsque la séquence ateint sa plus grande valeur
	 *                             possible
	 * @param incrementOnFirstCall définit si le premier appel à la méthode
	 *                             increment() retourne la première valeur ou son
	 *                             incrémentation
	 */
	public CustomizedIncrement(int[] firstValuesIndex, String[] incrementValues, boolean addNewChar,
			boolean incrementOnFirstCall) {
		this.incrementValues = incrementValues;
		this.addNewChar = addNewChar;
		this.activeIncrement = incrementOnFirstCall;
		initIncrement(firstValuesIndex);
	}

	/**
	 * Constructeur avec séquence de la première valeur <br>
	 * <p>
	 * Instancie un CustomizedIncrement avec une liste constituant les valeurs à
	 * utiliser lors de l'incrémentation et la séquence à retourner lors du premier
	 * appel
	 * </p>
	 * <u>A utiliser uniquement si la longueur des valeurs dans le tableau
	 * d'incrémentation est pas constante pour tous les éléments. <br>
	 * Dans le cas contraire, la séquence firstValue ne poura pas être interprétée.
	 * Utiliser alors {@link this#CustomizedIncrement(int, String[])} </u>
	 * 
	 * @param firstValue      valeur initiale
	 * @param incrementValues tableau des valeurs à utiliser lors de
	 *                        l'incrémentation
	 */
	public CustomizedIncrement(String firstValue, String[] incrementValues) {
		this.incrementValues = incrementValues;
		initIncrement(firstValue);
	}

	/**
	 * Constructeur avec séquence de la première valeur et options <br>
	 * <p>
	 * Instancie un CustomizedIncrement avec une liste constituant les valeurs à
	 * utiliser lors de l'incrémentation et la séquence à retourner lors du premier
	 * appel<br>
	 * L'option addNewChar permet d'ajouter une nouvelle valeur en début de séquence
	 * lorsque tous les éléments du tableau d'incrémentation ont été passés <br>
	 * L'option incrementOnFirstCall permet de définir si la première valeur
	 * retournée doit être la valeur définie à l'initialisation ou la valeur
	 * incrémentée
	 * </p>
	 * <u>A utiliser uniquement si la longueur des valeurs dans le tableau
	 * d'incrémentation est pas constante pour tous les éléments. <br>
	 * Dans le cas contraire, la séquence firstValue ne poura pas être interprétée.
	 * Utiliser alors {@link this#CustomizedIncrement(int[], String[], boolean,
	 * boolean)} </u>
	 * 
	 * @param firstValue           valeur initiale
	 * @param incrementValues      tableau des valeurs à utiliser lors de
	 *                             l'incrémentation.
	 * @param addNewChar           active ou non l'ajout d'un nouveau caractère
	 *                             lorsque la séquence ateint sa plus grande valeur
	 *                             possible
	 * @param incrementOnFirstCall définit si le premier appel à la méthode
	 *                             increment() retourne la première valeur ou son
	 *                             incrémentation
	 */
	public CustomizedIncrement(String firstValue, String[] incrementValues, boolean addNewChar,
			boolean incrementOnFirstCall) {
		this.incrementValues = incrementValues;
		this.addNewChar = addNewChar;
		this.activeIncrement = incrementOnFirstCall;
		initIncrement(firstValue);
	}

	public void initIncrement(int[] firstValuesIndex) {
		indexTab = firstValuesIndex;
	}

	/**
	 * initIncrement<br>
	 * <p>
	 * Méthode d'initialisation : découpe la première valeur de la séquence en
	 * tableau d'index correspondant aux valeurs définies dans le tableau
	 * d'incrémentation
	 * </p>
	 * <u> ATTENTION : cette méthode ne peut être utilisé que si toutes les valeurs
	 * présentent dans le tableau d'incrémentation personnalisé ont le même nombre
	 * de caractères. Dans le cas contraire, la chaine de valeur initiale ne peut
	 * pas être découpé pour identifier les valeurs de la première incrémentation.
	 * ALTERNATIVE : si les valeurs de références n'ont pas toutes la même longeur,
	 * utiliser la méthode {@link this#initIncrement(int[])} </u>
	 *
	 *
	 * @param firstValue première valeur de la séquence
	 * @see {this{@link #getStringsLength(List)}
	 */
	@Override
	public void initIncrement(String firstValue) {
		int incrementValuesLength = getStringsLength(Arrays.asList(incrementValues));
		if (firstValue != null && incrementValuesLength > 0) {
			indexTab = new int[firstValue.length() / incrementValuesLength];
			for (int j = 0; j < indexTab.length; j += incrementValuesLength) {
				for (int i = 0; i < incrementValues.length; i++) {
					if (firstValue.substring(j, j + incrementValuesLength).equals(incrementValues[i])) {
						indexTab[j] = i;
					}
				}
			}
		}
	}

	/* ***** ***** ***** ***** METHODES ***** ***** ***** ***** */

	/* ***** ***** METHODES D'APPEL ***** ***** */
	@Override
	public String increment() {
		this.count++;
		if (this.activeIncrement) {
			incrementIndex(indexTab.length - 1);
		} else {
			this.activeIncrement = !this.activeIncrement;
		}
		return transcriptIndexToIncrementSequence();
	}

	/* ***** ***** METHODES UTILITAIRES ***** ***** */

	/**
	 * transcriptIndexToIncrementSequence <br>
	 * <p>
	 * Reconstitue la séquence de caractère à partir du tableau d'index et du
	 * tableau des valeurs d'incrémentation personnalisé
	 * </p>
	 * 
	 * @return
	 */
	private String transcriptIndexToIncrementSequence() {
		StringBuilder sequence = new StringBuilder();
		for (int i : indexTab) {
			sequence.append(incrementValues[i]);
		}
		return sequence.toString();
	}

	/**
	 * getStringsLength <br>
	 * <p>
	 * retourne la longueur des chaines de caractères d'une liste de String
	 * uniquement si tous les éléments de la liste ont la même longeur. Dans le car
	 * contraire, retourne -1
	 * </p>
	 * 
	 */
	private int getStringsLength(List<String> values) {
		if (!values.isEmpty()) {
			int lengthRef = values.get(0).length();
			if (values.stream().allMatch(v -> v.length() == lengthRef)) {
				return lengthRef;
			}
		}
		return -1;
	}

	/* ***** ***** METHODES DE TRAITEMENT ***** ***** */

	/**
	 * incrementIndex<br>
	 * <p>
	 * Méthode permetant d'incrémenter la chaine de caractère
	 * </p>
	 * <div>
	 * <ul>
	 * <li>si le caractère a atteint la limite de sa séquence, réinitialise le
	 * caractère et incrémente le caractère supérieur par récursivité</li>
	 * <li>sinon, incrémente le caractère d'une unité</li>
	 * <li>si tous les caractères de la séquence ont ateint leur limite ET que
	 * l'option est activée, alors ajoute un caractère suplémentaire au début de la
	 * séquence</li>
	 * </ul>
	 * </div>
	 * 
	 * @param currentIndex index du caractère à traiter
	 */
	private void incrementIndex(int currentIndex) {
		if (currentIndex > -1) {
			if (indexTab[currentIndex] == (incrementValues.length - 1)) {
				indexTab[currentIndex] = 0;
				incrementIndex(currentIndex - 1);
			} else {
				++indexTab[currentIndex];
			}
		} else if (this.addNewChar) {
			indexTab = new int[indexTab.length + 1];
			for (int i = 0; i < indexTab.length; i++) {
				indexTab[i] = 0;
			}
		}
	}

	/* ***** ***** ***** ***** GETTEURS / SETTEURS ***** ***** ***** ***** */

	@Override
	public String getCurrentValue() {
		return transcriptIndexToIncrementSequence();
	}
}
