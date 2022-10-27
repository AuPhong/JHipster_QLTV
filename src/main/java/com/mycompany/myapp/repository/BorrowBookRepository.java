package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BorrowBook;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BorrowBook entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BorrowBookRepository extends JpaRepository<BorrowBook, Long> {}
