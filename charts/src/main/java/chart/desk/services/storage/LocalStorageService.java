package chart.desk.services.storage;

import chart.desk.errors.FetchingException;
import chart.desk.model.AssetKind;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Local Storage provider impl
 */
@Service
@ConditionalOnExpression("'${storage.type}'.contains('LOCAL')")
@Slf4j
public class LocalStorageService implements StorageService {

    @Value("${storage.local.path}")
    private String storagePath;

    @Override
    public String save(byte[] file, String name, String version, AssetKind assetKind, String userId) {
        String fileName = getFileName(name, version, assetKind);
        try {
            Path path = Path.of(storagePath, userId, fileName);
            if (!Files.exists(path)) {
                saveChart(file, path);
            }
            return getGatewayUrl() + userId + "/" + fileName;
        } catch (IOException e) {
            log.error("Error fetching chart " + name, e);
            throw new FetchingException(HttpStatus.INTERNAL_SERVER_ERROR, "Chart " + name + "-" + version + " fetching error");
        }
    }

    private void saveChart(byte[] file, Path path) throws IOException {
        Files.createDirectories(path.getParent());
        Files.createFile(path);
        Files.write(path, file);
    }

    private String getFileName(String name, String version, AssetKind assetKind) {
        return name + "-" + version + assetKind.getExtension();
    }

    public String getGatewayUrl() {
        return "http://localhost:8500/";
    }

    @Override
    public byte[] get(String name, String version, AssetKind assetKind, String userId) {
        String fileName = getFileName(name, version, assetKind);
        Path path = Path.of(storagePath, userId, fileName);
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            log.error("Error fetching chart " + name, e);
            throw new FetchingException(HttpStatus.INTERNAL_SERVER_ERROR, "Chart " + name + "-" + version + " fetching error");
        }
    }

    @Override
    public StorageType type() {
        return StorageType.LOCAL;
    }
}
