## How to deploy this sample app on Google Cloud Shell

### Enable the apis
    gcloud services enable \
      cloudapis.googleapis.com \
      container.googleapis.com \
      containerregistry.googleapis.com \
      servicenetworking.googleapis.com \
      sqladmin.googleapis.com \
      vpcaccess.googleapis.com


### Create the k8s cluster (Warning, can take a few minutes)

    gcloud container clusters create kube-java \
      --zone=us-central1-c \
      --enable-ip-alias \
      --create-subnetwork=name=kube-java-springboot \
      --scopes=cloud-platform

### Create the clusterrole binding
    kubectl create clusterrolebinding cluster-admin-binding \
    --clusterrole=cluster-admin \
    --user=$(gcloud config get-value core/account)


### To build the liquibase docker image (run from the liquibase folder) then tag the new build
    cp ~/.m2/repository/mysql/mysql-connector-java/8.0.26/mysql-connector-java-8.0.26.jar .
    docker build -t demo-liquibase .
    docker tag demo-liquibase gcr.io/$PROJECT_ID/demo-liquibase
    docker push gcr.io/$PROJECT_ID/demo-liquibase

### To push it to container registry
    docker push gcr.io/$PROJECT_ID/demo-liquibase
  
###### If the above fails, try running this before retrying:
    gcloud auth login
    gcloud auth configure-docker

### Create the db configmap
    declare dbpw=$(dd bs=24 count=1 if=/dev/urandom status=none | base64 | tr +/ _.)

    cat <<EOF> gcp-springboot-mysql-config.yaml
    apiVersion: v1
    kind: ConfigMap
    metadata:
      name: gcp-springboot-mysql-config
    data:
      HIBERNATE_HBM2DDL_AUTO: update
      HIBERNATE_DIALECT: org.hibernate.dialect.H2Dialect
      SPRING_DATASOURCE_DRIVER_CLASS_NAME: com.mysql.jdbc.Driver
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: $dbpw
    EOF


### Creating an address and VPC connection (pre-req for creating database)
    gcloud compute addresses create gcp-springboot \
    --global \
    --purpose=VPC_PEERING \
    --prefix-length=16 \
    --network=default
####  Warning this one can take a minute
    gcloud services vpc-peerings connect \
    --ranges=gcp-springboot \
    --network=default

### Creating the DB instance
  List of versions: https://cloud.google.com/sql/docs/mysql/admin-api/rest/v1beta4/SqlDatabaseVersion

####  This takes quite a while, around 10 minutes

    declare operation=$(gcloud beta sql instances create gcp-springboot \
    --database-version=MYSQL_5_7 \
    --tier=db-f1-micro \
    --region=us-central1 \
    --root-password=$dbpw \
    --network=default \
    --no-assign-ip \
    --async \
    --format='value(name)')


####  Once that's done, copy the IP into the config file
    declare dbip=$(gcloud sql instances describe gcp-springboot \
    --format='value(ipAddresses.ipAddress)')
    
    cat <<EOF>> ./gcp-springboot-mysql-config.yaml
      SPRING_DATASOURCE_URL: jdbc:mysql://$dbip/book_store?createDatabaseIfNotExist=true
    EOF

### Apply the configmap
    kubectl apply -f ./gcp-springboot-mysql-config.yaml


### Build image and deploy app
    export PROJECT_ID=$(gcloud config list --format 'value(core.project)')
    export APP_NAME=jib-demo

    mvn compile com.google.cloud.tools:jib-maven-plugin:2.8.0:build \
    -Dimage=gcr.io/$PROJECT_ID/jib-demo


    cat <<EOF | kubectl apply -f -
    apiVersion: apps/v1
    kind: Deployment
    metadata:
      name: $APP_NAME
    spec:
      selector:
        matchLabels:
          run: $APP_NAME
      replicas: 1
      template:
        metadata:
          labels:
            run: $APP_NAME
        spec:
          initContainers:
          - name: demo-liquibase
            image: gcr.io/$PROJECT_ID/demo-liquibase
            env:
            - name: SPRING_DATASOURCE_URL
              valueFrom:
                configMapKeyRef:
                  name: gcp-springboot-mysql-config
                  key: SPRING_DATASOURCE_URL
            - name: SPRING_DATASOURCE_USERNAME
              valueFrom:
                configMapKeyRef:
                  name: gcp-springboot-mysql-config
                  key: SPRING_DATASOURCE_USERNAME
            - name: SPRING_DATASOURCE_PASSWORD
              valueFrom:
                configMapKeyRef:
                  name: gcp-springboot-mysql-config
                  key: SPRING_DATASOURCE_PASSWORD
          containers:
          - name: $APP_NAME
            image: gcr.io/$PROJECT_ID/$APP_NAME
            ports:
            - containerPort: 8080
            env:
            - name: HIBERNATE_HBM2DDL_AUTO
              valueFrom:
                configMapKeyRef:
                  name: gcp-springboot-mysql-config
                  key:  HIBERNATE_HBM2DDL_AUTO
            - name: HIBERNATE_DIALECT
              valueFrom:
                configMapKeyRef:
                  name: gcp-springboot-mysql-config
                  key: HIBERNATE_DIALECT
            - name: SPRING_DATASOURCE_DRIVER_CLASS_NAME
              valueFrom:
                configMapKeyRef:
                  name: gcp-springboot-mysql-config
                  key: SPRING_DATASOURCE_DRIVER_CLASS_NAME
            - name: SPRING_DATASOURCE_URL
              valueFrom:
                configMapKeyRef:
                  name: gcp-springboot-mysql-config
                  key: SPRING_DATASOURCE_URL
            - name: SPRING_DATASOURCE_USERNAME
              valueFrom:
                configMapKeyRef:
                  name: gcp-springboot-mysql-config
                  key: SPRING_DATASOURCE_USERNAME
            - name: SPRING_DATASOURCE_PASSWORD
              valueFrom:
                configMapKeyRef:
                  name: gcp-springboot-mysql-config
                  key: SPRING_DATASOURCE_PASSWORD
    EOF


### Create the load balancer to expose it to the public
    cat <<EOF | kubectl apply -f -
    apiVersion: v1
    kind: Service
    metadata:
      name: jib-demo-loadbalancer
    spec:
      type: LoadBalancer
      externalTrafficPolicy: Cluster
      ports:
        - port: 80
          targetPort: 8080
      selector:
        run: jib-demo
    EOF

### Wait for the loudbalancer to have an IP
    until [ -n "$(kubectl get svc jib-demo-loadbalancer \
    -o jsonpath='{.status.loadBalancer.ingress[0].ip}')" ]; do
    sleep 10
    done


### Retrieve the IP and curl to it
    declare ip=$(kubectl get service jib-demo-loadbalancer \
    -o jsonpath="{.status.loadBalancer.ingress[*].ip}")
    
    curl -w "\n" $ip/books

