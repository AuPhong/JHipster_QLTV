package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Emp;
import com.mycompany.myapp.repository.EmpRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EmpResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmpResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final Long DEFAULT_DEPARTMENT = 1L;
    private static final Long UPDATED_DEPARTMENT = 2L;

    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Instant DEFAULT_SYN_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SYN_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_PROVINCE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PROVINCE_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/emps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmpRepository empRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmpMockMvc;

    private Emp emp;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Emp createEntity(EntityManager em) {
        Emp emp = new Emp()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .department(DEFAULT_DEPARTMENT)
            .position(DEFAULT_POSITION)
            .status(DEFAULT_STATUS)
            .synDate(DEFAULT_SYN_DATE)
            .provinceCode(DEFAULT_PROVINCE_CODE);
        return emp;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Emp createUpdatedEntity(EntityManager em) {
        Emp emp = new Emp()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .department(UPDATED_DEPARTMENT)
            .position(UPDATED_POSITION)
            .status(UPDATED_STATUS)
            .synDate(UPDATED_SYN_DATE)
            .provinceCode(UPDATED_PROVINCE_CODE);
        return emp;
    }

    @BeforeEach
    public void initTest() {
        emp = createEntity(em);
    }

    @Test
    @Transactional
    void createEmp() throws Exception {
        int databaseSizeBeforeCreate = empRepository.findAll().size();
        // Create the Emp
        restEmpMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emp)))
            .andExpect(status().isCreated());

        // Validate the Emp in the database
        List<Emp> empList = empRepository.findAll();
        assertThat(empList).hasSize(databaseSizeBeforeCreate + 1);
        Emp testEmp = empList.get(empList.size() - 1);
        assertThat(testEmp.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testEmp.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEmp.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEmp.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testEmp.getDepartment()).isEqualTo(DEFAULT_DEPARTMENT);
        assertThat(testEmp.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testEmp.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testEmp.getSynDate()).isEqualTo(DEFAULT_SYN_DATE);
        assertThat(testEmp.getProvinceCode()).isEqualTo(DEFAULT_PROVINCE_CODE);
    }

    @Test
    @Transactional
    void createEmpWithExistingId() throws Exception {
        // Create the Emp with an existing ID
        emp.setId(1L);

        int databaseSizeBeforeCreate = empRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmpMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emp)))
            .andExpect(status().isBadRequest());

        // Validate the Emp in the database
        List<Emp> empList = empRepository.findAll();
        assertThat(empList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmps() throws Exception {
        // Initialize the database
        empRepository.saveAndFlush(emp);

        // Get all the empList
        restEmpMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emp.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT.intValue())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].synDate").value(hasItem(DEFAULT_SYN_DATE.toString())))
            .andExpect(jsonPath("$.[*].provinceCode").value(hasItem(DEFAULT_PROVINCE_CODE)));
    }

    @Test
    @Transactional
    void getEmp() throws Exception {
        // Initialize the database
        empRepository.saveAndFlush(emp);

        // Get the emp
        restEmpMockMvc
            .perform(get(ENTITY_API_URL_ID, emp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(emp.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.department").value(DEFAULT_DEPARTMENT.intValue()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.synDate").value(DEFAULT_SYN_DATE.toString()))
            .andExpect(jsonPath("$.provinceCode").value(DEFAULT_PROVINCE_CODE));
    }

    @Test
    @Transactional
    void getNonExistingEmp() throws Exception {
        // Get the emp
        restEmpMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEmp() throws Exception {
        // Initialize the database
        empRepository.saveAndFlush(emp);

        int databaseSizeBeforeUpdate = empRepository.findAll().size();

        // Update the emp
        Emp updatedEmp = empRepository.findById(emp.getId()).get();
        // Disconnect from session so that the updates on updatedEmp are not directly saved in db
        em.detach(updatedEmp);
        updatedEmp
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .department(UPDATED_DEPARTMENT)
            .position(UPDATED_POSITION)
            .status(UPDATED_STATUS)
            .synDate(UPDATED_SYN_DATE)
            .provinceCode(UPDATED_PROVINCE_CODE);

        restEmpMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmp.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEmp))
            )
            .andExpect(status().isOk());

        // Validate the Emp in the database
        List<Emp> empList = empRepository.findAll();
        assertThat(empList).hasSize(databaseSizeBeforeUpdate);
        Emp testEmp = empList.get(empList.size() - 1);
        assertThat(testEmp.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testEmp.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEmp.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmp.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testEmp.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
        assertThat(testEmp.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testEmp.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testEmp.getSynDate()).isEqualTo(UPDATED_SYN_DATE);
        assertThat(testEmp.getProvinceCode()).isEqualTo(UPDATED_PROVINCE_CODE);
    }

    @Test
    @Transactional
    void putNonExistingEmp() throws Exception {
        int databaseSizeBeforeUpdate = empRepository.findAll().size();
        emp.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmpMockMvc
            .perform(
                put(ENTITY_API_URL_ID, emp.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emp))
            )
            .andExpect(status().isBadRequest());

        // Validate the Emp in the database
        List<Emp> empList = empRepository.findAll();
        assertThat(empList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmp() throws Exception {
        int databaseSizeBeforeUpdate = empRepository.findAll().size();
        emp.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emp))
            )
            .andExpect(status().isBadRequest());

        // Validate the Emp in the database
        List<Emp> empList = empRepository.findAll();
        assertThat(empList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmp() throws Exception {
        int databaseSizeBeforeUpdate = empRepository.findAll().size();
        emp.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emp)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Emp in the database
        List<Emp> empList = empRepository.findAll();
        assertThat(empList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmpWithPatch() throws Exception {
        // Initialize the database
        empRepository.saveAndFlush(emp);

        int databaseSizeBeforeUpdate = empRepository.findAll().size();

        // Update the emp using partial update
        Emp partialUpdatedEmp = new Emp();
        partialUpdatedEmp.setId(emp.getId());

        partialUpdatedEmp.code(UPDATED_CODE).email(UPDATED_EMAIL).position(UPDATED_POSITION);

        restEmpMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmp.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmp))
            )
            .andExpect(status().isOk());

        // Validate the Emp in the database
        List<Emp> empList = empRepository.findAll();
        assertThat(empList).hasSize(databaseSizeBeforeUpdate);
        Emp testEmp = empList.get(empList.size() - 1);
        assertThat(testEmp.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testEmp.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEmp.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmp.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testEmp.getDepartment()).isEqualTo(DEFAULT_DEPARTMENT);
        assertThat(testEmp.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testEmp.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testEmp.getSynDate()).isEqualTo(DEFAULT_SYN_DATE);
        assertThat(testEmp.getProvinceCode()).isEqualTo(DEFAULT_PROVINCE_CODE);
    }

    @Test
    @Transactional
    void fullUpdateEmpWithPatch() throws Exception {
        // Initialize the database
        empRepository.saveAndFlush(emp);

        int databaseSizeBeforeUpdate = empRepository.findAll().size();

        // Update the emp using partial update
        Emp partialUpdatedEmp = new Emp();
        partialUpdatedEmp.setId(emp.getId());

        partialUpdatedEmp
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .department(UPDATED_DEPARTMENT)
            .position(UPDATED_POSITION)
            .status(UPDATED_STATUS)
            .synDate(UPDATED_SYN_DATE)
            .provinceCode(UPDATED_PROVINCE_CODE);

        restEmpMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmp.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmp))
            )
            .andExpect(status().isOk());

        // Validate the Emp in the database
        List<Emp> empList = empRepository.findAll();
        assertThat(empList).hasSize(databaseSizeBeforeUpdate);
        Emp testEmp = empList.get(empList.size() - 1);
        assertThat(testEmp.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testEmp.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEmp.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmp.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testEmp.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
        assertThat(testEmp.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testEmp.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testEmp.getSynDate()).isEqualTo(UPDATED_SYN_DATE);
        assertThat(testEmp.getProvinceCode()).isEqualTo(UPDATED_PROVINCE_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingEmp() throws Exception {
        int databaseSizeBeforeUpdate = empRepository.findAll().size();
        emp.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmpMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, emp.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(emp))
            )
            .andExpect(status().isBadRequest());

        // Validate the Emp in the database
        List<Emp> empList = empRepository.findAll();
        assertThat(empList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmp() throws Exception {
        int databaseSizeBeforeUpdate = empRepository.findAll().size();
        emp.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(emp))
            )
            .andExpect(status().isBadRequest());

        // Validate the Emp in the database
        List<Emp> empList = empRepository.findAll();
        assertThat(empList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmp() throws Exception {
        int databaseSizeBeforeUpdate = empRepository.findAll().size();
        emp.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(emp)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Emp in the database
        List<Emp> empList = empRepository.findAll();
        assertThat(empList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmp() throws Exception {
        // Initialize the database
        empRepository.saveAndFlush(emp);

        int databaseSizeBeforeDelete = empRepository.findAll().size();

        // Delete the emp
        restEmpMockMvc.perform(delete(ENTITY_API_URL_ID, emp.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Emp> empList = empRepository.findAll();
        assertThat(empList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
