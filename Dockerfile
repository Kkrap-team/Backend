
# 기본 이미지는 jdk 17
FROM openjdk:21

#빌드 시점에 사용될 변수를 정의
#JAR_FILE은 빌드 컨텍스트 내의 jar 파일 경로를 지정하는 데 사용
ARG JAR_FILE=build/libs/Kkrap-0.0.1-SNAPSHOT.jar

# ARG에서 정의한 JAR_FILE 경로의 jar 파일을 컨테이너 내 app.jar로 복사
COPY ${JAR_FILE} app.jar

# 컨테이너가 시작될 때 실행될 명령어 정의
ENTRYPOINT ["java", "-jar", "app.jar"]