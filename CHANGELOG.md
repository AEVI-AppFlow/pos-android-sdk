# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.5.0] - TBD

### Changed
- Updates for compatibility with Android 12 (API level 33)

## [2.4.0] - 2022-09-29

### Changed
- Updated all android/gradle build tools
- Update to Androidx
- Changed to build aar as the main archive output instead of jars
- Fix up most/all dependencies
- Fix up tests
- Fix up javadcos and merged javadocs

## [2.2.8] - 2021-12-09

### Changed
- Updated rxmessenger and config api to latest versions

## [2.2.7] - 2021-11-15

### Changed
- Updated print api and json utils to latest versions hosted on GH repos

## [2.2.6] - 2021-03-04

### Fixed
- Issue with FlowApp equals / hashCode not taking `delegateCancellationsTo` into account

## [2.2.5] - 2021-02-25

### Added
- New flag `delegateCancellationsTo` for `FlowApp` to allow configuring cancellation delegations in the flow config
- New flow service event `cancelOrResumeUserInterface`, that is sent to active flow services if the above flag is set and the merchant requests cancellation

## [2.2.4] - 2021-02-24

First release with CHANGELOG, new CI workflows and publication to Github packages.

Note that this release is functionally identical to v2.2.3.
