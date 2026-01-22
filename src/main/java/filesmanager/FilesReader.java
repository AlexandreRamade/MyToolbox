package filesmanager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * FilesReader <br>
 * <p>
 * Charge le contenu d'un ou plusieurs fichiers, extrait de chaque ligne un
 * motif spécifique avant d'instancier une liste d'objet à partir de ces données
 * </p>
 *
 * @author a-ramade
 * @since 09/2022
 */
public class FilesReader {

	private final static String ALL_STRING_REGEX = ".*";
	
    private static String CSV_SEPARATOR = ";";

    public static void setCsvSeparator(String csvSeparator) {
        CSV_SEPARATOR = csvSeparator;
    }
    
    /**
     * readAllLinesInFile <br>
     * <p>
     * Extrait le contenu d'un fichier
     * </p>
     * 
     * @param path adresse du fichier
     * @param file nom du fichier
     * @return liste des lignes du fichier
     */
    public static List<String> readAllLinesInFile(String path, String file) {
    	return readAndInterpretLinesInFiles(path, Arrays.asList(file), ALL_STRING_REGEX, Function.identity());
    }
    
    /**
     * readAllLinesInFiles <br>
     * <p>
     * Extrait le contenu d'un ou plusieurs fichier(s)
     * </p>
     * 
     * @param path adresse des fichiers
     * @param files noms des fichiers
     * @return liste des lignes des fichiers
     */
    public static List<String> readAllLinesInFiles(String path, List<String> files) {
    	return readAndInterpretLinesInFiles(path, files, ALL_STRING_REGEX, Function.identity());
    }

    /**
     * readAndInterpretLinesInFiles <br>
     * <p>
     * Charge le contenu d'un ou plusieurs fichier(s), extrait de son contenu la valeur correspondant au pattern
     * et la restitue sous forme d'objet selon l'interpréteur donné en paramétre
     * </p>
     *
     * @param <R>              type d'objet à instancier à partir des données des
     *                         fichiers
     * @param path             adresse des fichiers
     * @param files            noms des fichiers
     * @param patternToExtract motif à extraire dans chaque ligne
     * @param interpreter      fonction instanciant un objet à partir des données
     *                         issues de chaque ligne
     * @return une liste d'objet de type <R>
     *
     */
    public static <R> List<R> readAndInterpretLinesInFiles(String path, List<String> files, String patternToExtract,
                                                           Function<String, R> interpreter) {

        Pattern pattern = Pattern.compile(patternToExtract);

        List<R> list = new ArrayList<>();

        for (String fileName : files) {
            try (Stream<String> sourceLines = Files.lines(Paths.get(path, fileName))) {
                list.addAll(sourceLines.map(line -> extractFirstOccurence(pattern, line)).filter(Objects::nonNull)
                        .map(interpreter).filter(Objects::nonNull).collect(Collectors.toList()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * extractFirstOccurence <br>
     * <p>
     * Methode utilitaire permettant d'extraire d'une chaine de caractère le premier
     * élément correspondant au pattern
     * </p>
     *
     * @param pattern à extraire
     * @param source  chaine de caractère
     * @return valeur extraite correspondant au pattern si trouvée, null sinon
     */
    private static String extractFirstOccurence(Pattern pattern, String source) {
        Matcher matcher = pattern.matcher(source);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public static List<Map<String, String>> readCsvFile(String absolutePath, String fileName, List<String> columnsTitles) {
        return readCsvFiles(absolutePath, Collections.singletonList(fileName), columnsTitles);
    }

    public static List<Map<String, String>> readCsvFile(String absolutePath, String fileName) {
        return readCsvFiles(absolutePath, Collections.singletonList(fileName), Collections.EMPTY_LIST);
    }

    public static List<Map<String, String>> readCsvFiles(String absolutePath, List<String> files) {
        return readCsvFiles(absolutePath, files, Collections.EMPTY_LIST);
    }

    /**
     * readCsvFiles <br>
     * <p>
     * Charge le contenu de plusieurs fichiers CSV et extrait les valeurs des
     * colonnes souhaitées pour les restituer sous forme de Map
     * </p>
     *
     * @param absolutePath adresse des fichiers
     * @param files        noms de fichiers
     * @param columnTitles       titres des colonnes des données à extraire (extrait toutes les collones si nulle ou vide)
     * @return la liste des données associées à chaque colonne sous forme de Map
     *         titre-valeur
     */
    public static List<Map<String, String>> readCsvFiles(String absolutePath, List<String> files, List<String> columnTitles) {
        List<Map<String, String>> datas = new LinkedList<>();

        boolean readAllColumns = columnTitles == null || columnTitles.isEmpty();

        for (String fileName : files) {

            // map associant le titre de la colonne et sa position dans le fichier CSV
            Map<String, Integer> columnsPositions = new LinkedHashMap<>();

            // récupère la première ligne du CSV pour déterminer la position des colonnes à
            // extraire
            try (Stream<String> sourceLines = Files.lines(Paths.get(absolutePath, fileName))) {
                Optional<String> titleLine = sourceLines.findFirst();
                if (titleLine.isPresent()) {
                    String[] columns = titleLine.get().split(CSV_SEPARATOR);
                    for (int i = 0; i < columns.length; i++) {
                        if (readAllColumns || columnTitles.contains(columns[i])) {
                            columnsPositions.put(columns[i], i);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // récupère les données
            if (!columnsPositions.isEmpty()) {
                try (Stream<String> sourceLines = Files.lines(Paths.get(absolutePath, fileName))) {
                    datas.addAll(sourceLines.skip(1).map(line -> extractColumnsOfCsvLine(line, columnsPositions))
                            .collect(Collectors.toList()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return datas;
    }

    /**
     * extractColumnsOfCsvLine <br>
     * <p>
     * Découpe les données selon le séparateur et extrait uniquement les colones
     * souhaitées
     * </p>
     *
     * @param line             ligne de données au format csv
     * @param columnsPositions map associant le titre des colonnes et leur position
     * @return les données sous forme de map titre-donée
     */
    private static Map<String, String> extractColumnsOfCsvLine(String line, Map<String, Integer> columnsPositions) {
        Map<String, String> datas = new LinkedHashMap<>();
        String[] rawDatas = line.split(CSV_SEPARATOR);
        columnsPositions.entrySet().stream().filter(entry -> entry.getValue() < rawDatas.length).forEach(entry -> datas.put(entry.getKey(), rawDatas[entry.getValue()]));
        return datas;
    }

}

