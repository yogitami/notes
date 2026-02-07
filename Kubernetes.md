# 🚢 Kubernetes

- Kubernetes is an open-source container orchestration platform (manages containers).
- It automates deployment, scaling, and management of containerized applications.
- Helps manage applications composed of multiple containers across different environments.
- Kubernetes does **not** manage application data persistence — backups and replication are our responsibility.
- A system deployed on Kubernetes is called a **cluster**.

 ## 📌 Quick Navigation
- [🌐 Cluster](#-cluster)
- [⚙️ Kubernetes Components](#%EF%B8%8F-kubernetes-components)
- [📁 Namespace](#-namespace)
- [🧱 Node & Pod](#-node--pod)
- [🌍 Service & Ingress](#-service--ingress)
- [🔐 ConfigMap & Secret](#-configmap--secret-external-configuration)
- [💾 Volumes](#-volumes-data-persistence)
- [📦 Deployment & StatefulSet](#-deployment--statefulset)
- [⎈ Helm Charts](#-helm-charts)
- [🔑 RBAC](#-rbac-role-based-access-control)
- [🛡️ Pod Disruption Budgets](#%EF%B8%8F-pod-disruption-budgets-pdb)
- [🏗️ How It All Works Together](#%EF%B8%8F-how-it-all-works-together)
- [🌍 Real-World Analogy](#-real-world-analogy)
- [🔄 Context](#context)
- [🧪 kubectl Commands](#-kubectl-commands)
- [🔄 Request Flow](#-request-flow)
- [🏗️ Architecture](#-architecture)
- [☁️ GCP / GKE](#-gcp--gke)
- [🧰 Tech Stack (onboarding-infra)](#-tech-stack-onboarding-infra)

---

## 🌐 Cluster

A Kubernetes cluster is a set of machines (**nodes**) used to run containerized applications.

### Cluster has two main parts:

#### 🧠 Control Plane Nodes (Master Nodes)
Responsible for managing the state of the cluster.  
In production, it usually runs on multiple nodes across different zones.

Components:
- API Server  
- etcd  
- Scheduler  
- Controller Manager  

Clients communicate with the Control Plane using REST APIs.

#### ⚙️ Worker Nodes
Run containerized application workloads inside **Pods**.

Components:
- **Kubelet** – communicates with Control Plane and container runtime.
- **Container Runtime** – pulls images and runs containers.
- **Kube-proxy** – routes traffic and provides load balancing.

---

### 🔁 Internal Working of Kubernetes Cluster

| Kubernetes Cluster Flow |
|-------------------------|
| <p align="center"> <img width="60%" src="ImagesForDocs/Kubernetes_Flow.png"> </p> |

---

## 💡 Why Kubernetes?

In **microservices architecture**, each service is containerized for easier scaling and management.  
Kubernetes manages and orchestrates these containers together.

---

## ⚠️ Problems Kubernetes Solves

1. High availability (no downtime)
2. Scalability and performance
3. Disaster recovery
4. Self-healing and automatic rollbacks
5. Horizontal scaling
6. Portability (on-prem, cloud, or hybrid)

### Managed Kubernetes Services
- **EKS** (AWS)
- **GKE** (Google)
- **AKS** (Azure)

---

## ⚙️ Kubernetes Components

| Component Diagram | Component Diagram |
|------------------|------------------|
| <img width="100%" src="ImagesForDocs/k8_Components_1.png"> | <img width="100%" src="ImagesForDocs/K8_Components_2.png"> |

---

## 📁 Namespace

- A Namespace is like a virtual folder or workspace inside your Kubernetes cluster that keeps resources organized and separated.
- Keeps different parts of your application separated and organized.
- Kubernetes uses namespaces to organize objects in the cluster.
- If you delete a namespace, pods in that namespace are also deleted.
- PODS run on nodes in namespaces.
- One node hosts pods from multiple namespaces
- When we create pods they are linked to a namespace & Kubernetes distribute them across available nodes.
- Secrets in default namespace aren't accessible from backoffice namespace (by default)
- A service named bootstrap in kafka namespace is different from bootstrap in default namespace.

```
namespace = "default"      # For onboarding apps
namespace = "backoffice"   # For backoffice apps
namespace = "kafka"        # For Kafka services
```

```
kubectl get namespaces
kubectl get pods -n backoffice
kubectl get pods --all-namespaces
kubectl config set-context --current --namespace=default
# Scale onboarding-web in "default" namespace
kubectl scale deployment onboarding-web -n default --replicas=5
```

---

## 🧱 Node & Pod

- Node = physical or virtual machine where Kubernetes runs your applications.
- Pods run inside nodes.
- Pod is an abstraction over containers. A wrapper around one or more containers running your application
- Usually one application per pod.
- Each pod has its own IP address.
- **Example**:
  * Each Java application (from the onboarding repo) runs in pods
  * App and database run in separate pods.

---

## 🌍 Service & Ingress

- A Service is like a permanent phone number or stable address that lets other applications find and communicate with your pods, even though pods are constantly being created, destroyed, and replaced.
- Pods communicate using **Services**.
- A Service provides static IP, DNS name (like bootstrap.kafka:9092), load balancing and service discovery.
- You connect to: **service-name:port**
- Service matches pods by labels.
- Pod and Service lifecycles are independent.
- We have Kafka Service in our onboarding project
```
kubectl get services -n default
kubectl get svc onboarding-web -n default (svc short form for services)
kubectl describe service onboarding-web -n default
kubectl get endpoints onboarding-web -n default
kubectl get pods -n default --show-labels
kubectl get pods -n default -l app=onboarding-web
kubectl get deployment onboarding-web -n default -o yaml | grep -A5 labels
```

### Types of Services:
- Internal Service
- External Service

**Ingress** routes traffic into the cluster and forwards it to services.

---

## 🔐 ConfigMap & Secret (External Configuration)
ConfigMaps and Secrets must be connected to Pods.

### ConfigMap
- Stores non-sensitive configuration (e.g., DB URL).
- Change settings without rebuilding your application
  
### Secret
- Secure storage for sensitive data (passwords, certificates, API keys) (base64 encoded).
- Extensively used for Kafka credentials, certificates, encryption keys.
- Keeps sensitive data out of your application code

---

## 💾 Volumes (Data Persistence)

Used when pods need persistent data (e.g., databases).

Types:
- Local volumes  
- Remote volumes (cloud storage)

Data persists even if the pod restarts.

---

## 📦 Deployment & StatefulSet

### Deployment
- Blueprint for pods
- A Deployment is like a "manager" or "supervisor" that makes sure your application is running correctly with the right number of copies (replicas) and handles updates safely.
- A Deployment tells Kubernetes what to run (which container image) , how many copies to run (replicas), how to update it (rolling updates, rollbacks), what to do if something crashes (restart it)
- Managed through Helm charts

  ```
  DEPLOYMENT (The Manager)
    ↓ Creates and manages
  REPLICASET (The Supervisor)
    ↓ Creates and maintains
  PODS (The Workers)
    ↓ Run
  CONTAINERS (Your Application)
  ```

  ```
  kubectl get pods
  kubectl get deployments -n default
  kubectl get pods -n default | grep onboarding-cs
  kubectl describe deployment onboarding-web -n default (View deployment details)
  kubectl get deployment onboarding-web
  kubectl scale deployment onboarding-web --replicas=5
  kubectl set image deployment/onboarding-web onboarding-web=gcr.io/bux/onboarding-web:2.60.0
  kubectl rollout status deployment/onboarding-web (Watch the rollout)
  kubectl rollout undo deployment/onboarding-web
  # Or rollback to a specific revision
  kubectl rollout history deployment/onboarding-web
  kubectl rollout undo deployment/onboarding-web --to-revision=2
  kubectl delete deployments onboarding-web
  ```

  #### What this does:
   - Terraform calls Helm
   - Helm downloads the onboarding-web chart
   - Chart contains a Deployment template
   - Helm renders the template with your values
   - Kubernetes creates the Deployment
   - Deployment creates ReplicaSet
   - ReplicaSet creates Pods
 
 #### How Service & Deployment works together
 ```
 ┌──────────────────────────────────────────────────┐
 │  DEPLOYMENT: onboarding-web                      │
 │  Manages:                                        │
 │  ├─ Pod 1 (label: app=onboarding-web)            │
 │  ├─ Pod 2 (label: app=onboarding-web)            │
 │  └─ Pod 3 (label: app=onboarding-web)            │
 └──────────────────────────────────────────────────┘
                        ↑
                        │ Finds pods by label
                        │
 ┌──────────────────────────────────────────────────┐
 │  SERVICE: onboarding-web                         │
 │  Selector: app=onboarding-web                    │
 │  Provides: onboarding-web.default:8080           │
 │  Routes traffic to all 3 pods                    │
 └──────────────────────────────────────────────────┘
```

### StatefulSet
- Used for databases and stateful applications

---

## ⎈ Helm Charts
- Like a "package manager" for Kubernetes - pre-configured templates for deploying applications.

```
  resource "helm_release" "app" {
  name       = "onboarding-applicants"
  repository = "https://buxcharts.devops.getbux.com"
  chart      = "onboarding-applicants"
  version    = var.chart_version
  namespace  = "default"
  values     = [
    file("global-apps.yaml"),     # Common settings
    file("onboarding-applicants.yaml")  # App-specific settings
  ]
}
```

---

## 🔑 RBAC (Role-Based Access Control)
- Roles allow specific service accounts to read/write ConfigMaps

---

## 🛡️ Pod Disruption Budgets (PDB)
- Ensures at least some pods stay running during cluster maintenance
---
## 🏗️ How It All Works Together

```
1. Terraform creates the Kubernetes resources
2. Helm deploys your Java applications as Deployments
3. Deployments create Pods (multiple copies for reliability)
4. Services give stable addresses to reach the Pods
5. Secrets inject passwords/certificates into Pods
6. Ingress routes external traffic from the internet to your Services
7. Namespaces keep everything organized
```

---

## 🌍 Real-World Analogy

Think of Kubernetes like a **shipping warehouse**.

| Concept   | Warehouse Analogy              | Kubernetes Meaning                     |
|----------|--------------------------------|----------------------------------------|
| Cluster  | Entire warehouse facility      | The complete Kubernetes system         |
| Node     | Individual shelving units      | Physical or virtual machines           |
| Pod      | Storage boxes on shelves       | Wrapper around one or more containers  |
| Container| Items inside the boxes         | Your application                       |
| Kubelet  | Shelf manager                  | Ensures Pods are running correctly     |
| Service  | Aisle numbers                  | Stable address to access Pods          |


---

## Context
- A context in Kubernetes is like a saved connection profile that tells kubectl Which cluster to connect to (production, development, local?), Which user/credentials to use (your identity), Which namespace to use by default (default, backoffice, kafka?)
- All your contexts are saved in a file called kubeconfig

```
kubectl config get-contexts
kubectl config use-context dev-kafka
kubectl config current-context
kubectl config view
kubectl config set-context --current --namespace=backoffice (change default namespace for current context)
kubectl config set-context my-new-context \
  --cluster=production \
  --user=mittaly-prod \
  --namespace=backoffice
```

---

## 🧪 kubectl Commands

| Command | Description |
|--------|-------------|
| `kubectl -h` | Show help |
| `kubectl get pods` | List pods |
| `kubectl get deployments` | List deployments |
| `kubectl get nodes` | List nodes |
| `kubectl get services` | List services |
| `kubectl logs pod_name` | View pod logs |
| `kubectl describe pod pod_name` | Detailed pod info |
| `kubectl exec -it pod_name -- /bin/bash` | Access pod shell |
| `kubectl config get-contexts` | List contexts |
| `kubectl config current-context` | Show active context |
| `kubectl apply -f config.yaml` | Apply configuration |
| `kubectl describe service service_name` | Debug service |
| `kubectl get pods -o wide` | Detailed pod list |
| `kubectl get all` | Show all resources |
| `kubectl get configmap -o yaml` | View ConfigMap |
| `kubectl get pods -o wide --all-namespaces` | See Which Pods are WHERE and WHAT |

---

## 🔄 Request Flow

    Browser → Ingress → Service → Pod

<p align="center">
  <img src="ImagesForDocs/k8.png" alt="Kubernetes request flow" width="450">
</p>


---

## 🏗️ Architecture

1. Cluster consists of Control Plane and Worker nodes
2. Worker nodes do the actual work
3. Worker node contains:
   - Container runtime
   - Kubelet
4. Kubelet starts Pods
5. Kube-proxy forwards requests from Services to Pods

---

## ⎈ Helm

| Helm |
|------|
| <p align="center"><img width="50%" src="ImagesForDocs/helm.png"> </p>|

Helm simplifies Kubernetes application deployment.

Without Helm:
- Each user must configure all YAML files manually.

With Helm:
- One person creates and packages YAML files into a **Helm chart**.
- Others can reuse the chart easily.

### Features:
1. Package manager for Kubernetes.
2. Packages YAML files into charts.
3. Charts can be stored in public or private repositories.
4. You can:
   - Create your own charts.
   - Download and use existing charts.

### Use Case:
For microservices:
- Deployment and service YAMLs differ only in app name and version.
- Helm allows creating a common template.
- Dynamic values are replaced using external configuration files.
- Easy deployment across multiple environments (dev, test, prod).

---

## ☁️ GCP / GKE

Commands:

    kubectl config current-context
    helm -n backoffice ls

---

## 🧰 Tech Stack (onboarding-infra)

### Kubernetes (K8s)

Used for container orchestration and managing microservices deployment.

### Helm

Packages and deploys Kubernetes applications.

Used to deploy services like:

    onboarding-cs
    broker-accounts-command

### Terraform

Provisions Kubernetes resources and manages infrastructure components.

Example:

    terraform/kubernetes/modules/broker-accounts-command/main.tf

### Google Cloud Platform (GCP)

- GKE for Kubernetes clusters
- Cloud SQL
- IAM and networking via gcloud

### SOPS

Encrypts and manages sensitive Kubernetes secrets.

### Bash Scripts

Scripts such as:

    create.sh
    create-test.sh

Used for:
- Cluster creation
- Resource provisioning
- Service deployment
















