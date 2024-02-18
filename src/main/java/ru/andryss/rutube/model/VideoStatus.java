package ru.andryss.rutube.model;

public enum VideoStatus {
    UPLOAD_PENDING,
    MODERATION_PENDING,
    MODERATION_FAILED,
    MODERATION_SUCCESS,
    READY,
    PUBLISHED,
    DELETED
}
