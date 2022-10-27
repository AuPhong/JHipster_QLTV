package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmpTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Emp.class);
        Emp emp1 = new Emp();
        emp1.setId(1L);
        Emp emp2 = new Emp();
        emp2.setId(emp1.getId());
        assertThat(emp1).isEqualTo(emp2);
        emp2.setId(2L);
        assertThat(emp1).isNotEqualTo(emp2);
        emp1.setId(null);
        assertThat(emp1).isNotEqualTo(emp2);
    }
}
