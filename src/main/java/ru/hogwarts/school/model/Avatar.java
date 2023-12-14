package ru.hogwarts.school.model;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
public class Avatar {
    @Id
    @GeneratedValue
    private Long id;
    private String filePath;
    private long fileSize;
    private String mediaType;
    @Lob
    private byte[] data;

    @OneToOne
    private Student student;

    public Avatar() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Avatar)) return false;

        Avatar avatar = (Avatar) o;

        if (fileSize != avatar.fileSize) return false;
        if (!Objects.equals(id, avatar.id)) return false;
        if (!Objects.equals(filePath, avatar.filePath)) return false;
        if (!Objects.equals(mediaType, avatar.mediaType)) return false;
        if (!Arrays.equals(data, avatar.data)) return false;
        return Objects.equals(student, avatar.student);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (filePath != null ? filePath.hashCode() : 0);
        result = 31 * result + (int) (fileSize ^ (fileSize >>> 32));
        result = 31 * result + (mediaType != null ? mediaType.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(data);
        result = 31 * result + (student != null ? student.hashCode() : 0);
        return result;
    }
}
