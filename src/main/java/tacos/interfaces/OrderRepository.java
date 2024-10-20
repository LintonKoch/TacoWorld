package tacos.interfaces;

import tacos.domain.TacoOrder;

public interface OrderRepository {

	TacoOrder save(TacoOrder order);
}
