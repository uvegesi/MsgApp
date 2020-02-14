package edu.progmatic.messageapp.services;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.Stream;

@Service
public class StorageService {

    private static String UPLOADED_FOLDER = "/home/progmatic-9/Letöltések/Messenger2019Autumn/uploadedImages/";

    @PersistenceContext
    EntityManager em;

    public void init() {
    }

    public void store(MultipartFile file) {
        try {
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stream<Path> loadAll() {return null;}

    public Path load(String filename) {
        return null;
    }

    public byte[] loadAsResource(String filename) throws IOException {
            byte[] bytes = new FileInputStream(UPLOADED_FOLDER + filename).readAllBytes();
        return bytes;
    }

    public void deleteAll() {
    }
}
