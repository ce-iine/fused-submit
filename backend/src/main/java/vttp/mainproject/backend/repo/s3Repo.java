package vttp.mainproject.backend.repo;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Repository
public class s3Repo {

    @Autowired
    AmazonS3 s3;

    public static final String BUCKET_NAME = "vttpce-iine";

    public String saveToS3(String imgId, InputStream is, String contentType, long length) {
        ObjectMetadata metadata = new ObjectMetadata();

        metadata.setContentType(contentType);
        metadata.setContentLength(length);

        PutObjectRequest putReq = new PutObjectRequest(BUCKET_NAME, "resume/%s".formatted(imgId), is, metadata);
        putReq = putReq.withCannedAcl(CannedAccessControlList.PublicRead);
        
        s3.putObject(putReq);

        String imageUrl = s3.getUrl("vttpce-iine", "resume/%s".formatted(imgId)).toExternalForm();
        return imageUrl;
    }

    public String getImageUrl(String key) {
        return s3.getUrl(BUCKET_NAME, key).toExternalForm();
    }
    
}
