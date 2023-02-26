package dayardiyev.shop.controller;


import dayardiyev.shop.model.Product;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/lesson")
public class LessonController {


    List<Product> list = new ArrayList<>();

    @RequestMapping("/products")
    public List<Product> getProducts() {
        list.add(new Product("Смартфоны", "Apple IPhone 12", 349900));
        list.add(new Product("Наушники", "Apple AirPods Pro", 109900));
        return list;
    }

    String input;
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public String test() {
        return "TEST MESSAGE with Header Old";
    }
}
