package ru.yandex.practicum.filmorate.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Genre implements Comparable<Genre> {
    private final int id;
    private String name;


    @Override
    public int compareTo(Genre o) {
        return Integer.compare(id, o.getId());

    }
}
