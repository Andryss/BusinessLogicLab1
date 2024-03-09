package ru.andryss.rutube.service;

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
     * Generates link for source downloading
     *
     * @param sourceId source id to be downloaded
     * @return download link
     */
    String generateDownloadLink(String sourceId);
}
