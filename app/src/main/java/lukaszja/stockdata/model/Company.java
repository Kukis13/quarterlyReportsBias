package lukaszja.stockdata.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Company {

	private List<CompanyReport> reportDates = new ArrayList<CompanyReport>();

	private String symbol;
	private String bankierName;
	private List<SharePrice> sharePrices = new ArrayList<SharePrice>();

	public Company(String bankierName, String symbol) {
		this.bankierName = bankierName;
		this.symbol = symbol;
	}

	public void addSharePrice(SharePrice sharePrice) {
		this.sharePrices.add(sharePrice);
	}

	public String getBankierName() {
		return this.bankierName;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public List<SharePrice> getSharePrices() {
		return sharePrices;
	}

	public void addReportDate(LocalDateTime dateTime, int year, int quater) {
		Optional<CompanyReport> any = this.getReportDates().stream().filter(f -> f.year() == year && f.quater() == quater).findAny();
		if (any.isPresent()) {
			return;
		}
		this.getReportDates().add(new CompanyReport(dateTime, year, quater));
	}

	public List<CompanyReport> getReportDates() {
		return reportDates;
	}

}
