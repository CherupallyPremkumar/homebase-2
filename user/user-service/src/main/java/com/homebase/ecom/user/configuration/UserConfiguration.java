package com.homebase.ecom.user.configuration;

import com.homebase.ecom.user.domain.model.User;
import com.homebase.ecom.user.domain.port.UserEventPublisher;
import com.homebase.ecom.user.domain.port.UserRepository;
import com.homebase.ecom.user.infrastructure.event.UserEventPublisherImpl;
import com.homebase.ecom.user.infrastructure.persistence.adapter.UserJpaRepository;
import com.homebase.ecom.user.infrastructure.persistence.adapter.UserRepositoryImpl;
import com.homebase.ecom.user.infrastructure.persistence.mapper.UserMapper;
import com.homebase.ecom.user.service.cmds.*;
import com.homebase.ecom.user.service.impl.UserServiceImpl;
import com.homebase.ecom.user.service.postSaveHooks.*;
import com.homebase.ecom.user.service.store.UserEntityStore;
import org.chenile.stm.*;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.impl.*;
import org.chenile.stm.spring.SpringBeanFactoryAdapter;
import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.chenile.workflow.service.stmcmds.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * UserConfiguration — explicit bean wiring for the User bounded context.
 *
 * Rules (from gemini.md):
 *  - No @Component, @Service, @Repository anywhere
 *  - Constructor injection everywhere
 *  - Action beans: user{EventId}Action (auto-resolved by STM)
 *  - Post-save hook beans: user{STATE}PostSaveHook (auto-resolved by DefaultPostSaveHook)
 */
@Configuration
public class UserConfiguration {

    private static final String FLOW_DEFINITION_FILE = "com/homebase/ecom/user/user-states.xml";
    public static final String PREFIX = "user";

    // ─── Event Publisher ──────────────────────────────────────────────────────

