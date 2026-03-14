//package com.homebase.ecom.cart;
//
//import org.testcontainers.containers.GenericContainer;
//import org.testcontainers.containers.Network;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.images.builder.ImageFromDockerfile;
//import org.testcontainers.containers.output.Slf4jLogConsumer;
//import org.testcontainers.containers.wait.strategy.Wait;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.ApplicationContextInitializer;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.test.context.support.TestPropertySourceUtils;
//
//import java.time.Duration;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.io.File;
//import java.nio.file.Files;
//import java.util.stream.Stream;
//
//public class QueryServiceContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
//    static {
//        System.setProperty("DOCKER_API_VERSION", "1.44");
//        System.setProperty("docker.api.version", "1.44");
//        // Force early initialization of the Docker client factory to pick up the properties
//        try {
//            org.testcontainers.DockerClientFactory.instance().client();
//        } catch (Exception e) {
//            // Ignore errors here, they will be caught during container start
//        }
//    }
//
//    private static final Logger logger = LoggerFactory.getLogger(QueryServiceContainerInitializer.class);
//
//    private static final Network network = Network.newNetwork();
//
//    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
//            .withDatabaseName("ecommerce_db")
//            .withUsername("postgres")
//            .withPassword("password")
//            .withNetwork(network)
//            .withNetworkAliases("postgres")
//            .withStartupTimeout(Duration.ofMinutes(5));
//
//    private static final GenericContainer<?> queryContainer = new GenericContainer<>(
//            new ImageFromDockerfile("query-build", false)
//                    .withFileFromString("Dockerfile",
//                            "FROM eclipse-temurin:25-jre\n" +
//                            "WORKDIR /app\n" +
//                            "COPY query-build.jar app.jar\n" +
//                            "EXPOSE 8081\n" +
//                            "ENTRYPOINT [\"java\", \"-Xmx512m\", \"-jar\", \"app.jar\"]\n")
//                    .withFileFromPath("query-build.jar", discoverQueryJar()))
//            .withNetwork(network)
//            .withExposedPorts(8081)
//            .withEnv("SPRING_DATASOURCE_URL", "jdbc:postgresql://postgres:5432/ecommerce_db")
//            .withEnv("SPRING_DATASOURCE_USERNAME", "postgres")
//            .withEnv("SPRING_DATASOURCE_PASSWORD", "password")
//            .withEnv("QUERY_DATASOURCES_DEFAULT_JDBCURL", "jdbc:postgresql://postgres:5432/ecommerce_db")
//            .withEnv("QUERY_DATASOURCES_DEFAULT_USERNAME", "postgres")
//            .withEnv("QUERY_DATASOURCES_DEFAULT_PASSWORD", "password")
//            .withEnv("QUERY_DEFAULT-TENANT-ID", "default")
//            .withStartupTimeout(Duration.ofMinutes(5))
//            .waitingFor(Wait.forHttp("/info").forPort(8081))
//            .dependsOn(postgres);
//
//    private static Path discoverQueryJar() {
//        String[] potentialPaths = {
//            "target/test-dependencies/query-build.jar",
//            "../../build/query-build/target/query-build-0.0.1-SNAPSHOT.jar",
//            "../build/query-build/target/query-build-0.0.1-SNAPSHOT.jar",
//            "build/query-build/target/query-build-0.0.1-SNAPSHOT.jar"
//        };
//
//        for (String pathStr : potentialPaths) {
//            Path path = Paths.get(pathStr).toAbsolutePath().normalize();
//            File file = path.toFile();
//            if (file.exists() && file.isFile() && file.length() > 1000000) { // At least 1MB
//                logger.info("Found valid query-build JAR at: {} (Size: {} MB)",
//                    path, file.length() / (1024 * 1024));
//                return path;
//            }
//        }
//
//        // Search for any JAR in build/query-build/target if the specific version name changed
//        try {
//            Path searchDir = Paths.get("../../build/query-build/target").toAbsolutePath().normalize();
//            if (Files.exists(searchDir)) {
//                try (Stream<Path> stream = Files.find(searchDir, 1, (p, attr) ->
//                    p.toString().endsWith(".jar") && !p.toString().endsWith("-sources.jar"))) {
//                    Path fallback = stream.findFirst().orElse(null);
//                    if (fallback != null && fallback.toFile().length() > 1000000) {
//                         logger.info("Found fallback query-build JAR at: {}", fallback);
//                         return fallback;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            // Ignore search errors
//        }
//
//        logger.error("COULD NOT FIND A VALID query-build JAR! Expected around 135MB. Tests will fail.");
//        return Paths.get("MISSING_JAR_FILE");
//    }
//
//    @Override
//    public void initialize(ConfigurableApplicationContext context) {
//        postgres.start();
//        queryContainer.withLogConsumer(new Slf4jLogConsumer(logger)).start();
//
//        String queryUrl = "http://" + queryContainer.getHost() + ":" + queryContainer.getMappedPort(8081) + "/";
//
//        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(context,
//                "chenile.query.proxy.enabled=true",
//                "chenile.query.remote.base-url=" + queryUrl
//        );
//    }
//}
