package lukaszja.stockdata;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import lukaszja.stockdata.model.Company;

public class InsiderTradingVerifier {

	public void verify(List<Company> companies) {
		//1. Filter out companies without full data for 2021-2023 (12 reports, and less than ~1% missing ticks.
		List<Company> companiesFullData = companies.stream().filter(c -> c.getReportDates().size() == 12).collect(Collectors.toList());
		companiesFullData = companies.stream().filter(c -> c.getSharePrices().size() > 720).toList();
		
		for(Company c  : companiesFullData) {
			verify(c);
		}
		
	}

	private void verify(Company c) {
		for(LocalDate reportDate : c.getReportDates()) {
			
		}
		
	}
}
