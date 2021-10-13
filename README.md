###To build the graal image (and skip tests)

    export PROJECT_ID=$(gcloud config list --format 'value(core.project)')
    
    ./mvnw clean spring-boot:build-image -DskipTests=true -Dspring-boot.build-image.imageName=gcr.io/$PROJECT_ID/spring-boot-graal-demo
    
    docker push gcr.io/$PROJECT_ID/spring-boot-graal-demo

###Create the graal deployment
    export PROJECT_ID=$(gcloud config list --format 'value(core.project)')

    cat <<EOF | kubectl apply -f -
    apiVersion: apps/v1
    kind: Deployment
    metadata:
      name: spring-boot-graal-demo
    spec:
      selector:
        matchLabels:
          run: spring-boot-graal-demo
      replicas: 1
      template:
        metadata:
          labels:
            run: spring-boot-graal-demo
        spec:
          containers:
          - name: spring-boot-graal-demo
          image: gcr.io/$PROJECT_ID/spring-boot-graal-demo
          ports:
          - containerPort: 8080
    EOF


###To check the logs for the graal deployment
    kubectl logs -l run=spring-boot-graal-demo

####Compare the startup time to that of the main branch (2-5 seconds typically) 