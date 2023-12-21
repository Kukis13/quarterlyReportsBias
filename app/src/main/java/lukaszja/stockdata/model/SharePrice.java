package lukaszja.stockdata.model;

import java.time.LocalDate;

public record SharePrice(LocalDate date, Double open, Double high, Double low, Double close, Double volume) {
	public static SharePrice from(String s) {
		String[] parts = s.split(",");
		LocalDate date = LocalDate.parse(parts[0]);
		Double open = Double.parseDouble(parts[1]);
		Double high = Double.parseDouble(parts[2]);
		Double low = Double.parseDouble(parts[3]);
		Double close = Double.parseDouble(parts[4]);
		Double volume = Double.parseDouble(parts[5]);	

		return new SharePrice(date, open, high, low, close, volume);
	}
}