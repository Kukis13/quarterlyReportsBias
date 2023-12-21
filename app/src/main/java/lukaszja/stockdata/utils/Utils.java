package lukaszja.stockdata.utils;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

import com.github.mizosoft.methanol.CacheAwareResponse;
import com.github.mizosoft.methanol.MutableRequest;

import lukaszja.stockdata.HttpClientConfig;

public class Utils {

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	public static void printErr(String s) {
		System.out.println(ANSI_PURPLE + s + ANSI_RESET);
	}

	public static void print(String s) {
		System.out.println(s);
	}

	public static void printWarn(String string) {
		boolean enabled = false;
		if (enabled) {
			System.out.println(ANSI_YELLOW + string + ANSI_RESET);
			
		}

	}

	public static CacheAwareResponse<String> httpGet(String url) {
		try {
			HttpRequest request = MutableRequest.GET(url).cacheControl(HttpClientConfig.cacheControl);
			CacheAwareResponse<String> response = ((CacheAwareResponse<String>) HttpClientConfig.client.send(request, BodyHandlers.ofString()));

			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static String getNextWord(String s) {
		int maxCharacters = 100;
		int index = 0;
		StringBuilder stringBuilder = new StringBuilder();
		do {
			char c = s.charAt(index);
			if (Character.isWhitespace(c)) {
				break;
			}
			stringBuilder.append(s.charAt(index));
			index++;
		} while (index < maxCharacters);

		return stringBuilder.toString();
	}

}
