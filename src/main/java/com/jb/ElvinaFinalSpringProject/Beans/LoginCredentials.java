package com.jb.ElvinaFinalSpringProject.Beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 * Object to store data of login credentials
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginCredentials {
    String email;
    String password;
}
