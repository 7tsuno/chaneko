Chaneko
==========
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

Chaneko is interactive response system for Slack.

![chaneko](https://github.com/7tsuno/chaneko/blob/image/src/main/resources/image/chanekoLogo.png?raw=true)

Demo
==========
Demo that works with gif.

Description
==========

1. User writes to Slack in browser.
1. Slack Outgoing WebHooks calls API of AWS Lambda.
1. AWS Lambda implemented by Chaneko gets the conversation pattern from the DynamoDB and responds.
1. If you need to do other processing by responds, Chaneko call another Lambda.

![flow](https://github.com/7tsuno/chaneko/blob/image/src/main/resources/image/chanekoFlow.png?raw=true)

Requirement
==========

* Slack
* AWS Lambda
* Dynamo DB
* java8 (or later)

Usage
==========
To be described...
