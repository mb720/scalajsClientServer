# Change Log
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/) and this project adheres to [Semantic Versioning](http://semver.org/).

## [0.0.2] - 2016-09-11
### Added
- Autowire example for having a type-safe API between client and server
- Title tag for web page

### Changed
- We use the launcher script created by SBT in the client to run the main JavaScript method. This way, we do not have to call the main method of the client directly which is better if we want to refactor the client

## [0.0.1] - 2016-09-04
### Added
- Code for starting a [Spray](http://spray.io/) server that serves a website where the user can list files from the server.
- README.md that explains how to start the server
- LICENSE containing the [BSD 2-Clause License](https://opensource.org/licenses/BSD-2-Clause)

