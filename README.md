# spring-kubernetes

## Before run this application please do the following

Execute the following command from spring-webflux-demo directory

```docker-compose up -d```

Then execute the following command to create necessary tables

```aws dynamodb --endpoint-url http://localhost:4566 create-table --table-name customer-sync --attribute-definitions AttributeName=customerId,AttributeType=S --key-schema AttributeName=customerId,KeyType=HASH --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5```

# Deploy to k8s

## (Section: 01)Basic steps to deploy an application to k8s cluster

1. Create a spring boot application without db.
2. Build docker image of the application.
    - Write Dockerfile script.

    ```
    FROM openjdk:11
    EXPOSE 8080
    ADD target/<jar file name> app.jar
    ENTRYPOINT ["java", "-jar", "/app.jar"]
    ```

    Here,

    The **FROM** layer denotes which parent image to use for our child image.
**EXPOSE** layer expose the port in given port

    **ADD** layer package the application into a .jar file using Maven.

    **ENTRYPOINT** layer tells docker to run inside the container once the previous steps’ve been executed.

   - Make .jar file using Maven command. This jar file’s path will be added to Dockerfile script. For a database-contained application, use this to build a .jar file. a database-contained applications

       ```
       mvn package -Dmaven.test.skip=true
       ```

   - Build docker image with any name and version. ( Not now, After doing Number 6 step)**

       ```
       docker build -t <name>:<tag> .
       ```

3. Make sure, both minikube & kubectl are installed. Check the version by these commands:

    ```
    minikube version
    kubectl version
    ```

4. Now, Start minikube and check the status.

    ```
    minikube start
    minikube status
    ```

   - This should be the output of the status:

    ```
    minikube
    type: Control Plane
    host: Running
    kubelet: Running
    apiserver: Running
    kubeconfig: Configured
    ```

5. Now, we need to give permission to read our local docker repository by using this:

    ```
    eval $(minikube docker-env)
    ```

6. Then go to the project directory. And build the docker image. 

   Check if it is created or not. By firing:

    ```
   docker image
   ```

   **Now, we’ll deploy the image into the k8s cluster.**


