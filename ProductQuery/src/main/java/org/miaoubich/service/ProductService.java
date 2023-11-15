package org.miaoubich.service;

import java.util.List;
import java.util.Optional;

import org.miaoubich.entity.Product;
import org.miaoubich.event.ProductEvent;
import org.miaoubich.repository.ProductRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;
	
	public Product findProduct(Long id) {
		Optional<Product> product = productRepository.findById(id);
		
		if(!product.isEmpty())
			return product.get();
		else
			throw new NullPointerException("No such product in the database!");
	}
	
	public List<Product> findProducts(){
		return productRepository.findAll();
	}
	
	@KafkaListener(topics = "product-topic", groupId = "lina-group")
	public void processProductEvents(ProductEvent productEvent) {
		Product product = productEvent.getProduct();
		
		if(productEvent.getType().equals("creatProduct")) {
			productRepository.save(product);
		}
		else if(productEvent.getType().equals("creatProduct")){
			Product existProduct = productRepository.findById(product.getId()).get();
			existProduct.setName(product.getName());
			existProduct.setPrice(product.getPrice());
			existProduct.setQuantity(product.getQuantity());
			productRepository.save(existProduct);
		}
	}
}
