package stringtools;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class IncrementTest {

	@Test
	public void incrementWithFirstValueStartSequence() {
		Increment increment = new Increment("A");
		
		assertThat(increment.increment(), is("A"));
		assertThat(increment.increment(), is("B"));
		assertThat(increment.increment(), is("C"));
		assertThat(increment.increment(), is("D"));
		assertThat(increment.increment(), is("E"));
		assertThat(increment.increment(), is("F"));
	}
	
	@Test
	public void incrementWithFirstValueMiddleOfSequence() {
		Increment increment = new Increment("c");
		
		assertThat(increment.increment(), is("c"));
		assertThat(increment.increment(), is("d"));
		assertThat(increment.increment(), is("e"));
		assertThat(increment.increment(), is("f"));
		assertThat(increment.increment(), is("g"));
		assertThat(increment.increment(), is("h"));
	}
	
	@Test
	public void incrementIncrementedOnFirstCall() {
		boolean addNewChar = false;
		boolean incrementOnFirstCall = true;
		Increment increment = new Increment("C", addNewChar, incrementOnFirstCall);
		
		assertThat(increment.increment(), is("D"));
		assertThat(increment.increment(), is("E"));
		assertThat(increment.increment(), is("F"));
		assertThat(increment.increment(), is("G"));
	}
	
	@Test
	public void incrementAndAddNewChar() {
		boolean addNewChar = true;
		boolean incrementOnFirstCall = false;
		Increment increment = new Increment("Y", addNewChar, incrementOnFirstCall);
		
		assertThat(increment.increment(), is("Y"));
		assertThat(increment.increment(), is("Z"));
		assertThat(increment.increment(), is("AA"));
		assertThat(increment.increment(), is("AB"));
		assertThat(increment.increment(), is("AC"));
	}
	
	@Test
	public void incrementAndAddNewCharWithNumber() {
		boolean addNewChar = true;
		boolean incrementOnFirstCall = false;
		Increment increment = new Increment("98", addNewChar, incrementOnFirstCall);
		
		assertThat(increment.increment(), is("98"));
		assertThat(increment.increment(), is("99"));
		assertThat(increment.increment(), is("100"));
		assertThat(increment.increment(), is("101"));
		assertThat(increment.increment(), is("102"));
	}
	
	@Test
	public void incrementWithConcatString() {
		Increment increment = new Increment("a");
		
		assertThat(increment.increment("", "", "-", "sufix", ""), is("a-sufix"));
		assertThat(increment.increment("Prefix", " ", "", "", ""), is("Prefix b"));
	}
	
	@Test
	public void incrementWithConcatStringList() {
		List<String> list = Arrays.asList("value 1", "value 2", "value 3");
		Increment increment = new Increment("AA");
		
		List<String> result = increment.addIncrementedSequenceToListItems(list, "-", Increment.BEFORE);
		assertThat(result.size(), is(3));
		assertThat(result, hasItems("AA-value 1", "AB-value 2", "AC-value 3"));
	}
	
	@Test
	public void incrementGenerateIncrementedList() {
		Increment increment = new Increment("01");
		
		List<String> result = increment.getIncrementedList(5);
		assertThat(result.size(), is(5));
		assertThat(result, hasItems("01", "02", "03", "04", "05"));
	}
	
}
