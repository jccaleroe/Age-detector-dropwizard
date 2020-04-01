package co.mil.imi.models.flowable;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class ImprovementConceptDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) {
        System.out.println("File " + execution.getVariable("file")
                + " is not worthy to be indexed on Hydra, do a better work next time ");
    }
}
