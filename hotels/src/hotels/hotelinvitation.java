package hotels;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.lang.reflect.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;

public class hotelinvitation {
	public int Id;
	public Date runDate;
	public int providerId;
	public Date cheakIn;
	public Date cheakOut;
	public int days;
	public String descriptionDays;
	public double originalPrice;
	public String roomType;
	public int codeRate;
	public int availibleRooms;
	public Boolean idRefundable;
	public int boardType;
	public String descriptionPartOne;
	public double pricePartOne;
	public String typePartOne;
	public String codeRatePartOne;
	public int avalibleRoomsPartOne;
	public Boolean idRefundablePartOne;
	public int boardTypePartOne;
	public String descriptionPartTwo;
	public double pricePartTwo;
	public String typePartTwo;
	public String codeRatePartTwo;
	public int avalibleRoomsPartTwo;
	public Boolean idRefundablePartTwo;
	public double boardTypePartTwo;
	public double totalPriceForCombine;
	public int codeHotel;
	public String hotelName;
	public double starsRate;
	public String hotelPicture;
	public String lat;
	public String longg;
	public String providerCode;
	public String decriptionBrandProvider;
	public int month;
	public int DayInWeek;
	public int daysDiff;
	public String companyName;
	public int dayInMonth;
	public String City;
	public String checkInFull;

	public String getHotelName() {
		return this.hotelName;
	}

	public Date getCheckIn() {
		return this.cheakIn;
	}

	public static Map<String, Map<java.sql.Date, List<hotelinvitation>>> collect(
			Collector<hotelinvitation, ?, Map<String, Map<Date, List<hotelinvitation>>>> groupingBy) {

		// TODO Auto-generated method stub
		return null;
	}

	public String getAllFields() {
		String fieldsNames = "";
		List<Field> fields = Arrays.asList(getClass().getDeclaredFields());
		for (Field field : fields) {
			fieldsNames = fieldsNames.concat(field.getName() + ",");
		}
		// fields.parallelStream().forEach(field -> fieldsNames =
		// fieldsNames.concat(field.getName() + ","));
		return fieldsNames.substring(0, fieldsNames.length() - 1);

	}

	public String getAllFieldsValues(hotelinvitation hotelinvitation) {
		String fieldsValues = "";
		List<Field> fields = Arrays.asList(getClass().getDeclaredFields());
		try {
			for (Field field : fields) {
				if (field.getType().isInstance(new Date())) {
					LocalDate d = ((Date) field.get(hotelinvitation)).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					fieldsValues = fieldsValues
							.concat(d.getDayOfMonth() + "-" + d.getMonthValue() + "-" +
									d.getYear())+ ",";
				} else {
					fieldsValues = fieldsValues.concat(field.get(hotelinvitation) + ",");
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return fieldsValues.substring(0, fieldsValues.length() - 1);
	}
}
