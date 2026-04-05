package com.homebase.ecom.query.service.sync;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ETL Sync Service — populates rules engine OLAP tables from OLTP.
 *
 * Tables synced:
 *   - hm_query.rule_sets_flat
 *   - hm_query.rules_flat
 *   - hm_query.decisions_flat
 *   - hm_query.fact_definitions_flat
 *
 * Runs nightly at 3:10 AM IST.
 */
public class RulesEngineSyncService {

    private static final Logger log = LoggerFactory.getLogger(RulesEngineSyncService.class);

    private final SqlSessionFactory sqlSessionFactory;

    public RulesEngineSyncService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Scheduled(cron = "0 10 3 * * *", zone = "Asia/Kolkata")
    public void syncAll() {
        log.info("Starting rules engine OLAP sync...");
        long start = System.currentTimeMillis();
        try {
            syncRuleSetsFlat();
            syncRulesFlat();
            syncDecisionsFlat();
            syncFactDefinitionsFlat();
            log.info("Rules engine OLAP sync completed in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Rules engine OLAP sync FAILED", e);
            throw e;
        }
    }

    public void syncRuleSetsFlat() {
        log.info("Syncing rule_sets_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("RulesEngineSync.truncateRuleSetsFlat");
            int rows = session.insert("RulesEngineSync.syncRuleSetsFlat");
            session.commit();
            log.info("rule_sets_flat: {} rows synced", rows);
        }
    }

    public void syncRulesFlat() {
        log.info("Syncing rules_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("RulesEngineSync.truncateRulesFlat");
            int rows = session.insert("RulesEngineSync.syncRulesFlat");
            session.commit();
            log.info("rules_flat: {} rows synced", rows);
        }
    }

    public void syncDecisionsFlat() {
        log.info("Syncing decisions_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("RulesEngineSync.truncateDecisionsFlat");
            int rows = session.insert("RulesEngineSync.syncDecisionsFlat");
            session.commit();
            log.info("decisions_flat: {} rows synced", rows);
        }
    }

    public void syncFactDefinitionsFlat() {
        log.info("Syncing fact_definitions_flat...");
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("RulesEngineSync.truncateFactDefinitionsFlat");
            int rows = session.insert("RulesEngineSync.syncFactDefinitionsFlat");
            session.commit();
            log.info("fact_definitions_flat: {} rows synced", rows);
        }
    }
}
