package com.example.vaccinebookingapp.models;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
@Table(name = "slots")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "slotId"
)
public class Slot {

    @Id
    @GeneratedValue
    private Integer slotId;

    // first thing refers to class, second thing refers to other class
    @NotNull
    @ManyToOne
    @JoinColumn(name = "location_id")
//    @JsonBackReference
    private Location location;

    @NotNull
    private Date date;

    @Enumerated(EnumType.STRING)
    private Vaccine vaccine;

    @NotNull
    @Min(1)
    private Integer count;

    @Enumerated(EnumType.STRING)
    private AgeLimit ageLimit;


}
