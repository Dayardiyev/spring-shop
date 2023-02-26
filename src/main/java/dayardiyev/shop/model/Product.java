package dayardiyev.shop.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Product {

    private final String category;

    private final String name;

    private int price;
}
