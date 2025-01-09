package listtools;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ListTools {

    /**
     * sameContent<br>
     * <p>
     * Méthode utilitaire permettant de comparer le contenu de deux collections sans tenir compte de
     * l'ordre des éléments
     * </p>
     *
     * @param collA
     * @param collB
     * @return true si tous les éléments sont présents dans les deux listes, false sinon
     */
    public static <T> boolean sameContent(Collection<T> collA, Collection<T> collB) {
        if (Objects.nonNull(collA) && Objects.nonNull(collB)) {
            return collA.size() == collB.size() && collA.containsAll(collB) && collB.containsAll(collA);
        }
        return Objects.isNull(collA) && Objects.isNull(collB);
    }

    /**
     * incremented <br>
     * <p>
     * Méthode utilitaire permettant de déterminer si une collection de nombre est bien incrémentée de N
     * en N entre la valeur minimale et la valeur maximale
     * </p>
     *
     * @param coll         collection de valeurs à tester
     * @return un objet de type {@link ListInfos} contenant les informations sur la collection de nombre,
     * notamment le boolean incremented = true si la liste contient tous les intermédiaires de N en N entre
     * la valeur minimale et la valeur maximale
     * 
     */
    public static ListInfos incremented(Collection<Integer> coll) {
        if(coll != null && coll.size() > 1) {
        	List<Integer> sortedList = coll.stream().sorted().distinct().collect(Collectors.toList());
        	int firstValue = sortedList.get(0);
        	int lastValue = sortedList.get(sortedList.size() - 1);
        	
        	if(coll.size() != sortedList.size()) {
        		return ListInfos.buildNotIncrementedListInfos(isOrdered(coll), firstValue, lastValue);
        	}
        	
        	int step = (lastValue - firstValue) / (sortedList.size() - 1);
        	        	
        	for(int i = 1; i < sortedList.size(); i++) {
        		if(sortedList.get(i - 1) + step != sortedList.get(i)) {
        			return ListInfos.buildNotIncrementedListInfos(isOrdered(coll), firstValue, lastValue);
        		}
        	}
        	return ListInfos.buildIncrementedListInfos(isOrdered(coll), firstValue, lastValue, step);
        }
        
        return null;
    }
    

    public static <T extends Comparable<? super T>> boolean isOrdered(Iterable<T> coll) {
    	  Iterator<T> i = coll.iterator();
    	  if (i.hasNext()) {
    	    T previous = i.next();
    	    while (i.hasNext()) {
    	      T current = i.next();
    	      if (previous.compareTo(current) > 0)
    	        return false;
    	      previous = current;
    	    }
    	  }
    	  return true;
    	}
    
    /**
     * filterListFromIndex </br>
     * <p>
     * Méthodes utilitaires permettant de filtrer une liste en ne retenant que les éléments correspondants aux index passés en second paramètre
     * </p>
     * 
     * @param <T>
     * @param listToFilter liste à filtrer
     * @param indexList liste des index des éléments à garder
     * @return la liste filtrée
     */
    public static <T> List<T> filterListFromIndex(List<T> listToFilter, List<Integer> indexList) {
    	return IntStream.range(0, listToFilter.size()).filter(indexList::contains).mapToObj(listToFilter::get).collect(Collectors.toList());
    }

}
