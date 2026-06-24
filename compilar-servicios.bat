@echo off

SET servicios=eureka-server api-gateway auth category product inventory cart order shipping notification review payment

for %%s in (%servicios%) do (
    if exist %%s (
        echo Compilando [%%s]...

        cd %%s
        call mvnw clean package
        cd ..
    ) else (
        echo Servicio [%%s] no existe, saltando...
    )
)

echo Listo.