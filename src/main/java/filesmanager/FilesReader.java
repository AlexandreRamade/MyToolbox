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

    private static String CSV_SEPARATOR = ";";

    public static void setCsvSeparator(String csvSeparator) {
        CSV_SEPARATOR = csvSeparator;
    }

    /**
     * readAndInterpretLinesInFiles <br>
     * <p>
     * Charge le contenu d'un ou plusieurs fichier et recherche/extrait de son
     * contenu la valeur spécifiée
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

    /**
     * readCsvFiles <br>
     * <p>
     * Charge le contenu de plusieurs fichiers CSV et extrait les valeurs des
     * colonnes souhaitées pour les restituer sous forme de Map
     * </p>
     *
     * @param absolutePath adresse des fichiers
     * @param files        noms de fichiers
     * @param titles       titres des colonnes des données à extraire
     * @return la liste des données associées à chaque colonne sous forme de Map
     *         titre-valeur
     */
    public static List<Map<String, String>> readCsvFiles(String absolutePath, List<String> files, List<String> titles) {
        List<Map<String, String>> datas = new LinkedList<>();

        for (String fileName : files) {

            // map associant le titre de la colonne et sa position dans le fichier CSV
            Map<String, Integer> columnsPositions = new HashMap<>();

            // récupère la première ligne du CSV pour déterminer la position des colonnes à
            // extraire
            try (Stream<String> sourceLines = Files.lines(Paths.get(absolutePath, fileName))) {
                Optional<String> titleLine = sourceLines.findFirst();
                if (titleLine.isPresent()) {
                    String[] columns = titleLine.get().split(CSV_SEPARATOR);
                    for (int i = 0; i < columns.length; i++) {
                        if (titles.contains(columns[i])) {
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
        Map<String, String> datas = new HashMap<>();
        String[] rawDatas = line.split(CSV_SEPARATOR);
        columnsPositions.entrySet().stream().forEach(entry -> datas.put(entry.getKey(), rawDatas[entry.getValue()]));
        return datas;
    }

}

