package com.uc.ms_security.entity;

import jakarta.persistence.*; // Validación de datos y palabras especiales de configuración de base de datos
// Con LOMBOK no hace falta crear Setters, Getters o constructor, ya que los genera en tiempo de ejecución
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Column(
            nullable = false,
            length = 100
    )
    private String name;

    @Column(
            nullable = false,
            unique = true,
            length = 150
    )
    private String email;

    @Column(
            nullable = false
    )
    private String password;
}