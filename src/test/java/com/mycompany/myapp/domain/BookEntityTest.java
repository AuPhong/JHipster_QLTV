package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookEntity.class);
        BookEntity bookEntity1 = new BookEntity();
        bookEntity1.setId(1L);
        BookEntity bookEntity2 = new BookEntity();
        bookEntity2.setId(bookEntity1.getId());
        assertThat(bookEntity1).isEqualTo(bookEntity2);
        bookEntity2.setId(2L);
        assertThat(bookEntity1).isNotEqualTo(bookEntity2);
        bookEntity1.setId(null);
        assertThat(bookEntity1).isNotEqualTo(bookEntity2);
    }
}
