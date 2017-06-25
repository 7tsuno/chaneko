chaneko
==========
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

chaneko is interactive response system for Slack.

![chaneko](https://github.com/7tsuno/chaneko/blob/image/src/main/resources/image/chanekoLogo.png?raw=true)

Description
==========

1. User writes slack in browser.
1. Slack Outgoing WebHooks calls API of AWS Lambda
1. AWS Lambda implemented by chaneko gets the conversation pattern from the DynamoDB and responds.
1. If you need to do other processing by replying, Chaneko call another Lambda.

![flow](https://github.com/7tsuno/chaneko/blob/image/src/main/resources/image/chanekoFlow.png?raw=true)

Requirement
==========

* Slack
* AWS Lambda
* Dynamo DB
* java8 (or later)

Usage
==========
