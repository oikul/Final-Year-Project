package generators;

import java.util.Random;

public class NameGenerator {
	
	private String[] nameParts = {
			"a", "", "ab", "ac", "ad", "ae", "af", "ag", "ah", "ai", "aj", "ak", "al", "am", "an", "ao", "ap", "aq", "ar", "as", "at", "au", "av", "aw", "ax", "ay", "az",
			"b", "ba", "", "", "", "be", "", "", "", "bi", "", "", "bl", "", "", "bo", "", "", "br", "bs", "", "bu", "", "", "", "by", "bz",
			"c", "ca", "", "", "", "ce", "", "", "ch", "ci", "", "ck", "cl", "", "", "co", "", "", "cr", "", "", "cu", "", "", "", "cy", "",
			"d", "da", "", "", "", "de", "", "", "", "di", "", "", "", "", "", "do", "", "", "dr", "", "", "du", "", "", "", "dy", "",
			"e", "ea", "eb", "ec", "ed", "ee", "ef", "eg", "eh", "ei", "ej", "ek", "el", "em", "en", "eo", "ep", "eq", "er", "es", "et", "eu", "ev", "ew", "ex", "ey", "ez",
			"f", "fa", "", "", "", "fe", "", "", "", "fi", "", "", "fl", "", "", "fo", "", "", "fr", "", "", "fu", "", "", "", "fy", "",
			"g", "ga", "", "", "", "ge", "", "", "", "gi", "", "", "gl", "", "", "go", "", "", "gr", "", "", "gu", "", "", "", "gy", "",
			"h", "ha", "", "", "", "he", "", "", "", "hi", "", "", "", "", "", "ho", "", "", "", "", "", "hu", "", "", "", "hy", "",
			"i", "ia", "ib", "ic", "id", "ie", "if", "ig", "ih", "", "ij", "ik", "il", "im", "in", "io", "ip", "iq", "ir", "is", "it", "iu", "iv", "iw", "ix", "iy", "iz",
			"j", "ja", "", "", "", "je", "", "", "", "ji", "", "", "", "", "", "jo", "", "", "", "", "", "ju", "", "", "", "jy", "",
			"k", "ka", "", "", "", "ke", "", "", "", "ki", "", "", "", "", "", "ko", "", "", "", "", "", "ku", "", "", "", "ky", "",
			"l", "la", "", "", "", "le", "", "", "", "li", "", "", "", "", "", "lo", "", "", "", "", "", "lu", "", "", "", "ly", "",
			"m", "ma", "", "", "", "me", "", "", "", "mi", "", "", "", "", "", "mo", "", "", "", "", "", "mu", "", "", "", "my", "",
			"n", "na", "", "", "", "ne", "", "", "", "ni", "", "", "", "", "", "no", "", "", "", "", "", "nu", "", "", "", "ny", "",
			"o", "oa", "ob", "oc", "od", "oe", "of", "og", "oh", "oi", "oj", "ok", "ol", "om", "on", "oo", "op", "oq", "or", "os", "ot", "ou", "ov", "ow", "ox", "oy", "oz",
			"p", "pa", "", "", "", "pe", "", "", "ph", "pi", "", "", "pl", "", "", "po", "", "", "pr", "", "", "pu", "", "", "", "py", "",
			"q", "qa", "", "", "", "qe", "", "", "", "qi", "", "", "", "", "", "qo", "", "", "", "", "", "qu", "", "", "", "qy", "",
			"r", "ra", "", "", "", "re", "", "", "", "ri", "", "", "", "", "", "ro", "", "", "", "", "", "ru", "", "", "", "ry", "",
			"s", "sa", "", "sc", "", "se", "", "", "sh", "si", "", "", "", "", "", "so", "sp", "", "", "ss", "st", "su", "", "", "", "sy", "",
			"t", "ta", "", "", "", "te", "", "", "th", "ti", "", "", "", "", "", "to", "", "", "", "", "", "tu", "", "", "", "ty", "",
			"u", "ua", "ub", "uc", "ud", "ue", "uf", "ug", "uh", "ui", "uj", "uk", "ul", "um", "un", "uo", "up", "uq", "ur", "us", "ut", "uu", "uv", "uw", "ux", "uy", "uz",
			"v", "va", "", "", "", "ve", "", "", "", "vi", "", "", "", "", "", "vo", "", "", "", "", "", "vu", "", "", "", "vy", "",
			"w", "wa", "", "", "", "we", "", "", "", "wi", "", "", "", "", "", "wo", "", "", "", "", "", "wu", "", "", "", "wy", "",
			"x", "xa", "", "", "", "xe", "", "", "", "xi", "", "", "", "", "", "xo", "", "", "", "", "", "xu", "", "", "", "xy", "",
			"y", "ya", "yb", "yc", "yd", "ye", "yf", "yg", "", "yi", "yj", "yk", "yl", "ym", "yn", "yo", "yp", "yq", "yr", "ys", "yt", "yu", "yv", "yw", "yx", "", "yz",
			"z", "za", "", "", "", "ze", "", "", "", "zi", "", "", "", "", "", "zo", "", "", "", "", "", "zu", "", "", "", "zy", "",
	};
	
	private Random random;
	
	public NameGenerator(long seed){
		random = new Random(seed);
	}
	
	public String getName(int partNumber){
		String name = "";
		String temp = nameParts[random.nextInt(nameParts.length)];
		while(partNumber > 0){
			if(temp.equals("")){
				partNumber++;
			}
			name += temp;
			temp = nameParts[random.nextInt(nameParts.length)];
			partNumber--;
		}
		return name;
	}

}
