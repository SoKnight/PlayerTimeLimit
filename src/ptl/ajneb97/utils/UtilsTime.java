package ptl.ajneb97.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ptl.ajneb97.configs.MainConfigManager;
import ptl.ajneb97.configs.TimeAccuracy;
import ptl.ajneb97.managers.MensajesManager;

public class UtilsTime {

	public static String getTime(long seconds, MensajesManager msgManager) {
		if(seconds == 0)
			return msgManager.getTimeInfinite();

		long days = seconds / 86400;
		seconds %= 86400;

		long hours = seconds / 3600;
		seconds %= 3600;

		long minutes = seconds / 60;
		seconds %= 60;

		List<String> timeParts = new ArrayList<>();
		TimeAccuracy accuracy = msgManager.getPlugin().getConfigsManager().getMainConfigManager().getTimeAccuracy();

		days = accuracy.roundAmount(TimeAccuracy.DAYS, days);
		hours = accuracy.roundAmount(TimeAccuracy.HOURS, hours);
		minutes = accuracy.roundAmount(TimeAccuracy.MINUTES, minutes);
		seconds = accuracy.roundAmount(TimeAccuracy.SECONDS, seconds);

		if(days > 0)
			timeParts.add(msgManager.getTimeFormatDays().replace("%amount%", String.valueOf(days)));

		if(hours > 0)
			timeParts.add(msgManager.getTimeFormatHours().replace("%amount%", String.valueOf(hours)));

		if(minutes > 0)
			timeParts.add(msgManager.getTimeFormatMinutes().replace("%amount%", String.valueOf(minutes)));

		if(seconds > 0)
			timeParts.add(msgManager.getTimeFormatSeconds().replace("%amount%", String.valueOf(seconds)));

		if(timeParts.isEmpty())
			timeParts.add(msgManager.getTimeNoMore());

		return String.join(msgManager.getTimeSeparator(), timeParts);
	}
	
	//Devuelve los millis del proximo reinicio de tiempo
	public static long getNextResetMillis(String resetTimeHour) {
		long currentMillis = System.currentTimeMillis();
		
		//Bukkit.getConsoleSender().sendMessage("reset time: "+resetTimeHour);
		String[] sep = resetTimeHour.split(":");
		String hour = sep[0];
		if(hour.startsWith("0")) {
			hour = hour.charAt(1)+"";
		}
		String minute = sep[1];
		if(minute.startsWith("0")) {
			minute = minute.charAt(1)+"";
		}
		
		Calendar calendar = Calendar.getInstance();
	    calendar.setTimeInMillis(currentMillis);
	    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
	    calendar.set(Calendar.MINUTE, Integer.parseInt(minute));
	    calendar.set(Calendar.SECOND, 0);
	    
	    if(calendar.getTimeInMillis() >= currentMillis) {
	    	//Aun no llega a la hora de reinicio en el dia
	    	//Bukkit.getConsoleSender().sendMessage("Hora reinicio: "+hour+":"+minute+"   | Aun no pasa");
		}else {
	    	//La hora de reinicio ya paso en el dia
	    	//Bukkit.getConsoleSender().sendMessage("Hora reinicio: "+hour+":"+minute+"   | Ya paso");
	    	calendar.add(Calendar.DAY_OF_YEAR, 1);
	    	//Bukkit.getConsoleSender().sendMessage("Nueva fecha: "+calendar.toString());
		}
		return calendar.getTimeInMillis();
	}
}
