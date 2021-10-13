### To build the jib image image (and skip tests)

    export PROJECT_ID=$(gcloud config list --format 'value(core.project)')
    
    mvn compile com.google.cloud.tools:jib-maven-plugin:2.8.0:build \
    -Dimage=gcr.io/$PROJECT_ID/basic-jib

    docker push gcr.io/$PROJECT_ID/basic-jib

### Create the graal deployment
    export PROJECT_ID=$(gcloud config list --format 'value(core.project)')
    
    cat <<EOF | kubectl apply -f -
    apiVersion: apps/v1
    kind: Deployment
    metadata:
      name: basic-jib
    spec:
      selector:
        matchLabels:
          run: basic-jib
      replicas: 1
      template:
        metadata:
          labels:
            run: basic-jib
        spec:
          containers:
          - name: basic-jib
            image: gcr.io/$PROJECT_ID/basic-jib
            ports:
            - containerPort: 8080
    EOF


### To check the logs for the graal deployment
    kubectl logs -l run=basic-jib

### Note the startup time