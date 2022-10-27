package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.BorrowBook;
import com.mycompany.myapp.repository.BorrowBookRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.BorrowBook}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BorrowBookResource {

    private final Logger log = LoggerFactory.getLogger(BorrowBookResource.class);

    private static final String ENTITY_NAME = "borrowBook";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BorrowBookRepository borrowBookRepository;

    public BorrowBookResource(BorrowBookRepository borrowBookRepository) {
        this.borrowBookRepository = borrowBookRepository;
    }

    /**
     * {@code POST  /borrow-books} : Create a new borrowBook.
     *
     * @param borrowBook the borrowBook to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new borrowBook, or with status {@code 400 (Bad Request)} if the borrowBook has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/borrow-books")
    public ResponseEntity<BorrowBook> createBorrowBook(@RequestBody BorrowBook borrowBook) throws URISyntaxException {
        log.debug("REST request to save BorrowBook : {}", borrowBook);
        if (borrowBook.getId() != null) {
            throw new BadRequestAlertException("A new borrowBook cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BorrowBook result = borrowBookRepository.save(borrowBook);
        return ResponseEntity
            .created(new URI("/api/borrow-books/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /borrow-books/:id} : Updates an existing borrowBook.
     *
     * @param id the id of the borrowBook to save.
     * @param borrowBook the borrowBook to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated borrowBook,
     * or with status {@code 400 (Bad Request)} if the borrowBook is not valid,
     * or with status {@code 500 (Internal Server Error)} if the borrowBook couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/borrow-books/{id}")
    public ResponseEntity<BorrowBook> updateBorrowBook(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BorrowBook borrowBook
    ) throws URISyntaxException {
        log.debug("REST request to update BorrowBook : {}, {}", id, borrowBook);
        if (borrowBook.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, borrowBook.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!borrowBookRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BorrowBook result = borrowBookRepository.save(borrowBook);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, borrowBook.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /borrow-books/:id} : Partial updates given fields of an existing borrowBook, field will ignore if it is null
     *
     * @param id the id of the borrowBook to save.
     * @param borrowBook the borrowBook to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated borrowBook,
     * or with status {@code 400 (Bad Request)} if the borrowBook is not valid,
     * or with status {@code 404 (Not Found)} if the borrowBook is not found,
     * or with status {@code 500 (Internal Server Error)} if the borrowBook couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/borrow-books/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<BorrowBook> partialUpdateBorrowBook(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BorrowBook borrowBook
    ) throws URISyntaxException {
        log.debug("REST request to partial update BorrowBook partially : {}, {}", id, borrowBook);
        if (borrowBook.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, borrowBook.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!borrowBookRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BorrowBook> result = borrowBookRepository
            .findById(borrowBook.getId())
            .map(
                existingBorrowBook -> {
                    if (borrowBook.getEmpCode() != null) {
                        existingBorrowBook.setEmpCode(borrowBook.getEmpCode());
                    }
                    if (borrowBook.getBookCode() != null) {
                        existingBorrowBook.setBookCode(borrowBook.getBookCode());
                    }
                    if (borrowBook.getStatus() != null) {
                        existingBorrowBook.setStatus(borrowBook.getStatus());
                    }
                    if (borrowBook.getBorrowDate() != null) {
                        existingBorrowBook.setBorrowDate(borrowBook.getBorrowDate());
                    }
                    if (borrowBook.getIntendedReturnDate() != null) {
                        existingBorrowBook.setIntendedReturnDate(borrowBook.getIntendedReturnDate());
                    }
                    if (borrowBook.getReturnDate() != null) {
                        existingBorrowBook.setReturnDate(borrowBook.getReturnDate());
                    }
                    if (borrowBook.getNumberOfExpirationDate() != null) {
                        existingBorrowBook.setNumberOfExpirationDate(borrowBook.getNumberOfExpirationDate());
                    }
                    if (borrowBook.getBookCategory() != null) {
                        existingBorrowBook.setBookCategory(borrowBook.getBookCategory());
                    }
                    if (borrowBook.getCreateUser() != null) {
                        existingBorrowBook.setCreateUser(borrowBook.getCreateUser());
                    }
                    if (borrowBook.getCreateDate() != null) {
                        existingBorrowBook.setCreateDate(borrowBook.getCreateDate());
                    }
                    if (borrowBook.getUpdateUser() != null) {
                        existingBorrowBook.setUpdateUser(borrowBook.getUpdateUser());
                    }
                    if (borrowBook.getUpdateDate() != null) {
                        existingBorrowBook.setUpdateDate(borrowBook.getUpdateDate());
                    }

                    return existingBorrowBook;
                }
            )
            .map(borrowBookRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, borrowBook.getId().toString())
        );
    }

    /**
     * {@code GET  /borrow-books} : get all the borrowBooks.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of borrowBooks in body.
     */
    @GetMapping("/borrow-books")
    public List<BorrowBook> getAllBorrowBooks() {
        log.debug("REST request to get all BorrowBooks");
        return borrowBookRepository.findAll();
    }

    /**
     * {@code GET  /borrow-books/:id} : get the "id" borrowBook.
     *
     * @param id the id of the borrowBook to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the borrowBook, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/borrow-books/{id}")
    public ResponseEntity<BorrowBook> getBorrowBook(@PathVariable Long id) {
        log.debug("REST request to get BorrowBook : {}", id);
        Optional<BorrowBook> borrowBook = borrowBookRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(borrowBook);
    }

    /**
     * {@code DELETE  /borrow-books/:id} : delete the "id" borrowBook.
     *
     * @param id the id of the borrowBook to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/borrow-books/{id}")
    public ResponseEntity<Void> deleteBorrowBook(@PathVariable Long id) {
        log.debug("REST request to delete BorrowBook : {}", id);
        borrowBookRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
