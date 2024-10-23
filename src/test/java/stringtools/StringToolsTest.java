package stringtools;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class StringToolsTest {
	
	private static final String EXTRACT_ALL_NUMBERS_TEST_STRING = "1 2 (3), 10,22-5 et3003Hgl";
	private static final String REPLACE_NTH_OCCURRENCE_TEST_STRING = "1, 2, 3, 4, 5, 6,";

	@Test
	public void extractAllIntegerNumbers_multiplesNumbers() {
		assertThat(StringTools.extractAllIntegerNumbers(EXTRACT_ALL_NUMBERS_TEST_STRING), CoreMatchers.hasItems(1, 2, 3, 10, 22, 5, 3003));
	}
	
	
	@Test
	public void replaceNthOccurrence_replace1stOccurence() {
		assertThat(StringTools.replaceNthOccurrence(",", 1, "ere_virgule_remplacée", REPLACE_NTH_OCCURRENCE_TEST_STRING), is("1ere_virgule_remplacée 2, 3, 4, 5, 6,"));
	}
	
	@Test
	public void replaceNthOccurrence_replaceAnyOccurence() {
		assertThat(StringTools.replaceNthOccurrence(",", 2, "nde_virgule_remplacée", REPLACE_NTH_OCCURRENCE_TEST_STRING), is("1, 2nde_virgule_remplacée 3, 4, 5, 6,"));
		assertThat(StringTools.replaceNthOccurrence(",", 3, "e_virgule_remplacée", REPLACE_NTH_OCCURRENCE_TEST_STRING), is("1, 2, 3e_virgule_remplacée 4, 5, 6,"));
		assertThat(StringTools.replaceNthOccurrence(",", 4, "e_virgule_remplacée", REPLACE_NTH_OCCURRENCE_TEST_STRING), is("1, 2, 3, 4e_virgule_remplacée 5, 6,"));
	}
	
	@Test
	public void replaceNthOccurrence_replaceLastOccurence() {
		assertThat(StringTools.replaceNthOccurrence(",", 6, "e_virgule_remplacée", REPLACE_NTH_OCCURRENCE_TEST_STRING), is("1, 2, 3, 4, 5, 6e_virgule_remplacée"));
	}
	
	@Test
	public void replaceNthOccurrence_nthOccurrenceNotFind() {
		assertThat(StringTools.replaceNthOccurrence(",", 10, "e_virgule_remplacée", REPLACE_NTH_OCCURRENCE_TEST_STRING), is(REPLACE_NTH_OCCURRENCE_TEST_STRING));
	}
	
	@Test
	public void replaceNthOccurrence_noOccurrenceFindInSourceString() {
		assertThat(StringTools.replaceNthOccurrence("motif_inconnu", 2, "motif_remplacé", REPLACE_NTH_OCCURRENCE_TEST_STRING), is(REPLACE_NTH_OCCURRENCE_TEST_STRING));
	}
	
	@Test
	public void replaceLast_noFindInSourceString() {
		assertThat(StringTools.replaceLast("motif_inconnu", "motif_remplacé", REPLACE_NTH_OCCURRENCE_TEST_STRING), is(REPLACE_NTH_OCCURRENCE_TEST_STRING));
	}
	
	@Test
	public void replaceLast_replaceLastOccurence() {
		assertThat(StringTools.replaceLast("1", "last_motif_remplacé", REPLACE_NTH_OCCURRENCE_TEST_STRING), is("last_motif_remplacé, 2, 3, 4, 5, 6,"));
		assertThat(StringTools.replaceLast("4,", "last_motif_remplacé", REPLACE_NTH_OCCURRENCE_TEST_STRING), is("1, 2, 3, last_motif_remplacé 5, 6,"));
		assertThat(StringTools.replaceLast(",", " last_motif_remplacé", REPLACE_NTH_OCCURRENCE_TEST_STRING), is("1, 2, 3, 4, 5, 6 last_motif_remplacé"));
	}
}
