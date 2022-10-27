package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BookEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BookEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookEntityRepository extends JpaRepository<BookEntity, Long> {}
