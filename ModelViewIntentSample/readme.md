# Model-View-Intent Sample

The goal of this project is to showcase a reactive implementation of [model-view-intent](http://hannesdorfmann.com/android/model-view-intent).

The core of this MVI implementation is an unidirectional data flow where user intents are processed and mapped to view states. The view state, in this example, is an immutable value object representing the different states of one screen.

# Components

In order to make this implementation reusable we extracted a couple of generic components:

A **MVIView** is UI component which exposes a stream of intents, which we call actions in this project to not confuse with android intents, and can render a view state.

The **Middleware** processes these actions using domain specific business rules and emits a stream of changes.

The **Reducer** consumes use-case specific changes from the Middleware and converts them to a view state.

The **Store** is the glue between the above components and is mainly responsible for forwarding events between these.

## MVIView < Action >

In this concrete implementation the MVIView is a wrapper around the Activity which collaborates between multiple custom views. It merges and exposes user actions from all custom view and is capable of rendering a view state, without performing any further logic. 

For example a `SearchInputView` will emit a `SubmitQuery:Action`.

## Middleware <Action, Change>

The Middlewares implement the domain specific business rules. They operate on the user actions and map them to domain specific change events. 

A `SearchMiddleware` would consume an `SubmitQuery:Action` and will perform a asynchronous operation to execute a search and map the result to a `SearchCompleted:Change`.
The idea is to have multiple middlewares for the different aspects of the domain and business requirements (e.g. tracking).

## Reducer<State, Change>

The Reducer consumes the use-case specific changes and maps them to a view-state. This means multiple use-case specific actions (`SearchInProgress, FilterInProgress`) might end up in the same view state indicating progress on the screen.

Following the above example the `SearchReducer` would map `SearchCompleted:Change` to a view state containing the data needed to render search results.

## Store<Action, State, Changes>

The Store is the glue between the above mentioned components. It listens to user actions, forwards them to all middlewares, forwards their changes to the reducer and passes the generated view state back to the view.

# Diagrams
| High-level Diagram|Sequence Diagram|
|----|----|
|![MainView](https://user-images.githubusercontent.com/1046688/61949720-071baa00-afac-11e9-96e1-4e68c5b0844e.png)| ![Untitled](https://user-images.githubusercontent.com/1046688/61949761-20245b00-afac-11e9-94ab-bf51764b6cca.png) |
