package com.example.pumb.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="animals", schema="data")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Integer id;

    @Column(name="name")
    private String name;

    @Column(name="type")
    private String type;

    @Column(name="sex")
    private String sex;

    @Column(name="weight")
    private Short weight;

    @Column(name="cost")
    private Short cost;

    @Column(name="category")
    private Byte category;

    public Animal(String name, String type, String sex, Short weight, Short cost) {
        this.name = name;
        this.type = type;
        this.sex = sex;
        this.weight = weight;
        this.cost = cost;
    }
}
