package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Emp;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Emp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmpRepository extends JpaRepository<Emp, Long> {}
