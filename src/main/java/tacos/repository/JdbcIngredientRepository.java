package tacos.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import tacos.domain.Ingredient;
import tacos.interfaces.IngredientRepository;

@Repository
public class JdbcIngredientRepository implements IngredientRepository {
	
	private JdbcTemplate jdbcTemplate;
	
	/* @Autowired is only needed here if there were multiple constructors, 
	 * @Repository injects the dependency */ 
	public JdbcIngredientRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public Iterable<Ingredient> findAll(){
		return jdbcTemplate.query("select id, name, type from Ingredient", this::mapRowToIngredient);
	}
	
	@Override
	public Optional<Ingredient> findById(String id){
		List<Ingredient> results = jdbcTemplate.query(
				"select id, name, type from Ingredient where id=?",
				this::mapRowToIngredient,
				id);
		// condition -> "?" if true ":" if false
		return results.size()==0 ?
				Optional.empty() :
				Optional.of(results.get(0));
	}
	
	@Override
	public Ingredient save(Ingredient ingredient) {
		jdbcTemplate.update(
				"insert into Ingredient(id, name, type) values(?, ?, ?)",
				ingredient.getId(),
				ingredient.getName(),
				ingredient.getType().toString());
		
		return ingredient;
		
	}
	
	private Ingredient mapRowToIngredient(ResultSet row, int rowNum) throws SQLException {
		return new Ingredient(
				row.getString("id"),
				row.getString("name"),
				Ingredient.Type.valueOf(row.getString("type")));		
	}
	
	
	
	
	
	
}
