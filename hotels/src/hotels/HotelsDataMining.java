package hotels;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.xml.crypto.Data;

import org.json.JSONObject;
import org.omg.CORBA.INV_FLAG;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class HotelsDataMining {

	public static final String EXCEL_SOURCE_FOLDER = "C:\\Users\\Ofra\\Desktop\\èì åâì\\hotels research\\original csv";
	public static final String EXCEL_SOURCE_FOLDER_PREDICTION = "C:\\Users\\Ofra\\Desktop\\èì åâì\\hotels research\\randomResult";
	public static final String FILE_NAME_PREDICTION = "wekaResultReal with conclusions.csv";
	static List<String> companiesNames = new ArrayList<>();
	private static final int ITERATIONS = 5;
	private static final double MEG = (Math.pow(1024, 2));
	private static final int RECORD_COUNT = 4000000;
	static HashMap<String, HashMap<Integer, HashMap<Integer, List<hotelinvitation>>>> dictionaryInvitations;

	enum Description {
		A("A"), B("B"), C("C"), D("D"), E("E");
		private String str;

		String getStr() {
			return this.str;
		}

		Description(String str) {
			this.str = str;
		}
	}

	public static void main(String[] args) throws Exception {

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

		List<hotelinvitation> inputList = new ArrayList<hotelinvitation>();

		/**
		 * Create a dictionary by hotel name, checkin month, checkin dayInWeek,
		 * days amount between rundate and checkIn
		 */

		dictionaryInvitations = new HashMap<String, HashMap<Integer, HashMap<Integer, List<hotelinvitation>>>>();
		try {
			final File folder = new File(EXCEL_SOURCE_FOLDER);
			for (final File fileEntry : folder.listFiles()) {
				System.out.println(fileEntry.getName());

				br = new BufferedReader(new FileReader(fileEntry.getAbsolutePath()));

				inputList = br.lines().skip(0).map(mapToItem).filter(newInvite -> newInvite.days == 5)
						.collect(Collectors.toList());
				for (hotelinvitation newInvite : inputList) {
					// Check if the hotel exist
					if (dictionaryInvitations.containsKey(newInvite.hotelName)) {

						// the hotel exist check if it is in the same month
						if (dictionaryInvitations.get(newInvite.hotelName).containsKey(newInvite.month)) {

							// the hotel exist, it is in the same month,
							// check if it is in the same day in week
							if (dictionaryInvitations.get(newInvite.hotelName).get(newInvite.month)
									.containsKey(newInvite.dayInMonth)) {

								// the hotel exist, it is in the same month,
								// it is in the same day in week, check if
								// it the same diff date

								// it is
								// Adding the hotel to the same group
								dictionaryInvitations.get(newInvite.hotelName).get(newInvite.month)
										.get(newInvite.dayInMonth).add(newInvite);

							} else {
								addDayInMonth(dictionaryInvitations, newInvite);
							}
						} else {
							addMonth(dictionaryInvitations, newInvite);
						}
					} else {
						addHotel(dictionaryInvitations, newInvite);
					}
				}
			}
			// }

			// getAllFilteredData();
			List<hotelinvitation> minimumIvitations = new ArrayList<>();

			HashMap<String, List<hotelinvitation>> minimumByHotels = new HashMap<String, List<hotelinvitation>>();
			HashMap<String, List<hotelinvitation>> minimumByCompany = new HashMap<String, List<hotelinvitation>>();
			HashMap<String, List<hotelinvitation>> minimumByCities = new HashMap<String, List<hotelinvitation>>();
			for (HashMap<Integer, HashMap<Integer, List<hotelinvitation>>> hotels : dictionaryInvitations.values()) {
				for (HashMap<Integer, List<hotelinvitation>> months : hotels.values()) {
					for (List<hotelinvitation> daysInWeek : months.values()) {
						double minimumPrice = 999999999;
						hotelinvitation minimumHotel = new hotelinvitation();
						// for (List<hotelinvitation> daysdiff :
						// daysInWeek.values()) {

						// Go over all invitations with the same checkIn
						// date and with the same hotel name
						if (isGroupValid(daysInWeek)) {

							for (hotelinvitation invite : daysInWeek) {

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

							// Check if there is more than three options of
							// description
							// Adding to general minimum invites list
							minimumIvitations.add(minimumHotel);

							// Check if this hotel exist in company list
							if (minimumByCompany.containsKey(minimumHotel.companyName)) {

								// It's exist
								// Adding the hotel into match company
								minimumByCompany.get(minimumHotel.companyName).add(minimumHotel);
							} else {
								// It's not
								// Adding new company to the list
								// Adding the new invite
								List<hotelinvitation> min = new ArrayList<>();
								min.add(minimumHotel);
								minimumByCompany.put(minimumHotel.companyName, min);
							}

							// Check if this hotel exist in hotels list
							if (minimumByHotels.containsKey(minimumHotel.hotelName)) {
								// It's exist
								// Adding the hotel into match hotel
								minimumByHotels.get(minimumHotel.hotelName).add(minimumHotel);
							} else {
								// It's not
								// Adding new hotel to the list
								// Adding the new invite
								List<hotelinvitation> min = new ArrayList<>();
								min.add(minimumHotel);
								minimumByHotels.put(minimumHotel.hotelName, min);
							}

							if (minimumByCities.containsKey(minimumHotel.City)) {
								// It's exist
								// Adding the hotel into match hotel
								minimumByCities.get(minimumHotel.City).add(minimumHotel);
							} else {
								// It's not
								// Adding new hotel to the list
								// Adding the new invite
								List<hotelinvitation> min = new ArrayList<>();
								min.add(minimumHotel);
								minimumByCities.put(minimumHotel.City, min);
							}
						}
						// }
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

			// writeAllData();

			writeResultByHotelArff(minimumByHotels);
			// writeResultByCompanyArff(minimumByCompany);

			// writeResulyByCity(minimumByCities,minimumIvitations);

			// writeLargeResult(minimumIvitations);
			// writeRandomResult(minimumIvitations, dictionaryInvitations);

			writeRandomResult(
					IntStream.range(0, minimumIvitations.size()).filter(i -> i % 4 == 0)
							.mapToObj(i -> minimumIvitations.get(i)).collect(Collectors.toList()),
					dictionaryInvitations);
			//
			// readFilesAndSearchPricingInDictionary(dictionaryInvitations);

		} catch (Exception e) {
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
		dictionaryInvitations.get(newInvite.hotelName).get(newInvite.month).get(newInvite.dayInMonth)
				.put((long) newInvite.days, list);
	}

	static void addDayInMonth(
			HashMap<String, HashMap<Integer, HashMap<Integer, List<hotelinvitation>>>> dictionaryInvitations,
			hotelinvitation newInvite) {
		List<hotelinvitation> list = new ArrayList<hotelinvitation>();
		list.add(newInvite);
		dictionaryInvitations.get(newInvite.hotelName).get(newInvite.month).put(newInvite.dayInMonth, list);
		// addDayDiff(dictionaryInvitations, newInvite);
	}

	static void addMonth(
			HashMap<String, HashMap<Integer, HashMap<Integer, List<hotelinvitation>>>> dictionaryInvitations,
			hotelinvitation newInvite) {
		HashMap<Integer, List<hotelinvitation>> a = new HashMap<Integer, List<hotelinvitation>>();
		dictionaryInvitations.get(newInvite.hotelName).put(newInvite.month, a);
		addDayInMonth(dictionaryInvitations, newInvite);
	}

	static void addHotel(
			HashMap<String, HashMap<Integer, HashMap<Integer, List<hotelinvitation>>>> dictionaryInvitations,
			hotelinvitation newInvite) {
		HashMap<Integer, HashMap<Integer, List<hotelinvitation>>> a = new HashMap<Integer, HashMap<Integer, List<hotelinvitation>>>();
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

		long daysBetween;
		for (hotelinvitation invite : minimumIvitations) {
			sb.append(invite.hotelName + ",");
			sb.append(invite.month + ",");
			sb.append(invite.DayInWeek + ",");

			// Get day diff
			if (invite.runDate != null && invite.cheakIn != null) {
				daysBetween = invite.cheakIn.getTime() - invite.runDate.getTime();
			} else {
				daysBetween = 0;
			}
			long diffDays = daysBetween / (24 * 60 * 60 * 1000);
			sb.append(diffDays + ",");

			// Get the real price
			double price = invite.totalPriceForCombine == -1 ? invite.originalPrice : invite.totalPriceForCombine;
			sb.append(price + ",");
			sb.append(invite.companyName + ",");

			// Get description
			sb.append(getDescriptoin(invite.descriptionPartOne));
			sb.append('\n');

			pw.write(sb.toString());

		}

		pw.close();
		System.out.println("done!");
		return false;
	}

	static boolean writeLargeResult(List<hotelinvitation> minimumIvitations) {
		List<String> records = new ArrayList<String>(RECORD_COUNT);
		int size = 0;
		StringBuilder sb = new StringBuilder();
		sb.append("hotelName,");
		sb.append("month,");
		sb.append("DayInWeek,");
		sb.append("diffDays,");
		sb.append("price,");
		sb.append("Company,");
		sb.append("Description");
		sb.append('\n');
		records.add(sb.toString());
		size += sb.toString().getBytes().length;
		long daysBetween;
		for (hotelinvitation invite : minimumIvitations) {
			sb = new StringBuilder();
			sb.append(invite.hotelName + ",");
			sb.append(invite.month + ",");
			sb.append(invite.DayInWeek + ",");

			// Get day diff
			if (invite.runDate != null && invite.cheakIn != null) {
				daysBetween = invite.cheakIn.getTime() - invite.runDate.getTime();
			} else {
				daysBetween = 0;
			}
			long diffDays = daysBetween / (24 * 60 * 60 * 1000);
			sb.append(diffDays + ",");

			// Get the real price
			double price = invite.totalPriceForCombine == -1 ? invite.originalPrice : invite.totalPriceForCombine;
			sb.append(price + ",");
			sb.append(invite.companyName + ",");

			// Get description
			sb.append(getDescriptoin(invite.descriptionPartOne));
			sb.append('\n');

			records.add(sb.toString());
			size += sb.toString().getBytes().length;
			;
		}

		System.out.println(records.size() + " 'records'");
		System.out.println(size / MEG + " MB");

		for (int i = 0; i < ITERATIONS; i++) {
			System.out.println("\nIteration " + i);
			try {
				writeRaw(records);
				writeBuffered(records, 8192);
				writeBuffered(records, (int) MEG);
				writeBuffered(records, 4 * (int) MEG);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	static void writeRaw(List<String> records) throws IOException {
		File file = new File("C:\\Users\\Ofra\\Desktop\\èì åâì\\hotels research\\randomResult\\realResult.csv");
		// PrintWriter pw = new PrintWriter(new File("results.csv"));
		try {
			FileWriter writer = new FileWriter(file);
			System.out.print("Writing raw... ");
			write(records, writer);
		} finally {
			// comment this out if you want to inspect the files afterward
			// file.delete();
		}
	}

	static void writeBuffered(List<String> records, int bufSize) throws IOException {
		File file = File.createTempFile("foo", ".txt");
		try {
			FileWriter writer = new FileWriter(file);
			BufferedWriter bufferedWriter = new BufferedWriter(writer, bufSize);

			System.out.print("Writing buffered (buffer size: " + bufSize + ")... ");
			write(records, bufferedWriter);
		} finally {
			// comment this out if you want to inspect the files afterward
			// file.delete();
		}
	}

	static void write(List<String> records, Writer writer) throws IOException {
		long start = System.currentTimeMillis();
		for (String record : records) {
			writer.write(record);
		}
		writer.flush();
		writer.close();
		long end = System.currentTimeMillis();
		System.out.println((end - start) / 1000f + " seconds");
	}

	static void writeResulyByCity(HashMap<String, List<hotelinvitation>> minimumByHotels,
			List<hotelinvitation> minimumByHotels2) {
		List<hotelinvitation> wekaLearn = IntStream.range(0, minimumByHotels2.size()).filter(i -> i % 4 != 0)
				.mapToObj(i -> minimumByHotels2.get(i)).collect(Collectors.toList());
		List<hotelinvitation> wekaTest = IntStream.range(0, minimumByHotels2.size()).filter(i -> i % 4 == 0)
				.mapToObj(i -> minimumByHotels2.get(i)).collect(Collectors.toList());
		String folderName = "þþwekaPredictions2";
		writeCSVFileToHotel(wekaLearn, folderName, true, "wekaLearnCSV");
		writeCSVFileToHotel(wekaTest, folderName, true, "wekaTestCSV");
		writeArffFileToHotel(wekaLearn, folderName, true, "wekaLearnArff");
		writeArffFileToHotel(wekaTest, folderName, true, "wekaTestArff");
		for (List<hotelinvitation> currHotel : minimumByHotels.values()) {
			writeArffFileToHotel(currHotel, "resultCitiesArff", true, "");
			// writeCSVFileToHotel(currHotel, "þþresultCitiesCSV", true);

			/*
			 * List<hotelinvitation> wekaLearn = IntStream.range(0,
			 * currHotel.size()).filter(i -> i % 4 != 0) .mapToObj(i ->
			 * currHotel.get(i)).collect(Collectors.toList());
			 * List<hotelinvitation> wekaTest = IntStream.range(0,
			 * currHotel.size()).filter(i -> i % 4 == 0) .mapToObj(i ->
			 * currHotel.get(i)).collect(Collectors.toList()); String folderName
			 * = "þþwekaPredictions2"; writeCSVFileToHotel(wekaLearn,
			 * folderName, true, "wekaLearnCSV"); writeCSVFileToHotel(wekaTest,
			 * folderName, true, "wekaTestCSV"); writeArffFileToHotel(wekaLearn,
			 * folderName, true, "wekaLearnArff");
			 * writeArffFileToHotel(wekaTest, folderName, true, "wekaTestArff");
			 */
			// if (isGroupValid(currHotel)) {
			// writeArffFileToHotel(currHotel, "resultCities");
			// writeCSVFileToHotel(currHotel, "resultCities");
			// }
		}

	}

	static boolean writeResultByHotelArff(HashMap<String, List<hotelinvitation>> minimumByHotels) {
		for (List<hotelinvitation> currHotel : minimumByHotels.values()) {
			writeArffFileToHotel(currHotel, "resultsHotelArff", false, "");
			writeCSVFileToHotel(currHotel, "resultHotelCSV", false, "");
			if (isGroupValid(currHotel)) {
				writeArffFileToHotel(currHotel, "specialArff", false, "");
				writeCSVFileToHotel(currHotel, "specialCSV", false, "");
			}
		}
		return true;
	}

	static boolean writeResultByCompanyArff(HashMap<String, List<hotelinvitation>> minimumByHotels) {
		for (List<hotelinvitation> currHotel : minimumByHotels.values()) {
			writeArffFileToHotel(currHotel, "resultsCompanyArff", false, "");
			writeCSVFileToHotel(currHotel, "resultCompanyCSV", false, "");
			if (isGroupValid(currHotel)) {
				writeArffFileToHotel(currHotel, "specialCompanyArff", false, "");
				writeCSVFileToHotel(currHotel, "specialCompanyCSV", false, "");
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

	static String getHotelCompany(String HotelName) {
		for (String companyName : companiesNames) {
			if (HotelName.contains(companyName)) {
				return companyName;
			}
		}
		return HotelName;
	}

	static String getDescriptoin(String desc) {
		if (desc.contains("[1]")) {
			return "A";
		} else if (desc.contains("[1-2]")) {
			return "B";
		} else if (desc.contains("[1-3]")) {
			return "C";
		} else if (desc.contains("[1-4]")) {
			return "D";
		} else if (desc.contains("[1-5]")) {
			return "E";
		}

		return "";
	}

	static boolean isGroupValid(List<hotelinvitation> hotels) {
		List<Boolean> valiodation = new ArrayList<>(Arrays.asList(new Boolean[5]));
		Collections.fill(valiodation, Boolean.FALSE);
		for (hotelinvitation hotel : hotels) {

			valiodation.set(Description.valueOf(getDescriptoin(hotel.descriptionPartOne)).ordinal(), true);
		}

		for (boolean b : valiodation) {
			if (!b) {
				return false;
			}
		}
		return true;
	}

	static boolean writeArffFileToHotel(List<hotelinvitation> currHotel, String folderName, boolean isCity,
			String fileName) {
		try {
			currHotel.get(0).hotelName = currHotel.get(0).hotelName.replaceAll("\"", "");
			currHotel.get(0).companyName = currHotel.get(0).companyName.replaceAll("\"", "");
			currHotel.get(0).City = currHotel.get(0).City.replaceAll("\"", "");
			PrintWriter pw;
			if (!fileName.isEmpty()) {
				pw = new PrintWriter(new File("C:\\Users\\Ofra\\Desktop\\èì åâì\\hotels research\\" + folderName + "\\"
						+ fileName + ".arff"));
			} else if (!isCity) {
				pw = new PrintWriter(new File("C:\\Users\\Ofra\\Desktop\\èì åâì\\hotels research\\" + folderName + "\\"
						+ currHotel.get(0).hotelName + ".arff"));
			} else {
				pw = new PrintWriter(new File("C:\\Users\\Ofra\\Desktop\\èì åâì\\hotels research\\" + folderName + "\\"
						+ currHotel.get(0).City + ".arff"));
			}
			StringBuilder sb = new StringBuilder();
			sb.append("@relation \"" + currHotel.get(0).hotelName + "\"");
			sb.append('\n');
			sb.append('\n');
			sb.append(
					"@attribute hotelName {'Home NYC','Hampton Inn Manhattan/Times Square Central','Comfort Inn & Suites JFK Airport','Mandarin Oriental New York','Hotel Bliss','W New York - Downtown','NYLO New York City','Fairfield Inn & Suites New York Queens/Queensboro Bridge','InterContinental - New York Times Square','Hilton Garden Inn New York-Times Square Central','The Mansfield Hotel','Holiday Inn - Long Island City - Manhattan View','Row NYC','CITY ROOMS NYC CHELSEA','Park Central New York','Colonial House Inn','The Pearl New York','Hotel Le Jolie','Hilton New York JFK Airport','The Shoreham Hotel','The Sherry Netherland','Comfort Inn Near Financial District','Skyline Hotel','Best Western Plus LaGuardia Airport Hotel Queens','Sheraton JFK Airport Hotel','Courtyard by Marriott New York Manhattan/Herald Square','Flatiron Hotel','Rodeway Inn Near JFK Airport','Bronx Guesthouse','Millennium Broadway Hotel - Times Square','Americas Best Value Inn','onefinestay - Chelsea apartments','Days Hotel Broadway','New York LaGuardia Airport Marriott','Jet Luxury at the Trump SoHo','Embassy Suites Newark Airport','Kings Hotel','Fairfield Inn by Marriott JFK Airport','Holiday Inn Manhattan 6th Ave - Chelsea',MiniStay,'Two-Bedroom Self-Catering Apartment - Lower East Side','Times Square Apartment','Wellington Hotel','Sanctuary NYC Retreats','Knights Inn JFK Airport NYC','Hilton Club New York','McCarren Hotel & Pool','CAMBRiA hotel & suites New York Times Square','The Local','The Michelangelo Hotel','Park Lane Hotel','Hampton Inn Manhattan/Times Square South','Country Inn & Suites By Carlson New York City in Queens NY','Howard Johnson Long Island City','Hotel BPM - Brooklyn New York','Bowery Grand Hotel','Upper Yorkville Apartments','onefinestay - Downtown West apartments','Wyndham Garden Brooklyn Sunset Park','Excelsior Hotel','Seton Hotel','East 55th Apartments','Fairfield Inn New York Midtown Manhattan/Penn Station','W New York Times Square','NOMO SOHO','Hotel Giraffe','Henry Norman Hotel','The Solita Soho Hotel an Ascend Hotel Collection Member','Hotel Hugo',Royalton,'Hotel Mulberry','Courtyard by Marriott New York City Manhattan Fifth Avenue','Herrick Guest Suites 14th Street Apartment','Z NYC Hotel','Super 8 Jamaica North Conduit','The Manhattan Club','Sleep Inn Prospect Park South','Oakwood at The Nash','Hotel Indigo Lower East Side New York','The Time Hotel','Sanctuary Hotel New York','Clarion Hotel Park Avenue','Hilton Garden Inn Queens/JFK Airport','The Bowery House','Courtyard by Marriott New York City Manhattan Midtown East','Comfort Inn LaGuardia Airport - 83rd St','Manhattan Broadway Budget Hotel','The Renwick','Residence Inn New York The Bronx At Metro Center Atrium','Gatsby Hotel – an Ascend Hotel Collection Member','Courtyard by Marriott New York Manhattan/SoHo','EVEN Hotel Times Square South','LaGuardia Plaza Hotel','Hotel Cliff','Courtyard New York Manhattan/Times Square West',MichaelNY,'Fairfield Inn & Suites New York Manhattan/Downtown East','Park Hyatt New York','Asiatic Hotel by LaGuardia Airport','CAMBRiA hotel & suites New York - Chelsea','NobleDEN Hotel','The Paramount - A Times Square New York Hotel','The Wall Street Inn','AnYi Guesthouse LaGuardia','Flushing Ymca','Hotel Indigo BROOKLYN','Comfort Inn Manhattan Bridge','Garden Inn & Suites','The Towers of the Waldorf Astoria New York','Travel Inn Hotel','Pointe Plaza Hotel','Harlem YMCA','Broadway Plaza Hotel','Midtown West Vacation Rentals','The Plaza Hotel','Club Quarters World Trade Center','Dream Midtown','The Out NYC','Sheraton LaGuardia East Hotel','Courtyard by Marriott New York Manhattan/Times Square','Hilton Garden Inn New York/Central Park South-Midtown West','The Paul an Ascend Hotel Collection Member','Hilton Manhattan East','AnYi Guesthouse JFK','Hotel Metro','SIXTY Lower East Side','DoubleTree Suites by Hilton New York City - Times Square','NY Moore Hostel','Amsterdam Court Hotel','Comfort Inn JFK Airport','The One Boutique Hotel','Best Western Plus Newark Airport West','Best Western Premier Herald Square','Gabbs Apartment','GEM Hotel - Chelsea an Ascend Hotel Collection Member','Oyo Bed & Breakfast','New York Marriott Downtown','Stay Smart Times Square','Howard Johnson Hotel Newark Airport','Best Western Bayside Inn','Blue Moon Boutique Hotel','The Benjamin','Empire 120','Ramada Jamaica/Queens','JFK Bed & Breakfast Guesthouse','United Nations Apartment Hotel','Hipsters Cove GuestHouse','Quality Inn','Hampton Inn New York - 35th Street - Empire State Building','Hotel Pennsylvania','Fitzpatrick Manhattan Hotel','The SunBright','La Quinta Inn & Suites Brooklyn East','Avalon Hotel','The Ritz-Carlton New York Battery Park','Holiday Inn Express Staten Island West','Hotel Wales','Fairfield Inn & Suites by Marriott New York Brooklyn','Hyatt Herald Square New York','Riverside Tower Hotel','36 Hudson Hotel','Howard Johnson Flushing','Hilton Garden Inn Tribeca','Moblat Apartments 25-42','Pod 51','Holiday Inn Express Brooklyn','City Club Hotel','Distrikt Hotel New York City an Ascend Collection Hotel','Hilton Garden Inn New York/Midtown Park Ave','The Townhouse Inn of Chelsea','Vacayo NYC East Apartments','Central Park View Apartments','Hotel Chandler','Fort Place Bed & Breakfast','Room Mate Grace','Holiday Inn Express Maspeth','Flushing Hotel','The Court - A St Giles Premier Hotel','Hotel Vetiver','The Harbor House Bed & Breakfast',Morgans,'Sofia Inn','Sleep Inn JFK Airport Rockaway Blvd','Omni Berkshire Place','Martha Washington','Ace Hotel New York','Fairfield Inn & Suites by Marriott New York ManhattanChelsea','Best Western Plus Prospect Park Hotel','Hampton Inn Madison Square Garden Area Hotel','Cosmopolitan Hotel - Tribeca','Park Savoy Hotel','The Library Hotel','Baccarat Hotel and Residences New York','The Marlton Hotel','Days Inn Brooklyn','Hotel Luxe','The Standard East Village','Fitzpatrick Grand Central','West Side YMCA','Howard Johnson Manhattan Soho','Aloft Harlem','Vanderbilt YMCA','Trump International Hotel & Tower New York','Comfort Inn Brooklyn Cruise Terminal','Howard Johnson Inn Jamaica JFK Airport NYC','The Tuscany - A St Giles Boutique Hotel','The Lombardy','Novotel New York - Times Square','The Lowell','Hilton Times Square','Super 8 Long Island City LGA Hotel','Hampton Inn Times Square North','Archer Hotel New York','Sheraton Brooklyn New York Hotel','The Muse Hotel a Kimpton Hotel','The Algonquin Hotel Times Square Autograph Collection','Crowne Plaza JFK Airport New York City','Hotel 31','The Missing Lantern','Duane Street Hotel Tribeca','Hotel Edison','The Belvedere Hotel','Da Vinci Hotel','Hotel Sofitel New York','Pod 39','Fairfield Inn New York Long Island City/Manhattan View','Best Western Plus Seaport Inn Downtown','Holiday Inn New York City - Times Square','Sleep Inn Brooklyn','West 51 Street Apartment','Courtyard by Marriott Newark Liberty International Airport','Holiday Inn LaGuardia Airport','Carlton Hotel Autograph Collection','The Westin New York Grand Central','Allies Inn Bed and Breakfast','Murray Hill Apartments','Hampton Inn Manhattan-Seaport-Financial District','Country Inn & Suites By Carlson Newark Airport','Element New York Times Square West','Hotel Mela Times Square','New York Hilton Midtown','Hampton Inn Newark Airport','Hotel 309','Comfort Inn Staten Island','Wingate by Wyndham Manhattan Midtown','Union Hotel an Ascend Hotel Collection Member','Aloft New York Brooklyn Hotel','Market Rental Midtown East Apartments','Delz Bed & Breakfast','Red Lion Inn and Suites Brooklyn','Waldorf Astoria New York','Gramercy Park Hotel','Quality Inn Woodside','Four Points by Sheraton Long Island City Queensboro Bridge','One Bedroom Self-Catering Apartment - Little Italy','Vacayo Premium West Side Apartments','Comfort Inn Long Island City','Market Rentals Uptown','Holiday Inn Express Kennedy Airport','The Strand Hotel','SpringHill Suites by Marriott Newark Liberty International','The Evelyn (Formerly The Gershwin Hotel)','Shelburne NYC-an Affinia hotel','The Alfie - Corporate Apartment','Candlewood Suites New York City-Times Square','Lexington Inn - Brooklyn NY','Residence Inn Marriott New York Downtown Manhattan/WTC Area','citizenM New York Times Square','Playland Motel','Econo Lodge Times Square','Night Hotel Theater District','Fifty NYC-an Affinia hotel','Hilton Garden Inn New York / Staten Island','Estudio 129','The Milburn Hotel','Sugar Hill Harlem Inn','Casablanca Hotel','Hotel Boutique at Grand Central','The Manhattan at Times Square Hotel','Club Quarters opposite Rockefeller Center','HYH Hotel Flushing','City View Inn','Be Home','Renaissance Newark Airport Hotel','Cassa Hotel 45th Street','SpringHill Suites by Marriott New York Midtown Manhattan','San Carlos Hotel','Washington Jefferson Hotel','The Marcel at Gramercy','Trump Soho New York','The Jade Hotel','TRYP By Wyndham Times Square South','SpringHill Suites by Marriott New York LaGuardia Airport','Royal Park Hotel and Hostel','Holiday Inn Newark Airport','Sleep Inn JFK Airport',MySuites,'YOTEL New York at Times Square','1 Hotel Central Park','The Sohotel','Comfort Inn Brooklyn City Center','RIFF Chelsea','Green Point YMCA','Sleep Inn Coney Island','Comfort Inn Times Square West','Carvi Hotel New York','Days Inn Bronx Near Stadium','Hilton Garden Inn New York Long Island City','Studio Self Catering Apt Lower East Side','Super 8 Brooklyn / Park Slope Hotel','The Roosevelt Hotel New York City','The Knickerbocker Hotel','Mayfair New York','Nesva Hotel','Salisbury Hotel','Fairfield Inn by Marriott New York Manhattan/Times Square','Wyndham Garden Hotel Newark Airport','NYC Vacation Suites','Holiday Inn Manhattan-Financial District','World Center Hotel','Herrick Guest Suites 18th St Oasis Apartment','The Boro Hotel','Sleep Days Avenue H','The Mark','Blakely New York','New York Marriott East Side','Manhattan NYC-an Affinia hotel','Holiday Inn Express New York City Fifth Avenue','Washington Square Hotel','Vacation Rental in New York','Hotel 32 32','Hotel Beacon','Comfort Inn Chelsea','Sugar Hill Suites','Langham Place New York Fifth Avenue','La Quinta Inn Queens (New York City)','Howard Johnson Jamaica NY Near AirTrain JFK','DoubleTree by Hilton New York City - Chelsea','Wyndham New Yorker','The Nolitan',Room945,'Le Parker Meridien New York','The Duchamp - Corporate Apartment','Sheraton Tribeca New York Hotel','Sheraton New York Times Square Hotel','Hotel St. James','Comfort Inn Central Park West','Edge Hotel','Metro Studio Apartment in Kipsbay','The Premier Hotel New York','The Inn at Irving Place','The Parc Hotel','Airway Inn at LaGuardia','Holiday Inn Express Laguardia Airport','Hilton Garden Inn New York/Manhattan-Midtown East','Hilton Garden Inn New York/Manhattan-Chelsea','The High Line Hotel','The Maritime Hotel','Hotel 17','Dazzler Brooklyn','onefinestay - Upper West Side apartments','Hotel Indigo NYC Chelsea','Holiday Inn New York City - Wall Street','Hyatt Place Flushing/LaGuardia Airport','Marco LaGuardia Hotel by Lexington','Hyatt Union Square New York','Executive Hotel Le Soleil New York','Best Western Plus Arena Hotel','Hotel Q New York','Holiday Inn New York City-Midtown-57th Street','CITY ROOMS+ NYC SoHo','Hells Kitchen Apartment','Comfort Inn Sunset Park / Park Slope','Days Inn & Suites Ozone Park/JFK Airport','The Gregory','The St. Regis New York','Eventi Hotel a Kimpton Hotel','Holiday Inn NYC - Lower East Side','Aloft Manhattan Downtown - Financial District','Super Family Flat in Little Italy','Par Central Motor Inn','Topping Apartment','Sleep Days','Marrakech Hotel','Comfort Inn Times Square South Area','Surfside Motel','The Gotham Hotel','Residence Inn Newark Elizabeth/Liberty International Airport','Quality Inn Convention Center','The Ludlow Hotel','Galaxy Motel','Days Inn Long Island City','The Surrey','Ramada Staten Island','The Marmara Park Avenue','Mi Casa Tu Casa','JW Marriott Essex House New York','ink48 hotel a Kimpton Hotel','The Roger','Howard Johnson Bronx Near Stadium','W New York','Hampton Inn Brooklyn Downtown NY','Hotel Americano','SIXTY SoHo','Gild Hall A Thompson Hotel','The Peninsula New York','The Iroquois New York','Wyndham Garden Chinatown','Residence Inn New York Manhattan/Central Park','Windsor Hotel','The Roger Smith Hotel','Mayor Hotel','Hotel Newton','Andaz Wall Street - a concept by Hyatt','Paris Suites Hotel','Renaissance New York Hotel 57','Serenity at Home Guest House','East Village Hotel','Hampton Inn New York Chelsea','Bentley Hotel','Homewood Suites by Hilton NY Midtown Manhattan/Times Square','Morris Guest House','Central Park West Hostel','Sleep Inn Brooklyn Downtown','Ramada Plaza Newark Liberty International Airport','Radio City Apartments','Wawa Realty Manhattan 1','Extraordinary Flat in Chinatown','Ramada Long Island City','Red Roof Inn Queens','The Lex NYC','Condor Hotel','Best Western Gregory Hotel','Comfort Inn Midtown West','Club Quarters Grand Central','W New York - Union Square','Hilton New York Fashion District','Red Roof Inn Flushing New York - LaGuardia Airport','DoubleTree by Hilton New York City - Financial District','Hotel Belleclaire','Club Quarters Hotel Wall Street','Howard Johnson Inn Queens','ONE UN New York','Ramada Flushing Queens','The Kitano New York','East Village Suites','Radisson Martinique on Broadway','Hampton Inn New York - LaGuardia Airport','Apartment 804','Holiday Inn Express Manhattan Times Square South','Night Hotel Times Square','St Marks Hotel','Empire Hotel','The Langston - Corporate Apartment','Hotel Plaza Athenee','Holiday Inn Express New York - Manhattan West Side','The James New York','the Quin','Hilton Garden Inn New York/West 35th Street','Jets Motor Inn','Americana Inn','Lincoln Center Apartments','Extended Stay America New York City - LaGuardia Airport','Best Western Plus Brooklyn Bay Hotel','Crowne Plaza Times Square Manhattan','Hampton Inn Manhattan/Downtown-Financial District','Days Inn JFK Airport','Hotel Five44','Market Rental NYC Midtown West','Upper West Side Brownstone','Crowne Plaza Newark Airport','Courtyard by Marriott New York Manhattan/Upper East Side','Club Quarters Midtown - Times Square','Courtyard by Marriott New York JFK Airport','Four Points by Sheraton Midtown-Times Square','New York Inn','Conrad New York','Victorian Bed & Breakfast','The Jane Hotel','Holiday Inn New York JFK Airport Area','Midtown West Apartment','Dream Downtown','The Moderne','MySuites The Meat Packing Suites','Sumner Hotel','AnYi Guesthouse Flushing','Wyndham Garden - Manhattan Chelsea West','Two Bedroom Self Catering Apartment- Midtown West','The Bryant Park Hotel','Harlem Short Term Stay','Lefferts Manor Bed & Breakfast','Radisson Hotel JFK Airport','Safehouse Suites Manhattan','Hyatt Times Square New York','Herald Square Hotel','Hudson New York Central Park','Holiday Inn Staten Island','Superior New York Apartments','Comfort Inn & Suites LaGuardia Airport','Red Carpet Inn New York City','Herrick Guest Suites Chelsea Apartment','Bed & Breakfast Little Italy','Fairfield Inn & Suites Newark Liberty International Airport','DoubleTree by Hilton Hotel New York - Times Square South','Comfort Inn','The Hotel 91','DoubleTree by Hilton Metropolitan - New York City','Best Western Jamaica Inn','Park South Hotel','Riviera Motor Inn Brooklyn','Broadway Hotel and Hostel','TRYP by Wyndham New York Times Square','Best Western Queens Court Hotel','Hotel 373 Fifth Avenue','Four Seasons Hotel New York','The Ritz-Carlton New York Central Park','The Box House Hotel','International Students Residence','Adria Hotel And Conference Center','New World Hotel','Roxy Hotel Tribeca (formerly the Tribeca Grand Hotel)','Arlington Place Bed & Breakfast','The Strayhorn - Corporate Apartment','Neptune Hotel','Morningside Inn','Sunny Guesthouse','Hotel On Rivington','The Franklin Hotel','Chambers Hotel','Bklyn House','Courtyard Newark Elizabeth','Howard Johnson Express Inn Bronx','Warwick New York Hotel','Smyth A Thompson Hotel','Williamsburg Hostel','Hotel 41 at Times Square','Ramada Bronx','Viceroy New York','Extended Stay America Elizabeth - Newark Airport','Greenwich Village Apartment','Executive Class at MTS Hotel','Leon Hotel','Lefferts Gardens Residence Bed and Breakfast','Fairfield Inn by Marriott New York Manhattan/Fifth Avenue','Your NYC Vacation','MySuites - Gramercy Suites','Incentra Village House','The Central Park North','6 Columbus - a SIXTY Hotel','Dylan Hotel','Ameritania at Times Square','Courtyard by Marriott New York Manhattan / Chelsea','Hampton Inn Manhattan/United Nations','Moblat 5','Home2 Suites by Hilton NY Long Island City/Manhattan View','Gansevoort Park Avenue NYC','Courtyard New York LaGuardia Airport','Herrick Guest Suites Christopher Street Apartments','Holiday Inn Express New York City Times Square','The Carlyle A Rosewood Hotel','Ravel Hotel','The London NYC','The Paper Factory Hotel','Lotte New York Palace','Tribeca Blu Hotel','Wyndham Midtown 45 at New York City','Fairfield Inn New York Manhattan/Financial District','The Brooklyn A Hotel','Wyndham Garden Long Island City Manhattan View','Hotel Elysee','Westin New York at Times Square','The Marmara Manhattan','Fairfield Inn by Marriott New York LaGuardia Airport/Astoria','Best Western Plus Hospitality House','Comfort Inn Lower East Side','The International Cozy Inn','Hyatt Place New York Midtown South','The Jewel facing Rockefeller Center','West 57th Street by Hilton Club','414 Hotel','JFK Inn','Hotel 48LEX New York','Riviera Hotel','La Quinta Inn & Suites Brooklyn Downtown','Newark Liberty International Airport Marriott','Hotel Le Bleu','Renaissance New York Times Square Hotel','Hampton Inn Manhattan Soho','The NoMad Hotel','Lexington Inn JFK Airport','Midtown East 1BR Six DR27','Best Western Plaza Hotel','West Side Apartment','Moblat Apartments 30-68','The Chatwal a Luxury Collection Hotel New York City','Magnuson Convention Center Hotel','Brooklyn Motor Inn','Orchard Street Hotel','Hampton Inn JFK Airport','Sheridan Hotel','Fairfield Inn by Marriott LaGuardia Airport/Flushing','Super 8 Bronx','Q&A Residential Hotel','DoubleTree by Hilton Hotel Newark Airport','Chelsea Savoy Hotel','Residence Inn by Marriott New York Manhattan/Midtown East','Four Points by Sheraton Manhattan SoHo Village','Chelsea Inn','Four Points by Sheraton Manhattan - Chelsea','70 Park Avenue Hotel a Kimpton Hotel','Quality Inn Floral Park','Dumont NYC-an Affinia hotel','onefinestay - Brooklyn apartments','Andaz 5th Avenue - a concept by Hyatt','The New York EDITION','Opera House Hotel','The Lexington New York City Autograph Collection','Hampton Inn & Suites Staten Island','The Kimberly Hotel & Suites','Best Western Bowery Hanbee Hotel','Belnord Hotel','The Lucerne Hotel','Hilton Garden Inn Times Square','Loews Regency New York Hotel','Superior Times Square Apartments','La Quinta Inn and Suites JFK Airport','The Pina - Corporate Apartment','Sankofa Aban Bed and Breakfast','Gardens NYC-an Affinia hotel','Bellerose Inn','Best Western JFK Airport Hotel','Holiday Inn Express New York City- Wall Street','New York Marriott at the Brooklyn Bridge','Park 79 Hotel','The Towers at Lotte New York Palace','Holiday Inn Express - Madison Square Garden','Millenium Hilton','New York Marriott Marquis','Anchor Inn','Airport Hotel Inn & Suites - Newark Airport','Courtyard by Marriott New York Manhattan / Central Park','Off Soho Suites Hotel','NU Hotel Brooklyn','MOGA Unico','Refinery Hotel','Staybridge Suites Times Square','Grand Hyatt New York','NH New York Jolly Madison Towers','Residence Inn by Marriott New York Manhattan/Times Square','MySuites - SoHo Bowery Suites','Hilton Newark Airport','Chelsea Apartment','Eurostars Wall Street','Soho Grand Hotel','Rodeway Inn Bronx Zoo','WestHouse New York','The Pierre A Taj Hotel New York'}");
			sb.append('\n');
			sb.append("@attribute month numeric");
			sb.append('\n');
			sb.append("@attribute DayInMonth numeric");
			sb.append('\n');
			sb.append("@attribute DayInWeek numeric");
			sb.append('\n');
			sb.append("@attribute diffDays numeric");
			sb.append('\n');
			sb.append("@attribute price numeric");
			sb.append('\n');

			sb.append(
					"@attribute Company {'Home NYC','Hampton Inn','Comfort Inn','Mandarin Oriental New York','Hotel Bliss','W New York - Downtown','NYLO New York City','Fairfield Inn','InterContinental - New York Times Square',Hilton,'The Mansfield Hotel','Holiday Inn - Long Island City - Manhattan View','Row NYC','CITY ROOMS NYC CHELSEA','Park Central New York','Colonial House Inn','The Pearl New York','Hotel Le Jolie','The Shoreham Hotel','The Sherry Netherland','Skyline Hotel','Best Western Plus LaGuardia Airport Hotel Queens',Sheraton,'Courtyard by Marriott','Flatiron Hotel','Rodeway Inn Near JFK Airport','Bronx Guesthouse','Millennium Broadway Hotel - Times Square','Americas Best Value Inn','onefinestay - Chelsea apartments','Days Hotel Broadway','New York LaGuardia Airport Marriott','Jet Luxury at the Trump SoHo','Embassy Suites Newark Airport','Kings Hotel','Holiday Inn Manhattan 6th Ave - Chelsea',MiniStay,'Two-Bedroom Self-Catering Apartment - Lower East Side','Times Square Apartment','Wellington Hotel','Sanctuary NYC Retreats','Knights Inn JFK Airport NYC','McCarren Hotel & Pool','CAMBRiA hotel & suites New York Times Square','The Local','The Michelangelo Hotel','Park Lane Hotel','Country Inn & Suites By Carlson New York City in Queens NY','Howard Johnson','Hotel BPM - Brooklyn New York','Bowery Grand Hotel','Upper Yorkville Apartments','onefinestay - Downtown West apartments','Wyndham Garden Brooklyn Sunset Park','Excelsior Hotel','Seton Hotel','East 55th Apartments','W New York Times Square','NOMO SOHO','Hotel Giraffe','Henry Norman Hotel','The Solita Soho Hotel an Ascend Hotel Collection Member','Hotel Hugo',Royalton,'Hotel Mulberry','Herrick Guest Suites 14th Street Apartment','Z NYC Hotel','Super 8','The Manhattan Club','Sleep Inn','Oakwood at The Nash','Hotel Indigo Lower East Side New York','The Time Hotel','Sanctuary Hotel New York','Clarion Hotel Park Avenue','The Bowery House','Manhattan Broadway Budget Hotel','The Renwick','Residence Inn New York The Bronx At Metro Center Atrium','Gatsby Hotel – an Ascend Hotel Collection Member','EVEN Hotel Times Square South','LaGuardia Plaza Hotel','Hotel Cliff','Courtyard New York Manhattan/Times Square West',MichaelNY,'Park Hyatt New York','Asiatic Hotel by LaGuardia Airport','CAMBRiA hotel & suites New York - Chelsea','NobleDEN Hotel','The Paramount - A Times Square New York Hotel','The Wall Street Inn','AnYi Guesthouse LaGuardia','Flushing Ymca','Hotel Indigo BROOKLYN','Garden Inn & Suites','The Towers of the Waldorf Astoria New York','Travel Inn Hotel','Pointe Plaza Hotel','Harlem YMCA','Broadway Plaza Hotel','Midtown West Vacation Rentals','The Plaza Hotel','Club Quarters World Trade Center','Dream Midtown','The Out NYC','The Paul an Ascend Hotel Collection Member','AnYi Guesthouse JFK','Hotel Metro','SIXTY Lower East Side','NY Moore Hostel','Amsterdam Court Hotel','The One Boutique Hotel','Best Western Plus Newark Airport West','Best Western Premier Herald Square','Gabbs Apartment','GEM Hotel - Chelsea an Ascend Hotel Collection Member','Oyo Bed & Breakfast','New York Marriott Downtown','Stay Smart Times Square','Best Western Bayside Inn','Blue Moon Boutique Hotel','The Benjamin','Empire 120','Ramada Jamaica/Queens','JFK Bed & Breakfast Guesthouse','United Nations Apartment Hotel','Hipsters Cove GuestHouse','Quality Inn','Hotel Pennsylvania','Fitzpatrick Manhattan Hotel','The SunBright','La Quinta Inn','Avalon Hotel','The Ritz-Carlton New York Battery Park','Holiday Inn Express Staten Island West','Hotel Wales','Hyatt Herald Square New York','Riverside Tower Hotel','36 Hudson Hotel','Moblat Apartments 25-42','Pod 51','Holiday Inn Express Brooklyn','City Club Hotel','Distrikt Hotel New York City an Ascend Collection Hotel','The Townhouse Inn of Chelsea','Vacayo NYC East Apartments','Central Park View Apartments','Hotel Chandler','Fort Place Bed & Breakfast','Room Mate Grace','Holiday Inn Express Maspeth','Flushing Hotel','The Court - A St Giles Premier Hotel','Hotel Vetiver','The Harbor House Bed & Breakfast',Morgans,'Sofia Inn','Omni Berkshire Place','Martha Washington','Ace Hotel New York','Best Western Plus Prospect Park Hotel','Cosmopolitan Hotel - Tribeca','Park Savoy Hotel','The Library Hotel','Baccarat Hotel and Residences New York','The Marlton Hotel','Days Inn','Hotel Luxe','The Standard East Village','Fitzpatrick Grand Central','West Side YMCA','Aloft Harlem','Vanderbilt YMCA','Trump International Hotel & Tower New York','The Tuscany - A St Giles Boutique Hotel','The Lombardy','Novotel New York - Times Square','The Lowell','Archer Hotel New York','The Muse Hotel a Kimpton Hotel','The Algonquin Hotel Times Square Autograph Collection','Crowne Plaza JFK Airport New York City','Hotel 31','The Missing Lantern','Duane Street Hotel Tribeca','Hotel Edison','The Belvedere Hotel','Da Vinci Hotel','Hotel Sofitel New York','Pod 39','Best Western Plus Seaport Inn Downtown','Holiday Inn New York City - Times Square','West 51 Street Apartment','Holiday Inn LaGuardia Airport','Carlton Hotel Autograph Collection',Westin,'Allies Inn Bed and Breakfast','Murray Hill Apartments','Country Inn & Suites By Carlson Newark Airport','Element New York Times Square West','Hotel Mela Times Square','Hotel 309','Wingate by Wyndham Manhattan Midtown','Union Hotel an Ascend Hotel Collection Member','Aloft New York Brooklyn Hotel','Market Rental Midtown East Apartments','Delz Bed & Breakfast','Red Lion Inn and Suites Brooklyn','Waldorf Astoria New York','Gramercy Park Hotel','Quality Inn Woodside','One Bedroom Self-Catering Apartment - Little Italy','Vacayo Premium West Side Apartments','Market Rentals Uptown','Holiday Inn Express Kennedy Airport','The Strand Hotel',SpringHill,'The Evelyn (Formerly The Gershwin Hotel)','Shelburne NYC-an Affinia hotel','The Alfie - Corporate Apartment','Candlewood Suites New York City-Times Square','Lexington Inn - Brooklyn NY','Residence Inn Marriott New York Downtown Manhattan/WTC Area','citizenM New York Times Square','Playland Motel','Econo Lodge Times Square','Night Hotel Theater District','Fifty NYC-an Affinia hotel','Estudio 129','The Milburn Hotel','Sugar Hill Harlem Inn','Casablanca Hotel','Hotel Boutique at Grand Central','The Manhattan at Times Square Hotel','Club Quarters opposite Rockefeller Center','HYH Hotel Flushing','City View Inn','Be Home','Renaissance Newark Airport Hotel','Cassa Hotel 45th Street','San Carlos Hotel','Washington Jefferson Hotel','The Marcel at Gramercy','Trump Soho New York','The Jade Hotel','TRYP By Wyndham Times Square South','Royal Park Hotel and Hostel','Holiday Inn Newark Airport',MySuites,'YOTEL New York at Times Square','1 Hotel Central Park','The Sohotel','RIFF Chelsea','Green Point YMCA','Carvi Hotel New York','Studio Self Catering Apt Lower East Side','The Roosevelt Hotel New York City','The Knickerbocker Hotel','Mayfair New York','Nesva Hotel','Salisbury Hotel','Wyndham Garden Hotel Newark Airport','NYC Vacation Suites','Holiday Inn Manhattan-Financial District','World Center Hotel','Herrick Guest Suites 18th St Oasis Apartment','The Boro Hotel','Sleep Days Avenue H','The Mark','Blakely New York','New York Marriott East Side','Manhattan NYC-an Affinia hotel','Holiday Inn Express New York City Fifth Avenue','Washington Square Hotel','Vacation Rental in New York','Hotel 32 32','Hotel Beacon','Sugar Hill Suites','Langham Place New York Fifth Avenue','Wyndham New Yorker','The Nolitan',Room945,'Le Parker Meridien New York','The Duchamp - Corporate Apartment','Hotel St. James','Edge Hotel','Metro Studio Apartment in Kipsbay','The Premier Hotel New York','The Inn at Irving Place','The Parc Hotel','Airway Inn at LaGuardia','Holiday Inn Express Laguardia Airport','The High Line Hotel','The Maritime Hotel','Hotel 17','Dazzler Brooklyn','onefinestay - Upper West Side apartments','Hotel Indigo NYC Chelsea','Holiday Inn New York City - Wall Street','Hyatt Place Flushing/LaGuardia Airport','Marco LaGuardia Hotel by Lexington','Hyatt Union Square New York','Executive Hotel Le Soleil New York','Best Western Plus Arena Hotel','Hotel Q New York','Holiday Inn New York City-Midtown-57th Street','CITY ROOMS+ NYC SoHo','Hells Kitchen Apartment','The Gregory','The St. Regis New York','Eventi Hotel a Kimpton Hotel','Holiday Inn NYC - Lower East Side','Aloft Manhattan Downtown - Financial District','Super Family Flat in Little Italy','Par Central Motor Inn','Topping Apartment','Sleep Days','Marrakech Hotel','Surfside Motel','The Gotham Hotel','Residence Inn Newark Elizabeth/Liberty International Airport','Quality Inn Convention Center','The Ludlow Hotel','Galaxy Motel','The Surrey','Ramada Staten Island','The Marmara Park Avenue','Mi Casa Tu Casa','JW Marriott Essex House New York','ink48 hotel a Kimpton Hotel','The Roger','W New York','Hotel Americano','SIXTY SoHo','Gild Hall A Thompson Hotel','The Peninsula New York','The Iroquois New York','Wyndham Garden Chinatown','Residence Inn New York Manhattan/Central Park','Windsor Hotel','The Roger Smith Hotel','Mayor Hotel','Hotel Newton',Andaz,'Paris Suites Hotel','Renaissance New York Hotel 57','Serenity at Home Guest House','East Village Hotel','Bentley Hotel','Morris Guest House','Central Park West Hostel','Ramada Plaza Newark Liberty International Airport','Radio City Apartments','Wawa Realty Manhattan 1','Extraordinary Flat in Chinatown','Ramada Long Island City','Red Roof Inn Queens','The Lex NYC','Condor Hotel','Best Western Gregory Hotel','Club Quarters Grand Central','W New York - Union Square','Red Roof Inn Flushing New York - LaGuardia Airport','Hotel Belleclaire','Club Quarters Hotel Wall Street','ONE UN New York','Ramada Flushing Queens','The Kitano New York','East Village Suites','Radisson Martinique on Broadway','Apartment 804','Holiday Inn Express Manhattan Times Square South','Night Hotel Times Square','St Marks Hotel','Empire Hotel','The Langston - Corporate Apartment','Hotel Plaza Athenee','Holiday Inn Express New York - Manhattan West Side','The James New York','the Quin','Jets Motor Inn','Americana Inn','Lincoln Center Apartments','Extended Stay','Best Western Plus Brooklyn Bay Hotel','Crowne Plaza Times Square Manhattan','Hotel Five44','Market Rental NYC Midtown West','Upper West Side Brownstone','Crowne Plaza Newark Airport','Club Quarters Midtown - Times Square','New York Inn','Conrad New York','Victorian Bed & Breakfast','The Jane Hotel','Holiday Inn New York JFK Airport Area','Midtown West Apartment','Dream Downtown','The Moderne','MySuites The Meat Packing Suites','Sumner Hotel','AnYi Guesthouse Flushing','Wyndham Garden - Manhattan Chelsea West','Two Bedroom Self Catering Apartment- Midtown West','The Bryant Park Hotel','Harlem Short Term Stay','Lefferts Manor Bed & Breakfast','Radisson Hotel JFK Airport','Safehouse Suites Manhattan','Hyatt Times Square New York','Herald Square Hotel','Hudson New York Central Park','Holiday Inn Staten Island','Superior New York Apartments','Red Carpet Inn New York City','Herrick Guest Suites Chelsea Apartment','Bed & Breakfast Little Italy','The Hotel 91','Best Western Jamaica Inn','Park South Hotel','Riviera Motor Inn Brooklyn','Broadway Hotel and Hostel','TRYP by Wyndham New York Times Square','Best Western Queens Court Hotel','Hotel 373 Fifth Avenue','Four Seasons Hotel New York','The Ritz-Carlton New York Central Park','The Box House Hotel','International Students Residence','Adria Hotel And Conference Center','New World Hotel','Roxy Hotel Tribeca (formerly the Tribeca Grand Hotel)','Arlington Place Bed & Breakfast','The Strayhorn - Corporate Apartment','Neptune Hotel','Morningside Inn','Sunny Guesthouse','Hotel On Rivington','The Franklin Hotel','Chambers Hotel','Bklyn House','Courtyard Newark Elizabeth','Warwick New York Hotel','Smyth A Thompson Hotel','Williamsburg Hostel','Hotel 41 at Times Square','Ramada Bronx','Viceroy New York','Greenwich Village Apartment','Executive Class at MTS Hotel','Leon Hotel','Lefferts Gardens Residence Bed and Breakfast','Your NYC Vacation','MySuites - Gramercy Suites','Incentra Village House','The Central Park North','6 Columbus - a SIXTY Hotel','Dylan Hotel','Ameritania at Times Square','Moblat 5','Gansevoort Park Avenue NYC','Courtyard New York LaGuardia Airport','Herrick Guest Suites Christopher Street Apartments','Holiday Inn Express New York City Times Square','The Carlyle A Rosewood Hotel','Ravel Hotel','The London NYC','The Paper Factory Hotel','Lotte New York Palace','Tribeca Blu Hotel','Wyndham Midtown 45 at New York City','The Brooklyn A Hotel','Wyndham Garden Long Island','Hotel Elysee','The Marmara Manhattan','Best Western Plus Hospitality House','The International Cozy Inn','Hyatt Place New York Midtown South','The Jewel facing Rockefeller Center','414 Hotel','JFK Inn','Hotel 48LEX New York','Riviera Hotel','Newark Liberty International Airport Marriott','Hotel Le Bleu','Renaissance New York Times Square Hotel','The NoMad Hotel','Lexington Inn JFK Airport','Midtown East 1BR Six DR27','Best Western Plaza Hotel','West Side Apartment','Moblat Apartments 30-68','The Chatwal a Luxury Collection Hotel New York City','Magnuson Convention Center Hotel','Brooklyn Motor Inn','Orchard Street Hotel','Sheridan Hotel','Q&A Residential Hotel','Chelsea Savoy Hotel','Residence Inn by Marriott New York Manhattan/Midtown East','Chelsea Inn','70 Park Avenue Hotel a Kimpton Hotel','Quality Inn Floral Park','Dumont NYC-an Affinia hotel','onefinestay - Brooklyn apartments','The New York EDITION','Opera House Hotel','The Lexington New York City Autograph Collection','The Kimberly Hotel & Suites','Best Western Bowery Hanbee Hotel','Belnord Hotel','The Lucerne Hotel','Loews Regency New York Hotel','Superior Times Square Apartments','The Pina - Corporate Apartment','Sankofa Aban Bed and Breakfast','Gardens NYC-an Affinia hotel','Bellerose Inn','Best Western JFK Airport Hotel','Holiday Inn Express New York City- Wall Street','New York Marriott at the Brooklyn Bridge','Park 79 Hotel','The Towers at Lotte New York Palace','Holiday Inn Express - Madison Square Garden','New York Marriott Marquis','Anchor Inn','Airport Hotel Inn & Suites - Newark Airport','Off Soho Suites Hotel','NU Hotel Brooklyn','MOGA Unico','Refinery Hotel','Staybridge Suites Times Square','Grand Hyatt New York','NH New York Jolly Madison Towers','Residence Inn by Marriott New York Manhattan/Times Square','MySuites - SoHo Bowery Suites','Chelsea Apartment','Eurostars Wall Street','Soho Grand Hotel','Rodeway Inn Bronx Zoo','WestHouse New York','The Pierre A Taj Hotel New York'}");
			sb.append('\n');
			sb.append("@attribute Description {A,B,C,D}");
			sb.append('\n');

			sb.append('\n');
			sb.append("@data");
			sb.append('\n');
			pw.write(sb.toString());
			for (hotelinvitation invite : currHotel) {
				if (!getDescriptoin(invite.descriptionPartOne).equals("E")) {
					sb = new StringBuilder();
					sb.append('"' + invite.hotelName.replaceAll("\"", "") + "\",");
					sb.append(invite.month + ",");
					sb.append(invite.DayInWeek + ",");
					sb.append(invite.dayInMonth + ",");
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
					sb.append('"' + invite.companyName.replaceAll("\"", "") + "\",");
					sb.append('"' + getDescriptoin(invite.descriptionPartOne) + '"');
					sb.append('\n');
					pw.write(sb.toString());
					System.out.println("doneH");
				}
			}
			// pw.write(sb.toString());
			System.out.println("done!!!!");
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return true;
	}

	// TODO fix here
	static boolean writeCSVFileToHotel(List<hotelinvitation> currHotel, String folderName, boolean isCity,
			String fileName) {
		PrintWriter pw = null;
		try {
			String url = "";
			if (!fileName.isEmpty()) {
				url = "C://Users/Ofra/desktop/èì åâì/hotels research/wekaPredictions2";
				url = MessageFormat.format("C://Users/Ofra/desktop/èì åâì/hotels research/{0}/{1}.csv", folderName,
						fileName);
			} else if (!isCity) {
				url = "C:\\Users\\Ofra\\Desktop\\èì åâì\\hotels research\\" + folderName + "\\"
						+ currHotel.get(0).hotelName.replace("\"", "").replace("/", " ").replace("\\", " ") + ".csv";
			} else {
				url = "C:\\Users\\Ofra\\Desktop\\èì åâì\\hotels research\\" + folderName + "\\"
						+ currHotel.get(0).City.replace("\"", "").replace("/", " ").replace("\\", " ") + ".csv";
			}
			File file = new File(url);
			if (!file.getParentFile().exists()) {
				file.mkdir();
			}
			pw = new PrintWriter(file);
			StringBuilder sb = new StringBuilder();
			for (hotelinvitation invite : currHotel) {
				if (!getDescriptoin(invite.descriptionPartOne).equals("E")) {
					sb.append(invite.hotelName + ",");
					sb.append(invite.month + ",");
					sb.append(invite.dayInMonth + ",");
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
					sb.append(invite.companyName + ",");
					sb.append('"' + getDescriptoin(invite.descriptionPartOne) + '"');
					sb.append('\n');

					System.out.println("doneH");
				}
			}
			pw.write(sb.toString());
			System.out.println("done!!!!");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			pw.flush();
			pw.close();
		}
		return true;
	}

	static void writeRandomResult(List<hotelinvitation> minimum,
			HashMap<String, HashMap<Integer, HashMap<Integer, List<hotelinvitation>>>> dictionaryInvitations) {
		String url = "C:\\Users\\Ofra\\Desktop\\èì åâì\\hotels research\\randomResult\\randomAll.csv";
		PrintWriter pw;
		try {
			pw = new PrintWriter(new File(url));
			StringBuilder sb = new StringBuilder();
			sb.append("hotelName,");
			sb.append("month,");
			sb.append("DayInWeek,");
			sb.append("DayInMonth,");
			sb.append("checkIn, ");
			sb.append("diffDays,");
			sb.append("Company,");
			sb.append("Description,");
			sb.append("descriptionPrice,");
			sb.append("Random,");
			sb.append("random price,");
			sb.append("diffPrice,");
			sb.append("diffPricePercent,");
			sb.append('\n');
			pw.write(sb.toString());
			int counter = 0;
			double sumPriceDiff = 0;
			String alphabet = "ABCD";
			Random rand = new Random();
			int withoutECounter = 0;
			for (hotelinvitation invite : minimum) {
				if (!getDescriptoin(invite.descriptionPartOne).equals("E")) {

					withoutECounter++;
					sb = new StringBuilder();
					sb = new StringBuilder();
					sb.append('"' + invite.hotelName + "\",");
					sb.append(invite.month + ",");
					sb.append(invite.DayInWeek + ",");
					sb.append(invite.dayInMonth + ",");
					sb.append(invite.cheakIn.toString() + ",");
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
					sb.append('"' + invite.companyName + "\",");
					String desc = getDescriptoin(invite.descriptionPartOne);
					char randDesc = alphabet.charAt(rand.nextInt(alphabet.length()));
					sb.append('"' + desc + "\",");
					sb.append(price + ",");
					sb.append(randDesc + ",");
					double randPrice = 0;
					List<hotelinvitation> invitesFromSameType;
					try {
						invitesFromSameType = dictionaryInvitations.get("\"" + invite.hotelName + "\"")
								.get(invite.month).get(invite.dayInMonth);
					} catch (Exception e) {
						invitesFromSameType = dictionaryInvitations.get(invite.hotelName).get(invite.month)
								.get(invite.dayInMonth);
					}
					for (hotelinvitation currInvinte : invitesFromSameType) {
						if (getDescriptoin(currInvinte.descriptionPartOne).indexOf(randDesc) != -1) {
							randPrice = currInvinte.totalPriceForCombine == -1 ? currInvinte.originalPrice
									: currInvinte.totalPriceForCombine;
						}
					}
					sb.append(randPrice + ",");
					sb.append(price - randPrice + ",");
					sb.append(randPrice / price + ",");
					sb.append('\n');
					sumPriceDiff += randPrice / price;
					if (desc.indexOf(randDesc) != -1) {
						counter++;
					}
					System.out.println("doneH");
					pw.write(sb.toString());
				}
			}
			sb = new StringBuilder();
			sb.append("îñôø ðëåðåú,");
			sb.append(counter + ",");
			sb.append("îñôø 2 äæîðåú,");
			sb.append(minimum.size() + ",");
			sb.append("àçåæ ðëåðåú,");
			sb.append((double) counter / (double) withoutECounter + ",");
			sb.append("äôøùé ðëåðåú,");
			sb.append(sumPriceDiff / withoutECounter);
			pw.write(sb.toString());
			System.out.println("done!!!!");
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	static void getAllFilteredData() throws Exception {
		File file = new File("C:\\Users\\Ofra\\Desktop\\èì åâì\\hotels research\\json\\filteredData.json");
		byte[] encoded = Files.readAllBytes(Paths.get(file.getPath()));
		String source = new String(encoded);
		Type t = new TypeToken<HashMap<String, HashMap<Integer, HashMap<Integer, List<hotelinvitation>>>>>() {
		}.getType();
		Gson gson = new Gson();
		dictionaryInvitations = gson.fromJson(source, t);
	}

	static void writeAllData() throws Exception {
		Gson gson = new Gson();

		// JSONObject jsonObject2 = new JSONObject(dictionaryInvitations);
		File jsonFile = new File("C:\\Users\\Ofra\\Desktop\\èì åâì\\hotels research\\json\\filteredData.json");
		FileWriter fileWriter = new FileWriter(jsonFile);
		fileWriter.write(gson.toJson(dictionaryInvitations));// jsonObject2.toString(2)
		fileWriter.flush();
		fileWriter.close();
	}

	static void readFilesAndSearchPricingInDictionary(
			HashMap<String, HashMap<Integer, HashMap<Integer, List<hotelinvitation>>>> dictionaryInvitations) {
		String pathToRead = "C:\\Users\\Ofra\\Desktop\\èì åâì\\hotels research\\þþwekaPredictions2\\predictedResults.csv";// MessageFormat.format("{0}/{1}",
																															// EXCEL_SOURCE_FOLDER_PREDICTION,
																															// FILE_NAME_PREDICTION);
		try (BufferedReader br = new BufferedReader(new FileReader(pathToRead))) {
			List<CustomInvite> inputList = br.lines().skip(1).map(mapCustomItem).collect(Collectors.toList());

			String urlToWrite = "C:\\Users\\Ofra\\Desktop\\èì åâì\\hotels research\\þþwekaPredictions2\\fullResults.csv";
			PrintWriter pw = new PrintWriter(new File(urlToWrite));
			StringBuilder sb = new StringBuilder();

			sb.append("hotelName,");
			sb.append("month,");
			sb.append("DayInWeek,");
			sb.append("DayInMonth,");
			sb.append("diffDays, ");
			sb.append("price,");
			sb.append("Company,");
			sb.append("prediction margin,");
			sb.append("predicted Description,");
			sb.append("Description,");
			sb.append("isCorrect,");
			sb.append("realPrice,");
			sb.append("predictionPrice,");
			sb.append("predictionPrice,");
			sb.append("difPrice,");
			sb.append("difPricePrecent");
			sb.append('\n');
			pw.write(sb.toString());
			double predictedPrice = 0;
			for (CustomInvite customInvite : inputList) {
				sb = new StringBuilder();

				for (hotelinvitation invite : dictionaryInvitations
						.get(("\"" + customInvite.hotelName + "\"").replaceAll("'", "")).get(customInvite.month)
						.get(customInvite.dayInMonth)) {
					if (getDescriptoin(invite.descriptionPartOne).equals(customInvite.predictedDescription)) {
						predictedPrice = invite.totalPriceForCombine == -1 ? invite.originalPrice
								: invite.totalPriceForCombine;
					}
				}
				sb.append(customInvite.hotelName + ",").append(customInvite.month + ",")
						.append(customInvite.dayInWeek + ",").append(customInvite.dayInMonth + ",")
						.append(customInvite.diffDays + ",").append(customInvite.price + ",")
						.append(customInvite.company + ",").append(customInvite.predictedMargin + ",")
						.append(customInvite.predictedDescription + ",").append(customInvite.realDescription + ",")
						.append(customInvite.isCorrect + ",").append(customInvite.price + ",")
						.append(predictedPrice + ",").append(-customInvite.price + predictedPrice + ",")
						.append(predictedPrice / customInvite.price + "\n");
				pw.write(sb.toString());
			}
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Function<String, CustomInvite> mapCustomItem = (line) -> {
		String[] invite = line.split(",");

		CustomInvite newInvite = new CustomInvite();
		newInvite.hotelName = invite[0];
		newInvite.month = Integer.parseInt(invite[1]);
		newInvite.dayInMonth = Integer.parseInt(invite[3]);
		newInvite.dayInWeek = Integer.parseInt(invite[2]);
		// TODO - import day in month
		// newInvite.dayInMonth = 7;
		newInvite.diffDays = Integer.parseInt(invite[4]);
		newInvite.price = Integer.parseInt(invite[5]);
		newInvite.company = invite[6];
		newInvite.predictedMargin = Double.parseDouble(invite[7]);
		newInvite.predictedDescription = invite[8];
		newInvite.realDescription = invite[9];
		newInvite.isCorrect = Boolean.parseBoolean(invite[10]);
		return newInvite;
	};

	public static class CustomInvite {
		public String hotelName;
		public int month;
		public int dayInWeek;
		public int dayInMonth;
		public int diffDays;
		public double price;
		public String company;
		public double predictedMargin;
		public String predictedDescription;
		public String realDescription;
		public boolean isCorrect;

		public CustomInvite() {
		}
	}

	private static Function<String, hotelinvitation> mapToItem = (line) -> {
		String[] invite = line.split(",");// a CSV has comma separated lines
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat formatterSlash = new SimpleDateFormat("dd/MM/yyyy");

		hotelinvitation newInvite = new hotelinvitation();

		try {
			newInvite.runDate = convertDate(invite[1], formatter, formatterSlash);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		newInvite.providerId = tryParseInt(invite[2]) ? Integer.parseInt(invite[2]) : -1;
		try {
			newInvite.cheakIn = convertDate(invite[3], formatter, formatterSlash);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			newInvite.cheakOut = convertDate(invite[4], formatter, formatterSlash);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long daysBetween = newInvite.cheakIn.getTime() - newInvite.runDate.getTime();
		newInvite.daysDiff = daysBetween / (24 * 60 * 60 * 1000);
		Calendar c = Calendar.getInstance();
		newInvite.month = newInvite.cheakIn.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue();// c.get(Calendar.MONTH);
																														// //newInvite.cheakIn.getMonth();

		c.setTime(newInvite.cheakIn);
		newInvite.DayInWeek = c.get(Calendar.DAY_OF_WEEK); // newInvite.cheakIn.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth()
		newInvite.dayInMonth = newInvite.cheakIn.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
				.getDayOfMonth();
		newInvite.days = tryParseInt(invite[5]) ? Integer.parseInt(invite[5]) : -1;
		newInvite.descriptionDays = invite[6];
		newInvite.originalPrice = tryParseDouble(invite[7]) ? Double.parseDouble(invite[7]) : -1;
		newInvite.roomType = invite[8];
		newInvite.codeRate = tryParseInt(invite[9]) ? Integer.parseInt(invite[9]) : -1;
		newInvite.availibleRooms = tryParseInt(invite[10]) ? Integer.parseInt(invite[10]) : -1;
		newInvite.boardType = tryParseInt(invite[12]) ? Integer.parseInt(invite[12]) : -1;
		newInvite.descriptionPartOne = invite[13];
		newInvite.pricePartOne = tryParseDouble(invite[14]) ? Double.parseDouble(invite[14]) : -1;
		newInvite.typePartOne = invite[15];
		newInvite.codeRatePartOne = invite[16];
		newInvite.avalibleRoomsPartOne = tryParseInt(invite[17]) ? Integer.parseInt(invite[17]) : -1;
		newInvite.boardTypePartOne = tryParseInt(invite[19]) ? Integer.parseInt(invite[19]) : -1;
		newInvite.descriptionPartTwo = invite[20];
		newInvite.pricePartTwo = tryParseDouble(invite[21]) ? Double.parseDouble(invite[21]) : -1;
		newInvite.typePartTwo = invite[22];
		newInvite.codeRatePartTwo = invite[23];
		newInvite.avalibleRoomsPartTwo = tryParseInt(invite[24]) ? Integer.parseInt(invite[24]) : -1;
		newInvite.boardTypePartTwo = tryParseDouble(invite[26]) ? Double.parseDouble(invite[26]) : -1;
		newInvite.totalPriceForCombine = tryParseDouble(invite[27]) ? Double.parseDouble(invite[27]) : -1;
		newInvite.codeHotel = tryParseInt(invite[28]) ? Integer.parseInt(invite[28]) : -1;
		newInvite.hotelName = invite[29];
		newInvite.starsRate = tryParseDouble(invite[30]) ? Double.parseDouble(invite[30]) : -1;
		newInvite.City = invite[31];
		newInvite.lat = invite[32];
		newInvite.longg = invite[33];
		newInvite.providerCode = invite[34];
		newInvite.decriptionBrandProvider = invite[35];
		newInvite.companyName = getHotelCompany(newInvite.hotelName);
		return newInvite;
		// more initialization goes here
	};
}
