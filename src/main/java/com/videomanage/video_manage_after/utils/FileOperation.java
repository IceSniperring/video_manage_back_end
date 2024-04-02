package com.videomanage.video_manage_after.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileOperation {
    public static boolean saveFile(String path, MultipartFile file) {
        File dir = new File(path);
        if (!dir.getParentFile().exists()) {
            dir.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dir);
            return true;
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }
    }

    public static boolean deleteFile(Path path) {
        try {
            Files.delete(path);
            deleteEmptyFolders(path.getParent());
            return true;
        } catch (IOException e) {
            System.out.println(e.toString());
            return false;
        }
    }

    private static void deleteEmptyFolders(Path folder) throws IOException {
        if (folder != null && Files.isDirectory(folder) && isEmpty(folder)) {
            Files.delete(folder);
            deleteEmptyFolders(folder.getParent());
        }
    }

    private static boolean isEmpty(Path folder) throws IOException {
        try (var stream = Files.list(folder)) {
            return !stream.findAny().isPresent();
        }
    }

    public static String extractFileName(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex != -1) { // 如果存在点号
            return fileName.substring(0, lastDotIndex); // 返回最后一个点号之前的部分
        } else {
            return fileName; // 如果没有点号，则返回原文件名
        }
    }

    public static String getFileType(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex != -1) { // 如果存在点号
            return fileName.substring(lastDotIndex); // 返回最后一个点号之后的部分
        } else {
            return fileName; // 如果没有点号，则返回原文件名
        }
    }
}
