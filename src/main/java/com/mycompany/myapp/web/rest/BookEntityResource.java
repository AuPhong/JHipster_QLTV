package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.BookEntity;
import com.mycompany.myapp.repository.BookEntityRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.BookEntity}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BookEntityResource {

    private final Logger log = LoggerFactory.getLogger(BookEntityResource.class);

    private static final String ENTITY_NAME = "bookEntity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookEntityRepository bookEntityRepository;

    public BookEntityResource(BookEntityRepository bookEntityRepository) {
        this.bookEntityRepository = bookEntityRepository;
    }

    /**
     * {@code POST  /book-entities} : Create a new bookEntity.
     *
     * @param bookEntity the bookEntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookEntity, or with status {@code 400 (Bad Request)} if the bookEntity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/book-entities")
    public ResponseEntity<BookEntity> createBookEntity(@Valid @RequestBody BookEntity bookEntity) throws URISyntaxException {
        log.debug("REST request to save BookEntity : {}", bookEntity);
        if (bookEntity.getId() != null) {
            throw new BadRequestAlertException("A new bookEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BookEntity result = bookEntityRepository.save(bookEntity);
        return ResponseEntity
            .created(new URI("/api/book-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /book-entities/:id} : Updates an existing bookEntity.
     *
     * @param id the id of the bookEntity to save.
     * @param bookEntity the bookEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookEntity,
     * or with status {@code 400 (Bad Request)} if the bookEntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/book-entities/{id}")
    public ResponseEntity<BookEntity> updateBookEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BookEntity bookEntity
    ) throws URISyntaxException {
        log.debug("REST request to update BookEntity : {}, {}", id, bookEntity);
        if (bookEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BookEntity result = bookEntityRepository.save(bookEntity);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookEntity.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /book-entities/:id} : Partial updates given fields of an existing bookEntity, field will ignore if it is null
     *
     * @param id the id of the bookEntity to save.
     * @param bookEntity the bookEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookEntity,
     * or with status {@code 400 (Bad Request)} if the bookEntity is not valid,
     * or with status {@code 404 (Not Found)} if the bookEntity is not found,
     * or with status {@code 500 (Internal Server Error)} if the bookEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/book-entities/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<BookEntity> partialUpdateBookEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BookEntity bookEntity
    ) throws URISyntaxException {
        log.debug("REST request to partial update BookEntity partially : {}, {}", id, bookEntity);
        if (bookEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BookEntity> result = bookEntityRepository
            .findById(bookEntity.getId())
            .map(
                existingBookEntity -> {
                    if (bookEntity.getBookCode() != null) {
                        existingBookEntity.setBookCode(bookEntity.getBookCode());
                    }
                    if (bookEntity.getBookName() != null) {
                        existingBookEntity.setBookName(bookEntity.getBookName());
                    }
                    if (bookEntity.getAuthor() != null) {
                        existingBookEntity.setAuthor(bookEntity.getAuthor());
                    }
                    if (bookEntity.getPublisher() != null) {
                        existingBookEntity.setPublisher(bookEntity.getPublisher());
                    }
                    if (bookEntity.getBookCategory() != null) {
                        existingBookEntity.setBookCategory(bookEntity.getBookCategory());
                    }
                    if (bookEntity.getInputDate() != null) {
                        existingBookEntity.setInputDate(bookEntity.getInputDate());
                    }
                    if (bookEntity.getStatus() != null) {
                        existingBookEntity.setStatus(bookEntity.getStatus());
                    }
                    if (bookEntity.getBorrower() != null) {
                        existingBookEntity.setBorrower(bookEntity.getBorrower());
                    }
                    if (bookEntity.getBorrowDate() != null) {
                        existingBookEntity.setBorrowDate(bookEntity.getBorrowDate());
                    }
                    if (bookEntity.getIntendedReturnDate() != null) {
                        existingBookEntity.setIntendedReturnDate(bookEntity.getIntendedReturnDate());
                    }
                    if (bookEntity.getCreateUser() != null) {
                        existingBookEntity.setCreateUser(bookEntity.getCreateUser());
                    }
                    if (bookEntity.getCreateDate() != null) {
                        existingBookEntity.setCreateDate(bookEntity.getCreateDate());
                    }
                    if (bookEntity.getUpdateUser() != null) {
                        existingBookEntity.setUpdateUser(bookEntity.getUpdateUser());
                    }
                    if (bookEntity.getUpdateDate() != null) {
                        existingBookEntity.setUpdateDate(bookEntity.getUpdateDate());
                    }
                    if (bookEntity.getBookImage() != null) {
                        existingBookEntity.setBookImage(bookEntity.getBookImage());
                    }
                    if (bookEntity.getIsActive() != null) {
                        existingBookEntity.setIsActive(bookEntity.getIsActive());
                    }

                    return existingBookEntity;
                }
            )
            .map(bookEntityRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookEntity.getId().toString())
        );
    }

    /**
     * {@code GET  /book-entities} : get all the bookEntities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookEntities in body.
     */
    @GetMapping("/book-entities")
    public List<BookEntity> getAllBookEntities() {
        log.debug("REST request to get all BookEntities");
        return bookEntityRepository.findAll();
    }

    /**
     * {@code GET  /book-entities/:id} : get the "id" bookEntity.
     *
     * @param id the id of the bookEntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookEntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/book-entities/{id}")
    public ResponseEntity<BookEntity> getBookEntity(@PathVariable Long id) {
        log.debug("REST request to get BookEntity : {}", id);
        Optional<BookEntity> bookEntity = bookEntityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(bookEntity);
    }

    /**
     * {@code DELETE  /book-entities/:id} : delete the "id" bookEntity.
     *
     * @param id the id of the bookEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/book-entities/{id}")
    public ResponseEntity<Void> deleteBookEntity(@PathVariable Long id) {
        log.debug("REST request to delete BookEntity : {}", id);
        bookEntityRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
