/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.until;

/**
 *
 * @author haidu
 */
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private static final String uploadDir = "./uploads"; // Thư mục lưu trữ tệp

    public static void saveFile(MultipartFile file, String randomFileName) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        // Tạo thư mục nếu nó không tồn tại
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        // Tạo tên ngẫu nhiên cho tệp       

        // Lưu tệp vào thư mục với tên ngẫu nhiên
        Path filePath = uploadPath.resolve(randomFileName);
        Files.copy(file.getInputStream(), filePath);
    }

    public static String getRelativeUploadPath(String randomFileName) {
        ClassLoader classLoader = FileStorageService.class.getClassLoader();
        File file = new File(classLoader.getResource("uploads").getFile());
        String absolutePath = file.getAbsolutePath();
        return absolutePath + File.separator + randomFileName;
    }

    public static String generateRandomFileName(String originalFileName) {
        // Lấy phần mở rộng của tệp (ví dụ: .jpg)
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

        // Tạo một chuỗi ngẫu nhiên và kết hợp với phần mở rộng của tệp
        String randomFileName = UUID.randomUUID().toString() + fileExtension;
        return randomFileName;
    }
}
