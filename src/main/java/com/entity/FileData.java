package com.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "file_data_control")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id", nullable = false)
    private Long id;
    @Column(name = "file_name", nullable = false)
    private String fileName;
    @JsonIgnore
    @Lob
    @Column(name = "file_data")
    private byte[] fileData;
    @Column(name = "file_created")
    private LocalDate dateCreated;

    public FileData(String fileName, byte[] fileData, LocalDate dateCreated) {
        this.fileName = fileName;
        this.fileData = fileData;
        this.dateCreated = dateCreated;
    }
}
