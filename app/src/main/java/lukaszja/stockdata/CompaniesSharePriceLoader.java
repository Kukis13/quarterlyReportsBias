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

	public static Map<LocalDate, SharePrice> averagePricesPerDay = new HashMap<>();

	String linkToFormat = "https://stooq.com/q/d/l/?s=%s&d1=20210101&d2=20231201&i=d";

	public void download(List<Company> allCompanies) {
		for (Company c : allCompanies) {
			downloadForCompany(c);
		}
		downloadForWig();

	}

	private void downloadForWig() {
		CacheAwareResponse<String> httpGet = Utils.httpGet(linkToFormat.formatted("wig"));
		String[] lines = httpGet.body().split("\\r?\\n");
		SharePrice previousSharePrice = null;
		for (String line : lines) {
			try {
				if (line.contains("Date,Open,High,Low,Close,Volume") || line.trim().isBlank()) {
					continue;
				}
				SharePrice sharePrice = SharePrice.from(line, previousSharePrice);
				previousSharePrice = sharePrice;
				averagePricesPerDay.put(sharePrice.date(), sharePrice);
			} catch (Exception e) {
				Utils.printErr("Cant download data for wig");
			}
		}
	}

	private void downloadForCompany(Company c) {
		CacheAwareResponse<String> httpGet = Utils.httpGet(linkToFormat.formatted(c.getSymbol()));

		String[] lines = httpGet.body().split("\\r?\\n");
		SharePrice previousSharePrice = null;
		for (String line : lines) {
			try {
				if (line.contains("Date,Open,High,Low,Close,Volume") || line.trim().isBlank()) {
					continue;
				}
				SharePrice sharePrice = SharePrice.from(line, previousSharePrice);
				previousSharePrice = sharePrice;
				c.addSharePrice(sharePrice);
			} catch (Exception e) {
				Utils.printErr("Cant download data for " + c.getSymbol());
			}
		}
	}

}
