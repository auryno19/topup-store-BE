package com.example.fufastore.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.fufastore.model.Banner;
import com.example.fufastore.repository.BannerRepository;
import com.example.fufastore.util.ApiResponse;
import com.example.fufastore.util.ResponseUtil;

import jakarta.transaction.Transactional;

@Transactional
@RestController
@CrossOrigin("*")
@RequestMapping("api/banner")
public class ApiBannerController {

    @Autowired
    private BannerRepository bannerRepository;

    @GetMapping("")
    public ResponseEntity<ApiResponse<Object>> getBanner() {
        try {
            Banner banner = bannerRepository.findBanner();
            if (banner == null) {
                return ResponseUtil.generateSuccessResponse("Banner not found.", null);
            }
            Map<String, Object> imgSrc = new HashMap<>();
            imgSrc.put("banner", banner.getImage());
            System.out.println(banner.getImage());
            return ResponseUtil.generateSuccessResponse("Banner retrieved successfully", imgSrc);
        } catch (Exception e) {
            return ResponseUtil.generateErrorResponse("Retrieved banner failed", e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("edit")
    public ResponseEntity<ApiResponse<Object>> editBanner(@RequestParam("file") MultipartFile file) {
        try {
            Banner banner = bannerRepository.findBanner();
            if (banner == null) {
                Banner newBanner = new Banner();
                newBanner.setCreatedAt(new Date());
                newBanner.setImage(file.getBytes());
                this.bannerRepository.save(newBanner);
            } else {
                banner.setImage(file.getBytes());
                banner.setUpdatedAt(new Date());
                this.bannerRepository.save(banner);
            }
            return ResponseUtil.generateSuccessResponse("Upload Banner success", null);
        } catch (Exception e) {
            return ResponseUtil.generateErrorResponse("Edit Banner failed", e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