    @Bean
    public UserEventPublisher userEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new UserEventPublisherImpl(applicationEventPublisher);
    }

    // ─── STM Infrastructure ──────────────────────────────────────────────────

    @Bean BeanFactoryAdapter userBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean STMFlowStoreImpl userFlowStore(
            @Qualifier("userBeanFactoryAdapter") BeanFactoryAdapter bfa) throws Exception {
        STMFlowStoreImpl store = new STMFlowStoreImpl();
        store.setBeanFactory(bfa);
        return store;
    }

    @Bean STM<User> userEntityStm(
            @Qualifier("userFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception {
        STMImpl<User> stm = new STMImpl<>();
        stm.setStmFlowStore(stmFlowStore);
        return stm;
    }

    @Bean STMActionsInfoProvider userActionsInfoProvider(
            @Qualifier("userFlowStore") STMFlowStoreImpl stmFlowStore) {
        STMActionsInfoProvider provider = new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider(PREFIX, provider);
        return provider;
    }

    @Bean XmlFlowReader userFlowReader(
            @Qualifier("userFlowStore") STMFlowStoreImpl flowStore) throws Exception {
        XmlFlowReader reader = new XmlFlowReader(flowStore);
        reader.setFilename(FLOW_DEFINITION_FILE);
        return reader;
    }

    // ─── Repository + Entity Store ────────────────────────────────────────────

    @Bean
    public UserMapper userMapper() {
        return new UserMapper();
    }

    @Bean
    public UserRepository userRepository(UserJpaRepository jpaRepository, UserMapper mapper) {
        return new UserRepositoryImpl(jpaRepository, mapper);
    }

    @Bean EntityStore<User> userEntityStore(UserRepository userRepository) {
        return new UserEntityStore(userRepository);
    }

    // ─── Service ──────────────────────────────────────────────────────────────

    @Bean StateEntityServiceImpl<User> _userStateEntityService_(
            @Qualifier("userEntityStm") STM<User> stm,
            @Qualifier("userActionsInfoProvider") STMActionsInfoProvider provider,
            @Qualifier("userEntityStore") EntityStore<User> entityStore) {
        return new UserServiceImpl(stm, provider, entityStore);
    }

    // ─── STM Wiring ──────────────────────────────────────────────────────────

    @Bean STMTransitionActionResolver userTransitionActionResolver(
            @Qualifier("defaultUserSTMTransitionAction") STMTransitionAction<User> defaultAction) {
        return new STMTransitionActionResolver(PREFIX, defaultAction, true);
    }

    @Bean STMTransitionAction<User> defaultUserSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean DefaultPostSaveHook<User> userDefaultPostSaveHook(
            @Qualifier("userTransitionActionResolver") STMTransitionActionResolver resolver) {
        return new DefaultPostSaveHook<>(resolver);
    }

    @Bean GenericEntryAction<User> userEntryAction(
            @Qualifier("userEntityStore") EntityStore<User> entityStore,
            @Qualifier("userActionsInfoProvider") STMActionsInfoProvider provider,
            @Qualifier("userFlowStore") STMFlowStoreImpl stmFlowStore,
            @Qualifier("userDefaultPostSaveHook") DefaultPostSaveHook<User> postSaveHook) {
        GenericEntryAction<User> entryAction = new GenericEntryAction<>(entityStore, provider, postSaveHook);
        stmFlowStore.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean GenericExitAction<User> userExitAction(
            @Qualifier("userFlowStore") STMFlowStoreImpl stmFlowStore) {
        GenericExitAction<User> exitAction = new GenericExitAction<>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
    }

    @Bean STMTransitionAction<User> userBaseTransitionAction(
            @Qualifier("userTransitionActionResolver") STMTransitionActionResolver resolver,
            @Qualifier("userActivityChecker") ActivityChecker activityChecker,
            @Qualifier("userFlowStore") STMFlowStoreImpl stmFlowStore) {
        BaseTransitionAction<User> baseAction = new BaseTransitionAction<>(resolver);
        baseAction.activityChecker = activityChecker;
        stmFlowStore.setDefaultTransitionAction(baseAction);
        return baseAction;
    }

    @Bean ActivityChecker userActivityChecker(
            @Qualifier("userFlowStore") STMFlowStoreImpl stmFlowStore) {
        return new ActivityChecker(stmFlowStore);
    }

    @Bean AreActivitiesComplete userActivitiesCompletionCheck(
            @Qualifier("userActivityChecker") ActivityChecker activityChecker) {
        return new AreActivitiesComplete(activityChecker);
    }

    @Bean StmBodyTypeSelector userBodyTypeSelector(
            @Qualifier("userActionsInfoProvider") STMActionsInfoProvider provider,
            @Qualifier("userTransitionActionResolver") STMTransitionActionResolver resolver) {
        return new StmBodyTypeSelector(provider, resolver);
    }

    @Bean DefaultAutomaticStateComputation<User> userDefaultAutoState(
            @Qualifier("userTransitionActionResolver") STMTransitionActionResolver resolver,
            @Qualifier("userFlowStore") STMFlowStoreImpl stmFlowStore) {
        DefaultAutomaticStateComputation<User> autoState = new DefaultAutomaticStateComputation<>(resolver);
        stmFlowStore.setDefaultAutomaticStateComputation(autoState);
        return autoState;
    }

    // ─── STM Actions (user{EventId}Action — auto-resolved by prefix) ─────────

    @Bean STMTransitionAction<User> userRegisterAction()         { return new RegisterUserAction(); }
    @Bean STMTransitionAction<User> userVerifyEmailAction()      { return new VerifyEmailAction(); }
    @Bean STMTransitionAction<User> userLockAccountAction()      { return new LockAccountAction(); }
    @Bean STMTransitionAction<User> userUnlockAccountAction()    { return new UnlockAccountAction(); }
    @Bean STMTransitionAction<User> userSuspendUserAction()      { return new SuspendUserAction(); }
    @Bean STMTransitionAction<User> userReinstateUserAction()    { return new ReinstateUserAction(); }
    @Bean STMTransitionAction<User> userUpdateProfileAction()    { return new UpdateProfileAction(); }
    @Bean STMTransitionAction<User> userAddAddressAction()       { return new AddAddressAction(); }
    @Bean STMTransitionAction<User> userDeleteAccountAction()    { return new DeleteAccountAction(); }
    @Bean STMTransitionAction<User> userResendVerificationAction() { return new ResendVerificationAction(); }
    @Bean STMTransitionAction<User> userChangePasswordAction()    { return new ChangePasswordAction(); }
    @Bean STMTransitionAction<User> userRemoveAddressAction()     { return new RemoveAddressAction(); }

    // ─── Post-Save Hooks ({STATE}UserPostSaveHook — auto-resolved) ───────────

    @Bean PENDING_VERIFICATIONUserPostSaveHook userPENDING_VERIFICATIONPostSaveHook(
            UserEventPublisher eventPublisher) {
        return new PENDING_VERIFICATIONUserPostSaveHook(eventPublisher);
    }

    @Bean ACTIVEUserPostSaveHook userACTIVEPostSaveHook(UserEventPublisher eventPublisher) {
        return new ACTIVEUserPostSaveHook(eventPublisher);
    }

    @Bean LOCKEDUserPostSaveHook userLOCKEDPostSaveHook(UserEventPublisher eventPublisher) {
        return new LOCKEDUserPostSaveHook(eventPublisher);
    }

    @Bean SUSPENDEDUserPostSaveHook userSUSPENDEDPostSaveHook(UserEventPublisher eventPublisher) {
        return new SUSPENDEDUserPostSaveHook(eventPublisher);
    }

    @Bean DELETEDUserPostSaveHook userDELETEDPostSaveHook(UserEventPublisher eventPublisher) {
        return new DELETEDUserPostSaveHook(eventPublisher);
    }
}
