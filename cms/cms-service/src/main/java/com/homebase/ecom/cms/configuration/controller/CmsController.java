package com.homebase.ecom.cms.configuration.controller;

import com.homebase.ecom.cms.dto.BannerDto;
import com.homebase.ecom.cms.dto.CmsPageDto;
import com.homebase.ecom.cms.service.CmsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cms")
public class CmsController {

    private final CmsService cmsService;

    public CmsController(CmsService cmsService) {
        this.cmsService = cmsService;
    }

    @GetMapping("/pages/{slug}")
    public ResponseEntity<CmsPageDto> getPage(@PathVariable String slug) {
        CmsPageDto page = cmsService.getPage(slug);
        return ResponseEntity.ok(page);
    }

    @PostMapping("/pages")
    public ResponseEntity<CmsPageDto> createPage(@RequestBody CmsPageDto page) {
        CmsPageDto saved = cmsService.savePage(page);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/pages/{id}")
    public ResponseEntity<CmsPageDto> updatePage(@PathVariable String id, @RequestBody CmsPageDto page) {
        page.setId(id);
        CmsPageDto saved = cmsService.savePage(page);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/banners")
    public ResponseEntity<List<BannerDto>> getBanners(@RequestParam(required = false) String position) {
        List<BannerDto> banners = cmsService.getBanners(position);
        return ResponseEntity.ok(banners);
    }

    @GetMapping("/banners/{position}")
    public ResponseEntity<List<BannerDto>> getBannersByPosition(@PathVariable String position) {
        List<BannerDto> banners = cmsService.getBanners(position);
        return ResponseEntity.ok(banners);
    }

    @PostMapping("/banners")
    public ResponseEntity<BannerDto> createBanner(@RequestBody BannerDto banner) {
        // Delegate to service - for now return the input as placeholder
        // Full implementation would go through CmsService
        return ResponseEntity.ok(banner);
    }
}
