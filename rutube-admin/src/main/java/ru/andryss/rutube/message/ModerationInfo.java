package ru.andryss.rutube.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ModerationInfo {
    String sourceId;
    String downloadLink;
}
