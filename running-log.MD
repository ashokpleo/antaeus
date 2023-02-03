# Philips Running Log 🏃

# Goals
- [x] dependency Injection
- Hexagonal Arch
- New table for transactions
- Diagram
- External Service for scheduling 

## Start 03/02/2023 11am
- I'm considering not doing the scheduling in this app. Reasons:
  - I'm almost certain that if this is an example of something I would be working on we would have a dedicated service or cloud infrastructure that will handle the actually scheduling.
  - Eg: Have a scheduled AWS lambda function that hits an API in this app that fetches all unpaid invoices, then publishes to an SNS topic and this application could then attach its own SQS to that Topic.
    - This will give automatic retries, redundancy, ensuing only one instant of this app picks up the message, so scaling is now not an issue.
- I will be creating another docker image that spins up a VM with a cron job to simulate cloud infrastructure.
- I see we dont really have any proper dependency injection in this project yet. I'll see if I can add that, but that's probably whole other can of worms.

### My Goals after initial investigating (this will change as I go)
1. Tests
2. Move this project to follow Hexagonal Architecture, the benefits:
   - Encourages separation of concerns through layers of responsibility
   - The technologies can evolve around the core business logic - Easy to change, loosely coupled.
   - The way the application is accessed can be changed very easily, will give code examples.
   - Testable
3. Will be using an "external service" to simulate scheduling using docker and docker-compose.
4. A new database table to hold all transactions for invoices, this can be used for easy tracking and analytics.
5. Drawing up a Diagram


### Nice to Haves
- Dependency injection
- Contract first design, Using something like OpenAPI and yaml files to discribe what the APIs will look like. Then on build openAPI creates interfaces from those yamls that our APIs implement.
    - Our API documentation is always update this way.

Finishing
 03/02/2023 12pm

------

## Start 03/02/2023 15:37pm
- I decided that dependency injection was a must have
    - So I've added google Guice to the project since its lightweight and seems easy to implement.

- I've done a first pass at dependency injection, next step will be the diagram and the cleaning up the interfaces and moving to Hex Arch.

Finishing 03/02/2023 17:30pm

----