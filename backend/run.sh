#!/bin/bash

# Configuración
PORT=${PORT:-4000}
ACTIVE_PROFILE=${ACTIVE_PROFILE:-dev}
JAR_PATH="app.jar"
DEV_MODE=${DEV_MODE:-false}

function error_exit {
    echo "[ERROR] $1" >&2
    exit 1
}

function run_app {
    local args="-Dserver.port=$PORT -Dspring.profiles.active=$ACTIVE_PROFILE"
    
    if [ "$DEV_MODE" = "true" ]; then
        echo "[INFO] Iniciando en modo desarrollo (con recarga automática)"
        args="$args -Dspring.devtools.restart.enabled=true"
    fi

    echo "[INFO] Iniciando aplicación Spring Boot..."
    echo "[DEBUG] Parámetros: $args"
    java $args -jar $JAR_PATH || error_exit "Falló al iniciar la aplicación"
}

run_app