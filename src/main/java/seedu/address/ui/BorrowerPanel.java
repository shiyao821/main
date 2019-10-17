package seedu.address.ui;

import java.net.URL;

import javax.swing.plaf.synth.Region;

import seedu.address.model.borrower.Borrower;

public class BorrowerPanel extends UiPart<Region> {

    private static final String FXML = "BorrowerPanel.fxml";

    public BorrowerPanel() {
        super(FXML);
    }
}
