package org.miaoubich.service;

import java.util.Optional;

import org.miaoubich.entity.Product;
import org.miaoubich.event.ProductEvent;
import org.miaoubich.repository.ProductRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ProductService {

	@Value("${spring.kafka.template.default-topic}")
	private String topic;
	private final ProductRepository productRepository;
	private final KafkaTemplate<String, ProductEvent> kafkaTemplate;
	
	public String addProduct(ProductEvent productEvent) {
		Product product = productRepository.save(productEvent.getProduct());
		ProductEvent event = new ProductEvent("creatProduct", product);
		kafkaTemplate.send(topic, event);
		String result = String.format("Product with ID: %s added successfully", product.getId());
		
		return result;
	}
	
	public Product updateProduct(Long id, ProductEvent productEvent) {
		Optional<Product> existProduct = productRepository.findById(id);
		Product product = productEvent.getProduct();
		
		if(!existProduct.isEmpty()) {
			existProduct.get().setName(product.getName());
			existProduct.get().setPrice(product.getPrice());
			existProduct.get().setQuantity(product.getQuantity());
			productRepository.save(existProduct.get());
			ProductEvent event = new ProductEvent("updateProduct", product);
			kafkaTemplate.send(topic, event);
		}
		else
			throw new NullPointerException(String.format("Product with ID %s doesn't exist.", id));
		
		return existProduct.get();
	}
	
	public String deleteProduct(Long id) {
		Optional<Product> product = productRepository.findById(id);
		String result = String.format("Product with ID: %s successfully deleted", id);
		
		if(!product.isEmpty()) {
			productRepository.delete(product.get());
		}
		else
			throw new NullPointerException(String.format("Product with ID %s doesn't exist.", id));
		
		return result;
	}
}
