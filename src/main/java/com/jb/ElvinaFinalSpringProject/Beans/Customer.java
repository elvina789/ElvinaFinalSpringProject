package com.jb.ElvinaFinalSpringProject.Beans;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/***
 * Object to store data of customer
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @ManyToMany(cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude // TODO - FIX ME
    @Singular
    private List<Coupon> coupons = new ArrayList<>();
}
