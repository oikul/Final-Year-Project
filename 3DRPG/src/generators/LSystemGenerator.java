package generators;

import java.util.Random;

public class LSystemGenerator {
	
	private String[] rules;
	private Random random;
	
	public LSystemGenerator(String rules, long seed){
		this.rules = rules.split("\\s*,\\s*");
		random = new Random(seed);
	}
	
	public String repeat(int numOfTimes, String start){
		String finish = start;
		while(numOfTimes > 0){
			finish = doOnce(finish);
			numOfTimes--;
		}
		return finish;
	}
	
	public String doOnce(String start){
		for(int i = 0; i < start.length(); i++){
			for(String rule : rules){
				char target = rule.charAt(0);
				float chance = Float.valueOf(rule.substring(1, rule.indexOf(">")));
				String replacement = rule.substring(rule.indexOf(">") + 1);
				if(start.charAt(i) == target && chance >= random.nextFloat()){
					start = start.substring(0, i) + replacement + start.substring(i + 1);
					i += replacement.length() - 1;
				}
			}
		}
		return start;
	}

}
