@echo off
setlocal
set SCRIPT_DIR=%~dp0
set REPO_ROOT=%SCRIPT_DIR%..

pushd "%REPO_ROOT%"
docker compose up -d postgres keycloak
call ".\mvnw.cmd" spring-boot:run -Dspring-boot.run.arguments="--debug --logging.level.com.backend=debug --logging.level.org.springframework.security=debug" -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
set EXIT_CODE=%ERRORLEVEL%
popd
exit /b %EXIT_CODE%
