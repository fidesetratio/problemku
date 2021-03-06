def getHost(){
    def remote = [:]
    remote.name = 'master'
    remote.host = '128.21.33.43'
    remote.user = 'administrator'
    remote.port = 22
    remote.password = 'Ajs123456'
    remote.allowAnyHosts = true
    return remote
}

pipeline {
    agent any
      environment {
           PIPELINE_BUILD_IMAGE = "api-vega"
           PIPELINE_NAME_SPACE = "vega"
           PIPELINE_REPLICA = 2
          // PIPELINE_LOAD_BALANCER_IP= "128.21.33.66"
           PIPELINE_LOAD_BALANCER_PORT=9092
           //PIPELINE_EUREKA_SERVICE_ADDRESS = "http://${PIPELINE_BUILD_IMAGE}-0.eureka:8080/eureka,http://${PIPELINE_BUILD_IMAGE}-1.eureka:8080/eureka"
           PIPELINE_EUREKA_SERVICE_ADDRESS = "http://eureka-0.eureka.cosmos.svc.cluster.local:8080/eureka,http://eureka-1.eureka.cosmos.svc.cluster.local:8080/eureka,http://eureka-2.eureka.cosmos.svc.cluster.local:8080/eureka,http://eureka-3.eureka.cosmos.svc.cluster.local:8080/eureka"
           PIPELINE_IMAGE = "128.21.33.43:5000/${env.PIPELINE_NAME_SPACE}/${env.PIPELINE_BUILD_IMAGE}${env.BUILD_NUMBER}:${env.BUILD_NUMBER}"
           def server = ''
           def name_space = "${env.PIPELINE_NAME_SPACE}"
           def build_image = "${env.PIPELINE_BUILD_IMAGE}"
      }

    stages {
        stage('Checkout') {
            steps {
                script {                 
                   server = getHost()                                   
                }
                 git branch:"api-vega-02" ,credentialsId: 'gitcredentialadmin', url: 'http://128.21.33.43:8888/vega/api-vega'
               
            }
        }
        
      stage('build') {
            steps {
              sh "mvn install:install-file -Dfile=lib/aesencrytion-app.jar -DgroupId=aesencrytion -DartifactId=aesencrytion -Dversion=1.0  -Dpackaging=jar"
              sh "mvn clean package"
           }
      }
           
     stage('dockerized') {
            steps{
                echo "Build Docker images ${env.PIPELINE_BUILD_IMAGE}"
                sh "docker build -t ${env.PIPELINE_BUILD_IMAGE} ."
              
            }
 
      }
    stage('Deploy To Docker Registry') {
            steps {
                echo "Docker Image Tag Name: ${env.PIPELINE_BUILD_IMAGE}"
                sh "docker tag ${env.PIPELINE_BUILD_IMAGE} ${env.PIPELINE_IMAGE}"
                sh "docker push ${env.PIPELINE_IMAGE}"
           }
           

    }
    
    stage("replace Env"){
        steps{
        	sh "printenv"
            echo "replace env"
            sh "ls k8s/"
            sh "envsubst < k8s/deployment.yaml > k8s/deployment${PIPELINE_NAME_SPACE}${PIPELINE_BUILD_IMAGE}.yaml "
            sh "cat k8s/deployment${PIPELINE_NAME_SPACE}${PIPELINE_BUILD_IMAGE}.yaml"            
            
            sh "pwd"
              
        }
    }
    
    stage('deploy to kubernetes') {
            steps {
                sh "ls"
                 script {
                   sshPut remote: server, from: "k8s/deployment${name_space}${build_image}.yaml", into: '.'
                   sshCommand remote: server, command: "kubectl apply -f deployment${name_space}${build_image}.yaml;kubectl -n ${name_space} rollout status deployment.app/${build_image}"
                }
                 sh "docker rmi -f ${env.PIPELINE_IMAGE}"
           }
           

    }
    
}
}
