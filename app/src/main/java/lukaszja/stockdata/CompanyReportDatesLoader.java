package lukaszja.stockdata;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.mizosoft.methanol.CacheAwareResponse;
import com.github.mizosoft.methanol.Methanol;
import com.github.mizosoft.methanol.MutableRequest;

import lukaszja.stockdata.model.Company;
import lukaszja.stockdata.utils.Utils;

public class CompanyReportDatesLoader {

	static private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	static Map<Integer, List<String>> reportIndicatorsInHtml = new HashMap<Integer, List<String>>();
	static {
		reportIndicatorsInHtml.put(1, List.of("SA-Q1", "SA - Q1", "SA - QSr1", "SA-QSr1"));
		reportIndicatorsInHtml.put(2, List.of("SA-P", "SA-PSr", "SA - P", "SA - PSr"));
		reportIndicatorsInHtml.put(3, List.of("SA-Q3", "SA - Q3", "SA-QSr3", "SA - QSr3"));
		reportIndicatorsInHtml.put(4, List.of("SA-R", "SA - RS", "SA - R", "SA-RS"));
	}

	public void download(List<Company> companies) {
		for (Company c : companies) {
			downloadForCompany(c);
		}
	}

	private void downloadForCompany(Company c) {
		String url = "https://www.stockwatch.pl/komunikaty-spolek/wszystkie.aspx?page=0&c=2&t=%s";
		String formattedUrl = url.formatted(c.getBankierName());
		CacheAwareResponse<String> response = Utils.httpGet(formattedUrl);
		findReportDate(response.body(), c);
	}

	private void findReportDate(String response, Company c) {
		int year = 2023;
		int quater = 0;
		while(year > 2020 && response.contains("<td class=\"c\">")) {
			int a = response.indexOf("<td class=\"c\">") + 14;
			response = response.substring(a);
			String eventDateTime = response.substring(0, 16);
			LocalDateTime dateTime = LocalDateTime.parse(eventDateTime, formatter);

			int b = response.indexOf("\">") + 2;
			response = response.substring(b);
			String reportString = response.substring(0, response.indexOf("<"));

			year = dateTime.getYear();

			for (Entry<Integer, List<String>> e : reportIndicatorsInHtml.entrySet()) {
				for (String s : e.getValue()) {
					if (!reportString.equals(s)) {
						continue;
					}
					quater = e.getKey();
					c.addReportDate(dateTime, year, quater);
				}
			}
		}


	}

}
