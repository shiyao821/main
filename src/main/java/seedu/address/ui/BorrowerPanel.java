package seedu.address.ui;

import javax.swing.plaf.synth.Region;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import seedu.address.model.loan.Loan;

/**
 * Panel to show borrower on SERVE MODE
 */
public class BorrowerPanel extends UiPart<Region> {

    private static final String FXML = "BorrowerPanel.fxml";

    @FXML
    private VBox borrowerPanel;
    @FXML
    private Label name;
    @FXML
    private Label borrowerId;
    @FXML
    private Label feesOutstanding;
    @FXML
    private ListView<Loan> loanListView;

    // TODO implement borrower parameter and update labels. See PersonCard.java
    public BorrowerPanel() {
        super(FXML);
    }

    public void setSize(double height, double width) {
        borrowerPanel.setPrefHeight(height);
        borrowerPanel.setPrefWidth(width);
    }
}
