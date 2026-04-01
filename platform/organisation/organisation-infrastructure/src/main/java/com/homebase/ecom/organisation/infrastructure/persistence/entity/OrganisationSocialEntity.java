package com.homebase.ecom.organisation.infrastructure.persistence.entity;

import org.chenile.jpautils.entity.BaseJpaEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "organisation_social")
public class OrganisationSocialEntity extends BaseJpaEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation_id", nullable = false)
    private OrganisationEntity organisation;

    @Column(name = "facebook_url") private String facebookUrl;
    @Column(name = "twitter_url") private String twitterUrl;
    @Column(name = "instagram_url") private String instagramUrl;
    @Column(name = "linkedin_url") private String linkedinUrl;
    @Column(name = "youtube_url") private String youtubeUrl;

    public OrganisationEntity getOrganisation() { return organisation; }
    public void setOrganisation(OrganisationEntity v) { this.organisation = v; }
    public String getFacebookUrl() { return facebookUrl; }
    public void setFacebookUrl(String v) { this.facebookUrl = v; }
    public String getTwitterUrl() { return twitterUrl; }
    public void setTwitterUrl(String v) { this.twitterUrl = v; }
    public String getInstagramUrl() { return instagramUrl; }
    public void setInstagramUrl(String v) { this.instagramUrl = v; }
    public String getLinkedinUrl() { return linkedinUrl; }
    public void setLinkedinUrl(String v) { this.linkedinUrl = v; }
    public String getYoutubeUrl() { return youtubeUrl; }
    public void setYoutubeUrl(String v) { this.youtubeUrl = v; }
}
