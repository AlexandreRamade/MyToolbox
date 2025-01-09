package listtools;

import java.util.Objects;

public class ListInfos {
	
	boolean incremented;
	boolean sorted;
	int firstValue;
	int lastValue;
	int step;
	
	
	
	private ListInfos(boolean incremented, boolean sorted, int firstValue, int lastValue, int step) {
		this.incremented = incremented;
		this.sorted = sorted;
		this.firstValue = firstValue;
		this.lastValue = lastValue;
		this.step = step;
	}
	
	public static ListInfos buildIncrementedListInfos(boolean sorted, int firstValue, int lastValue, int step) {
		return new ListInfos(true, sorted, firstValue, lastValue, step);
	}
	
	public static ListInfos buildNotIncrementedListInfos(boolean sorted, int firstValue, int lastValue) {
		return new ListInfos(false, sorted, firstValue, lastValue, 0);
	}
	
	
	
	@Override
	public int hashCode() {
		return Objects.hash(firstValue, incremented, lastValue, sorted, step);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ListInfos other = (ListInfos) obj;
		return firstValue == other.firstValue && incremented == other.incremented && lastValue == other.lastValue
				&& sorted == other.sorted && step == other.step;
	}

	public boolean isIncremented() {
		return incremented;
	}
	
	public int getFirstValue() {
		return firstValue;
	}
	
	public int getLastValue() {
		return lastValue;
	}
	
	public int getStep() {
		return step;
	}
	
	public boolean isSorted() {
		return sorted;
	}
	
	

}
