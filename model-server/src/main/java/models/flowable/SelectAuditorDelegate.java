package models.flowable;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class SelectAuditorDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) {
        System.out.println("Selecting auditor with the highest hierarchy for the file "
                + execution.getVariable("file") + "...\nSolder Ryan is selected");
        execution.setVariable("auditor", "Ryan");
    }
}
