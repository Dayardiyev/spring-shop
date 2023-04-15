package dayardiyev.shop.service;

import dayardiyev.shop.entity.enumiration.Status;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    public List<Status> getStatuses(){
        return List.of(Status.values());
    }

    public boolean isStatusEquals(Status a, Status b){
        return a == b;
    }
}
