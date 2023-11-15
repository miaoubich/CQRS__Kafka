package org.miaoubich.controller;

import org.miaoubich.event.ProductEvent;
import org.miaoubich.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("products")
public class ProductController {

	private final ProductService productService;
	
	@PostMapping
	public ResponseEntity<?> createProduct(@RequestBody ProductEvent productEvent){
		String result = productService.addProduct(productEvent);
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}
}
