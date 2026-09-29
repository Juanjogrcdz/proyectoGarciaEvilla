package com.plazoleta.infrastructure.output.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String description;
    
    // Getters y Setters...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}