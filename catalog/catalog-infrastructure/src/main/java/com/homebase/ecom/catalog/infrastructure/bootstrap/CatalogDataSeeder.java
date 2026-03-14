package com.homebase.ecom.catalog.infrastructure.bootstrap;

import com.homebase.ecom.catalog.model.Collection;
import com.homebase.ecom.catalog.model.CollectionType;
import com.homebase.ecom.catalog.repository.CollectionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Bootstraps the database with example Dynamic Collections.
 * Moved to infrastructure as it is a data-loading adapter.
 */
@Component
public class CatalogDataSeeder implements CommandLineRunner {

    private final CollectionRepository collectionRepository;

    public CatalogDataSeeder(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (collectionRepository.count() > 0) {
            return;
        }

        System.out.println("SEEDING CATALOG DATA...");

        createCollection(
                "Under $50",
                "under-50",
                CollectionType.DYNAMIC,
                "price < 50.00");

        createCollection(
                "Summer Vibes",
                "summer-vibes",
                CollectionType.DYNAMIC,
                "name.contains('Summer') or name.contains('Sun')");

        createCollection(
                "Premium Selection",
                "premium-selection",
                CollectionType.DYNAMIC,
                "price > 100.00 and active == true");

        System.out.println("CATALOG DATA SEEDING COMPLETE.");
    }

    private void createCollection(String name, String slug, CollectionType type, String rule) {
        Collection c = new Collection();
        c.setName(name);
        c.setSlug(slug);
        c.setType(type);
        c.setRuleExpression(rule);
        c.setAutoUpdate(true);
        c.setActive(true);
        c.setProductCount(0L);
        collectionRepository.save(c);
    }
}
