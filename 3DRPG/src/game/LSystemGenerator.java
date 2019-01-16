package game;

import java.util.ArrayList;

public class LSystemGenerator {
	
	private ArrayList<String> variables = new ArrayList<String>();
	private ArrayList<String> constants = new ArrayList<String>();
	private String[] rules;
	
	public LSystemGenerator(String variables, String constants, String rules){
		for(int i = 0; i < variables.length(); i++){
			this.variables.add(""+variables.charAt(i));
		}
		for(int i = 0; i < constants.length(); i++){
			this.constants.add(""+constants.charAt(i));
		}
		this.rules = rules.split("\\s*,\\s*");
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
		String finish = start;
		for(int i = 0; i < start.length(); i++){
			for(String rule : rules){
				String target = "" + rule.charAt(0);
				String replacement = rule.substring(rule.indexOf(">") + 1);
				if(("" + start.charAt(i)).equals(target)){
					finish = finish.substring(0, i) + replacement + finish.substring(i + 1);
				}
			}
		}
		return finish;
	}

}
