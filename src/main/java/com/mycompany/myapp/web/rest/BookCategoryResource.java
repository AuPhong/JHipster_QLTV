package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.BookCategory;
import com.mycompany.myapp.repository.BookCategoryRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.BookCategory}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BookCategoryResource {

    private final Logger log = LoggerFactory.getLogger(BookCategoryResource.class);

    private static final String ENTITY_NAME = "bookCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookCategoryRepository bookCategoryRepository;

    public BookCategoryResource(BookCategoryRepository bookCategoryRepository) {
        this.bookCategoryRepository = bookCategoryRepository;
    }

    /**
     * {@code POST  /book-categories} : Create a new bookCategory.
     *
     * @param bookCategory the bookCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookCategory, or with status {@code 400 (Bad Request)} if the bookCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/book-categories")
    public ResponseEntity<BookCategory> createBookCategory(@RequestBody BookCategory bookCategory) throws URISyntaxException {
        log.debug("REST request to save BookCategory : {}", bookCategory);
        if (bookCategory.getId() != null) {
            throw new BadRequestAlertException("A new bookCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BookCategory result = bookCategoryRepository.save(bookCategory);
        return ResponseEntity
            .created(new URI("/api/book-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /book-categories/:id} : Updates an existing bookCategory.
     *
     * @param id the id of the bookCategory to save.
     * @param bookCategory the bookCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookCategory,
     * or with status {@code 400 (Bad Request)} if the bookCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/book-categories/{id}")
    public ResponseEntity<BookCategory> updateBookCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BookCategory bookCategory
    ) throws URISyntaxException {
        log.debug("REST request to update BookCategory : {}, {}", id, bookCategory);
        if (bookCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BookCategory result = bookCategoryRepository.save(bookCategory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookCategory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /book-categories/:id} : Partial updates given fields of an existing bookCategory, field will ignore if it is null
     *
     * @param id the id of the bookCategory to save.
     * @param bookCategory the bookCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookCategory,
     * or with status {@code 400 (Bad Request)} if the bookCategory is not valid,
     * or with status {@code 404 (Not Found)} if the bookCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the bookCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/book-categories/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<BookCategory> partialUpdateBookCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BookCategory bookCategory
    ) throws URISyntaxException {
        log.debug("REST request to partial update BookCategory partially : {}, {}", id, bookCategory);
        if (bookCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BookCategory> result = bookCategoryRepository
            .findById(bookCategory.getId())
            .map(
                existingBookCategory -> {
                    if (bookCategory.getCategoryName() != null) {
                        existingBookCategory.setCategoryName(bookCategory.getCategoryName());
                    }

                    return existingBookCategory;
                }
            )
            .map(bookCategoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookCategory.getId().toString())
        );
    }

    /**
     * {@code GET  /book-categories} : get all the bookCategories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookCategories in body.
     */
    @GetMapping("/book-categories")
    public List<BookCategory> getAllBookCategories() {
        log.debug("REST request to get all BookCategories");
        return bookCategoryRepository.findAll();
    }

    /**
     * {@code GET  /book-categories/:id} : get the "id" bookCategory.
     *
     * @param id the id of the bookCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/book-categories/{id}")
    public ResponseEntity<BookCategory> getBookCategory(@PathVariable Long id) {
        log.debug("REST request to get BookCategory : {}", id);
        Optional<BookCategory> bookCategory = bookCategoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(bookCategory);
    }

    /**
     * {@code DELETE  /book-categories/:id} : delete the "id" bookCategory.
     *
     * @param id the id of the bookCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/book-categories/{id}")
    public ResponseEntity<Void> deleteBookCategory(@PathVariable Long id) {
        log.debug("REST request to delete BookCategory : {}", id);
        bookCategoryRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
