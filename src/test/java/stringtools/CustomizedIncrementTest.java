package stringtools;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class CustomizedIncrementTest {

	@Test
	public void incrementWithoutFirstValue() {
		String incrementValues[] = {"A", "B", "C", "D"};
		CustomizedIncrement increment = new CustomizedIncrement(incrementValues);
		
		assertThat(increment.increment(), is("A"));
		assertThat(increment.increment(), is("B"));
		assertThat(increment.increment(), is("C"));
		assertThat(increment.increment(), is("D"));
		assertThat(increment.increment(), is("A"));
		assertThat(increment.increment(), is("B"));
	}
	
	@Test
	public void incrementWithFirstValue() {
		String incrementValues[] = {"A", "B", "C", "D"};
		CustomizedIncrement increment = new CustomizedIncrement("C", incrementValues);
		
		assertThat(increment.increment(), is("C"));
		assertThat(increment.increment(), is("D"));
		assertThat(increment.increment(), is("A"));
		assertThat(increment.increment(), is("B"));
		assertThat(increment.increment(), is("C"));
		assertThat(increment.increment(), is("D"));
	}
	
	@Test
	public void incrementWithFirstValueAndIncrementedOnFirstCall() {
		String incrementValues[] = {"A", "B", "C", "D"};
		boolean addNewChar = false;
		boolean incrementOnFirstCall = true;
		CustomizedIncrement increment = new CustomizedIncrement("C", incrementValues, addNewChar, incrementOnFirstCall);
		
		assertThat(increment.increment(), is("D"));
		assertThat(increment.increment(), is("A"));
		assertThat(increment.increment(), is("B"));
		assertThat(increment.increment(), is("C"));
	}
	
	@Test
	public void incrementWithFirstValueAndAddNewChar() {
		String incrementValues[] = {"A", "B", "C"};
		boolean addNewChar = true;
		boolean incrementOnFirstCall = false;
		CustomizedIncrement increment = new CustomizedIncrement("C", incrementValues, addNewChar, incrementOnFirstCall);
		
		assertThat(increment.increment(), is("C"));
		assertThat(increment.increment(), is("AA"));
		assertThat(increment.increment(), is("AB"));
		assertThat(increment.increment(), is("AC"));
		assertThat(increment.increment(), is("BA"));
		assertThat(increment.increment(), is("BB"));
		assertThat(increment.increment(), is("BC"));
		assertThat(increment.increment(), is("CA"));
		assertThat(increment.increment(), is("CB"));
		assertThat(increment.increment(), is("CC"));
		assertThat(increment.increment(), is("AAA"));
		assertThat(increment.increment(), is("AAB"));
	}
	
	@Test
	public void incrementWithFirstValueWithMultipleCharacters() {
		String incrementValues[] = {"A", "B", "C", "D"};
		CustomizedIncrement increment = new CustomizedIncrement("AC", incrementValues);
		
		assertThat(increment.increment(), is("AC"));
		assertThat(increment.increment(), is("AD"));
		assertThat(increment.increment(), is("BA"));
		assertThat(increment.increment(), is("BB"));
		assertThat(increment.increment(), is("BC"));
		assertThat(increment.increment(), is("BD"));
	}
	
	@Test
	public void incrementWithValuesContainingExactly2Characters() {
		String incrementValues[] = {"A1", "A2", "A3", "B1", "B2", "B3"};
		CustomizedIncrement increment = new CustomizedIncrement("A3", incrementValues);
		
		assertThat(increment.increment(), is("A3"));
		assertThat(increment.increment(), is("B1"));
		assertThat(increment.increment(), is("B2"));
		assertThat(increment.increment(), is("B3"));
		assertThat(increment.increment(), is("A1"));
	}
	
	@Test
	public void incrementWithValuesContainingMultipleCharacters() {
		String incrementValues[] = {"i", "ii", "iii", "iv", "v", "vi"};
		CustomizedIncrement increment = new CustomizedIncrement(1, incrementValues);
		
		assertThat(increment.increment(), is("ii"));
		assertThat(increment.increment(), is("iii"));
		assertThat(increment.increment(), is("iv"));
		assertThat(increment.increment(), is("v"));
		assertThat(increment.increment(), is("vi"));
	}
	
	@Test
	public void incrementWithValuesContainingMultipleCharactersAndMultipleStartValues() {
		String incrementValues[] = {"i", "ii", "iii", "iv", "v", "vi"};
		int startValuesIndex[] = {1, 4, 4};
		CustomizedIncrement increment = new CustomizedIncrement(startValuesIndex, incrementValues, false, false);
		
		assertThat(increment.increment(), is("iivv"));
		assertThat(increment.increment(), is("iivvi"));
		assertThat(increment.increment(), is("iivii"));
		assertThat(increment.increment(), is("iiviii"));
	}
	
	@Test
	public void incrementWithConcatString() {
		String incrementValues[] = {"i", "ii", "iii", "iv", "v", "vi"};
		CustomizedIncrement increment = new CustomizedIncrement(incrementValues);
		
		assertThat(increment.increment("", "", "-", "sufix", ""), is("i-sufix"));
		assertThat(increment.increment("Prefix", " ", "", "", ""), is("Prefix ii"));
	}
	
	@Test
	public void incrementWithConcatStringList() {
		String incrementValues[] = {"i", "ii", "iii", "iv", "v", "vi"};
		List<String> list = Arrays.asList("value 1", "value 2", "value 3");
		CustomizedIncrement increment = new CustomizedIncrement(incrementValues);
		
		List<String> result = increment.addIncrementedSequenceToListItems(list, "-", CustomizedIncrement.BEFORE);
		assertThat(result.size(), is(3));
		assertThat(result, hasItems("i-value 1", "ii-value 2", "iii-value 3"));
	}
	
	@Test
	public void incrementGenerateIncrementedList() {
		String incrementValues[] = {"i", "ii", "iii", "iv", "v", "vi"};
		CustomizedIncrement increment = new CustomizedIncrement(1, incrementValues);
		
		List<String> result = increment.getIncrementedList(5);
		assertThat(result.size(), is(5));
		assertThat(result, hasItems("ii", "iii", "iv", "v", "vi"));
	}
	
}
