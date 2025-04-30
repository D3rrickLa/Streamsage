package com.laderrco.streamsage.domains.Enums;

public enum Genre {
    ACTION_ADVENTURE(10759), // tv series
    ACTION(28),
    ADVENTURE(12),
    ANIMATION(16),
    COMEDY(35),
    CRIME(80),
    DOCUMENTARY(99),
    DRAMA(18),
    FAMILY(10751),
    FANTASY(14),
    HISTORY(36),
    HORROR(27),
    KIDS(10762), // tv series
    MUSIC(10402),
    MYSTERY(9648),
    NEWS(10763), //tv series
    REALITY(10764), //tv series
    ROMANCE(10749),
    SCIENCE_FICTION(878),
    SCIFI_FANTASY(10765),
    SOAP(10766),
    TALK(10767),
    WAR_POLITICS(10768),
    TV_MOVIE(10770),
    THRILLER(53),
    WAR(10752),
    WESTERN(37),
    OTHER(-1);

    private final Integer id;

    Genre(Integer id) {
        this.id = id;
    }
    
    public Integer getId() {
        return id;
    }

    public static Genre fromId(Integer id) {
        for (Genre genre : values()) {
            if (genre.getId() == id.intValue()) {  // Explicit integer match
                return genre;
            }
        }
        return OTHER;
    }

}
