package com.hdse242052.lms_final.service;

import com.hdse242052.lms_final.dto.CategoryRequest;
import com.hdse242052.lms_final.dto.CategoryResponse;
import com.hdse242052.lms_final.dto.SubjectResponse;
import com.hdse242052.lms_final.entity.Category;
import com.hdse242052.lms_final.entity.Subject;
import com.hdse242052.lms_final.repository.CategoryRepository;
import com.hdse242052.lms_final.repository.SubjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final SubjectRepository subjectRepository;

    // Create category with subjects
    public void addCategory(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());

        Set<String> seenCodes = new HashSet<>();

        List<Subject> subjects = request.getSubjects().stream()
                .filter(s -> seenCodes.add(s.getCode()))
                .map(s -> {
                    Subject subject = new Subject();
                    subject.setName(s.getName());
                    subject.setCode(s.getCode());
                    subject.setCategory(category);
                    subject.setDegree(null);
                    return subject;
                }).collect(Collectors.toList());

        category.setSubjects(subjects);
        categoryRepository.save(category);
    }

    // Update category and subjects
    @Transactional
    public void updateCategory(Long id, CategoryRequest request) {
        try {
            Category category = categoryRepository.findById(id).orElseThrow(() ->
                    new RuntimeException("Category not found with ID: " + id));

            category.setName(request.getName());

            category.getSubjects().size(); // force initialization
            category.getSubjects().clear();
            categoryRepository.save(category); // flush orphan removal

            List<Subject> updatedSubjects = request.getSubjects().stream()
                    .map(s -> {
                        Subject subject = new Subject();
                        subject.setName(s.getName());
                        subject.setCode(s.getCode());
                        subject.setCategory(category);
                        subject.setDegree(null); // explicitly null
                        return subject;
                    }).collect(Collectors.toList());

            category.setSubjects(updatedSubjects);
            categoryRepository.save(category);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Update failed: " + e.getMessage());
        }
    }
    // Delete category
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found with ID: " + id);
        }
        categoryRepository.deleteById(id);
    }

    // Fetch all categories (raw entity)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Fetch all categories as DTOs with subjects
    public List<CategoryResponse> getAllCategoryResponses() {
        return categoryRepository.findAll().stream().map(cat -> {
            List<SubjectResponse> subjectResponses = cat.getSubjects().stream().map(sub ->
                    new SubjectResponse(sub.getName(), sub.getCode())
            ).collect(Collectors.toList());

            return new CategoryResponse(cat.getId(), cat.getName(), subjectResponses);
        }).collect(Collectors.toList());
    }

    // Fetch category by ID
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }
}