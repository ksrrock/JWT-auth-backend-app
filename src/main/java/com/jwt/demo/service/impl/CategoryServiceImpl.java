package com.jwt.demo.service.impl;

import com.jwt.demo.entities.Category;
import com.jwt.demo.exception.ResourceNotFoundException;

import com.jwt.demo.payload.CategoryRequestDto;
import com.jwt.demo.payload.CategoryResponseDto;
import com.jwt.demo.repository.CategoryRepository;
import com.jwt.demo.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

  @Autowired private CategoryRepository categoryRepository;
  @Autowired
  private ModelMapper modelMapper;

  @Override
  public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
    Category category = this.categoryRepository.save(this.modelMapper.map(categoryRequestDto, Category.class));
    return modelMapper.map(category, CategoryResponseDto.class);
  }

  @Override
  public CategoryResponseDto updateCategory(CategoryRequestDto categoryRequestDto, Integer id) {
    Category category =
        this.categoryRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category", id));
    category.setCategoryDescription(categoryRequestDto.getCategoryDescription());
    category.setCategoryTitle(categoryRequestDto.getCategoryTitle());
    this.categoryRepository.save(category);
    return this.modelMapper.map(category, CategoryResponseDto.class);
  }

  @Override
  public void deleteCategory(Integer id) {
    Category category =
        this.categoryRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category", id));
    this.categoryRepository.delete(category);
  }

  @Override
  public CategoryResponseDto getCategory(Integer id) {
    Category category =
        this.categoryRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category", id));
    return this.modelMapper.map(category , CategoryResponseDto.class);
  }

  @Override
  public List<CategoryResponseDto> getAllCategory() {
    List<Category> categories = this.categoryRepository.findAll();
    List<CategoryResponseDto> categoryResponseDtos =
        categories.stream()
            .map((category -> this.modelMapper.map(category, CategoryResponseDto.class)))
            .collect(Collectors.toList());
    return categoryResponseDtos;
  }
}
