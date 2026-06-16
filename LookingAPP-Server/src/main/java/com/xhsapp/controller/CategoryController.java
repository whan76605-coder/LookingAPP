package com.xhsapp.controller;

import com.xhsapp.common.Result;
import com.xhsapp.entity.Category;
import com.xhsapp.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /** GET /api/v1/categories */
    @GetMapping
    public Result<List<Category>> getAllCategories() {
        return Result.ok(categoryService.getAllCategories());
    }
}
