package tacos.interfaces;

import java.util.Optional;
import tacos.domain.Ingredient;


public interface IngredientRepository {

	Iterable<Ingredient> findAll();
	
	Optional<Ingredient> findById(String id);
	
	Ingredient save(Ingredient ingredient);
	
}
