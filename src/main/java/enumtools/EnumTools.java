package enumtools;

import java.util.Arrays;
import java.util.stream.Stream;

public class EnumTools {
		
	public static <E extends Enum<E>> Stream<E> enumStream(Class<E> enumClass) {
		return Arrays.asList(enumClass.getEnumConstants()).stream();
	}
}
