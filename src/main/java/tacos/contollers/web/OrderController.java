package tacos.contollers.web;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import tacos.domain.TacoOrder;
import tacos.interfaces.OrderRepository;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
public class OrderController {
	
	private OrderRepository orderRepo;
	
	public OrderController(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
	}
	
	@GetMapping("/current")
	public String orderForm() {
		return "orderForm";
	}
	@PostMapping
	public String processOrder(@Valid TacoOrder order, Errors error,
			SessionStatus sessionStatus) {
		
		if(error.hasErrors()) {
			return "orderForm";
		}

		orderRepo.save(order);
		log.info("Order Submitted: {}", order);
		
		sessionStatus.setComplete();
		return "redirect:/";
	}
}
