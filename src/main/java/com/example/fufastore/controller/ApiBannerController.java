package com.example.fufastore.controller;

import java.util.Date;
import java.util.stream.Collectors;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

@RestController
@CrossOrigin("*")
@RequestMapping("api/banner")
public class ApiBannerController {

    @Autowired
    private BannerRepository bannerRepository;

    @Transactional
    @GetMapping("")
    public ResponseEntity<ApiResponse<Object>> getBanner() {
        try {
            List<String> listBannerImages = bannerRepository.findIsActive().stream()
                    .map(Banner::getImage)
                    .collect(Collectors.toList());
            if (listBannerImages.isEmpty()) {
                return ResponseUtil.generateSuccessResponse("Banner not found.", null);
            }
            // System.out.println(banner.getImage());
            return ResponseUtil.generateSuccessResponse("Banner retrieved successfully", listBannerImages);
        } catch (Exception e) {
            return ResponseUtil.generateErrorResponse("Retrieved banner failed", e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @GetMapping("getAll")
    public ResponseEntity<ApiResponse<Object>> getAllBanner() {
        try {
            List<Banner> Listbanner = bannerRepository.findIsActive();
            if (Listbanner.isEmpty()) {
                return ResponseUtil.generateSuccessResponse("Banner not found.", null);
            }

            // System.out.println(response.toString());
            return ResponseUtil.generateSuccessResponse("Banner retrieved successfully", Listbanner);
        } catch (Exception e) {
            return ResponseUtil.generateErrorResponse("Retrieved banner failed", e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @GetMapping("get/{id}")
    public ResponseEntity<ApiResponse<Object>> getId(@PathVariable("id") Long id) {
        try {
            Banner banner = bannerRepository.findById(id).orElse(null);
            if (banner == null) {
                return ResponseUtil.generateSuccessResponse("Banner not found.", null);
            }

            return ResponseUtil.generateSuccessResponse("Banner retrieved successfully", banner);
        } catch (Exception e) {
            return ResponseUtil.generateErrorResponse("Retrieved banner failed", e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("edit")
    public ResponseEntity<ApiResponse<Object>> editBanner2(@RequestParam("file") MultipartFile file) {
        try {
            Banner banner = bannerRepository.findBanner();
            if (banner == null) {
                Banner newBanner = new Banner();
                newBanner.setCreatedAt(new Date());
                newBanner.setImage(file.getBytes());
                newBanner.setStatus(true);
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

    @PostMapping("add")
    public ResponseEntity<ApiResponse<Object>> addBanner(@RequestParam("file") MultipartFile file) {
        try {
            Banner newBanner = new Banner();
            newBanner.setCreatedAt(new Date());
            newBanner.setImage(file.getBytes());
            newBanner.setStatus(true);
            this.bannerRepository.save(newBanner);
            return ResponseUtil.generateSuccessResponse("Upload Banner success", null);
        } catch (Exception e) {
            return ResponseUtil.generateErrorResponse("Edit Banner failed", e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("edit/{id}")
    public ResponseEntity<ApiResponse<Object>> editBanner(@RequestParam("file") MultipartFile file,
            @PathVariable("id") Long id) {
        try {
            if (id != null) {
                Banner banner = bannerRepository.findById(id).orElse(null);
                if (banner != null) {
                    banner.setUpdatedAt(new Date());
                    banner.setImage(file.getBytes());
                    this.bannerRepository.save(banner);
                    return ResponseUtil.generateSuccessResponse("Edit Banner success", null);
                }
            }
            return ResponseUtil.generateSuccessResponse("Banner not found.", null,
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseUtil.generateErrorResponse("Edit Banner failed", e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("delete/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteBanner(@PathVariable("id") Long id) {
        try {
            if (id != null) {
                Banner banner = bannerRepository.findById(id).orElse(null);
                if (banner != null) {
                    banner.setUpdatedAt(new Date());
                    banner.setStatus(false);
                    this.bannerRepository.save(banner);
                    return ResponseUtil.generateSuccessResponse("delete Banner success", null);
                }
            }
            return ResponseUtil.generateSuccessResponse("Banner not found.", null,
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseUtil.generateErrorResponse("Delete Banner failed", e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
