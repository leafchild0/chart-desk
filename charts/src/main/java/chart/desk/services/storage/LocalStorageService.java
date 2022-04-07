package chart.desk.services.storage;

import chart.desk.model.AssetKind;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 */
@Service
@ConditionalOnProperty(
        value="storage.type",
        havingValue = "LOCAL")
public class LocalStorageService implements StorageService {

    @Value("${storage.local.path}" )
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
            // TODO: error handling
            throw new RuntimeException(e);
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
            // TODO: error handling
            throw new RuntimeException(e);
        }
    }

    @Override
    public StorageType type() {
        return StorageType.LOCAL;
    }
}
