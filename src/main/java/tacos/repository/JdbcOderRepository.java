package tacos.repository;

import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.asm.Type;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import tacos.interfaces.OrderRepository;
import tacos.domain.Taco;
import tacos.domain.TacoOrder;
import tacos.domain.Ingredient;
import tacos.domain.IngredientRef;

public class JdbcOderRepository implements OrderRepository {
	private JdbcOperations jdbcOperations;
	
	public JdbcOderRepository(JdbcOperations jdbcOperations) {
		this.jdbcOperations = jdbcOperations;
	}
	
	@Override
	@Transactional
	public TacoOrder save(TacoOrder order) {
		PreparedStatementCreatorFactory pscf =
				new PreparedStatementCreatorFactory(
						"insert into Taco_Order "
						+" (deliveryName, deliveryStreet, deliveryCity, "
						+"deliveryState, deliveryZip, ccNumber, "
						+"ccExpiration, ccCvv, placedAt) "
						+"values(?,?,?,?,?,?,?,?,?)",
						Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
						Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
						Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP
				);
		pscf.setReturnGeneratedKeys(true);
		
		order.setPlacedAt(new Date());
		PreparedStatementCreator psc =
				pscf.newPreparedStatementCreator(
						Arrays.asList(
							order.getDeliveryName(),
							order.getDeliveryStreet(),
							order.getDeliveryCity(),
							order.getDeliveryState(),
							order.getDeliveryZip(),
							order.getCcNumber(),
							order.getCcExpiration(),
							order.getCcCVV(),
							order.getPlacedAt()));
		
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcOperations.update(psc, keyHolder);
		long orderId = keyHolder.getKey().longValue();
		order.setId(orderId);
		
		List<Taco> tacos = order.getTacos();
		int i=0;
		for (Taco taco : tacos) {
			saveTaco(orderId, i++, taco);
		}
		return order;
	}
	
	private long saveTaco(Long orderId, int orderKey, Taco taco) {
		taco.setCreatedAt(new Date());
		PreparedStatementCreatorFactory pscf =
			new PreparedStatementCreatorFactory(
					"insert into Taco "
					+ "(name, created_at, taco_order, taco_order_key) "
					+"values (?, ?, ?, ?)",
				 	Types.VARCHAR, Types.TIMESTAMP, Types.BIGINT, Types.BIGINT
				);
		pscf.setReturnGeneratedKeys(true);
		
		PreparedStatementCreator psc = 
				pscf.newPreparedStatementCreator(
					Arrays.asList(
							taco.getName(),
							taco.getCreatedAt(),
							orderId,
							orderKey));
		
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcOperations.update(psc, keyHolder);
		long tacoId = keyHolder.getKey().longValue();
		taco.setId(tacoId);
		
		saveIngredientRefs(tacoId, taco.getIngredients());
		
		return tacoId;
	}
	
	private void saveIngredientRefs(
			long tacoId, List<IngredientRef> ingredientRefs) {
		
		int key = 0;
		for(IngredientRef ingredientRef : ingredientRefs) {
			jdbcOperations.update(
					"insert into Ingredient_Ref (ingredient, taco, taco_key) "
					+ "values (?, ?, ?)", 
					ingredientRef.getIngredient(), tacoId, key++);
		}
	}
}
