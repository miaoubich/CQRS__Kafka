package org.miaoubich.controller;

import org.miaoubich.entity.Product;
import org.miaoubich.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;
	
	@GetMapping
	public ResponseEntity<?> printProduct(@PathVariable Long id){
		Product product = productService.findProduct(id);
		
		return new ResponseEntity<>(product, HttpStatus.FOUND);
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> printProducts(){
		return new ResponseEntity<>(productService.findProducts(), HttpStatus.FOUND);
	}
}
