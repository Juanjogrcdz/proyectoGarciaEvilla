package com.plazoleta.infrastructure.output.jpa.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "restaurants")
public class RestaurantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)   // dos restaurantes no pueden tener el mismo NIT
    private String nit;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, length = 13)
    private String phone;

    @Column(name = "logo_url", nullable = false)
    private String logoUrl;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
}