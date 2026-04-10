@echo off
setlocal
set SCRIPT_DIR=%~dp0
set REPO_ROOT=%SCRIPT_DIR%..

pushd "%REPO_ROOT%"
docker compose up -d postgres keycloak
call ".\mvnw.cmd" spring-boot:run -Dspring-boot.run.arguments="--spring.main.banner-mode=off --logging.level.root=warn --logging.level.com.backend=info --logging.level.org.springframework=warn --logging.level.org.flywaydb=info"
set EXIT_CODE=%ERRORLEVEL%
popd
exit /b %EXIT_CODE%
