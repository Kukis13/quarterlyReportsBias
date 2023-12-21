package lukaszja.stockdata;

import java.io.IOException;
import java.util.List;

import lukaszja.stockdata.model.Company;

public class StockDataApp {
	
	CompaniesLoader companiesLoader = new CompaniesLoader();
	CompanyReportDatesLoader companyReportDatesLoader = new CompanyReportDatesLoader();
	CompaniesSharePriceLoader sharePriceLoader = new CompaniesSharePriceLoader();
	InsiderTradingVerifier insiderTradingVerifier  = new InsiderTradingVerifier();

	public void start() throws IOException {
    	List<Company> allCompanies = companiesLoader.loadAll();
		companyReportDatesLoader.download(allCompanies);
		sharePriceLoader.download(allCompanies);
		insiderTradingVerifier.verify(allCompanies);
		
		
		HttpClientConfig.cache.close();
		System.out.println(HttpClientConfig.cache.stats());
	}

}
