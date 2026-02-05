package filesmanager;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * FilesWriter <br>
 * <p>
 * Créé ou complète les données dans un fichier
 * </p>
 *
 * @author a-ramade
 * @since 11/2022
 */
public class FilesWriter {

    /** ***** ***** OPTIONS D'ECRITURE ***** ***** */
    /**
     * Créé un nouveau fichier ou écrase le fichier s'il existe
     */
    public static OpenOption OVERWRITE_EXISTING_CONTENT = StandardOpenOption.TRUNCATE_EXISTING;
    /**
     * Créé le fichier ou complète le contenu du fichier s'il existe déjà
     */
    public static OpenOption ADD_AFTER_EXISTING_CONTENT = StandardOpenOption.APPEND;

    /**
     * **** ***** CONSTANTES ***** *****
     */
    private static Charset ENCODING = StandardCharsets.UTF_8;
    private static String CSV_SEPARATOR = ";";
    private static String LINE_BREAK = "\n";

    public static void setEncoding(Charset encoding) {
        ENCODING = encoding;
    }

    public static void setCsvSeparator(String csvSeparator) {
        CSV_SEPARATOR = csvSeparator;
    }

    public static void setLineBreak(String lineBreak) {
        LINE_BREAK = lineBreak;
    }

    /** ***** ***** METHODES DE TRAITEMENT ***** ***** */

