<h1 align="center">
  <br>
  <a href="https://github.com/zugazagoitia/knag">
    <img src="https://raw.githubusercontent.com/zugazagoitia/knag/main/icons/logo@0.25x.png" alt="Knag logo" width="100">
  </a>
  <br>
  Knag
  <br>
</h1>

<h4 align="center">An Algorithmic Trading SaaS platform for my TFG (Final Degree Project)</h4>

<h3 align="center">
  Vault Service
</h3>

<p align="center">
  <a href="https://github.com/zugazagoitia/knag-vault/actions/workflows/actions.yml?query=branch%3Amain">
    <img src="https://img.shields.io/github/workflow/status/zugazagoitia/knag-vault/build/main?label=build%20%28main%29&logo=githubactions&logoColor=%23FFFFFF" alt="main build badge">
  </a>
  <a href="https://github.com/zugazagoitia/knag-vault/actions/workflows/actions.yml?query=branch%3Adev">
    <img src="https://img.shields.io/github/workflow/status/zugazagoitia/knag-vault/build/dev?label=build%20%28dev%29&logo=githubactions&logoColor=%23FFFFFF" alt="dev build badge">
  </a>
  <a href="https://codecov.io/gh/zugazagoitia/knag-vault">
    <img src="https://codecov.io/gh/zugazagoitia/knag-vault/branch/main/graph/badge.svg?token=o9OGCIfOkE" alt="Code coverage"/>
  </a>
  <a href="https://github.com/zugazagoitia/knag-vault/releases">
    <img alt="GitHub release (latest SemVer)" src="https://img.shields.io/github/v/release/zugazagoitia/knag-vault?sort=semver">
  </a>
</p>

## Requirements

In order to run the service the following software is required:

- A [MariaDB](https://mariadb.org/download/) database

## Usage

Container images are published to the GitHub Packages repository attached to this repository, they are also published under [releases](https://github.com/zugazagoitia/knag-vault/releases).

## Configuration

The following environment variables **need to be populated** in order for the service to work correctly.

| Name                       | Description                                                          |    Constraints |
|:---------------------------|----------------------------------------------------------------------|---------------:|
| SPRING_DATASOURCE_HOST     | A MariaDB host                                                       |              - |
| SPRING_DATASOURCE_PORT     | The MariaDB port                                                     |              - |
| SPRING_DATASOURCE_DATABASE | Database to use                                                      |              - |
| SPRING_DATASOURCE_USERNAME | A MariaDB username authorized on the previous database               |              - |                     
| SPRING_DATASOURCE_PASSWORD | The password for the user                                            |              - |   
| KNAG_KEY_PUB               | Public key used for verifying JWTs                                   |   X.509 Syntax |

## Endpoints

The service is exposed on the port `8080`

There are kubernetes probes exposed on the port `3000`
