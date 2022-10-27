package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BookCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BookCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {}
