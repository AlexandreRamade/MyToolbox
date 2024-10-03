package listtools;

import java.util.Collection;
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
     * Méthode utilitaire permettant de déterminer si une liste de nombre est bien incrémentée de N
     * en N à partir de la valeur initiale passée en paramètre
     * </p>
     *
     * @param coll         collection de valeurs à tester
     * @param step         valeur du pas d'incrémentation
     * @param initialValue valeur initiale attendu
     * @return true si la valeur minimale correspond à celle passée en paramètres et que tous les
     * intermédiaires de N en N jusqu'à la valeur maximale sont présents dans la liste,
     * false sinon
     */
    public static boolean incremented(Collection<Integer> coll, Integer step, Integer initialValue) {
        Integer somme = coll.stream().reduce(0, (x, y) -> x + y);
        Integer sommeRef = (int) ((2 * initialValue + (coll.size() - 1) * step) * coll.size() * 0.5);
        return Integer.compare(somme, sommeRef) == 0;
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
