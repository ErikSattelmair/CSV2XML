FROM tomee:latest
LABEL maintainer="Erik Sattelmair"

COPY target/CSV2XMLConverter.war /usr/local/tomee/webapps/

EXPOSE 8080

CMD ["catalina.sh", "run"]
