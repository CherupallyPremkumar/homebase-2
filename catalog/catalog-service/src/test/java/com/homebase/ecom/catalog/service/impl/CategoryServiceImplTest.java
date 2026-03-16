package com.homebase.ecom.catalog.service.impl;

import com.homebase.ecom.catalog.model.Category;
import com.homebase.ecom.catalog.domain.service.CatalogPolicyValidator;
import com.homebase.ecom.catalog.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CategoryServiceImpl Tests")
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CatalogPolicyValidator policyValidator;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Should set level to 0 for root category")
    void createCategory_RootCategory_SetsLevelZero() {
        // Arrange
        Category category = new Category();
        category.setName("Jewelry");
        category.setSlug("jewelry");
        category.setParentId(null);

        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        // Act
        Category result = categoryService.createCategory(category);

        // Assert
        verify(categoryRepository).save(argThat(cat -> cat.getLevel() == 0));
    }

    @Test
    @DisplayName("Should calculate level from parent for child category")
    void createCategory_ChildCategory_CalculatesLevelFromParent() {
        // Arrange
        Category parent = new Category();
        parent.setId("cat-001");
        parent.setLevel(0);

        Category child = new Category();
        child.setName("Necklaces");
        child.setSlug("necklaces");
        child.setParentId("cat-001");

        when(categoryRepository.findById("cat-001")).thenReturn(Optional.of(parent));
        when(categoryRepository.save(any(Category.class))).thenReturn(child);

        // Act
        Category result = categoryService.createCategory(child);

        // Assert
        verify(categoryRepository).save(argThat(cat -> cat.getLevel() == 1));
    }

    @Test
    @DisplayName("Should update category fields correctly")
    void updateCategory_ExistingCategory_UpdatesFields() {
        // Arrange
        Category existing = new Category();
        existing.setId("cat-001");
        existing.setName("Old Name");
        existing.setSlug("old-slug");

        Category updates = new Category();
        updates.setName("New Name");
        updates.setSlug("new-slug");
        updates.setDescription("New Description");
        updates.setActive(false);

        when(categoryRepository.findById("cat-001")).thenReturn(Optional.of(existing));
        when(categoryRepository.save(any(Category.class))).thenReturn(existing);

        // Act
        Category result = categoryService.updateCategory("cat-001", updates);

        // Assert
        verify(categoryRepository).save(argThat(cat -> cat.getName().equals("New Name") &&
                cat.getSlug().equals("new-slug") &&
                cat.getDescription().equals("New Description") &&
                cat.getActive() == false));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent category")
    void updateCategory_NonExistent_ThrowsException() {
        // Arrange
        when(categoryRepository.findById("cat-999")).thenReturn(Optional.empty());

        Category updates = new Category();
        updates.setName("Test");

        // Act & Assert
        assertThatThrownBy(() -> categoryService.updateCategory("cat-999", updates))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    @DisplayName("Should build category tree with children")
    void getCategoryTree_BuildsHierarchy() {
        // Arrange
        Category root = new Category();
        root.setId("cat-001");
        root.setName("Jewelry");

        Category child1 = new Category();
        child1.setId("cat-002");
        child1.setName("Necklaces");

        Category child2 = new Category();
        child2.setId("cat-003");
        child2.setName("Bracelets");

        when(categoryRepository.findRootCategories()).thenReturn(Arrays.asList(root));
        when(categoryRepository.findByParentId("cat-001")).thenReturn(Arrays.asList(child1, child2));
        when(categoryRepository.findByParentId("cat-002")).thenReturn(Arrays.asList());
        when(categoryRepository.findByParentId("cat-003")).thenReturn(Arrays.asList());

        // Act
        List<Category> tree = categoryService.getCategoryTree();

        // Assert
        assertThat(tree).hasSize(1);
        assertThat(tree.get(0).getChildren()).hasSize(2);
        assertThat(tree.get(0).getChildren())
                .extracting(Category::getName)
                .containsExactlyInAnyOrder("Necklaces", "Bracelets");
    }

    @Test
    @DisplayName("Should get root categories only")
    void getRootCategories_ReturnsOnlyRoots() {
        // Arrange
        Category root1 = new Category();
        root1.setName("Jewelry");

        Category root2 = new Category();
        root2.setName("Home Decor");

        when(categoryRepository.findRootCategories()).thenReturn(Arrays.asList(root1, root2));

        // Act
        List<Category> roots = categoryService.getRootCategories();

        // Assert
        assertThat(roots).hasSize(2);
        verify(categoryRepository).findRootCategories();
    }

    @Test
    @DisplayName("Should soft delete category by setting active to false")
    void deleteCategory_SoftDelete_SetsActiveFalse() {
        // Arrange
        Category category = new Category();
        category.setId("cat-001");
        category.setActive(true);

        when(categoryRepository.findById("cat-001")).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        // Act
        categoryService.deleteCategory("cat-001");

        // Assert
        verify(categoryRepository).save(argThat(cat -> cat.getActive() == false));
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent category")
    void deleteCategory_NonExistent_ThrowsException() {
        // Arrange
        when(categoryRepository.findById("cat-999")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> categoryService.deleteCategory("cat-999"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    @DisplayName("Should get children of specific category")
    void getChildCategories_ReturnsChildren() {
        // Arrange
        Category child1 = new Category();
        child1.setName("Child 1");

        Category child2 = new Category();
        child2.setName("Child 2");

        when(categoryRepository.findByParentId("cat-001")).thenReturn(Arrays.asList(child1, child2));

        // Act
        List<Category> children = categoryService.getChildCategories("cat-001");

        // Assert
        assertThat(children).hasSize(2);
        verify(categoryRepository).findByParentId("cat-001");
    }
}
