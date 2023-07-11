variable "gcp_credentials" {
  type        = string
  description = "gcp credentials json file path"
}

variable "gcp_project_id" {
  type        = string
  description = "gcp project id"
}

variable "mysql_password" {
  type        = string
  description = "mysql password for user root"
}

variable "white_ips" {
  type = list(string)
}

terraform {
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "3.84.0"
    }
  }
}

provider "google" {
  credentials = file(var.gcp_credentials)
  project     = var.gcp_project_id
  region      = "us-central1"
  zone        = "us-central1-f"
}

# resource doc: https://registry.terraform.io/providers/hashicorp/google/latest/docs/resources/sql_database_instance
# tier doc: https://cloud.google.com/sql/pricing
resource "google_sql_database_instance" "mysql" {
  name                = "instance"
  database_version    = "MYSQL_5_7"
  region              = "us-central1"
  deletion_protection = false
  settings {
    tier = "db-f1-micro"
    ip_configuration {
      dynamic "authorized_networks" {
        for_each = var.white_ips
        iterator = white_ip

        content {
          name  = "white-ip-${white_ip.key}"
          value = white_ip.value
        }
      }
    }
  }
}

resource "google_sql_user" "root_user" {
  name     = "root"
  instance = google_sql_database_instance.mysql.name
  password = var.mysql_password
}

resource "google_sql_ssl_cert" "client_cert" {
  common_name = "client"
  instance    = google_sql_database_instance.mysql.name
}

output "mysql_ip" {
  value = google_sql_database_instance.mysql.public_ip_address
}

output "server_ca" {
  value = google_sql_ssl_cert.client_cert.server_ca_cert
}

output "client_cert" {
  value = google_sql_ssl_cert.client_cert.cert
}

output "client_key" {
  value     = google_sql_ssl_cert.client_cert.private_key
  sensitive = true
}

