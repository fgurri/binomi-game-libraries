package binomi.game;

import java.util.ArrayList;
import java.util.Random;

public class DiceResult {
	private ArrayList<Integer> numbers = new ArrayList<Integer>();
	
	public static DiceResult generateResult(ArrayList<Dice> dices) {
		DiceResult res = new DiceResult();
		Random r = new Random();
		for (Dice dice:dices)
			res.addNumber(r.nextInt(dice.getSides() ) + 1);
		return res;
	}
	
	private void addNumber(int number) {
		this.numbers.add(number);
	}
	
	public int getNumericResult () {
		int res = 0;
		for (Integer i : numbers)
			res += i;
		return res;
	}
	
}
