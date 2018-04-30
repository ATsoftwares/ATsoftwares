package hotels;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import au.com.bytecode.opencsv.CSVReader;


public class Main3 {
	
public class DayDiff{
	public int DayDiff;
	List<hotelinvitation> inventations;
}

public class DayInWeek {
	public int dayInWeek;
	List<DayDiff> dayDiffList;
}

public class Month {
	public int month;
	List<DayInWeek> dayInWekList;
}

public class HotelName {
	public String hotelName;
	List<DayInWeek> monthList;
}

	static List<String> companiesNames = new ArrayList<>();
	static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	static DateFormat formatterSlash = new SimpleDateFormat("dd/MM/yyyy");
	static Calendar c = Calendar.getInstance();
	public static void main(String[] args) throws ParseException {
		companiesNames.add("Hilton");
		companiesNames.add("Howard Johnson");
		companiesNames.add("Hampton Inn");
		companiesNames.add("Sleep Inn");
		companiesNames.add("SpringHill");
		companiesNames.add("Sheraton");
		companiesNames.add("Super 8");
		companiesNames.add("Crown Plaza");
		companiesNames.add("Comfort Inn");
		companiesNames.add("Comfort Inn");
		companiesNames.add("Courtyard by Marriott");
		companiesNames.add("Fairfield Inn");
		companiesNames.add("Andaz");
		companiesNames.add("Days Inn");
		companiesNames.add("Wyndham Garden Long Island");
		companiesNames.add("La Quinta Inn");
		companiesNames.add("Extended Stay");
		companiesNames.add("Westin");

		BufferedReader br = null;
		CSVReader reader;
		String[] line;

		HashMap<String, HashMap<Integer, HashMap<Integer, HashMap<Long, List<hotelinvitation>>>>> dictionaryInvitations = new HashMap<String, HashMap<Integer, HashMap<Integer, HashMap<Long, List<hotelinvitation>>>>>();
		try {
			final File folder = new File("C:\\Users\\Ofra\\Desktop\\טל וגל\\hotels research\\original csv");
			for (final File fileEntry : folder.listFiles()) {
				System.out.println(fileEntry.getName());
				reader = new CSVReader(new FileReader(fileEntry.getAbsolutePath()));

				while ((line = reader.readNext()) != null) {

					// use comma as separator
					String[] invite = line;

					// Convert to hotel object
					hotelinvitation newInvite = parseToHotel(invite);

					// Adding the invitation

					if (newInvite.days == 5) {
						// Check if the hotel exist
						if (dictionaryInvitations.containsKey(newInvite.hotelName)) {

							// the hotel exist check if it is in the same month
							if (dictionaryInvitations.get(newInvite.hotelName).containsKey(newInvite.month)) {

								// the hotel exist, it is in the same month,
								// check if it is in the same day in week
								if (dictionaryInvitations.get(newInvite.hotelName).get(newInvite.month)
										.containsKey(newInvite.DayInWeek)) {

									// the hotel exist, it is in the same month,
									// it is in the same day in week, check if
									// it the same diff date
									if (dictionaryInvitations.get(newInvite.hotelName).get(newInvite.month)
											.get(newInvite.DayInWeek).containsKey(newInvite.daysDiff)) {

										// it is
										// Adding the hotel to the same group
										dictionaryInvitations.get(newInvite.hotelName).get(newInvite.month)
												.get(newInvite.DayInWeek).get(newInvite.daysDiff).add(newInvite);
									} else {
										addDayDiff(dictionaryInvitations, newInvite);
									}
								} else {
									addDayInWeek(dictionaryInvitations, newInvite);
								}
							} else {
								addMonth(dictionaryInvitations, newInvite);
							}
						} else {
							addHotel(dictionaryInvitations, newInvite);
						}
					}
				}
			}
			List<hotelinvitation> minimumIvitations = new ArrayList<>();
			HashMap<String, List<hotelinvitation>> minimumByHotels = new HashMap<String, List<hotelinvitation>>();
			HashMap<String, List<hotelinvitation>> minimumByCompany = new HashMap<String, List<hotelinvitation>>();
			for (HashMap<Integer, HashMap<Integer, HashMap<Long, List<hotelinvitation>>>> hotels : dictionaryInvitations
					.values()) {
				for (HashMap<Integer, HashMap<Long, List<hotelinvitation>>> months : hotels.values()) {
					for (HashMap<Long, List<hotelinvitation>> daysInWeek : months.values()) {
						double minimumPrice = 999999999;
						hotelinvitation minimumHotel = new hotelinvitation();
						for (List<hotelinvitation> daysdiff : daysInWeek.values()) {
							// Go over all invitations with the same checkin
							// date and with the same hotel name
							if (daysdiff.size() > 1) {
								String name = daysdiff.get(0).descriptionPartOne;
								int amount = 0;
								for (hotelinvitation invite : daysdiff) {
									if (name == invite.descriptionPartOne) {
										amount++;
									}
									if (invite.totalPriceForCombine != -1) {
										if (invite.totalPriceForCombine < minimumPrice) {
											minimumPrice = invite.totalPriceForCombine;
											minimumHotel = invite;
										}
									} else {
										if (invite.originalPrice < minimumPrice) {
											minimumPrice = invite.originalPrice;
											minimumHotel = invite;
										}
									}

								}
								if (amount != daysdiff.size()) {
									minimumIvitations.add(minimumHotel);
									if (minimumByCompany.containsKey(minimumHotel.companyName)) {
										minimumByCompany.get(minimumHotel.companyName).add(minimumHotel);
									} else {
										List<hotelinvitation> min = new ArrayList<>();
										min.add(minimumHotel);
										minimumByCompany.put(minimumHotel.companyName, min);
									}

									if (minimumByHotels.containsKey(minimumHotel.hotelName)) {
										minimumByHotels.get(minimumHotel.hotelName).add(minimumHotel);
									} else {
										List<hotelinvitation> min = new ArrayList<>();
										min.add(minimumHotel);
										minimumByHotels.put(minimumHotel.hotelName, min);
										// minimumByHotels.put
									}
								}

							}

						}
					}
				}
			}
			for (String hotelName : dictionaryInvitations.keySet()) {
				System.out.print('\\' + "\"" + hotelName + '\\' + "\",");
			}
			System.out.println("********************************************");
			for (String hotelName : companiesNames) {
				System.out.print("\\" + "\"" + hotelName + "\\" + "\",");
			}
			writeResults(minimumIvitations);
			writeResultsByHotels(minimumByHotels);
			// writeResultByCompany(minimumByCompany);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	static boolean tryParseInt(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	static boolean tryParseDouble(String value) {
		try {
			Double.parseDouble(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	static void addDayDiff(
			HashMap<String, HashMap<Integer, HashMap<Integer, HashMap<Long, List<hotelinvitation>>>>> dictionaryInvitations,
			hotelinvitation newInvite) {
		List<hotelinvitation> list = new ArrayList<hotelinvitation>();
		list.add(newInvite);
		dictionaryInvitations.get(newInvite.hotelName).get(newInvite.month).get(newInvite.DayInWeek)
				.put(newInvite.daysDiff, list);
	}

	static void addDayInWeek(
			HashMap<String, HashMap<Integer, HashMap<Integer, HashMap<Long, List<hotelinvitation>>>>> dictionaryInvitations,
			hotelinvitation newInvite) {
		HashMap<Long, List<hotelinvitation>> a = new HashMap<Long, List<hotelinvitation>>();
		dictionaryInvitations.get(newInvite.hotelName).get(newInvite.month).put(newInvite.DayInWeek, a);
		addDayDiff(dictionaryInvitations, newInvite);
	}

	static void addMonth(
			HashMap<String, HashMap<Integer, HashMap<Integer, HashMap<Long, List<hotelinvitation>>>>> dictionaryInvitations,
			hotelinvitation newInvite) {
		HashMap<Integer, HashMap<Long, List<hotelinvitation>>> a = new HashMap<Integer, HashMap<Long, List<hotelinvitation>>>();
		dictionaryInvitations.get(newInvite.hotelName).put(newInvite.month, a);
		addDayInWeek(dictionaryInvitations, newInvite);
	}

	static void addHotel(
			HashMap<String, HashMap<Integer, HashMap<Integer, HashMap<Long, List<hotelinvitation>>>>> dictionaryInvitations,
			hotelinvitation newInvite) {
		HashMap<Integer, HashMap<Integer, HashMap<Long, List<hotelinvitation>>>> a = new HashMap<Integer, HashMap<Integer, HashMap<Long, List<hotelinvitation>>>>();
		dictionaryInvitations.put(newInvite.hotelName, a);
		addMonth(dictionaryInvitations, newInvite);
	}

	static boolean writeResults(List<hotelinvitation> minimumIvitations) throws FileNotFoundException {
		HashMap<String, String> resultCombine = new HashMap<String, String>();
		resultCombine.put("[1]", "A");
		resultCombine.put("[1-2]", "B");
		resultCombine.put("[1-3]", "C");
		resultCombine.put("[1-4]", "D");
		resultCombine.put("[1-5]", "E");

		PrintWriter pw = new PrintWriter(new File("results.csv"));
		StringBuilder sb = new StringBuilder();
		sb.append("hotelName,");
		sb.append("month,");
		sb.append("DayInWeek,");
		sb.append("diffDays,");
		sb.append("price,");
		sb.append("Company,");
		sb.append("Description");

		sb.append('\n');
		for (hotelinvitation invite : minimumIvitations) {

			sb.append(invite.hotelName + ",");
			// sb.append(invite.providerId +",");
			// sb.append(invite.cheakIn.toString() + ",");
			// sb.append(invite.cheakOut.toString() + ",");
			sb.append(invite.month + ",");
			sb.append(invite.DayInWeek + ",");
			// sb.append(invite.runDate.toString() + ",");
			long daysBetween;
			if (invite.runDate != null && invite.cheakIn != null) {

				daysBetween = invite.cheakIn.getTime() - invite.runDate.getTime();
			} else {
				daysBetween = 0;
			}
			long diffDays = daysBetween / (24 * 60 * 60 * 1000);
			sb.append(diffDays + ",");
			// sb.append(TimeUnit.DAYS.convert(daysBetween,
			// TimeUnit.MILLISECONDS));
			// Interval interval = new Interval(invite.cheakIn, invite.runDate);

			double price = invite.totalPriceForCombine == -1 ? invite.originalPrice : invite.totalPriceForCombine;
			sb.append(price + ",");
			sb.append(invite.companyName + ",");
			sb.append(resultCombine.get(invite.descriptionPartOne));
			sb.append('\n');
		}

		pw.write(sb.toString());
		pw.close();
		System.out.println("done!");
		return false;
	}

	static boolean writeResultByCompany(HashMap<String, List<hotelinvitation>> minimumByHotels) {

		HashMap<String, String> resultCombine = new HashMap<String, String>();
		resultCombine.put("[1]", "A");
		resultCombine.put("[1-2]", "B");
		resultCombine.put("[1-3]", "C");
		resultCombine.put("[1-4]", "D");
		resultCombine.put("[1-5]", "E");

		for (List<hotelinvitation> currHotel : minimumByHotels.values()) {
			try {
				/*
				 * if (currHotel.get(0).hotelName.contains("/")){
				 * currHotel.get(0).hotelName.replaceAll("/", "-"); }
				 */
				PrintWriter pw = new PrintWriter(
						new File("C:\\Users\\Ofra\\Desktop\\טל וגל\\hotels research\\resultArffCompany\\"
								+ currHotel.get(0).companyName + ".arff"));
				StringBuilder sb = new StringBuilder();
				sb.append("@relation \"" + currHotel.get(0).hotelName + "\"");
				sb.append('\n');
				sb.append('\n');
				sb.append(
						"@attribute hotelName {\"Utica st apartment\",\"Quality Inn Convention Center\",\"Hilton Manhattan East\",\"Days Inn & Suites Ozone Park/JFK Airport\",\"Dazzler Brooklyn\",\"Courtyard by Marriott New York Manhattan/SoHo\",\"Hilton Garden Inn New York/Central Park South-Midtown West\",\"Holiday Inn Staten Island\",\"Excelsior Hotel\",\"Courtyard by Marriott New York City Manhattan Fifth Avenue\",\"Howard Johnson Jamaica NY Near AirTrain JFK\",\"Hotel Plaza Athenee\",\"Ramada Staten Island\",\"onefinestay - Downtown East apartments\",\"Dream Midtown\",\"Millenium Hilton\",\"Howard Johnson Hotel Newark Airport\",\"Safehouse Suites Manhattan\",\"The Towers of the Waldorf Astoria New York\",\"Estudio 129\",\"Howard Johnson Inn Queens\",\"Superior New York Apartments\",\"DoubleTree by Hilton Hotel Newark Airport\",\"Henry Norman Hotel\",\"Sheraton New York Times Square Hotel\",\"Country Inn & Suites By Carlson Newark Airport\",\"The Gregory\",\"Radisson Hotel JFK Airport\",\"Best Western Bayside Inn\",\"United Nations Apartment Hotel\",\"Lefferts Gardens Residence Bed and Breakfast\",\"Fifty NYC-an Affinia hotel\",\"Dream Downtown\",\"Hyatt Times Square New York\",\"W New York - Downtown\",\"Hilton Times Square\",\"Renaissance New York Times Square Hotel\",\"Best Western Plus Arena Hotel\",\"ONE UN New York\",\"Best Western Plus Seaport Inn Downtown\",\"The Iroquois New York\",\"SIXTY SoHo\",\"Holiday Inn Newark Airport\",\"Courtyard Newark Elizabeth\",\"Dumont NYC-an Affinia hotel\",\"Courtyard by Marriott New York Manhattan / Chelsea\",\"The Moderne\",\"Hilton Garden Inn New York/Manhattan-Chelsea\",\"Element New York Times Square West\",\"Sheraton Brooklyn New York Hotel\",\"Novotel New York - Times Square\",\"Hampton Inn Times Square North\",\"Flatiron Hotel\",\"Gramercy Park Hotel\",\"Carlton Hotel Autograph Collection\",\"Nesva Hotel\",\"Best Western Jamaica Inn\",\"Trump International Hotel & Tower New York\",\"Best Western Plus Newark Airport West\",\"Wyndham Garden Long Island City Manhattan View\",\"Hotel On Rivington\",\"One Bedroom Self-Catering Apartment - Little Italy\",\"The Bryant Park Hotel\",\"La Quinta Inn Queens (New York City)\",\"The Kitano New York\",\"Hotel Hugo\",\"The Algonquin Hotel Times Square Autograph Collection\",\"The Missing Lantern\",\"The Mansfield Hotel\",\"JFK Inn\",\"Gansevoort Park Avenue NYC\",\"Hilton Garden Inn New York/West 35th Street\",\"Hampton Inn Manhattan-Seaport-Financial District\",\"The One Boutique Hotel\",\"NYC Vacation Suites\",\"Hotel Q New York\",\"Extended Stay America New York City - LaGuardia Airport\",\"Westin New York at Times Square\",\"Comfort Inn Long Island City\",\"Hells Kitchen Apartment\",\"Omni Berkshire Place\",\"The Carlyle A Rosewood Hotel\",\"Days Hotel Broadway\",\"Holiday Inn Express - Madison Square Garden\",\"Hilton Garden Inn New York/Midtown Park Ave\",\"Night Hotel Times Square\",\"Four Points by Sheraton Manhattan - Chelsea\",\"Hilton Garden Inn New York-Times Square Central\",\"Homewood Suites by Hilton NY Midtown Manhattan/Times Square\",\"Knights Inn JFK Airport NYC\",\"Courtyard by Marriott JFK Airport\",\"New York Marriott Marquis\",\"Hilton Garden Inn New York/Manhattan-Midtown East\",\"Classy and Cozy\",\"The Strayhorn - Corporate Apartment\",\"JW Marriott Essex House New York\",\"Sheridan Hotel\",\"DoubleTree Suites by Hilton New York City - Times Square\",\"Vacayo NYC West Apartments\",\"The Roosevelt Hotel New York City\",\"The Solita Soho Hotel an Ascend Hotel Collection Member\",\"Hampton Inn Manhattan/United Nations\",\"Manhattan NYC-an Affinia hotel\",\"Ramada Plaza Newark Liberty International Airport\",\"Hotel Sofitel New York\",\"Linden Motor Inn\",\"Holiday Inn NYC - Lower East Side\",\"Hotel Belleclaire\",\"Radisson Martinique on Broadway\",\"Hotel Bliss\",\"Sleep Inn JFK Airport\",\"Holiday Inn Express New York City Fifth Avenue\",\"Luxury Suites Lenox Ave \",\"The Central Park North\",\"The James New York\",\"Hyatt Place Flushing/LaGuardia Airport\",\"Sleep Inn Prospect Park South\",\"Howard Johnson Inn Jamaica JFK Airport NYC\",\"Red Lion Inn and Suites Brooklyn\",\"Topping Apartment\",\"Cesar Suites at New York South\",\"Hampton Inn Manhattan/Times Square Central\",\"Renaissance Newark Airport Hotel\",\"Hampton Inn Manhattan Soho\",\"Hotel 48LEX New York\",\"TRYP by Wyndham New York Times Square\",\"Herrick Guest Suites 18th Street Apartment\",\"The Mark\",\"Quality Inn\",\"Ameritania at Times Square\",\"DoubleTree by Hilton Hotel New York - Times Square South\",\"Moblat 5\",\"Herrick Guest Suites Chelsea Apartment\",\"Hampton Inn Brooklyn Downtown NY\",\"Pointe Plaza Hotel\",\"Country Inn & Suites By Carlson New York City in Queens NY\",\"Park Central New York\",\"Holiday Inn Express New York - Manhattan West Side\",\"The Roger Smith Hotel\",\"Hotel Indigo BROOKLYN\",\"Club Quarters Hotel Wall Street\",\"Delz Bed & Breakfast\",\"SpringHill Suites by Marriott New York LaGuardia Airport\",\"La Quinta Inn and Suites JFK Airport\",\"Skyline Hotel\",\"The Alfie - Corporate Apartment\",\"Marrakech Hotel\",\"Hotel Le Bleu\",\"The Langston - Corporate Apartment\",\"Best Western Plus Hospitality House\",\"Carvi Hotel New York\",\"Hilton Garden Inn New York Long Island City\",\"Comfort Inn & Suites JFK Airport\",\"New York Marriott East Side\",\"The Paramount - A Times Square New York Hotel\",\"The Evelyn (Formerly The Gershwin Hotel)\",\"Crowne Plaza Times Square Manhattan\",\"Herrick Guest Suites Christopher Street Apartments\",\"Hotel Giraffe\",\"Gardens NYC-an Affinia hotel\",\"The Kimberly Hotel & Suites\",\"Comfort Inn Staten Island\",\"Best Western JFK Airport Hotel\",\"Midtown West Vacation Rentals\",\"Fairfield Inn by Marriott New York Manhattan/Times Square\",\"DoubleTree by Hilton Metropolitan - New York City\",\"Hotel Edison\",\"Howard Johnson Flushing\",\"Howard Johnson Long Island City\",\"Comfort Inn Times Square West\",\"NU Hotel Brooklyn\",\"The Muse Hotel a Kimpton Hotel\",\"The Library Hotel\",\"Newark Liberty International Airport Marriott\",\"Gatsby Hotel ג€“ an Ascend Hotel Collection Member\",\"Dylan Hotel\",\"Aloft Harlem\",\"Galaxy Motel\",\"Radio City Apartments\",\"the Quin\",\"Super 8 Jamaica North Conduit\",\"Days Inn JFK Airport\",\"Cosmopolitan Hotel - Tribeca\",\"Days Inn Brooklyn\",\"Lotte New York Palace\",\"Millennium Broadway Hotel - Times Square\",\"Empire Hotel\",\"Moblat Apartments 25-42\",\"Hotel Indigo NYC Chelsea\",\"Howard Johnson Bronx Near Stadium\",\"Best Western Premier Herald Square\",\"The Time Hotel\",\"The Belvedere Hotel\",\"DoubleTree by Hilton New York City - Chelsea\",\"Leon Hotel\",\"Wyndham Midtown 45 at New York City\",\"Comfort Inn Midtown West\",\"W New York - Union Square\",\"Holiday Inn Express New York City- Wall Street\",\"McCarren Hotel & Pool\",\"Courtyard by Marriott New York Manhattan/Times Square\",\"Hotel 41 at Times Square\",\"Flushing Hotel\",\"Gabbs Apartment\",\"Courtyard New York LaGuardia Airport\",\"Ramada Jamaica/Queens\",\"The Pina - Corporate Apartment\",\"The St. Regis New York\",\"Warwick New York Hotel\",\"Holiday Inn Express New York City Times Square\",\"Oasis Motel in Brooklyn\",\"Hampton Inn Manhattan/Times Square South\",\"Holiday Inn Express Staten Island West\",\"Bronx Guesthouse\",\"Holiday Inn New York City - Wall Street\",\"Refinery Hotel\",\"W New York Times Square\",\"Row NYC\",\"Best Western Queens Court Hotel\",\"Z NYC Hotel\",\"Mayor Hotel\",\"Ravel Hotel\",\"Fitzpatrick Manhattan Hotel\",\"Times Square Apartment\",\"Four Points by Sheraton Manhattan SoHo Village\",\"Crowne Plaza Newark Airport\",\"Langham Place New York Fifth Avenue\",\"Manhattan Broadway Budget Hotel\",\"Hampton Inn & Suites Staten Island\",\"The International Cozy Inn\",\"Renaissance New York Hotel 57\",\"Hotel Vetiver\",\"The Knickerbocker Hotel\",\"Avalon Hotel\",\"Residence Inn by Marriott New York Manhattan/Times Square\",\"West Side Apartment\",\"Comfort Inn Central Park West\",\"Hilton New York JFK Airport\",\"The Westin New York Grand Central\",\"Comfort Inn Brooklyn Cruise Terminal\",\"Sumner Hotel\",\"The Sohotel\",\"Park 79 Hotel\",\"Andaz 5th Avenue - a concept by Hyatt\",\"Fairfield Inn & Suites New York Manhattan/Downtown East\",\"Sleep Inn Brooklyn Downtown\",\"Hilton Garden Inn Times Square\",\"The Michelangelo Hotel\",\"Sanctuary Hotel New York\",\"Wyndham New Yorker\",\"Residence Inn by Marriott New York Manhattan/Midtown East\",\"Superior Times Square Apartments\",\"Hampton Inn Manhattan/Downtown-Financial District\",\"onefinestay - Brooklyn apartments\",\"Four Seasons Hotel New York\",\"Willamsburg Apartments\",\"Wingate by Wyndham Manhattan Midtown\",\"Holiday Inn Manhattan-Financial District\",\"Comfort Inn\",\"The Gracie Inn\",\"The Townhouse Inn of Chelsea\",\"SIXTY Lower East Side\",\"The High Line Hotel\",\"Opera House Hotel\",\"Howard Johnson Manhattan Soho\",\"LaGuardia Plaza Hotel\",\"Hotel Chandler\",\"Wyndham Garden - Manhattan Chelsea West\",\"Best Western Gregory Hotel\",\"Best Western Plus Brooklyn Bay Hotel\",\"The Marmara Manhattan\",\"InterContinental - New York Times Square\",\"Allies Inn Bed and Breakfast\",\"Best Western Bowery Hanbee Hotel\",\"Holiday Inn Manhattan 6th Ave - Chelsea\",\"Residence Inn New York The Bronx At Metro Center Atrium\",\"Fairfield Inn by Marriott JFK Airport\",\"Club Quarters opposite Rockefeller Center\",\"Hyatt Herald Square New York\",\"Club Quarters World Trade Center\",\"Fairfield Inn by Marriott New York LaGuardia Airport/Astoria\",\"Lexington Inn - Brooklyn NY\",\"The London NYC\",\"Hotel Five44\",\"Le Parker Meridien New York\",\"Club Quarters Grand Central\",\"The Roxy Hotel\",\"Comfort Inn Lower East Side\",\"SpringHill Suites by Marriott Newark Liberty International\",\"Days Inn Long Island City\",\"Comfort Inn Near Financial District\",\"The Benjamin\",\"Stay The Night B&B\",\"Sheraton Tribeca New York Hotel\",\"onefinestay - Downtown West apartments\",\"Upper Yorkville Apartments\",\"The Peninsula New York\",\"Grand Hyatt New York\",\"Herrick Guest Suites 18th St Oasis Apartment\",\"Days Inn Bronx Near Stadium\",\"Best Western Plus Prospect Park Hotel\",\"Club Quarters Midtown - Times Square\",\"Fairfield Inn by Marriott New York Manhattan/Fifth Avenue\",\"Ace Hotel New York\",\"Eurostars Wall Street\",\"Hampton Inn Madison Square Garden Area Hotel\",\"Residence Inn New York Manhattan/Central Park\",\"Jets Motor Inn\",\"Hilton Club New York\",\"70 Park Avenue Hotel a Kimpton Hotel\",\"Comfort Inn Chelsea\",\"Fairfield Inn & Suites New York Queens/Queensboro Bridge\",\"Courtyard by Marriott New York Manhattan/Herald Square\",\"Staybridge Suites Times Square\",\"Anchor Inn\",\"Wyndham Garden Hotel Newark Airport\",\"The Ritz-Carlton New York Battery Park\",\"Fairfield Inn & Suites by Marriott New York ManhattanChelsea\",\"Vacayo NYC East Apartments\",\"Sheraton LaGuardia East Hotel\",\"Travel Inn Hotel\",\"Hotel 31\",\"Comfort Inn JFK Airport\",\"Hyatt Place New York Midtown South\",\"Waldorf Astoria New York\",\"The Ritz-Carlton New York Central Park\",\"Condor Hotel\",\"Holiday Inn Express Kennedy Airport\",\"Marco LaGuardia Hotel by Lexington\",\"1 Hotel Central Park\",\"The Lucerne Hotel\",\"Ramada Long Island City\",\"Shelburne NYC-an Affinia hotel\",\"Chelsea Apartment\",\"Hotel Le Jolie\",\"Times Square Grande Duplex\",\"Red Roof Inn Queens\",\"The Lexington New York City Autograph Collection\",\"Comfort Inn Brooklyn City Center\",\"Hotel Wales\",\"Hilton New York Fashion District\",\"The Jewel facing Rockefeller Center\",\"New York Marriott Downtown\",\"Four Points by Sheraton Long Island City Queensboro Bridge\",\"Courtyard by Marriott Newark Liberty International Airport\",\"Home2 Suites by Hilton NY Long Island City/Manhattan View\",\"Gild Hall A Thompson Hotel\",\"Red Carpet Inn New York City\",\"East Village Suites\",\"Fairfield Inn New York Manhattan/Financial District\",\"Hotel Boutique at Grand Central\",\"Fairfield Inn New York Midtown Manhattan/Penn Station\",\"Hotel 17\",\"Holiday Inn Express Laguardia Airport\",\"Best Western Plaza Hotel\",\"The Paul an Ascend Hotel Collection Member\",\"Herald Square Hotel\",\"New World Hotel\",\"Hotel St. James\",\"Trump Soho New York\",\"YOTEL New York at Times Square\",\"Bentley Hotel\",\"CITY ROOMS+ NYC SoHo\",\"The Pearl New York\",\"Murray Hill Apartments\",\"Eventi Hotel a Kimpton Hotel\",\"A Hostel\",\"Rodeway Inn Bronx Zoo\",\"Extended Stay America Elizabeth - Newark Airport\",\"Fairfield Inn New York Long Island City/Manhattan View\",\"Holiday Inn Express Brooklyn\",\"Sleep Inn Brooklyn\",\"Hilton Garden Inn New York / Staten Island\",\"Riviera Hotel\",\"Sugar Hill Harlem Inn\",\"Mandarin Oriental New York\",\"World Center Hotel\",\"The Towers at Lotte New York Palace\",\"MySuites - Christopher Suites\",\"The Lowell\",\"Holiday Inn - Long Island City - Manhattan View\",\"Aloft New York Brooklyn Hotel\",\"Comfort Inn Sunset Park / Park Slope\",\"The Duchamp - Corporate Apartment\",\"Econo Lodge South Ozone Park\",\"Guest House Off Park\",\"Studio Self Catering Apt Lower East Side\",\"Distrikt Hotel New York City an Ascend Collection Hotel\",\"NYLO New York City\",\"Red Roof Inn Flushing New York - LaGuardia Airport\",\"Quality Inn Woodside\",\"Courtyard by Marriott New York Manhattan / Central Park\",\"City Club Hotel\",\"Hilton Newark Airport\",\"Hampton Inn Newark Airport\",\"The Box House Hotel\",\"Fairfield Inn & Suites by Marriott New York Brooklyn\",\"Comfort Inn Times Square South Area\",\"Paris Suites Hotel\",\"San Carlos Hotel\",\"Vacation Rental in New York\",\"Comfort Inn & Suites LaGuardia Airport\",\"Baccarat Hotel and Residences New York\",\"The Roger\",\"Hampton Inn JFK Airport\",\"Lexington Inn JFK Airport\",\"The Plaza Hotel\",\"DoubleTree by Hilton New York City - Financial District\",\"Hampton Inn New York Chelsea\",\"Windsor Hotel\",\"Residence Inn New York Manhattan/World Trade Center Area\",\"Loews Regency New York Hotel\",\"Hotel de Point\",\"Super 8 Brooklyn / Park Slope Hotel\",\"Hampton Inn New York - 35th Street - Empire State Building\",\"Moblat Apartments 30-68\",\"Fairfield Inn & Suites Newark Liberty International Airport\",\"Courtyard New York Manhattan/Times Square West\",\"Super 8 Bronx\",\"Colonial House Inn\",\"The Hotel 91\",\"Sofia Inn\",\"Holiday Inn New York JFK Airport Area\",\"Howard Johnson Express Inn Bronx\",\"Embassy Suites Newark Airport\",\"Hotel 32 32\",\"Holiday Inn New York City-Midtown-57th Street\",\"Residence Inn Newark Elizabeth/Liberty International Airport\",\"Smyth A Thompson Hotel\",\"Upper West Side Brownstone\",\"Hampton Inn New York - LaGuardia Airport\",\"Hotel Elysee\",\"The Sherry Netherland\",\"Adria Hotel And Conference Center\",\"WestHouse New York\",\"Hilton Garden Inn Tribeca\",\"La Quinta Inn & Suites Brooklyn Downtown\",\"Off Soho Suites Hotel\",\"New York Hilton Midtown\",\"TRYP By Wyndham Times Square South\",\"Park Hyatt New York\",\"Sheraton JFK Airport Hotel\",\"Manhattan West Hotel\",\"New York Marriott at the Brooklyn Bridge\",\"Four Points by Sheraton Midtown-Times Square\",\"Ramada Bronx\",\"HYH Hotel Flushing\",\"Courtyard by Marriott New York City Manhattan Midtown East\",\"Martha Washington\",\"Park Lane Hotel\",\"West 57th Street by Hilton Club\",\"Midtown West Apartment\",\"Hotel Mulberry\",\"The Paper Factory Hotel\",\"Royal Park Hotel and Hostel\",\"Riverside Tower Hotel\",\"Blue Moon Boutique Hotel\",\"The Milburn Hotel\",\"ink48 hotel a Kimpton Hotel\",\"New York LaGuardia Airport Marriott\",\"Conrad New York\",\"The Shoreham Hotel\",\"Andaz Wall Street - a concept by Hyatt\",\"Garden Inn & Suites\",\"Airport Hotel Inn & Suites - Newark Airport\",\"Super 8 Long Island City LGA Hotel\",\"Metro Studio Midtown West\",\"The Marcel at Gramercy\",\"Chambers Hotel\",\"Fairfield Inn by Marriott LaGuardia Airport/Flushing\",\"The Surrey\",\"Courtyard by Marriott New York Manhattan/Upper East Side\",\"Ramada Flushing Queens\",\"The Chatwal A Luxury Collection Hotel\",\"6 Columbus - a SIXTY Hotel\",\"Crowne Plaza JFK Airport New York City\",\"Hotel Pennsylvania\",\"Hilton Garden Inn Queens/JFK Airport\"}");
				sb.append('\n');
				sb.append("@attribute month numeric");
				sb.append('\n');
				sb.append("@attribute DayInWeek numeric");
				sb.append('\n');
				sb.append("@attribute diffDays numeric");
				sb.append('\n');
				sb.append("@attribute price numeric");
				sb.append('\n');

				sb.append(
						"@attribute Company {\"Hilton\",\"Howard Johnson\",\"Hampton Inn\",\"Sleep Inn\",\"SpringHill\",\"Sheraton\",\"Super 8\",\"Crown Plaza\",\"Comfort Inn\",\"Courtyard by Marriott\",\"Fairfield Inn\",\"Andaz\",\"Days Inn\",\"Wyndham Garden Long Island\",\"La Quinta Inn\",\"Extended Stay\",\"Westin\",\"Utica st apartment\",\"Quality Inn Convention Center\",\"Hilton Manhattan East\",\"Days Inn & Suites Ozone Park/JFK Airport\",\"Dazzler Brooklyn\",\"Courtyard by Marriott New York Manhattan/SoHo\",\"Hilton Garden Inn New York/Central Park South-Midtown West\",\"Holiday Inn Staten Island\",\"Excelsior Hotel\",\"Courtyard by Marriott New York City Manhattan Fifth Avenue\",\"Howard Johnson Jamaica NY Near AirTrain JFK\",\"Hotel Plaza Athenee\",\"Ramada Staten Island\",\"onefinestay - Downtown East apartments\",\"Dream Midtown\",\"Millenium Hilton\",\"Howard Johnson Hotel Newark Airport\",\"Safehouse Suites Manhattan\",\"The Towers of the Waldorf Astoria New York\",\"Estudio 129\",\"Howard Johnson Inn Queens\",\"Superior New York Apartments\",\"DoubleTree by Hilton Hotel Newark Airport\",\"Henry Norman Hotel\",\"Sheraton New York Times Square Hotel\",\"Country Inn & Suites By Carlson Newark Airport\",\"The Gregory\",\"Radisson Hotel JFK Airport\",\"Best Western Bayside Inn\",\"United Nations Apartment Hotel\",\"Lefferts Gardens Residence Bed and Breakfast\",\"Fifty NYC-an Affinia hotel\",\"Dream Downtown\",\"Hyatt Times Square New York\",\"W New York - Downtown\",\"Hilton Times Square\",\"Renaissance New York Times Square Hotel\",\"Best Western Plus Arena Hotel\",\"ONE UN New York\",\"Best Western Plus Seaport Inn Downtown\",\"The Iroquois New York\",\"SIXTY SoHo\",\"Holiday Inn Newark Airport\",\"Courtyard Newark Elizabeth\",\"Dumont NYC-an Affinia hotel\",\"Courtyard by Marriott New York Manhattan / Chelsea\",\"The Moderne\",\"Hilton Garden Inn New York/Manhattan-Chelsea\",\"Element New York Times Square West\",\"Sheraton Brooklyn New York Hotel\",\"Novotel New York - Times Square\",\"Hampton Inn Times Square North\",\"Flatiron Hotel\",\"Gramercy Park Hotel\",\"Carlton Hotel Autograph Collection\",\"Nesva Hotel\",\"Best Western Jamaica Inn\",\"Trump International Hotel & Tower New York\",\"Best Western Plus Newark Airport West\",\"Wyndham Garden Long Island City Manhattan View\",\"Hotel On Rivington\",\"One Bedroom Self-Catering Apartment - Little Italy\",\"The Bryant Park Hotel\",\"La Quinta Inn Queens (New York City)\",\"The Kitano New York\",\"Hotel Hugo\",\"The Algonquin Hotel Times Square Autograph Collection\",\"The Missing Lantern\",\"The Mansfield Hotel\",\"JFK Inn\",\"Gansevoort Park Avenue NYC\",\"Hilton Garden Inn New York/West 35th Street\",\"Hampton Inn Manhattan-Seaport-Financial District\",\"The One Boutique Hotel\",\"NYC Vacation Suites\",\"Hotel Q New York\",\"Extended Stay America New York City - LaGuardia Airport\",\"Westin New York at Times Square\",\"Comfort Inn Long Island City\",\"Hells Kitchen Apartment\",\"Omni Berkshire Place\",\"The Carlyle A Rosewood Hotel\",\"Days Hotel Broadway\",\"Holiday Inn Express - Madison Square Garden\",\"Hilton Garden Inn New York/Midtown Park Ave\",\"Night Hotel Times Square\",\"Four Points by Sheraton Manhattan - Chelsea\",\"Hilton Garden Inn New York-Times Square Central\",\"Homewood Suites by Hilton NY Midtown Manhattan/Times Square\",\"Knights Inn JFK Airport NYC\",\"Courtyard by Marriott JFK Airport\",\"New York Marriott Marquis\",\"Hilton Garden Inn New York/Manhattan-Midtown East\",\"Classy and Cozy\",\"The Strayhorn - Corporate Apartment\",\"JW Marriott Essex House New York\",\"Sheridan Hotel\",\"DoubleTree Suites by Hilton New York City - Times Square\",\"Vacayo NYC West Apartments\",\"The Roosevelt Hotel New York City\",\"The Solita Soho Hotel an Ascend Hotel Collection Member\",\"Hampton Inn Manhattan/United Nations\",\"Manhattan NYC-an Affinia hotel\",\"Ramada Plaza Newark Liberty International Airport\",\"Hotel Sofitel New York\",\"Linden Motor Inn\",\"Holiday Inn NYC - Lower East Side\",\"Hotel Belleclaire\",\"Radisson Martinique on Broadway\",\"Hotel Bliss\",\"Sleep Inn JFK Airport\",\"Holiday Inn Express New York City Fifth Avenue\",\"Luxury Suites Lenox Ave \",\"The Central Park North\",\"The James New York\",\"Hyatt Place Flushing/LaGuardia Airport\",\"Sleep Inn Prospect Park South\",\"Howard Johnson Inn Jamaica JFK Airport NYC\",\"Red Lion Inn and Suites Brooklyn\",\"Topping Apartment\",\"Cesar Suites at New York South\",\"Hampton Inn Manhattan/Times Square Central\",\"Renaissance Newark Airport Hotel\",\"Hampton Inn Manhattan Soho\",\"Hotel 48LEX New York\",\"TRYP by Wyndham New York Times Square\",\"Herrick Guest Suites 18th Street Apartment\",\"The Mark\",\"Quality Inn\",\"Ameritania at Times Square\",\"DoubleTree by Hilton Hotel New York - Times Square South\",\"Moblat 5\",\"Herrick Guest Suites Chelsea Apartment\",\"Hampton Inn Brooklyn Downtown NY\",\"Pointe Plaza Hotel\",\"Country Inn & Suites By Carlson New York City in Queens NY\",\"Park Central New York\",\"Holiday Inn Express New York - Manhattan West Side\",\"The Roger Smith Hotel\",\"Hotel Indigo BROOKLYN\",\"Club Quarters Hotel Wall Street\",\"Delz Bed & Breakfast\",\"SpringHill Suites by Marriott New York LaGuardia Airport\",\"La Quinta Inn and Suites JFK Airport\",\"Skyline Hotel\",\"The Alfie - Corporate Apartment\",\"Marrakech Hotel\",\"Hotel Le Bleu\",\"The Langston - Corporate Apartment\",\"Best Western Plus Hospitality House\",\"Carvi Hotel New York\",\"Hilton Garden Inn New York Long Island City\",\"Comfort Inn & Suites JFK Airport\",\"New York Marriott East Side\",\"The Paramount - A Times Square New York Hotel\",\"The Evelyn (Formerly The Gershwin Hotel)\",\"Crowne Plaza Times Square Manhattan\",\"Herrick Guest Suites Christopher Street Apartments\",\"Hotel Giraffe\",\"Gardens NYC-an Affinia hotel\",\"The Kimberly Hotel & Suites\",\"Comfort Inn Staten Island\",\"Best Western JFK Airport Hotel\",\"Midtown West Vacation Rentals\",\"Fairfield Inn by Marriott New York Manhattan/Times Square\",\"DoubleTree by Hilton Metropolitan - New York City\",\"Hotel Edison\",\"Howard Johnson Flushing\",\"Howard Johnson Long Island City\",\"Comfort Inn Times Square West\",\"NU Hotel Brooklyn\",\"The Muse Hotel a Kimpton Hotel\",\"The Library Hotel\",\"Newark Liberty International Airport Marriott\",\"Gatsby Hotel ג€“ an Ascend Hotel Collection Member\",\"Dylan Hotel\",\"Aloft Harlem\",\"Galaxy Motel\",\"Radio City Apartments\",\"the Quin\",\"Super 8 Jamaica North Conduit\",\"Days Inn JFK Airport\",\"Cosmopolitan Hotel - Tribeca\",\"Days Inn Brooklyn\",\"Lotte New York Palace\",\"Millennium Broadway Hotel - Times Square\",\"Empire Hotel\",\"Moblat Apartments 25-42\",\"Hotel Indigo NYC Chelsea\",\"Howard Johnson Bronx Near Stadium\",\"Best Western Premier Herald Square\",\"The Time Hotel\",\"The Belvedere Hotel\",\"DoubleTree by Hilton New York City - Chelsea\",\"Leon Hotel\",\"Wyndham Midtown 45 at New York City\",\"Comfort Inn Midtown West\",\"W New York - Union Square\",\"Holiday Inn Express New York City- Wall Street\",\"McCarren Hotel & Pool\",\"Courtyard by Marriott New York Manhattan/Times Square\",\"Hotel 41 at Times Square\",\"Flushing Hotel\",\"Gabbs Apartment\",\"Courtyard New York LaGuardia Airport\",\"Ramada Jamaica/Queens\",\"The Pina - Corporate Apartment\",\"The St. Regis New York\",\"Warwick New York Hotel\",\"Holiday Inn Express New York City Times Square\",\"Oasis Motel in Brooklyn\",\"Hampton Inn Manhattan/Times Square South\",\"Holiday Inn Express Staten Island West\",\"Bronx Guesthouse\",\"Holiday Inn New York City - Wall Street\",\"Refinery Hotel\",\"W New York Times Square\",\"Row NYC\",\"Best Western Queens Court Hotel\",\"Z NYC Hotel\",\"Mayor Hotel\",\"Ravel Hotel\",\"Fitzpatrick Manhattan Hotel\",\"Times Square Apartment\",\"Four Points by Sheraton Manhattan SoHo Village\",\"Crowne Plaza Newark Airport\",\"Langham Place New York Fifth Avenue\",\"Manhattan Broadway Budget Hotel\",\"Hampton Inn & Suites Staten Island\",\"The International Cozy Inn\",\"Renaissance New York Hotel 57\",\"Hotel Vetiver\",\"The Knickerbocker Hotel\",\"Avalon Hotel\",\"Residence Inn by Marriott New York Manhattan/Times Square\",\"West Side Apartment\",\"Comfort Inn Central Park West\",\"Hilton New York JFK Airport\",\"The Westin New York Grand Central\",\"Comfort Inn Brooklyn Cruise Terminal\",\"Sumner Hotel\",\"The Sohotel\",\"Park 79 Hotel\",\"Andaz 5th Avenue - a concept by Hyatt\",\"Fairfield Inn & Suites New York Manhattan/Downtown East\",\"Sleep Inn Brooklyn Downtown\",\"Hilton Garden Inn Times Square\",\"The Michelangelo Hotel\",\"Sanctuary Hotel New York\",\"Wyndham New Yorker\",\"Residence Inn by Marriott New York Manhattan/Midtown East\",\"Superior Times Square Apartments\",\"Hampton Inn Manhattan/Downtown-Financial District\",\"onefinestay - Brooklyn apartments\",\"Four Seasons Hotel New York\",\"Willamsburg Apartments\",\"Wingate by Wyndham Manhattan Midtown\",\"Holiday Inn Manhattan-Financial District\",\"The Gracie Inn\",\"The Townhouse Inn of Chelsea\",\"SIXTY Lower East Side\",\"The High Line Hotel\",\"Opera House Hotel\",\"Howard Johnson Manhattan Soho\",\"LaGuardia Plaza Hotel\",\"Hotel Chandler\",\"Wyndham Garden - Manhattan Chelsea West\",\"Best Western Gregory Hotel\",\"Best Western Plus Brooklyn Bay Hotel\",\"The Marmara Manhattan\",\"InterContinental - New York Times Square\",\"Allies Inn Bed and Breakfast\",\"Best Western Bowery Hanbee Hotel\",\"Holiday Inn Manhattan 6th Ave - Chelsea\",\"Residence Inn New York The Bronx At Metro Center Atrium\",\"Fairfield Inn by Marriott JFK Airport\",\"Club Quarters opposite Rockefeller Center\",\"Hyatt Herald Square New York\",\"Club Quarters World Trade Center\",\"Fairfield Inn by Marriott New York LaGuardia Airport/Astoria\",\"Lexington Inn - Brooklyn NY\",\"The London NYC\",\"Hotel Five44\",\"Le Parker Meridien New York\",\"Club Quarters Grand Central\",\"The Roxy Hotel\",\"Comfort Inn Lower East Side\",\"SpringHill Suites by Marriott Newark Liberty International\",\"Days Inn Long Island City\",\"Comfort Inn Near Financial District\",\"The Benjamin\",\"Stay The Night B&B\",\"Sheraton Tribeca New York Hotel\",\"onefinestay - Downtown West apartments\",\"Upper Yorkville Apartments\",\"The Peninsula New York\",\"Grand Hyatt New York\",\"Herrick Guest Suites 18th St Oasis Apartment\",\"Days Inn Bronx Near Stadium\",\"Best Western Plus Prospect Park Hotel\",\"Club Quarters Midtown - Times Square\",\"Fairfield Inn by Marriott New York Manhattan/Fifth Avenue\",\"Ace Hotel New York\",\"Eurostars Wall Street\",\"Hampton Inn Madison Square Garden Area Hotel\",\"Residence Inn New York Manhattan/Central Park\",\"Jets Motor Inn\",\"Hilton Club New York\",\"70 Park Avenue Hotel a Kimpton Hotel\",\"Comfort Inn Chelsea\",\"Fairfield Inn & Suites New York Queens/Queensboro Bridge\",\"Courtyard by Marriott New York Manhattan/Herald Square\",\"Staybridge Suites Times Square\",\"Anchor Inn\",\"Wyndham Garden Hotel Newark Airport\",\"The Ritz-Carlton New York Battery Park\",\"Fairfield Inn & Suites by Marriott New York ManhattanChelsea\",\"Vacayo NYC East Apartments\",\"Sheraton LaGuardia East Hotel\",\"Travel Inn Hotel\",\"Hotel 31\",\"Comfort Inn JFK Airport\",\"Hyatt Place New York Midtown South\",\"Waldorf Astoria New York\",\"The Ritz-Carlton New York Central Park\",\"Condor Hotel\",\"Holiday Inn Express Kennedy Airport\",\"Marco LaGuardia Hotel by Lexington\",\"1 Hotel Central Park\",\"The Lucerne Hotel\",\"Ramada Long Island City\",\"Shelburne NYC-an Affinia hotel\",\"Chelsea Apartment\",\"Hotel Le Jolie\",\"Times Square Grande Duplex\",\"Red Roof Inn Queens\",\"The Lexington New York City Autograph Collection\",\"Comfort Inn Brooklyn City Center\",\"Hotel Wales\",\"Hilton New York Fashion District\",\"The Jewel facing Rockefeller Center\",\"New York Marriott Downtown\",\"Four Points by Sheraton Long Island City Queensboro Bridge\",\"Courtyard by Marriott Newark Liberty International Airport\",\"Home2 Suites by Hilton NY Long Island City/Manhattan View\",\"Gild Hall A Thompson Hotel\",\"Red Carpet Inn New York City\",\"East Village Suites\",\"Fairfield Inn New York Manhattan/Financial District\",\"Hotel Boutique at Grand Central\",\"Fairfield Inn New York Midtown Manhattan/Penn Station\",\"Hotel 17\",\"Holiday Inn Express Laguardia Airport\",\"Best Western Plaza Hotel\",\"The Paul an Ascend Hotel Collection Member\",\"Herald Square Hotel\",\"New World Hotel\",\"Hotel St. James\",\"Trump Soho New York\",\"YOTEL New York at Times Square\",\"Bentley Hotel\",\"CITY ROOMS+ NYC SoHo\",\"The Pearl New York\",\"Murray Hill Apartments\",\"Eventi Hotel a Kimpton Hotel\",\"A Hostel\",\"Rodeway Inn Bronx Zoo\",\"Extended Stay America Elizabeth - Newark Airport\",\"Fairfield Inn New York Long Island City/Manhattan View\",\"Holiday Inn Express Brooklyn\",\"Sleep Inn Brooklyn\",\"Hilton Garden Inn New York / Staten Island\",\"Riviera Hotel\",\"Sugar Hill Harlem Inn\",\"Mandarin Oriental New York\",\"World Center Hotel\",\"The Towers at Lotte New York Palace\",\"MySuites - Christopher Suites\",\"The Lowell\",\"Holiday Inn - Long Island City - Manhattan View\",\"Aloft New York Brooklyn Hotel\",\"Comfort Inn Sunset Park / Park Slope\",\"The Duchamp - Corporate Apartment\",\"Econo Lodge South Ozone Park\",\"Guest House Off Park\",\"Studio Self Catering Apt Lower East Side\",\"Distrikt Hotel New York City an Ascend Collection Hotel\",\"NYLO New York City\",\"Red Roof Inn Flushing New York - LaGuardia Airport\",\"Quality Inn Woodside\",\"Courtyard by Marriott New York Manhattan / Central Park\",\"City Club Hotel\",\"Hilton Newark Airport\",\"Hampton Inn Newark Airport\",\"The Box House Hotel\",\"Fairfield Inn & Suites by Marriott New York Brooklyn\",\"Comfort Inn Times Square South Area\",\"Paris Suites Hotel\",\"San Carlos Hotel\",\"Vacation Rental in New York\",\"Comfort Inn & Suites LaGuardia Airport\",\"Baccarat Hotel and Residences New York\",\"The Roger\",\"Hampton Inn JFK Airport\",\"Lexington Inn JFK Airport\",\"The Plaza Hotel\",\"DoubleTree by Hilton New York City - Financial District\",\"Hampton Inn New York Chelsea\",\"Windsor Hotel\",\"Residence Inn New York Manhattan/World Trade Center Area\",\"Loews Regency New York Hotel\",\"Hotel de Point\",\"Super 8 Brooklyn / Park Slope Hotel\",\"Hampton Inn New York - 35th Street - Empire State Building\",\"Moblat Apartments 30-68\",\"Fairfield Inn & Suites Newark Liberty International Airport\",\"Courtyard New York Manhattan/Times Square West\",\"Super 8 Bronx\",\"Colonial House Inn\",\"The Hotel 91\",\"Sofia Inn\",\"Holiday Inn New York JFK Airport Area\",\"Howard Johnson Express Inn Bronx\",\"Embassy Suites Newark Airport\",\"Hotel 32 32\",\"Holiday Inn New York City-Midtown-57th Street\",\"Residence Inn Newark Elizabeth/Liberty International Airport\",\"Smyth A Thompson Hotel\",\"Upper West Side Brownstone\",\"Hampton Inn New York - LaGuardia Airport\",\"Hotel Elysee\",\"The Sherry Netherland\",\"Adria Hotel And Conference Center\",\"WestHouse New York\",\"Hilton Garden Inn Tribeca\",\"La Quinta Inn & Suites Brooklyn Downtown\",\"Off Soho Suites Hotel\",\"New York Hilton Midtown\",\"TRYP By Wyndham Times Square South\",\"Park Hyatt New York\",\"Sheraton JFK Airport Hotel\",\"Manhattan West Hotel\",\"New York Marriott at the Brooklyn Bridge\",\"Four Points by Sheraton Midtown-Times Square\",\"Ramada Bronx\",\"HYH Hotel Flushing\",\"Courtyard by Marriott New York City Manhattan Midtown East\",\"Martha Washington\",\"Park Lane Hotel\",\"West 57th Street by Hilton Club\",\"Midtown West Apartment\",\"Hotel Mulberry\",\"The Paper Factory Hotel\",\"Royal Park Hotel and Hostel\",\"Riverside Tower Hotel\",\"Blue Moon Boutique Hotel\",\"The Milburn Hotel\",\"ink48 hotel a Kimpton Hotel\",\"New York LaGuardia Airport Marriott\",\"Conrad New York\",\"The Shoreham Hotel\",\"Andaz Wall Street - a concept by Hyatt\",\"Garden Inn & Suites\",\"Airport Hotel Inn & Suites - Newark Airport\",\"Super 8 Long Island City LGA Hotel\",\"Metro Studio Midtown West\",\"The Marcel at Gramercy\",\"Chambers Hotel\",\"Fairfield Inn by Marriott LaGuardia Airport/Flushing\",\"The Surrey\",\"Courtyard by Marriott New York Manhattan/Upper East Side\",\"Ramada Flushing Queens\",\"The Chatwal A Luxury Collection Hotel\",\"6 Columbus - a SIXTY Hotel\",\"Crowne Plaza JFK Airport New York City\",\"Hotel Pennsylvania\",\"Hilton Garden Inn Queens/JFK Airport\"}");
				sb.append('\n');
				sb.append("@attribute Description {\"A\", \"B\", \"C\", \"D\", \"E\" }");
				sb.append('\n');

				sb.append('\n');
				sb.append("@data");
				sb.append('\n');
				for (hotelinvitation invite : currHotel) {
					sb.append('"' + invite.hotelName + "\",");
					sb.append(invite.month + ",");
					sb.append(invite.DayInWeek + ",");
					long daysBetween;
					if (invite.runDate != null && invite.cheakIn != null) {

						daysBetween = invite.cheakIn.getTime() - invite.runDate.getTime();
					} else {
						daysBetween = 0;
					}
					long diffDays = daysBetween / (24 * 60 * 60 * 1000);
					sb.append(diffDays + ",");
					double price = invite.totalPriceForCombine == -1 ? invite.originalPrice
							: invite.totalPriceForCombine;
					sb.append(price + ",");
					sb.append('"' + invite.companyName + "\",");
					sb.append('"' + resultCombine.get(invite.descriptionPartOne) + '"');
					sb.append('\n');

					System.out.println("doneH");
				}
				pw.write(sb.toString());
				System.out.println("done!!!!");
				pw.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;

	}

	static boolean writeResultsByHotels(HashMap<String, List<hotelinvitation>> minimumByHotels) {
		HashMap<String, String> resultCombine = new HashMap<String, String>();
		resultCombine.put("[1]", "A");
		resultCombine.put("[1-2]", "B");
		resultCombine.put("[1-3]", "C");
		resultCombine.put("[1-4]", "D");
		resultCombine.put("[1-5]", "E");

		for (List<hotelinvitation> currHotel : minimumByHotels.values()) {
			try {
				/*
				 * if (currHotel.get(0).hotelName.contains("/")){
				 * currHotel.get(0).hotelName.replaceAll("/", "-"); }
				 */
				PrintWriter pw = new PrintWriter(
						new File("C:\\Users\\Ofra\\Desktop\\טל וגל\\hotels research\\resultsArff\\"
								+ currHotel.get(0).hotelName + ".arff"));
				StringBuilder sb = new StringBuilder();
				// sb.append("@relation \"" + currHotel.get(0).hotelName +
				// "\"");
				// sb.append('\n');
				// sb.append('\n');
				// sb.append(
				// "@attribute hotelName {\"Utica st apartment\",\"Quality Inn
				// Convention Center\",\"Hilton Manhattan East\",\"Days Inn &
				// Suites Ozone Park/JFK Airport\",\"Dazzler
				// Brooklyn\",\"Courtyard by Marriott New York
				// Manhattan/SoHo\",\"Hilton Garden Inn New York/Central Park
				// South-Midtown West\",\"Holiday Inn Staten
				// Island\",\"Excelsior Hotel\",\"Courtyard by Marriott New York
				// City Manhattan Fifth Avenue\",\"Howard Johnson Jamaica NY
				// Near AirTrain JFK\",\"Hotel Plaza Athenee\",\"Ramada Staten
				// Island\",\"onefinestay - Downtown East apartments\",\"Dream
				// Midtown\",\"Millenium Hilton\",\"Howard Johnson Hotel Newark
				// Airport\",\"Safehouse Suites Manhattan\",\"The Towers of the
				// Waldorf Astoria New York\",\"Estudio 129\",\"Howard Johnson
				// Inn Queens\",\"Superior New York Apartments\",\"DoubleTree by
				// Hilton Hotel Newark Airport\",\"Henry Norman
				// Hotel\",\"Sheraton New York Times Square Hotel\",\"Country
				// Inn & Suites By Carlson Newark Airport\",\"The
				// Gregory\",\"Radisson Hotel JFK Airport\",\"Best Western
				// Bayside Inn\",\"United Nations Apartment Hotel\",\"Lefferts
				// Gardens Residence Bed and Breakfast\",\"Fifty NYC-an Affinia
				// hotel\",\"Dream Downtown\",\"Hyatt Times Square New
				// York\",\"W New York - Downtown\",\"Hilton Times
				// Square\",\"Renaissance New York Times Square Hotel\",\"Best
				// Western Plus Arena Hotel\",\"ONE UN New York\",\"Best Western
				// Plus Seaport Inn Downtown\",\"The Iroquois New York\",\"SIXTY
				// SoHo\",\"Holiday Inn Newark Airport\",\"Courtyard Newark
				// Elizabeth\",\"Dumont NYC-an Affinia hotel\",\"Courtyard by
				// Marriott New York Manhattan / Chelsea\",\"The
				// Moderne\",\"Hilton Garden Inn New
				// York/Manhattan-Chelsea\",\"Element New York Times Square
				// West\",\"Sheraton Brooklyn New York Hotel\",\"Novotel New
				// York - Times Square\",\"Hampton Inn Times Square
				// North\",\"Flatiron Hotel\",\"Gramercy Park Hotel\",\"Carlton
				// Hotel Autograph Collection\",\"Nesva Hotel\",\"Best Western
				// Jamaica Inn\",\"Trump International Hotel & Tower New
				// York\",\"Best Western Plus Newark Airport West\",\"Wyndham
				// Garden Long Island City Manhattan View\",\"Hotel On
				// Rivington\",\"One Bedroom Self-Catering Apartment - Little
				// Italy\",\"The Bryant Park Hotel\",\"La Quinta Inn Queens (New
				// York City)\",\"The Kitano New York\",\"Hotel Hugo\",\"The
				// Algonquin Hotel Times Square Autograph Collection\",\"The
				// Missing Lantern\",\"The Mansfield Hotel\",\"JFK
				// Inn\",\"Gansevoort Park Avenue NYC\",\"Hilton Garden Inn New
				// York/West 35th Street\",\"Hampton Inn
				// Manhattan-Seaport-Financial District\",\"The One Boutique
				// Hotel\",\"NYC Vacation Suites\",\"Hotel Q New
				// York\",\"Extended Stay America New York City - LaGuardia
				// Airport\",\"Westin New York at Times Square\",\"Comfort Inn
				// Long Island City\",\"Hells Kitchen Apartment\",\"Omni
				// Berkshire Place\",\"The Carlyle A Rosewood Hotel\",\"Days
				// Hotel Broadway\",\"Holiday Inn Express - Madison Square
				// Garden\",\"Hilton Garden Inn New York/Midtown Park
				// Ave\",\"Night Hotel Times Square\",\"Four Points by Sheraton
				// Manhattan - Chelsea\",\"Hilton Garden Inn New York-Times
				// Square Central\",\"Homewood Suites by Hilton NY Midtown
				// Manhattan/Times Square\",\"Knights Inn JFK Airport
				// NYC\",\"Courtyard by Marriott JFK Airport\",\"New York
				// Marriott Marquis\",\"Hilton Garden Inn New
				// York/Manhattan-Midtown East\",\"Classy and Cozy\",\"The
				// Strayhorn - Corporate Apartment\",\"JW Marriott Essex House
				// New York\",\"Sheridan Hotel\",\"DoubleTree Suites by Hilton
				// New York City - Times Square\",\"Vacayo NYC West
				// Apartments\",\"The Roosevelt Hotel New York City\",\"The
				// Solita Soho Hotel an Ascend Hotel Collection
				// Member\",\"Hampton Inn Manhattan/United Nations\",\"Manhattan
				// NYC-an Affinia hotel\",\"Ramada Plaza Newark Liberty
				// International Airport\",\"Hotel Sofitel New York\",\"Linden
				// Motor Inn\",\"Holiday Inn NYC - Lower East Side\",\"Hotel
				// Belleclaire\",\"Radisson Martinique on Broadway\",\"Hotel
				// Bliss\",\"Sleep Inn JFK Airport\",\"Holiday Inn Express New
				// York City Fifth Avenue\",\"Luxury Suites Lenox Ave \",\"The
				// Central Park North\",\"The James New York\",\"Hyatt Place
				// Flushing/LaGuardia Airport\",\"Sleep Inn Prospect Park
				// South\",\"Howard Johnson Inn Jamaica JFK Airport NYC\",\"Red
				// Lion Inn and Suites Brooklyn\",\"Topping Apartment\",\"Cesar
				// Suites at New York South\",\"Hampton Inn Manhattan/Times
				// Square Central\",\"Renaissance Newark Airport
				// Hotel\",\"Hampton Inn Manhattan Soho\",\"Hotel 48LEX New
				// York\",\"TRYP by Wyndham New York Times Square\",\"Herrick
				// Guest Suites 18th Street Apartment\",\"The Mark\",\"Quality
				// Inn\",\"Ameritania at Times Square\",\"DoubleTree by Hilton
				// Hotel New York - Times Square South\",\"Moblat 5\",\"Herrick
				// Guest Suites Chelsea Apartment\",\"Hampton Inn Brooklyn
				// Downtown NY\",\"Pointe Plaza Hotel\",\"Country Inn & Suites
				// By Carlson New York City in Queens NY\",\"Park Central New
				// York\",\"Holiday Inn Express New York - Manhattan West
				// Side\",\"The Roger Smith Hotel\",\"Hotel Indigo
				// BROOKLYN\",\"Club Quarters Hotel Wall Street\",\"Delz Bed &
				// Breakfast\",\"SpringHill Suites by Marriott New York
				// LaGuardia Airport\",\"La Quinta Inn and Suites JFK
				// Airport\",\"Skyline Hotel\",\"The Alfie - Corporate
				// Apartment\",\"Marrakech Hotel\",\"Hotel Le Bleu\",\"The
				// Langston - Corporate Apartment\",\"Best Western Plus
				// Hospitality House\",\"Carvi Hotel New York\",\"Hilton Garden
				// Inn New York Long Island City\",\"Comfort Inn & Suites JFK
				// Airport\",\"New York Marriott East Side\",\"The Paramount - A
				// Times Square New York Hotel\",\"The Evelyn (Formerly The
				// Gershwin Hotel)\",\"Crowne Plaza Times Square
				// Manhattan\",\"Herrick Guest Suites Christopher Street
				// Apartments\",\"Hotel Giraffe\",\"Gardens NYC-an Affinia
				// hotel\",\"The Kimberly Hotel & Suites\",\"Comfort Inn Staten
				// Island\",\"Best Western JFK Airport Hotel\",\"Midtown West
				// Vacation Rentals\",\"Fairfield Inn by Marriott New York
				// Manhattan/Times Square\",\"DoubleTree by Hilton Metropolitan
				// - New York City\",\"Hotel Edison\",\"Howard Johnson
				// Flushing\",\"Howard Johnson Long Island City\",\"Comfort Inn
				// Times Square West\",\"NU Hotel Brooklyn\",\"The Muse Hotel a
				// Kimpton Hotel\",\"The Library Hotel\",\"Newark Liberty
				// International Airport Marriott\",\"Gatsby Hotel ג€“ an Ascend
				// Hotel Collection Member\",\"Dylan Hotel\",\"Aloft
				// Harlem\",\"Galaxy Motel\",\"Radio City Apartments\",\"the
				// Quin\",\"Super 8 Jamaica North Conduit\",\"Days Inn JFK
				// Airport\",\"Cosmopolitan Hotel - Tribeca\",\"Days Inn
				// Brooklyn\",\"Lotte New York Palace\",\"Millennium Broadway
				// Hotel - Times Square\",\"Empire Hotel\",\"Moblat Apartments
				// 25-42\",\"Hotel Indigo NYC Chelsea\",\"Howard Johnson Bronx
				// Near Stadium\",\"Best Western Premier Herald Square\",\"The
				// Time Hotel\",\"The Belvedere Hotel\",\"DoubleTree by Hilton
				// New York City - Chelsea\",\"Leon Hotel\",\"Wyndham Midtown 45
				// at New York City\",\"Comfort Inn Midtown West\",\"W New York
				// - Union Square\",\"Holiday Inn Express New York City- Wall
				// Street\",\"McCarren Hotel & Pool\",\"Courtyard by Marriott
				// New York Manhattan/Times Square\",\"Hotel 41 at Times
				// Square\",\"Flushing Hotel\",\"Gabbs Apartment\",\"Courtyard
				// New York LaGuardia Airport\",\"Ramada Jamaica/Queens\",\"The
				// Pina - Corporate Apartment\",\"The St. Regis New
				// York\",\"Warwick New York Hotel\",\"Holiday Inn Express New
				// York City Times Square\",\"Oasis Motel in
				// Brooklyn\",\"Hampton Inn Manhattan/Times Square
				// South\",\"Holiday Inn Express Staten Island West\",\"Bronx
				// Guesthouse\",\"Holiday Inn New York City - Wall
				// Street\",\"Refinery Hotel\",\"W New York Times Square\",\"Row
				// NYC\",\"Best Western Queens Court Hotel\",\"Z NYC
				// Hotel\",\"Mayor Hotel\",\"Ravel Hotel\",\"Fitzpatrick
				// Manhattan Hotel\",\"Times Square Apartment\",\"Four Points by
				// Sheraton Manhattan SoHo Village\",\"Crowne Plaza Newark
				// Airport\",\"Langham Place New York Fifth Avenue\",\"Manhattan
				// Broadway Budget Hotel\",\"Hampton Inn & Suites Staten
				// Island\",\"The International Cozy Inn\",\"Renaissance New
				// York Hotel 57\",\"Hotel Vetiver\",\"The Knickerbocker
				// Hotel\",\"Avalon Hotel\",\"Residence Inn by Marriott New York
				// Manhattan/Times Square\",\"West Side Apartment\",\"Comfort
				// Inn Central Park West\",\"Hilton New York JFK Airport\",\"The
				// Westin New York Grand Central\",\"Comfort Inn Brooklyn Cruise
				// Terminal\",\"Sumner Hotel\",\"The Sohotel\",\"Park 79
				// Hotel\",\"Andaz 5th Avenue - a concept by Hyatt\",\"Fairfield
				// Inn & Suites New York Manhattan/Downtown East\",\"Sleep Inn
				// Brooklyn Downtown\",\"Hilton Garden Inn Times Square\",\"The
				// Michelangelo Hotel\",\"Sanctuary Hotel New York\",\"Wyndham
				// New Yorker\",\"Residence Inn by Marriott New York
				// Manhattan/Midtown East\",\"Superior Times Square
				// Apartments\",\"Hampton Inn Manhattan/Downtown-Financial
				// District\",\"onefinestay - Brooklyn apartments\",\"Four
				// Seasons Hotel New York\",\"Willamsburg Apartments\",\"Wingate
				// by Wyndham Manhattan Midtown\",\"Holiday Inn
				// Manhattan-Financial District\",\"Comfort Inn\",\"The Gracie
				// Inn\",\"The Townhouse Inn of Chelsea\",\"SIXTY Lower East
				// Side\",\"The High Line Hotel\",\"Opera House Hotel\",\"Howard
				// Johnson Manhattan Soho\",\"LaGuardia Plaza Hotel\",\"Hotel
				// Chandler\",\"Wyndham Garden - Manhattan Chelsea West\",\"Best
				// Western Gregory Hotel\",\"Best Western Plus Brooklyn Bay
				// Hotel\",\"The Marmara Manhattan\",\"InterContinental - New
				// York Times Square\",\"Allies Inn Bed and Breakfast\",\"Best
				// Western Bowery Hanbee Hotel\",\"Holiday Inn Manhattan 6th Ave
				// - Chelsea\",\"Residence Inn New York The Bronx At Metro
				// Center Atrium\",\"Fairfield Inn by Marriott JFK
				// Airport\",\"Club Quarters opposite Rockefeller
				// Center\",\"Hyatt Herald Square New York\",\"Club Quarters
				// World Trade Center\",\"Fairfield Inn by Marriott New York
				// LaGuardia Airport/Astoria\",\"Lexington Inn - Brooklyn
				// NY\",\"The London NYC\",\"Hotel Five44\",\"Le Parker Meridien
				// New York\",\"Club Quarters Grand Central\",\"The Roxy
				// Hotel\",\"Comfort Inn Lower East Side\",\"SpringHill Suites
				// by Marriott Newark Liberty International\",\"Days Inn Long
				// Island City\",\"Comfort Inn Near Financial District\",\"The
				// Benjamin\",\"Stay The Night B&B\",\"Sheraton Tribeca New York
				// Hotel\",\"onefinestay - Downtown West apartments\",\"Upper
				// Yorkville Apartments\",\"The Peninsula New York\",\"Grand
				// Hyatt New York\",\"Herrick Guest Suites 18th St Oasis
				// Apartment\",\"Days Inn Bronx Near Stadium\",\"Best Western
				// Plus Prospect Park Hotel\",\"Club Quarters Midtown - Times
				// Square\",\"Fairfield Inn by Marriott New York Manhattan/Fifth
				// Avenue\",\"Ace Hotel New York\",\"Eurostars Wall
				// Street\",\"Hampton Inn Madison Square Garden Area
				// Hotel\",\"Residence Inn New York Manhattan/Central
				// Park\",\"Jets Motor Inn\",\"Hilton Club New York\",\"70 Park
				// Avenue Hotel a Kimpton Hotel\",\"Comfort Inn
				// Chelsea\",\"Fairfield Inn & Suites New York Queens/Queensboro
				// Bridge\",\"Courtyard by Marriott New York Manhattan/Herald
				// Square\",\"Staybridge Suites Times Square\",\"Anchor
				// Inn\",\"Wyndham Garden Hotel Newark Airport\",\"The
				// Ritz-Carlton New York Battery Park\",\"Fairfield Inn & Suites
				// by Marriott New York ManhattanChelsea\",\"Vacayo NYC East
				// Apartments\",\"Sheraton LaGuardia East Hotel\",\"Travel Inn
				// Hotel\",\"Hotel 31\",\"Comfort Inn JFK Airport\",\"Hyatt
				// Place New York Midtown South\",\"Waldorf Astoria New
				// York\",\"The Ritz-Carlton New York Central Park\",\"Condor
				// Hotel\",\"Holiday Inn Express Kennedy Airport\",\"Marco
				// LaGuardia Hotel by Lexington\",\"1 Hotel Central Park\",\"The
				// Lucerne Hotel\",\"Ramada Long Island City\",\"Shelburne
				// NYC-an Affinia hotel\",\"Chelsea Apartment\",\"Hotel Le
				// Jolie\",\"Times Square Grande Duplex\",\"Red Roof Inn
				// Queens\",\"The Lexington New York City Autograph
				// Collection\",\"Comfort Inn Brooklyn City Center\",\"Hotel
				// Wales\",\"Hilton New York Fashion District\",\"The Jewel
				// facing Rockefeller Center\",\"New York Marriott
				// Downtown\",\"Four Points by Sheraton Long Island City
				// Queensboro Bridge\",\"Courtyard by Marriott Newark Liberty
				// International Airport\",\"Home2 Suites by Hilton NY Long
				// Island City/Manhattan View\",\"Gild Hall A Thompson
				// Hotel\",\"Red Carpet Inn New York City\",\"East Village
				// Suites\",\"Fairfield Inn New York Manhattan/Financial
				// District\",\"Hotel Boutique at Grand Central\",\"Fairfield
				// Inn New York Midtown Manhattan/Penn Station\",\"Hotel
				// 17\",\"Holiday Inn Express Laguardia Airport\",\"Best Western
				// Plaza Hotel\",\"The Paul an Ascend Hotel Collection
				// Member\",\"Herald Square Hotel\",\"New World Hotel\",\"Hotel
				// St. James\",\"Trump Soho New York\",\"YOTEL New York at Times
				// Square\",\"Bentley Hotel\",\"CITY ROOMS+ NYC SoHo\",\"The
				// Pearl New York\",\"Murray Hill Apartments\",\"Eventi Hotel a
				// Kimpton Hotel\",\"A Hostel\",\"Rodeway Inn Bronx
				// Zoo\",\"Extended Stay America Elizabeth - Newark
				// Airport\",\"Fairfield Inn New York Long Island City/Manhattan
				// View\",\"Holiday Inn Express Brooklyn\",\"Sleep Inn
				// Brooklyn\",\"Hilton Garden Inn New York / Staten
				// Island\",\"Riviera Hotel\",\"Sugar Hill Harlem
				// Inn\",\"Mandarin Oriental New York\",\"World Center
				// Hotel\",\"The Towers at Lotte New York Palace\",\"MySuites -
				// Christopher Suites\",\"The Lowell\",\"Holiday Inn - Long
				// Island City - Manhattan View\",\"Aloft New York Brooklyn
				// Hotel\",\"Comfort Inn Sunset Park / Park Slope\",\"The
				// Duchamp - Corporate Apartment\",\"Econo Lodge South Ozone
				// Park\",\"Guest House Off Park\",\"Studio Self Catering Apt
				// Lower East Side\",\"Distrikt Hotel New York City an Ascend
				// Collection Hotel\",\"NYLO New York City\",\"Red Roof Inn
				// Flushing New York - LaGuardia Airport\",\"Quality Inn
				// Woodside\",\"Courtyard by Marriott New York Manhattan /
				// Central Park\",\"City Club Hotel\",\"Hilton Newark
				// Airport\",\"Hampton Inn Newark Airport\",\"The Box House
				// Hotel\",\"Fairfield Inn & Suites by Marriott New York
				// Brooklyn\",\"Comfort Inn Times Square South Area\",\"Paris
				// Suites Hotel\",\"San Carlos Hotel\",\"Vacation Rental in New
				// York\",\"Comfort Inn & Suites LaGuardia Airport\",\"Baccarat
				// Hotel and Residences New York\",\"The Roger\",\"Hampton Inn
				// JFK Airport\",\"Lexington Inn JFK Airport\",\"The Plaza
				// Hotel\",\"DoubleTree by Hilton New York City - Financial
				// District\",\"Hampton Inn New York Chelsea\",\"Windsor
				// Hotel\",\"Residence Inn New York Manhattan/World Trade Center
				// Area\",\"Loews Regency New York Hotel\",\"Hotel de
				// Point\",\"Super 8 Brooklyn / Park Slope Hotel\",\"Hampton Inn
				// New York - 35th Street - Empire State Building\",\"Moblat
				// Apartments 30-68\",\"Fairfield Inn & Suites Newark Liberty
				// International Airport\",\"Courtyard New York Manhattan/Times
				// Square West\",\"Super 8 Bronx\",\"Colonial House Inn\",\"The
				// Hotel 91\",\"Sofia Inn\",\"Holiday Inn New York JFK Airport
				// Area\",\"Howard Johnson Express Inn Bronx\",\"Embassy Suites
				// Newark Airport\",\"Hotel 32 32\",\"Holiday Inn New York
				// City-Midtown-57th Street\",\"Residence Inn Newark
				// Elizabeth/Liberty International Airport\",\"Smyth A Thompson
				// Hotel\",\"Upper West Side Brownstone\",\"Hampton Inn New York
				// - LaGuardia Airport\",\"Hotel Elysee\",\"The Sherry
				// Netherland\",\"Adria Hotel And Conference
				// Center\",\"WestHouse New York\",\"Hilton Garden Inn
				// Tribeca\",\"La Quinta Inn & Suites Brooklyn Downtown\",\"Off
				// Soho Suites Hotel\",\"New York Hilton Midtown\",\"TRYP By
				// Wyndham Times Square South\",\"Park Hyatt New
				// York\",\"Sheraton JFK Airport Hotel\",\"Manhattan West
				// Hotel\",\"New York Marriott at the Brooklyn Bridge\",\"Four
				// Points by Sheraton Midtown-Times Square\",\"Ramada
				// Bronx\",\"HYH Hotel Flushing\",\"Courtyard by Marriott New
				// York City Manhattan Midtown East\",\"Martha
				// Washington\",\"Park Lane Hotel\",\"West 57th Street by Hilton
				// Club\",\"Midtown West Apartment\",\"Hotel Mulberry\",\"The
				// Paper Factory Hotel\",\"Royal Park Hotel and
				// Hostel\",\"Riverside Tower Hotel\",\"Blue Moon Boutique
				// Hotel\",\"The Milburn Hotel\",\"ink48 hotel a Kimpton
				// Hotel\",\"New York LaGuardia Airport Marriott\",\"Conrad New
				// York\",\"The Shoreham Hotel\",\"Andaz Wall Street - a concept
				// by Hyatt\",\"Garden Inn & Suites\",\"Airport Hotel Inn &
				// Suites - Newark Airport\",\"Super 8 Long Island City LGA
				// Hotel\",\"Metro Studio Midtown West\",\"The Marcel at
				// Gramercy\",\"Chambers Hotel\",\"Fairfield Inn by Marriott
				// LaGuardia Airport/Flushing\",\"The Surrey\",\"Courtyard by
				// Marriott New York Manhattan/Upper East Side\",\"Ramada
				// Flushing Queens\",\"The Chatwal A Luxury Collection
				// Hotel\",\"6 Columbus - a SIXTY Hotel\",\"Crowne Plaza JFK
				// Airport New York City\",\"Hotel Pennsylvania\",\"Hilton
				// Garden Inn Queens/JFK Airport\"}");
				// sb.append('\n');
				// sb.append("@attribute month numeric");
				// sb.append('\n');
				// sb.append("@attribute DayInWeek numeric");
				// sb.append('\n');
				// sb.append("@attribute diffDays numeric");
				// sb.append('\n');
				// sb.append("@attribute price numeric");
				// sb.append('\n');
				//
				// sb.append(
				// "@attribute Company {\"Hilton\",\"Howard Johnson\",\"Hampton
				// Inn\",\"Sleep Inn\",\"SpringHill\",\"Sheraton\",\"Super
				// 8\",\"Crown Plaza\",\"Comfort Inn\",\"Courtyard by
				// Marriott\",\"Fairfield Inn\",\"Andaz\",\"Days Inn\",\"Wyndham
				// Garden Long Island\",\"La Quinta Inn\",\"Extended
				// Stay\",\"Westin\",\"Utica st apartment\",\"Quality Inn
				// Convention Center\",\"Hilton Manhattan East\",\"Days Inn &
				// Suites Ozone Park/JFK Airport\",\"Dazzler
				// Brooklyn\",\"Courtyard by Marriott New York
				// Manhattan/SoHo\",\"Hilton Garden Inn New York/Central Park
				// South-Midtown West\",\"Holiday Inn Staten
				// Island\",\"Excelsior Hotel\",\"Courtyard by Marriott New York
				// City Manhattan Fifth Avenue\",\"Howard Johnson Jamaica NY
				// Near AirTrain JFK\",\"Hotel Plaza Athenee\",\"Ramada Staten
				// Island\",\"onefinestay - Downtown East apartments\",\"Dream
				// Midtown\",\"Millenium Hilton\",\"Howard Johnson Hotel Newark
				// Airport\",\"Safehouse Suites Manhattan\",\"The Towers of the
				// Waldorf Astoria New York\",\"Estudio 129\",\"Howard Johnson
				// Inn Queens\",\"Superior New York Apartments\",\"DoubleTree by
				// Hilton Hotel Newark Airport\",\"Henry Norman
				// Hotel\",\"Sheraton New York Times Square Hotel\",\"Country
				// Inn & Suites By Carlson Newark Airport\",\"The
				// Gregory\",\"Radisson Hotel JFK Airport\",\"Best Western
				// Bayside Inn\",\"United Nations Apartment Hotel\",\"Lefferts
				// Gardens Residence Bed and Breakfast\",\"Fifty NYC-an Affinia
				// hotel\",\"Dream Downtown\",\"Hyatt Times Square New
				// York\",\"W New York - Downtown\",\"Hilton Times
				// Square\",\"Renaissance New York Times Square Hotel\",\"Best
				// Western Plus Arena Hotel\",\"ONE UN New York\",\"Best Western
				// Plus Seaport Inn Downtown\",\"The Iroquois New York\",\"SIXTY
				// SoHo\",\"Holiday Inn Newark Airport\",\"Courtyard Newark
				// Elizabeth\",\"Dumont NYC-an Affinia hotel\",\"Courtyard by
				// Marriott New York Manhattan / Chelsea\",\"The
				// Moderne\",\"Hilton Garden Inn New
				// York/Manhattan-Chelsea\",\"Element New York Times Square
				// West\",\"Sheraton Brooklyn New York Hotel\",\"Novotel New
				// York - Times Square\",\"Hampton Inn Times Square
				// North\",\"Flatiron Hotel\",\"Gramercy Park Hotel\",\"Carlton
				// Hotel Autograph Collection\",\"Nesva Hotel\",\"Best Western
				// Jamaica Inn\",\"Trump International Hotel & Tower New
				// York\",\"Best Western Plus Newark Airport West\",\"Wyndham
				// Garden Long Island City Manhattan View\",\"Hotel On
				// Rivington\",\"One Bedroom Self-Catering Apartment - Little
				// Italy\",\"The Bryant Park Hotel\",\"La Quinta Inn Queens (New
				// York City)\",\"The Kitano New York\",\"Hotel Hugo\",\"The
				// Algonquin Hotel Times Square Autograph Collection\",\"The
				// Missing Lantern\",\"The Mansfield Hotel\",\"JFK
				// Inn\",\"Gansevoort Park Avenue NYC\",\"Hilton Garden Inn New
				// York/West 35th Street\",\"Hampton Inn
				// Manhattan-Seaport-Financial District\",\"The One Boutique
				// Hotel\",\"NYC Vacation Suites\",\"Hotel Q New
				// York\",\"Extended Stay America New York City - LaGuardia
				// Airport\",\"Westin New York at Times Square\",\"Comfort Inn
				// Long Island City\",\"Hells Kitchen Apartment\",\"Omni
				// Berkshire Place\",\"The Carlyle A Rosewood Hotel\",\"Days
				// Hotel Broadway\",\"Holiday Inn Express - Madison Square
				// Garden\",\"Hilton Garden Inn New York/Midtown Park
				// Ave\",\"Night Hotel Times Square\",\"Four Points by Sheraton
				// Manhattan - Chelsea\",\"Hilton Garden Inn New York-Times
				// Square Central\",\"Homewood Suites by Hilton NY Midtown
				// Manhattan/Times Square\",\"Knights Inn JFK Airport
				// NYC\",\"Courtyard by Marriott JFK Airport\",\"New York
				// Marriott Marquis\",\"Hilton Garden Inn New
				// York/Manhattan-Midtown East\",\"Classy and Cozy\",\"The
				// Strayhorn - Corporate Apartment\",\"JW Marriott Essex House
				// New York\",\"Sheridan Hotel\",\"DoubleTree Suites by Hilton
				// New York City - Times Square\",\"Vacayo NYC West
				// Apartments\",\"The Roosevelt Hotel New York City\",\"The
				// Solita Soho Hotel an Ascend Hotel Collection
				// Member\",\"Hampton Inn Manhattan/United Nations\",\"Manhattan
				// NYC-an Affinia hotel\",\"Ramada Plaza Newark Liberty
				// International Airport\",\"Hotel Sofitel New York\",\"Linden
				// Motor Inn\",\"Holiday Inn NYC - Lower East Side\",\"Hotel
				// Belleclaire\",\"Radisson Martinique on Broadway\",\"Hotel
				// Bliss\",\"Sleep Inn JFK Airport\",\"Holiday Inn Express New
				// York City Fifth Avenue\",\"Luxury Suites Lenox Ave \",\"The
				// Central Park North\",\"The James New York\",\"Hyatt Place
				// Flushing/LaGuardia Airport\",\"Sleep Inn Prospect Park
				// South\",\"Howard Johnson Inn Jamaica JFK Airport NYC\",\"Red
				// Lion Inn and Suites Brooklyn\",\"Topping Apartment\",\"Cesar
				// Suites at New York South\",\"Hampton Inn Manhattan/Times
				// Square Central\",\"Renaissance Newark Airport
				// Hotel\",\"Hampton Inn Manhattan Soho\",\"Hotel 48LEX New
				// York\",\"TRYP by Wyndham New York Times Square\",\"Herrick
				// Guest Suites 18th Street Apartment\",\"The Mark\",\"Quality
				// Inn\",\"Ameritania at Times Square\",\"DoubleTree by Hilton
				// Hotel New York - Times Square South\",\"Moblat 5\",\"Herrick
				// Guest Suites Chelsea Apartment\",\"Hampton Inn Brooklyn
				// Downtown NY\",\"Pointe Plaza Hotel\",\"Country Inn & Suites
				// By Carlson New York City in Queens NY\",\"Park Central New
				// York\",\"Holiday Inn Express New York - Manhattan West
				// Side\",\"The Roger Smith Hotel\",\"Hotel Indigo
				// BROOKLYN\",\"Club Quarters Hotel Wall Street\",\"Delz Bed &
				// Breakfast\",\"SpringHill Suites by Marriott New York
				// LaGuardia Airport\",\"La Quinta Inn and Suites JFK
				// Airport\",\"Skyline Hotel\",\"The Alfie - Corporate
				// Apartment\",\"Marrakech Hotel\",\"Hotel Le Bleu\",\"The
				// Langston - Corporate Apartment\",\"Best Western Plus
				// Hospitality House\",\"Carvi Hotel New York\",\"Hilton Garden
				// Inn New York Long Island City\",\"Comfort Inn & Suites JFK
				// Airport\",\"New York Marriott East Side\",\"The Paramount - A
				// Times Square New York Hotel\",\"The Evelyn (Formerly The
				// Gershwin Hotel)\",\"Crowne Plaza Times Square
				// Manhattan\",\"Herrick Guest Suites Christopher Street
				// Apartments\",\"Hotel Giraffe\",\"Gardens NYC-an Affinia
				// hotel\",\"The Kimberly Hotel & Suites\",\"Comfort Inn Staten
				// Island\",\"Best Western JFK Airport Hotel\",\"Midtown West
				// Vacation Rentals\",\"Fairfield Inn by Marriott New York
				// Manhattan/Times Square\",\"DoubleTree by Hilton Metropolitan
				// - New York City\",\"Hotel Edison\",\"Howard Johnson
				// Flushing\",\"Howard Johnson Long Island City\",\"Comfort Inn
				// Times Square West\",\"NU Hotel Brooklyn\",\"The Muse Hotel a
				// Kimpton Hotel\",\"The Library Hotel\",\"Newark Liberty
				// International Airport Marriott\",\"Gatsby Hotel ג€“ an Ascend
				// Hotel Collection Member\",\"Dylan Hotel\",\"Aloft
				// Harlem\",\"Galaxy Motel\",\"Radio City Apartments\",\"the
				// Quin\",\"Super 8 Jamaica North Conduit\",\"Days Inn JFK
				// Airport\",\"Cosmopolitan Hotel - Tribeca\",\"Days Inn
				// Brooklyn\",\"Lotte New York Palace\",\"Millennium Broadway
				// Hotel - Times Square\",\"Empire Hotel\",\"Moblat Apartments
				// 25-42\",\"Hotel Indigo NYC Chelsea\",\"Howard Johnson Bronx
				// Near Stadium\",\"Best Western Premier Herald Square\",\"The
				// Time Hotel\",\"The Belvedere Hotel\",\"DoubleTree by Hilton
				// New York City - Chelsea\",\"Leon Hotel\",\"Wyndham Midtown 45
				// at New York City\",\"Comfort Inn Midtown West\",\"W New York
				// - Union Square\",\"Holiday Inn Express New York City- Wall
				// Street\",\"McCarren Hotel & Pool\",\"Courtyard by Marriott
				// New York Manhattan/Times Square\",\"Hotel 41 at Times
				// Square\",\"Flushing Hotel\",\"Gabbs Apartment\",\"Courtyard
				// New York LaGuardia Airport\",\"Ramada Jamaica/Queens\",\"The
				// Pina - Corporate Apartment\",\"The St. Regis New
				// York\",\"Warwick New York Hotel\",\"Holiday Inn Express New
				// York City Times Square\",\"Oasis Motel in
				// Brooklyn\",\"Hampton Inn Manhattan/Times Square
				// South\",\"Holiday Inn Express Staten Island West\",\"Bronx
				// Guesthouse\",\"Holiday Inn New York City - Wall
				// Street\",\"Refinery Hotel\",\"W New York Times Square\",\"Row
				// NYC\",\"Best Western Queens Court Hotel\",\"Z NYC
				// Hotel\",\"Mayor Hotel\",\"Ravel Hotel\",\"Fitzpatrick
				// Manhattan Hotel\",\"Times Square Apartment\",\"Four Points by
				// Sheraton Manhattan SoHo Village\",\"Crowne Plaza Newark
				// Airport\",\"Langham Place New York Fifth Avenue\",\"Manhattan
				// Broadway Budget Hotel\",\"Hampton Inn & Suites Staten
				// Island\",\"The International Cozy Inn\",\"Renaissance New
				// York Hotel 57\",\"Hotel Vetiver\",\"The Knickerbocker
				// Hotel\",\"Avalon Hotel\",\"Residence Inn by Marriott New York
				// Manhattan/Times Square\",\"West Side Apartment\",\"Comfort
				// Inn Central Park West\",\"Hilton New York JFK Airport\",\"The
				// Westin New York Grand Central\",\"Comfort Inn Brooklyn Cruise
				// Terminal\",\"Sumner Hotel\",\"The Sohotel\",\"Park 79
				// Hotel\",\"Andaz 5th Avenue - a concept by Hyatt\",\"Fairfield
				// Inn & Suites New York Manhattan/Downtown East\",\"Sleep Inn
				// Brooklyn Downtown\",\"Hilton Garden Inn Times Square\",\"The
				// Michelangelo Hotel\",\"Sanctuary Hotel New York\",\"Wyndham
				// New Yorker\",\"Residence Inn by Marriott New York
				// Manhattan/Midtown East\",\"Superior Times Square
				// Apartments\",\"Hampton Inn Manhattan/Downtown-Financial
				// District\",\"onefinestay - Brooklyn apartments\",\"Four
				// Seasons Hotel New York\",\"Willamsburg Apartments\",\"Wingate
				// by Wyndham Manhattan Midtown\",\"Holiday Inn
				// Manhattan-Financial District\",\"The Gracie Inn\",\"The
				// Townhouse Inn of Chelsea\",\"SIXTY Lower East Side\",\"The
				// High Line Hotel\",\"Opera House Hotel\",\"Howard Johnson
				// Manhattan Soho\",\"LaGuardia Plaza Hotel\",\"Hotel
				// Chandler\",\"Wyndham Garden - Manhattan Chelsea West\",\"Best
				// Western Gregory Hotel\",\"Best Western Plus Brooklyn Bay
				// Hotel\",\"The Marmara Manhattan\",\"InterContinental - New
				// York Times Square\",\"Allies Inn Bed and Breakfast\",\"Best
				// Western Bowery Hanbee Hotel\",\"Holiday Inn Manhattan 6th Ave
				// - Chelsea\",\"Residence Inn New York The Bronx At Metro
				// Center Atrium\",\"Fairfield Inn by Marriott JFK
				// Airport\",\"Club Quarters opposite Rockefeller
				// Center\",\"Hyatt Herald Square New York\",\"Club Quarters
				// World Trade Center\",\"Fairfield Inn by Marriott New York
				// LaGuardia Airport/Astoria\",\"Lexington Inn - Brooklyn
				// NY\",\"The London NYC\",\"Hotel Five44\",\"Le Parker Meridien
				// New York\",\"Club Quarters Grand Central\",\"The Roxy
				// Hotel\",\"Comfort Inn Lower East Side\",\"SpringHill Suites
				// by Marriott Newark Liberty International\",\"Days Inn Long
				// Island City\",\"Comfort Inn Near Financial District\",\"The
				// Benjamin\",\"Stay The Night B&B\",\"Sheraton Tribeca New York
				// Hotel\",\"onefinestay - Downtown West apartments\",\"Upper
				// Yorkville Apartments\",\"The Peninsula New York\",\"Grand
				// Hyatt New York\",\"Herrick Guest Suites 18th St Oasis
				// Apartment\",\"Days Inn Bronx Near Stadium\",\"Best Western
				// Plus Prospect Park Hotel\",\"Club Quarters Midtown - Times
				// Square\",\"Fairfield Inn by Marriott New York Manhattan/Fifth
				// Avenue\",\"Ace Hotel New York\",\"Eurostars Wall
				// Street\",\"Hampton Inn Madison Square Garden Area
				// Hotel\",\"Residence Inn New York Manhattan/Central
				// Park\",\"Jets Motor Inn\",\"Hilton Club New York\",\"70 Park
				// Avenue Hotel a Kimpton Hotel\",\"Comfort Inn
				// Chelsea\",\"Fairfield Inn & Suites New York Queens/Queensboro
				// Bridge\",\"Courtyard by Marriott New York Manhattan/Herald
				// Square\",\"Staybridge Suites Times Square\",\"Anchor
				// Inn\",\"Wyndham Garden Hotel Newark Airport\",\"The
				// Ritz-Carlton New York Battery Park\",\"Fairfield Inn & Suites
				// by Marriott New York ManhattanChelsea\",\"Vacayo NYC East
				// Apartments\",\"Sheraton LaGuardia East Hotel\",\"Travel Inn
				// Hotel\",\"Hotel 31\",\"Comfort Inn JFK Airport\",\"Hyatt
				// Place New York Midtown South\",\"Waldorf Astoria New
				// York\",\"The Ritz-Carlton New York Central Park\",\"Condor
				// Hotel\",\"Holiday Inn Express Kennedy Airport\",\"Marco
				// LaGuardia Hotel by Lexington\",\"1 Hotel Central Park\",\"The
				// Lucerne Hotel\",\"Ramada Long Island City\",\"Shelburne
				// NYC-an Affinia hotel\",\"Chelsea Apartment\",\"Hotel Le
				// Jolie\",\"Times Square Grande Duplex\",\"Red Roof Inn
				// Queens\",\"The Lexington New York City Autograph
				// Collection\",\"Comfort Inn Brooklyn City Center\",\"Hotel
				// Wales\",\"Hilton New York Fashion District\",\"The Jewel
				// facing Rockefeller Center\",\"New York Marriott
				// Downtown\",\"Four Points by Sheraton Long Island City
				// Queensboro Bridge\",\"Courtyard by Marriott Newark Liberty
				// International Airport\",\"Home2 Suites by Hilton NY Long
				// Island City/Manhattan View\",\"Gild Hall A Thompson
				// Hotel\",\"Red Carpet Inn New York City\",\"East Village
				// Suites\",\"Fairfield Inn New York Manhattan/Financial
				// District\",\"Hotel Boutique at Grand Central\",\"Fairfield
				// Inn New York Midtown Manhattan/Penn Station\",\"Hotel
				// 17\",\"Holiday Inn Express Laguardia Airport\",\"Best Western
				// Plaza Hotel\",\"The Paul an Ascend Hotel Collection
				// Member\",\"Herald Square Hotel\",\"New World Hotel\",\"Hotel
				// St. James\",\"Trump Soho New York\",\"YOTEL New York at Times
				// Square\",\"Bentley Hotel\",\"CITY ROOMS+ NYC SoHo\",\"The
				// Pearl New York\",\"Murray Hill Apartments\",\"Eventi Hotel a
				// Kimpton Hotel\",\"A Hostel\",\"Rodeway Inn Bronx
				// Zoo\",\"Extended Stay America Elizabeth - Newark
				// Airport\",\"Fairfield Inn New York Long Island City/Manhattan
				// View\",\"Holiday Inn Express Brooklyn\",\"Sleep Inn
				// Brooklyn\",\"Hilton Garden Inn New York / Staten
				// Island\",\"Riviera Hotel\",\"Sugar Hill Harlem
				// Inn\",\"Mandarin Oriental New York\",\"World Center
				// Hotel\",\"The Towers at Lotte New York Palace\",\"MySuites -
				// Christopher Suites\",\"The Lowell\",\"Holiday Inn - Long
				// Island City - Manhattan View\",\"Aloft New York Brooklyn
				// Hotel\",\"Comfort Inn Sunset Park / Park Slope\",\"The
				// Duchamp - Corporate Apartment\",\"Econo Lodge South Ozone
				// Park\",\"Guest House Off Park\",\"Studio Self Catering Apt
				// Lower East Side\",\"Distrikt Hotel New York City an Ascend
				// Collection Hotel\",\"NYLO New York City\",\"Red Roof Inn
				// Flushing New York - LaGuardia Airport\",\"Quality Inn
				// Woodside\",\"Courtyard by Marriott New York Manhattan /
				// Central Park\",\"City Club Hotel\",\"Hilton Newark
				// Airport\",\"Hampton Inn Newark Airport\",\"The Box House
				// Hotel\",\"Fairfield Inn & Suites by Marriott New York
				// Brooklyn\",\"Comfort Inn Times Square South Area\",\"Paris
				// Suites Hotel\",\"San Carlos Hotel\",\"Vacation Rental in New
				// York\",\"Comfort Inn & Suites LaGuardia Airport\",\"Baccarat
				// Hotel and Residences New York\",\"The Roger\",\"Hampton Inn
				// JFK Airport\",\"Lexington Inn JFK Airport\",\"The Plaza
				// Hotel\",\"DoubleTree by Hilton New York City - Financial
				// District\",\"Hampton Inn New York Chelsea\",\"Windsor
				// Hotel\",\"Residence Inn New York Manhattan/World Trade Center
				// Area\",\"Loews Regency New York Hotel\",\"Hotel de
				// Point\",\"Super 8 Brooklyn / Park Slope Hotel\",\"Hampton Inn
				// New York - 35th Street - Empire State Building\",\"Moblat
				// Apartments 30-68\",\"Fairfield Inn & Suites Newark Liberty
				// International Airport\",\"Courtyard New York Manhattan/Times
				// Square West\",\"Super 8 Bronx\",\"Colonial House Inn\",\"The
				// Hotel 91\",\"Sofia Inn\",\"Holiday Inn New York JFK Airport
				// Area\",\"Howard Johnson Express Inn Bronx\",\"Embassy Suites
				// Newark Airport\",\"Hotel 32 32\",\"Holiday Inn New York
				// City-Midtown-57th Street\",\"Residence Inn Newark
				// Elizabeth/Liberty International Airport\",\"Smyth A Thompson
				// Hotel\",\"Upper West Side Brownstone\",\"Hampton Inn New York
				// - LaGuardia Airport\",\"Hotel Elysee\",\"The Sherry
				// Netherland\",\"Adria Hotel And Conference
				// Center\",\"WestHouse New York\",\"Hilton Garden Inn
				// Tribeca\",\"La Quinta Inn & Suites Brooklyn Downtown\",\"Off
				// Soho Suites Hotel\",\"New York Hilton Midtown\",\"TRYP By
				// Wyndham Times Square South\",\"Park Hyatt New
				// York\",\"Sheraton JFK Airport Hotel\",\"Manhattan West
				// Hotel\",\"New York Marriott at the Brooklyn Bridge\",\"Four
				// Points by Sheraton Midtown-Times Square\",\"Ramada
				// Bronx\",\"HYH Hotel Flushing\",\"Courtyard by Marriott New
				// York City Manhattan Midtown East\",\"Martha
				// Washington\",\"Park Lane Hotel\",\"West 57th Street by Hilton
				// Club\",\"Midtown West Apartment\",\"Hotel Mulberry\",\"The
				// Paper Factory Hotel\",\"Royal Park Hotel and
				// Hostel\",\"Riverside Tower Hotel\",\"Blue Moon Boutique
				// Hotel\",\"The Milburn Hotel\",\"ink48 hotel a Kimpton
				// Hotel\",\"New York LaGuardia Airport Marriott\",\"Conrad New
				// York\",\"The Shoreham Hotel\",\"Andaz Wall Street - a concept
				// by Hyatt\",\"Garden Inn & Suites\",\"Airport Hotel Inn &
				// Suites - Newark Airport\",\"Super 8 Long Island City LGA
				// Hotel\",\"Metro Studio Midtown West\",\"The Marcel at
				// Gramercy\",\"Chambers Hotel\",\"Fairfield Inn by Marriott
				// LaGuardia Airport/Flushing\",\"The Surrey\",\"Courtyard by
				// Marriott New York Manhattan/Upper East Side\",\"Ramada
				// Flushing Queens\",\"The Chatwal A Luxury Collection
				// Hotel\",\"6 Columbus - a SIXTY Hotel\",\"Crowne Plaza JFK
				// Airport New York City\",\"Hotel Pennsylvania\",\"Hilton
				// Garden Inn Queens/JFK Airport\"}");
				// sb.append('\n');
				// sb.append("@attribute Description {\"A\", \"B\", \"C\",
				// \"D\", \"E\" }");
				// sb.append('\n');
				//
				// sb.append('\n');
				// sb.append("@data");
				// sb.append('\n');
				for (hotelinvitation invite : currHotel) {
					sb.append('"' + invite.hotelName + "\",");
					sb.append(invite.month + ",");
					sb.append(invite.DayInWeek + ",");
					long daysBetween;
					if (invite.runDate != null && invite.cheakIn != null) {

						daysBetween = invite.cheakIn.getTime() - invite.runDate.getTime();
					} else {
						daysBetween = 0;
					}
					long diffDays = daysBetween / (24 * 60 * 60 * 1000);
					sb.append(diffDays + ",");
					double price = invite.totalPriceForCombine == -1 ? invite.originalPrice
							: invite.totalPriceForCombine;
					sb.append(price + ",");
					sb.append('"' + invite.companyName + "\",");
					sb.append('"' + resultCombine.get(invite.descriptionPartOne) + '"');
					sb.append('\n');

					System.out.println("doneH");
				}
				pw.write(sb.toString());
				System.out.println("done!!!!");
				pw.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}

	static Date convertDate(String date, DateFormat formatterMinus, DateFormat formatterSlash) throws ParseException {
		date = date.replace("\"", "");
		try {
			if (date.contains("-")) {
				return formatterMinus.parse(date);
			} else {
				return formatterSlash.parse(date);
			}
		} catch (Exception r) {
			return null;
		}

	}
	// List<String> companiesNames = new ArrayList<>();

	static String getHotelCompany(String HotelName) {
		for (String companyName : companiesNames) {
			if (HotelName.contains(companyName)) {
				return companyName;
			}
		}
		return HotelName;
	}

	private static Function<String, hotelinvitation> mapToItem = (line) -> {
		String[] invite = line.split(",");// a CSV has comma separated lines
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat formatterSlash = new SimpleDateFormat("dd/MM/yyyy");
		// DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		hotelinvitation newInvite = new hotelinvitation();
		// newInvite.runDate = (Date)formatter.parse("06/27/2007");
		// newInvite.Id = tryParseInt(invite[0]) ?
		// Integer.parseInt(invite[0]) : -1;
		try {
			newInvite.runDate = convertDate(invite[1], formatter, formatterSlash);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // formatter.parse(invite[1].toString());
		newInvite.providerId = tryParseInt(invite[2]) ? Integer.parseInt(invite[2]) : -1;
		try {
			newInvite.cheakIn = convertDate(invite[3], formatter, formatterSlash);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			newInvite.cheakOut = convertDate(invite[4], formatter, formatterSlash);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long daysBetween = newInvite.cheakIn.getTime() - newInvite.runDate.getTime();
		newInvite.daysDiff = daysBetween / (24 * 60 * 60 * 1000);
		newInvite.month = newInvite.cheakIn.getMonth();

		Calendar c = Calendar.getInstance();
		c.setTime(newInvite.cheakIn);
		newInvite.DayInWeek = c.get(Calendar.DAY_OF_WEEK);

		newInvite.days = tryParseInt(invite[5]) ? Integer.parseInt(invite[5]) : -1;
		newInvite.descriptionDays = invite[6];
		newInvite.originalPrice = tryParseDouble(invite[7]) ? Double.parseDouble(invite[7]) : -1;
		newInvite.roomType = invite[8];
		newInvite.codeRate = tryParseInt(invite[9]) ? Integer.parseInt(invite[9]) : -1;
		newInvite.availibleRooms = tryParseInt(invite[10]) ? Integer.parseInt(invite[10]) : -1;
		// public Boolean idRefundable;
		newInvite.boardType = tryParseInt(invite[12]) ? Integer.parseInt(invite[12]) : -1;
		newInvite.descriptionPartOne = invite[13];
		newInvite.pricePartOne = tryParseDouble(invite[14]) ? Double.parseDouble(invite[14]) : -1;
		newInvite.typePartOne = invite[15];
		newInvite.codeRatePartOne = invite[16];
		newInvite.avalibleRoomsPartOne = tryParseInt(invite[17]) ? Integer.parseInt(invite[17]) : -1;
		// public Boolean idRefundablePartOne;
		newInvite.boardTypePartOne = tryParseInt(invite[19]) ? Integer.parseInt(invite[19]) : -1;
		newInvite.descriptionPartTwo = invite[20];
		newInvite.pricePartTwo = tryParseDouble(invite[21]) ? Double.parseDouble(invite[21]) : -1;
		newInvite.typePartTwo = invite[22];
		newInvite.codeRatePartTwo = invite[23];
		newInvite.avalibleRoomsPartTwo = tryParseInt(invite[24]) ? Integer.parseInt(invite[24]) : -1;
		// public Boolean idRefundablePartTwo;
		newInvite.boardTypePartTwo = tryParseDouble(invite[26]) ? Double.parseDouble(invite[26]) : -1;
		newInvite.totalPriceForCombine = tryParseDouble(invite[27]) ? Double.parseDouble(invite[27]) : -1;
		newInvite.codeHotel = tryParseInt(invite[28]) ? Integer.parseInt(invite[28]) : -1;
		newInvite.hotelName = invite[29];
		newInvite.starsRate = tryParseDouble(invite[30]) ? Double.parseDouble(invite[30]) : -1;
		newInvite.hotelPicture = invite[31];
		newInvite.lat = invite[32];
		newInvite.longg = invite[33];
		newInvite.providerCode = invite[34];
		newInvite.decriptionBrandProvider = invite[35];
		newInvite.companyName = getHotelCompany(newInvite.hotelName);
		return newInvite;
		// more initialization goes here
	};

	@SuppressWarnings("deprecation")
	static hotelinvitation parseToHotel(String[] invite) throws ParseException {
		if (invite.length > 0) {

			hotelinvitation newInvite = new hotelinvitation();

			newInvite.runDate = convertDate(invite[1], formatter, formatterSlash);// formatter.parse(invite[1].toString());
			newInvite.providerId = tryParseInt(invite[2]) ? Integer.parseInt(invite[2]) : -1;
			newInvite.cheakIn = convertDate(invite[3], formatter, formatterSlash);
			newInvite.cheakOut = convertDate(invite[4], formatter, formatterSlash);
			long daysBetween = newInvite.cheakIn.getTime() - newInvite.runDate.getTime();
			newInvite.daysDiff = daysBetween / (24 * 60 * 60 * 1000);
			newInvite.month = newInvite.cheakIn.getMonth();

			
			c.setTime(newInvite.cheakIn);
			newInvite.DayInWeek = c.get(Calendar.DAY_OF_WEEK);

			newInvite.days = tryParseInt(invite[5]) ? Integer.parseInt(invite[5]) : -1;
			newInvite.descriptionDays = invite[6];
			newInvite.originalPrice = tryParseDouble(invite[7]) ? Double.parseDouble(invite[7]) : -1;
			newInvite.roomType = invite[8];
			newInvite.codeRate = tryParseInt(invite[9]) ? Integer.parseInt(invite[9]) : -1;
			newInvite.availibleRooms = tryParseInt(invite[10]) ? Integer.parseInt(invite[10]) : -1;
			// public Boolean idRefundable;
			newInvite.boardType = tryParseInt(invite[12]) ? Integer.parseInt(invite[12]) : -1;
			newInvite.descriptionPartOne = invite[13];
			newInvite.pricePartOne = tryParseDouble(invite[14]) ? Double.parseDouble(invite[14]) : -1;
			newInvite.typePartOne = invite[15];
			newInvite.codeRatePartOne = invite[16];
			newInvite.avalibleRoomsPartOne = tryParseInt(invite[17]) ? Integer.parseInt(invite[17]) : -1;
			// public Boolean idRefundablePartOne;
			newInvite.boardTypePartOne = tryParseInt(invite[19]) ? Integer.parseInt(invite[19]) : -1;
			newInvite.descriptionPartTwo = invite[20];
			newInvite.pricePartTwo = tryParseDouble(invite[21]) ? Double.parseDouble(invite[21]) : -1;
			newInvite.typePartTwo = invite[22];
			newInvite.codeRatePartTwo = invite[23];
			newInvite.avalibleRoomsPartTwo = tryParseInt(invite[24]) ? Integer.parseInt(invite[24]) : -1;
			// public Boolean idRefundablePartTwo;
			newInvite.boardTypePartTwo = tryParseDouble(invite[26]) ? Double.parseDouble(invite[26]) : -1;
			newInvite.totalPriceForCombine = tryParseDouble(invite[27]) ? Double.parseDouble(invite[27]) : -1;
			newInvite.codeHotel = tryParseInt(invite[28]) ? Integer.parseInt(invite[28]) : -1;
			newInvite.hotelName = invite[29];
			newInvite.starsRate = tryParseDouble(invite[30]) ? Double.parseDouble(invite[30]) : -1;
			newInvite.hotelPicture = invite[31];
			newInvite.lat = invite[32];
			newInvite.longg = invite[33];
			newInvite.providerCode = invite[34];
			newInvite.decriptionBrandProvider = invite[35];
			newInvite.companyName = getHotelCompany(newInvite.hotelName);
			return newInvite;
		} else {
			return new hotelinvitation();
		}
	}
}
