## Overview

![architecture-overview](diagrams/architecture.png)

> A high level overview of Faux's architecture.

Faux is composed of the following components:

- An `api` which a game is built upon.
- A `runtime` which runs a game for a specific platform.
- An `editor` which allows editing certain aspect of a game.
- An `asset-processor` which converts a game's assets to a format `editor`
  and `runtime` understands.

## API
Defines a game's structure and how it interacts with the engine, a more detailed explanation can be found [here](api.md).

## Runtime
Implements `api` and responsible for running a game for a specific platform.

## Editor
A pseudo-`runtime` that also implements `api` but allows developers to edit certain aspect of a game and view the changes on the fly. 

## Asset Processor
An on-demand service that transforms raw assets to a format `runtime` and `editor` can efficiently use.

