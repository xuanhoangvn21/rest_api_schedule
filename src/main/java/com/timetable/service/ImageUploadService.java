package com.timetable.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageUploadService {
    ResponseEntity<?> imageUpload(List<MultipartFile> files);
}
