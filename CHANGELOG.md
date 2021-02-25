# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.2.5] - 2021-02-25

### Added
- New flag `delegateCancellationsTo` for `FlowApp` to allow configuring cancellation delegations in the flow config
- New flow service event `cancelOrResumeUserInterface`, that is sent to active flow services if the above flag is set and the merchant requests cancellation

## [2.2.4] - 2021-02-24

First release with CHANGELOG, new CI workflows and publication to Github packages.

Note that this release is functionally identical to v2.2.3.