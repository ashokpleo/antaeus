# Philips Running Log 🏃

# Goals
- [x] Dependency Injection
- [x] Hexagonal Arch
- ~~New table for transactions~~
- [x] Diagram
- [x] External Service for scheduling

## Start 03/02/2023 11am
- I'm considering not doing the scheduling in this app. Reasons:
    - I'm almost certain that if this is an example of something I would be working on we would have a dedicated service or cloud infrastructure that will handle the actual scheduling.
    - Eg: Have a scheduled AWS lambda function that hits an API in this app that fetches all unpaid invoices, then publishes to an SNS topic and this application could then attach its own SQS to that Topic.
        - This will give automatic retries, redundancy, ensuring only one instance of this app picks up the message, so scaling is now not an issue.
- I will be creating another docker image that spins up a VM with a cron job to simulate cloud infrastructure.
- I see we don't really have any proper dependency injection in this project yet. I'll see if I can add that, but that's probably a whole other can of worms.

### My Goals after the initial investigation (this will change as I go)
1. Tests
2. Move this project to follow Hexagonal Architecture, the benefits:
    - Encourages separation of concerns through layers of responsibility
    - The technologies can evolve around the core business logic - Easy to change, loosely coupled.
    - The way the application is accessed can be changed very easily and will give code examples.
    - Testable
3. Will be using an "external service" to simulate scheduling using docker and docker-compose.
4. A new database table to hold all transactions for invoices, this can be used for easy tracking and analytics.
5. Drawing up a Diagram


### Nice to Haves
- Dependency injection
- Contract first design, Using something like OpenAPI and yaml files to describe what the APIs will look like. Then on build OpenAPI creates interfaces from those yamls that our APIs implement.
    - Our API documentation is always updated this way.

Finishing
03/02/2023 12 pm

------

## Start 03/02/2023 15:37pm
- I decided that dependency injection was a must-have
    - So I've added google Guice to the project since it's lightweight and seems easy to implement.

- I've done a first pass at dependency injection, next step will be the diagram and cleaning up the interfaces, and moving to Hex Arch.

Finishing 03/02/2023 17:30 pm

----

## Start 04/02/2023 17:00pm

- I'm splitting up the DAL into individual repos to separate each to their responsibilities.
- I've also moved the bootstrap back to the app main level, I might come back to this, it also uses the Services and not the repos.
- So I see that invoices have a state of Paid, but that doesn't make sense for a recurring subscription payment that happens once a month.
    - I will be changing the Invoices states to `Active`, `Cancelled`
    - I'll have a new table for transactions.
    - Maybe I'll keep a transaction state on the invoice, but I'll see when I get there.


- Okay so I've decided not to do the above because it would fundamentally change how these invoices work, Before making a change like this I would want to chat with my colleagues (I'm pretending I'm working at Pleo here) to get more context around why it was built this way
    - However, I still feel that we need an `Active` flag somewhere, So I will be adding that to the Customer object.
    - I don't need a table for transactions anymore if the invoice has the state. So have removed that from the above Goals.
- I've done a first pass at the billing service and added that `Active` flag to the Customer Object, I'll be writing tests shortly
- I still need to clean up the interfaces and move them to where they should be, then add the Docker-compose with the "External Service"

Finishing 04/02/2023 20:11 pm

-----

## Starting 05/02/2023 10:00am

- Thinking about it overnight I'm moving the Active flag back to the invoice but keeping the paid state there as well.
- I've gone ahead and Isolated all the layers both vertically and horizontally. E.g Customers have their own domain so if this ever needs to rip out into its own Microservice it's easy.
    - This is the crucks of Hex Arch.


- Just a Note I decided to make the BillingService granular i.e it takes a single invoice ID at a time. I assumed that we would use some sort of eventing per invoice for scheduling, so retrying is easy and targeted.
- Billing service is up and running.


- External Service is up and running, I've hard-coded the script to just use a range from 1-1000. Ideally, I would get all invoice Ids first then hit the endpoint.
- I think I'm nearing the end here, Will be drawing up my diagram shortly (this should have been done first)

- I have added an active boolean flag to the invoice, there is no way to change it for now. But I do have tests that test it.


- I'm done

finishing 17:00 pm

-----

Total time: 13 hours