package chart.desk.services.storage;

import chart.desk.model.AssetKind;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@ConditionalOnProperty(
        value = "storage.type",
        havingValue = "AWS_S3")
@Service
public class S3StorageService implements StorageService {

    @Value("${storage.aws.bucket}")
    private String bucketName;

    @Override
    public String save(byte[] file, String name, String version, AssetKind assetKind, String userId) {
        String key = "charts" + "/" + userId + "/" + name + "-" + version + assetKind.getExtension();
        try (InputStream is = new ByteArrayInputStream(file)) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.length);
            getS3Client().putObject(bucketName, key, is, new ObjectMetadata());
            return getUrl(key).toString();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Chart " + name + "-" + version + " fetching error", e);
        }
    }

    private URL getUrl(String key) {
        return getS3Client().getUrl(bucketName, key);
    }

    @Override
    public byte[] get(String name, String version, AssetKind assetKind, String userId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public StorageType type() {
        return StorageType.AWS_S3;
    }

    private AmazonS3 getS3Client() {
        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.DEFAULT_REGION)
                .build();
    }
}
