package com.jb.ElvinaFinalSpringProject.Beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/***
 * Object to store data of the token record for logged in entity
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Session {
    @Id
    @Column(length = 40)
    private String token;
    private long expirationDate;
    private int beanId;
    private int clientType;
}
