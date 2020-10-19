package com.service.micronaut.users;

import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Builder
@Table(name = "users")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    private String phone;
}
