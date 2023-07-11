# Infra

## Create GCP Service Account

IAM & Admin -> Service Accounts -> Select Or Create
Keys -> Add Key -> Key type JSON

## Install terraform

```bash
# Download terraform
# Terraform v1.0.6
# add terraform to path
mv terraform /usr/local/bin
cd infra
terraform init
terraform validate
terraform apply
terraform apply -var="gcp_credentials=<credential-path>" -var="gcp_project_id=<project-id>" -var="mysql_password=<password>" -var='white_ips=[""]' 
terraform destroy -var="gcp_credentials=<credential-path>" -var="gcp_project_id=<project-id>" -var="mysql_password=<password>"

terraform show
terraform import google_sql_database_instance.master projects/{{project}}/instances/{{name}}
cat terraform.tfstate | jq '.outputs.server_ca.value' -r > ./secrets/server_ca.pem
cat terraform.tfstate | jq '.outputs.client_cert.value' -r > ./secrets/client_cert.pem
cat terraform.tfstate | jq '.outputs.client_key.value' -r > ./secrets/client_key.pem
```

## Cloud Run

```txt
https://cloud.google.com/kubernetes-engine/docs/tutorials/hello-app
https://registry.terraform.io/providers/hashicorp/google/latest/docs/resources/cloud_run_service
https://cloud.google.com/run/docs/deploying
https://medium.com/google-cloud/deploying-docker-images-to-cloud-run-using-terraform-ee8ae4ecb72e
```
