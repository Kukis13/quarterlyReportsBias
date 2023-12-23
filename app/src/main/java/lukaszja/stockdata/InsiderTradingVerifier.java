package lukaszja.stockdata;

import static lukaszja.stockdata.CompaniesSharePriceLoader.averagePricesPerDay;

import java.time.LocalDate;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import lukaszja.stockdata.model.Company;
import lukaszja.stockdata.model.CompanyReport;
import lukaszja.stockdata.model.ReportTrading;
import lukaszja.stockdata.model.SharePrice;

public class InsiderTradingVerifier {

	private List<ReportTrading> reportTrading;

	public void verify(List<Company> companies) {
		for (Company c : companies) {
			verify(c);
		}

	}

	private void verify(Company c) {
		if (c.getSharePrices() == null || c.getSharePrices().isEmpty()) {
			return;
		}
		for (CompanyReport report : c.getReportDates()) {
			if (report.time().getYear() < 2021) {
				continue;
			}
			LocalDate reportDate = report.time().toLocalDate();
			LocalDate d0 = null;
			if (report.time().getHour() >= 17) {
				d0 = reportDate;
			} else if (report.time().getHour() < 9) {
				d0 = previousStockDate(reportDate, c);
			}
			if(d0 == null) {
				continue;
			}
			LocalDate d1 = null;
			if (report.time().getHour() >= 17) {
				d1 = nextStockDate(reportDate, c);
			} else if (report.time().getHour() < 9) {
				d1 = reportDate;
			}
			if(d1 == null) {
				continue;
			}
			
			final LocalDate d0copy = d0;
			final LocalDate d1copy = d1;
			Optional<SharePrice> d0Any = c.getSharePrices().stream().filter(sp -> sp.date().equals(d0copy)).findAny();
			Optional<SharePrice> d1Any = c.getSharePrices().stream().filter(sp -> sp.date().equals(d1copy)).findAny();
			if(d0Any.isEmpty() || d1Any.isEmpty()) {
				continue;
			}
			
			Optional<Entry<LocalDate,SharePrice>> wigChangeD0 = averagePricesPerDay.entrySet().stream()
					.filter(a -> a.getKey().equals(d0Any.get().date())).findAny();
			Optional<Entry<LocalDate,SharePrice>> wigChangeD1 = averagePricesPerDay.entrySet().stream()
					.filter(a -> a.getKey().equals(d1Any.get().date())).findAny();
			
			Double d0change = d0Any.get().change();
			Double d1change = d1Any.get().change();
			
			double increaseBeforeReport = (d0change - wigChangeD0.get().getValue().change()) * 100;
			double increaseAfterReport = (d1change - wigChangeD1.get().getValue().change()) * 100;
			
			System.out.println(c.getBankierName() + ":" + report.quater() + ":" + report.year() + ":" + increaseBeforeReport + ":" + increaseAfterReport);
		}
	}

	private LocalDate nextStockDate(LocalDate reportDate, Company c) {
		LocalDate nextDay = reportDate;
		int i = 5;
		while(i >= 0) {
			nextDay = nextDay.plusDays(1);
			final LocalDate dateToFind = nextDay;
			Optional<SharePrice> any = c.getSharePrices().stream().filter(s -> s.date().equals(dateToFind)).findAny();
			if(any.isPresent()) {
				return any.get().date();
			}
			i--;
		}
		return null;
	}

	private LocalDate previousStockDate(LocalDate reportDate, Company c) {
		LocalDate previousDay = reportDate;
		int i = 5;
		while(i >= 0) {
			previousDay = previousDay.minusDays(1);
			final LocalDate dateToFind = previousDay;
			Optional<SharePrice> any = c.getSharePrices().stream().filter(s -> s.date().equals(dateToFind)).findAny();
			if(any.isPresent()) {
				return any.get().date();
			}
			i--;
		}
		return null;
		
	}
}
