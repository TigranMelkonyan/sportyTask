pipeline {
    agent any

    environment {
        DOCKER_HUB_CREDS = credentials('docker-hub-credentials')
        EC2_HOST = "${env.EC2_HOST}"   
        EC2_USER = "${env.EC2_USER}"
        RDS_HOST = "${env.RDS_HOST}"
        RDS_USER = "${env.RDS_USER}"
        RDS_PASSWORD = "${env.RDS_PASSWORD}"
    } 

    stages {
        stage('Checkout') {
            steps {
                git branch: 'jenkins-setup', url: 'git@github.com:TigranMelkonyan/sportyTask.git'
            }
        }

        stage('Build Docker Images') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'docker-hub-credentials',
                    passwordVariable: 'DOCKER_PASSWORD',
                    usernameVariable: 'DOCKER_USERNAME'
                )]) {
                    sh '''
                        echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
                        docker build -t $DOCKER_USERNAME/bookstore:latest ./bookstore
                        docker build -t $DOCKER_USERNAME/iam:latest ./iam
                    '''
                }
            }
        }

        stage('Push Docker Images') {
            steps {
                sh """
                docker push $DOCKER_HUB_CREDS_USR/bookstore:latest
                docker push $DOCKER_HUB_CREDS_USR/iam:latest
                """
            }
        }

        stage('Deploy to EC2') {
            steps {
                sshagent(['aws-ec2-key']) {
                    sh """
                    ssh -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} '
                        # Login to Docker Hub on EC2
                        echo "${DOCKER_HUB_CREDS_PSW}" | docker login -u "${DOCKER_HUB_CREDS_USR}" --password-stdin
                        
                        # Pull the latest images
                        docker pull $DOCKER_HUB_CREDS_USR/bookstore:latest
                        docker pull $DOCKER_HUB_CREDS_USR/iam:latest
                        
                        # Stop and remove existing containers
                        docker stop bookstore || true
                        docker rm bookstore || true
                        docker stop iam || true
                        docker rm iam || true
                        
                        # Run new containers
                        docker run -d --name bookstore -p 8080:8080 \
                            -e SPRING_DATASOURCE_URL="jdbc:mysql://${RDS_HOST}:3306/sporty_db" \
                            -e SPRING_DATASOURCE_USERNAME="${RDS_USER}" \
                            -e SPRING_DATASOURCE_PASSWORD="${RDS_PASSWORD}" \
                            $DOCKER_HUB_CREDS_USR/bookstore:latest
                            
                        docker run -d --name iam -p 8081:8081 \
                            -e SPRING_DATASOURCE_URL="jdbc:mysql://${RDS_HOST}:3306/sporty_iam" \
                            -e SPRING_DATASOURCE_USERNAME="${RDS_USER}" \
                            -e SPRING_DATASOURCE_PASSWORD="${RDS_PASSWORD}" \
                            $DOCKER_HUB_CREDS_USR/iam:latest
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