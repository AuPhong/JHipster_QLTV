package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

/**
 * A Emp.
 */
@Entity
@Table(name = "emp")
public class Emp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "department")
    private Long department;

    @Column(name = "position")
    private String position;

    @Column(name = "status")
    private String status;

    @Column(name = "syn_date")
    private Instant synDate;

    @Column(name = "province_code")
    private String provinceCode;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Emp id(Long id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public Emp code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public Emp name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public Emp email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public Emp phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getDepartment() {
        return this.department;
    }

    public Emp department(Long department) {
        this.department = department;
        return this;
    }

    public void setDepartment(Long department) {
        this.department = department;
    }

    public String getPosition() {
        return this.position;
    }

    public Emp position(String position) {
        this.position = position;
        return this;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStatus() {
        return this.status;
    }

    public Emp status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getSynDate() {
        return this.synDate;
    }

    public Emp synDate(Instant synDate) {
        this.synDate = synDate;
        return this;
    }

    public void setSynDate(Instant synDate) {
        this.synDate = synDate;
    }

    public String getProvinceCode() {
        return this.provinceCode;
    }

    public Emp provinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
        return this;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Emp)) {
            return false;
        }
        return id != null && id.equals(((Emp) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Emp{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", department=" + getDepartment() +
            ", position='" + getPosition() + "'" +
            ", status='" + getStatus() + "'" +
            ", synDate='" + getSynDate() + "'" +
            ", provinceCode='" + getProvinceCode() + "'" +
            "}";
    }
}
