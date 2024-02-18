package ru.andryss.rutube.message;

import lombok.Setter;

import java.util.List;

@Setter
public class GetReactionsResponse {
    List<ReactionInfo> reactions;
}
