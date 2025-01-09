package listtools;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class ListToolsTest {
	
	@Test
	public void incremented_() {
		// liste nulle, vide ou avec un seul élément
		assertThat(ListTools.incremented(null), nullValue());
		assertThat(ListTools.incremented(Arrays.asList()), nullValue());
		assertThat(ListTools.incremented(Arrays.asList(10)), nullValue());
		
		// liste avec seulement 2 éléments
		assertThat(ListTools.incremented(Arrays.asList(10, 15)), is(ListInfos.buildIncrementedListInfos(true, 10, 15, 5)));
		
		// step = 1
		assertThat(ListTools.incremented(Arrays.asList(1, 2, 3, 4)), is(ListInfos.buildIncrementedListInfos(true, 1, 4, 1)));
		
		// step = 3
		assertThat(ListTools.incremented(Arrays.asList(2, 5, 8, 11, 14)), is(ListInfos.buildIncrementedListInfos(true, 2, 14, 3)));
		assertThat(ListTools.incremented(Arrays.asList(2, 4, 8, 11, 14)), is(ListInfos.buildNotIncrementedListInfos(true, 2, 14)));
		assertThat(ListTools.incremented(Arrays.asList(2, 5, 9, 11, 14)), is(ListInfos.buildNotIncrementedListInfos(true, 2, 14)));
		assertThat(ListTools.incremented(Arrays.asList(2, 5, 11, 14)), is(ListInfos.buildNotIncrementedListInfos(true, 2, 14)));
		// avec duplication
		assertThat(ListTools.incremented(Arrays.asList(2, 5, 11, 11, 14)), is(ListInfos.buildNotIncrementedListInfos(true, 2, 14)));
		
		// step = 2
		assertThat(ListTools.incremented(Arrays.asList(4, 6, 8, 10, 12, 14)), is(ListInfos.buildIncrementedListInfos(true, 4, 14, 2)));
		assertThat(ListTools.incremented(Arrays.asList(4, 6, 8, 9, 13, 14)), is(ListInfos.buildNotIncrementedListInfos(true, 4, 14)));
		// non ordonnée
		assertThat(ListTools.incremented(Arrays.asList(4, 8, 6, 10, 12, 14)), is(ListInfos.buildIncrementedListInfos(false, 4, 14, 2)));
	}
	
	@Test
	public void isOrdered_() {
		assertThat(ListTools.isOrdered(Arrays.asList(2, 4, 5, 7)), is(true));
		assertThat(ListTools.isOrdered(Arrays.asList(2, 5, 4, 7)), is(false));
		assertThat(ListTools.isOrdered(Arrays.asList(2, 4, 4, 7)), is(true));
	}

}
