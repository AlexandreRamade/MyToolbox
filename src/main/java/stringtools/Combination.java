package stringtools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Combination extends IncrementAbstr {
	
	private int currentIndex = 0;
	
	List<String> permutations = new LinkedList<>();

	/* ***** ***** ***** ***** CONSTRUCTEUR ***** ***** ***** ***** */

	public Combination(String initialSequence) {
		initIncrement(initialSequence);
	}
	
	public Combination(String initialSequence, boolean incrementOnFirstCall) {
		this.initIncrement(initialSequence);
		this.activeIncrement = incrementOnFirstCall;
	}
	
	@Override
	public void initIncrement(String initialSequence) {
		permutations.clear();
		permute(toCharList(initialSequence), 0);
		currentIndex = permutations.indexOf(initialSequence);
	}
	

	/* ***** ***** ***** ***** METHODES ***** ***** ***** ***** */

	/* ***** ***** METHODES D'APPEL ***** ***** */
	@Override
	public String increment() {
		this.count++;
		if (this.activeIncrement) {
			currentIndex = (currentIndex + 1) % permutations.size();
		} else {
			this.activeIncrement = !this.activeIncrement;
		}
		return permutations.get(currentIndex);
	}

	/* ***** ***** METHODES UTILITAIRES ***** ***** */
	private static List<Character> toCharList(String sequence) {
		List<Character> chars = new ArrayList<>();
        for (char c : sequence.toCharArray()) {
            chars.add(c);
        }
        return chars;
	}


	/* ***** ***** METHODES DE TRAITEMENT ***** ***** */
	
	private void permute(List<Character> chars, int index) {
        if (index == chars.size() - 1) {
            StringBuilder sb = new StringBuilder();
            for (char c : chars) {
                sb.append(c);
            }
            permutations.add(sb.toString());
        } else {
            for (int i = index; i < chars.size(); i++) {
                Collections.swap(chars, i, index);
                permute(chars, index + 1);
                Collections.swap(chars, i, index);  // Revenir à l'état initial
            }
        }
        Collections.sort(permutations);
    }

	/* ***** ***** ***** ***** GETTEURS / SETTEURS ***** ***** ***** ***** */

	@Override
	public String getCurrentValue() {
		return permutations.get(currentIndex);
	}
	
	public List<String> getPermutations() {
		return this.permutations;
	}

}
