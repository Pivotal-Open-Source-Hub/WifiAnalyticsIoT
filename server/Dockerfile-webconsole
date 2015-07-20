FROM		java:8u45

COPY 		WebConsole WebConsole
WORKDIR 	WebConsole
RUN		./gradlew build jar

EXPOSE		8080
CMD		./gradlew run
