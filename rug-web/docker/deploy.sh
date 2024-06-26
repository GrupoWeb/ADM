#!/bin/bash
set -e

asadmin start-domain &

echo "Esperando que Payara esté completamente inicializado..."
until asadmin list-applications --user admin --passwordfile /opt/payara/passwordFile > /dev/null 2>&1; do
  echo "Esperando que Payara inicie..."
  sleep 5
done


for file in $DEPLOY_DIR/*.{war,ear}; do
  if [[ -f "$file" ]]; then
    echo "Desplegando $file..."
    asadmin --user admin --passwordfile /opt/payara/passwordFile deploy --force=true "$file"

    if [ $? -eq 0 ]; then
      echo "$file desplegado exitosamente."
    else
      echo "Fallo al desplegar $file."
    fi
  fi
done

tail -f /opt/payara/appserver/glassfish/domains/domain1/logs/server.log
