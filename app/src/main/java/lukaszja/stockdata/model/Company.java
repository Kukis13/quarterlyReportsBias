package lukaszja.stockdata.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Company {
	
	private List<LocalDate> reportDates = new ArrayList<LocalDate>();

	private String symbol;
	private String bankierName;
	private List<SharePrice> sharePrices = new ArrayList<SharePrice>(); 
	
	public Company(String bankierName, String symbol) {
		this.bankierName = bankierName;
		this.symbol = symbol;
	}

	public List<LocalDate> getReportDates() {
		return reportDates;
	}

	public void setReportDates(List<LocalDate> reportDates) {
		this.reportDates = reportDates;
	}
	
	public void addReportDate(LocalDate reportDate) {
		this.reportDates.add(reportDate);
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
	
	

}
