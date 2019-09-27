package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.Catalog;
import seedu.address.model.book.Book;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalBooks {

    public static final Book BOOK_1 = new BookBuilder().withTitle("Harry Botter")
            .withSerialNumber("0001").withGenres("Fiction").build();
    public static final Book BOOK_2 = new BookBuilder().withTitle("Legend of the Condor Heroes")
            .withAuthor("Jin Yong").withSerialNumber("0002").withGenres("Fiction", "Action").build();
    public static final Book BOOK_3 = new BookBuilder().withTitle("Animal the Farm").withSerialNumber("0003")
            .withAuthor("George Orwell").build();
    public static final Book BOOK_4 = new BookBuilder().withTitle("Harri Botter and the Full Blood Prince")
            .withSerialNumber("0004").withAuthor("J K Rowling").withGenres("Fiction", "Action").build();
    public static final Book BOOK_5 = new BookBuilder().withTitle("The Heavenly Sword and the Dragon Saber")
            .withSerialNumber("0005").withAuthor("Jin Yong").withGenres("Fiction", "Action").build();
    public static final Book BOOK_6 = new BookBuilder().withTitle("My Book")
            .withSerialNumber("0006").withAuthor("Jin Yong").withGenres("Fiction", "Action").build();

    private TypicalBooks() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static Catalog getTypicalCatalog() {
        Catalog ab = new Catalog();
        for (Book book : getTypicalBooks()) {
            ab.addBook(book);
        }
        return ab;
    }

    public static List<Book> getTypicalBooks() {
        return new ArrayList<>(Arrays.asList(BOOK_1, BOOK_2, BOOK_3, BOOK_4));
    }
}
