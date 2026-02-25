package com.homebase.ecom.user.configuration;

import org.chenile.stm.*;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.impl.*;
import org.chenile.stm.spring.SpringBeanFactoryAdapter;
import org.chenile.workflow.param.MinimalPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.chenile.workflow.service.stmcmds.*;
import com.homebase.ecom.user.model.User;
import com.homebase.ecom.user.service.cmds.*;
import com.homebase.ecom.user.service.healthcheck.UserHealthChecker;
import com.homebase.ecom.user.service.store.UserEntityStore;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.stm.State;
import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;
import com.homebase.ecom.user.service.postSaveHooks.*;

/**
 This is where you will instantiate all the required classes in Spring
*/
@Configuration
public class UserConfiguration {
	private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/user/user-states.xml";
	public static final String PREFIX_FOR_PROPERTIES = "User";
    public static final String PREFIX_FOR_RESOLVER = "user";

    @Bean BeanFactoryAdapter userBeanFactoryAdapter() {
		return new SpringBeanFactoryAdapter();
	}
	
	@Bean STMFlowStoreImpl userFlowStore(
            @Qualifier("userBeanFactoryAdapter") BeanFactoryAdapter userBeanFactoryAdapter
            )throws Exception{
		STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
		stmFlowStore.setBeanFactory(userBeanFactoryAdapter);
		return stmFlowStore;
	}
	
