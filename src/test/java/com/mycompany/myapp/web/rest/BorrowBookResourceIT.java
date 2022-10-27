package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.BorrowBook;
import com.mycompany.myapp.repository.BorrowBookRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link BorrowBookResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BorrowBookResourceIT {

    private static final Long DEFAULT_EMP_CODE = 1L;
    private static final Long UPDATED_EMP_CODE = 2L;

    private static final String DEFAULT_BOOK_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BOOK_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BORROW_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BORROW_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_INTENDED_RETURN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INTENDED_RETURN_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_RETURN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RETURN_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_NUMBER_OF_EXPIRATION_DATE = 1L;
    private static final Long UPDATED_NUMBER_OF_EXPIRATION_DATE = 2L;

    private static final Long DEFAULT_BOOK_CATEGORY = 1L;
    private static final Long UPDATED_BOOK_CATEGORY = 2L;

    private static final Long DEFAULT_CREATE_USER = 1L;
    private static final Long UPDATED_CREATE_USER = 2L;

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_UPDATE_USER = 1L;
    private static final Long UPDATED_UPDATE_USER = 2L;

    private static final Instant DEFAULT_UPDATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/borrow-books";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BorrowBookRepository borrowBookRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBorrowBookMockMvc;

    private BorrowBook borrowBook;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BorrowBook createEntity(EntityManager em) {
        BorrowBook borrowBook = new BorrowBook()
            .empCode(DEFAULT_EMP_CODE)
            .bookCode(DEFAULT_BOOK_CODE)
            .status(DEFAULT_STATUS)
            .borrowDate(DEFAULT_BORROW_DATE)
            .intendedReturnDate(DEFAULT_INTENDED_RETURN_DATE)
            .returnDate(DEFAULT_RETURN_DATE)
            .numberOfExpirationDate(DEFAULT_NUMBER_OF_EXPIRATION_DATE)
            .bookCategory(DEFAULT_BOOK_CATEGORY)
            .createUser(DEFAULT_CREATE_USER)
            .createDate(DEFAULT_CREATE_DATE)
            .updateUser(DEFAULT_UPDATE_USER)
            .updateDate(DEFAULT_UPDATE_DATE);
        return borrowBook;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BorrowBook createUpdatedEntity(EntityManager em) {
        BorrowBook borrowBook = new BorrowBook()
            .empCode(UPDATED_EMP_CODE)
            .bookCode(UPDATED_BOOK_CODE)
            .status(UPDATED_STATUS)
            .borrowDate(UPDATED_BORROW_DATE)
            .intendedReturnDate(UPDATED_INTENDED_RETURN_DATE)
            .returnDate(UPDATED_RETURN_DATE)
            .numberOfExpirationDate(UPDATED_NUMBER_OF_EXPIRATION_DATE)
            .bookCategory(UPDATED_BOOK_CATEGORY)
            .createUser(UPDATED_CREATE_USER)
            .createDate(UPDATED_CREATE_DATE)
            .updateUser(UPDATED_UPDATE_USER)
            .updateDate(UPDATED_UPDATE_DATE);
        return borrowBook;
    }

    @BeforeEach
    public void initTest() {
        borrowBook = createEntity(em);
    }

    @Test
    @Transactional
    void createBorrowBook() throws Exception {
        int databaseSizeBeforeCreate = borrowBookRepository.findAll().size();
        // Create the BorrowBook
        restBorrowBookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(borrowBook)))
            .andExpect(status().isCreated());

        // Validate the BorrowBook in the database
        List<BorrowBook> borrowBookList = borrowBookRepository.findAll();
        assertThat(borrowBookList).hasSize(databaseSizeBeforeCreate + 1);
        BorrowBook testBorrowBook = borrowBookList.get(borrowBookList.size() - 1);
        assertThat(testBorrowBook.getEmpCode()).isEqualTo(DEFAULT_EMP_CODE);
        assertThat(testBorrowBook.getBookCode()).isEqualTo(DEFAULT_BOOK_CODE);
        assertThat(testBorrowBook.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testBorrowBook.getBorrowDate()).isEqualTo(DEFAULT_BORROW_DATE);
        assertThat(testBorrowBook.getIntendedReturnDate()).isEqualTo(DEFAULT_INTENDED_RETURN_DATE);
        assertThat(testBorrowBook.getReturnDate()).isEqualTo(DEFAULT_RETURN_DATE);
        assertThat(testBorrowBook.getNumberOfExpirationDate()).isEqualTo(DEFAULT_NUMBER_OF_EXPIRATION_DATE);
        assertThat(testBorrowBook.getBookCategory()).isEqualTo(DEFAULT_BOOK_CATEGORY);
        assertThat(testBorrowBook.getCreateUser()).isEqualTo(DEFAULT_CREATE_USER);
        assertThat(testBorrowBook.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testBorrowBook.getUpdateUser()).isEqualTo(DEFAULT_UPDATE_USER);
        assertThat(testBorrowBook.getUpdateDate()).isEqualTo(DEFAULT_UPDATE_DATE);
    }

    @Test
    @Transactional
    void createBorrowBookWithExistingId() throws Exception {
        // Create the BorrowBook with an existing ID
        borrowBook.setId(1L);

        int databaseSizeBeforeCreate = borrowBookRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBorrowBookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(borrowBook)))
            .andExpect(status().isBadRequest());

        // Validate the BorrowBook in the database
        List<BorrowBook> borrowBookList = borrowBookRepository.findAll();
        assertThat(borrowBookList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBorrowBooks() throws Exception {
        // Initialize the database
        borrowBookRepository.saveAndFlush(borrowBook);

        // Get all the borrowBookList
        restBorrowBookMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(borrowBook.getId().intValue())))
            .andExpect(jsonPath("$.[*].empCode").value(hasItem(DEFAULT_EMP_CODE.intValue())))
            .andExpect(jsonPath("$.[*].bookCode").value(hasItem(DEFAULT_BOOK_CODE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].borrowDate").value(hasItem(DEFAULT_BORROW_DATE.toString())))
            .andExpect(jsonPath("$.[*].intendedReturnDate").value(hasItem(DEFAULT_INTENDED_RETURN_DATE.toString())))
            .andExpect(jsonPath("$.[*].returnDate").value(hasItem(DEFAULT_RETURN_DATE.toString())))
            .andExpect(jsonPath("$.[*].numberOfExpirationDate").value(hasItem(DEFAULT_NUMBER_OF_EXPIRATION_DATE.intValue())))
            .andExpect(jsonPath("$.[*].bookCategory").value(hasItem(DEFAULT_BOOK_CATEGORY.intValue())))
            .andExpect(jsonPath("$.[*].createUser").value(hasItem(DEFAULT_CREATE_USER.intValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].updateUser").value(hasItem(DEFAULT_UPDATE_USER.intValue())))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(DEFAULT_UPDATE_DATE.toString())));
    }

    @Test
    @Transactional
    void getBorrowBook() throws Exception {
        // Initialize the database
        borrowBookRepository.saveAndFlush(borrowBook);

        // Get the borrowBook
        restBorrowBookMockMvc
            .perform(get(ENTITY_API_URL_ID, borrowBook.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(borrowBook.getId().intValue()))
            .andExpect(jsonPath("$.empCode").value(DEFAULT_EMP_CODE.intValue()))
            .andExpect(jsonPath("$.bookCode").value(DEFAULT_BOOK_CODE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.borrowDate").value(DEFAULT_BORROW_DATE.toString()))
            .andExpect(jsonPath("$.intendedReturnDate").value(DEFAULT_INTENDED_RETURN_DATE.toString()))
            .andExpect(jsonPath("$.returnDate").value(DEFAULT_RETURN_DATE.toString()))
            .andExpect(jsonPath("$.numberOfExpirationDate").value(DEFAULT_NUMBER_OF_EXPIRATION_DATE.intValue()))
            .andExpect(jsonPath("$.bookCategory").value(DEFAULT_BOOK_CATEGORY.intValue()))
            .andExpect(jsonPath("$.createUser").value(DEFAULT_CREATE_USER.intValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.updateUser").value(DEFAULT_UPDATE_USER.intValue()))
            .andExpect(jsonPath("$.updateDate").value(DEFAULT_UPDATE_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBorrowBook() throws Exception {
        // Get the borrowBook
        restBorrowBookMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBorrowBook() throws Exception {
        // Initialize the database
        borrowBookRepository.saveAndFlush(borrowBook);

        int databaseSizeBeforeUpdate = borrowBookRepository.findAll().size();

        // Update the borrowBook
        BorrowBook updatedBorrowBook = borrowBookRepository.findById(borrowBook.getId()).get();
        // Disconnect from session so that the updates on updatedBorrowBook are not directly saved in db
        em.detach(updatedBorrowBook);
        updatedBorrowBook
            .empCode(UPDATED_EMP_CODE)
            .bookCode(UPDATED_BOOK_CODE)
            .status(UPDATED_STATUS)
            .borrowDate(UPDATED_BORROW_DATE)
            .intendedReturnDate(UPDATED_INTENDED_RETURN_DATE)
            .returnDate(UPDATED_RETURN_DATE)
            .numberOfExpirationDate(UPDATED_NUMBER_OF_EXPIRATION_DATE)
            .bookCategory(UPDATED_BOOK_CATEGORY)
            .createUser(UPDATED_CREATE_USER)
            .createDate(UPDATED_CREATE_DATE)
            .updateUser(UPDATED_UPDATE_USER)
            .updateDate(UPDATED_UPDATE_DATE);

        restBorrowBookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBorrowBook.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBorrowBook))
            )
            .andExpect(status().isOk());

        // Validate the BorrowBook in the database
        List<BorrowBook> borrowBookList = borrowBookRepository.findAll();
        assertThat(borrowBookList).hasSize(databaseSizeBeforeUpdate);
        BorrowBook testBorrowBook = borrowBookList.get(borrowBookList.size() - 1);
        assertThat(testBorrowBook.getEmpCode()).isEqualTo(UPDATED_EMP_CODE);
        assertThat(testBorrowBook.getBookCode()).isEqualTo(UPDATED_BOOK_CODE);
        assertThat(testBorrowBook.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBorrowBook.getBorrowDate()).isEqualTo(UPDATED_BORROW_DATE);
        assertThat(testBorrowBook.getIntendedReturnDate()).isEqualTo(UPDATED_INTENDED_RETURN_DATE);
        assertThat(testBorrowBook.getReturnDate()).isEqualTo(UPDATED_RETURN_DATE);
        assertThat(testBorrowBook.getNumberOfExpirationDate()).isEqualTo(UPDATED_NUMBER_OF_EXPIRATION_DATE);
        assertThat(testBorrowBook.getBookCategory()).isEqualTo(UPDATED_BOOK_CATEGORY);
        assertThat(testBorrowBook.getCreateUser()).isEqualTo(UPDATED_CREATE_USER);
        assertThat(testBorrowBook.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testBorrowBook.getUpdateUser()).isEqualTo(UPDATED_UPDATE_USER);
        assertThat(testBorrowBook.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
    }

    @Test
    @Transactional
    void putNonExistingBorrowBook() throws Exception {
        int databaseSizeBeforeUpdate = borrowBookRepository.findAll().size();
        borrowBook.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBorrowBookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, borrowBook.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(borrowBook))
            )
            .andExpect(status().isBadRequest());

        // Validate the BorrowBook in the database
        List<BorrowBook> borrowBookList = borrowBookRepository.findAll();
        assertThat(borrowBookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBorrowBook() throws Exception {
        int databaseSizeBeforeUpdate = borrowBookRepository.findAll().size();
        borrowBook.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBorrowBookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(borrowBook))
            )
            .andExpect(status().isBadRequest());

        // Validate the BorrowBook in the database
        List<BorrowBook> borrowBookList = borrowBookRepository.findAll();
        assertThat(borrowBookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBorrowBook() throws Exception {
        int databaseSizeBeforeUpdate = borrowBookRepository.findAll().size();
        borrowBook.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBorrowBookMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(borrowBook)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BorrowBook in the database
        List<BorrowBook> borrowBookList = borrowBookRepository.findAll();
        assertThat(borrowBookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBorrowBookWithPatch() throws Exception {
        // Initialize the database
        borrowBookRepository.saveAndFlush(borrowBook);

        int databaseSizeBeforeUpdate = borrowBookRepository.findAll().size();

        // Update the borrowBook using partial update
        BorrowBook partialUpdatedBorrowBook = new BorrowBook();
        partialUpdatedBorrowBook.setId(borrowBook.getId());

        partialUpdatedBorrowBook
            .empCode(UPDATED_EMP_CODE)
            .bookCode(UPDATED_BOOK_CODE)
            .status(UPDATED_STATUS)
            .borrowDate(UPDATED_BORROW_DATE)
            .intendedReturnDate(UPDATED_INTENDED_RETURN_DATE)
            .bookCategory(UPDATED_BOOK_CATEGORY)
            .createUser(UPDATED_CREATE_USER)
            .createDate(UPDATED_CREATE_DATE)
            .updateDate(UPDATED_UPDATE_DATE);

        restBorrowBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBorrowBook.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBorrowBook))
            )
            .andExpect(status().isOk());

        // Validate the BorrowBook in the database
        List<BorrowBook> borrowBookList = borrowBookRepository.findAll();
        assertThat(borrowBookList).hasSize(databaseSizeBeforeUpdate);
        BorrowBook testBorrowBook = borrowBookList.get(borrowBookList.size() - 1);
        assertThat(testBorrowBook.getEmpCode()).isEqualTo(UPDATED_EMP_CODE);
        assertThat(testBorrowBook.getBookCode()).isEqualTo(UPDATED_BOOK_CODE);
        assertThat(testBorrowBook.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBorrowBook.getBorrowDate()).isEqualTo(UPDATED_BORROW_DATE);
        assertThat(testBorrowBook.getIntendedReturnDate()).isEqualTo(UPDATED_INTENDED_RETURN_DATE);
        assertThat(testBorrowBook.getReturnDate()).isEqualTo(DEFAULT_RETURN_DATE);
        assertThat(testBorrowBook.getNumberOfExpirationDate()).isEqualTo(DEFAULT_NUMBER_OF_EXPIRATION_DATE);
        assertThat(testBorrowBook.getBookCategory()).isEqualTo(UPDATED_BOOK_CATEGORY);
        assertThat(testBorrowBook.getCreateUser()).isEqualTo(UPDATED_CREATE_USER);
        assertThat(testBorrowBook.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testBorrowBook.getUpdateUser()).isEqualTo(DEFAULT_UPDATE_USER);
        assertThat(testBorrowBook.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
    }

    @Test
    @Transactional
    void fullUpdateBorrowBookWithPatch() throws Exception {
        // Initialize the database
        borrowBookRepository.saveAndFlush(borrowBook);

        int databaseSizeBeforeUpdate = borrowBookRepository.findAll().size();

        // Update the borrowBook using partial update
        BorrowBook partialUpdatedBorrowBook = new BorrowBook();
        partialUpdatedBorrowBook.setId(borrowBook.getId());

        partialUpdatedBorrowBook
            .empCode(UPDATED_EMP_CODE)
            .bookCode(UPDATED_BOOK_CODE)
            .status(UPDATED_STATUS)
            .borrowDate(UPDATED_BORROW_DATE)
            .intendedReturnDate(UPDATED_INTENDED_RETURN_DATE)
            .returnDate(UPDATED_RETURN_DATE)
            .numberOfExpirationDate(UPDATED_NUMBER_OF_EXPIRATION_DATE)
            .bookCategory(UPDATED_BOOK_CATEGORY)
            .createUser(UPDATED_CREATE_USER)
            .createDate(UPDATED_CREATE_DATE)
            .updateUser(UPDATED_UPDATE_USER)
            .updateDate(UPDATED_UPDATE_DATE);

        restBorrowBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBorrowBook.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBorrowBook))
            )
            .andExpect(status().isOk());

        // Validate the BorrowBook in the database
        List<BorrowBook> borrowBookList = borrowBookRepository.findAll();
        assertThat(borrowBookList).hasSize(databaseSizeBeforeUpdate);
        BorrowBook testBorrowBook = borrowBookList.get(borrowBookList.size() - 1);
        assertThat(testBorrowBook.getEmpCode()).isEqualTo(UPDATED_EMP_CODE);
        assertThat(testBorrowBook.getBookCode()).isEqualTo(UPDATED_BOOK_CODE);
        assertThat(testBorrowBook.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBorrowBook.getBorrowDate()).isEqualTo(UPDATED_BORROW_DATE);
        assertThat(testBorrowBook.getIntendedReturnDate()).isEqualTo(UPDATED_INTENDED_RETURN_DATE);
        assertThat(testBorrowBook.getReturnDate()).isEqualTo(UPDATED_RETURN_DATE);
        assertThat(testBorrowBook.getNumberOfExpirationDate()).isEqualTo(UPDATED_NUMBER_OF_EXPIRATION_DATE);
        assertThat(testBorrowBook.getBookCategory()).isEqualTo(UPDATED_BOOK_CATEGORY);
        assertThat(testBorrowBook.getCreateUser()).isEqualTo(UPDATED_CREATE_USER);
        assertThat(testBorrowBook.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testBorrowBook.getUpdateUser()).isEqualTo(UPDATED_UPDATE_USER);
        assertThat(testBorrowBook.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingBorrowBook() throws Exception {
        int databaseSizeBeforeUpdate = borrowBookRepository.findAll().size();
        borrowBook.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBorrowBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, borrowBook.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(borrowBook))
            )
            .andExpect(status().isBadRequest());

        // Validate the BorrowBook in the database
        List<BorrowBook> borrowBookList = borrowBookRepository.findAll();
        assertThat(borrowBookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBorrowBook() throws Exception {
        int databaseSizeBeforeUpdate = borrowBookRepository.findAll().size();
        borrowBook.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBorrowBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(borrowBook))
            )
            .andExpect(status().isBadRequest());

        // Validate the BorrowBook in the database
        List<BorrowBook> borrowBookList = borrowBookRepository.findAll();
        assertThat(borrowBookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBorrowBook() throws Exception {
        int databaseSizeBeforeUpdate = borrowBookRepository.findAll().size();
        borrowBook.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBorrowBookMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(borrowBook))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BorrowBook in the database
        List<BorrowBook> borrowBookList = borrowBookRepository.findAll();
        assertThat(borrowBookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBorrowBook() throws Exception {
        // Initialize the database
        borrowBookRepository.saveAndFlush(borrowBook);

        int databaseSizeBeforeDelete = borrowBookRepository.findAll().size();

        // Delete the borrowBook
        restBorrowBookMockMvc
            .perform(delete(ENTITY_API_URL_ID, borrowBook.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BorrowBook> borrowBookList = borrowBookRepository.findAll();
        assertThat(borrowBookList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
