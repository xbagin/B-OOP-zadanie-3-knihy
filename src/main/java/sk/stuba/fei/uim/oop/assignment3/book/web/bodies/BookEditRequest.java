package sk.stuba.fei.uim.oop.assignment3.book.web.bodies;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookEditRequest {
    private String name;
    private String description;
    private Long author;
    private Integer pages;
}
