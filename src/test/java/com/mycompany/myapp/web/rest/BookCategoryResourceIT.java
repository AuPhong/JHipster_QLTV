package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.BookCategory;
import com.mycompany.myapp.repository.BookCategoryRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BookCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BookCategoryResourceIT {

    private static final String DEFAULT_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/book-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BookCategoryRepository bookCategoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookCategoryMockMvc;

    private BookCategory bookCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookCategory createEntity(EntityManager em) {
        BookCategory bookCategory = new BookCategory().categoryName(DEFAULT_CATEGORY_NAME);
        return bookCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookCategory createUpdatedEntity(EntityManager em) {
        BookCategory bookCategory = new BookCategory().categoryName(UPDATED_CATEGORY_NAME);
        return bookCategory;
    }

    @BeforeEach
    public void initTest() {
        bookCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createBookCategory() throws Exception {
        int databaseSizeBeforeCreate = bookCategoryRepository.findAll().size();
        // Create the BookCategory
        restBookCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookCategory)))
            .andExpect(status().isCreated());

        // Validate the BookCategory in the database
        List<BookCategory> bookCategoryList = bookCategoryRepository.findAll();
        assertThat(bookCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        BookCategory testBookCategory = bookCategoryList.get(bookCategoryList.size() - 1);
        assertThat(testBookCategory.getCategoryName()).isEqualTo(DEFAULT_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void createBookCategoryWithExistingId() throws Exception {
        // Create the BookCategory with an existing ID
        bookCategory.setId(1L);

        int databaseSizeBeforeCreate = bookCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookCategory)))
            .andExpect(status().isBadRequest());

        // Validate the BookCategory in the database
        List<BookCategory> bookCategoryList = bookCategoryRepository.findAll();
        assertThat(bookCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBookCategories() throws Exception {
        // Initialize the database
        bookCategoryRepository.saveAndFlush(bookCategory);

        // Get all the bookCategoryList
        restBookCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)));
    }

    @Test
    @Transactional
    void getBookCategory() throws Exception {
        // Initialize the database
        bookCategoryRepository.saveAndFlush(bookCategory);

        // Get the bookCategory
        restBookCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, bookCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bookCategory.getId().intValue()))
            .andExpect(jsonPath("$.categoryName").value(DEFAULT_CATEGORY_NAME));
    }

    @Test
    @Transactional
    void getNonExistingBookCategory() throws Exception {
        // Get the bookCategory
        restBookCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBookCategory() throws Exception {
        // Initialize the database
        bookCategoryRepository.saveAndFlush(bookCategory);

        int databaseSizeBeforeUpdate = bookCategoryRepository.findAll().size();

        // Update the bookCategory
        BookCategory updatedBookCategory = bookCategoryRepository.findById(bookCategory.getId()).get();
        // Disconnect from session so that the updates on updatedBookCategory are not directly saved in db
        em.detach(updatedBookCategory);
        updatedBookCategory.categoryName(UPDATED_CATEGORY_NAME);

        restBookCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBookCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBookCategory))
            )
            .andExpect(status().isOk());

        // Validate the BookCategory in the database
        List<BookCategory> bookCategoryList = bookCategoryRepository.findAll();
        assertThat(bookCategoryList).hasSize(databaseSizeBeforeUpdate);
        BookCategory testBookCategory = bookCategoryList.get(bookCategoryList.size() - 1);
        assertThat(testBookCategory.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void putNonExistingBookCategory() throws Exception {
        int databaseSizeBeforeUpdate = bookCategoryRepository.findAll().size();
        bookCategory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookCategory in the database
        List<BookCategory> bookCategoryList = bookCategoryRepository.findAll();
        assertThat(bookCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBookCategory() throws Exception {
        int databaseSizeBeforeUpdate = bookCategoryRepository.findAll().size();
        bookCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookCategory in the database
        List<BookCategory> bookCategoryList = bookCategoryRepository.findAll();
        assertThat(bookCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBookCategory() throws Exception {
        int databaseSizeBeforeUpdate = bookCategoryRepository.findAll().size();
        bookCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookCategory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookCategory in the database
        List<BookCategory> bookCategoryList = bookCategoryRepository.findAll();
        assertThat(bookCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookCategoryWithPatch() throws Exception {
        // Initialize the database
        bookCategoryRepository.saveAndFlush(bookCategory);

        int databaseSizeBeforeUpdate = bookCategoryRepository.findAll().size();

        // Update the bookCategory using partial update
        BookCategory partialUpdatedBookCategory = new BookCategory();
        partialUpdatedBookCategory.setId(bookCategory.getId());

        partialUpdatedBookCategory.categoryName(UPDATED_CATEGORY_NAME);

        restBookCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBookCategory))
            )
            .andExpect(status().isOk());

        // Validate the BookCategory in the database
        List<BookCategory> bookCategoryList = bookCategoryRepository.findAll();
        assertThat(bookCategoryList).hasSize(databaseSizeBeforeUpdate);
        BookCategory testBookCategory = bookCategoryList.get(bookCategoryList.size() - 1);
        assertThat(testBookCategory.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void fullUpdateBookCategoryWithPatch() throws Exception {
        // Initialize the database
        bookCategoryRepository.saveAndFlush(bookCategory);

        int databaseSizeBeforeUpdate = bookCategoryRepository.findAll().size();

        // Update the bookCategory using partial update
        BookCategory partialUpdatedBookCategory = new BookCategory();
        partialUpdatedBookCategory.setId(bookCategory.getId());

        partialUpdatedBookCategory.categoryName(UPDATED_CATEGORY_NAME);

        restBookCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBookCategory))
            )
            .andExpect(status().isOk());

        // Validate the BookCategory in the database
        List<BookCategory> bookCategoryList = bookCategoryRepository.findAll();
        assertThat(bookCategoryList).hasSize(databaseSizeBeforeUpdate);
        BookCategory testBookCategory = bookCategoryList.get(bookCategoryList.size() - 1);
        assertThat(testBookCategory.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingBookCategory() throws Exception {
        int databaseSizeBeforeUpdate = bookCategoryRepository.findAll().size();
        bookCategory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bookCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookCategory in the database
        List<BookCategory> bookCategoryList = bookCategoryRepository.findAll();
        assertThat(bookCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBookCategory() throws Exception {
        int databaseSizeBeforeUpdate = bookCategoryRepository.findAll().size();
        bookCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bookCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookCategory in the database
        List<BookCategory> bookCategoryList = bookCategoryRepository.findAll();
        assertThat(bookCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBookCategory() throws Exception {
        int databaseSizeBeforeUpdate = bookCategoryRepository.findAll().size();
        bookCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bookCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookCategory in the database
        List<BookCategory> bookCategoryList = bookCategoryRepository.findAll();
        assertThat(bookCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBookCategory() throws Exception {
        // Initialize the database
        bookCategoryRepository.saveAndFlush(bookCategory);

        int databaseSizeBeforeDelete = bookCategoryRepository.findAll().size();

        // Delete the bookCategory
        restBookCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, bookCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BookCategory> bookCategoryList = bookCategoryRepository.findAll();
        assertThat(bookCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
