package com.example.CashMate.repositories;


import com.example.CashMate.data.Category;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

@DataJpaTest
@ActiveProfiles("h2")
@Slf4j
public class CategoryRepositoryTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    public void testFindAll_shouldReturnAllCategories() {
        List<Category> expectedCategories = Arrays.asList(
                new Category(1L, "Electronics", "Electronics"),
                new Category(2L, "Books", "Books")
        );

        when(categoryRepository.findAll()).thenReturn(expectedCategories);

        List<Category> actualCategories = categoryRepository.findAll();

        assertThat(actualCategories).isNotEmpty();
        assertThat(actualCategories).containsExactlyElementsOf(expectedCategories);
    }

    @Test
    public void testFindAll_whenNoCategoriesExist_shouldReturnEmptyList() {
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        List<Category> actualCategories = categoryRepository.findAll();

        assertThat(actualCategories).isEmpty();
    }
}