7. Create Deployment Object using YAML configuration ( Many ways to create a deployment object, YAML is one of them). By this, k8s takes the image and runs it in k8s pods.

   Create a YAML file (k8s-deployment.yaml) from the project directory. For now, we will use this as a **sample**.

    ```
    apiVersion: apps/v1
    kind: Deployment # Kubernetes resource kind we are creating
    metadata:
      name: spring-boot-k8s # this is the name of that specific deployment. you may use any
    spec:
      selector:
        matchLabels:
          app: spring-boot-k8s
      replicas: 2 
    # Number of replicas that will be created for this deployment
    template:
        metadata:
          labels:
            app: spring-boot-k8s
        spec:
          containers:
            - name: spring-boot-k8s
              image: <image name>:<tag> # Image that will be used to containers in the cluster
    imagePullPolicy: IfNotPresent
              ports:
                - containerPort: 8080 # The port that the container is running on in the cluster
    ```

   For more info, follow this. ( [https://eskala.io/tutorial/how-to-write-yaml-files-for-kubernetes/](https://eskala.io/tutorial/how-to-write-yaml-files-for-kubernetes/) )

   Deploy the yaml file with the following command:

    ```
    kubectl apply -f k8s-deployment.yaml
    ```

   The output should be this:

    ```
    <repo name> created  # repo name is metadata name from yaml
    ```

   Check if the pod is running or not by this:

    ```
    kubectl get pods
    
    # NAME                     READY   STATUS    RESTARTS   AGE
    # spring-boot-k8s-12314s   1/1     Running   0           3s
    # spring-boot-k8s-12491s   1/1     Running   0           3s
    ```
   To check details of pods: 
   ```
   kubectl get pods -o wide
   ```
    ```
    #We can check the logs with this:
    kubectl logs -f <pod name>
    ```

8. Finally, create a service object. By this, k8s expose the application to outside the cluster so that users can access it.

   We’ll create a **Service** by YAML approach. For now, we’ll use this as a **Sample**:

    ```
    apiVersion: v1 # Kubernetes API version
    kind: Service # Kubernetes resource kind we are creating
    metadata: # Metadata of the resource kind we are creating
        name: springboot-k8s-service # this is the name of that specific deployment. you may use any
    spec:
      selector:
        app: spring-boot-k8s
      ports:
        - protocol: "TCP"
          port: 8080 # The port that the service is running on in the cluster
                targetPort: 8080 # The port exposed by the service
        type: NodePort # type of the service.others can be used here like LoadBalancer.
    ```

   To deploy the yaml file, we’ll fire this command:

    ```
    kubectl apply -f k8s-service.yaml
    ```

   Now, check the service.

    ```
    kubectl get services
    
    #NAME                        TYPE           CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE
    springboot-k8s-service       NodePort       10.106.114.161   <none>        8080:30733/TCP   33s
    ```

   Here, 30733 is the port by which we can use our API.

   To get the IP:

    ```
    kubectl get nodes -o wide
    
    #NAME       STATUS   ROLES                  AGE   VERSION   INTERNAL-IP    EXTERNAL-IP   OS-IMAGE             KERNEL-VERSION      CONTAINER-RUNTIME
    #minikube   Ready    control-plane,master   8d    v1.22.3   192.168.49.2   <none>        Ubuntu 20.04.2 LTS   5.11.0-46-generic   docker://20.10.8
    ```

   Internal-ip is our ip address. We can also get this by:

    ```
    minikube ip 
    ```

   So, the url is from this example: **192.168.49.2/30733/{the given endpoint}**

   We successfully deployed our application to kubernetes cluster. One can easily find the component by seeing the k8s dashboard GUI.

    ```
    minikube dashboard 
    ```

## (Section: 02) **For the application contains Database(MySQL):**

First, We will run database instances in different ports to the k8s cluster. Similar steps we need to follow to deploy the database into k8s port.

1. Pull database image from docker hub.

   Here, we will use MySQL.

   Before that, make sure the minikube is started and local repository access permission is given.

    ```
    minikube start 
    eval $(minikube docker-env)
    ```

   It is not needed to pull the image manually rather we can create a deployment object and specify the version of MySQL. k8s will take care to pull this image from the docker hub.

2. Create Deployment Object using YAML configuration ( Many ways to create a deployment object, YAML is one of them). By this, k8s takes the database image and runs it in separate k8s pods.

      From the project directory, create a db-deployment.yaml file and put the necessary configurations into it.

   Sample yaml file for the db deployment:

   ```
       # Define a 'Persistent Voulume Claim'(PVC) for Mysql Storage, dynamically provisioned by cluster
       apiVersion: v1
       kind: PersistentVolumeClaim
       metadata:
         name: mysql-pv-claim# name of PVC essential for identifying the storage data
       labels:
           app: mysql
           tier: database
       spec:
         accessModes:
           - ReadWriteOnce #This specifies the mode of the claim that we are trying to create.
       resources:
           requests:
             storage: 1Gi #This will tell kubernetes about the amount of space we are trying to claim.
       ---
       # Configure 'Deployment' of mysql server
       apiVersion: apps/v1
       kind: Deployment
       metadata:
         name: mysql
         labels:
           app: mysql
           tier: database
       spec:
         selector: # mysql Pod Should contain same labels
               matchLabels:
             app: mysql
             tier: database
         strategy:
           type: Recreate
         template:
           metadata:
             labels:  # Must match 'Service' and 'Deployment' selectors
                       app: mysql
               tier: database
           spec:
             containers:
                     - image: mysql:5.7 # image from docker-hub, use that version what you used as docker  image version
                           args:
                   - "--ignore-db-dir=lost+found" # Workaround for https://github.com/docker-library/mysql/issues/186
                           name: mysql
                 env:
                   - name: MYSQL_ROOT_PASSWORD
                     value: <root password>
                   - name: MYSQL_DATABASE # Setting Database Name from a 'ConfigMap'
                                   value: <datbase name>
                 ports:
                   - containerPort: 3306
                     name: mysql
                 volumeMounts: # Mounting voulume obtained from Persistent Volume Claim
                               - name: mysql-persistent-storage
                     mountPath: /var/lib/mysql#This is the path in the container on which the mounting will take place.
                   volumes:
               - name: mysql-persistent-storage# Obtaining 'vloume' from PVC
                           persistentVolumeClaim:
                   claimName: mysql-pv-claim
       ---
       # Define a 'Service' To Expose mysql to Other Services
       apiVersion: v1
       kind: Service
       metadata:
         name: mysql # DNS name
       labels:
           app: mysql
           tier: database
       spec:
         ports:
           - port: 3306
             targetPort: 3306
         selector: # mysql Pod Should contain same labels
               app: mysql
           tier: database
         clusterIP: None # We Use DNS, Thus ClusterIP is not relevant
    ```

    Deployment kind is the configuration which responsible for pulling MySQL 5.7 from docker hub and set the root password and port. When executing this YAML file, this creates orders table also. Persistent volume claim is responsible for telling Kubernetes to get the storage for running database instance. In service, simply we are telling to Kubernetes the port where MySQL is running.       Now, Deploy the yaml file so that all components get created.

   ```
       kubectl apply -f <yaml file name>
        
       #persistentvolumeclaim/mysql-pv-claim created
       #deployment.apps/mysql created
       #service/mysql created
   ```

    Check this by the following commands if it is running and check logs:

    ```  
       kubectl get deployments
        
       #NAME    READY   UP-TO-DATE   AVAILABLE   AGE
       #mysql   1/1     1            1           2m43s
        
       kubectl get pods
        
       #NAME                     READY   STATUS    RESTARTS   AGE
       #mysql-565c87ff99-m5hjf   1/1     Running   0          2m49s
        
       **To get pod details**
   
       kubectl get pods -o wide
   
       **To get the logs:**
        
       kubectl logs -f <pod name>
    ```
   
    From the yaml, <your given name> table should be created when executing the file. Now, We will go to the mysql pods and check the schema status.
    ```
       kubectl exec -it <pod name> /bin/bash
    ```   

    Now we need to login the mysql.

    ```   
       mysql -h <hostname> -u <username> -p
   ```

    Then check the tables if the “orders” schema ( as named it in the yaml file, you can use any) is created or not by **“show databases;”**
  
   
3. Now, If all goes well perfectly, then our db is successfully deployed to k8s. It can be checked by opening “minikube dashboard”.
4. It’s turn to deploy our spring boot application. We need to change the local database properties to pod configurations.

    ```
       url: jdbc:mysql://${DB_HOST}/${DB_NAME}?useSSL=false
       username: ${DB_USERNAME}
       password: ${DB_PASSWORD}
    ```

      Dynamically pod configurations will be added there from the app-deployment yaml file.

      After those changes, jar file needs to be created again as there are some changes in code base.

    Make .jar file using Maven command. This jar file’s path will be added to Dockerfile script. For a database-contained application, use this to build a .jar file. a database-contained applications

      ```
    mvn package -Dmaven.test.skip=true
    ```

5. Follow 3-6 steps from section 1 and then, 
   Build docker image with any name and tag.
   ```
   docker build -t <name>:<tag> .
   ```

6. Now, the app configured yaml (app-deployment.yaml) file needs to be created from project root directory with database configurations and image name and service object which exposes the port.

```
       apiVersion: apps/v1
       kind: Deployment
       metadata:
         name: springboot-crud-deployment
       spec:
         selector:
           matchLabels:
             app: springboot-k8s-mysql
         replicas: 3
         template:
           metadata:
             labels:
               app: springboot-k8s-mysql
           spec:
             containers:
               - name: springboot-crud-k8s
                 image: springboot-crud-k8s:1.0
                 ports:
                   - containerPort: 8080
                 env: # Setting Enviornmental Variables
                               - name: DB_HOST# Setting Database host address from configMap
                                   value: mysql
                   - name: DB_NAME# Setting Database name from configMap
                                   value: <schema name>
                   - name: DB_USERNAME# Setting Database username from Secret
                                   value: <user>
                   - name: DB_PASSWORD# Setting Database password from Secret
                                   value: <password>
        
       ---
        
       apiVersion: v1 # Kubernetes API version
       kind: Service # Kubernetes resource kind we are creating
       metadata: # Metadata of the resource kind we are creating
           name: springboot-crud-svc
       spec:
         selector:
           app: springboot-k8s-mysql
         ports:
           - protocol: "TCP"
             port: 8080 # The port that the service is running on in the cluster
                   targetPort: 8080 # The port exposed by the service
           type: NodePort # type of the service.
   ```

7. Deploy this yaml file (app-deployment.yaml) by the following command and check the status of the deployment object and pods.

   ```
       kubectl apply -f app-deployment.yaml
       #....created
        
       kubectl get deployment
       #if deployment is success
   
       kubectl get pods
       # checking if all pods are running
   
       kubectl get pods -o wide
       **To get pod details**
   
       kubectl get services 
       #expose the port
   ```

      Now check from the database if the table (from yaml file) is created in the schema by the previous way how we got into the pod’s database.
8.   To run the application directly:
     ```
     minikube service < service name >
      ```
      Add the required endpoints after the url. <br />
      **Alter way:** From service, the port is exposed. And find the IP using:

      ```
      minikube ip
     ```
      Use **https://{minikube ip}/{port}/{required api endpoints from controller}**

      If any changes in YAML file are required then do step 7 again and test.

      If the codebase is changed then we need to make the .jar file then create image and update the YAML file. Finally, deploy and test. The whole process should be re-configured unless a database is changed.


## (Section: 03) DynamoDB config deployment in K8s
    
1. **Deploy db configuration yaml file. (localstack_deployment.yaml)**
        
    First, We will run database instances in different ports to the k8s cluster. Similar steps we need to follow to deploy the database into k8s port. 
        
    Pull database image from docker hub. 
        
    Here, we will use DynamoDB localstack image. 
        
    Before that,make sure the minikube is started and local repository access permission is given.
        
    ```
    minikube start 
    eval $(minikube docker-env)
    ```
        
    It is not needed to pull the image manually rather we can create a deployment object and specify the version of localstack. k8s will take care to pull this image from the docker hub.
        
    Now, Create Deployment Object using YAML configuration. By this, k8s pulls the database image and runs it in separate k8s pods.
        
    The sample yaml file (**localstack_deployment.yml**):
        
    ```
        apiVersion: apps/v1
        kind: Deployment
        metadata:
          name: localstack
        spec:
          selector:
            matchLabels:
              app: localstack
          replicas: 1
          template:
            metadata:
              labels:
                app: localstack
            spec:
              containers:
                - name: localstack
                  image: localstack/localstack:0.8.6
                  ports:
                    - containerPort: 31001
                    - containerPort: 32000
                  env:
                    # with the SERVICES environment variable, you can tell LocalStack
                    # what services to expose on what port
                    - name: SERVICES
                      value: "dynamodb:31001"
                    - name: PORT_WEB_UI
                      value: "32000"
        ---
        apiVersion: v1
        kind: Service
        metadata:
          name: localstack
        spec:
          # selector tells Kubernetes what Deployment this Service
          # belongs to
          selector:
            app: localstack
          ports:
            - port: 32000
              protocol: TCP
              name: ui
              nodePort: 32000
            - port: 31001
              protocol: TCP
              name: dynamodb
              nodePort: 31001
          type: LoadBalancer
          
   ```
        
    Now, Deploy the yaml file so that all components get created. 
        
    ```
        kubectl apply -f <yml file name>
        
        #deployment.apps/localstack created
        #service/localstack created
   ```
        
    Check these by the following commands if it is running or not and check logs:
        
    ```
        kubectl get deployments
        
        #NAME         READY   UP-TO-DATE   AVAILABLE   AGE
        #localstack   1/1     1            1           2m43s
        
        kubectl get pods
        
        #NAME         READY   STATUS    RESTARTS   AGE
        #<pod name>   1/1     Running   0          2m49s
        
        **To get details pods information**
        kubectl get pods -o wide
        NAME                 READY   STATUS    RESTARTS   AGE     IP           NODE       NOMINATED NODE   READINESS GATES
        localstack           1/1     Running   0          3h21m   172.17.0.3   minikube   <none>           <none>

        **To get the logs:**
        kubectl logs -f <pod name>
   ```
        
2. **Deploy the spring boot application by deploying the app configuration yml file. (app-deployment.yml)**
        
    The “**Application properties**” file needs to be updated by the following way.
        
    ```
    aws.dynamodb.endpoint=http://<localstack-pod-IP>:<dynamoDB-port> # Ex: http://172.17.0.4:31001
    aws.region=ap-southeast-1
    aws.access.key=fakeMyKeyId
    aws.secret.key=fakeSecretAccessKey
    ```
        
    Dynamically pod configurations will be added there from the app-deployment yml file. 
        
    After those changes, jar file needs to be created again as there are some changes in code base.
        
    - Make .jar file using Maven command. This jar file’s path will be added to Dockerfile script. For a database-contained application, use this to build a .jar file. a database-contained applications
            
        ```
        mvn package -Dmaven.test.skip=true
        ```
            
    
Follow 3-6 steps from section 1. 
    
- Build docker image with any name and tag.
    
    ```
    docker build -t <name>:<tag> .
    ```
    
    Now, the yml (app-deployment.yml) file needs to be created from project root directory with database configurations and created image name and service object which exposes the port. See the sample here:
    
    ```
    apiVersion: v1 # Kubernetes API version
    kind: Service # Kubernetes resource kind we are creating
    metadata: # Metadata of the resource kind we are creating
      name: spring-webflux-demo
    spec:
      selector:
        app: spring-webflux-demo
      ports:
        - protocol: "TCP"
          port: 8080 # The port that the service is running on in the cluster
          targetPort: 8080 # The port exposed by the service
      type: LoadBalancer # type of the service. LoadBalancer indicates that our service will be external.
    ---
    apiVersion: apps/v1
    kind: Deployment # Kubernetes resource kind we are creating
    metadata:
      name: spring-webflux-demo
    spec:
      selector:
        matchLabels:
          app: spring-webflux-demo
      replicas: 2 # Number of replicas that will be created for this deployment
      template:
        metadata:
          labels:
            app: spring-webflux-demo
        spec:
          containers:
            - name: spring-webflux-demo
              image: spring-webflux-demo:1.0 # Image that will be used to containers in the cluster
              imagePullPolicy: IfNotPresent
              ports:
                - containerPort: 8080
    ```
    
    Now, we are going to deploy the yml file by the following command and check the deployment object, pods and services:
    
    ```
    kubectl apply -f app-deployment.yml
    #spring-without-webflux created
    
    kubectl get deployment
    #if deployment is success
  
    kubectl get pods
    # checking if all pods are running
  
    kubectl get pods -o wide
    #get all details of every pod
  
    kubectl get services 
    #expose the port
    ```
    To run the application directly: 
    ```
    minikube service <serviceName >
  ```
  
    **Alter way:** From service, the port is exposed. And find the IP using:
    
    ```
    minikube ip
    ```
    
    Use **https://{minikube ip}/{port}/{required api endpoints from controller}**

    For accessing database from integrated terminal:
    ```
     kubectl exec -it <ProjectPodName> /bin/bash
   ```
    Now, install aws-cli inside this pod.
    ```
  apt update   
  apt install awscli
    ```
    **Configure aws** <br>
Configure aws with access key, secret key and region. <br>
<br>
  To check table list (IP address can be changed according to your system):
    
  ```  
    aws dynamodb list-tables --endpoint-url http://172.17.0.3:31001
  ```
    
  To create table (IP address can be changed according to your system):
   
  ```
    aws dynamodb --endpoint-url http://172.17.0.3:31001 create-table --table-name Customer --attribute-definitions AttributeName=CustomerID,AttributeType=S --key-schema AttributeName=CustomerID,KeyType=HASH --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5
  ```

  That's all. Now, check from kubernetes dashboard.
