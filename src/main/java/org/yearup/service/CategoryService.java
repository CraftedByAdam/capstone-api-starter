package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.Category;
import org.yearup.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService
{
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository)
    {
        this.categoryRepository = categoryRepository;
    }

    //Calls the repository to find every category saved in the database and returns them as a list.
    public List<Category> getAllCategories()
    {
        return categoryRepository.findAll();
    }

    //Looks for one specific category.
    public Category getById(int categoryId)
    {
        //Used orElse(null) so if the ID is wrong I just returns null.
        return categoryRepository.findById(categoryId).orElse(null);
    }

    public Category create(Category category)
    {
        //Set the ID to 0 so it's forced to create a brand-new record instead of updating an old one.
        category.setCategoryId(0);
        return categoryRepository.save(category);
    }

    public Category update(int categoryId, Category category)
    {
        //Getting the original categoryId, then overwrite the old name and description with the new info.
        Category existing = categoryRepository.findById(categoryId).orElseThrow();
        existing.setName(category.getName());
        existing.setDescription(category.getDescription());
        return categoryRepository.save(existing);
    }

    //Tells the repo to find the category by its ID and delete it rom the database.
    public void delete(int categoryId)
    {
        categoryRepository.deleteById(categoryId);
    }
}