	@Bean  STM<User> userEntityStm(@Qualifier("userFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception{
		STMImpl<User> stm = new STMImpl<>();		
		stm.setStmFlowStore(stmFlowStore);
		return stm;
	}
	
	@Bean  STMActionsInfoProvider userActionsInfoProvider(@Qualifier("userFlowStore") STMFlowStoreImpl stmFlowStore) {
		STMActionsInfoProvider provider =  new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("user",provider);
        return provider;
	}
	
	@Bean EntityStore<User> userEntityStore() {
		return new UserEntityStore();
	}
	
	@Bean  StateEntityServiceImpl<User> _userStateEntityService_(
			@Qualifier("userEntityStm") STM<User> stm,
			@Qualifier("userActionsInfoProvider") STMActionsInfoProvider userInfoProvider,
			@Qualifier("userEntityStore") EntityStore<User> entityStore){
		return new StateEntityServiceImpl<>(stm, userInfoProvider, entityStore);
	}
	
	// Now we start constructing the STM Components 


    @Bean  DefaultPostSaveHook<User> userDefaultPostSaveHook(
    @Qualifier("userTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver){
    DefaultPostSaveHook<User> postSaveHook = new DefaultPostSaveHook<>(stmTransitionActionResolver);
    return postSaveHook;
    }

    @Bean  GenericEntryAction<User> userEntryAction(@Qualifier("userEntityStore") EntityStore<User> entityStore,
    @Qualifier("userActionsInfoProvider") STMActionsInfoProvider userInfoProvider,
    @Qualifier("userFlowStore") STMFlowStoreImpl stmFlowStore,
    @Qualifier("userDefaultPostSaveHook") DefaultPostSaveHook<User> postSaveHook)  {
    GenericEntryAction<User> entryAction =  new GenericEntryAction<User>(entityStore,userInfoProvider,postSaveHook);
    stmFlowStore.setEntryAction(entryAction);
    return entryAction;
    }

    @Bean  DefaultAutomaticStateComputation<User> userDefaultAutoState(
    @Qualifier("userTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
    @Qualifier("userFlowStore") STMFlowStoreImpl stmFlowStore){
    DefaultAutomaticStateComputation<User> autoState = new DefaultAutomaticStateComputation<>(stmTransitionActionResolver);
    stmFlowStore.setDefaultAutomaticStateComputation(autoState);
    return autoState;
    }

	@Bean GenericExitAction<User> userExitAction(@Qualifier("userFlowStore") STMFlowStoreImpl stmFlowStore){
        GenericExitAction<User> exitAction = new GenericExitAction<User>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
	}

	@Bean
	XmlFlowReader userFlowReader(@Qualifier("userFlowStore") STMFlowStoreImpl flowStore) throws Exception {
		XmlFlowReader flowReader = new XmlFlowReader(flowStore);
		flowReader.setFilename(FLOW_DEFINITION_FILE);
		return flowReader;
	}
	

	@Bean UserHealthChecker userHealthChecker(){
    	return new UserHealthChecker();
    }

    @Bean STMTransitionAction<User> defaultuserSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver userTransitionActionResolver(
    @Qualifier("defaultuserSTMTransitionAction") STMTransitionAction<User> defaultSTMTransitionAction){
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER,defaultSTMTransitionAction,true);
    }

    @Bean  StmBodyTypeSelector userBodyTypeSelector(
    @Qualifier("userActionsInfoProvider") STMActionsInfoProvider userInfoProvider,
    @Qualifier("userTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(userInfoProvider,stmTransitionActionResolver);
    }


    @Bean  STMTransitionAction<User> userBaseTransitionAction(
        @Qualifier("userTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
        @Qualifier("userActivityChecker") ActivityChecker activityChecker,
        @Qualifier("userFlowStore") STMFlowStoreImpl stmFlowStore){
        BaseTransitionAction<User> baseTransitionAction = new BaseTransitionAction<>(stmTransitionActionResolver);
        baseTransitionAction.activityChecker = activityChecker;
        stmFlowStore.setDefaultTransitionAction(baseTransitionAction);
        return baseTransitionAction;
    }

    @Bean ActivityChecker userActivityChecker(@Qualifier("userFlowStore") STMFlowStoreImpl stmFlowStore){
        return new ActivityChecker(stmFlowStore);
    }

    @Bean
    AreActivitiesComplete activitiesCompletionCheck(@Qualifier("userActivityChecker") ActivityChecker activityChecker){
        return new AreActivitiesComplete(activityChecker);
    }

    // Create the specific transition actions here. Make sure that these actions are inheriting from
    // AbstractSTMTransitionMachine (The sample classes provide an example of this). To automatically wire
    // them into the STM use the convention of "user" + eventId + "Action" for the method name. (user is the
    // prefix passed to the TransitionActionResolver above.)
    // This will ensure that these are detected automatically by the Workflow system.
    // The payload types will be detected as well so that there is no need to introduce an <event-information/>
    // segment in src/main/resources/com/homebase/user/user-states.xml


    @Bean SkipProfileUserAction
            userSkipProfileAction(){
        return new SkipProfileUserAction();
    }

    @Bean CancelSignupUserAction
            userCancelSignupAction(){
        return new CancelSignupUserAction();
    }

    @Bean DeactivateSupplierUserAction
            userDeactivateSupplierAction(){
        return new DeactivateSupplierUserAction();
    }

    @Bean BanAccountUserAction
            userBanAccountAction(){
        return new BanAccountUserAction();
    }

    @Bean RestartVerificationUserAction
            userRestartVerificationAction(){
        return new RestartVerificationUserAction();
    }

    @Bean VerificationExpiredUserAction
            userVerificationExpiredAction(){
        return new VerificationExpiredUserAction();
    }

    @Bean ResendVerificationEmailUserAction
            userResendVerificationEmailAction(){
        return new ResendVerificationEmailUserAction();
    }

    @Bean ApproveSupplierUserAction
            userApproveSupplierAction(){
        return new ApproveSupplierUserAction();
    }

    @Bean CompleteProfileUserAction
            userCompleteProfileAction(){
        return new CompleteProfileUserAction();
    }

    @Bean VerifyEmailUserAction
            userVerifyEmailAction(){
        return new VerifyEmailUserAction();
    }

    @Bean SendVerificationEmailUserAction
            userSendVerificationEmailAction(){
        return new SendVerificationEmailUserAction();
    }

    @Bean SuspendAccountUserAction
            userSuspendAccountAction(){
        return new SuspendAccountUserAction();
    }

    @Bean SuspendSupplierUserAction
            userSuspendSupplierAction(){
        return new SuspendSupplierUserAction();
    }

    @Bean RejectSupplierUserAction
            userRejectSupplierAction(){
        return new RejectSupplierUserAction();
    }

    @Bean DeactivateAccountUserAction
            userDeactivateAccountAction(){
        return new DeactivateAccountUserAction();
    }

    @Bean UpgradeToSupplierUserAction
            userUpgradeToSupplierAction(){
        return new UpgradeToSupplierUserAction();
    }

    @Bean DeleteAccountUserAction
            userDeleteAccountAction(){
        return new DeleteAccountUserAction();
    }

    @Bean ActivateAccountUserAction
            userActivateAccountAction(){
        return new ActivateAccountUserAction();
    }

    @Bean UnsuspendAccountUserAction
            userUnsuspendAccountAction(){
        return new UnsuspendAccountUserAction();
    }

    @Bean ReactivateAccountUserAction
            userReactivateAccountAction(){
        return new ReactivateAccountUserAction();
    }


    @Bean ConfigProviderImpl userConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean ConfigBasedEnablementStrategy userConfigBasedEnablementStrategy(
        @Qualifier("userConfigProvider") ConfigProvider configProvider,
        @Qualifier("userFlowStore") STMFlowStoreImpl stmFlowStore) {
        ConfigBasedEnablementStrategy enablementStrategy = new ConfigBasedEnablementStrategy(configProvider,PREFIX_FOR_PROPERTIES);
        stmFlowStore.setEnablementStrategy(enablementStrategy);
        return enablementStrategy;
    }



    @Bean SUPPLIER_APPROVEDUserPostSaveHook
        userSUPPLIER_APPROVEDPostSaveHook(){
            return new SUPPLIER_APPROVEDUserPostSaveHook();
    }

    @Bean PROFILE_COMPLETEUserPostSaveHook
        userPROFILE_COMPLETEPostSaveHook(){
            return new PROFILE_COMPLETEUserPostSaveHook();
    }

    @Bean ACTIVEUserPostSaveHook
        userACTIVEPostSaveHook(){
            return new ACTIVEUserPostSaveHook();
    }

    @Bean BANNEDUserPostSaveHook
        userBANNEDPostSaveHook(){
            return new BANNEDUserPostSaveHook();
    }

    @Bean PROFILE_INCOMPLETEUserPostSaveHook
        userPROFILE_INCOMPLETEPostSaveHook(){
            return new PROFILE_INCOMPLETEUserPostSaveHook();
    }

    @Bean DELETEDUserPostSaveHook
        userDELETEDPostSaveHook(){
            return new DELETEDUserPostSaveHook();
    }

    @Bean SUPPLIER_APPROVAL_PENDINGUserPostSaveHook
        userSUPPLIER_APPROVAL_PENDINGPostSaveHook(){
            return new SUPPLIER_APPROVAL_PENDINGUserPostSaveHook();
    }

    @Bean EMAIL_VERIFIEDUserPostSaveHook
        userEMAIL_VERIFIEDPostSaveHook(){
            return new EMAIL_VERIFIEDUserPostSaveHook();
    }

    @Bean VERIFICATION_EXPIREDUserPostSaveHook
        userVERIFICATION_EXPIREDPostSaveHook(){
            return new VERIFICATION_EXPIREDUserPostSaveHook();
    }

    @Bean CANCELLEDUserPostSaveHook
        userCANCELLEDPostSaveHook(){
            return new CANCELLEDUserPostSaveHook();
    }

    @Bean EMAIL_VERIFICATION_PENDINGUserPostSaveHook
        userEMAIL_VERIFICATION_PENDINGPostSaveHook(){
            return new EMAIL_VERIFICATION_PENDINGUserPostSaveHook();
    }

    @Bean INACTIVEUserPostSaveHook
        userINACTIVEPostSaveHook(){
            return new INACTIVEUserPostSaveHook();
    }

    @Bean SUSPENDEDUserPostSaveHook
        userSUSPENDEDPostSaveHook(){
            return new SUSPENDEDUserPostSaveHook();
    }

    @Bean REGISTEREDUserPostSaveHook
        userREGISTEREDPostSaveHook(){
            return new REGISTEREDUserPostSaveHook();
    }

}
