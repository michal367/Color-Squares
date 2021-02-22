package main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class NumberChangeListener implements ChangeListener<Boolean>{

    private TextField textField;

    public NumberChangeListener(TextField textField){
        super();
        this.textField = textField;
    }

    @Override
    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
        if (!newValue) {
            if(!textField.getText().matches("^[0-9]*$"))
                textField.setText("");
        }
    }
}
