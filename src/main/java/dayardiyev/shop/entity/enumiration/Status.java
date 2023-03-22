package dayardiyev.shop.entity.enumiration;

import lombok.Getter;

@Getter
public enum Status {
    INSTOCK("На складе"),
    DELIVERY("Доставка"),
    DELIEVERED("Доставлен");
    private final String displayStatusName;

    Status(String displayStatusName){
        this.displayStatusName = displayStatusName;
    }
}
