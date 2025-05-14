package com.laderrco.streamsage.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Generic way to cachine the movie name
public class QueryNormalizer {
    
    public static String normalizeQuery(String query) {
        if (query == null) {
            return null;
        }

        String lower = query.toLowerCase().trim();
        String title = extractMovieOrShowTitle(lower);
        return title != null ? "recommendations:similar_to:" + title : "recommendations:unknown";
    }

    private static String extractMovieOrShowTitle(String query) {
        // Define common patterns
        String[] patterns = {
            "like\\s+([a-z0-9\\s]+)",               // e.g. "like snowpiercer"
            "similar to\\s+([a-z0-9\\s]+)",         // e.g. "similar to snowpiercer"
            "such as\\s+([a-z0-9\\s]+)",            // e.g. "such as snowpiercer"
            "based on\\s+([a-z0-9\\s]+)",           // e.g. "based on snowpiercer"
            "watch\\s+([a-z0-9\\s]+)",              // e.g. "watch snowpiercer"
            "movies like\\s+([a-z0-9\\s]+)",        // e.g. "movies like snowpiercer"
            "shows like\\s+([a-z0-9\\s]+)"          // e.g. "shows like snowpiercer"
        };

        for (String regex : patterns) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(query);
            if (matcher.find()) {
                String matched = matcher.group(1).trim();
                return sanitizeTitle(matched);
            }
        }

        return null;
    }

    private static String sanitizeTitle(String raw) {
        // Remove trailing words like "please", "?", ".", etc.
        raw = raw.replaceAll("(\\s+(please|now|thanks|movies|shows)[.!?,]*)*$", "");
        return raw.trim().replaceAll("\\s{2,}", " ");
    }
}
