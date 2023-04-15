package dayardiyev.shop.service;

import dayardiyev.shop.entity.Category;
import dayardiyev.shop.entity.Option;
import dayardiyev.shop.repository.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionService {

    @Autowired
    private OptionRepository optionRepository;

    public List<Option> getOptionsByCategory(Category category){
        return optionRepository.findAllByCategoryOrderById(category);
    }

}
