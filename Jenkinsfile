pipeline {
    agent any

    environment {
        DOCKER_HUB_USERNAME = credentials('docker-hub-username')
        DOCKER_HUB_ACCESS_TOKEN = credentials('docker-hub-token')
        EC2_HOST = credentials('ec2-host')   
        EC2_USER = credentials('ec2-user')
        RDS_HOST = credentials('rds-host')
        RDS_USER = credentials('rds-user')
        RDS_PASSWORD = credentials('rds-password')
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'jenkins-setup', url: 'git@github.com:TigranMelkonyan/sportyTask.git'
            }
        }

        stage('Build Docker Images') {
            steps {
                sh """
                echo $DOCKER_HUB_ACCESS_TOKEN | docker login -u $DOCKER_HUB_USERNAME --password-stdin
                docker build -t $DOCKER_HUB_USERNAME/bookstore:latest ./bookstore
                docker build -t $DOCKER_HUB_USERNAME/iam:latest ./iam
                """
            }
        }

        stage('Push Docker Images') {
            steps {
                sh """
                docker push $DOCKER_HUB_USERNAME/bookstore:latest
                docker push $DOCKER_HUB_USERNAME/iam:latest
                """
            }
        }

        stage('Deploy to EC2') {
            steps {
                sshagent(['aws-ec2-key']) {
                    sh """
                    ssh -o StrictHostKeyChecking=no $EC2_USER@$EC2_HOST '
                        docker pull $DOCKER_HUB_USERNAME/bookstore:latest &&
                        docker pull $DOCKER_HUB_USERNAME/iam:latest &&
                        docker stop bookstore || true &&
                        docker rm bookstore || true &&
                        docker run -d --name bookstore -p 8080:8080 -e SPRING_DATASOURCE_URL=jdbc:mysql://$RDS_HOST:3306/sporty_db -e SPRING_DATASOURCE_USERNAME=$RDS_USER -e SPRING_DATASOURCE_PASSWORD=$RDS_PASSWORD $DOCKER_HUB_USERNAME/bookstore:latest &&
                        docker stop iam || true &&
                        docker rm iam || true &&
                        docker run -d --name iam -p 8081:8081 -e SPRING_DATASOURCE_URL=jdbc:mysql://$RDS_HOST:3306/sporty_iam -e SPRING_DATASOURCE_USERNAME=$RDS_USER -e SPRING_DATASOURCE_PASSWORD=$RDS_PASSWORD $DOCKER_HUB_USERNAME/iam:latest
                    '
                    """
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished'
        }
        success {
            echo 'Deployment succeeded!'
        }
        failure {
            echo 'Deployment failed!'
        }
    }
}