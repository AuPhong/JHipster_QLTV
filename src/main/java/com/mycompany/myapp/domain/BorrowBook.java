package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;

/**
 * A BorrowBook.
 */
@Entity
@Table(name = "borrow_book")
public class BorrowBook implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "emp_code")
    private Long empCode;

    @Column(name = "book_code")
    private String bookCode;

    @Column(name = "status")
    private String status;

    @Column(name = "borrow_date")
    private LocalDate borrowDate;

    @Column(name = "intended_return_date")
    private LocalDate intendedReturnDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "number_of_expiration_date")
    private Long numberOfExpirationDate;

    @Column(name = "book_category")
    private Long bookCategory;

    @Column(name = "create_user")
    private Long createUser;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "update_user")
    private Long updateUser;

    @Column(name = "update_date")
    private Instant updateDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BorrowBook id(Long id) {
        this.id = id;
        return this;
    }

    public Long getEmpCode() {
        return this.empCode;
    }

    public BorrowBook empCode(Long empCode) {
        this.empCode = empCode;
        return this;
    }

    public void setEmpCode(Long empCode) {
        this.empCode = empCode;
    }

    public String getBookCode() {
        return this.bookCode;
    }

    public BorrowBook bookCode(String bookCode) {
        this.bookCode = bookCode;
        return this;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    public String getStatus() {
        return this.status;
    }

    public BorrowBook status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getBorrowDate() {
        return this.borrowDate;
    }

    public BorrowBook borrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
        return this;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getIntendedReturnDate() {
        return this.intendedReturnDate;
    }

    public BorrowBook intendedReturnDate(LocalDate intendedReturnDate) {
        this.intendedReturnDate = intendedReturnDate;
        return this;
    }

    public void setIntendedReturnDate(LocalDate intendedReturnDate) {
        this.intendedReturnDate = intendedReturnDate;
    }

    public LocalDate getReturnDate() {
        return this.returnDate;
    }

    public BorrowBook returnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
        return this;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public Long getNumberOfExpirationDate() {
        return this.numberOfExpirationDate;
    }

    public BorrowBook numberOfExpirationDate(Long numberOfExpirationDate) {
        this.numberOfExpirationDate = numberOfExpirationDate;
        return this;
    }

    public void setNumberOfExpirationDate(Long numberOfExpirationDate) {
        this.numberOfExpirationDate = numberOfExpirationDate;
    }

    public Long getBookCategory() {
        return this.bookCategory;
    }

    public BorrowBook bookCategory(Long bookCategory) {
        this.bookCategory = bookCategory;
        return this;
    }

    public void setBookCategory(Long bookCategory) {
        this.bookCategory = bookCategory;
    }

    public Long getCreateUser() {
        return this.createUser;
    }

    public BorrowBook createUser(Long createUser) {
        this.createUser = createUser;
        return this;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public BorrowBook createDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Long getUpdateUser() {
        return this.updateUser;
    }

    public BorrowBook updateUser(Long updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public Instant getUpdateDate() {
        return this.updateDate;
    }

    public BorrowBook updateDate(Instant updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public void setUpdateDate(Instant updateDate) {
        this.updateDate = updateDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BorrowBook)) {
            return false;
        }
        return id != null && id.equals(((BorrowBook) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BorrowBook{" +
            "id=" + getId() +
            ", empCode=" + getEmpCode() +
            ", bookCode='" + getBookCode() + "'" +
            ", status='" + getStatus() + "'" +
            ", borrowDate='" + getBorrowDate() + "'" +
            ", intendedReturnDate='" + getIntendedReturnDate() + "'" +
            ", returnDate='" + getReturnDate() + "'" +
            ", numberOfExpirationDate=" + getNumberOfExpirationDate() +
            ", bookCategory=" + getBookCategory() +
            ", createUser=" + getCreateUser() +
            ", createDate='" + getCreateDate() + "'" +
            ", updateUser=" + getUpdateUser() +
            ", updateDate='" + getUpdateDate() + "'" +
            "}";
    }
}
