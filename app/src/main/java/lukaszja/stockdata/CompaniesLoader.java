package lukaszja.stockdata;

import static lukaszja.stockdata.utils.Utils.print;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import lukaszja.stockdata.model.Company;
import lukaszja.stockdata.utils.Utils;

public class CompaniesLoader {

	public List<Company> loadAll() {
		List<Company> companies = new ArrayList<Company>();
		try {
			Path of = Path.of("listaspolek");
			System.out.println(of.toAbsolutePath());
			List<String> allLines = Files.readAllLines(of);
			for(String line : allLines) {
				String trimmed = line.trim();
				String companyName = Utils.getNextWord(trimmed);
				String trimmedNext = trimmed.substring(companyName.length() + 1).trim();
				String symbol = Utils.getNextWord(trimmedNext);
				Company company = new Company(companyName, symbol);
				companies.add(company);
			}
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		return companies;
	}

}
