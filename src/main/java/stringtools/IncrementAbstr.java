package stringtools;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class IncrementAbstr {
	
	public static final int BEFORE = 1;
	public static final int AFTER = 2;
	
	/**
	 * Compteur
	 */
	protected int count = 0;
	
	/**
	 * Option d'ajout de caractère si séquence limite atteinte
	 */
	protected boolean addNewChar = false;

	/**
	 * Active l'incrémentation après avoir restitué la première valeur
	 */
	protected boolean activeIncrement = false;
	
	/**
	 * initIncrement<br>
	 * <p>
	 * Méthode d'initialisation de la séquence à incrémenter
	 * </p>
	 *
	 * @param firstValue première valeur de la séquence
	 */
	public abstract void initIncrement(String firstValue);
	
	/* ***** ***** ***** ***** METHODES ***** ***** ***** ***** */

	/* ***** ***** METHODES D'APPEL ***** ***** */
	
	/**
	 * increment<br>
	 * <p>
	 * Méthode permettant d'obtenir la séquence incrémentée d'une unité
	 * </p>
	 *
	 * @return la séquence incrémentée d'une unité
	 */
	public abstract String increment();

	/**
	 * increment<br>
	 * <p>
	 * Méthode permettant d'obtenir une chaine de caractère incluant la séquence
	 * incrémentée d'une unité <br>
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
				.append(this.increment())
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
	 * @param position  {@link this#BEFORE} = séquence ajoutée en début de
	 *                  chaine, {@link this#AFTER} = séquence ajoutée en fin de
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
			return new StringBuilder(item).append(separator).append(this.increment()).toString();
		}
		return new StringBuilder(this.increment()).append(separator).append(item).toString();
	}
	
	
	/* ***** ***** ***** ***** GETTEURS / SETTEURS ***** ***** ***** ***** */
	
	public int getCount() {
		return count;
	}

	public abstract String getCurrentValue();

}
