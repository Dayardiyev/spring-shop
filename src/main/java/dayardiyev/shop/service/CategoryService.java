package dayardiyev.shop.service;

import dayardiyev.shop.entity.Category;
import dayardiyev.shop.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getCategories(){
        return categoryRepository.findAll();
    }

    public Category getById(long id){
        return categoryRepository.findById(id).orElseThrow();
    }
}
