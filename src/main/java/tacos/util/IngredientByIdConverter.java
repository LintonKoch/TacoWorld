package tacos.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import tacos.domain.Ingredient;
import tacos.interfaces.IngredientRepository;


@Component
public class IngredientByIdConverter implements Converter<String, Ingredient>{
	 private IngredientRepository ingredientRepo;
	 
	  public IngredientByIdConverter(IngredientRepository ingredientRepo) {
	    this.ingredientRepo = ingredientRepo;
	  }
	 
	  @Override
	  public Ingredient convert(String id) {
	    return ingredientRepo.findById(id).orElse(null);
	  }
	 
}

  
 
