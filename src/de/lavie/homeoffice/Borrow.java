package de.lavie.homeoffice;

import java.time.LocalDateTime;

public class Borrow {
    private LocalDateTime from;
    private LocalDateTime to;

    public Borrow(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }
}
