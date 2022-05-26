package com.ccw.project.service;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ThumbnailService {
    /**
     * Resize the image
     * @param resource
     * @param scale
     * @param toFile
     * @return
     */
    public String changeScale(MultipartFile resource, double scale, String toFile) {
        try {
            Thumbnails.of(resource.getInputStream()).scale(scale).toFile(toFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "changeScale";
    }

    /**
     * Rize the gallery images
     * @param resource
     * @param width
     * @param height
     * @param toFile
     * @return
     */
    public String changeSize(MultipartFile resource, int width, int height, String toFile) {
        try {
            Thumbnails.of(resource.getInputStream()).size(width, height).outputFormat("jpg").toFile(toFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "changeSize";
    }

}
