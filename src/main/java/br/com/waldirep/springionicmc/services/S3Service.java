package br.com.waldirep.springionicmc.services;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

	@Autowired // Injetando uma instancia do Bean AmazonS3
	private AmazonS3 s3client;

	@Value("${s3.bucket}")
	private String bucketName;

	private Logger LOG = LoggerFactory.getLogger(S3Service.class);

	/**
	 * Metodo para fazer upload para o AmazonS3
	 * 
	 * @param localFilePath
	 */
	public void uploadFile(String localFilePath) {

		try {
			File file = new File(localFilePath); // Objeto do java.io
			LOG.info("Iniciando upload");
			s3client.putObject(new PutObjectRequest(bucketName, "teste.jpg", file));
			LOG.info("Upload finalizado");


		} catch (AmazonServiceException e) {
			LOG.info("AmazonServiceException: " + e.getMessage());
			LOG.info("Status code: " + e.getErrorCode());
			
		} catch (AmazonClientException e) {
			LOG.info("AmazonClientException: " + e.getMessage());		}
	}

}
