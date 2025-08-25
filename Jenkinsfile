pipeline {
  agent any
  options { timestamps() }

  environment {
    REGISTRY   = "docker.io"
    IMAGE      = "jeongjaeyoon/kkrap-app"
    GIT_SHA    = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
    VERSION    = "${GIT_SHA}-${env.BUILD_NUMBER}"
    LATEST     = "latest"

    SSH_HOST   = "ubuntu@ci.kkrap.cloud"      // 도메인 사용
    REMOTE_DIR = "/home/ubuntu/kkrap-app"
  }

  stages {
    stage('Checkout') { steps { checkout scm } }

    stage('Build (Gradle)') {
      steps { sh "./gradlew clean build -x test" }
      post { always {
               junit testResults: '**/build/test-results/test/*.xml', allowEmptyResults: true
               // or 파일 존재 체크
               // script {
               //   if (fileExists('build/test-results/test')) {
               //     junit testResults: 'build/test-results/test/*.xml'
               //   } else {
               //     echo 'No test reports found (tests skipped).'
               //   }
               // }
       } }
    }

    stage('Build & Push Image') {
      when { expression { env.BRANCH_NAME ==~ /release\/.*/ } }  // release/* 만
      steps {
        withCredentials([usernamePassword(
          credentialsId: 'dockerhub-backend',    // ← 백엔드용 Docker Hub 크리덴셜
          usernameVariable: 'USER',
          passwordVariable: 'PASS'
        )]) {
          sh """
            docker build -t ${REGISTRY}/${IMAGE}:${VERSION} .
            docker tag  ${REGISTRY}/${IMAGE}:${VERSION} ${REGISTRY}/${IMAGE}:${LATEST}
            echo $PASS | docker login -u $USER --password-stdin
            docker push ${REGISTRY}/${IMAGE}:${VERSION}
            docker push ${REGISTRY}/${IMAGE}:${LATEST}
            docker logout ${REGISTRY} || true
          """
        }
      }
    }

    stage('Deploy to EC2') {
      when { expression { env.BRANCH_NAME ==~ /release\/.*/ } }
      steps {
        sshagent(credentials: ['ec2-ssh']) {
          sh """
            ssh -o StrictHostKeyChecking=no ${SSH_HOST} \
              'cd ${REMOTE_DIR} && ./deploy.sh ${VERSION} || ./deploy.sh ${LATEST}'
          """
        }
      }
    }
  }

  post {
    success { echo "Backend deployed: ${env.BRANCH_NAME} → ${VERSION}" }
    failure { echo "Backend failed: ${env.BRANCH_NAME}" }
  }
}