    /**
     * writeFile <br>
     * <p>
     * Ecrit dans un fichier les données transmises sous la forme d'une liste de
     * lignes de texte
     * </p>
     *
     * @param absolutePath                          adresse absolue du fichier
     * @param fileName                              nom du fichier avec son extension
     * @param lines                                 contenu sous la forme d'une liste de lignes de texte
     * @param overwriteOrAddToExistingContentOption option d'écriture (écrasement ou ajout à la suite du contenu
     *                                              existant)
     * @see {@link Files#write(Path, Iterable, Charset, OpenOption...)}
     */
    public static void writeFile(String absolutePath, String fileName, Collection<String> lines,
                                 OpenOption overwriteOrAddToExistingContentOption) {
        Path path = Paths.get(absolutePath, fileName);

        try {
            Files.write(path, lines, ENCODING, StandardOpenOption.CREATE, StandardOpenOption.WRITE,
                    overwriteOrAddToExistingContentOption);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * writeFile <br>
     * <p>
     * Ecrit dans un fichier les données transmises sous la forme d'une chaine de
     * caractères
     * </p>
     *
     * @param absolutePath                          adresse absolue du fichier
     * @param fileName                              nom du fichier avec son extension
     * @param datas                                 contenu sous la forme d'une chaine de caractère
     * @param overwriteOrAddToExistingContentOption option d'écriture (écrasement ou ajout à la suite du contenu
     *                                              existant)
     * @see {@link FileChannel}
     * @see {@link FileChannel#write(ByteBuffer)}
     */
    public static void writeFile(String absolutePath, String fileName, String datas,
                                 OpenOption overwriteOrAddToExistingContentOption) {
        ByteBuffer datasBuffer = ByteBuffer.wrap(datas.getBytes(ENCODING));

        try (FileChannel writer = FileChannel.open(Paths.get(absolutePath, fileName), StandardOpenOption.CREATE,
                StandardOpenOption.WRITE, StandardOpenOption.SYNC, overwriteOrAddToExistingContentOption)) {
            // writer.position(writer.size());
            writer.write(datasBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * writeCsvFile <br>
     * <p>
     * Formate puis écrit dans un fichier des données au format CSV
     * </p>
     *
     * @param absolutePath                          adresse absolue du fichier
     * @param fileName                              nom du fichier avec son extension
     * @param datas                                 contenu à formatter
     * @param overwriteOrAddToExistingContentOption option d'écriture (écrasement ou ajout à la suite du contenu
     *                                              existant)
     * @see {@link this#generateCsvContent(Collection, List, boolean)}
     * @see {@link this#writeFile(String, String, String, OpenOption)}
     */
    public static void writeCsvFile(String absolutePath, String fileName, Collection<Map<String, String>> datas,
                                    OpenOption overwriteOrAddToExistingContentOption) {

        // affiche les titres sur la première ligne si option 'écraser fichier existant'
        // si option 'compléter le contenu à la suite', pas affichage des titres
        writeFile(absolutePath, fileName,
                generateCsvContent(datas, Collections.EMPTY_LIST,
                        OVERWRITE_EXISTING_CONTENT.equals(overwriteOrAddToExistingContentOption)),
                overwriteOrAddToExistingContentOption);
    }

    /**
     * writeCsvFile <br>
     * <p>
     * Formate puis écrit dans un fichier des données au format CSV
     * </p>
     *
     * @param absolutePath                          adresse absolue du fichier
     * @param fileName                              nom du fichier avec son extension
     * @param datas                                 contenu à formatter
     * @param columnTitles                          liste des titres des colonnes
     * @param overwriteOrAddToExistingContentOption option d'écriture (écrasement ou ajout à la suite du contenu
     *                                              existant)
     * @see {@link this#generateCsvContent(Collection, List, boolean)}
     * @see {@link this#writeFile(String, String, String, OpenOption)}
     */
    public static void writeCsvFile(String absolutePath, String fileName, Collection<Map<String, String>> datas,
                                    List<String> columnTitles, OpenOption overwriteOrAddToExistingContentOption) {

        // affiche les titres sur la première ligne si option 'écraser fichier existant'
        // si option 'compléter le contenu à la suite', pas affichage des titres
        writeFile(absolutePath, fileName,
                generateCsvContent(datas, columnTitles,
                        OVERWRITE_EXISTING_CONTENT.equals(overwriteOrAddToExistingContentOption)),
                overwriteOrAddToExistingContentOption);
    }

    /**
     * generateCsvContent <br>
     * <p>
     * Formate les données au format CSV restitué en une simple ligne de caractères
     * </p>
     * <div> Pour chaque ligne :
     * <ul>
     * <li>ajoute les noms des colones dans la première ligne si demandé en
     * paramètre</li>
     * <li>ajoute un saut de ligne</li>
     * <li>filtre les valeurs en fonction des titres des colonnes attendues</li>
     * <li>ordonne les valeurs selon la liste des titres des colonnes</li>
     * <li>joint les valeurs en intercalant le séparateur</li>
     * <li>joint les lignes en intercalant le saut de ligne</li>
     * </ul>
     * </div>
     *
     * @param datas                  contenu à formatter sous la forme d'une collection de Map
     * @param columnTitles           liste ordonnée des titres des colones
     * @param writeTitlesOnFirstLine option d'affichage du nom des colones sur la première ligne
     * @return les données filtrées et ordonnées au format CSV en une simple chaine
     * de caractères
     */
    public static String generateCsvContent(Collection<Map<String, String>> datas, List<String> columnTitles,
                                            boolean writeTitlesOnFirstLine) {

        final List<String> titles = (columnTitles != null && !columnTitles.isEmpty())
                ? columnTitles
                : datas.stream().findAny().get().keySet().stream().toList();

        StringBuilder csvContent = new StringBuilder();
        if (writeTitlesOnFirstLine) {
            csvContent.append(titles.stream().collect(Collectors.joining(CSV_SEPARATOR)));
        }
        csvContent.append(LINE_BREAK);

        //complète les données manquantes avec des chaines vides si nécéssaire
        // afin qu'il n'y ai pas de décalage dans les colones du fichier
        titles.stream().forEach(title -> datas.stream().forEach(mapData -> {
            if (!mapData.containsKey(title)) {
                mapData.put(title, "");
            }
        }));

        // pour chaque map de la liste :
        csvContent.append(datas.stream().map(map -> map.entrySet().stream()
                        // filtre les valeurs attendues dans la liste des titres des colonnes
                        .filter(entry -> titles.contains(entry.getKey())).sorted((entr1, entr2) -> {
                            // ordonne les valeurs selon la liste des titres des colonnes
                            return titles.indexOf(entr1.getKey()) > titles.indexOf(entr2.getKey()) ? 1 : -1;
                            // récupère les valeurs et les concatène en intercalant le séparateur
                        }).map(entry -> entry.getValue()).collect(Collectors.joining(CSV_SEPARATOR)))
                // puis concatène chaque ligne en intercalant un saut de ligne
                .collect(Collectors.joining(LINE_BREAK)));

        return csvContent.toString();
    }

    /**
     * replaceLinesInFile <br>
     * <p>
     *     Remplace les lignes correspondant avec le pattern par une nouvelle ligne
     * </p>
     * @param absolutePath adresse absolue du fichier
     * @param fileName nom du fichier
     * @param patternLineIdentifiant pattern permettant d'identifier les lignes à remplacer
     * @param newLineContent contenu de la nouvelle ligne
     * @return le nombre de lignes ayant été remplacées dans le fichier
     */
    public static int replaceLinesInFile(String absolutePath, String fileName, String patternLineIdentifiant, String newLineContent) {
        int linesReplaced = 0;
        try {
            Path filePath = Paths.get(absolutePath, fileName);
            if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
                throw new IOException("Le fichier n'existe pas ou n'est pas un fichier normal");
            }

            Pattern pattern = Pattern.compile(patternLineIdentifiant);

            List<String> lines = Files.readAllLines(filePath);
            for (int i = 0; i < lines.size(); i++) {
                if (pattern.matcher(lines.get(i)).matches()) {
                    lines.set(i, newLineContent);
                    linesReplaced++;
                }
            }
            if (linesReplaced > 0) {
                Files.write(filePath, lines, ENCODING);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return linesReplaced;
    }

}

