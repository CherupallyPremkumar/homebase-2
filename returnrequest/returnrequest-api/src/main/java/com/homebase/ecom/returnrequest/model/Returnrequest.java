package com.homebase.ecom.returnrequest.model;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import java.util.*;
import com.homebase.ecom.returnrequest.model.ReturnrequestActivityLog;
import org.chenile.workflow.model.*;
import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;
@Entity
@Table(name = "returnrequest")
public class Returnrequest extends AbstractJpaStateEntity
    implements ActivityEnabledStateEntity
        ,
    ContainsTransientMap
{
	public String description;
    @Transient
    public TransientMap transientMap = new TransientMap();
    public TransientMap getTransientMap(){
        return this.transientMap;
    }

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER,orphanRemoval = true)
    public List< ReturnrequestActivityLog> activities = new ArrayList<>();

    @Override
    public Collection<ActivityLog> obtainActivities() {
        Collection<ActivityLog> acts = new ArrayList<>();
        for (ActivityLog a: activities){
            acts.add(a);
        }
        return acts;
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        ReturnrequestActivityLog activityLog = new ReturnrequestActivityLog();
        activityLog.activityName = eventId;
        activityLog.activityComment = comment;
        activityLog.activitySuccess = true;
        activities.add(activityLog);
        return activityLog;
    }
}
