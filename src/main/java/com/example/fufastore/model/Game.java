package com.example.fufastore.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "game")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "publisher", length = 100)
    private String publisher;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "status", columnDefinition = "boolean default true")
    private Boolean status;

    @Column(name = "slug", length = 100)
    private String slug;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_path")
    private String imagePath;

    @Lob
    @Column(name = "image_title")
    private byte[] imageTitle;

    @Column(name = "image_title_path")
    private String imageTitlePath;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public byte[] getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(byte[] imageTitle) {
        this.imageTitle = imageTitle;
    }

    public String getImageTitlePath() {
        return imageTitlePath;
    }

    public void setImageTitlePath(String imageTitlePath) {
        this.imageTitlePath = imageTitlePath;
    }

}
