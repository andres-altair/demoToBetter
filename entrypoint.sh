#!/bin/sh
# entrypoint.sh
export SPRING_PROFILES_ACTIVE=prod
export SPRING_DATASOURCE_URL=$(tr -d '\n' < /run/secrets/pg_url)
export SPRING_DATASOURCE_USERNAME=$(tr -d '\n' < /run/secrets/pg_user)
export SPRING_DATASOURCE_PASSWORD=$(tr -d '\n' < /run/secrets/pg_password)
export JWT_SECRET=$(tr -d '\n' < /run/secrets/jwt_secret)

exec java -jar /app/app.jar


