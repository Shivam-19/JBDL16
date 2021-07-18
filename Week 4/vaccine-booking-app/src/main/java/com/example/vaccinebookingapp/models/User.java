package com.example.vaccinebookingapp.models;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Integer userId;

    @NotNull
    private String name;

    @NotNull
    private String address;

    @NotNull
    private Date dateOfBirth;

    @Column(unique = true, nullable = false)
    private String governmentId;

    @Column(unique = true, nullable = false)
    @Size(min = 10, max = 10)
    private String phoneNumber;

    @Transient
    @Getter(AccessLevel.NONE)
    private Integer age;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    public Integer getAge() {
        return Period.between(this.dateOfBirth.toLocalDate(), LocalDate.now()).getYears();
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
