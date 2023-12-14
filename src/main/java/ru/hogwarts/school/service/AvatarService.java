package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Service
@Transactional
public class AvatarService {
    private final StudentService studentService;
    private final AvatarRepository avatarRepository;

    @Value("${base.avatars.dir}")
    private String avatarsDir;

    public AvatarService(StudentService studentService, AvatarRepository avatarRepository) {
        this.studentService = studentService;
        this.avatarRepository = avatarRepository;
    }

    public Avatar getAvatar(Long studentId) {
        return avatarRepository.findAvatarByStudentId(studentId).orElseThrow();
    }

    public Student uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        Student student = studentService.getStudent(studentId);

        Path filePath = Path.of(avatarsDir, studentId.toString(), file.getOriginalFilename());
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        int rwByteCount = 1024;

        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, StandardOpenOption.CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, rwByteCount);
             BufferedOutputStream bos = new BufferedOutputStream(os, rwByteCount)
        ) {
            bis.transferTo(bos);
        }

        Avatar avatar = getAvatarByStudentId(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());

        avatar.setData(generateImagePreview(filePath));

        avatarRepository.save(avatar);

        return student;
    }

    private Avatar getAvatarByStudentId(Long studentId) {
        return avatarRepository.findAvatarByStudentId(studentId).orElse(new Avatar());
    }

    private byte[] generateImagePreview(Path filePath) throws IOException {
        try (InputStream is = Files.newInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(bis);

            int height = image.getHeight() / (image.getWidth() / 100);

            BufferedImage preview = new BufferedImage(100, height, image.getType());
            Graphics2D graphics = preview.createGraphics();
            graphics.drawImage(image, 0, 0, 100, height, null);
            graphics.dispose();

            ImageIO.write(preview, getFileExtension(filePath.getFileName().toString()), baos);
            baos.close();
            return baos.toByteArray();
        }
    }

    private byte[] getAvatarBytes(Path filePath) throws IOException {
        int rwByteCount = 1024;

        try (InputStream is = Files.newInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(is, rwByteCount);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ) {
            baos.writeBytes(bis.readAllBytes());

            return baos.toByteArray();
        }
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
