package lukaszja.stockdata;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDate;
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

	static Map<Integer, List<String>> reportIndicatorsInHtml = new HashMap<Integer, List<String>>();
	static {
		reportIndicatorsInHtml.put(1, List.of("raportu za I kwartał"));
		reportIndicatorsInHtml.put(2, List.of("raportu za I półrocze", "raportu za II kwartał"));
		reportIndicatorsInHtml.put(3, List.of("raportu za III kwartał"));
		reportIndicatorsInHtml.put(4, List.of("raportu za 20", "raportu za rok obrotowy"));
	}

	public void download(List<Company> companies) {
		for (Company c : companies) {
			downloadForCompany(c);
		}
	}

	private void downloadForCompany(Company c)  {
		String url = "https://www.bankier.pl/gielda/kalendarium/kalendarium_dane?calendar_date=%s&spolka=%s";
		for (int year = 2021; year <= 2023; year++) {
			String formattedUrl = url.formatted(year + "-01-01", c.getBankierName());
			CacheAwareResponse<String> response = Utils.httpGet(formattedUrl);

			for (Entry<Integer, List<String>> e : reportIndicatorsInHtml.entrySet()) {
				LocalDate reportDate = findReportDate(response.body(), e.getValue());
				if (reportDate == null) {
					Utils.printWarn("Cant find report date for Q" + e.getKey() + " " + c.getBankierName() + " for year " + year);
					continue;
				}
				c.addReportDate(reportDate);
			}
		}

	}

	private LocalDate findReportDate(String response, List<String> textsToSearch) {
		String textToSearch = null;
		for (String s : textsToSearch) {
			if (response.contains(s)) {
				textToSearch = s;
			}
		}
		if (textToSearch == null) {
			return null;
		}

		int firstBoundary = response.indexOf(textToSearch);
		int startOfDate = response.indexOf("datetime", firstBoundary) + "datetime=\"".length();
		String dateString = response.substring(startOfDate, startOfDate + 10);
		try {
			return LocalDate.parse(dateString);
		} catch (DateTimeParseException e) {
			return null;
		}
	}

}
