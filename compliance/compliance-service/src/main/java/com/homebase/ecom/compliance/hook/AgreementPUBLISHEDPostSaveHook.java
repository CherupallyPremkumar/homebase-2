package com.homebase.ecom.compliance.hook;

import org.chenile.stm.State;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.chenile.workflow.model.TransientMap;
import com.homebase.ecom.compliance.model.Agreement;
import com.homebase.ecom.compliance.port.out.NotificationPort;

public class AgreementPUBLISHEDPostSaveHook implements PostSaveHook<Agreement> {

    private final NotificationPort notificationPort;

    public AgreementPUBLISHEDPostSaveHook(NotificationPort notificationPort) {
        this.notificationPort = notificationPort;
    }

    @Override
    public void execute(State startState, State endState, Agreement entity, TransientMap transientMap) {
        if (entity.isMandatoryAcceptance()) {
            notificationPort.notifyNewMandatoryAgreement(entity.getId(), entity.getAgreementType());
        }
    }
}
