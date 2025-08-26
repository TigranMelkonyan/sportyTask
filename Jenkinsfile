pipeline {
    agent any

    environment {
        EC2_HOST = "${env.EC2_HOST}"   
        EC2_USER = "${env.EC2_USER}"
        RDS_HOST = "${env.RDS_HOST}"
    } 

    stages {       
        stage('Checkout') {
            steps {
                git(
                    branch: 'jenkins-setup', 
                    url: 'https://github.com/TigranMelkonyan/sportyTask.git',
                    credentialsId: 'github-cred'
                )
            }
        }

        stage('Build with Maven') {
            steps {
                dir('./bookstore') {
                    sh 'mvn clean package -DskipTests'
                }
                dir('./iam') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
        
        stage('Build Docker Images') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', 
                                                usernameVariable: 'DOCKER_USER', 
                                                passwordVariable: 'DOCKER_PASSWORD')]) {
                    sh '''
                        echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USER" --password-stdin
                        docker build -t $DOCKER_USER/bookstore:latest ./bookstore
                        docker build -t $DOCKER_USER/iam:latest ./iam
                    '''
                }
            }
        }

        stage('Push Docker Images') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', 
                                                usernameVariable: 'DOCKER_USER', 
                                                passwordVariable: 'DOCKER_PASSWORD')]) {
                    sh '''
                        docker push $DOCKER_USER/bookstore:latest
                        docker push $DOCKER_USER/iam:latest
                    '''
                }
            }
        }

        stage('Deploy to EC2') {
            steps {
                withCredentials([
                    usernamePassword(credentialsId: 'docker-hub-credentials', 
                                   usernameVariable: 'DOCKER_USER', 
                                   passwordVariable: 'DOCKER_PASSWORD'),
                    usernamePassword(credentialsId: 'rds-credentials', 
                                   usernameVariable: 'RDS_USER', 
                                   passwordVariable: 'RDS_PASSWORD'),
                    sshUserPrivateKey(credentialsId: 'aws-ec2-key', 
                                    keyFileVariable: 'SSH_KEY',
                                    usernameVariable: 'SSH_USER')
                ]) {
                    sshagent(['aws-ec2-key']) {
                        sh """
                            ssh -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} '
                                # Login to Docker Hub on EC2
                                echo "${DOCKER_PASSWORD}" | docker login -u "${DOCKER_USER}" --password-stdin
                                
                                # Pull the latest images
                                docker pull ${DOCKER_USER}/bookstore:latest
                                docker pull ${DOCKER_USER}/iam:latest
                                
                                # Stop and remove existing containers
                                docker stop bookstore || true
                                docker rm bookstore || true
                                docker stop iam || true
                                docker rm iam || true
                                
                                # Run new containers
                                docker run -d --name bookstore -p 8080:8080 \\
                                    -e SPRING_DATASOURCE_URL="jdbc:mysql://${RDS_HOST}:3306/sporty_db" \\
                                    -e SPRING_DATASOURCE_USERNAME="${RDS_USER}" \\
                                    -e SPRING_DATASOURCE_PASSWORD="${RDS_PASSWORD}" \\
                                    ${DOCKER_USER}/bookstore:latest
                                    
                                docker run -d --name iam -p 8081:8081 \\
                                    -e SPRING_DATASOURCE_URL="jdbc:mysql://${RDS_HOST}:3306/sporty_iam" \\
                                    -e SPRING_DATASOURCE_USERNAME="${RDS_USER}" \\
                                    -e SPRING_DATASOURCE_PASSWORD="${RDS_PASSWORD}" \\
                                    ${DOCKER_USER}/iam:latest
                            '
                        """
                    }
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