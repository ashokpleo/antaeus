#! /bin/bash

# TODO: PS: 05/02/2022: Ideally we should use the invoice Get endpoint to fetch all invoices.
for i in {1..1000}
do
  curl --location --request POST "http://pleo-antaeus:8000/rest/v1/billing/$i" # >> /var/log/cron.log 2>&1 # Uncomment this to see the logs
done