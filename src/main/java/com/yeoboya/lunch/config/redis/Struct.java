package com.yeoboya.lunch.config.redis;

import lombok.Data;

@Data
public class Struct {
    @Data
    public static class SortedSet{
        Double score = 0D;
        String value = "";
    }

}