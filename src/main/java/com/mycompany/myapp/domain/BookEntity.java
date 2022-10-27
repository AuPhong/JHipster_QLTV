package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A BookEntity.
 */
@Entity
@Table(name = "book_entity")
public class BookEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "book_code", nullable = false)
    private String bookCode;

    @NotNull
    @Column(name = "book_name", nullable = false)
    private String bookName;

    @Column(name = "author")
    private String author;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "book_category")
    private Long bookCategory;

    @Column(name = "input_date")
    private Instant inputDate;

    @Column(name = "status")
    private String status;

    @Column(name = "borrower")
    private Long borrower;

    @Column(name = "borrow_date")
    private LocalDate borrowDate;

    @Column(name = "intended_return_date")
    private LocalDate intendedReturnDate;

    @Column(name = "create_user")
    private Long createUser;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "update_user")
    private Long updateUser;

    @Column(name = "update_date")
    private Instant updateDate;

    @Column(name = "book_image")
    private String bookImage;

    @Column(name = "is_active")
    private String isActive;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getBookCode() {
        return this.bookCode;
    }

    public BookEntity bookCode(String bookCode) {
        this.bookCode = bookCode;
        return this;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    public String getBookName() {
        return this.bookName;
    }

    public BookEntity bookName(String bookName) {
        this.bookName = bookName;
        return this;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return this.author;
    }

    public BookEntity author(String author) {
        this.author = author;
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public BookEntity publisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Long getBookCategory() {
        return this.bookCategory;
    }

    public BookEntity bookCategory(Long bookCategory) {
        this.bookCategory = bookCategory;
        return this;
    }

    public void setBookCategory(Long bookCategory) {
        this.bookCategory = bookCategory;
    }

    public Instant getInputDate() {
        return this.inputDate;
    }

    public BookEntity inputDate(Instant inputDate) {
        this.inputDate = inputDate;
        return this;
    }

    public void setInputDate(Instant inputDate) {
        this.inputDate = inputDate;
    }

    public String getStatus() {
        return this.status;
    }

    public BookEntity status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getBorrower() {
        return this.borrower;
    }

    public BookEntity borrower(Long borrower) {
        this.borrower = borrower;
        return this;
    }

    public void setBorrower(Long borrower) {
        this.borrower = borrower;
    }

    public LocalDate getBorrowDate() {
        return this.borrowDate;
    }

    public BookEntity borrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
        return this;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getIntendedReturnDate() {
        return this.intendedReturnDate;
    }

    public BookEntity intendedReturnDate(LocalDate intendedReturnDate) {
        this.intendedReturnDate = intendedReturnDate;
        return this;
    }

    public void setIntendedReturnDate(LocalDate intendedReturnDate) {
        this.intendedReturnDate = intendedReturnDate;
    }

    public Long getCreateUser() {
        return this.createUser;
    }

    public BookEntity createUser(Long createUser) {
        this.createUser = createUser;
        return this;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public BookEntity createDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Long getUpdateUser() {
        return this.updateUser;
    }

    public BookEntity updateUser(Long updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public Instant getUpdateDate() {
        return this.updateDate;
    }

    public BookEntity updateDate(Instant updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public void setUpdateDate(Instant updateDate) {
        this.updateDate = updateDate;
    }

    public String getBookImage() {
        return this.bookImage;
    }

    public BookEntity bookImage(String bookImage) {
        this.bookImage = bookImage;
        return this;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    public String getIsActive() {
        return this.isActive;
    }

    public BookEntity isActive(String isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookEntity)) {
            return false;
        }
        return id != null && id.equals(((BookEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookEntity{" +
            "id=" + getId() +
            ", bookCode='" + getBookCode() + "'" +
            ", bookName='" + getBookName() + "'" +
            ", author='" + getAuthor() + "'" +
            ", publisher='" + getPublisher() + "'" +
            ", bookCategory=" + getBookCategory() +
            ", inputDate='" + getInputDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", borrower=" + getBorrower() +
            ", borrowDate='" + getBorrowDate() + "'" +
            ", intendedReturnDate='" + getIntendedReturnDate() + "'" +
            ", createUser=" + getCreateUser() +
            ", createDate='" + getCreateDate() + "'" +
            ", updateUser=" + getUpdateUser() +
            ", updateDate='" + getUpdateDate() + "'" +
            ", bookImage='" + getBookImage() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
