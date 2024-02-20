package ru.andryss.rutube.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Service for working with sources
 */
public interface SourceService {
    /**
     * Generates link for source uploading
     *
     * @param sourceId source id to be uploaded
     * @return upload link
     */
    String generateUploadLink(String sourceId);

    /**
     * Uploads video by generated link
     *
     * @param sourceId source id to be uploaded
     * @param video video file
     */
    void putVideo(String sourceId, MultipartFile video);

    /**
     * Generates link for source downloading
     *
     * @param sourceId source id to be downloaded
     * @return download link
     */
    String generateDownloadLink(String sourceId);

    /**
     * Gets video by generated link
     *
     * @param sourceId source id to be downloaded
     * @return video file
     */
    byte[] getVideo(String sourceId);
}
