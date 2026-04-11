@echo off
setlocal
set SCRIPT_DIR=%~dp0
set REPO_ROOT=%SCRIPT_DIR%..
set KEYCLOAK_DISCOVERY_URL=http://localhost:8090/realms/backend-java/.well-known/openid-configuration

pushd "%REPO_ROOT%"
docker compose up -d postgres keycloak
if errorlevel 1 (
  set EXIT_CODE=%ERRORLEVEL%
  popd
  exit /b %EXIT_CODE%
)

powershell -NoProfile -ExecutionPolicy Bypass -Command "$deadline=(Get-Date).AddMinutes(2); $url='%KEYCLOAK_DISCOVERY_URL%'; do { try { $response = Invoke-WebRequest -UseBasicParsing -Uri $url -TimeoutSec 5; if ($response.StatusCode -eq 200) { exit 0 } } catch { } Start-Sleep -Seconds 2 } while ((Get-Date) -lt $deadline); Write-Error ('Keycloak discovery endpoint not ready: ' + $url); exit 1"
if errorlevel 1 (
  set EXIT_CODE=%ERRORLEVEL%
  popd
  exit /b %EXIT_CODE%
)

call ".\mvnw.cmd" spring-boot:run -Dspring-boot.run.arguments="--debug --logging.level.com.backend=debug --logging.level.org.springframework.security=debug" -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
set EXIT_CODE=%ERRORLEVEL%
popd
exit /b %EXIT_CODE%
