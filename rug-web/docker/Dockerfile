FROM jjolon/rgm:1.0.2

USER root

COPY deploy.sh /opt/payara/deploy.sh
RUN chmod +x /opt/payara/deploy.sh

EXPOSE 8080 4848

CMD ["/opt/payara/deploy.sh"]
