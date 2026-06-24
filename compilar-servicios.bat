@echo off
setlocal

set "servicios=eureka-server api-gateway auth category product inventory cart order shipping notification review payment"
set "args=%*"

for %%s in (%servicios%) do (
    if exist "%%s" (
        echo Compilando [%%s]...

        cd "%%s"
        call mvnw clean package %args%
        cd ..
    ) else (
        echo Servicio [%%s] no existe, saltando...
    )
)

echo Listo.
endlocal