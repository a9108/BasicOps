package basic;

import java.util.LinkedList;

public class StringOps {
	private static boolean isEmojiCharacter(char codePoint) {
		return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
				|| (codePoint == 0xD)
				|| ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
				|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
	}

	private static final boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	public static final boolean hasChinese(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				return true;
			}
		}
		return false;
	}

	public static final String simplifyHTML(String s) {
		return s.replaceAll("<.*?>", "").replace("\n", "").replace("\r", "")
				.replace("&nbsp;", "").trim();
	}

	public static final LinkedList<String> splitForce(String s, String sep) {
		LinkedList<String> res = new LinkedList<String>();
		int la = 0;
		for (;;) {
			int nxt = s.indexOf(sep, la);
			if (nxt == -1)
				break;
			res.add(s.substring(la, nxt));
			la = nxt + sep.length();
		}
		res.add(s.substring(la));
		return res;
	}

	public static final String array2str(String[] data) {
		String t = "";
		for (int i = 0; i < data.length; i++) {
			String s = data[i];
			if (t.length() == 0)
				t += s;
			else
				t += "\t" + s;
		}
		return t;
	}

	public static String array2csv(String[] data) {
		String t = "";
		for (int i = 0; i < data.length; i++) {
			String s = "\"" + data[i].replace("\"", "\"\"") + "\"";
			if (t.length() == 0)
				t += s;
			else
				t += "," + s;
		}
		return t;
	}

	public static String array2csv_simple(String[] data) {
		String t = "";
		for (int i = 0; i < data.length; i++) {
			String s = data[i];
			if (t.length() == 0)
				t += s;
			else
				t += "," + s;
		}
		return t;	
	}

}
