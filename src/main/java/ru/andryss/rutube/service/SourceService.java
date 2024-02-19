package ru.andryss.rutube.service;

/**
 * Service for working with source uploading
 */
public interface SourceService {
    /**
     * Generates link for source uploading
     *
     * @param sourceId source id to be uploaded
     * @return upload link
     */
    String generateUploadLink(String sourceId);
}
