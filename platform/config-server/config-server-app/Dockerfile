# ---- Build stage ----
FROM eclipse-temurin:25-jdk-alpine AS builder
WORKDIR /build
COPY target/config-server-app-*.jar app.jar
RUN java -Djarmode=tools -jar app.jar extract --layers --launcher

# ---- Runtime stage ----
FROM eclipse-temurin:25-jre-alpine
LABEL maintainer="homebase-platform"

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Git client needed for Git-backed config server
RUN apk add --no-cache git openssh-client

WORKDIR /app

COPY --from=builder /build/app/dependencies/ ./
COPY --from=builder /build/app/spring-boot-loader/ ./
COPY --from=builder /build/app/snapshot-dependencies/ ./
COPY --from=builder /build/app/application/ ./

# SSH config directory for private Git repo access
RUN mkdir -p /home/appuser/.ssh && \
    chown -R appuser:appgroup /home/appuser/.ssh && \
    chmod 700 /home/appuser/.ssh

USER appuser

EXPOSE 8888

HEALTHCHECK --interval=30s --timeout=5s --retries=3 \
    CMD wget -qO- http://localhost:8888/actuator/health/liveness || exit 1

ENTRYPOINT ["java", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+UseG1GC", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "org.springframework.boot.loader.launch.JarLauncher"]
