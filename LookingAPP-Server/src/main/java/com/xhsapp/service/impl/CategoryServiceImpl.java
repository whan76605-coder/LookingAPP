package com.xhsapp.service.impl;

import com.xhsapp.entity.Category;
import com.xhsapp.mapper.CategoryMapper;
import com.xhsapp.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryMapper.selectList(null);
    }
}
