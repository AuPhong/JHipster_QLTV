package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.BookEntity;
import com.mycompany.myapp.repository.BookEntityRepository;
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
 * Integration tests for the {@link BookEntityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BookEntityResourceIT {

    private static final String DEFAULT_BOOK_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BOOK_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BOOK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BOOK_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_AUTHOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR = "BBBBBBBBBB";

    private static final String DEFAULT_PUBLISHER = "AAAAAAAAAA";
    private static final String UPDATED_PUBLISHER = "BBBBBBBBBB";

    private static final Long DEFAULT_BOOK_CATEGORY = 1L;
    private static final Long UPDATED_BOOK_CATEGORY = 2L;

    private static final Instant DEFAULT_INPUT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INPUT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Long DEFAULT_BORROWER = 1L;
    private static final Long UPDATED_BORROWER = 2L;

    private static final LocalDate DEFAULT_BORROW_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BORROW_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_INTENDED_RETURN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INTENDED_RETURN_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_CREATE_USER = 1L;
    private static final Long UPDATED_CREATE_USER = 2L;

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_UPDATE_USER = 1L;
    private static final Long UPDATED_UPDATE_USER = 2L;

    private static final Instant DEFAULT_UPDATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_BOOK_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_BOOK_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_IS_ACTIVE = "AAAAAAAAAA";
    private static final String UPDATED_IS_ACTIVE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/book-entities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BookEntityRepository bookEntityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookEntityMockMvc;

    private BookEntity bookEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookEntity createEntity(EntityManager em) {
        BookEntity bookEntity = new BookEntity()
            .bookCode(DEFAULT_BOOK_CODE)
            .bookName(DEFAULT_BOOK_NAME)
            .author(DEFAULT_AUTHOR)
            .publisher(DEFAULT_PUBLISHER)
            .bookCategory(DEFAULT_BOOK_CATEGORY)
            .inputDate(DEFAULT_INPUT_DATE)
            .status(DEFAULT_STATUS)
            .borrower(DEFAULT_BORROWER)
            .borrowDate(DEFAULT_BORROW_DATE)
            .intendedReturnDate(DEFAULT_INTENDED_RETURN_DATE)
            .createUser(DEFAULT_CREATE_USER)
            .createDate(DEFAULT_CREATE_DATE)
            .updateUser(DEFAULT_UPDATE_USER)
            .updateDate(DEFAULT_UPDATE_DATE)
            .bookImage(DEFAULT_BOOK_IMAGE)
            .isActive(DEFAULT_IS_ACTIVE);
        return bookEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookEntity createUpdatedEntity(EntityManager em) {
        BookEntity bookEntity = new BookEntity()
            .bookCode(UPDATED_BOOK_CODE)
            .bookName(UPDATED_BOOK_NAME)
            .author(UPDATED_AUTHOR)
            .publisher(UPDATED_PUBLISHER)
            .bookCategory(UPDATED_BOOK_CATEGORY)
            .inputDate(UPDATED_INPUT_DATE)
            .status(UPDATED_STATUS)
            .borrower(UPDATED_BORROWER)
            .borrowDate(UPDATED_BORROW_DATE)
            .intendedReturnDate(UPDATED_INTENDED_RETURN_DATE)
            .createUser(UPDATED_CREATE_USER)
            .createDate(UPDATED_CREATE_DATE)
            .updateUser(UPDATED_UPDATE_USER)
            .updateDate(UPDATED_UPDATE_DATE)
            .bookImage(UPDATED_BOOK_IMAGE)
            .isActive(UPDATED_IS_ACTIVE);
        return bookEntity;
    }

    @BeforeEach
    public void initTest() {
        bookEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createBookEntity() throws Exception {
        int databaseSizeBeforeCreate = bookEntityRepository.findAll().size();
        // Create the BookEntity
        restBookEntityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookEntity)))
            .andExpect(status().isCreated());

        // Validate the BookEntity in the database
        List<BookEntity> bookEntityList = bookEntityRepository.findAll();
        assertThat(bookEntityList).hasSize(databaseSizeBeforeCreate + 1);
        BookEntity testBookEntity = bookEntityList.get(bookEntityList.size() - 1);
        assertThat(testBookEntity.getBookCode()).isEqualTo(DEFAULT_BOOK_CODE);
        assertThat(testBookEntity.getBookName()).isEqualTo(DEFAULT_BOOK_NAME);
        assertThat(testBookEntity.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testBookEntity.getPublisher()).isEqualTo(DEFAULT_PUBLISHER);
        assertThat(testBookEntity.getBookCategory()).isEqualTo(DEFAULT_BOOK_CATEGORY);
        assertThat(testBookEntity.getInputDate()).isEqualTo(DEFAULT_INPUT_DATE);
        assertThat(testBookEntity.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testBookEntity.getBorrower()).isEqualTo(DEFAULT_BORROWER);
        assertThat(testBookEntity.getBorrowDate()).isEqualTo(DEFAULT_BORROW_DATE);
        assertThat(testBookEntity.getIntendedReturnDate()).isEqualTo(DEFAULT_INTENDED_RETURN_DATE);
        assertThat(testBookEntity.getCreateUser()).isEqualTo(DEFAULT_CREATE_USER);
        assertThat(testBookEntity.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testBookEntity.getUpdateUser()).isEqualTo(DEFAULT_UPDATE_USER);
        assertThat(testBookEntity.getUpdateDate()).isEqualTo(DEFAULT_UPDATE_DATE);
        assertThat(testBookEntity.getBookImage()).isEqualTo(DEFAULT_BOOK_IMAGE);
        assertThat(testBookEntity.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createBookEntityWithExistingId() throws Exception {
        // Create the BookEntity with an existing ID
        bookEntity.setId(1L);

        int databaseSizeBeforeCreate = bookEntityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookEntityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookEntity)))
            .andExpect(status().isBadRequest());

        // Validate the BookEntity in the database
        List<BookEntity> bookEntityList = bookEntityRepository.findAll();
        assertThat(bookEntityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBookCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookEntityRepository.findAll().size();
        // set the field null
        bookEntity.setBookCode(null);

        // Create the BookEntity, which fails.

        restBookEntityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookEntity)))
            .andExpect(status().isBadRequest());

        List<BookEntity> bookEntityList = bookEntityRepository.findAll();
        assertThat(bookEntityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBookNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookEntityRepository.findAll().size();
        // set the field null
        bookEntity.setBookName(null);

        // Create the BookEntity, which fails.

        restBookEntityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookEntity)))
            .andExpect(status().isBadRequest());

        List<BookEntity> bookEntityList = bookEntityRepository.findAll();
        assertThat(bookEntityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBookEntities() throws Exception {
        // Initialize the database
        bookEntityRepository.saveAndFlush(bookEntity);

        // Get all the bookEntityList
        restBookEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].bookCode").value(hasItem(DEFAULT_BOOK_CODE)))
            .andExpect(jsonPath("$.[*].bookName").value(hasItem(DEFAULT_BOOK_NAME)))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR)))
            .andExpect(jsonPath("$.[*].publisher").value(hasItem(DEFAULT_PUBLISHER)))
            .andExpect(jsonPath("$.[*].bookCategory").value(hasItem(DEFAULT_BOOK_CATEGORY.intValue())))
            .andExpect(jsonPath("$.[*].inputDate").value(hasItem(DEFAULT_INPUT_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].borrower").value(hasItem(DEFAULT_BORROWER.intValue())))
            .andExpect(jsonPath("$.[*].borrowDate").value(hasItem(DEFAULT_BORROW_DATE.toString())))
            .andExpect(jsonPath("$.[*].intendedReturnDate").value(hasItem(DEFAULT_INTENDED_RETURN_DATE.toString())))
            .andExpect(jsonPath("$.[*].createUser").value(hasItem(DEFAULT_CREATE_USER.intValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].updateUser").value(hasItem(DEFAULT_UPDATE_USER.intValue())))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(DEFAULT_UPDATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].bookImage").value(hasItem(DEFAULT_BOOK_IMAGE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    @Test
    @Transactional
    void getBookEntity() throws Exception {
        // Initialize the database
        bookEntityRepository.saveAndFlush(bookEntity);

        // Get the bookEntity
        restBookEntityMockMvc
            .perform(get(ENTITY_API_URL_ID, bookEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bookEntity.getId().intValue()))
            .andExpect(jsonPath("$.bookCode").value(DEFAULT_BOOK_CODE))
            .andExpect(jsonPath("$.bookName").value(DEFAULT_BOOK_NAME))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR))
            .andExpect(jsonPath("$.publisher").value(DEFAULT_PUBLISHER))
            .andExpect(jsonPath("$.bookCategory").value(DEFAULT_BOOK_CATEGORY.intValue()))
            .andExpect(jsonPath("$.inputDate").value(DEFAULT_INPUT_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.borrower").value(DEFAULT_BORROWER.intValue()))
            .andExpect(jsonPath("$.borrowDate").value(DEFAULT_BORROW_DATE.toString()))
            .andExpect(jsonPath("$.intendedReturnDate").value(DEFAULT_INTENDED_RETURN_DATE.toString()))
            .andExpect(jsonPath("$.createUser").value(DEFAULT_CREATE_USER.intValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.updateUser").value(DEFAULT_UPDATE_USER.intValue()))
            .andExpect(jsonPath("$.updateDate").value(DEFAULT_UPDATE_DATE.toString()))
            .andExpect(jsonPath("$.bookImage").value(DEFAULT_BOOK_IMAGE))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE));
    }

    @Test
    @Transactional
    void getNonExistingBookEntity() throws Exception {
        // Get the bookEntity
        restBookEntityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBookEntity() throws Exception {
        // Initialize the database
        bookEntityRepository.saveAndFlush(bookEntity);

        int databaseSizeBeforeUpdate = bookEntityRepository.findAll().size();

        // Update the bookEntity
        BookEntity updatedBookEntity = bookEntityRepository.findById(bookEntity.getId()).get();
        // Disconnect from session so that the updates on updatedBookEntity are not directly saved in db
        em.detach(updatedBookEntity);
        updatedBookEntity
            .bookCode(UPDATED_BOOK_CODE)
            .bookName(UPDATED_BOOK_NAME)
            .author(UPDATED_AUTHOR)
            .publisher(UPDATED_PUBLISHER)
            .bookCategory(UPDATED_BOOK_CATEGORY)
            .inputDate(UPDATED_INPUT_DATE)
            .status(UPDATED_STATUS)
            .borrower(UPDATED_BORROWER)
            .borrowDate(UPDATED_BORROW_DATE)
            .intendedReturnDate(UPDATED_INTENDED_RETURN_DATE)
            .createUser(UPDATED_CREATE_USER)
            .createDate(UPDATED_CREATE_DATE)
            .updateUser(UPDATED_UPDATE_USER)
            .updateDate(UPDATED_UPDATE_DATE)
            .bookImage(UPDATED_BOOK_IMAGE)
            .isActive(UPDATED_IS_ACTIVE);

        restBookEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBookEntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBookEntity))
            )
            .andExpect(status().isOk());

        // Validate the BookEntity in the database
        List<BookEntity> bookEntityList = bookEntityRepository.findAll();
        assertThat(bookEntityList).hasSize(databaseSizeBeforeUpdate);
        BookEntity testBookEntity = bookEntityList.get(bookEntityList.size() - 1);
        assertThat(testBookEntity.getBookCode()).isEqualTo(UPDATED_BOOK_CODE);
        assertThat(testBookEntity.getBookName()).isEqualTo(UPDATED_BOOK_NAME);
        assertThat(testBookEntity.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testBookEntity.getPublisher()).isEqualTo(UPDATED_PUBLISHER);
        assertThat(testBookEntity.getBookCategory()).isEqualTo(UPDATED_BOOK_CATEGORY);
        assertThat(testBookEntity.getInputDate()).isEqualTo(UPDATED_INPUT_DATE);
        assertThat(testBookEntity.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBookEntity.getBorrower()).isEqualTo(UPDATED_BORROWER);
        assertThat(testBookEntity.getBorrowDate()).isEqualTo(UPDATED_BORROW_DATE);
        assertThat(testBookEntity.getIntendedReturnDate()).isEqualTo(UPDATED_INTENDED_RETURN_DATE);
        assertThat(testBookEntity.getCreateUser()).isEqualTo(UPDATED_CREATE_USER);
        assertThat(testBookEntity.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testBookEntity.getUpdateUser()).isEqualTo(UPDATED_UPDATE_USER);
        assertThat(testBookEntity.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
        assertThat(testBookEntity.getBookImage()).isEqualTo(UPDATED_BOOK_IMAGE);
        assertThat(testBookEntity.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingBookEntity() throws Exception {
        int databaseSizeBeforeUpdate = bookEntityRepository.findAll().size();
        bookEntity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookEntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookEntity in the database
        List<BookEntity> bookEntityList = bookEntityRepository.findAll();
        assertThat(bookEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBookEntity() throws Exception {
        int databaseSizeBeforeUpdate = bookEntityRepository.findAll().size();
        bookEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookEntity in the database
        List<BookEntity> bookEntityList = bookEntityRepository.findAll();
        assertThat(bookEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBookEntity() throws Exception {
        int databaseSizeBeforeUpdate = bookEntityRepository.findAll().size();
        bookEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookEntityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookEntity)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookEntity in the database
        List<BookEntity> bookEntityList = bookEntityRepository.findAll();
        assertThat(bookEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookEntityWithPatch() throws Exception {
        // Initialize the database
        bookEntityRepository.saveAndFlush(bookEntity);

        int databaseSizeBeforeUpdate = bookEntityRepository.findAll().size();

        // Update the bookEntity using partial update
        BookEntity partialUpdatedBookEntity = new BookEntity();
        partialUpdatedBookEntity.setId(bookEntity.getId());

        partialUpdatedBookEntity
            .status(UPDATED_STATUS)
            .borrowDate(UPDATED_BORROW_DATE)
            .createUser(UPDATED_CREATE_USER)
            .createDate(UPDATED_CREATE_DATE)
            .updateDate(UPDATED_UPDATE_DATE)
            .isActive(UPDATED_IS_ACTIVE);

        restBookEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBookEntity))
            )
            .andExpect(status().isOk());

        // Validate the BookEntity in the database
        List<BookEntity> bookEntityList = bookEntityRepository.findAll();
        assertThat(bookEntityList).hasSize(databaseSizeBeforeUpdate);
        BookEntity testBookEntity = bookEntityList.get(bookEntityList.size() - 1);
        assertThat(testBookEntity.getBookCode()).isEqualTo(DEFAULT_BOOK_CODE);
        assertThat(testBookEntity.getBookName()).isEqualTo(DEFAULT_BOOK_NAME);
        assertThat(testBookEntity.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testBookEntity.getPublisher()).isEqualTo(DEFAULT_PUBLISHER);
        assertThat(testBookEntity.getBookCategory()).isEqualTo(DEFAULT_BOOK_CATEGORY);
        assertThat(testBookEntity.getInputDate()).isEqualTo(DEFAULT_INPUT_DATE);
        assertThat(testBookEntity.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBookEntity.getBorrower()).isEqualTo(DEFAULT_BORROWER);
        assertThat(testBookEntity.getBorrowDate()).isEqualTo(UPDATED_BORROW_DATE);
        assertThat(testBookEntity.getIntendedReturnDate()).isEqualTo(DEFAULT_INTENDED_RETURN_DATE);
        assertThat(testBookEntity.getCreateUser()).isEqualTo(UPDATED_CREATE_USER);
        assertThat(testBookEntity.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testBookEntity.getUpdateUser()).isEqualTo(DEFAULT_UPDATE_USER);
        assertThat(testBookEntity.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
        assertThat(testBookEntity.getBookImage()).isEqualTo(DEFAULT_BOOK_IMAGE);
        assertThat(testBookEntity.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateBookEntityWithPatch() throws Exception {
        // Initialize the database
        bookEntityRepository.saveAndFlush(bookEntity);

        int databaseSizeBeforeUpdate = bookEntityRepository.findAll().size();

        // Update the bookEntity using partial update
        BookEntity partialUpdatedBookEntity = new BookEntity();
        partialUpdatedBookEntity.setId(bookEntity.getId());

        partialUpdatedBookEntity
            .bookCode(UPDATED_BOOK_CODE)
            .bookName(UPDATED_BOOK_NAME)
            .author(UPDATED_AUTHOR)
            .publisher(UPDATED_PUBLISHER)
            .bookCategory(UPDATED_BOOK_CATEGORY)
            .inputDate(UPDATED_INPUT_DATE)
            .status(UPDATED_STATUS)
            .borrower(UPDATED_BORROWER)
            .borrowDate(UPDATED_BORROW_DATE)
            .intendedReturnDate(UPDATED_INTENDED_RETURN_DATE)
            .createUser(UPDATED_CREATE_USER)
            .createDate(UPDATED_CREATE_DATE)
            .updateUser(UPDATED_UPDATE_USER)
            .updateDate(UPDATED_UPDATE_DATE)
            .bookImage(UPDATED_BOOK_IMAGE)
            .isActive(UPDATED_IS_ACTIVE);

        restBookEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBookEntity))
            )
            .andExpect(status().isOk());

        // Validate the BookEntity in the database
        List<BookEntity> bookEntityList = bookEntityRepository.findAll();
        assertThat(bookEntityList).hasSize(databaseSizeBeforeUpdate);
        BookEntity testBookEntity = bookEntityList.get(bookEntityList.size() - 1);
        assertThat(testBookEntity.getBookCode()).isEqualTo(UPDATED_BOOK_CODE);
        assertThat(testBookEntity.getBookName()).isEqualTo(UPDATED_BOOK_NAME);
        assertThat(testBookEntity.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testBookEntity.getPublisher()).isEqualTo(UPDATED_PUBLISHER);
        assertThat(testBookEntity.getBookCategory()).isEqualTo(UPDATED_BOOK_CATEGORY);
        assertThat(testBookEntity.getInputDate()).isEqualTo(UPDATED_INPUT_DATE);
        assertThat(testBookEntity.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBookEntity.getBorrower()).isEqualTo(UPDATED_BORROWER);
        assertThat(testBookEntity.getBorrowDate()).isEqualTo(UPDATED_BORROW_DATE);
        assertThat(testBookEntity.getIntendedReturnDate()).isEqualTo(UPDATED_INTENDED_RETURN_DATE);
        assertThat(testBookEntity.getCreateUser()).isEqualTo(UPDATED_CREATE_USER);
        assertThat(testBookEntity.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testBookEntity.getUpdateUser()).isEqualTo(UPDATED_UPDATE_USER);
        assertThat(testBookEntity.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
        assertThat(testBookEntity.getBookImage()).isEqualTo(UPDATED_BOOK_IMAGE);
        assertThat(testBookEntity.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingBookEntity() throws Exception {
        int databaseSizeBeforeUpdate = bookEntityRepository.findAll().size();
        bookEntity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bookEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookEntity in the database
        List<BookEntity> bookEntityList = bookEntityRepository.findAll();
        assertThat(bookEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBookEntity() throws Exception {
        int databaseSizeBeforeUpdate = bookEntityRepository.findAll().size();
        bookEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bookEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookEntity in the database
        List<BookEntity> bookEntityList = bookEntityRepository.findAll();
        assertThat(bookEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBookEntity() throws Exception {
        int databaseSizeBeforeUpdate = bookEntityRepository.findAll().size();
        bookEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookEntityMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bookEntity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookEntity in the database
        List<BookEntity> bookEntityList = bookEntityRepository.findAll();
        assertThat(bookEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBookEntity() throws Exception {
        // Initialize the database
        bookEntityRepository.saveAndFlush(bookEntity);

        int databaseSizeBeforeDelete = bookEntityRepository.findAll().size();

        // Delete the bookEntity
        restBookEntityMockMvc
            .perform(delete(ENTITY_API_URL_ID, bookEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BookEntity> bookEntityList = bookEntityRepository.findAll();
        assertThat(bookEntityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
