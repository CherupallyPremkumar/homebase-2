package com.homebase.ecom.cms.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

import java.util.Date;

@Entity
@Table(name = "cms_pages")
public class CmsPageEntity extends BaseJpaEntity {

    @Column(name = "slug", unique = true, nullable = false)
    private String slug;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "body", columnDefinition = "TEXT")
    private String body;

    @Column(name = "page_type")
    private String pageType;

    @Column(name = "meta_title")
    private String metaTitle;

    @Column(name = "meta_description")
    private String metaDescription;

    @Column(name = "published")
    private boolean published;

    @Column(name = "published_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date publishedAt;

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }
}
