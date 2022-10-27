package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BorrowBookTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BorrowBook.class);
        BorrowBook borrowBook1 = new BorrowBook();
        borrowBook1.setId(1L);
        BorrowBook borrowBook2 = new BorrowBook();
        borrowBook2.setId(borrowBook1.getId());
        assertThat(borrowBook1).isEqualTo(borrowBook2);
        borrowBook2.setId(2L);
        assertThat(borrowBook1).isNotEqualTo(borrowBook2);
        borrowBook1.setId(null);
        assertThat(borrowBook1).isNotEqualTo(borrowBook2);
    }
}
