package com.kaungsett;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping("api/v1/customer")
public class Main {
	
	private final CustomerRepository customerRepository;
	
	public Main(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	
	public static void main(String args[]) {
		SpringApplication.run(Main.class, args);
	}
	
	
	@GetMapping
	public List<Customer> getCustomer() {
	    List<Customer> customers = customerRepository.findAll();
	    System.out.println("Retrieved customers: " + customers);
	    return customers;
	}
	
	record NewCustomerRequest(
			String name,
			String email,
			Integer age
			){}
	
	
	@PostMapping
	public void addCustomer(@RequestBody NewCustomerRequest request) {
		Customer customer = new Customer();
		customer.setName(request.name);
		customer.setEmail(request.email);
		customer.setAge(request.age);
		customerRepository.save(customer);
	}
	
	
	@DeleteMapping("{customerId}")
	public void deleteCustomer(@PathVariable("customerId") Integer id) {
		customerRepository.deleteById(id);
	}
	
	record EditCustomer(
			String name,
			String email,
			Integer age) {}
	
	@PutMapping("/{customerId}")
	public ResponseEntity<Customer> updateCustomer(
	        @PathVariable("customerId") Integer id,
	        @RequestBody EditCustomer edit) {
	    
	    Optional<Customer> optionalCustomer = customerRepository.findById(id);
	    
	    if (optionalCustomer.isEmpty()) {
	        return ResponseEntity.notFound().build();
	    }

	    Customer existingCustomer = optionalCustomer.get();

	    
	    if (edit.name() != null) {
	        existingCustomer.setName(edit.name());
	    }
	    if (edit.email() != null) {
	        existingCustomer.setEmail(edit.email());
	    }
	    if (edit.age() != null) {
	        existingCustomer.setAge(edit.age());
	    }

	    
	    Customer savedCustomer = customerRepository.save(existingCustomer);

	    return ResponseEntity.ok(savedCustomer);
	}


}
