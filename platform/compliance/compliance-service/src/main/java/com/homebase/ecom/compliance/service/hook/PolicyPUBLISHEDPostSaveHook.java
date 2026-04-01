package com.homebase.ecom.compliance.service.hook;

import org.chenile.stm.State;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.chenile.workflow.model.TransientMap;
import com.homebase.ecom.compliance.model.PlatformPolicy;
import com.homebase.ecom.compliance.port.out.NotificationPort;

public class PolicyPUBLISHEDPostSaveHook implements PostSaveHook<PlatformPolicy> {

    private final NotificationPort notificationPort;

    public PolicyPUBLISHEDPostSaveHook(NotificationPort notificationPort) {
        this.notificationPort = notificationPort;
    }

    @Override
    public void execute(State startState, State endState, PlatformPolicy entity, TransientMap transientMap) {
        notificationPort.notifyPolicyPublished(entity.getId(), entity.getPolicyCategory());
    }
}
