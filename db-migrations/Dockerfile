FROM liquibase/liquibase:4.27
COPY src/main/resources/db /liquibase/changelog/db
ENTRYPOINT ["liquibase"]
CMD ["--changelog-file=db/changelog/db.changelog-master.xml", "update"]
