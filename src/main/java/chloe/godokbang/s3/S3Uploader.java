package chloe.godokbang.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadImage(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("Fail to convert MultipartFile to File"));
        String fileName = dirName + "/" + changeImageName(uploadFile.getName());
        String uploadImageUrl = putS3(uploadFile, fileName);

        // 로컬에 생성된 file 삭제
        uploadFile.delete();

        return uploadImageUrl;
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        File convertFile = new File(tempDir, file.getOriginalFilename());

        try (FileOutputStream fos = new FileOutputStream(convertFile)) {
            fos.write(file.getBytes());
        }

        return Optional.of(convertFile);
    }

    // 파일 이름 중복 방지용
    private String changeImageName(String originName) {
        String random = UUID.randomUUID().toString();
        return random + "_" + originName;
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }
}
