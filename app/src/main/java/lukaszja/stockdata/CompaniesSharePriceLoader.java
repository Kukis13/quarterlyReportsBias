package lukaszja.stockdata;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.mizosoft.methanol.CacheAwareResponse;

import lukaszja.stockdata.model.Company;
import lukaszja.stockdata.model.SharePrice;
import lukaszja.stockdata.utils.Utils;

public class CompaniesSharePriceLoader {

	public static Map<LocalDate, List<Double>> allPricesPerDay = new HashMap<>();
	public static Map<LocalDate, Double> averagePricesPerDay = new HashMap<>();

	String linkToFormat = "https://stooq.com/q/d/l/?s=%s&d1=20210101&d2=20231201&i=d";

	public void download(List<Company> allCompanies) {
		for (Company c : allCompanies) {
			downloadForCompany(c);
		}
		for (Entry<LocalDate, List<Double>> e : allPricesPerDay.entrySet()) {
			int size = e.getValue().size();
			double sum = e.getValue().stream().mapToDouble(Double::doubleValue).sum();
			double averagePriceChange = sum / size;
			averagePricesPerDay.put(e.getKey(), averagePriceChange);
		}

	}

	private void downloadForCompany(Company c) {
		CacheAwareResponse<String> httpGet = Utils.httpGet(linkToFormat.formatted(c.getSymbol()));

		String[] lines = httpGet.body().split("\\r?\\n");
		for (String line : lines) {
			try {
				if (line.contains("Date,Open,High,Low,Close,Volume") || line.trim().isBlank()) {
					continue;
				}
				SharePrice sharePrice = SharePrice.from(line);
				c.addSharePrice(sharePrice);
				allPricesPerDay.computeIfAbsent(sharePrice.date(), sp -> new ArrayList());
				allPricesPerDay.computeIfPresent(sharePrice.date(), (a, b) -> {
					b.add(sharePrice.change());
					return b;
				});
			} catch (Exception e) {
				Utils.printErr("Cant download data for " + c.getSymbol());
			}
		}
	}

}
