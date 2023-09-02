package com.example.flyway.constants.enums;

import javax.validation.ValidationException;
import lombok.Getter;

@Getter
public enum MeetingType {

    RETROSPECTIVE("retro"), PLANNING("planning"), STANDUP("standup");

    private final String urlType;

    MeetingType(String urlType) {
        this.urlType = urlType;
    }

    public static MeetingType getByUrlType(String urlPath) {

        for (MeetingType type : MeetingType.values()) {
            if (urlPath.equals(type.getUrlType())) {
                return type;
            }
        }
        throw new ValidationException("Provided meeting type doesn't exist!");
    }
}
