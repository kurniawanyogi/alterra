package com.clean.architecture.controller;

import com.clean.architecture.form.ProductForm;
import com.clean.architecture.model.ProductModel;
import com.clean.architecture.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController {
    @Autowired
    private ProductService productService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody ProductForm form) {
        return productService.create(form);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return productService.getAll();
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping(value = "{id}")
    public ResponseEntity<Object> update(@RequestBody ProductForm form, @PathVariable Long id) {
        return productService.updateById(form, id);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping(value = "{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        return productService.deleteById(id);
    }
}