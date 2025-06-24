package stringtools;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class CombinationTest {

	@Test
	public void combinationWithFirstValueStartSequence() {
		Combination combination = new Combination("ABC");
		
		assertThat(combination.increment(), is("ABC"));
		assertThat(combination.increment(), is("ACB"));
		assertThat(combination.increment(), is("BAC"));
		assertThat(combination.increment(), is("BCA"));
		assertThat(combination.increment(), is("CAB"));
		assertThat(combination.increment(), is("CBA"));
		assertThat(combination.increment(), is("ABC"));
	}
	
	@Test
	public void combinationWithFirstValueMiddleOfSequence() {
		Combination combination = new Combination("BAC");
		
		assertThat(combination.increment(), is("BAC"));
		assertThat(combination.increment(), is("BCA"));
		assertThat(combination.increment(), is("CAB"));
		assertThat(combination.increment(), is("CBA"));
		assertThat(combination.increment(), is("ABC"));
	}
	
	@Test
	public void combinationIncrementedOnFirstCall() {
		boolean incrementOnFirstCall = true;
		Combination combination = new Combination("BAC", incrementOnFirstCall);
		
		assertThat(combination.increment(), is("BCA"));
		assertThat(combination.increment(), is("CAB"));
		assertThat(combination.increment(), is("CBA"));
		assertThat(combination.increment(), is("ABC"));
	}
	
	@Test
	public void combinationListContainsOnlyDistinctsSequences() {
		Combination combination = new Combination("ABCDE");
		
		List<String> combinationsResult = combination.getPermutations();
		List<String> combinationsFiltered = combination.getPermutations().stream().distinct().collect(Collectors.toList());
		assertThat(combinationsFiltered.size(), is(combinationsResult.size()));
	}
	
	@Test
	public void combinationListSizeDependsOnSequenceLength() {
		Combination combination = new Combination("AB");
		assertThat(combination.getPermutations().size(), is(2));
		
		combination.initIncrement("ABC");
		assertThat(combination.getPermutations().size(), is(6));
		
		combination.initIncrement("ABCD");
		assertThat(combination.getPermutations().size(), is(24));
		
		combination.initIncrement("ABCDE");
		assertThat(combination.getPermutations().size(), is(120));
		
		combination.initIncrement("ABCDEF");
		assertThat(combination.getPermutations().size(), is(720));
	}
	
}
