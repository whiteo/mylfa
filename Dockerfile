FROM --platform=$BUILDPLATFORM eclipse-temurin:20-jre-alpine

RUN mkdir -p /app/mylfa/languages

WORKDIR /app

COPY target/mylfa.jar /app/mylfa.jar

# env values are stored in .env file
ENV DATABASE_URL ${DATABASE_URL}
ENV DATABASE_USER ${DATABASE_USER}
ENV DATABASE_PWD ${DATABASE_PWD}
ENV APP_LOG_LEVEL ${APP_LOG_LEVEL}
ENV TOKEN_EXPIRATION_TIME ${TOKEN_EXPIRATION_TIME}
ENV TOKEN_SECRET_KEY ${TOKEN_SECRET_KEY}
ENV DEMO_ONLY ${DEMO_ONLY}

EXPOSE 8080/tcp

RUN chown -R nobody:nobody /app/mylfa

USER nobody

CMD ["java", "-XX:MaxRAMPercentage=60.0", "-jar", "mylfa.jar"]