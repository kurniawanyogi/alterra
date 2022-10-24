package com.clean.architecture.service;

import com.clean.architecture.common.ResponseUtil;
import com.clean.architecture.constant.MessageConstant;
import com.clean.architecture.form.ProductForm;
import com.clean.architecture.model.ProductModel;
import com.clean.architecture.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Object> create(ProductForm form) {
        try {
            ProductModel product = mapToProductModel(form);
            productRepository.save(product);
            return ResponseUtil.build(MessageConstant.SUCCESS, product, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Object> getAll() {
        try {
            List<ProductModel> products = productRepository.findAll();
            return ResponseUtil.build(MessageConstant.SUCCESS, products, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Object> findById(Long id) {
        try {
            Optional<ProductModel> getById = productRepository.findById(id);
            return getById.map(productModel -> ResponseUtil.build(MessageConstant.SUCCESS, productModel, HttpStatus.OK))
                    .orElseGet(() -> ResponseUtil.build(MessageConstant.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Object> updateById(ProductForm form, Long id) {
        try {
            Optional<ProductModel> getById = productRepository.findById(id);
            if (!getById.isPresent())
                return ResponseUtil.build(MessageConstant.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            ProductModel product = getById.get();
            product.setProductName(form.getProductName());
            product.setPrice(form.getPrice());
            product.setQuantity(form.getQuantity());
            productRepository.save(product);
            return ResponseUtil.build(MessageConstant.SUCCESS_UPDATE, productRepository.save(product), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Object> deleteById(Long id) {
        try {
            Optional<ProductModel> data = productRepository.findById(id);
            if (!data.isPresent())
                return ResponseUtil.build(MessageConstant.DATA_NOT_FOUND, null, HttpStatus.NOT_FOUND);
            productRepository.delete(data.get());
            return ResponseUtil.build(MessageConstant.SUCCESS_DELETE, null, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static ProductModel mapToProductModel(ProductForm form) {
        ProductModel product = new ProductModel();
        product.setProductName(form.getProductName());
        product.setPrice(form.getPrice());
        return product;
    }
}
