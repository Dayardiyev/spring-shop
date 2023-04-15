package dayardiyev.shop.service;

import dayardiyev.shop.entity.enumiration.Status;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Service
public class OrderService {

    public List<Status> getStatuses(){
        return List.of(Status.values());
    }

    public boolean isStatusEquals(Status a, Status b){
        return a == b;
    }

    public String getOrderDate(LocalDateTime date){
        return date.getDayOfMonth() + " "
                + getMonthOnRus(date) + " "
                + date.getYear() + " г. в "
                + date.getHour() + ":"
                + String.format("%02d", date.getMinute());
    }

    public String getMonthOnRus(LocalDateTime date){
        Month month = date.getMonth();
        Locale locale = new Locale("ru", "KZ");
        return month.getDisplayName(TextStyle.SHORT, locale);
    }
}
