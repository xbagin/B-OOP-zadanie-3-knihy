package sk.stuba.fei.uim.oop.assignment3.list.web.bodies;

import lombok.Getter;
import sk.stuba.fei.uim.oop.assignment3.book.web.bodies.BookResponse;
import sk.stuba.fei.uim.oop.assignment3.list.data.LendingList;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class LendingListResponse {
    private final Long id;
    private final List<BookResponse> lendingList;
    private final Boolean lended;

    public LendingListResponse(LendingList list) {
        this.id = list.getId();
        this.lendingList = list.getBooks().stream().map(BookResponse::new).collect(Collectors.toList());
        this.lended = list.getLended();
    }
}
