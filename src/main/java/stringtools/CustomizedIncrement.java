package stringtools;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CustomizedIncrement {
	
	public static final int BEFORE = 1;
	public static final int AFTER = 2;
	
	/**
	 * Compteur
	 */
	private int count = 1;
		
	private int[] indexTab = new int[1];

	private String[] incrementValues = new String[0];
	
	/**
	 * Option d'ajout de caractère si séquence limite atteinte
	 */
	private boolean addNewChar = false;

	/**
	 * Active l'incrémentation après avoir restitué la première valeur
	 */
	private boolean activeIncrement = false;
	
	/* ***** ***** ***** ***** CONSTRUCTEUR ***** ***** ***** ***** */
	public CustomizedIncrement(String[] incrementValues) {
		this.incrementValues = incrementValues;
	}
	
	public CustomizedIncrement(int firstValueIndex, String[] incrementValues) {
		this.incrementValues = incrementValues;
		int[] tab = {firstValueIndex};
		initIncrement(tab);
	}
	
	public CustomizedIncrement(int[] firstValuesIndex, String[] incrementValues, boolean addNewChar, boolean incrementOnFirstCall) {
		this.incrementValues = incrementValues;
		this.addNewChar = addNewChar;
		this.activeIncrement = incrementOnFirstCall;
		initIncrement(firstValuesIndex);
	}
	
	public CustomizedIncrement(String firstValue, String[] incrementValues) {
		this.incrementValues = incrementValues;
		initIncrement(firstValue);
	}
	
	public CustomizedIncrement(String firstValue, String[] incrementValues, boolean addNewChar, boolean incrementOnFirstCall) {
		this.incrementValues = incrementValues;
		this.addNewChar = addNewChar;
		this.activeIncrement = incrementOnFirstCall;
		initIncrement(firstValue);
	}
	
	public void initIncrement(int[] firstValuesIndex) {
		indexTab = firstValuesIndex;
	}

	public void initIncrement(String firstValue) {
		int incrementValuesLength = getStringsLength(Arrays.asList(incrementValues));
		if(firstValue != null && incrementValuesLength > 0) {
			indexTab = new int[firstValue.length() / incrementValuesLength];
			for(int j = 0; j < indexTab.length; j += incrementValuesLength) {
				for(int i = 0; i < incrementValues.length; i++) {
					if(firstValue.substring(j, j + incrementValuesLength).equals(incrementValues[i])) {
						indexTab[j] = i;
					}
				}
			}
		}
	}
	
	
	/* ***** ***** ***** ***** METHODES ***** ***** ***** ***** */

	/* ***** ***** METHODES D'APPEL ***** ***** */
	public String increment() {
		if(activeIncrement) {
			incrementIndex(indexTab.length - 1);
		} else {
			activeIncrement = !activeIncrement;
		}
		return transcriptIndexToIncrementSequence();
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
		return new StringBuilder(prefix).append(prefixSeparator)
				.append(increment())
				.append(suffixSeparator).append(suffix)
				.append(extension).toString();
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
	
	private String transcriptIndexToIncrementSequence() {
		String sequence = "";
		for(int i : indexTab) {
			sequence += incrementValues[i];
		}
		return sequence;
	}
	
	private int getStringsLength(List<String> values) {
		if(!values.isEmpty()) {
			int lengthRef = values.get(0).length();
			if(values.stream().allMatch(v -> v.length() == lengthRef)) {
				return lengthRef;
			}
		}
		return -1;
	}
	
	private String addIncrementedSequenceToListItem(String item, String separator, int position) {
		if (position == AFTER) {
			return new StringBuilder(item).append(separator).append(increment()).toString();
		}
		return new StringBuilder(increment()).append(separator).append(item).toString();
	}

	/* ***** ***** METHODES DE TRAITEMENT ***** ***** */
	
	private void incrementIndex(int currentIndex) {
		count++;
		if(currentIndex > -1) {
			if(indexTab[currentIndex] == (incrementValues.length - 1)) {
				indexTab[currentIndex] = 0;
				incrementIndex(currentIndex - 1);
			}else {
				++indexTab[currentIndex];
			}
		} else if(addNewChar) {
			indexTab = new int[indexTab.length + 1];
			for(int i = 0; i < indexTab.length; i++) {
				indexTab[i] = 0;
			}
		}
	}
	
	
	/* ***** ***** ***** ***** GETTEURS / SETTEURS ***** ***** ***** ***** */

	public int getCount() {
		return count;
	}

	public String getCurrentValue() {
		return transcriptIndexToIncrementSequence();
	}
}
