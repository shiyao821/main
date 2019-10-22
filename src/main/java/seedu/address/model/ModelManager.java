package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.UserSettings;
import seedu.address.model.book.Book;
import seedu.address.model.book.SerialNumber;
import seedu.address.model.book.SerialNumberGenerator;
import seedu.address.model.borrower.Borrower;
import seedu.address.model.borrower.BorrowerId;
import seedu.address.model.borrower.BorrowerIdGenerator;
import seedu.address.model.loan.Loan;
import seedu.address.model.loan.LoanIdGenerator;

/**
 * Represents the in-memory model of the Library data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final UserPrefs userPrefs;
    private final LoanRecords loanRecords;
    private final Catalog catalog;
    private final BorrowerRecords borrowerRecords;
    private final FilteredList<Book> filteredBooks;
    private Optional<Borrower> servingBorrower;

    /**
     * Initializes a ModelManager with the given catalog, loan records, borrower records and userPrefs.
     * TODO change
     */
    public ModelManager(ReadOnlyCatalog catalog,
                        ReadOnlyLoanRecords loanRecords,
                        ReadOnlyBorrowerRecords borrowerRecords,
                        ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(userPrefs, loanRecords, catalog, borrowerRecords);

        logger.fine("Initializing with catalog: " + catalog + " and user prefs " + userPrefs);

        this.userPrefs = new UserPrefs(userPrefs);
        // testing loan records
        this.loanRecords = new LoanRecords(loanRecords);
        LoanIdGenerator.setLoanRecords(this.loanRecords);
        // testing
        this.catalog = new Catalog(catalog);
        SerialNumberGenerator.setCatalog((Catalog) catalog);
        // testing
        this.borrowerRecords = new BorrowerRecords(borrowerRecords);
        filteredBooks = new FilteredList<>(this.catalog.getBookList());

        this.servingBorrower = Optional.empty(); // TODO
    }

    public ModelManager() {
        this(new Catalog(), new LoanRecords(), new BorrowerRecords(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public UserSettings getUserSettings() {
        return userPrefs.getUserSettings();
    }

    @Override
    public void setUserSettings(UserSettings userSettings) {
        requireNonNull(userSettings);
        userPrefs.setUserSettings(userSettings);
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    public Path getLoanRecordsFilePath() {
        return userPrefs.getLoanRecordsFilePath();
    }

    @Override
    public void setLoanRecordsFilePath(Path loanRecordsFilePath) {
        requireNonNull(loanRecordsFilePath);
        userPrefs.setLoanRecordsFilePath(loanRecordsFilePath);
    }

    @Override
    public Path getCatalogFilePath() {
        return userPrefs.getCatalogFilePath();
    }

    @Override
    public void setCatalogFilePath(Path catalogFilePath) {
        requireNonNull(catalogFilePath);
        userPrefs.setCatalogFilePath(catalogFilePath);
    }

    @Override
    public Path getBorrowerRecordsFilePath() {
        return userPrefs.getBorrowerRecordsFilePath();
    }

    @Override
    public void setBorrowerRecordsFilePath(Path borrowerRecordsFilePath) {
        requireNonNull(borrowerRecordsFilePath);
        userPrefs.setBorrowerRecordsFilePath(borrowerRecordsFilePath);
    }

    //=========== Loan Records ===============================================================================

    public ReadOnlyLoanRecords getLoanRecords() {
        return loanRecords;
    }

    /**
     * Adds a <code>Loan</code> object to the loan records.
     *
     * @param loan <code>Loan</code> object to be added.
     */
    public void addLoan(Loan loan) {
        requireNonNull(loan);
        loanRecords.addLoan(loan);
    }

    //=========== Catalog ===============================================================================


    @Override
    public void setCatalog(ReadOnlyCatalog catalog) {
        this.catalog.resetData(catalog);
    }

    @Override
    public ReadOnlyCatalog getCatalog() {
        return catalog;
    }

    @Override
    public boolean hasBook(Book book) {
        requireNonNull(book);
        return catalog.hasBook(book);
    }

    @Override
    public boolean hasBook(SerialNumber bookSn) {
        requireNonNull(bookSn);
        return catalog.checkIfSerialNumberExists(bookSn);
    }

    @Override
    public void deleteBook(Book target) {
        requireNonNull(target);
        catalog.removeBook(target);
        SerialNumberGenerator.setCatalog(catalog);
    }

    @Override
    public void addBook(Book book) {
        requireNonNull(book);
        catalog.addBook(book);
        SerialNumberGenerator.setCatalog(catalog);
        updateFilteredBookList(PREDICATE_SHOW_ALL_BOOKS);
    }

    @Override
    public Book getBook(SerialNumber bookSn) {
        return catalog.getBook(bookSn);
    }

    @Override
    public void setBook(Book target, Book editedBook) {
        requireAllNonNull(target, editedBook);
        catalog.setBook(target, editedBook);
        SerialNumberGenerator.setCatalog(catalog);
    }

    /**
     * Returns a list of overdue books in the catalog.
     *
     * @return an <code>ObservableList</code> of overdue books.
     */
    @Override
    public ObservableList<Book> getOverdueBooks() {
        return catalog.getOverdueBooks();
    }

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Book> getFilteredBookList() {
        return filteredBooks;
    }

    @Override
    public void updateFilteredBookList(Predicate<Book> predicate) {
        requireNonNull(predicate);
        filteredBooks.setPredicate(predicate);
    }

    public void resetFilteredBookList() {
        filteredBooks.setPredicate(x -> true);
    }

    @Override
    public Model excludeBookBeingReplaced(Book toBeReplaced) {
        Catalog tempCatalog = new Catalog(this.getCatalog());
        tempCatalog.removeBook(toBeReplaced);
        return new ModelManager(tempCatalog, this.getLoanRecords(), this.getBorrowerRecords(), this.getUserPrefs());
    }

    //=========== BorrowerRecords ===============================================================================

    @Override
    public ReadOnlyBorrowerRecords getBorrowerRecords() {
        return borrowerRecords;
    }


    @Override
    public Borrower getServingBorrower() {
        if (!isServeMode()) {
            throw new AssertionError("Not in Serve mode!");
        }
        return servingBorrower.get();
    }

    @Override
    public boolean isServeMode() {
        return servingBorrower.isPresent();
    }

    @Override
    public boolean hasBorrower(Borrower borrower) {
        return borrowerRecords.hasBorrower(borrower);
    }

    @Override
    public void registerBorrower(Borrower borrower) {
        borrowerRecords.addBorrower(borrower);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return userPrefs.equals(other.userPrefs)
                && loanRecords.equals(other.loanRecords)
                && catalog.equals(other.catalog)
                && borrowerRecords.equals(other.borrowerRecords);
    }

    @Override
    public void resetGenerator() {
        BorrowerIdGenerator.setBorrowers(borrowerRecords);
    }

    @Override
    public void setServingBorrower(BorrowerId borrowerId) {
        this.servingBorrower = Optional.of(borrowerRecords.getBorrowerFromId(borrowerId));
    }

    @Override
    public boolean hasBorrowerId(BorrowerId borrowerId) {
        return borrowerRecords.checkIfBorrowerIdExists(borrowerId);
    }

    @Override
    public void exitsServeMode() {
        this.servingBorrower = Optional.empty();
    }

    @Override
    public List<Book> getBorrowerBooks() {
        assert(isServeMode());

        ArrayList<Loan> loans = new ArrayList<>();
        servingBorrower.get()
                .getCurrentLoanList()
                .forEach(loan -> loans.add(loan));
        return loans.stream()
                .map(loan -> loan.getBookSerialNumber())
                .map(sn -> catalog.getBook(sn))
                .collect(Collectors.toList());
    }

    @Override
    public Borrower getBorrowerFromId(BorrowerId borrowerId) {
        return borrowerRecords.getBorrowerFromId(borrowerId);
    }

    @Override
    public void setBorrower(Borrower borrowerToEdit, Borrower editedBorrower) {

    }

    @Override
    public boolean hasDuplicatedBorrower(Borrower editedBorrower) {
        return borrowerRecords.hasDuplicateBorrower(editedBorrower);
    }

}
